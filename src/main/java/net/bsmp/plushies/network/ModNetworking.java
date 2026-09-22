package net.bsmp.plushies.network;

import net.bsmp.plushies.BsmpPlushiesMod;
import net.minecraft.util.Identifier;

public class ModNetworking {
	/** Cliente -> Servidor: "quiero que este peluche (entityId) use el kin de <name>". */
	public static final Identifier SET_KIN = new Identifier(BsmpPlushiesMod.MOD_ID, "set_kin");
}
