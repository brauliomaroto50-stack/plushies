package net.bsmp.plushies.client.gui;

import net.bsmp.plushies.network.ModNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

/**
 * Menú simple: escribes el nombre del jugador cuyo kin (skin) quieres que
 * use el peluche, o pulsas "Usar mi propia skin".
 */
public class KinSelectScreen extends Screen {

	private final int plushieEntityId;
	private final String initialName;
	private TextFieldWidget nameField;

	public KinSelectScreen(int plushieEntityId, String initialName) {
		super(Text.literal("BSMP Plushies - Elegir kin"));
		this.plushieEntityId = plushieEntityId;
		this.initialName = initialName == null ? "" : initialName;
	}

	@Override
	protected void init() {
		super.init();
		int centerX = this.width / 2;
		int fieldY = this.height / 2 - 10;

		this.nameField = new TextFieldWidget(this.textRenderer, centerX - 100, fieldY, 200, 20,
				Text.literal("Nombre del jugador"));
		this.nameField.setMaxLength(32);
		this.nameField.setText(this.initialName);
		this.addDrawableChild(this.nameField);
		this.setInitialFocus(this.nameField);

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Aplicar kin"), button -> {
					sendKin(this.nameField.getText());
					this.close();
				})
				.dimensions(centerX - 100, fieldY + 28, 96, 20)
				.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Usar mi skin"), button -> {
					sendKin("yo");
					this.close();
				})
				.dimensions(centerX + 4, fieldY + 28, 96, 20)
				.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancelar"), button -> this.close())
				.dimensions(centerX - 50, fieldY + 56, 100, 20)
				.build());
	}

	private void sendKin(String name) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(this.plushieEntityId);
		buf.writeString(name == null ? "" : name);
		ClientPlayNetworking.send(ModNetworking.SET_KIN, buf);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 34, 0xFFFFFF);
		context.drawCenteredTextWithShadow(this.textRenderer,
				Text.literal("Escribe un nombre de jugador y aplica su kin"),
				this.width / 2, this.height / 2 - 22, 0xAAAAAA);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
