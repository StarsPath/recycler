package dev.latvian.mods.recycler.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RecyclerRecipeStore extends MapData {
	public static final String KEY = "recycler_recipes";
	public final Map<Item, RecyclerRecipe> recipeMap;

	@Nullable
	public static RecyclerRecipe getRecipe(World level, Item item) {
		if (item == Items.AIR) {
			return null;
		}

		RecyclerRecipeStore store = (RecyclerRecipeStore) level.getMapData(KEY);

		if (store == null) {
			store = new RecyclerRecipeStore(level);
			level.setMapData(store);
		}

		return store.recipeMap.get(item);
	}

	public RecyclerRecipeStore(World l) {
		super(KEY);
		recipeMap = new HashMap<>();

		for (RecyclerRecipe recyclerRecipe : l.getRecipeManager().getRecipesFor(RecyclerRecipeSerializers.RECYCLER_TYPE, NoInventory.INSTANCE, l)) {
			for (ItemStack item : recyclerRecipe.ingredient.getItems()) {
				recipeMap.put(item.getItem(), recyclerRecipe);
			}
		}
	}
}
