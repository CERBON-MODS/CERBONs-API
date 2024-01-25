package com.cerbon.cerbons_api.forge.platform;

import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.cerbon.cerbons_api.forge.registry.ForgeResourcefulRegistry;
import com.cerbon.cerbons_api.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;

public class ForgeRegistryHelper implements IRegistryHelper {

    @Override
    public <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new ForgeResourcefulRegistry<>(registry, id);
    }
}
