package dev.latvian.mods.recycler.recipe;

import dev.latvian.mods.recycler.Recycler;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class RecyclerRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Recycler.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> RECYCLER = REGISTRY.register("recycler", RecyclerRecipeSerializer::new);
	public static final RecipeType<RecyclerRecipe> RECYCLER_TYPE = RecipeType.register(Recycler.MOD_ID + ":recycler");
}