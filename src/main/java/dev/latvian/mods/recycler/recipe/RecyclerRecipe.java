package dev.latvian.mods.recycler.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class RecyclerRecipe implements IRecipe<NoInventory> {
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
	public boolean matches(NoInventory inv, World world) {
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
	public IRecipeSerializer<?> getSerializer() {
		return RecyclerRecipeSerializers.RECYCLER.get();
	}

	@Override
	public IRecipeType<?> getType() {
		return RecyclerRecipeSerializers.RECYCLER_TYPE;
	}
}