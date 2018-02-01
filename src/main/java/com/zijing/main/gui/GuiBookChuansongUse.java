package com.zijing.main.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.card.ItemCardChuansong;
import com.zijing.items.staff.ItemZilingZhu;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.util.PlayerUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBookChuansongUse {
	public static final int GUIID = 3;

	public static class MyContainer extends Container {
		private EntityPlayer player;
		private NBTTagCompound bookTag;
		private NonNullList<ItemStack> items;
		
		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.bookTag = player.getHeldItemMainhand().getItem() instanceof ItemBookChuansong ? player.getHeldItemMainhand().getTagCompound() : player.getHeldItemOffhand().getTagCompound();
			this.items = NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY);
			this.player = player;
			ItemStackHelper.loadAllItems(bookTag, items);
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}

		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			ItemStackHelper.saveAllItems(bookTag, items, true);
		}
		
		public String getCardName(int index) {
			if(index > 0 && index < 29 && null != items.get(index) && ItemStack.EMPTY != items.get(index) && items.get(index).hasTagCompound()) {
				NBTTagCompound cardTag = items.get(index).getTagCompound();
				if(null != cardTag && cardTag.getBoolean(ItemCardChuansong.IS_BIND)) {
					return cardTag.getString(ItemCardChuansong.BIND_NAME);
				}
			}
			return null;
		}
		
		public void teleportEntity(int index) {
			if(index > 0 && index < 29 && null != items.get(index) && !items.get(index).isEmpty()) {
				if(items.get(index).hasTagCompound() && items.get(index).getTagCompound().getBoolean(ItemCardChuansong.IS_BIND)) {
					if(null != items.get(0) && !items.get(0).isEmpty() && items.get(0).getItem() instanceof ItemZilingZhu && items.get(0).hasTagCompound()) {
						NBTTagCompound chuansongCardTag = items.get(index).getTagCompound();
						NBTTagCompound zhilingZhuTag = items.get(0).getTagCompound();
						if(player.dimension == chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD) && zhilingZhuTag.getInteger(MagicConsumer.MAGIC_ENERGY_STR) >= 3) {
							PlayerUtil.minusMagic(items.get(0), 3);
							ItemStackHelper.saveAllItems(bookTag, items, true);
							double x = chuansongCardTag.getDouble(ItemCardChuansong.BIND_LX);
							double y = chuansongCardTag.getDouble(ItemCardChuansong.BIND_LY);
							double z = chuansongCardTag.getDouble(ItemCardChuansong.BIND_LZ);
							player.setPositionAndUpdate(x, y, z);
							player.world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
						}else if(player.dimension != chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)){
							player.sendMessage(new TextComponentString("Not the same world!"));
						}else if(zhilingZhuTag.getInteger(MagicConsumer.MAGIC_ENERGY_STR) < 3){
							player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 3!"));
						}
					}else {
						player.sendMessage(new TextComponentString("No magic energy!"));
					}
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
					int x = (index - 1) % 7;
					int y = (index - 1) / 7;
					this.buttonList.add(new GuiButton(index, this.width/2 + ((x - 3)*50 - 20), this.height/2 + ((y - 1)*40 - 30), 40, 20, name));
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
