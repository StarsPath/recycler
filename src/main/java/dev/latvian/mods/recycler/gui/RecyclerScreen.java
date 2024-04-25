package dev.latvian.mods.recycler.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.recycler.Recycler;
import dev.latvian.mods.recycler.block.RecyclerBlock;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RecyclerScreen extends ContainerScreen<RecyclerMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Recycler.MOD_ID, "textures/gui/recycler.png");

	private AbstractButton button;

	public RecyclerScreen(RecyclerMenu menu, PlayerInventory playerInv, ITextComponent title) {
		super(menu, playerInv, title);
		imageWidth = 176;
		imageHeight = 166;
	}

	@Override
	protected void init() {
		super.init();
		addButton(button = new AbstractButton(leftPos + 7, topPos + 35, 162, 16, ITextComponent.nullToEmpty("")) {
			@Override
			public void onPress() {
				minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
			}

			@Override
			public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float delta) {
			}
		});
	}

	@Override
	public void render(MatrixStack poseStack, int mouseX, int mouseY, float delta) {
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, delta);
		renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack poseStack, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bind(TEXTURE);
		blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		if (button.isHovered()) {
			blit(poseStack, button.x, button.y, 0, 167, 162, 16);
		}

		boolean running = menu.recycler.getBlockState().getValue(RecyclerBlock.RUNNING);

		if (running) {
			int i = (int) ((System.currentTimeMillis() / 200L) % 12L);

			if (i > 0) {
				blit(poseStack, leftPos + 156, topPos + 37, 163, 167, 11, i);
			}
		}

		font.draw(poseStack, new TranslationTextComponent(running ? "block.recycler.recycler.stop" : "block.recycler.recycler.start"), leftPos + 11, topPos + 39, button.isHovered() ? 0xFFFFFFFF : 0xFF373737);
	}
}
