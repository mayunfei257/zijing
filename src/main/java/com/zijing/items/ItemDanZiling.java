package com.zijing.items;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDanZiling extends ItemFood {
	public ItemDanZiling() {
		super(2, 1F, true);
		setAlwaysEdible();
		setMaxStackSize(64);
		setUnlocalizedName("itemDanZiling");
		setRegistryName(ZijingMod.MODID + ":itemdanziling");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entity) {
		super.onFoodEaten(itemStack, world, entity);
		if (!world.isRemote) {
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 400, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.LUCK, 400, 0));
		}
	}
}