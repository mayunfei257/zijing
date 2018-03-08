package com.zijing.main.gui;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import com.zijing.ZijingMod;
import com.zijing.main.BaseControl;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.playerdata.ShepherdCapability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiEntityTaoistPriest {
	public static final int GUIID = 6;

	public static class MyContainer extends Container {
		private EntityLiving entity;
		private IInventory entityInv;
		
		public MyContainer(World world, EntityLiving entity, EntityPlayer player) {
			this.entity = entity;
			this.entityInv = new InventoryBasic(ZijingMod.MODID + ":entityInv", true, 6);
			NonNullList<ItemStack> inventoryArmor = (NonNullList<ItemStack>) entity.getArmorInventoryList();
			NonNullList<ItemStack> inventoryHands = (NonNullList<ItemStack>) entity.getHeldEquipment();
			for(int n = 0; n < 6; n++){
				if(n < 4) {
					this.entityInv.setInventorySlotContents(n, inventoryArmor.get(n));
				}else {
					this.entityInv.setInventorySlotContents(n, inventoryHands.get(n - 4));
				}
			}
			this.addSlotToContainer(new Slot(entityInv, 3, 7, 81) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD;
				}
			});
			this.addSlotToContainer(new Slot(entityInv, 2, 25, 81) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
				}
			});
			this.addSlotToContainer(new Slot(entityInv, 1, 43, 81) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.LEGS;
				}
			});
			this.addSlotToContainer(new Slot(entityInv, 0, 7, 99) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET;
				}
			});
			this.addSlotToContainer(new Slot(entityInv, 4, 25, 99) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemSword;
				}
			});
			this.addSlotToContainer(new Slot(entityInv, 5, 43, 99) {
				public boolean isItemValid(ItemStack stack) {
					return null != stack && null != stack.getItem() && stack.getItem() == BaseControl.itemZilingZhu;
				}
			});
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
		public boolean canInteractWith(EntityPlayer player) {
			return true;
		}
		
		@Override
		public void onContainerClosed(EntityPlayer player) {
			super.onContainerClosed(player);
			this.entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, this.entityInv.getStackInSlot(3));
			this.entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, this.entityInv.getStackInSlot(2));
			this.entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, this.entityInv.getStackInSlot(1));
			this.entity.setItemStackToSlot(EntityEquipmentSlot.FEET, this.entityInv.getStackInSlot(0));
			this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.entityInv.getStackInSlot(4));
			this.entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.entityInv.getStackInSlot(5));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class MyGuiContainer extends GuiContainer {
		private final ResourceLocation texture = new ResourceLocation(ZijingMod.MODID + ":entitycapabilitygui.png");
		private EntityHasShepherdCapability shepherdEntity;
		private ShepherdCapability shepherdCapability;
		private DecimalFormat df1;
		private DecimalFormat df2;
		private DecimalFormat df4;

		public MyGuiContainer(World world, EntityLiving entity, EntityPlayer player) {
			super(new MyContainer(world, entity, player));
			this.shepherdEntity = (EntityHasShepherdCapability)entity;
			this.shepherdCapability = shepherdEntity.getShepherdCapability();
			df1 = new DecimalFormat("#0.0");
			df2 = new DecimalFormat("#0.00");
			df4 = new DecimalFormat("#0.0000");
			this.xSize = 176;
			this.ySize = 205;
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
			this.mc.getTextureManager().bindTexture(texture);
			this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	        drawEntityOnScreen(this.guiLeft + 33, this.guiTop + 78, 30, (EntityLiving)this.shepherdEntity);//7 7 , 58 77
		}
		
		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			if(null != shepherdCapability) {
//				int tempFontSize = this.fontRenderer.FONT_HEIGHT;
//				this.fontRenderer.FONT_HEIGHT = 7;
				this.fontRenderer.drawString("name: " + ((EntityLiving)this.shepherdEntity).getCustomNameTag(), 65, 8, 0xFF9933);
				this.fontRenderer.drawString("level: " + shepherdCapability.getLevel(), 65, 16, 0xFF9933);
				this.fontRenderer.drawString("race: " + shepherdCapability.getRace(), 65, 24, 0xFF9933);
				this.fontRenderer.drawString("blood: " + df1.format(shepherdCapability.getBlood()) + "/" + df1.format(shepherdCapability.getMaxBlood()), 65, 32, 0xFF9933);
				this.fontRenderer.drawString("magic: " + df1.format(shepherdCapability.getMagic()) + "/" + df1.format(shepherdCapability.getMaxMagic()), 65, 40, 0xFF9933);
				this.fontRenderer.drawString("speed: " + df2.format(shepherdCapability.getSpeed()), 65, 48, 0xFF9933);
				this.fontRenderer.drawString("power: " + df2.format(shepherdCapability.getPower()), 65, 56, 0xFF9933);
				this.fontRenderer.drawString("intellect: " + df2.format(shepherdCapability.getIntellect()), 63, 64, 0xFF9933);
				this.fontRenderer.drawString("bloodRestore: " + df4.format(shepherdCapability.getBloodRestore()) + "/T", 65, 72, 0xFF9933);
				this.fontRenderer.drawString("magicRestore: " + df4.format(shepherdCapability.getMagicRestore()) + "/T", 65, 80, 0xFF9933);
				this.fontRenderer.drawString("physicalDefense: " + df2.format(shepherdCapability.getPhysicalDefense()), 65, 88, 0xFF9933);
				this.fontRenderer.drawString("magicDefense: " + df2.format(shepherdCapability.getMagicDefense()), 65, 96, 0xFF9933);
				this.fontRenderer.drawString("needExperience: " + shepherdEntity.getExperience() + "/" + shepherdEntity.getNextLevelNeedExperience(), 65, 104, 0xFF9933);
//				this.fontRenderer.FONT_HEIGHT = tempFontSize;
			}
		}
	    
	    /**
	     * Draws an entity on the screen looking toward the cursor.
	     */
	    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent){
	        GlStateManager.enableColorMaterial();
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float)posX, (float)posY, 50.0F);
	        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
	        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

	        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
	        RenderHelper.enableStandardItemLighting();
	        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(-20.0F, 1.0F, 0.0F, 0.0F);

	        GlStateManager.translate(0.0F, 0.0F, 0.0F);
	        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
	        rendermanager.setPlayerViewY(180.0F);
	        rendermanager.setRenderShadow(false);
	        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
	        rendermanager.setRenderShadow(true);

	        GlStateManager.popMatrix();
	        RenderHelper.disableStandardItemLighting();
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	        GlStateManager.disableTexture2D();
	        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	    }
	}
}
