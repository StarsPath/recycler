package dev.latvian.mods.recycler.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class RecyclerEntity extends BlockEntity implements TickableBlockEntity {
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
		return cap == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (side != Direction.DOWN ? inputOptional : outputOptional).cast() : super.getCapability(cap, side);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.put("Input", input.serializeNBT());
		compound.put("Output", output.serializeNBT());
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		input.deserializeNBT(compound.getCompound("Input"));
		output.deserializeNBT(compound.getCompound("Output"));
	}

	@Override
	public void tick() {
	}
}
