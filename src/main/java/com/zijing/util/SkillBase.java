package com.zijing.util;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
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
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkillBase {
	protected static final int WORLD_MAX_HIGHT = 512;
	protected static final float VELOCITY = 3.0F;
	protected static final float INACCURACY = 0.0F;

	//TODO ------Base Skill Start--------------------
	protected static EntityArrowBingDan shootBingDanBase(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double vecX, double vecY, double vecZ, float attackDamage, float slownessProbability, int slownessStrength, boolean checkFaction) {
		EntityArrowBingDan entityDan = new EntityArrowBingDan(thrower.world, thrower, attackDamage, slownessProbability, 80, slownessStrength, checkFaction);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(vecX, vecY, vecZ, VELOCITY, INACCURACY);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}

	protected static EntityArrowHuoDan shootHuoDanBase(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double vecX, double vecY, double vecZ, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		EntityArrowHuoDan entityDan = new EntityArrowHuoDan(thrower.world, thrower, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock, checkFaction);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(vecX, vecY, vecZ, VELOCITY, INACCURACY);
		entityDan.setFire(5);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}

	protected static EntityArrowXukongDan shootXukongDanBase(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double vecX, double vecY, double vecZ, float attackDamage, boolean checkFaction) {
		EntityArrowXukongDan entityDan = new EntityArrowXukongDan(thrower.world, thrower, attackDamage, checkFaction);
    	entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(vecX, vecY, vecZ, VELOCITY, INACCURACY);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}
	
	protected static EntityArrowFengyinDan shootFengyinDanBase(EntityLivingBase thrower, double throwerX, double throwerY, double throwerZ, double vecX, double vecY, double vecZ, float attackDamage, boolean checkFaction) {
		EntityArrowFengyinDan entityDan = new EntityArrowFengyinDan(thrower.world, thrower, attackDamage, checkFaction);
		entityDan.setPosition(throwerX, throwerY, throwerZ);
		entityDan.shoot(vecX, vecY, vecZ, VELOCITY, INACCURACY);
		thrower.world.spawnEntity(entityDan);
		thrower.world.playSound((EntityPlayer) null, throwerX, throwerY, throwerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entityDan;
	}

	protected static void addEffectBase(EntityLivingBase entityLivingBase, Potion potion, int durationIn, int amplifierIn) {
		entityLivingBase.addPotionEffect(new PotionEffect(potion, durationIn, amplifierIn, false, false));
	}
	protected static void addEffectBase(List<EntityLivingBase> entityLivingBaseList, Potion potion, int durationIn, int amplifierIn) {
		for(EntityLivingBase entityLivingBase: entityLivingBaseList) {
			addEffectBase(entityLivingBase, potion, durationIn, amplifierIn);
		}
	}
	protected static void removeEffectBase(EntityLivingBase entityLivingBase) {
		Collection<PotionEffect> potionEffects = entityLivingBase.getActivePotionEffects();
		if(null != potionEffects && potionEffects.size() > 0) {
			for(PotionEffect potionEffect: potionEffects) {
				entityLivingBase.removePotionEffect(potionEffect.getPotion());
			}
		}
	}
	protected static void removeEffectBase(List<EntityLivingBase> entityLivingBaseList) {
		for(EntityLivingBase entityLivingBase: entityLivingBaseList) {
			removeEffectBase(entityLivingBase);
		}
	}

	protected static boolean ImmuneFallDamage() {
		//---
		return true;
	}

	protected static EntitySuperSnowman SummonSuperSnowmanBase(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntitySuperSnowman entity = new EntitySuperSnowman(world, baseLevel);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		entity.playLivingSound();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	protected static EntitySuperIronGolem summonSuperIronGolemBase(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntitySuperIronGolem entity = new EntitySuperIronGolem(world, 20);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		entity.playLivingSound();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	protected static EntityDisciple summonDiscipleBase(World world, BlockPos blockPos, int baseLevel, GENDER gender, float yaw, float pitch, int distance) {
		EntityDisciple entity = new EntityDisciple(world, baseLevel, gender);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	protected static EntityEvilTaoist summonEvilTaoistBase(World world, BlockPos blockPos, int baseLevel, float yaw, float pitch, int distance) {
		EntityEvilTaoist entity = new EntityEvilTaoist(world, baseLevel);
		entity.setLocationAndAngles(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, yaw, pitch);
		entity.setHomePosAndDistance(blockPos, distance);
		entity.updataSwordDamageAndArmorValue();
		world.spawnEntity(entity);
		world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return entity;
	}

	protected static void firestormBase(World world, int centerX, int centerY, int centerZ, int rangeX, int rangeY, int rangeZ, float explosionProbability, float explosionStrength, boolean exceptCenter) {
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
						world.spawnEntity(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), true));
					}
				}
			}
		}
		world.playSound((EntityPlayer) null, centerX, centerY + 0.5D, centerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.lightning.impact")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		world.playSound((EntityPlayer) null, centerX, centerY + 0.5D, centerZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.lightning.thunder")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
	}
	
	protected static BlockPos teleportPointBase(EntityLivingBase entityLivingBase, BlockPos blockPos) {
		entityLivingBase.setPositionAndUpdate(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
		entityLivingBase.world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return blockPos;
	}
	
	protected static BlockPos teleportBlurPointBase(EntityLivingBase entityLivingBase, BlockPos blockPos, int blurRange) {
		World world = entityLivingBase.world;
		if(utilCanStand(world, blockPos, true, true)) {
			return teleportPointBase(entityLivingBase, blockPos);
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
				return teleportPointBase(entityLivingBase, blockPosTem);
			}
			return blockPosTem;
		}
	}
	
	protected static BlockPos teleportVerticalBlurPointBase(EntityLivingBase entityLivingBase, BlockPos pos, int blurRange) {
		BlockPos blockPos = teleportBlurPointBase(entityLivingBase, pos, blurRange);
		if(null == blockPos) {
			BlockPos blockPosTem = null;
			World world = entityLivingBase.world;
			for(int j = WORLD_MAX_HIGHT; j > 0; j--) {
				for(int i = - blurRange; i <= blurRange; i++) {
					for(int k = - blurRange; k <= blurRange; k++) {
						if(utilCanStand(world, new BlockPos(pos.getX() + i, j, pos.getZ() + k), true, false)) {
							blockPosTem = new BlockPos(pos.getX() + i, j, pos.getZ() + k);
							BlockPos blockPosDown = blockPosTem.down();
							Block blockDown = world.getBlockState(blockPosDown).getBlock();
							if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
								world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
							}
							return teleportPointBase(entityLivingBase, blockPosTem);
						}
					}
				}
			}
			return blockPosTem;
		}
		return blockPos;
	}
	
	protected static BlockPos teleportUpBase(EntityLivingBase entityLivingBase, BlockPos basePos, boolean checkBedRock) {
		World world = entityLivingBase.world;
		int x = basePos.getX();
		int y = basePos.getY();
		int z = basePos.getZ();
		for(; y <= WORLD_MAX_HIGHT; y++) {
			BlockPos blockPos = new BlockPos(x, y, z);
			if(checkBedRock && world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) { break; }
			if(utilCanStand(world, blockPos, false, false)) {
				BlockPos blockPosDown = blockPos.down();
				Block blockDown = world.getBlockState(blockPosDown).getBlock();
				if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
					world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
				}
				return teleportPointBase(entityLivingBase, blockPos);
			}
		}
		return null;
	}

	protected static BlockPos teleportDownBase(EntityLivingBase entityLivingBase, BlockPos basePos, boolean checkBedRock) {
		World world = entityLivingBase.world;
		int x = basePos.getX();
		int y = basePos.getY();
		int z = basePos.getZ();
		for(; y > 0; y--) {
			BlockPos blockPos = new BlockPos(x, y, z);
			if(checkBedRock && world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) { break; }
			if(utilCanStand(world, blockPos, false, false)) {
				BlockPos blockPosDown = blockPos.down();
				Block blockDown = world.getBlockState(blockPosDown).getBlock();
				if(blockDown == Blocks.LAVA || blockDown == Blocks.FLOWING_LAVA) {
					world.setBlockState(blockPosDown, Blocks.STONE.getDefaultState());
				}
				return teleportPointBase(entityLivingBase, blockPos);
			}
		}
		return null;
	}
    
	protected static void growBlockBase(World world, BlockPos pos) {
		IBlockState iBlockState = world.getBlockState(pos);
		iBlockState.getBlock().updateTick(world, pos, iBlockState, world.rand);
		world.playSound((EntityPlayer) null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    protected static void spawnParticlesBlockPosBase(BlockPos blockPos, EnumParticleTypes particleType, World world, int particleNum){
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
    protected static void spawnParticlesEntityBase(Entity entity, EnumParticleTypes particleType, World world, int particleNum){
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
    protected static void setBlockStateBase(World world, IBlockState blockState, BlockPos blockPos) {
    	if(blockState.getBlock().canPlaceBlockAt(world, blockPos)) {
        	world.setBlockState(blockPos, blockState);
    	}
    }
    protected static void setAreaBlockStateBase(World world, IBlockState blockState, BlockPos centerBlockPos, int radiusX, int radiusY, int radiusZ) {
    	for(int x = -radiusX; x <= radiusX; x++) {
    		for(int y = -radiusY; y <= radiusY; y++) {
    			for(int z = -radiusZ; z <= radiusZ; z++) {
    				setBlockStateBase(world, blockState, new BlockPos(centerBlockPos.getX() + x, centerBlockPos.getY() + y, centerBlockPos.getZ() + z));
    			}
    		}
    	}
    }
    protected static List<Entity> getEntitiesBase(World world, Entity entityIn, AxisAlignedBB boundingBox, Predicate <? super Entity > predicate){
    	return world.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
	//------Base Skill End--------------------
	

	//TODO------Base Skill Util--------------------
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
