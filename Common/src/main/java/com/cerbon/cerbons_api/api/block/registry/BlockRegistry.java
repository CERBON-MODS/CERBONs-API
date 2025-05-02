package com.cerbon.cerbons_api.api.block.registry;

import com.cerbon.cerbons_api.api.item.registry.ItemRegistry;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistries;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.google.common.base.Suppliers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockRegistry {
    private final String modId;

    private final ResourcefulRegistry<Block> blockRegistry;
    private final Supplier<ItemRegistry> itemRegistry;

    private boolean registerItemRegistry = false;

    public BlockRegistry(String modId) {
        this(modId, Suppliers.memoize(() -> new ItemRegistry(modId)));
        this.registerItemRegistry = true;
    }

    public BlockRegistry(String modId, Supplier<ItemRegistry> itemRegistry) {
        this.modId = modId;
        this.blockRegistry = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, modId);
        this.itemRegistry = itemRegistry;
    }

    public RegistryEntry<Block> registerBlockWithItem(String id) {
        return registerBlockWithItem(BlockBehaviour.Properties.of(), id);
    }

    public RegistryEntry<Block> registerBlockWithItem(BlockBehaviour.Properties blockProperties, String id) {
        return registerBlockWithItem(blockProperties, new Item.Properties(), id);
    }

    public RegistryEntry<Block> registerBlockWithItem(BlockBehaviour.Properties blockProperties, Item.Properties itemProperties, String id) {
        Supplier<Block> block = () -> new Block(blockProperties.setId(makeId(id)));
        RegistryEntry<Block> blockEntry = registerBlock(block, id);
        itemRegistry.get().registerBlockItem(blockEntry, itemProperties, id);
        return blockEntry;
    }

    public RegistryEntry<Block> registerBlockWithItem(Supplier<Block> block, Item.Properties itemProperties, String id) {
        RegistryEntry<Block> blockEntry = registerBlock(block, id);
        itemRegistry.get().registerBlockItem(blockEntry, itemProperties, id);
        return blockEntry;
    }

    public RegistryEntry<Block> registerBlockWithItem(Supplier<Block> block, String id) {
        RegistryEntry<Block> blockEntry = registerBlock(block, id);
        itemRegistry.get().registerBlockItem(blockEntry, id);
        return blockEntry;
    }

    public RegistryEntry<Block> registerBlock(String id) {
        return registerBlock(BlockBehaviour.Properties.of(), id);
    }

    public RegistryEntry<Block> registerBlock(Block.Properties blockProperties, String id) {
        return registerBlock(() -> new Block(blockProperties.setId(makeId(id))), id);
    }

    public <T extends Block> RegistryEntry<T> registerBlock(Supplier<T> block, String id) {
        return blockRegistry.register(id, block);
    }

    public ResourceKey<Block> makeId(String id) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, id));
    }

    public Collection<RegistryEntry<Block>> getEntries() { return blockRegistry.getEntries(); }
    public Stream<RegistryEntry<Block>> stream() { return blockRegistry.stream(); }
    public Stream<Block> boundStream() { return blockRegistry.boundStream(); }

    public void register() {
        blockRegistry.register();

        if (registerItemRegistry)
            itemRegistry.get().register();
    }
}
