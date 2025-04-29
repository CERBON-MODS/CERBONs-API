package com.cerbon.cerbons_api.api.registry.custom;

import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.stream.Stream;

public class ItemRegistry {
    private final ResourcefulRegistry<Item> itemRegistry;

    public ItemRegistry(String modId) {
        this.itemRegistry = ResourcefulRegistries.create(BuiltInRegistries.ITEM, modId);
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
