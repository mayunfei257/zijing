package com.zijing.main.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.items.card.ItemCardChuansong;
import com.zijing.util.PlayerUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiCardChuansong {
	public static final int GUIID = 1;

	public static class MyContainer extends Container {
		private EntityPlayer player;
		private NBTTagCompound chuansongCardTag;
		
		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.chuansongCardTag = player.getHeldItemMainhand().getItem() instanceof ItemCardChuansong ? player.getHeldItemMainhand().getTagCompound() : player.getHeldItemOffhand().getTagCompound();
			this.player = player;
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {return true;}
		
		@Override
		public void onContainerClosed(EntityPlayer playerIn) {}
		
		public void saveDate(String name){
			chuansongCardTag.setDouble(ItemCardChuansong.BIND_LX, player.posX);
			chuansongCardTag.setDouble(ItemCardChuansong.BIND_LY, player.posY);
			chuansongCardTag.setDouble(ItemCardChuansong.BIND_LZ, player.posZ);
			chuansongCardTag.setInteger(ItemCardChuansong.BIND_WORLD, player.dimension);
			chuansongCardTag.setString(ItemCardChuansong.BIND_NAME, name);
			chuansongCardTag.setBoolean(ItemCardChuansong.IS_BIND, true);
			PlayerUtil.minusFoodlevel(player, ItemCardChuansong.foodConsume);
		}
	}

	@SideOnly(Side.CLIENT)	//TODO
	public static class MyGuiContainer extends GuiContainer {
		private GuiTextField paperName;
		
		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer player) {
			super(new MyContainer(world, i, j, k, player));
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		@Override
		protected void mouseClicked(int par1, int par2, int par3) throws java.io.IOException {
			super.mouseClicked(par1, par2, par3);
			this.paperName.mouseClicked(par1, par2, par3);
			this.paperName.setFocused(true);
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }
		
		@Override
		public void updateScreen() {
			super.updateScreen();
			this.paperName.updateCursorCounter();
		}
		
		@Override
		protected void keyTyped(char keyChar, int keyCode) throws java.io.IOException {
			if (keyCode == 1){ this.mc.player.closeScreen();}
			this.paperName.textboxKeyTyped(keyChar, keyCode);
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			this.paperName.drawTextBox();
		}

		@Override
		public void initGui() {
			super.initGui();
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 , 40, 20, "OK"));
			this.buttonList.add(new GuiButton(2, this.width / 2 + 10, this.height / 2, 40, 20, "Cancle"));
			this.paperName = new GuiTextField(0, this.fontRenderer, 30, 40, 120, 20);
			this.paperName.setMaxStringLength(512);
			this.paperName.setFocused(true);
			this.paperName.setText("");
		}

		@Override
		protected void actionPerformed(GuiButton button) {
			if (button.id == 1) {
				((MyContainer)this.inventorySlots).saveDate(this.paperName.getText());
				this.mc.player.closeScreen();
			}
			if (button.id == 2) {
				this.mc.player.closeScreen();
			}
		}
	}
}
