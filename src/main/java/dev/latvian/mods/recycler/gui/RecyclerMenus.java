package dev.latvian.mods.recycler.gui;

import dev.latvian.mods.recycler.Recycler;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerMenus {
	public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, Recycler.MOD_ID);

	public static final RegistryObject<ContainerType<RecyclerMenu>> RECYCLER = REGISTRY.register("recycler", () -> new ContainerType<>((IContainerFactory<RecyclerMenu>) RecyclerMenu::new));
}
