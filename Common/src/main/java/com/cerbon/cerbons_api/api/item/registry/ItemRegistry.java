package com.cerbon.cerbons_api.api.item.registry;

import com.cerbon.cerbons_api.api.item.enums.ToolType;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ItemRegistry {
    private final ResourcefulRegistry<Item> itemRegistry;

    public ItemRegistry(String modId) {
        this.itemRegistry = ResourcefulRegistries.create(BuiltInRegistries.ITEM, modId);
    }

    public Map<ArmorItem.Type, RegistryEntry<ArmorItem>> registerFullArmorSet(Holder<ArmorMaterial> material, int... durabilityFactors) {
        return registerFullArmorSet(material, properties -> properties, durabilityFactors);
    }

    public Map<ArmorItem.Type, RegistryEntry<ArmorItem>> registerFullArmorSet(Holder<ArmorMaterial> material, UnaryOperator<Item.Properties> itemProperties, int... durabilityFactors) {
        Preconditions.checkArgument(
                durabilityFactors != null && durabilityFactors.length == 4,
                "Expected durability array of length 4 (helmet, chest, legs, boots)"
        );

        return ImmutableMap.of(
                ArmorItem.Type.HELMET, registerArmor(ArmorItem.Type.HELMET, material, itemProperties, durabilityFactors[0]),
                ArmorItem.Type.CHESTPLATE, registerArmor(ArmorItem.Type.CHESTPLATE, material, itemProperties, durabilityFactors[1]),
                ArmorItem.Type.LEGGINGS, registerArmor(ArmorItem.Type.LEGGINGS, material, itemProperties, durabilityFactors[2]),
                ArmorItem.Type.BOOTS, registerArmor(ArmorItem.Type.BOOTS, material, itemProperties, durabilityFactors[3])
        );
    }

    public RegistryEntry<ArmorItem> registerArmor(ArmorItem.Type armorType, Holder<ArmorMaterial> material, int durabilityFactor) {
        return registerArmor(armorType, material, properties -> properties, durabilityFactor);
    }

    public RegistryEntry<ArmorItem> registerArmor(ArmorItem.Type armorType, Holder<ArmorMaterial> material, UnaryOperator<Item.Properties> itemProperties, int durabilityFactor) {
        String materialName = material.unwrapKey().map(resourceKey -> resourceKey.location().getPath()).orElse("[unregistered]");
        return registerItem(new ArmorItem(material, armorType, itemProperties.apply(new Item.Properties().durability(armorType.getDurability(durabilityFactor)))), materialName + "_" + armorType.getSerializedName());
    }

    public RegistryEntry<TieredItem> registerTool(ToolType toolType, Tier tier, float attackDamage, float attackSpeed, String id) {
        return registerTool(toolType, tier, properties -> properties, attackDamage, attackSpeed, id);
    }

    public RegistryEntry<TieredItem> registerTool(ToolType toolType, Tier tier, UnaryOperator<Item.Properties> itemProperties, float attackDamage, float attackSpeed, String id) {
        return switch (toolType) {
            case SWORD -> registerItem(new SwordItem(tier, itemProperties.apply(new Item.Properties().attributes(SwordItem.createAttributes(tier, (int) attackDamage, attackSpeed)))), id);
            case PICKAXE -> registerItem(new PickaxeItem(tier, itemProperties.apply(new Item.Properties().attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
            case AXE -> registerItem(new AxeItem(tier, itemProperties.apply(new Item.Properties().attributes(AxeItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
            case SHOVEL -> registerItem(new ShovelItem(tier, itemProperties.apply(new Item.Properties().attributes(ShovelItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
            case HOE -> registerItem(new HoeItem(tier, itemProperties.apply(new Item.Properties().attributes(HoeItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
        };
    }

    public RegistryEntry<BlockItem> registerBlockItem(Block block) {
        return registerBlockItem(block, new Item.Properties());
    }

    public RegistryEntry<BlockItem> registerBlockItem(Block block, Item.Properties itemProperties) {
        return registerBlockItem(new BlockItem(block, itemProperties));
    }

    public RegistryEntry<BlockItem> registerBlockItem(BlockItem blockItem) {
        return registerItem(blockItem, BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).getPath());
    }

    public RegistryEntry<Item> registerItem(String id) {
        return registerItem(new Item.Properties(), id);
    }

    public RegistryEntry<Item> registerItem(Item.Properties itemProperties, String id) {
        return registerItem(new Item(itemProperties), id);
    }

    public <T extends Item> RegistryEntry<T> registerItem(T item, String id) {
        return itemRegistry.register(id, () -> item);
    }

    public Collection<RegistryEntry<Item>> getEntries() { return itemRegistry.getEntries(); }
    public Stream<RegistryEntry<Item>> stream() { return itemRegistry.stream(); }
    public Stream<Item> boundStream() { return itemRegistry.boundStream(); }

    public void register() {
        itemRegistry.register();
    }
}
