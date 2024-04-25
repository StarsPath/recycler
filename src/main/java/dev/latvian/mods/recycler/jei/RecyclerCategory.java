package dev.latvian.mods.recycler.jei;


import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.item.RecyclerItems;
import dev.latvian.mods.recycler.recipe.RecyclerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public class RecyclerCategory implements IRecipeCategory<RecyclerRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(Recycler.MOD_ID + ":recycler");

	private final IDrawable background;
	private final IDrawable icon;

	public RecyclerCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(new ResourceLocation(Recycler.MOD_ID + ":textures/gui/recycler_recipe.png"), 0, 0, 117, 18).setTextureSize(128, 32).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(RecyclerItems.RECYCLER.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends RecyclerRecipe> getRecipeClass() {
		return RecyclerRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.get("block.recycler.recycler");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(RecyclerRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(Arrays.asList(recipe.ingredient.getItems())));
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.result);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, RecyclerRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = layout.getItemStacks();
		IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();

		itemStacks.init(0, true, 0, 0);

		for (int i = 0; i < recipe.result.size(); i++) {
			itemStacks.init(i + 1, false, 39 + i * 20, 0);
		}

		itemStacks.set(ingredients);
		fluidStacks.set(ingredients);
	}
}
