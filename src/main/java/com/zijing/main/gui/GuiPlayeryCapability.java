package com.zijing.main.gui;

import java.io.IOException;
import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingEvent;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;
import com.zijing.util.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPlayeryCapability {
	public static final int GUIID = 5;

	public static class MyContainer extends Container {
		private EntityPlayer player;
		private ShepherdCapability shepherdCapability;

		public MyContainer(World world, int i, int j, int k, EntityPlayer player) {
			this.player = player;
			this.shepherdCapability = ShepherdProvider.hasCapabilityFromPlayer(player) ? ShepherdProvider.getCapabilityFromPlayer(player) : null;
			InventoryPlayer inventory = player.inventory;
			for (int m = 0; m < 4; ++m) {
				for (int n = 0; n < 9; ++n) {
					if(m == 0) {
						this.addSlotToContainer(new Slot(inventory, n, 8 + n*18, 181));
					}else {
						this.addSlotToContainer(new Slot(inventory, n + m*9, 8 + n*18, 105 + m*18));
					}
				}
			}
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
		private final ResourceLocation texture = new ResourceLocation(ZijingMod.MODID + ":playerycapabilitygui.png");
		private EntityPlayer player;
		private ShepherdCapability shepherdCapability;
	    /** The old x position of the mouse pointer */
	    private float oldMouseX;
	    /** The old y position of the mouse pointer */
	    private float oldMouseY;
		private DecimalFormat df1;
		private DecimalFormat df2;
		private DecimalFormat df4;
		
		public MyGuiContainer(World world, int i, int j, int k, EntityPlayer player) {
			super(new MyContainer(world, i, j, k, player));
			this.player = player;
			this.shepherdCapability = ShepherdProvider.hasCapabilityFromPlayer(player) ? ShepherdProvider.getCapabilityFromPlayer(player) : null;
			this.xSize = 176;
			this.ySize = 205;
			df1 = new DecimalFormat("#0.0");
			df2 = new DecimalFormat("#0.00");
			df4 = new DecimalFormat("#0.0000");
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(texture);
			this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	        drawEntityOnScreen(this.guiLeft + 33, this.guiTop + 77, 30, this.guiLeft + 33 - this.oldMouseX, this.guiTop + 77 - 50 - this.oldMouseY, this.mc.player);
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	        this.oldMouseX = (float)mouseX;
	        this.oldMouseY = (float)mouseY;
		}
		
		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			if(null != shepherdCapability) {
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.level", new Object[0]) + "LV " + shepherdCapability.getLevel(), 64, 8, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.race", new Object[0]) + shepherdCapability.getRace(), 64, 17, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.blood", new Object[0]) + df1.format(shepherdCapability.getBlood()) + "/" + df1.format(shepherdCapability.getMaxBlood()), 64, 26, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.magic", new Object[0]) + df1.format(shepherdCapability.getMagic()) + "/" + df1.format(shepherdCapability.getMaxMagic()), 64, 35, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.speed", new Object[0]) + df2.format(shepherdCapability.getSpeed()), 64, 44, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.power", new Object[0]) + df2.format(shepherdCapability.getPower()), 64, 53, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.intellect", new Object[0]) + df2.format(shepherdCapability.getIntellect()), 64, 62, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.bloodRestore", new Object[0]) + df4.format(shepherdCapability.getBloodRestore()) + "/T", 64, 71, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.magicRestore", new Object[0]) + df4.format(shepherdCapability.getMagicRestore()) + "/T", 64, 80, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.physicalDefense", new Object[0]) + df2.format(shepherdCapability.getPhysicalDefense()), 64, 89, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.magicDefense", new Object[0]) + df2.format(shepherdCapability.getMagicDefense()), 64, 98, 0xFFAA00);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.needExperience", new Object[0]) + player.experienceTotal + "/" + (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ZijingMod.config.getUPGRADE_NEED_XP_K(), 64, 107, 0xFFAA00);
			}
		}

		@Override
		public void initGui() {
			super.initGui();
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(1, this.guiLeft + 7, this.guiTop + 89, 50, 20, I18n.format(ZijingMod.MODID + ".gui.upGrade", new Object[0])));
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

	    /**
	     * Draws an entity on the screen looking toward the cursor.
	     */
	    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent){
	        GlStateManager.enableColorMaterial();
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float)posX, (float)posY, 50.0F);
	        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
	        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
	        float f = ent.renderYawOffset;
	        float f1 = ent.rotationYaw;
	        float f2 = ent.rotationPitch;
	        float f3 = ent.prevRotationYawHead;
	        float f4 = ent.rotationYawHead;
	        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
	        RenderHelper.enableStandardItemLighting();
	        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
	        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
	        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
	        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
	        ent.rotationYawHead = ent.rotationYaw;
	        ent.prevRotationYawHead = ent.rotationYaw;
	        GlStateManager.translate(0.0F, 0.0F, 0.0F);
	        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
	        rendermanager.setPlayerViewY(180.0F);
	        rendermanager.setRenderShadow(false);
	        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
	        rendermanager.setRenderShadow(true);
	        ent.renderYawOffset = f;
	        ent.rotationYaw = f1;
	        ent.rotationPitch = f2;
	        ent.prevRotationYawHead = f3;
	        ent.rotationYawHead = f4;
	        GlStateManager.popMatrix();
	        RenderHelper.disableStandardItemLighting();
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	        GlStateManager.disableTexture2D();
	        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	    }
	}
}
