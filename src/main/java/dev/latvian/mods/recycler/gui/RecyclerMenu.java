package dev.latvian.mods.recycler.gui;

import dev.latvian.mods.recycler.block.RecyclerBlock;
import dev.latvian.mods.recycler.block.entity.RecyclerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class RecyclerMenu extends AbstractContainerMenu {
	public final RecyclerEntity recycler;

	public RecyclerMenu(int id, Inventory playerInv, RecyclerEntity r) {
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

	public RecyclerMenu(int i, Inventory playerInv, FriendlyByteBuf buf) {
		this(i, playerInv, (RecyclerEntity) playerInv.player.level.getBlockEntity(buf.readBlockPos()));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotId) {
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
	public boolean stillValid(Player arg) {
		return !recycler.isRemoved();
	}

	@Override
	public boolean clickMenuButton(Player player, int button) {
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
