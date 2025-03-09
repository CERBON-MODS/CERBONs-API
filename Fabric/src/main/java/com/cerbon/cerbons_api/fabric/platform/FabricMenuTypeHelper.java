package com.cerbon.cerbons_api.fabric.platform;

import com.cerbon.cerbons_api.platform.services.IMenuTypeHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class FabricMenuTypeHelper implements IMenuTypeHelper {

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerSimpleMenuType(SimpleMenuSupplier<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    public <T extends AbstractContainerMenu, D> MenuType<T> registerExtendedMenuType(ExtendedMenuSupplier<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> extraDataFabric) {
        if (extraDataFabric == null)
            throw new IllegalArgumentException("extraDataFabric must not be null for Fabric environment");

        return new ExtendedScreenHandlerType<>((windowId, playerInv, extraData) -> factory.create(windowId, playerInv, extraData, null), extraDataFabric);
    }

    @Override
    public <D> void openExtendedMenu(Player player, MenuProvider menuProvider, Function<ServerPlayer, D> extraDataFabric, Consumer<FriendlyByteBuf> extraDataForge) {
        player.openMenu(new ExtendedScreenHandlerFactory<>() {

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
                return menuProvider.createMenu(windowId, playerInv, player);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            public D getScreenOpeningData(ServerPlayer serverPlayer) {
                return extraDataFabric.apply(serverPlayer);
            }
        });
    }
}
