package com.cerbon.cerbons_api.forge;

import com.cerbon.cerbons_api.CerbonsAPI;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.forge.network.ForgeNetworkHandler;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
public class CerbonsAPIForge {
    private static ForgeNetworkHandler handler;

    public static ForgeNetworkHandler getHandler() {
        return handler;
    }

    public CerbonsAPIForge() {
        CerbonsAPI.init();
        MinecraftForge.EVENT_BUS.register(this);
        handler = new ForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER);
        FMLJavaModLoadingContext.get().getModEventBus().register(handler);
    }
}