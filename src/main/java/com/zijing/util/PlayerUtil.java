package com.zijing.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public class PlayerUtil {

	public static float getAllFoodlevel(EntityPlayer player) {
		FoodStats playerFoodStats = player.getFoodStats();
		return playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel();
	}
	
	public static boolean minusFoodlevel(EntityPlayer player, float foodConsume) {
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
		return true;
	}
}