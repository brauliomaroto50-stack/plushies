package net.bsmp.plushies;

import net.bsmp.plushies.entity.ModEntities;
import net.bsmp.plushies.item.ModItems;
import net.bsmp.plushies.network.ServerNetworking;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BsmpPlushiesMod implements ModInitializer {

	public static final String MOD_ID = "bsmpplushies";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[BSMP Plushies] Inicializando mod...");
		ModEntities.register();
		ModItems.register();
		ServerNetworking.register();
	}
}
