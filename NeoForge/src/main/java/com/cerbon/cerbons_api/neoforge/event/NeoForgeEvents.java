package com.cerbon.cerbons_api.neoforge.event;

import com.cerbon.cerbons_api.neoforge.attachment.NeoForgeAttachments;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeEvents {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        event.getLevel().getData(NeoForgeAttachments.EVENT_SCHEDULER).updateEvents();
    }
}
