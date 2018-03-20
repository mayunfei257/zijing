package com.zijing.util;

import java.util.Collection;

import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.entity.EntityDisciple;
import com.zijing.entity.EntityDisciple.GENDER;
import com.zijing.entity.EntitySuperIronGolem;
import com.zijing.entity.EntitySuperSnowman;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

	public static boolean addEffect(EntityLivingBase entityLivingBase, Potion potion, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(potion, durationIn, amplifierIn, false, false));
		return true;
	}

	public static boolean removeEffect(EntityLivingBase entityLivingBase) {
		Collection<PotionEffect> potionEffects = entityLivingBase.getActivePotionEffects();
		for(PotionEffect potionEffect:potionEffects) {
			entityLivingBase.removePotionEffect(potionEffect.getPotion());
		}
		return true;
	}

	public static boolean ImmuneFallDamage() {
		//---
		return true;
	}

	public static boolean SummonSuperSnowman(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntitySuperSnowman snowman = new EntitySuperSnowman(world, baseLevel);
		snowman.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		snowman.setHomePosAndDistance(blockPos, distance);
		snowman.updataSwordDamageAndArmorValue();
		snowman.playLivingSound();
		world.spawnEntity(snowman);
		return true;
	}

	public static boolean summonSuperIronGolem(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntitySuperIronGolem entity = new EntitySuperIronGolem(world, 20);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		entity.playLivingSound();
		world.spawnEntity(entity);
		return true;
	}

	public static boolean summonDisciple(World world, BlockPos blockPos, int baseLevel, GENDER gender, float yaw, float pitch, int distance) {
		EntityDisciple entity = new EntityDisciple(world, baseLevel, gender);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		world.spawnEntity(entity);
		return true;
	}

	public static boolean summonEvilTaoist() {
		
		return true;
	}

	public static boolean fireArea(World world, double centerX, double centerY, double centerZ, int rangeX, int rangeY, int rangeZ, float explosionProbability, float explosionStrength, boolean exceptCenter) {
		boolean explosionFlag = world.rand.nextFloat() < explosionProbability;
		for(int i = - rangeX; i <= rangeX; i++) {
			for(int j = - rangeY; j <= rangeY; j++) {
				for(int k = - rangeZ; k <= rangeZ; k++) {
					if(exceptCenter && i == 0 && j == 0 && k == 0) { continue; }
					BlockPos blockPos = new BlockPos(centerX + i, centerY + j, centerZ + k);
					if(world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR) {
						if(explosionFlag) {
							world.createExplosion(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), explosionStrength, true);
						}else {
							world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
						}
					}
				}
			}
		}
		world.playSound((EntityPlayer) null, centerX, centerY + 0.5D, centerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.lightning.impact")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		world.playSound((EntityPlayer) null, centerX, centerY + 0.5D, centerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.lightning.thunder")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
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
