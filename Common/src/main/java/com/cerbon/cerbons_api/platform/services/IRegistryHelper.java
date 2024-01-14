package com.cerbon.cerbons_api.platform.services;

import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import net.minecraft.core.Registry;

public interface IRegistryHelper {

    <T> ResourcefulRegistry<T> create(Registry<T> registry, String id);
}
