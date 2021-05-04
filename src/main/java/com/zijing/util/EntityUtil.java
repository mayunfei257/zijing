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
import net.minecraft.nbt.NBTTagCompound;
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
	public static boolean upPlayerGrade(EntityPlayerMP player) {
    	if(ShepherdProvider.hasCapabilityFromPlayer(player)) {
    		
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
    		int needXP = MathUtil.getNeedXP(shepherdCapability.getLevel());
    		double needMagic = MathUtil.getMaxMagic(shepherdCapability.getLevel());
    		int level = shepherdCapability.getLevel() + 1;
    		
			if(shepherdCapability.getMagic() >= needMagic && player.experienceTotal >= needXP && level <= ZijingMod.config.getMAX_LEVEL()) {
    			int remainingXP = player.experienceTotal - needXP;
    			player.experience = 0.0F;
    			player.experienceLevel = 0;
    			player.experienceTotal = 0;
    			player.addExperience(remainingXP);

    			shepherdCapability.setLevel(level);
    			shepherdCapability.setBlood(shepherdCapability.getBlood());
    			shepherdCapability.setMagic(shepherdCapability.getMagic() - needMagic);
    			shepherdCapability.setMaxBlood(MathUtil.getMaxBlood(level));
    			shepherdCapability.setMaxMagic(MathUtil.getMaxMagic(level));
//    			shepherdCapability.setSpeed(level * 0.1 + 1);
    			shepherdCapability.setAttack(MathUtil.getAttack(level));
//            	shepherdCapability.setIntellect(intellect);
    			shepherdCapability.setBloodRestore(MathUtil.getBloodRestore(level));
    			shepherdCapability.setMagicRestore(MathUtil.getMagicRestore(level));
            	shepherdCapability.setPhysicalDefense(MathUtil.getPhysicalDefense(level));
//            	shepherdCapability.setMagicDefense(magicDefense);
    			player.world.playSound((EntityPlayer) null, player.posX, player.posY + player.getEyeHeight(), player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    			
    			setPlayerAllValue(player, shepherdCapability);
    			setRewards(player, level);
    			ShepherdProvider.updateChangeToClient(player);
			}else {
				player.sendMessage(new TextComponentString("Magic energy or experience is not enough!"));
			}
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
		player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(shepherdCapability.getAttack());
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shepherdCapability.getMaxBlood());
		player.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(shepherdCapability.getPhysicalDefense());
//		player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(shepherdCapability.getPhysicalDefense());
		player.setHealth((float) shepherdCapability.getBlood());
		if(shepherdCapability.getLevel() >= ZijingMod.config.getALLOWFLYING_LEVEL()) {
//			player.capabilities.disableDamage = true;
			player.capabilities.allowFlying = true;
//			player.capabilities.isCreativeMode = true;
		}
		return true;
	}

	/**
	 * Using
	 * @param player
	 * @return
	 */
	public static boolean setRewards(EntityPlayer player, int level) {
		InventoryPlayer inventory = player.inventory;
		ItemStack itenStack = new ItemStack(BaseControl.blockZilingCao, level);
		if(!inventory.addItemStackToInventory(itenStack)) {
			player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, itenStack));
		}
		
		if(level == ZijingMod.config.getREWARDS_LEVEL()) {
			ItemStack zhulingTai = new ItemStack(BaseControl.blockZhulingTai, 1);
			if(!inventory.addItemStackToInventory(zhulingTai)) {
				player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, zhulingTai));
			}
			ItemStack tiandaoGaiwu = new ItemStack(BaseControl.blockTiandaoGaiwu, 1);
			if(!inventory.addItemStackToInventory(tiandaoGaiwu)) {
				player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, tiandaoGaiwu));
			}
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
	public static boolean upEntityGrade(EntityShepherdCapability shepherdEntity) {
		ShepherdCapability shepherdCapability = shepherdEntity.getShepherdCapability();
		int needXP = MathUtil.getNeedXP(shepherdCapability.getLevel());
		int level = shepherdCapability.getLevel() + 1;
		
		if(shepherdEntity.getExperience() >= needXP && level <= ZijingMod.config.getMAX_LEVEL()) {
			shepherdEntity.setExperience(shepherdEntity.getExperience() - needXP);
			shepherdCapability.setLevel(level);
			shepherdCapability.setMaxBlood(MathUtil.getMaxBlood(level));
			shepherdCapability.setMaxMagic(MathUtil.getMaxMagic(level));
			shepherdCapability.setSpeed(level * 0.005D + 0.2D);
			shepherdCapability.setAttack(MathUtil.getAttack(level));
//          shepherdCapability.setIntellect(intellect);
			shepherdCapability.setBloodRestore(MathUtil.getBloodRestore(level));
			shepherdCapability.setMagicRestore(MathUtil.getMagicRestore(level));
        	shepherdCapability.setPhysicalDefense(MathUtil.getPhysicalDefense(level));
//          shepherdCapability.setMagicDefense(magicDefense);
        	shepherdEntity.world.playSound(null, shepherdEntity.posX, shepherdEntity.posY + shepherdEntity.getEyeHeight(), shepherdEntity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);

    		shepherdCapability.setBlood(shepherdCapability.getMaxBlood());
			shepherdCapability.setMagic(shepherdCapability.getMaxMagic());
			shepherdEntity.setNextLevelNeedExperience(MathUtil.getNeedXP(shepherdCapability.getLevel()));
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
