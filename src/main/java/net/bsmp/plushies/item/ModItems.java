package net.bsmp.plushies.item;

import net.bsmp.plushies.BsmpPlushiesMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

	public static final Item PLUSHIE_TAG = new PlushieTagItem(
			new Item.Settings().maxCount(16)
	);

	public static final RegistryKey<ItemGroup> BSMP_GROUP_KEY = RegistryKey.of(
			RegistryKeys.ITEM_GROUP, new Identifier(BsmpPlushiesMod.MOD_ID, "bsmp_plushies_group")
	);

	public static void register() {
		Registry.register(Registries.ITEM, new Identifier(BsmpPlushiesMod.MOD_ID, "plushie_tag"), PLUSHIE_TAG);

		Registry.register(Registries.ITEM_GROUP, BSMP_GROUP_KEY, FabricItemGroup.builder()
				.displayName(Text.literal("BSMP Plushies"))
				.icon(() -> new ItemStack(PLUSHIE_TAG))
				.entries((displayContext, entries) -> entries.add(PLUSHIE_TAG))
				.build());

		BsmpPlushiesMod.LOGGER.info("[BSMP Plushies] Items registrados.");
	}
}
