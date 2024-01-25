package com.cerbon.cerbons_api.neoforge.platform;

import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.cerbon.cerbons_api.neoforge.registry.NeoForgeResourcefulRegistry;
import com.cerbon.cerbons_api.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    @Override
    public <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new NeoForgeResourcefulRegistry<>(registry, id);
    }
}
