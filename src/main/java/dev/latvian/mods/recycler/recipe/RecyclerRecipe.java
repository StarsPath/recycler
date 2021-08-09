package dev.latvian.mods.recycler.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class RecyclerRecipe implements Recipe<NoInventory> {
	private final ResourceLocation id;
	private final String group;
	public Ingredient ingredient;
	public final List<ItemStack> result;
	public int time;

	public RecyclerRecipe(ResourceLocation i, String g) {
		id = i;
		group = g;
		ingredient = Ingredient.EMPTY;
		result = new ArrayList<>();
		time = 60;
	}

	@Override
	public boolean matches(NoInventory inv, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(NoInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecyclerRecipeSerializers.RECYCLER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return RecyclerRecipeSerializers.RECYCLER_TYPE;
	}
}