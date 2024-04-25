package dev.latvian.mods.recycler.block.entity;

import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.block.RecyclerBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerBlockEntities {
	public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Recycler.MOD_ID);

	public static final RegistryObject<TileEntityType<RecyclerEntity>> RECYCLER = REGISTRY.register("recycler", () -> TileEntityType.Builder.of(RecyclerEntity::new, RecyclerBlocks.RECYCLER.get(), RecyclerBlocks.ADVANCED_RECYCLER.get()).build(null));
}