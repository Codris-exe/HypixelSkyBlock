package net.swofty.type.hub.villagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;

public class VillagerPlumberJoe extends SkyBlockVillagerNPC {
    public VillagerPlumberJoe() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§fPlumber Joe", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(56, 70, -78, 180, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.TOOLSMITH;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }
}
