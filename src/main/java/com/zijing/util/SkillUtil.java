package com.zijing.util;

import java.util.Collection;

import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SkillUtil {
	private static final float velocity = 3.0F;
	private static final float inaccuracy = 0.0F;
	
	public static final int MagicSkill_BingDan = 1;
	public static final int MagicSkill_HuoDan = 1;
	public static final int MagicSkill_XukongDan = 2;
	public static final int MagicSkill_FengyinDan = 1;

	public static final int MagicSkill_Levitation = 1;
	public static final int MagicSkill_RemoveEffect = 2;
	public static final int MagicSkill_TeleportUp = 2;
	public static final int MagicSkill_TeleportDown = 2;
	public static final int MagicSkill_FireArea = 3;
	public static final int MagicSkill_ImmuneFallDamage = 2;
	
	public static final int MagicSkill_SummonSnowman = 100;
	public static final int MagicSkill_SummonTaoistPriest = 1000;
	public static final int MagicSkill_RandomTeleport = 5;
	

	//------Base Skill Start--------------------
	
	public static boolean shootBingDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage, float slownessProbability, int slownessStrength) {
    	EntityThrowable entityDan = new EntityArrowBingDan(thrower.world, thrower, attackDamage, slownessProbability, 80, slownessStrength);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX, targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return true;
	}

	public static boolean shootHuoDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock) {
    	EntityThrowable entityDan = new EntityArrowHuoDan(thrower.world, thrower, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX,targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return true;
	}

	public static boolean shootXukongDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage) {
    	EntityThrowable entityDan = new EntityArrowXukongDan(thrower.world, thrower, attackDamage);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX, targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return true;
	}
	
	public static boolean shootFengyinDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage) {
		EntityArrowFengyinDan entityDan = new EntityArrowFengyinDan(thrower.world, thrower, attackDamage);
		entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX, targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return true;
	}

	public static boolean SPEED(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SPEED, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean SLOWNESS(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean HASTE(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.HASTE, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean MINING_FATIGUE(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean STRENGTH(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean INSTANT_HEALTH(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean INSTANT_DAMAGE(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean JUMP_BOOST(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean NAUSEA(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean REGENERATION(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean RESISTANCE(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean FIRE_RESISTANCE(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean WATER_BREATHING(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean INVISIBILITY(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean BLINDNESS(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean NIGHT_VISION(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean HUNGER(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.HUNGER, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean WEAKNESS(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean POISON(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.POISON, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean WITHER(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WITHER, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean HEALTH_BOOST(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean ABSORPTION(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean SATURATION(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SATURATION, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean GLOWING(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.GLOWING, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean LEVITATION(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean LUCK(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.LUCK, durationIn, amplifierIn, false, false));
		return true;
	}
	public static boolean UNLUCK(EntityLivingBase entityLivingBase, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.UNLUCK, durationIn, amplifierIn, false, false));
		return true;
	}

	public static boolean removeEffect(EntityLivingBase entityLivingBase) {
		Collection<PotionEffect> potionEffects = entityLivingBase.getActivePotionEffects();
		for(PotionEffect potionEffect:potionEffects) {
			entityLivingBase.removePotionEffect(potionEffect.getPotion());
		}
		return true;
	}

	public static boolean FireArea() {
		
		return true;
	}

	public static boolean ImmuneFallDamage() {
		
		return true;
	}

	public static boolean SummonSnowman() {
		
		return true;
	}

	public static boolean SummonTaoistPriest() {
		
		return true;
	}
	
	public static boolean teleportPoint() {

		return true;
	}
	
	public static boolean teleportUp() {
		
		return true;
	}

	public static boolean teleportDown() {
		
		return true;
	}

	public static boolean RandomTeleport() {
		
		return true;
	}

	//------Base Skill End--------------------
	

	//------Base Skill Util--------------------
	
}
