package dev.latvian.mods.recycler.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RecyclerRecipeStore extends MapItemSavedData {
	public static final String KEY = "recycler_recipes";
	public final Map<Item, RecyclerRecipe> recipeMap;

	@Nullable
	public static RecyclerRecipe getRecipe(Level level, Item item) {
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

	public RecyclerRecipeStore(Level l) {
		super(KEY);
		recipeMap = new HashMap<>();

		for (RecyclerRecipe recyclerRecipe : l.getRecipeManager().getRecipesFor(RecyclerRecipeSerializers.RECYCLER_TYPE, NoInventory.INSTANCE, l)) {
			for (ItemStack item : recyclerRecipe.ingredient.getItems()) {
				recipeMap.put(item.getItem(), recyclerRecipe);
			}
		}
	}
}
