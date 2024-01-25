package com.cerbon.cerbons_api.api.registry;

import com.cerbon.cerbons_api.platform.Services;
import net.minecraft.core.Registry;

public class ResourcefulRegistries {

    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return Services.PLATFORM_REGISTRY.create(registry, id);
    }
}
