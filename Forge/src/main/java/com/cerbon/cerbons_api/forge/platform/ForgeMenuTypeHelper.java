package com.cerbon.cerbons_api.forge.platform;

import com.cerbon.cerbons_api.platform.services.IMenuTypeHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeMenuTypeHelper implements IMenuTypeHelper {

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerSimpleMenuType(SimpleMenuSupplier<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    public <T extends AbstractContainerMenu, D> MenuType<T> registerExtendedMenuType(ExtendedMenuSupplier<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> extraDataFabric) {
        return IForgeMenuType.create((windowId, playerInv, extraData) -> factory.create(windowId, playerInv, null, extraData));
    }

    @Override
    public <D> void openExtendedMenu(Player player, MenuProvider menuProvider, Function<ServerPlayer, D> extraDataFabric, Consumer<FriendlyByteBuf> extraDataForge) {
        if (extraDataForge == null)
            throw new IllegalArgumentException("extraDataForge must not be null for Forge environment");

        ((ServerPlayer) player).openMenu(menuProvider, extraDataForge);
    }
}
