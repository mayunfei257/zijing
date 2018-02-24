package com.zijing.main.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.card.ItemCardChuansong;
import com.zijing.main.BaseControl;
import com.zijing.main.message.ChuansongBookToServerMessage;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBookChuansongUse {
	public static final int GUIID = 3;

	public static class MyContainer extends Container {
		private EntityPlayer player;
		private NBTTagCompound chuansongBookTag;
		private NonNullList<ItemStack> items;
		private EnumHand hand;
		
		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.hand = null == player.getActiveHand() ? EnumHand.MAIN_HAND : player.getActiveHand();
			this.hand = player.getHeldItem(this.hand).getItem() instanceof ItemBookChuansong ? this.hand : (this.hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
			this.chuansongBookTag = player.getHeldItem(this.hand).getTagCompound();
			this.items = NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY);
			this.player = player;
			ItemStackHelper.loadAllItems(chuansongBookTag, items);
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}

		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			ItemStackHelper.saveAllItems(chuansongBookTag, items, true);
		}
		
		public String getCardName(int index) {
			if(index > 0 && index < 29 && null != items.get(index) && !items.get(index).isEmpty() && items.get(index).hasTagCompound()) {
				NBTTagCompound cardTag = items.get(index).getTagCompound();
				if(null != cardTag && cardTag.getBoolean(ItemCardChuansong.IS_BIND)) {
					return cardTag.getString(ItemCardChuansong.BIND_NAME);
				}
			}
			return null;
		}
		
		public void teleportEntity(int index) {
			if(index > 0 && index < 29 && null != items.get(index) && !items.get(index).isEmpty()) {
				NBTTagCompound chuansongCardTag = items.get(index).getTagCompound();
				if(null != chuansongCardTag && chuansongCardTag.getBoolean(ItemCardChuansong.IS_BIND) && player.dimension == chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)) {
					if(ShepherdProvider.hasCapabilityFromPlayer(player) && ShepherdProvider.getCapabilityFromPlayer(player).getMagic() >= ItemBookChuansong.MagicSkill1) {
						ItemStackHelper.saveAllItems(chuansongBookTag, items, true);
						BaseControl.netWorkWrapper.sendToServer(new ChuansongBookToServerMessage(chuansongBookTag, chuansongCardTag, hand, player.getUniqueID()));
					}else {
						player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + ItemBookChuansong.MagicSkill1 + " !"));
					}
				}else if(player.dimension != chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)){
					player.sendMessage(new TextComponentString("Not the same world! the world is " + player.dimension + ", this card is " + chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)));
				}else {
					player.sendMessage(new TextComponentString("Not yet bound!"));
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class MyGuiContainer extends GuiContainer {

		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer player) {
			super(new MyContainer(world, i, j, k, player));
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			this.fontRenderer.drawString("Some text",this.width/2 + 125, this.height/2 + 70, 0xffffff);
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }
		
		@Override
		public void initGui() {
			super.initGui();
			this.buttonList.clear();
			for(int index = 1; index < 29; index++) {
				String name = ((MyContainer)this.inventorySlots).getCardName(index);
				if(null != name) {
					int x = (index - 1) % 6;
					int y = (index - 1) / 6;
					this.buttonList.add(new GuiButton(index, this.width/2 + (60 * x - 175), this.height/2 + (40 * y - 90), 50, 20, name));
				}
			}
		}

		@Override
		protected void actionPerformed(GuiButton button) {
			for(int index = 1; index < 29; index++) {
				if (button.id == index) {
					((MyContainer)this.inventorySlots).teleportEntity(index);
					this.mc.player.closeScreen();
				}
			}
		}
	}
}
