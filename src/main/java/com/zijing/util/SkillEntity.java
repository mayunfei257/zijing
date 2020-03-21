package com.zijing.util;

import java.util.ArrayList;
import java.util.List;

import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class SkillEntity extends SkillBase{
	protected static final int DAN_GROUP_LAUNCH_HEIGHT = 32;
	protected static final int DAN_GROUP_FREQUENCY_TICK = 5;
	
	public static final float EXPLOSION_PROBABILITY_K = 0.01F;
	public static final float EXPLOSION_STRENGTH_K = 0.05F;
	public static final float SLOWNESS_PROBABILITY_K = 0.02F;
	public static final float SLOWNESS_STRENGTH_K = 0.1F;
	public static final float ThousandsFrozen_Range_K = 0.1F;
	public static final float ThousandsFrozen_Range_Base = 3;
	public static final float Firestorm_Range_K = 0.1F;
	public static final float Firestorm_Range_Base = 3F;
	
	public static final int CAN_SHOOT_HUODAN_LEVEL = 15;
	public static final int IMMUNE_FIRE_LEVEL = 30;
	public static final int CAN_LIGHTNING_LEVEL = 45;
	public static final int CAN_EXPLOSION_LEVEL = 60;
	
	public static final int MagicSkill_BingDan = 1;
	public static final int MagicSkill_HuoDan = 1;
	public static final int MagicSkill_XukongDan = 2;
	public static final int MagicSkill_FengyinDan = 1;

	public static final int MagicSkill_Levitation = 1;
	public static final int MagicSkill_RemoveEffect = 2;
	public static final int MagicSkill_TeleportUp = 2;
	public static final int MagicSkill_TeleportDown = 2;
	public static final int MagicSkill_ImmuneFallDamage = 2;
	public static final int MagicSkill_ThousandsFrozen = 5;
	public static final int MagicSkill_Firestorm = 5;
	public static final int MagicSkill_RandomTeleport = 2;
	public static final int MagicSkill_RandomTeleportFar = 5;
	public static final int MagicSkill_GrowBlock = 1;
	public static final int MagicSkill_GrowAreaBlock = 32;
	
	public static final int MagicSkill_SummonSnowman = 100;
	public static final int MagicSkill_SummonTaoistPriest = 1000;

	
	protected static EntityArrowBingDan shootBingDan(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, float slownessProbability, int slownessStrength, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight();
		double targetY = target.getEntityBoundingBox().minY + target.height * 0.7D;
		double vecX = target.posX - thrower.posX;
		double vecY = targetY - throwerY;
		double vecZ = target.posZ - thrower.posZ;
		return shootBingDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vecX, vecY, vecZ, attackDamage, slownessProbability, slownessStrength, checkFaction);
	}

	protected static EntityArrowHuoDan shootHuoDan(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight();
		double targetY = target.getEntityBoundingBox().minY + target.height * 0.7D;
		double vecX = target.posX - thrower.posX;
		double vecY = targetY - throwerY;
		double vecZ = target.posZ - thrower.posZ;
		return shootHuoDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vecX, vecY, vecZ, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock, checkFaction);
	}

	protected static EntityArrowXukongDan shootXukongDan(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight();
		double targetY = target.getEntityBoundingBox().minY + target.height * 0.7D;
		double vecX = target.posX - thrower.posX;
		double vecY = targetY - throwerY;
		double vecZ = target.posZ - thrower.posZ;
		return shootXukongDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vecX, vecY, vecZ, attackDamage, checkFaction);
	}
	
	protected static EntityArrowFengyinDan shootFengyinDan(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight();
		double targetY = target.getEntityBoundingBox().minY + target.height * 0.7D;
		double vecX = target.posX - thrower.posX;
		double vecY = targetY - throwerY;
		double vecZ = target.posZ - thrower.posZ;
		return shootFengyinDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vecX, vecY, vecZ, attackDamage, checkFaction);
	}

	protected static EntityArrowBingDan shootBingDan(EntityLivingBase thrower, float attackDamage, float slownessProbability, int slownessStrength, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight() - 0.10000000149011612D;
		Vec3d vec = thrower.getLookVec();
		return shootBingDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vec.x, vec.y, vec.z, attackDamage, slownessProbability, slownessStrength, checkFaction);
	}

	protected static EntityArrowHuoDan shootHuoDan(EntityLivingBase thrower, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight() - 0.10000000149011612D;
		Vec3d vec = thrower.getLookVec();
		return shootHuoDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vec.x, vec.y, vec.z, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock, checkFaction);
	}

	protected static EntityArrowXukongDan shootXukongDan(EntityLivingBase thrower, float attackDamage, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight() - 0.10000000149011612D;
		Vec3d vec = thrower.getLookVec();
		return shootXukongDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vec.x, vec.y, vec.z, attackDamage, checkFaction);
	}
	
	protected static EntityArrowFengyinDan shootFengyinDan(EntityLivingBase thrower, float attackDamage, boolean checkFaction) {
		double throwerY = thrower.posY + (double)thrower.getEyeHeight() - 0.10000000149011612D;
		Vec3d vec = thrower.getLookVec();
		return shootFengyinDanBase(thrower, thrower.posX, throwerY, thrower.posZ, vec.x, vec.y, vec.z, attackDamage, checkFaction);
	}
	
	protected static List<EntityArrowBingDan> shootBingDanGroup(EntityLivingBase thrower, BlockPos basePos, float attackDamage, float slownessProbability, int slownessStrength, boolean checkFaction, int amount, int frequency) {
    	List<EntityArrowBingDan> entityList = new ArrayList<EntityArrowBingDan>();
    	for(int n = 0; n < amount; n++) {
    		double y = basePos.getY();
    		double x = basePos.getX() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		double z = basePos.getZ() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		BlockPos targetPos = new BlockPos(x, y, z);
    		BlockPos throwerPos = getLaunchBlock(thrower.world, targetPos);
    		double vecX = targetPos.getX() - throwerPos.getX();
    		double vecY = targetPos.getY() - throwerPos.getY();
    		double vecZ = targetPos.getZ() - throwerPos.getZ();
    		entityList.add(shootBingDanBase(thrower, throwerPos.getX(), throwerPos.getY(), throwerPos.getZ(), vecX, vecY, vecZ, attackDamage, slownessProbability, slownessStrength, checkFaction));
    	}
		return entityList;
	}

	protected static List<EntityArrowHuoDan> shootHuoDanGroup(EntityLivingBase thrower, BlockPos basePos, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction, int amount, int frequency) {
    	List<EntityArrowHuoDan> entityList = new ArrayList<EntityArrowHuoDan>();
    	for(int n = 0; n < amount; n++) {
    		double y = basePos.getY();
    		double x = basePos.getX() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		double z = basePos.getZ() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		BlockPos targetPos = new BlockPos(x, y, z);
    		BlockPos throwerPos = getLaunchBlock(thrower.world, targetPos);
    		double vecX = targetPos.getX() - throwerPos.getX();
    		double vecY = targetPos.getY() - throwerPos.getY();
    		double vecZ = targetPos.getZ() - throwerPos.getZ();
    		entityList.add(shootHuoDanBase(thrower, throwerPos.getX(), throwerPos.getY(), throwerPos.getZ(), vecX, vecY, vecZ, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock, checkFaction));
    	}
		return entityList;
	}

	protected static List<EntityArrowXukongDan> shootXukongDanGroup(EntityLivingBase thrower, BlockPos basePos, float attackDamage, boolean checkFaction, int amount, int frequency) {
    	List<EntityArrowXukongDan> entityList = new ArrayList<EntityArrowXukongDan>();
    	for(int n = 0; n < amount; n++) {
    		double y = basePos.getY();
    		double x = basePos.getX() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		double z = basePos.getZ() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		BlockPos targetPos = new BlockPos(x, y, z);
    		BlockPos throwerPos = getLaunchBlock(thrower.world, targetPos);
    		double vecX = targetPos.getX() - throwerPos.getX();
    		double vecY = targetPos.getY() - throwerPos.getY();
    		double vecZ = targetPos.getZ() - throwerPos.getZ();
    		entityList.add(shootXukongDanBase(thrower, throwerPos.getX(), throwerPos.getY(), throwerPos.getZ(), vecX, vecY, vecZ, attackDamage, checkFaction));
    	}
		return entityList;
	}
	
	protected static List<EntityArrowFengyinDan> shootFengyinDanGroup(EntityLivingBase thrower, BlockPos basePos, float attackDamage, boolean checkFaction, int amount, int frequency) {
    	List<EntityArrowFengyinDan> entityList = new ArrayList<EntityArrowFengyinDan>();
    	for(int n = 0; n < amount; n++) {
    		double y = basePos.getY();
    		double x = basePos.getX() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		double z = basePos.getZ() + Math.random() * 3 * (Math.random() < 0.5 ? -1 : 1);
    		BlockPos targetPos = new BlockPos(x, y, z);
    		BlockPos throwerPos = getLaunchBlock(thrower.world, targetPos);
    		double vecX = targetPos.getX() - throwerPos.getX();
    		double vecY = targetPos.getY() - throwerPos.getY();
    		double vecZ = targetPos.getZ() - throwerPos.getZ();
    		entityList.add(shootFengyinDanBase(thrower, throwerPos.getX(), throwerPos.getY(), throwerPos.getZ(), vecX, vecY, vecZ, attackDamage, checkFaction));
    	}
		return entityList;
	}

	protected static List<EntityArrowBingDan> shootBingDanGroup(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, float slownessProbability, int slownessStrength, boolean checkFaction, int amount, int frequency) {
		return shootBingDanGroup(thrower, target.getPosition(), attackDamage, slownessProbability, slownessStrength, checkFaction, amount, frequency);
	}

	protected static List<EntityArrowHuoDan> shootHuoDanGroup(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction, int amount, int frequency) {
		return shootHuoDanGroup(thrower, target.getPosition(), attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock, checkFaction, amount, frequency);
	}

	protected static List<EntityArrowXukongDan> shootXukongDanGroup(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, boolean checkFaction, int amount, int frequency) {
		return shootXukongDanGroup(thrower, target.getPosition(), attackDamage, checkFaction, amount, frequency);
	}
	
	protected static List<EntityArrowFengyinDan> shootFengyinDanGroup(EntityLivingBase thrower, EntityLivingBase target, float attackDamage, boolean checkFaction, int amount, int frequency) {
		return shootFengyinDanGroup(thrower, target.getPosition(), attackDamage, checkFaction, amount, frequency);
	}
	
	protected static void addEffect(EntityLivingBase entityLivingBase, Potion potion, int durationIn, int amplifierIn) {
		addEffectBase(entityLivingBase, potion, durationIn, amplifierIn);
	}

	protected static void removeEffect(EntityLivingBase entityLivingBase) {
		removeEffectBase(entityLivingBase);
	}

	protected static BlockPos teleportUp(EntityLivingBase entityLivingBase, BlockPos basePos, boolean checkBedRock) {
		return teleportUpBase(entityLivingBase, basePos, checkBedRock);
	}

	protected static BlockPos teleportDown(EntityLivingBase entityLivingBase, BlockPos basePos, boolean checkBedRock) {
		return teleportDownBase(entityLivingBase, basePos, checkBedRock);
	}

	protected static void thousandsFrozen(EntityLivingBase entity, World world, BlockPos centerPos, int rangeX, int rangeY, int rangeZ, float slownessProbability, int slownessStrength, float attackDamage) {
		thousandsFrozenBase(entity, world, centerPos.getX(), centerPos.getY(), centerPos.getZ(), rangeX, rangeY, rangeZ, slownessProbability, slownessStrength, attackDamage);
	}
	
	protected static void firestorm(EntityLivingBase entity, World world, BlockPos centerPos, int rangeX, int rangeY, int rangeZ, float explosionProbability, float explosionStrength, boolean exceptCenter, float attackDamage) {
		firestormBase(entity, world, centerPos.getX(), centerPos.getY(), centerPos.getZ(), rangeX, rangeY, rangeZ, explosionProbability, explosionStrength, exceptCenter, attackDamage);
	}

	protected static BlockPos randomTeleport(EntityLivingBase entityLivingBase, int blurRange, int distance) {
		BlockPos pos = entityLivingBase.getPosition();
		double x = pos.getX() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		double y = pos.getY() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		double z = pos.getZ() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		BlockPos baseBlockPos = new BlockPos(x, y < 1 ? 1 : y, z);
		return teleportBlurPointBase(entityLivingBase, baseBlockPos, blurRange);
	}

	protected static BlockPos randomTeleportFar(EntityLivingBase entityLivingBase, int blurRange, int distance) {
		BlockPos pos = entityLivingBase.getPosition();
		double x = pos.getX() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		double y = 1 + Math.random() * 255;
		double z = pos.getZ() + Math.random() * distance * (Math.random() < 0.5 ? -1 : 1);
		BlockPos baseBlockPos = new BlockPos(x, y, z);
		return teleportVerticalBlurPointBase(entityLivingBase, baseBlockPos, blurRange);
	}
	
	protected static void growBlock(World world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock() instanceof IPlantable) {
			for(;world.getBlockState(pos.up()).getBlock() instanceof IPlantable;) {
				pos = pos.up();
			}
		}
		growBlockBase(world, pos);
    }
	
	protected static void growAreaBlock(World world, BlockPos pos, int rangeX, int rangeY, int rangeZ) {
		for(int i = - rangeX; i <= rangeX; i++) {
			for(int k = - rangeZ; k <= rangeZ; k++) {
				for(int j = - rangeY; j <= rangeY; j++) {
					BlockPos theBlockPos = new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
					if(world.getBlockState(theBlockPos).getBlock() instanceof IPlantable) {
						for(;world.getBlockState(theBlockPos.up()).getBlock() instanceof IPlantable;) {
							theBlockPos = theBlockPos.up();
						}
						growBlockBase(world, theBlockPos);
						break;
					}else {
						growBlockBase(world, theBlockPos);
					}
				}
			}
		}
    }
	
	protected static int chainDrop(ItemStack toolStack, World world, IBlockState blockState, BlockPos pos, int maxAmount) {
		if(canChainDrop(toolStack, blockState)) {
			return chainDropBlockBase(world, blockState, pos, 0, maxAmount);
		}
		return 0;
	}
	//TODO------Skill Util--------------------
    private static BlockPos getLaunchBlock(World world, BlockPos targetPos) {
		int x = targetPos.getX();
		int y = targetPos.getY();
		int z = targetPos.getZ();
		BlockPos airPos = null;
		for(int n = y; n < WORLD_MAX_HIGHT; n++) {
			BlockPos tempPos = new BlockPos(x, y + n, z);
			if(world.getBlockState(tempPos).getBlock() == Blocks.AIR) {
				airPos = tempPos;
				break;
			}
		}
		BlockPos throwerPos = new BlockPos(x, y + 1, z);
		if(null != airPos) {
			for(int n = 1; n <= DAN_GROUP_LAUNCH_HEIGHT; n++) {
				if(world.getBlockState(new BlockPos(airPos.getX(), airPos.getY() + n, airPos.getZ())).getBlock() != Blocks.AIR) {
					throwerPos = new BlockPos(airPos.getX(), airPos.getY() + n - 1, airPos.getZ());
					break;
				}else if(n == DAN_GROUP_LAUNCH_HEIGHT){
					throwerPos = new BlockPos(airPos.getX(), airPos.getY() + DAN_GROUP_LAUNCH_HEIGHT, airPos.getZ());
				}
			}
		}
		return throwerPos;
    }
    
    private static boolean canChainDrop(ItemStack toolStack, IBlockState blockState) {
    	if(null == toolStack || null == toolStack.getItem() || null == blockState)
    		return false;
		if(toolStack.getItem().getDestroySpeed(toolStack, blockState) > 1.0F)
			return true;
		
    	if(toolStack.getItem() instanceof ItemSword) {
    		
		}else if(toolStack.getItem() instanceof ItemAxe) {
			
		}else if(toolStack.getItem() instanceof ItemPickaxe) {
			
		}else if(toolStack.getItem() instanceof ItemSpade) {
			
		}else if(toolStack.getItem() instanceof ItemHoe) {
			
		}
    	return false;
    }
}
