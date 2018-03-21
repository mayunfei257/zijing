package com.zijing.util;

import java.util.Collection;

import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.entity.EntityDisciple;
import com.zijing.entity.EntityDisciple.GENDER;
import com.zijing.entity.EntityEvilTaoist;
import com.zijing.entity.EntitySuperIronGolem;
import com.zijing.entity.EntitySuperSnowman;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	public static EntityArrowBingDan shootBingDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage, float slownessProbability, int slownessStrength) {
		EntityArrowBingDan entityDan = new EntityArrowBingDan(thrower.world, thrower, attackDamage, slownessProbability, 80, slownessStrength);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX, targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}

	public static EntityArrowHuoDan shootHuoDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock) {
		EntityArrowHuoDan entityDan = new EntityArrowHuoDan(thrower.world, thrower, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX,targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		entityDan.setFire(5);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}

	public static EntityArrowXukongDan shootXukongDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage) {
		EntityArrowXukongDan entityDan = new EntityArrowXukongDan(thrower.world, thrower, attackDamage);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX, targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}
	
	public static EntityArrowFengyinDan shootFengyinDan(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double targetX, double targetY, double targetZ, float attackDamage) {
		EntityArrowFengyinDan entityDan = new EntityArrowFengyinDan(thrower.world, thrower, attackDamage);
		entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(targetX - entityDan.posX, targetY - entityDan.posY, targetZ - entityDan.posZ, velocity, inaccuracy);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}

	public static void addEffect(EntityLivingBase entityLivingBase, Potion potion, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(potion, durationIn, amplifierIn, false, false));
	}

	public static void removeEffect(EntityLivingBase entityLivingBase) {
		Collection<PotionEffect> potionEffects = entityLivingBase.getActivePotionEffects();
		for(PotionEffect potionEffect:potionEffects) {
			entityLivingBase.removePotionEffect(potionEffect.getPotion());
		}
	}

	public static boolean ImmuneFallDamage() {
		//---
		return true;
	}

	public static EntitySuperSnowman SummonSuperSnowman(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntitySuperSnowman entity = new EntitySuperSnowman(world, baseLevel);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		entity.playLivingSound();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	public static EntitySuperIronGolem summonSuperIronGolem(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntitySuperIronGolem entity = new EntitySuperIronGolem(world, 20);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		entity.playLivingSound();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	public static EntityDisciple summonDisciple(World world, BlockPos blockPos, int baseLevel, GENDER gender, float yaw, float pitch, int distance) {
		EntityDisciple entity = new EntityDisciple(world, baseLevel, gender);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	public static EntityEvilTaoist summonEvilTaoist(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntityEvilTaoist entity = new EntityEvilTaoist(world, baseLevel);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	public static void fireArea(World world, double centerX, double centerY, double centerZ, int rangeX, int rangeY, int rangeZ, float explosionProbability, float explosionStrength, boolean exceptCenter) {
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
	}
	
	public static BlockPos teleportPoint(EntityLivingBase entityLivingBase, World world, BlockPos blockPos) {
		entityLivingBase.setPositionAndUpdate(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return blockPos;
	}
	
	public static BlockPos teleportBlurPoint(EntityLivingBase entityLivingBase, World world, BlockPos blockPos, int blurRange) {
		if(utilCanStand(world, blockPos, true, true)) {
			return teleportPoint(entityLivingBase, world, blockPos);
		}else {
			double distends = blurRange * blurRange + blurRange * blurRange + blurRange * blurRange + 1;
			BlockPos blockPosTem = null;
			for(int i = - blurRange; i <= blurRange; i++) {
				for(int j = - blurRange; j <= blurRange; j++) {
					for(int k = - blurRange; k <= blurRange; k++) {
						double tem = i * i + j * j + k * k;
						if(tem < distends && blockPos.getY() + j > 0 && utilCanStand(world, new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k), true, true)) {
							distends = tem;
							blockPosTem = new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k);
						}
					}
				}
			}
			if(null != blockPosTem) {
				BlockPos blockPosDown = blockPosTem.down();
				Block blockDown = world.getBlockState(blockPosDown).getBlock();
				if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
					world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
				}
				return teleportPoint(entityLivingBase, world, blockPosTem);
			}
			return blockPosTem;
		}
	}
	
	public static BlockPos teleportVerticalBlurPoint(EntityLivingBase entityLivingBase, World world, BlockPos pos, int blurRange) {
		BlockPos blockPos = teleportBlurPoint(entityLivingBase, world, pos, blurRange);
		if(null == blockPos) {
			BlockPos blockPosTem = null;
			for(int j = 256; j > 0; j--) {
				for(int i = - blurRange; i <= blurRange; i++) {
					for(int k = - blurRange; k <= blurRange; k++) {
						if(utilCanStand(world, new BlockPos(pos.getX() + i, j, pos.getZ() + k), true, false)) {
							blockPosTem = new BlockPos(pos.getX() + i, j, pos.getZ() + k);
							BlockPos blockPosDown = blockPosTem.down();
							Block blockDown = world.getBlockState(blockPosDown).getBlock();
							if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
								world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
							}
							return teleportPoint(entityLivingBase, world, blockPosTem);
						}
					}
				}
			}
			return blockPosTem;
		}
		return blockPos;
	}
	
	public static BlockPos teleportUp(EntityLivingBase entityLivingBase, World world, BlockPos pos, boolean checkBedRock) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for(; y <= 513; y++) {
			BlockPos blockPos = new BlockPos(x, y, z);
			if(checkBedRock && world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) { break; }
			if(utilCanStand(world, blockPos, false, false)) {
				BlockPos blockPosDown = blockPos.down();
				Block blockDown = world.getBlockState(blockPosDown).getBlock();
				if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
					world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
				}
				return teleportPoint(entityLivingBase, world, blockPos);
			}
		}
		return null;
	}

	public static BlockPos teleportDown(EntityLivingBase entityLivingBase, World world, BlockPos pos, boolean checkBedRock) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for(; y > 0; y--) {
			BlockPos blockPos = new BlockPos(x, y, z);
			if(checkBedRock && world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) { break; }
			if(utilCanStand(world, blockPos, false, false)) {
				BlockPos blockPosDown = blockPos.down();
				Block blockDown = world.getBlockState(blockPosDown).getBlock();
				if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
					world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
				}
				return teleportPoint(entityLivingBase, world, blockPos);
			}
		}
		return null;
	}

	public static BlockPos RandomTeleport(EntityLivingBase entityLivingBase, World world, BlockPos pos, int blurRange, int distance) {
		double x = pos.getX() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		double y = pos.getY() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		double z = pos.getZ() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		BlockPos baseBlockPos = new BlockPos(x, y < 1 ? 1 : y, z);
		return teleportBlurPoint(entityLivingBase, world, baseBlockPos, blurRange);
	}

	public static BlockPos RandomTeleportFar(EntityLivingBase entityLivingBase, World world, BlockPos pos, int blurRange, int distance) {
		double x = pos.getX() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		double y = 1 + Math.random() * 255;
		double z = pos.getZ() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		BlockPos baseBlockPos = new BlockPos(x, y, z);
		return teleportVerticalBlurPoint(entityLivingBase, world, baseBlockPos, blurRange);
	}


    @SideOnly(Side.CLIENT)
    public static void spawnParticlesBlockPos(BlockPos blockPos, EnumParticleTypes particleType, World world, int particleNum){
        for (int i = 0; i < particleNum; ++i){
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double xCoord = blockPos.getX() - 0.5D + (world.rand.nextFloat() * 2.0F);
            double yCoord = blockPos.getY() + 0.5D + world.rand.nextFloat();
            double zCoord = blockPos.getZ() - 0.5D + (world.rand.nextFloat() * 2.0F);
            world.spawnParticle(particleType, xCoord, yCoord, zCoord, d0, d1, d2);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void spawnParticlesEntity(Entity entity, EnumParticleTypes particleType, World world, int particleNum){
        for (int i = 0; i < particleNum; ++i){
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double xCoord = entity.posX + (world.rand.nextFloat() * entity.width * 2.0F) - entity.width;
            double yCoord = entity.posY + 1.0D + (world.rand.nextFloat() * entity.height);
            double zCoord = entity.posZ + (world.rand.nextFloat() * entity.width * 2.0F) - entity.width;
            world.spawnParticle(particleType, xCoord, yCoord, zCoord, d0, d1, d2);
        }
    }
    
    public static void growBlock(World world, BlockPos pos) {
		IBlockState iBlockState = world.getBlockState(pos);
		iBlockState.getBlock().updateTick(world, pos, iBlockState, world.rand);
    }
	//------Base Skill End--------------------
	

	//------Base Skill Util--------------------

	private static boolean utilCanStand(World world, BlockPos blockPos, boolean canWater, boolean canPortal) {
		if(world.getBlockState(blockPos).getBlock() == Blocks.END_GATEWAY) {
			return true;
		}
		if((world.getBlockState(blockPos).getBlock() == Blocks.AIR
				|| world.getBlockState(blockPos).getBlock() == Blocks.END_GATEWAY
				|| world.getBlockState(blockPos).getBlock() == Blocks.PORTAL
				|| world.getBlockState(blockPos).getBlock() == Blocks.WATER
				|| world.getBlockState(blockPos).getBlock() == Blocks.FLOWING_WATER
			)&&(world.getBlockState(blockPos.up()).getBlock() == Blocks.AIR
				|| world.getBlockState(blockPos.up()).getBlock() == Blocks.END_GATEWAY
				|| world.getBlockState(blockPos.up()).getBlock() == Blocks.PORTAL
				|| world.getBlockState(blockPos.up()).getBlock() == Blocks.WATER
				|| world.getBlockState(blockPos.up()).getBlock() == Blocks.FLOWING_WATER
			)&& world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR) {
			return true;
		}
		return false;
	}
}
