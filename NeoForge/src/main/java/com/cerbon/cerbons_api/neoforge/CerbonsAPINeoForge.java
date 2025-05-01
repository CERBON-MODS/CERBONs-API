package com.cerbon.cerbons_api.neoforge;

import com.cerbon.cerbons_api.CerbonsAPI;
import com.cerbon.cerbons_api.api.network.CommonNetwork;
import com.cerbon.cerbons_api.api.network.PacketRegistrationHandler;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.neoforge.attachment.NeoForgeAttachments;
import com.cerbon.cerbons_api.neoforge.network.NeoForgeNetworkHandler;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
public class CerbonsAPINeoForge {
    private final PacketRegistrationHandler handler;

    public CerbonsAPINeoForge(IEventBus modEventBus) {
        CerbonsAPI.init();

        NeoForgeAttachments.register(modEventBus);

        modEventBus.addListener(this::commonSetupEvent);
        handler = new NeoForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER);
        modEventBus.register(handler);
    }

    public void commonSetupEvent(FMLCommonSetupEvent event) {
        new CommonNetwork(handler);
    }
}
