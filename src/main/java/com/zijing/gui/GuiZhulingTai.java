package com.zijing.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zijing.BaseControl;
import com.zijing.blocks.BlockZijingKuai;
import com.zijing.data.message.ClientToServerMessage;
import com.zijing.entity.TileEntityZhulingTai;
import com.zijing.items.ItemQiankunDai;
import com.zijing.items.ItemZijing;
import com.zijing.items.ItemZiqi;
import com.zijing.itf.MagicConsumer;
import com.zijing.itf.MagicSource;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiZhulingTai {
	public static final int GUIID = 4;
	public static final String GUINAME = "GuiZhulingTai";
	
	public static class MyContainer extends Container {
		public IInventory entityInventory;

		public MyContainer(World world, int x, int y, int z, EntityPlayer player) {
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity != null && (tileEntity instanceof TileEntityZhulingTai))
				entityInventory = (IInventory) tileEntity;
			
			//tileEntity
			this.addSlotToContainer(new Slot(entityInventory, 0, 80, 8) {//Crystal
				public boolean isItemValid(ItemStack stack) {
					return (stack != null) && stack.getItem() instanceof ItemZijing;
				}
			});
			for(int i = 0; i < 3; i++) {//Input
				for(int j = 0; j < 3; j++) {
					this.addSlotToContainer(new Slot(entityInventory, i * 3 + j + 1, 8 + j * 18, 8 + i * 18) {
						public boolean isItemValid(ItemStack stack) {
							boolean value = stack != null 
									&& (stack.getItem() != BaseControl.itemZiqi)
									&& (stack.getItem() != BaseControl.itemZijing) 
									&& (stack.getItem() != Item.getItemFromBlock(BaseControl.blockZijingKuai))
									&& (stack.getItem() != Item.getItemFromBlock(BaseControl.blockZilingCao))
									&& (stack.getItem() != BaseControl.itemQiankunDai);
							return value;
						}
					});
				}
			}
			for(int i = 0; i < 3; i++) {//Output
				for(int j = 0; j < 3; j++) {
					this.addSlotToContainer(new Slot(entityInventory, i * 3 + j + 10, 116 + j * 18, 8 + i * 18) {
						public boolean isItemValid(ItemStack stack) {
							return false;
						}
					});
				}
			}
			//player
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 9; j++) {
					if(i == 0) {
						this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
					}else {
						this.addSlotToContainer(new Slot(player.inventory, j + i * 9, 8 + j * 18, 84 + (i-1) * 18));
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
			ItemStack itemstack = ItemStack.EMPTY;
			Slot slot = (Slot) this.inventorySlots.get(index);
			if (slot != null && slot.getHasStack()) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				if (index < 19) {
					if (!this.mergeItemStack(itemstack1, 19, this.inventorySlots.size(), true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.mergeItemStack(itemstack1, 0, 19, false)) {
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
		
		public ItemStack getCrystalItemStack() {
			return entityInventory.getStackInSlot(0);
		}
	}

	public static class MyGuiContainer extends GuiContainer {
		private static final ResourceLocation texture = new ResourceLocation(ConstantUtil.MODID + ":zhulingtaigui.png");
		private EntityPlayer player = null;
		private GuiButton guiButton;
		private int dimensionId;
		private int x;
		private int y;
		private int z;

		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer entity) {
			super(new MyContainer(world, i, j, k, entity));
			this.player = entity;
			this.dimensionId = world.provider.getDimension();
			this.xSize = 176;
			this.ySize = 166;
			this.x = i;
			this.y = j;
			this.z = k;
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }

		protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(texture);
			this.drawTexturedModalRect((this.width - this.xSize)/2, (this.height - this.ySize)/2, 0, 0, this.xSize, this.ySize);
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

			guiButton = new GuiButton(0, this.guiLeft + 72, this.guiTop + 41, 32, 20, I18n.format(ConstantUtil.MODID + ".gui.blockZhulingTai.button.type0", new Object[]{}));
			this.buttonList.add(guiButton);
		}
		
		@Override
		protected void actionPerformed(GuiButton button) {
//			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
//			World world = server.worldServers[0];
			if (button.id == 0) {
				NBTTagCompound nbtTag = new NBTTagCompound();
				nbtTag.setInteger("DimensionId", this.dimensionId);
				nbtTag.setInteger("X", this.x);
				nbtTag.setInteger("Y", this.y);
				nbtTag.setInteger("Z", this.z);
				BaseControl.netWorkWrapper.sendToServer(new ClientToServerMessage(this.player.getUniqueID(), GuiZhulingTai.GUINAME, nbtTag));
			}
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
			ItemStack itemCrystal = ((MyContainer)this.inventorySlots).getCrystalItemStack();
			String buttonName = I18n.format(ConstantUtil.MODID + ".gui.blockZhulingTai.button.type0", new Object[]{});
			if(null != itemCrystal && !itemCrystal.isEmpty()) {
//				if(itemCrystal.getItem() == BaseControl.itemHunDunCrystal)
//					buttonName = I18n.format(ConstantUtil.MODID + ".guiHunDunTable.button.type1", new Object[]{});
//				else if(itemCrystal.getItem() == BaseControl.itemFuZhiCrystal) 
//					buttonName = I18n.format(ConstantUtil.MODID + ".guiHunDunTable.button.type2", new Object[]{});
//				else if(itemCrystal.getItem() == BaseControl.itemHuiMieCrystal || itemCrystal.getItem() == BaseControl.itemShengMingCrystal) 
//					buttonName = I18n.format(ConstantUtil.MODID + ".guiHunDunTable.button.type3", new Object[]{});
			}
			guiButton.displayString = buttonName;
		}
	}
}
