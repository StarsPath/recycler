package dev.latvian.mods.recycler.gui;

import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.block.RecyclerBlock;
import dev.latvian.mods.recycler.block.entity.RecyclerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class RecyclerMenu extends Container {
	public final RecyclerEntity recycler;

	public RecyclerMenu(int id, PlayerInventory playerInv, RecyclerEntity r) {
		super(RecyclerMenus.RECYCLER.get(), id);
		recycler = r;

		for (int x = 0; x < 9; x++) {
			addSlot(new SlotItemHandler(recycler.input, x, 8 + x * 18, 17));
		}

		for (int x = 0; x < 9; x++) {
			addSlot(new SlotItemHandler(recycler.output, x, 8 + x * 18, 53));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlot(new Slot(playerInv, x, 8 + x * 18, 142));
		}
	}

	public RecyclerMenu(int i, PlayerInventory playerInv, PacketBuffer buf) {
		this(i, playerInv, (RecyclerEntity) playerInv.player.level.getBlockEntity(buf.readBlockPos()));
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
		ItemStack lv = ItemStack.EMPTY;
		Slot lv2 = slots.get(slotId);
		if (lv2 != null && lv2.hasItem()) {
			ItemStack lv3 = lv2.getItem();
			lv = lv3.copy();
			if (slotId < 18) {
				if (!moveItemStackTo(lv3, 18, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!moveItemStackTo(lv3, 0, 18, false)) {
				return ItemStack.EMPTY;
			}

			if (lv3.isEmpty()) {
				lv2.set(ItemStack.EMPTY);
			} else {
				lv2.setChanged();
			}
		}

		return lv;
	}

	@Override
	public boolean stillValid(PlayerEntity arg) {
		return !recycler.isRemoved();
	}

	@Override
	public boolean clickMenuButton(PlayerEntity player, int button) {
		if (button == 0) {
			if (recycler.getBlockState().getValue(RecyclerBlock.RUNNING)) {
				RecyclerBlock.stop(recycler.getBlockState(), player.level, recycler.getBlockPos());
			} else {
				RecyclerBlock.start(recycler.getBlockState(), player.level, recycler.getBlockPos());
			}
		}

		return true;
	}
}
