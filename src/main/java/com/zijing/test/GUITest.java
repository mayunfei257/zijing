package com.zijing.test;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GUITest {
	public static int GUIID = 1;
	public static IInventory inveAAA;
	public static IInventory inherited;
	public static class GuiContainerMod extends Container {
		World world = null;
		EntityPlayer entity = null;
		int i, j, k;
		public GuiContainerMod(World world, int i, int j, int k, EntityPlayer player) {
			this.world = world;
			this.entity = player;
			this.i = i;
			this.j = j;
			this.k = k;
			inveAAA = new InventoryBasic("inveAAA", true, 9);
			this.addSlotToContainer(new Slot(inveAAA, 0, 30, 30) {
				public void onSlotChanged() {
					super.onSlotChanged();
					if (getHasStack()) {
						EntityPlayer entity = Minecraft.getMinecraft().player;
						int i = (int) entity.posX;
						int j = (int) entity.posY;
						int k = (int) entity.posZ;
						MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
						World world = server.worlds[0];
					}
				}
			});
			bindPlayerInventory(player.inventory);
		}

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return true;
		}

		protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
			int i;
			int j;
			for (i = 0; i < 3; ++i) {
				for (j = 0; j < 9; ++j) {
					this.addSlotToContainer(new Slot(inventoryPlayer, j + (i + 1) * 9, 0 + 8 + j * 18, 0 + 84 + i * 18));
				}
			}
			for (i = 0; i < 9; ++i) {
				this.addSlotToContainer(new Slot(inventoryPlayer, i, 0 + 8 + i * 18, 0 + 142));
			}
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
			ItemStack itemstack = null;
			Slot slot = (Slot) this.inventorySlots.get(index);
			if (slot != null && slot.getHasStack()) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();

				if (index < 9) {
					if (!this.mergeItemStack(itemstack1, 9, (45 - 9), true)) {// fixes
																				// shiftclick
																				// error
						return null;
					}
				} else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
					return null;
				}

				if (itemstack1.getCount() == 0) {
					slot.putStack((ItemStack) null);
				} else {
					slot.onSlotChanged();
				}

				if (itemstack1.getCount() == itemstack.getCount()) {
					return null;
				}

				slot.onTake(playerIn, itemstack1);
			}

			return itemstack;
		}

		public void onContainerClosed(EntityPlayer playerIn) {
			super.onContainerClosed(playerIn);

			MinecraftServer server2 = FMLCommonHandler.instance().getMinecraftServerInstance();
			World world2 = server2.worlds[0];
			InventoryHelper.dropInventoryItems(world2, new BlockPos(i, j, k), inveAAA);

		}
	}

	public static class GuiWindow extends GuiContainer {
		int i = 0;
		int j = 0;
		int k = 0;
		EntityPlayer entity = null;
		public GuiWindow(World world, int i, int j, int k, EntityPlayer entity) {
			super(new GuiContainerMod(world, i, j, k, entity));
			this.i = i;
			this.j = j;
			this.k = k;
			this.entity = entity;
			this.xSize = 176;
			this.ySize = 166;
		}
		private static final ResourceLocation texture = new ResourceLocation("ttttt.png");
		@Override
		protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawDefaultBackground();

			this.mc.renderEngine.bindTexture(texture);
			int k = (this.width - this.xSize) / 2;
			int l = (this.height - this.ySize) / 2;
			this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
			zLevel = 100.0F;
		}

		@Override
		protected void mouseClicked(int par1, int par2, int par3) throws java.io.IOException {
			super.mouseClicked(par1, par2, par3);
		}

		@Override
		public void updateScreen() {
			super.updateScreen();
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;

		}

		@Override
		protected void keyTyped(char par1, int par2) throws java.io.IOException {

			super.keyTyped(par1, par2);

		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;
			this.fontRenderer.drawString("Some text", (73), (8), 0xffffff);

		}

		@Override
		public void onGuiClosed() {
			super.onGuiClosed();
			Keyboard.enableRepeatEvents(false);
		}

		@Override
		public void initGui() {
			super.initGui();
			this.guiLeft = (this.width - 176) / 2;
			this.guiTop = (this.height - 166) / 2;
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;
			this.buttonList.add(new GuiButton(0, this.guiLeft + 73, this.guiTop + 32, 84, 20, "BBBBBBB"));

		}

		@Override
		protected void actionPerformed(GuiButton button) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			World world = server.worlds[0];

			if (button.id == 0) {

				if (true) {
					if (entity instanceof EntityPlayer)
						((EntityPlayer) entity).inventory.addItemStackToInventory(new ItemStack(Blocks.GOLD_BLOCK, 1));
				}

				if (true) {
					world.setWorldTime(0);
				}

				if (true) {
					world.createExplosion((Entity) null, i, j, k, 2F, true);
				}

			}

		}

		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}

	}

}