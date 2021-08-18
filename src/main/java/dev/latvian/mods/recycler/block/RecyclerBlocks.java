package dev.latvian.mods.recycler.block;

import dev.latvian.mods.recycler.Recycler;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Recycler.MOD_ID);

	public static final RegistryObject<Block> RECYCLER = REGISTRY.register("recycler", () -> new RecyclerBlock(false));
	public static final RegistryObject<Block> ADVANCED_RECYCLER = REGISTRY.register("advanced_recycler", () -> new RecyclerBlock(true));
}
