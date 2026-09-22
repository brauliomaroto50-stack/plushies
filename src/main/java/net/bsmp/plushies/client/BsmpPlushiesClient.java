package net.bsmp.plushies.client;

import net.bsmp.plushies.entity.ModEntities;
import net.bsmp.plushies.client.render.PlushieModel;
import net.bsmp.plushies.client.render.PlushieRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class BsmpPlushiesClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(PlushieModel.LAYER, PlushieModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.PLUSHIE, PlushieRenderer::new);
	}
}
