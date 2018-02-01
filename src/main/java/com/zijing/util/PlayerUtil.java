package com.zijing.util;

import com.zijing.main.itf.MagicConsumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

public class PlayerUtil {

	public static float getAllFoodlevel(EntityPlayer player) {
		FoodStats playerFoodStats = player.getFoodStats();
		return playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel();
	}
	
	public static boolean minusFoodlevel(EntityPlayer player, float foodConsume) {
		if(null != player && foodConsume != 0) {
			FoodStats playerFoodStats = player.getFoodStats();
			if(playerFoodStats.getSaturationLevel() >= foodConsume) {
				playerFoodStats.setFoodSaturationLevel(playerFoodStats.getSaturationLevel() - foodConsume);
			}else {
				playerFoodStats.setFoodLevel(playerFoodStats.getFoodLevel() - (int)(foodConsume - playerFoodStats.getSaturationLevel()));
				playerFoodStats.setFoodSaturationLevel(0);
			}
			if(playerFoodStats.getFoodLevel() < 0) {
				playerFoodStats.setFoodLevel(0);
			}
		}
		return true;
	}
	
	public static boolean minusMagic(ItemStack itemStack, int magicConsume) {
		if(!itemStack.isEmpty() && itemStack.getItem() instanceof MagicConsumer) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			nbt.setInteger(MagicConsumer.MAGIC_ENERGY_STR, nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR) - magicConsume);
			itemStack.setItemDamage(nbt.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR) - nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR));
		}
		return true;
	}
}
