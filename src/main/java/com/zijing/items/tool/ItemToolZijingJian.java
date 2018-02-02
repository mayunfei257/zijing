package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;

public class ItemToolZijingJian extends ItemSword{

	public ItemToolZijingJian() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingJian");
		setRegistryName(ZijingMod.MODID + ":itemtoolzijingjian");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 4));
		if(attacker.getHealth() + 1 <= attacker.getMaxHealth()) {
			attacker.setHealth(attacker.getHealth() + 1);
		}
		if(null == attacker.getActivePotionEffect(MobEffects.STRENGTH))
			attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60, 0));
		return super.hitEntity(stack, target, attacker);
	}
}
