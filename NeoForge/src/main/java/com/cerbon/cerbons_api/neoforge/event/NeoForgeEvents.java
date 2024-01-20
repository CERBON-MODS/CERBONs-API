package com.cerbon.cerbons_api.neoforge.event;

import com.cerbon.cerbons_api.neoforge.attachment.saved_data.LevelEventScheduler;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeEvents {

    @SubscribeEvent
    public static void onPlayerLogging(ClientPlayerNetworkEvent.LoggingIn event) {
        LevelEventScheduler.INSTANCE = new LevelEventScheduler();
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.getGameTime() % 2 == 0)
            LevelEventScheduler.get(event.level).updateEvents();
    }
}
