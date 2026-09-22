package net.bsmp.plushies.network;

import net.bsmp.plushies.BsmpPlushiesMod;
import net.bsmp.plushies.entity.PlushieEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class ServerNetworking {

	private static final double MAX_DISTANCE_SQ = 8.0 * 8.0;

	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(ModNetworking.SET_KIN, (server, player, handler, buf, responseSender) -> {
			int entityId = buf.readInt();
			String requestedName = buf.readString(64);

			// IMPORTANTE: leer todo del buffer aquí mismo (arriba). El acceso al
			// mundo/entidades se hace después, en el hilo del servidor.
			server.execute(() -> {
				Entity entity = player.getWorld().getEntityById(entityId);
				if (!(entity instanceof PlushieEntity plushie)) {
					return;
				}
				if (plushie.squaredDistanceTo(player) > MAX_DISTANCE_SQ) {
					return; // evita que te "roben" peluches lejanos por paquetes falsos
				}

				String name = requestedName.trim();
				if (name.isEmpty() || name.equalsIgnoreCase("yo") || name.equalsIgnoreCase("me")) {
					plushie.setOwnerPlayer(player);
					player.sendMessage(Text.literal("§a[BSMP Plushies] Ahora este peluche usa tu kin."), true);
					return;
				}

				server.getUserCache().findByName(name).ifPresentOrElse(
						profile -> {
							plushie.setOwnerRaw(profile.getId(), profile.getName());
							player.sendMessage(Text.literal("§a[BSMP Plushies] Kin cambiado a " + profile.getName() + "."), true);
						},
						() -> player.sendMessage(Text.literal("§c[BSMP Plushies] No se encontró a '" + name + "'."), true)
				);
			});
		});

		BsmpPlushiesMod.LOGGER.info("[BSMP Plushies] Networking del servidor registrado.");
	}
}
