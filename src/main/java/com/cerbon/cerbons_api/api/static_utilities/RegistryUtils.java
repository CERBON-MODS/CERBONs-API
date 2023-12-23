package com.cerbon.cerbons_api.api.static_utilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class RegistryUtils {

    public static Block getBlockByKey(String key) {
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(key));
    }

    public static Item getItemByKey(String key) {
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(key));
    }

    public static @Nullable String getItemKeyAsString(Item item) {
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
        return itemKey != null ? itemKey.toString() : null;
    }

    public static MobEffect getMobEffectByKey(String key) {
        return ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(key));
    }
}
