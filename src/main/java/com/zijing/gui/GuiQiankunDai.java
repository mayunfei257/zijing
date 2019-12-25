package com.zijing.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.items.ItemQiankunDai;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiQiankunDai {

	public static int GUIID = 7;

	public static class MyContainer extends Container {
		private static final int guiSize = 54;
		private final int qiankunDaiSize;
		private IInventory QKDInv;
		private NBTTagCompound qiankunDaiTag;
		private NonNullList<ItemStack> items;


		public MyContainer(World world, int handValue, EntityPlayer player) {
			EnumHand hand = 0 == handValue ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			hand = player.getHeldItem(hand).getItem() instanceof ItemQiankunDai ? hand : (hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
			this.qiankunDaiTag = player.getHeldItem(hand).getTagCompound();
			
			this.qiankunDaiSize = qiankunDaiTag.getInteger(ConstantUtil.MODID + ":invsize");
			this.QKDInv = new InventoryBasic(ConstantUtil.MODID + ":qkdinventory", true, this.qiankunDaiSize);
			this.items = NonNullList.<ItemStack>withSize(this.qiankunDaiSize, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(qiankunDaiTag, items);
			
			for(int n = 0; n < this.qiankunDaiSize; n++) {
				QKDInv.setInventorySlotContents(n, items.get(n));
			}
			
			//bind bag inventory
			for(int n = 0; n < QKDInv.getSizeInventory(); n++) {
				this.addSlotToContainer(new Slot(QKDInv, n, n % 9 * 18 + 8, n / 9 * 18 + 18){
					public boolean isItemValid(ItemStack stack){
						if(null == stack || ItemStack.EMPTY == stack || null == stack.getItem()) {return false;}
						if(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox) {return false;}
						if(stack.getItem() instanceof ItemQiankunDai) {return false;}
						return true;
					}
				});
			}
			
			//player Inventory
			InventoryPlayer inventory = player.inventory;
			for (int m = 0; m < 3; ++m) {
				for (int n = 0; n < 9; ++n) {
					this.addSlotToContainer(new Slot(inventory, n + ( m + 1 ) * 9, n * 18 + 8, m * 18 + 140));
				}
			}
			for (int m = 0; m < 9; m++) {
				if(hand == EnumHand.MAIN_HAND && m == inventory.currentItem) {
					this.addSlotToContainer(new Slot(inventory, m, m * 18 + 8, 198){
						public boolean isItemValid(ItemStack stack) {
							return false;
						}
						public boolean canTakeStack(EntityPlayer playerIn){
					        return false;
					    }
					});
				}else {
					this.addSlotToContainer(new Slot(inventory, m, m * 18 + 8, 198));
				}
			
			}
		}

		public int getEmptySlotSize(){
			return guiSize - qiankunDaiSize;
		}

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return true;
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
			ItemStack itemstack = ItemStack.EMPTY;
			Slot slot = this.inventorySlots.get(index);

			if (slot != null && slot.getHasStack()){
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();

				if (index < this.qiankunDaiSize){
					if (!this.mergeItemStack(itemstack1, this.qiankunDaiSize, this.inventorySlots.size(), true)){
						return ItemStack.EMPTY;
					}
				}else if (!this.mergeItemStack(itemstack1, 0, this.qiankunDaiSize, false)){
					return ItemStack.EMPTY;
				}

				if (itemstack1.isEmpty()){
					slot.putStack(ItemStack.EMPTY);
				}else{
					slot.onSlotChanged();
				}
			}

			return itemstack;
		}
		
		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			for(int n = 0; n < qiankunDaiSize; n++){
				items.set(n, QKDInv.getStackInSlot(n));
			}
			ItemStackHelper.saveAllItems(qiankunDaiTag, items, true);
		}
	}

	public static class MyGuiContainer extends GuiContainer {
		private static final ResourceLocation texture = new ResourceLocation(ConstantUtil.MODID + ":shepherdqkbag.png");

		public MyGuiContainer(World world, int handValue, EntityPlayer entity) {
			super(new MyContainer(world, handValue, entity));
			this.xSize = 176;
			this.ySize = 222;
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(texture);
			this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
			int emptySize = ((MyContainer)this.inventorySlots).getEmptySlotSize();
			if(emptySize > 0){
				for(int n = 0; n < emptySize; n++){
					this.drawTexturedModalRect(this.guiLeft + ( 8 - n % 9 ) * 18 + 8, this.guiTop + ( 5 - n / 9 ) * 18 + 18, 176, 18, 16, 16);
				}
			}
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }
	}
}
