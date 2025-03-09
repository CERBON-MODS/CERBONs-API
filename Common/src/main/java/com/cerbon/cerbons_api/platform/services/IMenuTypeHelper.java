package com.cerbon.cerbons_api.platform.services;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IMenuTypeHelper {

    <T extends AbstractContainerMenu> MenuType<T> registerSimpleMenuType(SimpleMenuSupplier<T> factory);
    <T extends AbstractContainerMenu, D> MenuType<T> registerExtendedMenuType(ExtendedMenuSupplier<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> extraDataFabric);

    <D> void openExtendedMenu(Player player, MenuProvider menuProvider, Function<ServerPlayer, D> extraDataFabric, Consumer<FriendlyByteBuf> extraDataForge);

    @FunctionalInterface
    interface SimpleMenuSupplier<T extends AbstractContainerMenu> {
        T create(int windowId, Inventory playerInv);
    }

    @FunctionalInterface
    interface ExtendedMenuSupplier<T extends AbstractContainerMenu, D> {
        T create(int windowId, Inventory playerInv, @Nullable D extraDataFabric, @Nullable FriendlyByteBuf extraDataForge);
    }
}
