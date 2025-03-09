package com.cerbon.cerbons_api.api.static_utilities;

import com.cerbon.cerbons_api.platform.Services;
import com.cerbon.cerbons_api.platform.services.IMenuTypeHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Consumer;
import java.util.function.Function;

public class MenuTypeUtils {

    public static <T extends AbstractContainerMenu> MenuType<T> registerSimpleMenuType(IMenuTypeHelper.SimpleMenuSupplier<T> factory) {
        return Services.PLATFORM_MENU_TYPE.registerSimpleMenuType(factory);
    }

    public static <T extends AbstractContainerMenu, D> MenuType<T> registerExtendedMenuType(IMenuTypeHelper.ExtendedMenuSupplier<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> extraDataFabric) {
        return Services.PLATFORM_MENU_TYPE.registerExtendedMenuType(factory, extraDataFabric);
    }

    public static <D> void openExtendedMenu(Player player, MenuProvider menuProvider, Function<ServerPlayer, D> extraDataFabric, Consumer<FriendlyByteBuf> extraDataForge) {
        Services.PLATFORM_MENU_TYPE.openExtendedMenu(player, menuProvider, extraDataFabric, extraDataForge);
    }
}
