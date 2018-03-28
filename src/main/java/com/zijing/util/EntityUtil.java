package com.zijing.util;

import com.zijing.ZijingMod;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.itf.EntityFriendly;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class EntityUtil {

	/**
	 * UnUsing
	 * @param player
	 * @return
	 */
	public static float getPlayerAllFoodlevel(EntityPlayer player) {
		FoodStats playerFoodStats = player.getFoodStats();
		return playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel();
	}
	
	/**
	 * UnUsing
	 * @param player
	 * @param foodConsume
	 * @return
	 */
	public static boolean minusPlayerFoodlevel(EntityPlayer player, float foodConsume) {
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
	
	/**
	 * Using
	 * @param player
	 * @param upLevel
	 * @return
	 */
	public static boolean upPlayerGrade(EntityPlayerMP player, int upLevel) {
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
    			shepherdCapability.setAttack((level - 1) * ZijingMod.config.getUPGRADE_ATTACK_K());
//            	shepherdCapability.setIntellect(intellect);
    			shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K());
    			shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K());
            	shepherdCapability.setPhysicalDefense((level - 1) * ZijingMod.config.getUPGRADE_PHYSICALDEFENSE_K());
//            	shepherdCapability.setMagicDefense(magicDefense);
    			player.world.playSound((EntityPlayer) null, player.posX, player.posY + player.getEyeHeight(), player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    			
    			setPlayerAllValue(player, shepherdCapability);
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
	public static boolean setPlayerAllValue(EntityPlayer player, ShepherdCapability shepherdCapability) {
		player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D + shepherdCapability.getAttack());
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
		player.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(shepherdCapability.getPhysicalDefense());
//		player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(shepherdCapability.getPhysicalDefense());
		player.setHealth((float) shepherdCapability.getBlood());
		return true;
	}

	//--------------------------------------------upSide = player ----- downSide = EntityHasShepherdCapability----------------------------------------------------------------------
	
	/**
	 * Using
	 * @param entity
	 * @param upLevel
	 * @return
	 */
	public static boolean upEntityGrade(EntityLiving entity, int upLevel) {
    	if(entity instanceof EntityFriendly && upLevel >= 0) {
    		EntityFriendly shepherdEntity = (EntityFriendly)entity;
			ShepherdCapability shepherdCapability = shepherdEntity.getShepherdCapability();
    		int needXP = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), upLevel) * ZijingMod.config.getUPGRADE_NEED_XP_K();
    		int level = shepherdCapability.getLevel() + upLevel;
			if(shepherdEntity.getExperience() >= needXP && level <= ZijingMod.config.getMAX_LEVEL()) {
				shepherdEntity.setExperience(shepherdEntity.getExperience() - needXP);
    			shepherdCapability.setLevel(level);
    			shepherdCapability.setBlood(shepherdCapability.getBlood());
    			shepherdCapability.setMagic(shepherdCapability.getMagic());
    			shepherdCapability.setMaxBlood(level * ZijingMod.config.getUPGRADE_MAXBLOOD_K());
    			shepherdCapability.setMaxMagic(level * ZijingMod.config.getUPGRADE_MAXMAGIC_K());
    			shepherdCapability.setSpeed(level * 0.005D + 0.2D);
    			shepherdCapability.setAttack((level - 1) * ZijingMod.config.getUPGRADE_ATTACK_K());
//            	shepherdCapability.setIntellect(intellect);
    			shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K());
    			shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K());
            	shepherdCapability.setPhysicalDefense((level - 1) * ZijingMod.config.getUPGRADE_PHYSICALDEFENSE_K());
//            	shepherdCapability.setMagicDefense(magicDefense);
    			entity.world.playSound(null, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
 //   			ShepherdProvider.updateChangeToClient(entity);

        		shepherdCapability.setBlood(shepherdCapability.getMaxBlood());
    			shepherdCapability.setMagic(shepherdCapability.getMaxMagic());
    			shepherdEntity.setNextLevelNeedExperience((int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ZijingMod.config.getUPGRADE_NEED_XP_K());
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
	public static boolean setEntityAllValue(EntityLiving entity) {
    	if(entity instanceof EntityFriendly) {
    		EntityFriendly shepherdEntity = (EntityFriendly)entity;
    		ShepherdCapability shepherdCapability = shepherdEntity.getShepherdCapability();
    		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
    		entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(shepherdCapability.getSpeed());
    		entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D + shepherdCapability.getAttack());
    		entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(shepherdCapability.getPhysicalDefense());// + shepherdEntity.getArmorValue()
//    		entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(shepherdCapability.getPhysicalDefense());
    		entity.setHealth((float)shepherdCapability.getBlood());
    	}
		return true;
	}
	
	/**
	 * Using
	 * @param entity
	 * @return
	 */
	public static boolean setEntityArmorValueAndSwordDamage(EntityLiving entity) {
    	if(entity instanceof EntityFriendly) {
    		EntityFriendly shepherdEntity = (EntityFriendly)entity;
    		ShepherdCapability shepherdCapability = shepherdEntity.getShepherdCapability();
    		shepherdEntity.setArmorValue(0);
        	ItemStack itemStack1 = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        	if(null != itemStack1.getItem() && itemStack1.getItem() instanceof ItemArmor) {
        		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)itemStack1.getItem()).damageReduceAmount);
        	}
        	ItemStack itemStack2 = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        	if(null != itemStack2.getItem() && itemStack2.getItem() instanceof ItemArmor) {
        		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)itemStack2.getItem()).damageReduceAmount);
        	}
        	ItemStack itemStack3 = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        	if(null != itemStack3.getItem() && itemStack3.getItem() instanceof ItemArmor) {
        		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)itemStack3.getItem()).damageReduceAmount);
        	}
        	ItemStack itemStack4 = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        	if(null != itemStack4.getItem() && itemStack4.getItem() instanceof ItemArmor) {
        		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)itemStack4.getItem()).damageReduceAmount);
        	}
//    		entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(shepherdCapability.getPhysicalDefense() + shepherdEntity.getArmorValue());
    		
        	shepherdEntity.setSwordDamage(0);
        	ItemStack itemStack5 = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        	if(null != itemStack5.getItem() && itemStack5.getItem() instanceof ItemSword) {
        		shepherdEntity.setSwordDamage(shepherdEntity.getSwordDamage() + ((ItemSword)itemStack5.getItem()).getAttackDamage() + 4F);
        	}
//			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND)) {
//				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND)));
//			}
    	}
		return true;
	}
	
	public static boolean checkAndTryMoveToHome(EntityCreature entity) {
		if(entity.hasHome() && entity.getHomePosition() != BlockPos.ORIGIN && (entity.getAttackTarget() == null || entity.getAttackTarget().isDead)) {
			BlockPos pos = entity.getHomePosition();
			float distance = entity.getMaximumHomeDistance();
			if(entity.getDistance(pos.getX(), pos.getY(), pos.getZ()) > distance) {
				entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
				return true;
			}
		}
		return false;
	}
}
