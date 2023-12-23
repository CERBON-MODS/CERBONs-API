package com.cerbon.cerbons_api.event;

import com.cerbon.cerbons_api.CerbonsApi;
import com.cerbon.cerbons_api.packet.CerbonsApiPacketHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CerbonsApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CerbonsApiCommonEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(CerbonsApiPacketHandler::register);
    }
}
