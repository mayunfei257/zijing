package com.zijing.util;

import com.zijing.ZijingMod;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;

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
	
	/**
	 * Using
	 * @param player
	 * @param upLevel
	 * @return
	 */
	public static boolean upGrade(EntityPlayerMP player, int upLevel) {
    	if(ShepherdProvider.hasCapabilityFromPlayer(player) && upLevel >= 0) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
    		int needXP = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ZijingMod.config.getUPGRADE_NEED_XP_K();
    		double needMagic = MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ZijingMod.config.getUPGRADE_NEED_MAGIC_K();
    		int level = shepherdCapability.getLevel() + upLevel;
			if(shepherdCapability.getMagic() >= needMagic && player.experienceTotal >= needXP && level <= ZijingMod.config.getMAX_LEVEL()) {
    			int remainingXP = player.experienceTotal - needXP;
    			player.experience = 0.0F;
    			player.experienceLevel = 0;
    			player.experienceTotal = 0;
    			player.addExperience(remainingXP);

    			shepherdCapability.setLevel(level);
    			shepherdCapability.setBlood(shepherdCapability.getBlood());
    			shepherdCapability.setMagic(shepherdCapability.getMagic() - needMagic);
    			shepherdCapability.setMaxBlood(level * ZijingMod.config.getUPGRADE_MAXBLOOD_K());
    			shepherdCapability.setMaxMagic(level * ZijingMod.config.getUPGRADE_MAXMAGIC_K());
//    			shepherdCapability.setSpeed(level * 0.1 + 1);
    			shepherdCapability.setPower((level - 1) * ZijingMod.config.getUPGRADE_POWER_K());
//            	shepherdCapability.setIntellect(intellect);
    			shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K());
    			shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K());
//            	shepherdCapability.setPhysicalDefense(physicalDefense);
//            	shepherdCapability.setMagicDefense(magicDefense);
    			player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
    			player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D + shepherdCapability.getPower());
    			player.world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    			ShepherdProvider.updateChangeToClient(player);
			}else {
				player.sendMessage(new TextComponentString("Magic energy or experience is not enough!"));
			}
    	}else if(upLevel < 0){
			player.sendMessage(new TextComponentString("Error upLevel! upLevel < 0"));
    	}else {
			player.sendMessage(new TextComponentString("You have not the capability!"));
    	}
		return true;
	}
	
	/**
	 * Using
	 * @param player
	 * @param shepherdCapability
	 * @return
	 */
	public static boolean setAllValueToPlayer(EntityPlayer player, ShepherdCapability shepherdCapability) {
		player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D + shepherdCapability.getPower());
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
		player.setHealth((float) shepherdCapability.getBlood());
		return true;
	}

	
	/**
	 * Using
	 * @param entity
	 * @param upLevel
	 * @return
	 */
	public static boolean upGradeFromEntity(EntityLiving entity, int upLevel) {
    	if(entity instanceof EntityHasShepherdCapability && upLevel >= 0) {
			ShepherdCapability shepherdCapability = ((EntityHasShepherdCapability)entity).getShepherdCapability();
    		int needXP = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ZijingMod.config.getUPGRADE_NEED_XP_K();
    		int level = shepherdCapability.getLevel() + upLevel;
			if(((EntityHasShepherdCapability)entity).getExperience() >= needXP && level <= ZijingMod.config.getMAX_LEVEL()) {
    			((EntityHasShepherdCapability)entity).setExperience(((EntityHasShepherdCapability)entity).getExperience() - needXP);
    			shepherdCapability.setLevel(level);
    			shepherdCapability.setBlood(shepherdCapability.getBlood());
    			shepherdCapability.setMagic(shepherdCapability.getMagic());
    			shepherdCapability.setMaxBlood(level * ZijingMod.config.getUPGRADE_MAXBLOOD_K() * 2);
    			shepherdCapability.setMaxMagic(level * ZijingMod.config.getUPGRADE_MAXMAGIC_K() * 2);
    			shepherdCapability.setSpeed(level * 0.01D + 0.2D);
    			shepherdCapability.setPower((level - 1) * ZijingMod.config.getUPGRADE_POWER_K() * 2);
//            	shepherdCapability.setIntellect(intellect);
    			shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K() * 2);
    			shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K() * 2);
            	shepherdCapability.setPhysicalDefense(level * 0.5D);
//            	shepherdCapability.setMagicDefense(magicDefense);
        		shepherdCapability.setBlood(shepherdCapability.getMaxBlood());
    			entity.world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
 //   			ShepherdProvider.updateChangeToClient(entity);
			}
    	}
		return true;
	}
	
	/**
	 * Using
	 * @param entity
	 * @param shepherdCapability
	 * @return
	 */
	public static boolean setAllValueToEntity(EntityLiving entity, ShepherdCapability shepherdCapability) {
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
		entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(shepherdCapability.getSpeed());
		entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D + shepherdCapability.getPower());
		entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(shepherdCapability.getPhysicalDefense());
		entity.setHealth((float)shepherdCapability.getBlood());
		return true;
	}
}
