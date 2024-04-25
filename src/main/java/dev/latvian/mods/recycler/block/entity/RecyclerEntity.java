package dev.latvian.mods.recycler.block.entity;

import dev.latvian.mods.recycler.recipe.RecyclerRecipe;
import dev.latvian.mods.recycler.recipe.RecyclerRecipeStore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class RecyclerEntity extends TileEntity {
	public boolean advanced;
	public final ItemStackHandler input;
	public final ItemStackHandler output;
	public final LazyOptional<IItemHandler> inputOptional;
	public final LazyOptional<IItemHandler> outputOptional;

	public RecyclerEntity() {
		super(RecyclerBlockEntities.RECYCLER.get());
		input = new ItemStackHandler(9) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				// level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
			}
		};

		output = new ItemStackHandler(9) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
		};

		inputOptional = LazyOptional.of(() -> input);
		outputOptional = LazyOptional.of(() -> output);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (!advanced) {
			return super.getCapability(cap, side);
		}

		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (side != Direction.DOWN ? inputOptional : outputOptional).cast() : super.getCapability(cap, side);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putBoolean("Advanced", advanced);
		compound.put("Input", input.serializeNBT());
		compound.put("Output", output.serializeNBT());
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		advanced = compound.getBoolean("Advanced");
		input.deserializeNBT(compound.getCompound("Input"));
		output.deserializeNBT(compound.getCompound("Output"));
	}

	public int getNextTime() {
		for (int i = 0; i < input.getSlots(); i++) {
			RecyclerRecipe recipe = RecyclerRecipeStore.getRecipe(level, input.getStackInSlot(i).getItem());

			if (recipe != null) {
				return recipe.time;
			}
		}

		return advanced ? 100 : 0;
	}

	public int recycle() {
		for (int i = 0; i < input.getSlots(); i++) {
			ItemStack is = input.getStackInSlot(i);

			if (!is.isEmpty()) {
				RecyclerRecipe recipe = RecyclerRecipeStore.getRecipe(level, is.getItem());

				if (recipe != null) {
					is.shrink(1);

					if (is.isEmpty()) {
						input.setStackInSlot(i, ItemStack.EMPTY);
					}

					boolean all = true;

					for (ItemStack result : recipe.result) {
						ItemStack r = ItemHandlerHelper.insertItem(output, result.copy(), false);

						if (!r.isEmpty()) {
							all = false;
							Block.popResource(level, getBlockPos().above(), r);
						}
					}

					setChanged();
					return all ? getNextTime() : (advanced ? 100 : 0);
				}
			}
		}

		return advanced ? 100 : 0;
	}
}
