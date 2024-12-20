package com.cerbon.cerbons_api.neoforge.event;

import com.cerbon.cerbons_api.neoforge.attachment.saved_data.LevelEventScheduler;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeEvents {

    @SubscribeEvent
    public static void onPlayerLogging(ClientPlayerNetworkEvent.LoggingIn event) {
        LevelEventScheduler.INSTANCE = new LevelEventScheduler();
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        LevelEventScheduler.get(event.getLevel()).updateEvents();
    }
}
