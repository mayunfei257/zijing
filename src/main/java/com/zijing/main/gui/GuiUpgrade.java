package com.zijing.main.gui;

import java.io.IOException;
import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingEvent;
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
	    		double needMagic = MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ZijingMod.config.getUPGRADE_NEED_MAGIC_K();
	    		int needXP = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ZijingMod.config.getUPGRADE_NEED_XP_K();
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
		private DecimalFormat df1;
		private DecimalFormat df2;
		private DecimalFormat df4;
		
		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer player) {
			super(new MyContainer(world, i, j, k, player));
			this.player = player;
			shepherdCapability = ShepherdProvider.hasCapabilityFromPlayer(player) ? ShepherdProvider.getCapabilityFromPlayer(player) : null;
			df1 = new DecimalFormat("#0.0");
			df2 = new DecimalFormat("#0.00");
			df4 = new DecimalFormat("#0.0000");
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
				this.fontRenderer.drawString("level: " + shepherdCapability.getLevel(), 0, 0, 0xFFCC33);
				this.fontRenderer.drawString("race: " + shepherdCapability.getRace(), 0, 10, 0xFFCC33);
				this.fontRenderer.drawString("blood: " + df1.format(shepherdCapability.getBlood()) + "/" + df1.format(shepherdCapability.getMaxBlood()), 0, 20, 0xFFCC33);
				this.fontRenderer.drawString("magic: " + df1.format(shepherdCapability.getMagic()) + "/" + df1.format(shepherdCapability.getMaxMagic()), 0, 30, 0xFFCC33);
				this.fontRenderer.drawString("speed: " + df2.format(shepherdCapability.getSpeed()), 0, 40, 0xFFCC33);
				this.fontRenderer.drawString("power: " + df2.format(shepherdCapability.getPower()), 0, 50, 0xFFCC33);
				this.fontRenderer.drawString("intellect: " + df2.format(shepherdCapability.getIntellect()), 0, 60, 0xFFCC33);
				this.fontRenderer.drawString("bloodRestore: " + df4.format(shepherdCapability.getBloodRestore()) + "/T", 0, 70, 0xFFCC33);
				this.fontRenderer.drawString("magicRestore: " + df4.format(shepherdCapability.getMagicRestore()) + "/T", 0, 80, 0xFFCC33);
				this.fontRenderer.drawString("physicalDefense: " + df2.format(shepherdCapability.getPhysicalDefense()), 0, 90, 0xFFCC33);
				this.fontRenderer.drawString("magicDefense: " + df2.format(shepherdCapability.getMagicDefense()), 0, 100, 0xFFCC33);
				this.fontRenderer.drawString("needExperience: " + player.experienceTotal + "/" + (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ZijingMod.config.getUPGRADE_NEED_XP_K(), 0, 110, 0xFFCC33);
			}
		}

		@Override
		public void initGui() {
			super.initGui();
//			guiLabel = new GuiLabel(this.fontRenderer, 1, this.width / 2 - 40, this.height / 2 - 90, 120, 150, 0xffffff);
//			if(null != shepherdCapability) {
//				DecimalFormat df = new DecimalFormat("#0.00");
//				guiLabel.addLine("level:" + shepherdCapability.getLevel());
//				guiLabel.addLine("race:" + shepherdCapability.getRace());
//				guiLabel.addLine("blood:" + df.format(shepherdCapability.getBlood()) + "/" + df.format(shepherdCapability.getMaxBlood()));
//				guiLabel.addLine("magic:" + df.format(shepherdCapability.getMagic()) + "/" + df.format(shepherdCapability.getMaxMagic()));
//				guiLabel.addLine("speed:" + df.format(shepherdCapability.getSpeed()));
//				guiLabel.addLine("power:" + df.format(shepherdCapability.getPower()));
//				guiLabel.addLine("intellect:" + df.format(shepherdCapability.getIntellect()));
//				guiLabel.addLine("bloodRestore:" + df.format(shepherdCapability.getBloodRestore()));
//				guiLabel.addLine("magicRestore:" + df.format(shepherdCapability.getMagicRestore()));
//				guiLabel.addLine("physicalDefense:" + df.format(shepherdCapability.getPhysicalDefense()));
//				guiLabel.addLine("magicDefense:" + df.format(shepherdCapability.getMagicDefense()));
//				guiLabel.addLine("needExperience:" + player.experienceTotal + "/" + (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ShepherdCapability.UPGRADE_NEED_XP_K);
//			}
//			this.labelList.clear();
//			this.labelList.add(guiLabel);
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(1, this.width / 2 - 30, this.height / 2 + 60, 60, 20, "UpGrade"));
		}

		@Override
		protected void actionPerformed(GuiButton button) {
			if (button.id == 1) {
				((MyContainer)this.inventorySlots).upgrade(1);
			}
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException{
			if(ZijingEvent.key1.getKeyCode() == keyCode) {
				this.mc.player.closeScreen();
			}
			super.keyTyped(typedChar, keyCode);
		}
	}
}
