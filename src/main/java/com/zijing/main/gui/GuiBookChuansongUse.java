package com.zijing.main.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.ZijingMod;
import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.staff.ItemZilingZhu;

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

	public static class GuiBookChuansongUseContainer extends Container {
		private EntityPlayer player;
		private NBTTagCompound bookTag;
		private NonNullList<ItemStack> items;
		
		public GuiBookChuansongUseContainer(World world, int i, int j, int k, EntityPlayer player) {
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
				if(null != cardTag && cardTag.getBoolean(ZijingMod.MODID + ":isbind")) {
					return cardTag.getString(ZijingMod.MODID + ":name");
				}
			}
			return null;
		}
		
		public void teleportEntity(int index) {
			if(index > 0 && index < 29 && null != items.get(index) && ItemStack.EMPTY != items.get(index)) {
				NBTTagCompound chuansongCardTag = items.get(index).getTagCompound();
				if(items.get(index).hasTagCompound() && null != chuansongCardTag && chuansongCardTag.getBoolean(ZijingMod.MODID + ":isbind")) {
					if(null != items.get(0) && ItemStack.EMPTY != items.get(0) && items.get(0).getItem() instanceof ItemZilingZhu && items.get(0).hasTagCompound()) {
						NBTTagCompound zhilingZhuTag = items.get(0).getTagCompound();
						if(player.dimension == chuansongCardTag.getInteger(ZijingMod.MODID + ":world") && zhilingZhuTag.getInteger(ZijingMod.MODID + ":magicEnergy") >= 3) {
							double x = chuansongCardTag.getDouble(ZijingMod.MODID + ":lx");
							double y = chuansongCardTag.getDouble(ZijingMod.MODID + ":ly");
							double z = chuansongCardTag.getDouble(ZijingMod.MODID + ":lz");
							player.setPositionAndUpdate(x, y, z);
							player.world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
							zhilingZhuTag.setInteger(ZijingMod.MODID + ":magicEnergy", zhilingZhuTag.getInteger(ZijingMod.MODID + ":magicEnergy") - 3);
							items.get(0).setItemDamage(zhilingZhuTag.getInteger(ZijingMod.MODID + ":maxMagicEnergy") - zhilingZhuTag.getInteger(ZijingMod.MODID + ":magicEnergy"));
						}else if(player.dimension != chuansongCardTag.getInteger(ZijingMod.MODID + ":world")){
							player.sendMessage(new TextComponentString("Not the same world!"));
						}else if(zhilingZhuTag.getInteger(ZijingMod.MODID + ":magicEnergy") < 2){
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
	public static class GuiBookChuansongUseWindow extends GuiContainer {

		public GuiBookChuansongUseWindow(World world, int i, int j, int k, EntityPlayer player) {
			super(new GuiBookChuansongUseContainer(world, i, j, k, player));
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		
		@Override
		public void initGui() {
			super.initGui();
			this.buttonList.clear();
			for(int index = 1; index < 29; index++) {
				String name = ((GuiBookChuansongUseContainer)this.inventorySlots).getCardName(index);
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
					((GuiBookChuansongUseContainer)this.inventorySlots).teleportEntity(index);
					this.mc.player.closeScreen();
				}
			}
		}
	}
}
