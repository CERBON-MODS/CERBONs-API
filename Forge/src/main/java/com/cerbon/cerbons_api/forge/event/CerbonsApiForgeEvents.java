package com.cerbon.cerbons_api.forge.event;

import com.cerbon.cerbons_api.api.network.CommonNetwork;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.forge.network.ForgeNetworkHandler;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CerbonsApiForgeEvents {

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        new CommonNetwork(new ForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER));
    }
}
