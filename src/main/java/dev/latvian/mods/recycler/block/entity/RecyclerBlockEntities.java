package dev.latvian.mods.recycler.block.entity;

import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.block.RecyclerBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Recycler.MOD_ID);

	public static final RegistryObject<BlockEntityType<RecyclerEntity>> RECYCLER = REGISTRY.register("recycler", () -> BlockEntityType.Builder.of(RecyclerEntity::new, RecyclerBlocks.RECYCLER.get()).build(null));
}