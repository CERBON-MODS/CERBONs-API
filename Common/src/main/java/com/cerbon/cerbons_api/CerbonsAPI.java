package com.cerbon.cerbons_api;

import com.cerbon.cerbons_api.packet.CerbonsApiPacketHandler;

public class CerbonsAPI {

	public static void init() {
		new CerbonsApiPacketHandler().register();
	}
}
