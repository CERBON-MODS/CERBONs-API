package com.cerbon.cerbons_api.fabric.cardinalComponents;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class Components implements WorldComponentInitializer {
    private static final ComponentKey<ILevelEventSchedulerComponent> eventSchedulerComponentKey = ComponentRegistryV3.INSTANCE.getOrCreate(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "event_scheduler"),
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
