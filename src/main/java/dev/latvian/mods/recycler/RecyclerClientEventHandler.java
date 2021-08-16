package dev.latvian.mods.recycler;

import dev.latvian.mods.recycler.gui.RecyclerMenus;
import dev.latvian.mods.recycler.gui.RecyclerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Recycler.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecyclerClientEventHandler {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(RecyclerMenus.RECYCLER.get(), RecyclerScreen::new);
	}
}
