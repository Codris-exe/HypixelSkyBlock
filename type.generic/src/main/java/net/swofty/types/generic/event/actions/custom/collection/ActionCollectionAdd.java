package net.swofty.types.generic.event.actions.custom.collection;

import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointCollection;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.CollectionUpdateEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.user.SkyBlockActionBar;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;

public class ActionCollectionAdd implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CustomBlockBreakEvent event) {
        if (event.getPlayerPlaced()) return;

        SkyBlockPlayer player = event.getPlayer();
        ItemType type = ItemType.fromMaterial(event.getMaterial());

        if (type == null) return;
        int oldAmount = player.getCollection().get(type);
        player.getCollection().increase(type);

        SkyBlockEventHandler.callSkyBlockEvent(new CollectionUpdateEvent(player, type, oldAmount));

        player.getDataHandler().get(DataHandler.Data.COLLECTION, DatapointCollection.class).setValue(
                player.getCollection()
        );

        if (player.isCoop()) {
            CoopDatabase.Coop coop = player.getCoop();

            coop.getOnlineMembers().forEach(member -> {
                if (member.getUuid().equals(player.getUuid())) return;
                SkyBlockEventHandler.callSkyBlockEvent(new CollectionUpdateEvent(member, type, oldAmount));
            });

            coop.members().removeIf(
                    uuid -> SkyBlockGenericLoader.getFromUUID(uuid) != null
            );

            ProxyPlayerSet proxyPlayerSet = new ProxyPlayerSet(coop.members());
            proxyPlayerSet.asProxyPlayers().forEach(proxyPlayer -> {
                if (!proxyPlayer.isOnline().join()) return;

                proxyPlayer.runEvent(new CollectionUpdateEvent(null, type, oldAmount));
            });
        }

        CollectionCategory category = CollectionCategories.getCategory(type);
        if (category == null) return;
        CollectionCategory.ItemCollection collection = category.getCollection(type);

        MathUtility.delay(() -> {
            SkyBlockActionBar bar = SkyBlockActionBar.getFor(player);
            int startingPriority = 5;
            int addedAmount = 1;

            SkyBlockActionBar.DisplayReplacement existingReplacement = bar.getReplacement(SkyBlockActionBar.BarSection.DEFENSE);
            if (existingReplacement != null) {
                startingPriority = existingReplacement.priority() + 1;
                try {
                    addedAmount = Integer.parseInt(existingReplacement.display().substring(2, existingReplacement.display().indexOf(" "))) + 1;
                } catch (NumberFormatException ignored) {}
            }
            if (player.getCollection().getReward(collection) != null) {
                bar.addReplacement(
                        SkyBlockActionBar.BarSection.DEFENSE,
                        new SkyBlockActionBar.DisplayReplacement(
                                "§2+" + addedAmount + " " + type.getDisplayName() +
                                        " §7(" + StringUtility.commaify(player.getCollection().get(type)) +
                                        "/" +
                                        StringUtility.shortenNumber(player.getCollection().getReward(collection).requirement()) + ")",
                                20,
                                startingPriority
                        )
                );
            } else { //if Collection is maxed
                bar.addReplacement(
                        SkyBlockActionBar.BarSection.DEFENSE,
                        new SkyBlockActionBar.DisplayReplacement(
                                "§2+" + addedAmount + " " + type.getDisplayName() +
                                        " §7(" + StringUtility.commaify(player.getCollection().get(type)) + ")",
                                20,
                                startingPriority
                        )
                );
            }
        }, 5);
    }
}
