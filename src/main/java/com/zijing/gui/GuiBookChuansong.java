package com.zijing.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.card.ItemCardChuansong;
import com.zijing.items.staff.ItemZilingZhu;
import com.zijing.items.tool.ItemToolZijingChu;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBookChuansong {
	public static final int GUIID = 2;

	public static class MyContainer extends Container {
		private IInventory bookInv;
		private NonNullList<ItemStack> items;
		private NBTTagCompound bookTag;
		private EnumHand hand;
		private String type;
		
		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.hand = null == player.getActiveHand() ? EnumHand.MAIN_HAND : player.getActiveHand();
			
			Item item = player.getHeldItem(this.hand).getItem();
			this.hand = (item instanceof ItemBookChuansong || item instanceof ItemToolZijingChu) ? this.hand : (this.hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
			this.type = player.getHeldItem(this.hand).getItem() instanceof ItemBookChuansong ? "ItemBookChuansong" : "ItemToolZijingChu";
			
			this.bookTag = player.getHeldItem(this.hand).getTagCompound();
			this.bookInv = new InventoryBasic(ConstantUtil.MODID + ":itembookchuansong", true, 29);
			this.items = NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(bookTag, items);
			for(int n = 0; n < items.size(); n++){
				bookInv.setInventorySlotContents(n, items.get(n));
			}
			this.addSlotToContainer(new Slot(bookInv, 0, 148, 35) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemZilingZhu;
				}
			});
			for(int m = 0; m < 4; m++){
				for(int n = 1; n<= 7; n++){
					this.addSlotToContainer(new Slot(bookInv, n + m*7, (n - 1)*18 + 8, m*18 + 8) {
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && ("ItemBookChuansong".equals(type) ? stack.getItem() instanceof ItemCardChuansong : (stack.getItem() instanceof IPlantable && stack.getItem() instanceof Item));
						}
					});
				}
			}
			InventoryPlayer inventory = player.inventory;
			
			for (int m = 0; m < 4; ++m) {
				for (int n = 0; n < 9; ++n) {
					if(m == 0) {
						if(this.hand == EnumHand.MAIN_HAND && n == inventory.currentItem) {
							this.addSlotToContainer(new Slot(inventory, n, 8 + n*18, 142) {
								public boolean isItemValid(ItemStack stack) {
									return false;
								}
								public boolean canTakeStack(EntityPlayer playerIn){
							        return false;
							    }
							});
						}else {
							this.addSlotToContainer(new Slot(inventory, n, 8 + n*18, 142));
						}
					}else {
						this.addSlotToContainer(new Slot(inventory, n + m*9, 8 + n*18, 66 + m*18));
					}
				}
			}
		}

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return true;
		}
		
		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			for(int n = 0; n < 29; n++){
				items.set(n, bookInv.getStackInSlot(n));
			}
			ItemStackHelper.saveAllItems(bookTag, items, true);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class MyGuiContainer extends GuiContainer {
		private final ResourceLocation texture = new ResourceLocation(ConstantUtil.MODID + ":featurebook.png");

		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer player) {
			super(new MyContainer(world, i, j, k, player));
			this.xSize = 176;
			this.ySize = 166;
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }

		@Override
		protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(texture);
			this.drawTexturedModalRect((this.width-this.xSize)/2, (this.height-this.ySize)/2, 0, 0, this.xSize, this.ySize);
		}
	}
}
