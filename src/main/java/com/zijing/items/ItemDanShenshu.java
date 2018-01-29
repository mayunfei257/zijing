package com.zijing.items;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDanShenshu extends ItemFood {

	public ItemDanShenshu() {
		super(1, 1F, true);
		setAlwaysEdible();
		maxStackSize = 64;
		setUnlocalizedName("itemDanShenshu");
		setRegistryName(ZijingMod.MODID + ":itemdanshenshu");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entity) {
		super.onFoodEaten(itemStack, world, entity);
		if (!world.isRemote) {
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 120, 20));
		}
	}
}