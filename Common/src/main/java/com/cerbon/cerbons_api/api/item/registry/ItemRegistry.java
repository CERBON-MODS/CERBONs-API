package com.cerbon.cerbons_api.api.item.registry;

import com.cerbon.cerbons_api.api.item.enums.ToolType;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ItemRegistry {
    private final ResourcefulRegistry<Item> itemRegistry;

    public ItemRegistry(String modId) {
        this.itemRegistry = ResourcefulRegistries.create(BuiltInRegistries.ITEM, modId);
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
