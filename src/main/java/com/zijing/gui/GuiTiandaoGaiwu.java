package com.zijing.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.zijing.BaseControl;
import com.zijing.data.message.ClientToServerMessage;
import com.zijing.entity.TileEntityTiandaoGaiwu;
import com.zijing.entity.TileEntityZhulingTai;
import com.zijing.gui.GuiZhulingTai.MyContainer;
import com.zijing.items.ItemZijing;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiTiandaoGaiwu {
	public static final int GUIID = 8;
	public static final String GUINAME = "GuiTiandaoGaiwu";
	
	public static class MyContainer extends Container {
		private TileEntityTiandaoGaiwu entity;
		private IInventory entityInventory;

		public MyContainer(World world, int x, int y, int z, EntityPlayer player) {
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity != null && (tileEntity instanceof TileEntityTiandaoGaiwu)) {
				entity = (TileEntityTiandaoGaiwu)tileEntity;
				entityInventory = (IInventory) tileEntity;
			}
			
			//tileEntity
			this.addSlotToContainer(new Slot(entityInventory, 0, 117, 26){
				public boolean isItemValid(ItemStack stack) {
					return (stack != null) && stack.getItem() instanceof ItemZijing;
				}
			});
			this.addSlotToContainer(new Slot(entityInventory, 1, 45, 26){//Input
				public boolean isItemValid(ItemStack stack){
					return true;
				}
			});
			this.addSlotToContainer(new Slot(entityInventory, 2, 81, 44){//Output
				public boolean isItemValid(ItemStack stack){
					return false;
				}
			});
				
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
				if (index < 3) {
					if (!this.mergeItemStack(itemstack1, 3, this.inventorySlots.size(), true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.mergeItemStack(itemstack1, 0, 3, false)) {
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

		public String getInputNbtStr() {
			return null == entity.getInputNbtStr() ? "" : entity.getInputNbtStr();
		}
		public void setInputNbtStr(String inputNbtStr) {
			entity.setInputNbtStr(inputNbtStr);
		}
	}

	public static class MyGuiContainer extends GuiContainer {
		private static final ResourceLocation texture = new ResourceLocation(ConstantUtil.MODID + ":tiandaogaiwugui.png");
		private EntityPlayer player = null;
		private GuiTextField field;
		private GuiButton guiButton1;
		private GuiButton guiButton2;
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

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(texture);
			this.drawTexturedModalRect((this.width - this.xSize)/2, (this.height - this.ySize)/2, 0, 0, this.xSize, this.ySize);
		}

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
			super.mouseClicked(mouseX, mouseY, mouseButton);
//			this.field.mouseClicked(mouseX, mouseY, mouseButton);
			
			boolean flag = mouseX >= this.guiLeft + (this.xSize - 160)/2 && mouseX < this.guiLeft + (this.xSize - 160)/2 + 160 && mouseY >= this.guiTop + 5 && mouseY < this.guiTop + 5 + 16;
	        this.field.setFocused(flag);
		}

		@Override
		public void updateScreen(){
			super.updateScreen();
			this.field.updateCursorCounter();
		}

		@Override
		protected void keyTyped(char typedChar, int keyCode) {
			this.field.textboxKeyTyped(typedChar, keyCode);
			if (keyCode != 28 && keyCode != 156){
				if (keyCode == 1){
					this.mc.displayGuiScreen((GuiScreen)null);
				}
			}
		}
		

		@Override
		public void initGui() {
			super.initGui();
			this.guiLeft = (this.width - 176) / 2;
			this.guiTop = (this.height - 166) / 2;
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();

			this.field = new GuiTextField(0, this.fontRenderer, (this.xSize - 160)/2, 5, 160, 16);
			this.field.setMaxStringLength(Short.MAX_VALUE);
			this.field.setFocused(true);
			this.field.setText(((MyContainer)this.inventorySlots).getInputNbtStr());
			
			guiButton1 = new GuiButton(0, this.guiLeft + 10, this.guiTop + 50, 60, 16, I18n.format(ConstantUtil.MODID + ".gui.blockTiandaoGaiwu.button1", new Object[]{}));
			guiButton2 = new GuiButton(1, this.guiLeft + 106, this.guiTop + 50, 60, 16, I18n.format(ConstantUtil.MODID + ".gui.blockTiandaoGaiwu.button2", new Object[]{}));
			this.buttonList.add(guiButton1);
			this.buttonList.add(guiButton2);
		}
		
		@Override
		protected void actionPerformed(GuiButton button) {
			if (button.id == 0) {
				NBTTagCompound nbtTag = new NBTTagCompound();
				nbtTag.setString("OperationType", "CleanNBT");
				nbtTag.setInteger("DimensionId", this.dimensionId);
				nbtTag.setInteger("X", this.x);
				nbtTag.setInteger("Y", this.y);
				nbtTag.setInteger("Z", this.z);
				BaseControl.netWorkWrapper.sendToServer(new ClientToServerMessage(this.player.getUniqueID(), GuiTiandaoGaiwu.GUINAME, nbtTag));
			} else if (button.id == 1) {
				NBTTagCompound nbtTag = new NBTTagCompound();
				nbtTag.setString("OperationType", "AddNBT");
				nbtTag.setInteger("DimensionId", this.dimensionId);
				nbtTag.setInteger("X", this.x);
				nbtTag.setInteger("Y", this.y);
				nbtTag.setInteger("Z", this.z);
				nbtTag.setString("nbtStr", this.field.getText());
				BaseControl.netWorkWrapper.sendToServer(new ClientToServerMessage(this.player.getUniqueID(), GuiTiandaoGaiwu.GUINAME, nbtTag));
//				this.field.setText("");
			}
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
			this.field.drawTextBox();
		}
		
		@Override
		public void onGuiClosed() {
			((MyContainer)this.inventorySlots).setInputNbtStr(this.field.getText());
			super.onGuiClosed();
			Keyboard.enableRepeatEvents(false);
		}
	}
}