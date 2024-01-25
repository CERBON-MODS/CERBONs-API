package com.cerbon.cerbons_api.forge.platform;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.forge.capability.LevelEventSchedulerProvider;
import com.cerbon.cerbons_api.platform.services.ICapabilityHelper;
import net.minecraft.world.level.Level;

public class ForgeCapabilityHelper implements ICapabilityHelper {

    @Override
    public EventScheduler getLevelEventScheduler(Level level) {
        return level.getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).resolve().orElseThrow();
    }
}
