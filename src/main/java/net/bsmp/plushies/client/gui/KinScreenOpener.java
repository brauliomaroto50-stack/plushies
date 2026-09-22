package net.bsmp.plushies.client.gui;

import net.minecraft.client.MinecraftClient;

/**
 * Punto de entrada client-only. PlushieEntity (código común) solo llama a
 * KinScreenOpener.open(...) dentro de un "if (world.isClient)", así que esta
 * clase (y MinecraftClient/Screen) nunca se cargan en el servidor dedicado.
 */
public class KinScreenOpener {
	public static void open(int plushieEntityId, String currentName) {
		MinecraftClient.getInstance().setScreen(new KinSelectScreen(plushieEntityId, currentName));
	}
}
