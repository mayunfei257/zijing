package com.zijing.main.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.ZijingMod;
import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.card.ItemCardChuansong;
import com.zijing.items.staff.ItemZilingZhu;

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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMagicEnergySwitch {
	public static final int GUIID = 4;

	public static class GuiMagicEnergySwitchContainer extends Container {
		private IInventory bookInv;
		private NonNullList<ItemStack> items;
		private NBTTagCompound bookTag;
		
		public GuiMagicEnergySwitchContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.bookTag = player.getHeldItemMainhand().getItem() instanceof ItemBookChuansong ? player.getHeldItemMainhand().getTagCompound() : player.getHeldItemOffhand().getTagCompound();
			this.bookInv = new InventoryBasic(ZijingMod.MODID + ":itembookchuansong", true, 29);
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
							return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemCardChuansong;
						}
					});
				}
			}
			InventoryPlayer inventory = player.inventory;
			for (int m = 0; m < 4; ++m) {
				for (int n = 0; n < 9; ++n) {
					if(m == 0) {
						this.addSlotToContainer(new Slot(inventory, n, 8 + n*18, 142));
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
	public static class GuiMagicEnergySwitchWindow extends GuiContainer {
		private final ResourceLocation texture = new ResourceLocation(ZijingMod.MODID + ":featurebook.png");
	    private final InventoryPlayer playerInventory;
		private IInventory bookInv;

		public GuiMagicEnergySwitchWindow(World world, int i, int j, int k, EntityPlayer player) {
			super(new GuiMagicEnergySwitchContainer(world, i, j, k, player));
			this.xSize = 176;
			this.ySize = 166;
			playerInventory = player.inventory;
			NBTTagCompound bookTag = player.getHeldItemMainhand().getItem() instanceof ItemBookChuansong ? player.getHeldItemMainhand().getTagCompound() : player.getHeldItemOffhand().getTagCompound();
			this.bookInv = new InventoryBasic(ZijingMod.MODID + ":itembookchuansong", true, 29);
			NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(bookTag, items);
			for(int n = 0; n < items.size(); n++){
				bookInv.setInventorySlotContents(n, items.get(n));
			}
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

		@Override
	    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
	        String s = this.bookInv.getDisplayName().getUnformattedText();
	        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	    }
	}
}
