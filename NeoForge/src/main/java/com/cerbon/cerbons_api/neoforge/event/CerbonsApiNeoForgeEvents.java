package com.cerbon.cerbons_api.neoforge.event;

import com.cerbon.cerbons_api.api.network.CommonNetwork;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.neoforge.network.NeoForgeNetworkHandler;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CerbonsApiNeoForgeEvents {

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        new CommonNetwork(new NeoForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER));
    }
}
