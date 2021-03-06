package com.zijing.util;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.itf.EntityShepherdCapability;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
    			shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K() + 0.002D);
    			shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K() + 0.01D);
            	shepherdCapability.setPhysicalDefense((level - 1) * ZijingMod.config.getUPGRADE_PHYSICALDEFENSE_K());
//            	shepherdCapability.setMagicDefense(magicDefense);
    			player.world.playSound((EntityPlayer) null, player.posX, player.posY + player.getEyeHeight(), player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    			
    			setPlayerAllValue(player, shepherdCapability);
    			setRewards(player);
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

	/**
	 * Using
	 * @param player
	 * @return
	 */
	public static boolean setRewards(EntityPlayer player) {
		InventoryPlayer inventory = player.inventory;
		ItemStack itenStack = new ItemStack(BaseControl.blockZilingCao, 1);
		if(!inventory.addItemStackToInventory(itenStack)) {
			player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, itenStack));
		}
		return true;
	}
	//--------------------------------------------upSide = player ----- downSide = EntityHasShepherdCapability----------------------------------------------------------------------
	
	/**
	 * Using
	 * @param entity
	 * @param upLevel
	 * @return
	 */
	public static boolean upEntityGrade(EntityShepherdCapability shepherdEntity, int upLevel) {
    	if(upLevel >= 0) {
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
    			shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K() + 0.002D);
    			shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K() + 0.01D);
            	shepherdCapability.setPhysicalDefense((level - 1) * ZijingMod.config.getUPGRADE_PHYSICALDEFENSE_K());
//            	shepherdCapability.setMagicDefense(magicDefense);
            	shepherdEntity.world.playSound(null, shepherdEntity.posX, shepherdEntity.posY + shepherdEntity.getEyeHeight(), shepherdEntity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);

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
	public static boolean setEntityAllValue(EntityShepherdCapability shepherdEntity) {
    		ShepherdCapability shepherdCapability = shepherdEntity.getShepherdCapability();
    		shepherdEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
    		shepherdEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(shepherdCapability.getSpeed());
    		shepherdEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D + shepherdCapability.getAttack());
    		shepherdEntity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(shepherdCapability.getPhysicalDefense());// + shepherdEntity.getArmorValue()
//    		shepherdEntity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(shepherdCapability.getPhysicalDefense());
    		shepherdEntity.setHealth((float)shepherdCapability.getBlood());
		return true;
	}
	
	/**
	 * Using
	 * @param entity
	 * @return
	 */
	public static boolean setEntityArmorValueAndSwordDamage(EntityShepherdCapability shepherdEntity) {
		ShepherdCapability shepherdCapability = shepherdEntity.getShepherdCapability();
		shepherdEntity.setArmorValue(0);
    	Item item1 = shepherdEntity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem();
    	if(null != item1 && item1 instanceof ItemArmor) {
    		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)item1).damageReduceAmount);
    	}
    	Item item2 = shepherdEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
    	if(null != item2 && item2 instanceof ItemArmor) {
    		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)item2).damageReduceAmount);
    	}
    	Item item3 = shepherdEntity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem();
    	if(null != item3 && item3 instanceof ItemArmor) {
    		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)item3).damageReduceAmount);
    	}
    	Item item4 = shepherdEntity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem();
    	if(null != item4 && item4 instanceof ItemArmor) {
    		shepherdEntity.setArmorValue(shepherdEntity.getArmorValue() + ((ItemArmor)item4).damageReduceAmount);
    	}
    	shepherdEntity.setSwordDamage(0);
    	Item item5 = shepherdEntity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem();
    	if(null != item5 && item5 instanceof ItemSword) {
    		shepherdEntity.setSwordDamage(shepherdEntity.getSwordDamage() + ((ItemSword)item5).getAttackDamage() + 4F);
    	}
		return true;
	}
}
