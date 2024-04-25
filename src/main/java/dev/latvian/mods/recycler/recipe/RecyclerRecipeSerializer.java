package dev.latvian.mods.recycler.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class RecyclerRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecyclerRecipe> {
	@Override
	public RecyclerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		RecyclerRecipe r = new RecyclerRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("ingredient")) {
			r.ingredient = Ingredient.fromJson(json.get("ingredient"));
		}

		if (json.has("result")) {
			for (JsonElement e : json.get("result").getAsJsonArray()) {
				r.result.add(ShapedRecipe.itemFromJson(e.getAsJsonObject()));
			}
		}

		if (json.has("time")) {
			r.time = json.get("time").getAsInt();
		}

		return r;
	}

	@Override
	public RecyclerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
		RecyclerRecipe r = new RecyclerRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.time = buffer.readVarInt();
		r.ingredient = Ingredient.fromNetwork(buffer);

		int iout = buffer.readUnsignedByte();

		for (int i = 0; i < iout; i++) {
			r.result.add(buffer.readItem());
		}

		return r;
	}

	@Override
	public void toNetwork(PacketBuffer buffer, RecyclerRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeVarInt(r.time);
		r.ingredient.toNetwork(buffer);
		buffer.writeByte(r.result.size());

		for (ItemStack s : r.result) {
			buffer.writeItem(s);
		}
	}
}
