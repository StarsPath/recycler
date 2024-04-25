package dev.latvian.mods.recycler.recipe;

import dev.latvian.mods.recycler.Recycler;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerRecipeSerializers {
	public static final DeferredRegister<IRecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Recycler.MOD_ID);

	public static final RegistryObject<IRecipeSerializer<?>> RECYCLER = REGISTRY.register("recycler", RecyclerRecipeSerializer::new);
	public static final IRecipeType<RecyclerRecipe> RECYCLER_TYPE = IRecipeType.register(Recycler.MOD_ID + ":recycler");
}