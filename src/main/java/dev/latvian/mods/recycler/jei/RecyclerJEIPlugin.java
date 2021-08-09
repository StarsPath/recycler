package dev.latvian.mods.recycler.jei;

import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.item.RecyclerItems;
import dev.latvian.mods.recycler.recipe.NoInventory;
import dev.latvian.mods.recycler.recipe.RecyclerRecipeSerializers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class RecyclerJEIPlugin implements IModPlugin {
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Recycler.MOD_ID + ":jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r) {
		r.addRecipeCatalyst(new ItemStack(RecyclerItems.RECYCLER.get()), RecyclerCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Minecraft.getInstance().level;
		r.addRecipes(level.getRecipeManager().getRecipesFor(RecyclerRecipeSerializers.RECYCLER_TYPE, NoInventory.INSTANCE, level), RecyclerCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new RecyclerCategory(r.getJeiHelpers().getGuiHelper()));
	}
}