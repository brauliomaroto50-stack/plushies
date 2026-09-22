package net.bsmp.plushies.client.render;

import net.bsmp.plushies.BsmpPlushiesMod;
import net.bsmp.plushies.entity.PlushieEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * Modelo humanoide simplificado que reutiliza EXACTAMENTE las mismas cajas y
 * coordenadas UV que la skin estándar de un jugador (64x64), para que la
 * textura del "kin" se vea correcta. El aspecto de "peluche chiquito" se
 * logra escalando el modelo completo hacia abajo en el Renderer, en vez de
 * deformar las cajas (así no se rompe el mapeo UV de la skin).
 */
public class PlushieModel extends EntityModel<PlushieEntity> {

	public static final EntityModelLayer LAYER =
			new EntityModelLayer(new Identifier(BsmpPlushiesMod.MOD_ID, "plushie"), "main");

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public PlushieModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		root.addChild("head", ModelPartBuilder.create()
						.uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f),
				ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		root.addChild("body", ModelPartBuilder.create()
						.uv(16, 16).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f),
				ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		root.addChild("right_arm", ModelPartBuilder.create()
						.uv(40, 16).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f),
				ModelTransform.pivot(-5.0f, 2.0f, 0.0f));

		root.addChild("left_arm", ModelPartBuilder.create()
						.uv(40, 16).mirrored().cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f),
				ModelTransform.pivot(5.0f, 2.0f, 0.0f));

		root.addChild("right_leg", ModelPartBuilder.create()
						.uv(0, 16).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f),
				ModelTransform.pivot(-1.9f, 12.0f, 0.0f));

		root.addChild("left_leg", ModelPartBuilder.create()
						.uv(0, 16).mirrored().cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f),
				ModelTransform.pivot(1.9f, 12.0f, 0.0f));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(PlushieEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * ((float) Math.PI / 180F);
		this.head.pitch = headPitch * ((float) Math.PI / 180F);

		this.rightArm.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.0f * limbDistance * 0.5f;
		this.leftArm.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.0f * limbDistance * 0.5f;
		this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance;
		this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.4f * limbDistance;

		// leve "bamboleo" de peluche al caminar/idle
		this.body.pitch = 0.0f;
	}

	@Override
	public void render(net.minecraft.client.util.math.MatrixStack matrices, net.minecraft.client.render.VertexConsumer vertices,
						int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		leftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		leftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
