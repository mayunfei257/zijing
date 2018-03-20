package com.zijing.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zijing.itf.MagicConsumer;
import com.zijing.itf.MagicSource;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiZhulingTai {
	public static final int GUIID = 4;
	public static class MyContainer extends Container {
		private IInventory magicInve;
		private World world = null;
		private EntityPlayer player = null;
		private BlockPos blockPos = null;
		private Slot slot1 = null;
		private Slot slot2 = null;
		private Slot slot3 = null;

		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.world = world;
			this.player = player;
			this.blockPos = new BlockPos(i, j, k);
			magicInve = new InventoryBasic("inveA", true, 3);
			slot1 = new Slot(magicInve, 0, 26, 34) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && stack.getItem() instanceof MagicSource;
				}
				public void onSlotChanged() {
					super.onSlotChanged();
					onTheSlotChanged();
				}
			};
			slot2 = new Slot(magicInve, 1, 80, 34) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && stack.getItem() instanceof MagicConsumer;
				}
				public void onSlotChanged() {
					super.onSlotChanged();
					onTheSlotChanged();
				}
			};
			slot3 = new Slot(magicInve, 2, 134, 34) {
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
			};
			this.addSlotToContainer(slot1);
			this.addSlotToContainer(slot2);
			this.addSlotToContainer(slot3);
			InventoryPlayer inventory = player.inventory;
			for (int m = 0; m < 4; ++m) {
				for (int n = 0; n < 9; ++n) {
					if(m == 0) {
						this.addSlotToContainer(new Slot(inventory, n, 8 + n*18, 142));
					}else {
						this.addSlotToContainer(new Slot(inventory, n + m*9, 8 + n*18, 84 + m*18));
					}
				}
			}
		}

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return true;
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
			Slot slot = this.inventorySlots.get(index);
			ItemStack itemstack = slot.getStack();
			if(slot == slot3 && slot.getHasStack() && itemstack.getItem() instanceof MagicConsumer) {
				magicInve.setInventorySlotContents(0, ItemStack.EMPTY);
				magicInve.setInventorySlotContents(1, ItemStack.EMPTY);
			}
			return itemstack;
		}

		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			super.onContainerClosed(playerIn);
			InventoryHelper.dropInventoryItems(world, blockPos, magicInve);
		}
		
		private void onTheSlotChanged() {
			ItemStack itemStack1 = slot1.getStack();
			ItemStack itemStack2 = slot2.getStack();
			if(slot1.getHasStack() && itemStack1.getItem() instanceof MagicSource && slot2.getHasStack() && itemStack2.getItem() instanceof MagicConsumer) {
				ItemStack itemStack3 = itemStack2.copy();
				int magic = ((MagicConsumer)itemStack2.getItem()).getMaxMagicEnergyValue();
				magicInve.setInventorySlotContents(1, ItemStack.EMPTY);
			}
		}
	}

	public static class MyGuiContainer extends GuiContainer {
		EntityPlayer entity = null;
		private static final ResourceLocation texture = new ResourceLocation("guifgfg.png");

		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer entity) {
			super(new MyContainer(world, i, j, k, entity));
			this.entity = entity;
			this.xSize = 176;
			this.ySize = 166;
		}


		protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			this.mc.renderEngine.bindTexture(texture);
			int k = (this.width - this.xSize) / 2;
			int l = (this.height - this.ySize) / 2;
			this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

			this.mc.renderEngine.bindTexture(new ResourceLocation("jia.png"));
			this.drawTexturedModalRect((this.guiLeft + 53), (this.guiTop + 34), 0, 0, 256, 256);

			this.mc.renderEngine.bindTexture(new ResourceLocation("you.png"));
			this.drawTexturedModalRect((this.guiLeft + 108), (this.guiTop + 34), 0, 0, 256, 256);

		}

		public void onGuiClosed() {
			super.onGuiClosed();
			Keyboard.enableRepeatEvents(false);
		}

		public void initGui() {
			super.initGui();
			this.guiLeft = (this.width - 176) / 2;
			this.guiTop = (this.height - 166) / 2;
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();
			int posX = (this.width) / 2;
			int posY = (this.height) / 2;

		}

		public boolean doesGuiPauseGame() {
			return false;
		}
	}
}
