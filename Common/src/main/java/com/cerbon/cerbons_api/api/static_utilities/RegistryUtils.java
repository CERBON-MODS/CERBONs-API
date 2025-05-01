package com.cerbon.cerbons_api.api.static_utilities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

public class RegistryUtils {

    public static Block getBlockByKey(String key) {
        return BuiltInRegistries.BLOCK.getValue(ResourceLocation.tryParse(key));
    }

    public static Item getItemByKey(String key) {
        return BuiltInRegistries.ITEM.getValue(ResourceLocation.tryParse(key));
    }

    public static @NotNull String getItemKeyAsString(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    public static MobEffect getMobEffectByKey(String key) {
        return BuiltInRegistries.MOB_EFFECT.getValue(ResourceLocation.tryParse(key));
    }

    public static EntityType<?> getEntityTypeByKey(String key) {
        return BuiltInRegistries.ENTITY_TYPE.getValue(ResourceLocation.tryParse(key));
    }

    public static Structure getStructureByKey(String key, ServerLevel serverLevel) {
        return serverLevel.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(ResourceLocation.tryParse(key));
    }
}
