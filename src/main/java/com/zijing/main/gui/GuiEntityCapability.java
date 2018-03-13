package com.zijing.main.gui;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import com.zijing.ZijingMod;
import com.zijing.entity.EntitySummonTaoistPriest;
import com.zijing.main.BaseControl;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.playerdata.ShepherdCapability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
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

public class GuiEntityCapability {
	public static final int GUIID = 6;

	public static class MyContainer extends Container {
		private EntityLiving shepherdEntity;
		private IInventory entityInv;
		
		public MyContainer(World world, int entityId, EntityPlayer player) {
			Entity entity = player.world.getEntityByID(entityId);
			if(entity instanceof EntityLiving && entity instanceof EntityHasShepherdCapability) {
				this.shepherdEntity = (EntityLiving)entity ;
				this.entityInv = new InventoryBasic(ZijingMod.MODID + ":entityInv", true, 6);
				if(shepherdEntity instanceof EntitySummonTaoistPriest) {
					NonNullList<ItemStack> inventoryArmor = (NonNullList<ItemStack>) shepherdEntity.getArmorInventoryList();
					NonNullList<ItemStack> inventoryHands = (NonNullList<ItemStack>) shepherdEntity.getHeldEquipment();
					for(int n = 0; n < 6; n++){
						if(n < 4) {
							this.entityInv.setInventorySlotContents(n, inventoryArmor.get(n));
						}else {
							this.entityInv.setInventorySlotContents(n, inventoryHands.get(n - 4));
						}
					}
					this.addSlotToContainer(new Slot(entityInv, 3, 7, 81) {
						@Override
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD;
						}
					});
					this.addSlotToContainer(new Slot(entityInv, 2, 25, 81) {
						@Override
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
						}
					});
					this.addSlotToContainer(new Slot(entityInv, 1, 43, 81) {
						@Override
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.LEGS;
						}
					});
					this.addSlotToContainer(new Slot(entityInv, 0, 7, 99) {
						@Override
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET;
						}
					});
					this.addSlotToContainer(new Slot(entityInv, 4, 25, 99) {
						@Override
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && stack.getItem() instanceof ItemSword;
						}
					});
					this.addSlotToContainer(new Slot(entityInv, 5, 43, 99) {
						@Override
						public boolean isItemValid(ItemStack stack) {
							return null != stack && null != stack.getItem() && stack.getItem() == BaseControl.itemZilingZhu;
						}
					});
				}
			}
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
			this.upDateEntityArmor();
			if(this.shepherdEntity instanceof EntityHasShepherdCapability) {
				((EntityHasShepherdCapability)this.shepherdEntity).updataSwordDamageAndArmorValue();
			}
		}
		
		private void upDateEntityArmor() {
			if(null != shepherdEntity && !shepherdEntity.world.isRemote) {
				if(shepherdEntity instanceof EntitySummonTaoistPriest) {
					this.shepherdEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, this.entityInv.getStackInSlot(3));
					this.shepherdEntity.setItemStackToSlot(EntityEquipmentSlot.CHEST, this.entityInv.getStackInSlot(2));
					this.shepherdEntity.setItemStackToSlot(EntityEquipmentSlot.LEGS, this.entityInv.getStackInSlot(1));
					this.shepherdEntity.setItemStackToSlot(EntityEquipmentSlot.FEET, this.entityInv.getStackInSlot(0));
					this.shepherdEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.entityInv.getStackInSlot(4));
					this.shepherdEntity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.entityInv.getStackInSlot(5));
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class MyGuiContainer extends GuiContainer {
		private final ResourceLocation texture = new ResourceLocation(ZijingMod.MODID + ":entitycapabilitygui.png");
		private EntityLiving shepherdEntity;
		private ShepherdCapability shepherdCapability;
		private DecimalFormat df1;
		private DecimalFormat df2;
		private DecimalFormat df4;

		public MyGuiContainer(World world, int entityId, EntityPlayer player) {
			super(new MyContainer(world, entityId, player));
			Entity entity = player.world.getEntityByID(entityId);
			if(entity instanceof EntityLiving && entity instanceof EntityHasShepherdCapability) {
				this.shepherdEntity = (EntityLiving) entity;
				this.shepherdCapability = ((EntityHasShepherdCapability)this.shepherdEntity).getShepherdCapability();
			}
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
	        drawEntityOnScreen(this.guiLeft + 33, this.guiTop + 77, 30, this.shepherdEntity);//7 7 , 58 77
			if(null == this.shepherdEntity || !(this.shepherdEntity instanceof EntitySummonTaoistPriest)) {
				this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 82, 176, 1, 16, 16);
				this.drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 82, 176, 1, 16, 16);
				this.drawTexturedModalRect(this.guiLeft + 44, this.guiTop + 82, 176, 1, 16, 16);
				this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 100, 176, 1, 16, 16);
				this.drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 100, 176, 1, 16, 16);
				this.drawTexturedModalRect(this.guiLeft + 44, this.guiTop + 100, 176, 1, 16, 16);
			}
		}
		
		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			if(null != this.shepherdCapability) {
//				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.name", new Object[0]) + this.shepherdEntity.getCustomNameTag(), 64, 8, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.level", new Object[0]) + "LV" + shepherdCapability.getLevel() + " " + this.shepherdEntity.getCustomNameTag(), 64, 10, 0xFF9933);
//				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.race", new Object[0]) + shepherdCapability.getRace(), 64, 24, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.blood", new Object[0]) + df1.format(shepherdCapability.getBlood()) + "/" + df1.format(shepherdCapability.getMaxBlood()), 64, 20, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.magic", new Object[0]) + df1.format(shepherdCapability.getMagic()) + "/" + df1.format(shepherdCapability.getMaxMagic()), 64, 30, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.speed", new Object[0]) + df2.format(shepherdCapability.getSpeed()), 64, 40, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.power", new Object[0]) + df2.format(shepherdCapability.getPower()), 64, 50, 0xFF9933);
//				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.intellect", new Object[0]) + df2.format(shepherdCapability.getIntellect()), 64, 64, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.physicalDefense", new Object[0]) + df2.format(shepherdCapability.getPhysicalDefense()), 64, 60, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.magicDefense", new Object[0]) + df2.format(shepherdCapability.getMagicDefense()), 64, 70, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.bloodRestore", new Object[0]) + df4.format(shepherdCapability.getBloodRestore()) + "/T", 64, 80, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.magicRestore", new Object[0]) + df4.format(shepherdCapability.getMagicRestore()) + "/T", 64, 90, 0xFF9933);
				this.fontRenderer.drawString(I18n.format(ZijingMod.MODID + ".gui.needExperience", new Object[0]) + ((EntityHasShepherdCapability)shepherdEntity).getExperience() + "/" + ((EntityHasShepherdCapability)shepherdEntity).getNextLevelNeedExperience(), 64, 100, 0xFF9933);
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
	        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);

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

	    public void updateScreen(){
	        super.updateScreen();
	        if (null != this.shepherdEntity && !this.shepherdEntity.isEntityAlive()){
	            this.mc.player.closeScreen();
	        }
	    }
	}
}
