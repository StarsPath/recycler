package dev.latvian.mods.recycler.kubejs;

import com.google.gson.JsonArray;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

public class RecyclerRecipeJS extends RecipeJS {
	@Override
	public void create(ListJS args) {
		outputItems.addAll(parseResultItemList(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)));
	}

	@Override
	public void deserialize() {
		outputItems.addAll(parseResultItemList(json.get("result")));
		inputItems.add(parseIngredientItem(json.get("ingredient")));
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			JsonArray array = new JsonArray();

			for (ItemStackJS in : outputItems) {
				array.add(in.toResultJson());
			}

			json.add("result", array);
		}

		if (serializeInputs) {
			json.add("ingredient", inputItems.get(0).toJson());
		}
	}
}
