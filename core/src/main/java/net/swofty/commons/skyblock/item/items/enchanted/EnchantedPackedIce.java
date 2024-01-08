package net.swofty.commons.skyblock.item.items.enchanted;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.impl.Enchanted;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;

public class EnchantedPackedIce implements Enchanted {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.PACKED_ICE;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.MINING;
    }
}