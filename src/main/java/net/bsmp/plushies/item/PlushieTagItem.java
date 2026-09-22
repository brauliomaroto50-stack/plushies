package net.bsmp.plushies.item;

import net.bsmp.plushies.entity.ModEntities;
import net.bsmp.plushies.entity.PlushieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Al usarlo (clic derecho en el suelo), invoca un peluche pequeño con la
 * skin (kin) y el nombre del jugador que lo usó.
 */
public class PlushieTagItem extends Item {

	public PlushieTagItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos().up();

		if (!world.isClient && context.getPlayer() != null) {
			PlushieEntity plushie = ModEntities.PLUSHIE.create(world);
			if (plushie != null) {
				plushie.refreshPositionAndAngles(
						pos.getX() + 0.5,
						pos.getY(),
						pos.getZ() + 0.5,
						0.0f, 0.0f
				);
				plushie.setOwnerPlayer(context.getPlayer());
				world.spawnEntity(plushie);
				world.playSound(null, pos, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.NEUTRAL, 0.8f, 1.2f);

				if (!context.getPlayer().getAbilities().creativeMode) {
					context.getStack().decrement(1);
				}
			}
		}
		return ActionResult.success(world.isClient);
	}
}
