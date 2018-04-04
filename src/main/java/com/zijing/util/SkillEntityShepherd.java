package com.zijing.util;

import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.itf.EntityShepherdCapability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;

public class SkillEntityShepherd extends SkillEntity{

	public static EntityArrowBingDan shootBingDanSkill(EntityShepherdCapability thrower, EntityLivingBase target) {
		EntityArrowBingDan entityDan = null;
		ShepherdCapability shepherdCapability = thrower.getShepherdCapability();
        if(shepherdCapability.getMagic() >= MagicSkill_BingDan) {
    		int level = shepherdCapability.getLevel();
        	float attackDamage = (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	entityDan = shootBingDan(thrower, target, attackDamage, level * SLOWNESS_PROBABILITY_K, (int)(level * SLOWNESS_STRENGTH_K), true);
    		shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill_BingDan);
        }
        return entityDan;
	}

	public static EntityArrowHuoDan shootHuoDanSkill(EntityShepherdCapability thrower, EntityLivingBase target, boolean canExplosionOnBlock) {
		EntityArrowHuoDan entityDan = null;
		ShepherdCapability shepherdCapability = thrower.getShepherdCapability();
		if(shepherdCapability.getMagic() >= MagicSkill_HuoDan) {
			int level = shepherdCapability.getLevel();
			float attackDamage =  (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
			entityDan = shootHuoDan(thrower, target, attackDamage, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, canExplosionOnBlock, true);
			shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill_HuoDan);
		}
		return entityDan;
	}
	public static EntityArrowXukongDan shootXukongDanSkill(EntityShepherdCapability thrower, EntityLivingBase target) {
		EntityArrowXukongDan entityDan = null;
		ShepherdCapability shepherdCapability = thrower.getShepherdCapability();
        if(shepherdCapability.getMagic() >= MagicSkill_XukongDan) {
    		int level = shepherdCapability.getLevel();
        	float attackDamage = (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	entityDan = shootXukongDan(thrower, target, 0, true);
    		shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill_XukongDan);
        }
        return entityDan;
	}
	
	public static EntityArrowFengyinDan shootFengyinDanSkill(EntityShepherdCapability thrower, EntityLivingBase target) {
		EntityArrowFengyinDan entityDan = null;
		ShepherdCapability shepherdCapability = thrower.getShepherdCapability();
        if(shepherdCapability.getMagic() >= MagicSkill_FengyinDan) {
    		int level = shepherdCapability.getLevel();
        	float attackDamage = (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	entityDan = shootFengyinDan(thrower, target, 0, true);
    		shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill_FengyinDan);
        }
        return entityDan;
	}
	
//	public static List<EntityArrowBingDan> shootBingDanGroupSkill(EntityShepherdCapability thrower, EntityLivingBase target) {
//		int level = thrower.getShepherdCapability().getLevel();
//    	float attackDamage =  (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootBingDanGroup(thrower, target, attackDamage, level * SLOWNESS_PROBABILITY_K, (int)(level * SLOWNESS_STRENGTH_K), 2, 5);
//	}
//
//	public static List<EntityArrowHuoDan> shootHuoDanGroupSkill(EntityShepherdCapability thrower, EntityLivingBase target, boolean canExplosionOnBlock) {
//		int level = thrower.getShepherdCapability().getLevel();
//    	float attackDamage =  (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootHuoDanGroup(thrower, target, attackDamage, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, canExplosionOnBlock, 2, 5);
//	}
//
//	public static List<EntityArrowXukongDan> shootXukongDanGroupSkill(EntityShepherdCapability thrower, EntityLivingBase target) {
//		int level = thrower.getShepherdCapability().getLevel();
////    	float attackDamage =  (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootXukongDanGroup(thrower, target, 0, 2, 5);
//	}
//	
//	public static List<EntityArrowFengyinDan> shootFengyinDanGroupSkill(EntityShepherdCapability thrower, EntityLivingBase target) {
//		int level = thrower.getShepherdCapability().getLevel();
////    	float attackDamage =  (float)thrower.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
//		return shootFengyinDanGroup(thrower, target, 0, 2, 5);
//	}
	
	public static void firestormSkill(EntityShepherdCapability thrower, int range) {
		ShepherdCapability shepherdCapability = thrower.getShepherdCapability();
        if(shepherdCapability.getMagic() >= MagicSkill_Firestorm) {
    		int level = shepherdCapability.getLevel();
    		BlockPos centerPos = thrower.getPosition();
    		firestorm(thrower.world, centerPos, 3, 2, 3, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, true);
    		shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill_Firestorm);
        }
	}
	
	public static void firestormSkill(EntityShepherdCapability thrower, EntityLivingBase target, int range) {
		ShepherdCapability shepherdCapability = thrower.getShepherdCapability();
        if(shepherdCapability.getMagic() >= MagicSkill_Firestorm) {
    		int level = shepherdCapability.getLevel();
    		BlockPos centerPos = target.getPosition();
    		firestorm(thrower.world, centerPos, 2, 2, 2, level * EXPLOSION_PROBABILITY_K, level * EXPLOSION_STRENGTH_K, false);
    		shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill_Firestorm);
        }
	}
}
