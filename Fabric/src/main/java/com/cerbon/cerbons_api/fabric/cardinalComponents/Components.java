package com.cerbon.cerbons_api.fabric.cardinalComponents;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.util.Constants;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class Components implements WorldComponentInitializer {
    private static final ComponentKey<ILevelEventSchedulerComponent> eventSchedulerComponentKey = ComponentRegistryV3.INSTANCE.getOrCreate(
            new ResourceLocation(Constants.MOD_ID, "event_scheduler"),
            ILevelEventSchedulerComponent.class
    );

    public static EventScheduler getLevelEventScheduler(Level level) {
        return eventSchedulerComponentKey.get(level).get();
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(eventSchedulerComponentKey, LevelEventScheduler.class, LevelEventScheduler::new);
    }
}
