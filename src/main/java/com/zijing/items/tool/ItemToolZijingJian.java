package com.zijing.items.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingJian extends ItemSword{
	public static final int effectTick = 60;
	public static final int bloodRestore = 1;
	public static final int xpDrop = 2;

	public ItemToolZijingJian() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingJian");
		setRegistryName(ZijingMod.MODID + ":itemtoolzijingjian");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, effectTick, 4));
		if(attacker.getHealth() + 1 <= attacker.getMaxHealth()) {
			attacker.setHealth(attacker.getHealth() + bloodRestore);
		}
		if(null == attacker.getActivePotionEffect(MobEffects.STRENGTH)) {
			attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, effectTick, 0));
		}
		EntityXPOrb xPOrb = new EntityXPOrb(target.world, target.posX , target.posY, target.posZ, xpDrop);
		target.world.spawnEntity(xPOrb);
		return super.hitEntity(stack, target, attacker);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemToolZijingJian.1", new Object[] {effectTick/20}));
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemToolZijingJian.2", new Object[] {effectTick/20}));
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemToolZijingJian.3", new Object[] {bloodRestore}));
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemToolZijingJian.4", new Object[] {xpDrop}));
	}
}
