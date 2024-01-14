package com.cerbon.cerbons_api.platform;

import com.cerbon.cerbons_api.platform.services.ICapabilityHelper;
import com.cerbon.cerbons_api.platform.services.IPlatformHelper;
import com.cerbon.cerbons_api.platform.services.IRegistryHelper;
import com.cerbon.cerbons_api.util.Constants;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper PLATFORM_REGISTRY = load(IRegistryHelper.class);
    public static final ICapabilityHelper PLATFORM_CAPABILITY = load(ICapabilityHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
