package com.cerbon.cerbons_api.platform.services;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import net.minecraft.world.level.Level;

public interface ICapabilityHelper {

    EventScheduler getLevelEventScheduler(Level level);
}
