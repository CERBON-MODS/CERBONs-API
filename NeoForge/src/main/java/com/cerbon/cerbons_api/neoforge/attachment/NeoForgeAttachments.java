package com.cerbon.cerbons_api.neoforge.attachment;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class NeoForgeAttachments {
    public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final Supplier<AttachmentType<EventScheduler>> EVENT_SCHEDULER = REGISTRY.register(
            "event_scheduler",
            () -> AttachmentType.builder(EventScheduler::new).build()
    );

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
