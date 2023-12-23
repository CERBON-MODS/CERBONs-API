package com.cerbon.cerbons_api.event;

import com.cerbon.cerbons_api.CerbonsApi;
import com.cerbon.cerbons_api.capability.providers.LevelEventSchedulerProvider;
import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CerbonsApi.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesLevel(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() == null || event.getObject().getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).isPresent()) return;
        event.addCapability(new ResourceLocation(CerbonsApi.MOD_ID, "event_scheduler"), new LevelEventSchedulerProvider());
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.getGameTime() % 2 == 0)
            event.level.getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).ifPresent(EventScheduler::updateEvents);
    }
}
