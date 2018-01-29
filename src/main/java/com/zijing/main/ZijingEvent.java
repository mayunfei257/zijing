package com.zijing.main;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ZijingEvent {
	
	@SubscribeEvent
	public void entityFall(LivingFallEvent event) {
		Entity entity = event.getEntity();
		if (!entity.world.isRemote && entity instanceof EntityPlayer) {
			ItemStack mainHand = ((EntityLivingBase) entity).getHeldItemMainhand();
			ItemStack offhand = ((EntityLivingBase) entity).getHeldItemOffhand();
			if((null != mainHand && mainHand.getItem() == BaseControl.itemZilingZhu) || (null != offhand && offhand.getItem() == BaseControl.itemZilingZhu)) {
				event.setDistance(0);
			}
		}
	}

//	@SubscribeEvent
//	public void entityAttack(AttackEntityEvent event) {
//		Entity target = event.getTarget();
//		if (!target.world.isRemote && target instanceof EntityPlayer) {
//			EntityPlayer player = (EntityPlayer)target;
//			Iterable<ItemStack> armorList = player.getArmorInventoryList();
//			for(ItemStack stack: armorList) {
//				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingHelmet) {
//					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 1));
//				}
//				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBody) {
//					player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 60, 1));
//				}
//				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingLegs) {
//					player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 1));
//				}
//				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBoots) {
//					player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 1));
//				}
//			}
//		}
//	}

	@SubscribeEvent
	public void entityAttack(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote && entity instanceof EntityPlayer && event.getAmount() > 0) {
			EntityPlayer player = (EntityPlayer)entity;
			Iterable<ItemStack> armorList = player.getArmorInventoryList();
			for(ItemStack stack: armorList) {
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingHelmet && null == player.getActivePotionEffect(MobEffects.WATER_BREATHING)) {
					player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 60, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBody && null == player.getActivePotionEffect(MobEffects.REGENERATION)) {
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingLegs && null == player.getActivePotionEffect(MobEffects.RESISTANCE)) {
					player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBoots && null == player.getActivePotionEffect(MobEffects.SPEED)) {
					player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 0));
				}
			}
		}
	}
	
}
