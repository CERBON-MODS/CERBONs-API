package com.cerbon.cerbons_api.fabric.platform;

import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.cerbon.cerbons_api.fabric.registry.FabricResourcefulRegistry;
import com.cerbon.cerbons_api.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new FabricResourcefulRegistry<>(registry, id);
    }
}
