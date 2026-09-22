package net.bsmp.plushies.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

/**
 * Entidad "peluche" pequeña. Puede llevar el UUID de un jugador para que,
 * del lado del cliente, el renderer use la skin (kin) de ese jugador como
 * textura, y el nombre del jugador se muestre como nametag.
 */
public class PlushieEntity extends PathAwareEntity {

	private static final TrackedData<Optional<UUID>> OWNER_UUID =
			DataTracker.registerData(PlushieEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

	public PlushieEntity(EntityType<? extends PlushieEntity> entityType, World world) {
		super(entityType, world);
		// Los peluches no deberían asustarse ni sufrir la caída como un mob normal
		this.setStepHeight(0.6f);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return PathAwareEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new WanderAroundFarGoal(this, 0.6D));
		this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(3, new LookAroundGoal(this));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
	}

	/** Asigna qué jugador "es" este peluche (para skin + nombre). */
	public void setOwnerPlayer(PlayerEntity player) {
		setOwnerRaw(player.getUuid(), player.getGameProfile().getName());
	}

	/** Igual que setOwnerPlayer pero a partir de UUID/nombre ya resueltos (usado por el menú/red). */
	public void setOwnerRaw(UUID uuid, String name) {
		this.dataTracker.set(OWNER_UUID, Optional.of(uuid));
		this.setCustomName(Text.literal(name));
		this.setCustomNameVisible(true);
	}

	/**
	 * Clic derecho sin correa en la mano -> abre el menú para elegir el kin.
	 * Si el jugador tiene una correa (lead), se deja el comportamiento normal (atar).
	 */
	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isOf(Items.LEAD)) {
			return super.interactMob(player, hand);
		}

		if (this.getWorld().isClient) {
			// Clase client-only; solo se toca dentro de este bloque, nunca en el servidor.
			String currentName = this.getCustomName() != null ? this.getCustomName().getString() : "";
			net.bsmp.plushies.client.gui.KinScreenOpener.open(this.getId(), currentName);
		}
		return ActionResult.success(this.getWorld().isClient);
	}

	public Optional<UUID> getOwnerUuid() {
		return this.dataTracker.get(OWNER_UUID);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.getOwnerUuid().ifPresent(uuid -> nbt.putUuid("OwnerKinUuid", uuid));
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.containsUuid("OwnerKinUuid")) {
			this.dataTracker.set(OWNER_UUID, Optional.of(nbt.getUuid("OwnerKinUuid")));
		}
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return true;
	}

	public static SpawnGroup spawnGroup() {
		return SpawnGroup.CREATURE;
	}
}
