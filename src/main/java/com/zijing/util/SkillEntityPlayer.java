package com.zijing.util;

import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;

public class SkillEntityPlayer extends SkillEntity{

	public static EntityArrowBingDan shootBingDanSkill(EntityPlayer player) {
		EntityArrowBingDan entityDan = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_BingDan || player.isCreative()) {
    		int level = shepherdCapability.getLevel();
        	float attackDamage = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	entityDan = shootBingDan(player, attackDamage, level * SLOWNESS_PROBABILITY_K, (int)(level * SLOWNESS_STRENGTH_K), true);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_BingDan);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_BingDan));
		}
        return entityDan;
	}

	public static EntityArrowHuoDan shootHuoDanSkill(EntityPlayer player, boolean canExplosionOnBlock) {
		EntityArrowHuoDan entityDan = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
		if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_HuoDan || player.isCreative()) {
			int level = shepherdCapability.getLevel();
			float attackDamage =  (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
			entityDan = shootHuoDan(player, attackDamage, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, canExplosionOnBlock, true);
			shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_HuoDan);
		}else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_HuoDan));
		}
		return entityDan;
	}
	public static EntityArrowXukongDan shootXukongDanSkill(EntityPlayer player) {
		EntityArrowXukongDan entityDan = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_XukongDan || player.isCreative()) {
    		int level = shepherdCapability.getLevel();
        	float attackDamage = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	entityDan = shootXukongDan(player, 0, false);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_XukongDan);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_XukongDan));
		}
        return entityDan;
	}
	
	public static EntityArrowFengyinDan shootFengyinDanSkill(EntityPlayer player) {
		EntityArrowFengyinDan entityDan = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_FengyinDan || player.isCreative()) {
    		int level = shepherdCapability.getLevel();
        	float attackDamage = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	entityDan = shootFengyinDan(player, 0, false);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_FengyinDan);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_FengyinDan));
		}
        return entityDan;
	}
	
//	public static List<EntityArrowBingDan> shootBingDanGroupSkill(EntityPlayer player, EntityLivingBase target) {
//		int level = player.getShepherdCapability().getLevel();
//    	float attackDamage =  (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootBingDanGroup(player, target, attackDamage, level * SLOWNESS_PROBABILITY_K, (int)(level * SLOWNESS_STRENGTH_K), 2, 5);
//	}
//
//	public static List<EntityArrowHuoDan> shootHuoDanGroupSkill(EntityPlayer player, EntityLivingBase target, boolean canExplosionOnBlock) {
//		int level = player.getShepherdCapability().getLevel();
//    	float attackDamage =  (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootHuoDanGroup(player, target, attackDamage, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, canExplosionOnBlock, 2, 5);
//	}
//
//	public static List<EntityArrowXukongDan> shootXukongDanGroupSkill(EntityPlayer player, EntityLivingBase target) {
//		int level = player.getShepherdCapability().getLevel();
////    	float attackDamage =  (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootXukongDanGroup(player, target, 0, 2, 5);
//	}
//	
//	public static List<EntityArrowFengyinDan> shootFengyinDanGroupSkill(EntityPlayer player, EntityLivingBase target) {
//		int level = player.getShepherdCapability().getLevel();
////    	float attackDamage =  (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootFengyinDanGroup(player, target, 0, 2, 5);
//	}

	public static void levitationSkill(EntityPlayer player) {
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_Levitation || player.isCreative()) {
        	addEffect(player, MobEffects.LEVITATION, 80, 0);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_Levitation);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_Levitation));
		}
	}

	public static void removeEffectSkill(EntityPlayer player) {
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_RemoveEffect || player.isCreative()) {
    		removeEffect(player);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_RemoveEffect);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_RemoveEffect));
		}
	}

	public static BlockPos teleportUpSkill(EntityPlayer player, BlockPos basePos) {
		BlockPos resultPos = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_TeleportUp || player.isCreative()) {
        	resultPos = teleportUp(player, basePos, true);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_TeleportUp);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_TeleportUp));
		}
        return resultPos;
	}

	public static BlockPos teleportDownSkill(EntityPlayer player, BlockPos basePos) {
		BlockPos resultPos = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_TeleportDown || player.isCreative()) {
        	resultPos = teleportDown(player, basePos, true);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_TeleportDown);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_TeleportDown));
		}
        return resultPos;
	}
	
	public static void firestormSkill(EntityPlayer player) {
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_Firestorm || player.isCreative()) {
    		int level = shepherdCapability.getLevel();
    		BlockPos centerPos = player.getPosition();
    		firestorm(player.world, centerPos, 3, 2, 3, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, true);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_Firestorm);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_Firestorm));
		}
	}
	
	public static void firestormSkill(EntityPlayer player, EntityLivingBase target) {
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_Firestorm || player.isCreative()) {
    		int level = shepherdCapability.getLevel();
    		BlockPos centerPos = target.getPosition();
    		firestorm(player.world, centerPos, 2, 2, 2, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, false);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_Firestorm);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_Firestorm));
		}
	}
	
	public static BlockPos randomTeleportSkill(EntityPlayer player) {
		BlockPos resultPos = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_RandomTeleport || player.isCreative()) {
        	resultPos = randomTeleport(player, 2, 5);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_RandomTeleport);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_RandomTeleport));
		}
        return resultPos;
	}

	public static BlockPos randomTeleportFarSkill(EntityPlayer player) {
		BlockPos resultPos = null;
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_RandomTeleportFar || player.isCreative()) {
        	resultPos = randomTeleportFar(player, 3, 1000);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_RandomTeleportFar);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_RandomTeleportFar));
		}
        return resultPos;
	}
	
	public static void growBlockSkill(EntityPlayer player, BlockPos pos) {
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_GrowBlock || player.isCreative()) {
        	growBlock(player.world, pos);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_GrowBlock);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_GrowBlock));
		}
    }
	
	public static void growAreaBlockSkill(EntityPlayer player, BlockPos pos) {
		ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
        if(null != shepherdCapability && shepherdCapability.getMagic() >= MagicSkill_GrowAreaBlock || player.isCreative()) {
        	growAreaBlock(player.world, pos, 4, 1, 4);
    		shepherdCapability.setMagic(player.isCreative() ? shepherdCapability.getMagic() : shepherdCapability.getMagic() - MagicSkill_GrowAreaBlock);
        }else {
			player.sendMessage(StringUtil.MagicIsNotEnough(MagicSkill_GrowBlock));
		}
    }
}
