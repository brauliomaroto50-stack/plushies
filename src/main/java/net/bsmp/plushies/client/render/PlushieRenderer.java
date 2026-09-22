package net.bsmp.plushies.client.render;

import net.bsmp.plushies.entity.PlushieEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class PlushieRenderer extends MobEntityRenderer<PlushieEntity, PlushieModel> {

	// Qué tan "chiquito" es el peluche respecto a un jugador normal. 0.4 = 40%.
	private static final float PLUSHIE_SCALE = 0.4f;

	public PlushieRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new PlushieModel(ctx.getPart(PlushieModel.LAYER)), 0.25f);
	}

	@Override
	public Identifier getTexture(PlushieEntity entity) {
		UUID ownerUuid = entity.getOwnerUuid().orElse(null);
		if (ownerUuid == null) {
			// Sin dueño asignado: skin por defecto (Steve)
			return DefaultSkinHelper.getTexture(new UUID(0L, 0L));
		}

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.getNetworkHandler() != null) {
			PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(ownerUuid);
			if (entry != null) {
				// Usa la skin (kin) real del jugador conectado
				// (En 1.20.1 este método se llama getSkinTexture(); si tu IDE marca error
				// aquí, usa Ctrl+Click sobre PlayerListEntry para confirmar el nombre exacto
				// según tu versión de Yarn — cambió entre snapshots.)
				return entry.getSkinTexture();
			}
		}

		// Jugador no está en la lista de tablist (p.ej. offline/desconectado):
		// cae en la skin por defecto calculada a partir del UUID
		return DefaultSkinHelper.getTexture(ownerUuid);
	}

	@Override
	protected void scale(PlushieEntity entity, MatrixStack matrices, float amount) {
		matrices.scale(PLUSHIE_SCALE, PLUSHIE_SCALE, PLUSHIE_SCALE);
	}
}
