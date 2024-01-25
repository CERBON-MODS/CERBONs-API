package com.cerbon.cerbons_api.fabric.cardinalComponents;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface ILevelEventSchedulerComponent extends ComponentV3 {

    EventScheduler get();
}
