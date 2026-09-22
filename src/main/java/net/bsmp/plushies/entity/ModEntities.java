package net.bsmp.plushies.entity;

import net.bsmp.plushies.BsmpPlushiesMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

	// Tamaño chiquito, tipo peluche: bastante más bajo/estrecho que un jugador (0.6 x 1.8)
	public static final EntityType<PlushieEntity> PLUSHIE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(BsmpPlushiesMod.MOD_ID, "plushie"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PlushieEntity::new)
					.dimensions(EntityDimensions.fixed(0.45f, 0.55f))
					.build()
	);

	public static void register() {
		FabricDefaultAttributeRegistry.register(PLUSHIE, PlushieEntity.createAttributes());
		BsmpPlushiesMod.LOGGER.info("[BSMP Plushies] Entidad 'plushie' registrada.");
	}
}
