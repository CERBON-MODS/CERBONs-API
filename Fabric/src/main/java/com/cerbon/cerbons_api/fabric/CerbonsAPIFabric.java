package com.cerbon.cerbons_api.fabric;

import com.cerbon.cerbons_api.CerbonsAPI;
import com.cerbon.cerbons_api.api.network.CommonNetwork;
import com.cerbon.cerbons_api.api.network.data.Side;
import com.cerbon.cerbons_api.fabric.network.FabricNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class CerbonsAPIFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        CerbonsAPI.init();
        new CommonNetwork(new FabricNetworkHandler(Side.SERVER));
    }

    @Override
    public void onInitializeClient() {
        new CommonNetwork(new FabricNetworkHandler(Side.CLIENT));
    }
}