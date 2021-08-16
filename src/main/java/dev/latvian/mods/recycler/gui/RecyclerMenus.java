package dev.latvian.mods.recycler.gui;

import dev.latvian.mods.recycler.Recycler;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, Recycler.MOD_ID);

	public static final RegistryObject<MenuType<RecyclerMenu>> RECYCLER = REGISTRY.register("recycler", () -> new MenuType<>((IContainerFactory<RecyclerMenu>) RecyclerMenu::new));
}
