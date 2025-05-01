package com.cerbon.cerbons_api.api.item.registry;

import com.cerbon.cerbons_api.api.item.enums.ToolType;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ItemRegistry {
    private final ResourcefulRegistry<Item> itemRegistry;

    public ItemRegistry(String modId) {
        this.itemRegistry = ResourcefulRegistries.create(BuiltInRegistries.ITEM, modId);
    }

    public Map<ArmorType, RegistryEntry<Item>> registerFullArmorSet(ArmorMaterial material) {
        return registerFullArmorSet(material, properties -> properties);
    }

    public Map<ArmorType, RegistryEntry<Item>> registerFullArmorSet(ArmorMaterial material, UnaryOperator<Item.Properties> itemProperties) {
        return ImmutableMap.of(
                ArmorType.HELMET, registerArmor(ArmorType.HELMET, material, itemProperties),
                ArmorType.CHESTPLATE, registerArmor(ArmorType.CHESTPLATE, material, itemProperties),
                ArmorType.LEGGINGS, registerArmor(ArmorType.LEGGINGS, material, itemProperties),
                ArmorType.BOOTS, registerArmor(ArmorType.BOOTS, material, itemProperties)
        );
    }

    public RegistryEntry<Item> registerArmor(ArmorType armorType, ArmorMaterial material) {
        return registerArmor(armorType, material, properties -> properties);
    }

    public RegistryEntry<Item> registerArmor(ArmorType armorType, ArmorMaterial material, UnaryOperator<Item.Properties> itemProperties) {
        String materialName = material.assetId().location().getPath();
        return registerItem(itemProperties.apply(new Item.Properties().humanoidArmor(material, armorType)), materialName + "_" + armorType.getSerializedName());
    }

    public RegistryEntry<Item> registerSimpleTool(ToolType toolType, ToolMaterial toolMaterial, float attackDamage, float attackSpeed, String id) {
        return registerSimpleTool(toolType, toolMaterial, properties -> properties, attackDamage, attackSpeed, id);
    }

    public RegistryEntry<Item> registerSimpleTool(ToolType toolType, ToolMaterial toolMaterial, UnaryOperator<Item.Properties> itemProperties, float attackDamage, float attackSpeed, String id) {
        return switch (toolType) {
            case SWORD -> registerItem(itemProperties.apply(new Item.Properties().sword(toolMaterial, attackDamage, attackSpeed)), id);
            case PICKAXE -> registerItem(itemProperties.apply(new Item.Properties().pickaxe(toolMaterial, attackDamage, attackSpeed)), id);
            case AXE -> registerItem(itemProperties.apply(new Item.Properties().axe(toolMaterial, attackDamage, attackSpeed)), id);
            case SHOVEL -> registerItem(itemProperties.apply(new Item.Properties().shovel(toolMaterial, attackDamage, attackSpeed)), id);
            case HOE -> registerItem(itemProperties.apply(new Item.Properties().hoe(toolMaterial, attackDamage, attackSpeed)), id);
        };
    }

    public RegistryEntry<Item> registerFood(FoodProperties foodProperties, String id) {
        return registerFood(foodProperties, properties -> properties, id);
    }

    public RegistryEntry<Item> registerFood(FoodProperties foodProperties, UnaryOperator<Item.Properties> itemProperties, String id) {
        return registerItem(itemProperties.apply(new Item.Properties().food(foodProperties)), id);
    }

    public RegistryEntry<BlockItem> registerBlockItem(Supplier<Block> block, String id) {
        return registerBlockItem(block, new Item.Properties(), id);
    }

    public RegistryEntry<BlockItem> registerBlockItem(Supplier<Block> block, Item.Properties itemProperties, String id) {
        return registerBlockItem(id, () -> new BlockItem(block.get(), itemProperties));
    }

    public RegistryEntry<BlockItem> registerBlockItem(String id, Supplier<BlockItem> blockItem) {
        return registerItem(blockItem, id);
    }

    public RegistryEntry<Item> registerItem(String id) {
        return registerItem(new Item.Properties(), id);
    }

    public RegistryEntry<Item> registerItem(Item.Properties itemProperties, String id) {
        return registerItem(() -> new Item(itemProperties), id);
    }

    public <T extends Item> RegistryEntry<T> registerItem(Supplier<T> item, String id) {
        return itemRegistry.register(id, item);
    }

    public Collection<RegistryEntry<Item>> getEntries() { return itemRegistry.getEntries(); }
    public Stream<RegistryEntry<Item>> stream() { return itemRegistry.stream(); }
    public Stream<Item> boundStream() { return itemRegistry.boundStream(); }

    public void register() {
        itemRegistry.register();
    }
}
