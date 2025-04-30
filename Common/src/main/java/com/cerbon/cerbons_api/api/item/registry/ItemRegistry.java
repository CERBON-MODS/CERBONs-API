package com.cerbon.cerbons_api.api.item.registry;

import com.cerbon.cerbons_api.api.item.enums.ToolType;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ItemRegistry {
    private final ResourcefulRegistry<Item> itemRegistry;

    public ItemRegistry(String modId) {
        this.itemRegistry = ResourcefulRegistries.create(BuiltInRegistries.ITEM, modId);
    }

    public Map<ArmorItem.Type, RegistryEntry<ArmorItem>> registerFullArmorSet(Holder<ArmorMaterial> material, int durabilityFactor) {
        return registerFullArmorSet(
                material,
                List.of(durabilityFactor, durabilityFactor, durabilityFactor, durabilityFactor)
        );
    }

    public Map<ArmorItem.Type, RegistryEntry<ArmorItem>> registerFullArmorSet(Holder<ArmorMaterial> material, List<Integer> durabilityFactors) {
        return registerFullArmorSet(material, properties -> properties, durabilityFactors);
    }

    public Map<ArmorItem.Type, RegistryEntry<ArmorItem>> registerFullArmorSet(Holder<ArmorMaterial> material, UnaryOperator<Item.Properties> itemProperties, List<Integer> durabilityFactors) {
        Preconditions.checkArgument(
                durabilityFactors != null && durabilityFactors.size() == 4,
                "Expected durability array of length 4 (helmet, chest, legs, boots)"
        );

        return ImmutableMap.of(
                ArmorItem.Type.HELMET, registerArmor(ArmorItem.Type.HELMET, material, itemProperties, durabilityFactors.get(0)),
                ArmorItem.Type.CHESTPLATE, registerArmor(ArmorItem.Type.CHESTPLATE, material, itemProperties, durabilityFactors.get(1)),
                ArmorItem.Type.LEGGINGS, registerArmor(ArmorItem.Type.LEGGINGS, material, itemProperties, durabilityFactors.get(2)),
                ArmorItem.Type.BOOTS, registerArmor(ArmorItem.Type.BOOTS, material, itemProperties, durabilityFactors.get(3))
        );
    }

    public RegistryEntry<ArmorItem> registerArmor(ArmorItem.Type armorType, Holder<ArmorMaterial> material, int durabilityFactor) {
        return registerArmor(armorType, material, properties -> properties, durabilityFactor);
    }

    public RegistryEntry<ArmorItem> registerArmor(ArmorItem.Type armorType, Holder<ArmorMaterial> material, UnaryOperator<Item.Properties> itemProperties, int durabilityFactor) {
        String materialName = material.unwrapKey().map(resourceKey -> resourceKey.location().getPath()).orElseThrow();
        return registerItem(() -> new ArmorItem(material, armorType, itemProperties.apply(new Item.Properties().durability(armorType.getDurability(durabilityFactor)))), materialName + "_" + armorType.getSerializedName());
    }

    public RegistryEntry<TieredItem> registerSimpleTool(ToolType toolType, Tier tier, float attackDamage, float attackSpeed, String id) {
        return registerSimpleTool(toolType, tier, properties -> properties, attackDamage, attackSpeed, id);
    }

    public RegistryEntry<TieredItem> registerSimpleTool(ToolType toolType, Tier tier, UnaryOperator<Item.Properties> itemProperties, float attackDamage, float attackSpeed, String id) {
        return switch (toolType) {
            case SWORD -> registerItem(() -> new SwordItem(tier, itemProperties.apply(new Item.Properties().attributes(SwordItem.createAttributes(tier, (int) attackDamage, attackSpeed)))), id);
            case PICKAXE -> registerItem(() -> new PickaxeItem(tier, itemProperties.apply(new Item.Properties().attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
            case AXE -> registerItem(() -> new AxeItem(tier, itemProperties.apply(new Item.Properties().attributes(AxeItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
            case SHOVEL -> registerItem(() -> new ShovelItem(tier, itemProperties.apply(new Item.Properties().attributes(ShovelItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
            case HOE -> registerItem(() -> new HoeItem(tier, itemProperties.apply(new Item.Properties().attributes(HoeItem.createAttributes(tier, attackDamage, attackSpeed)))), id);
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
