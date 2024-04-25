package dev.latvian.mods.recycler.item;

import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.block.RecyclerBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class RecyclerItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Recycler.MOD_ID);

	public static RegistryObject<BlockItem> blockItem(String id, Supplier<Block> sup) {
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
	}

	public static final RegistryObject<BlockItem> RECYCLER = blockItem("recycler", RecyclerBlocks.RECYCLER);
	public static final RegistryObject<BlockItem> ADVANCED_RECYCLER = blockItem("advanced_recycler", RecyclerBlocks.ADVANCED_RECYCLER);
}