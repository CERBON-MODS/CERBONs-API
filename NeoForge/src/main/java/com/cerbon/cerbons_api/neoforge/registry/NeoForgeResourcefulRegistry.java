package com.cerbon.cerbons_api.neoforge.registry;

import com.cerbon.cerbons_api.api.registry.RegistryEntries;
import com.cerbon.cerbons_api.api.registry.RegistryEntry;
import com.cerbon.cerbons_api.api.registry.ResourcefulRegistry;
import com.cerbon.cerbons_api.neoforge.CerbonsAPINeoForge;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class NeoForgeResourcefulRegistry<T> implements ResourcefulRegistry<T> {
    private final DeferredRegister<T> register;
    private final RegistryEntries<T> entries = new RegistryEntries<>();

    public NeoForgeResourcefulRegistry(Registry<T> registry, String id) {
        this.register = DeferredRegister.create(registry.key(), id);
    }

    @Override
    public <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(new NeoForgeRegistryEntry<>(register.register(id, supplier)));
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void register() {
        register.register(CerbonsAPINeoForge.getModEventBus());
    }
}
