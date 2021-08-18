package dev.latvian.mods.recycler;

import dev.latvian.mods.recycler.block.RecyclerBlocks;
import dev.latvian.mods.recycler.block.entity.RecyclerBlockEntities;
import dev.latvian.mods.recycler.gui.RecyclerMenus;
import dev.latvian.mods.recycler.item.RecyclerItems;
import dev.latvian.mods.recycler.recipe.RecyclerRecipeSerializers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author LatvianModder
 */
@Mod(Recycler.MOD_ID)
@Mod.EventBusSubscriber(modid = Recycler.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Recycler {
	public static final String MOD_ID = "recycler";

	public Recycler() {
		RecyclerBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		RecyclerItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		RecyclerBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		RecyclerRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		RecyclerMenus.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}