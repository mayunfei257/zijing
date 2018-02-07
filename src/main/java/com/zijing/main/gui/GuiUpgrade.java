package com.zijing.main.gui;

import org.lwjgl.opengl.GL11;

import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;
import com.zijing.util.MathUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUpgrade {
	public static final int GUIID = 5;

	public static class MyContainer extends Container {
		private EntityPlayer player;
		private ShepherdCapability shepherdCapability;

		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.player = player;
			shepherdCapability = ShepherdProvider.hasCapabilityFromPlayer(player) ? ShepherdProvider.getCapabilityFromPlayer(player) : null;
		}

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {return true;}

		public void upgrade(int upLevel){
			if(null != shepherdCapability) {
	    		double needMagic = MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ShepherdCapability.UPGRADE_NEED_MAGIC_K;
	    		int needXP = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ShepherdCapability.UPGRADE_NEED_XP_K;
				if(shepherdCapability.getMagic() >= needMagic) {
					if(player.experienceTotal >= needXP) {
						if(player.world.isRemote) {
							ShepherdProvider.upgradeToServer(player, upLevel);
						}
					}else {
						player.sendMessage(new TextComponentString("Experience is not enough, need at least " + needXP));
					}
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + needMagic));
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)	//TODO
	public static class MyGuiContainer extends GuiContainer {
		private EntityPlayer player;
		private ShepherdCapability shepherdCapability;
		private GuiLabel guiLabel;

		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer player) {
			super(new MyContainer(world, i, j, k, player));
			this.player = player;
			shepherdCapability = ShepherdProvider.hasCapabilityFromPlayer(player) ? ShepherdProvider.getCapabilityFromPlayer(player) : null;
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
			this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
		}

		@Override
		public void updateScreen() {
			super.updateScreen();
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			if(null != shepherdCapability) {
//				this.fontRenderer.drawString("level:" + shepherdCapability.getLevel(), this.width / 2 - 40, this.height / 2 - 90, 0xffffff);
//				this.fontRenderer.drawString("race:" + shepherdCapability.getRace(), this.width / 2 - 40, this.height / 2 - 75, 0xffffff);
//				this.fontRenderer.drawString("blood:" + shepherdCapability.getBlood() + "/" + shepherdCapability.getMaxBlood(), this.width / 2 - 60, this.height / 2 - 80, 0xffffff);
//				this.fontRenderer.drawString("magic:" + shepherdCapability.getMagic() + "/" + shepherdCapability.getMaxMagic(), this.width / 2 - 45, this.height / 2 - 60, 0xffffff);
//				this.fontRenderer.drawString("speed:" + shepherdCapability.getSpeed(), this.width / 2 - 40, this.height / 2 - 30, 0xffffff);
//				this.fontRenderer.drawString("power:" + shepherdCapability.getPower(), this.width / 2 - 40, this.height / 2 - 15, 0xffffff);
//				this.fontRenderer.drawString("intellect:" + shepherdCapability.getIntellect(), this.width / 2 - 40, this.height / 2 + 0, 0xffffff);
//				this.fontRenderer.drawString("bloodRestore:" + shepherdCapability.getBloodRestore(), this.width / 2 - 40, this.height / 2 + 15, 0xffffff);
//				this.fontRenderer.drawString("magicRestore:" + shepherdCapability.getMagicRestore(), this.width / 2 - 40, this.height / 2 + 30, 0xffffff);
//				this.fontRenderer.drawString("physicalDefense:" + shepherdCapability.getPhysicalDefense(), this.width / 2 - 40, this.height / 2 + 45, 0xffffff);
//				this.fontRenderer.drawString("magicDefense:" + shepherdCapability.getMagicDefense(), this.width / 2 - 40, this.height / 2 + 60, 0xffffff);
			}
		}

		@Override
		public void initGui() {
			super.initGui();
			guiLabel = new GuiLabel(this.fontRenderer, 1, this.width / 2 - 40, this.height / 2 - 90, 120, 150, 0xffffff);
			if(null != shepherdCapability) {
				guiLabel.addLine("level:" + shepherdCapability.getLevel());
				guiLabel.addLine("race:" + shepherdCapability.getRace());
				guiLabel.addLine("blood:" + shepherdCapability.getBlood() + "/" + shepherdCapability.getMaxBlood());
				guiLabel.addLine("magic:" + shepherdCapability.getMagic() + "/" + shepherdCapability.getMaxMagic());
				guiLabel.addLine("speed:" + shepherdCapability.getSpeed());
				guiLabel.addLine("power:" + shepherdCapability.getPower());
				guiLabel.addLine("intellect:" + shepherdCapability.getIntellect());
				guiLabel.addLine("bloodRestore:" + shepherdCapability.getBloodRestore());
				guiLabel.addLine("magicRestore:" + shepherdCapability.getMagicRestore());
				guiLabel.addLine("physicalDefense:" + shepherdCapability.getPhysicalDefense());
				guiLabel.addLine("magicDefense:" + shepherdCapability.getMagicDefense());
			}
			this.labelList.clear();
			this.labelList.add(guiLabel);
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(1, this.width / 2 - 30, this.height / 2 + 60, 60, 20, "Upgrade"));
		}

		@Override
		protected void actionPerformed(GuiButton button) {
			if (button.id == 1) {
				((MyContainer)this.inventorySlots).upgrade(1);
			}
		}
	}
}
