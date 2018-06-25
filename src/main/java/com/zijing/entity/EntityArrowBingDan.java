package com.zijing.entity;

import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowBingDan extends EntityThrowable {
	private float attackDamage = 4;
	private float slownessProbability = 0.125F;
	private int slownessStrength = 2;
	private int slownessTick = 80;
	private boolean checkFaction = false;

	public EntityArrowBingDan(World a) {
		super(a);
	}

	public EntityArrowBingDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
	
	public EntityArrowBingDan(World worldIn, double x, double y, double z, float attackDamage, float slownessProbability, int slownessStrength) {
		this(worldIn, x, y, z);
		this.attackDamage = attackDamage;
		this.slownessProbability = slownessProbability;
		this.slownessStrength = slownessStrength;
	}
	
	public EntityArrowBingDan(World worldIn, double x, double y, double z, float attackDamage, float slownessProbability, int slownessStrength, boolean checkFaction) {
		this(worldIn, x, y, z, attackDamage, slownessProbability, slownessStrength);
		this.checkFaction = checkFaction;
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter, float attackDamage, float slownessProbability, int slownessTick, int slownessStrength) {
		this(worldIn, shooter);
		this.attackDamage = attackDamage;
		this.slownessProbability = slownessProbability;
		this.slownessTick = slownessTick;
		this.slownessStrength = slownessStrength;
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter, float attackDamage, float slownessProbability, int slownessTick, int slownessStrength, boolean checkFaction) {
		this(worldIn, shooter, attackDamage, slownessProbability, slownessStrength, slownessTick);
		this.checkFaction = checkFaction;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
	protected void onImpact(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;
		BlockPos blockPos = raytraceResultIn.getBlockPos();
		if(!this.world.isRemote && null != entity && entity instanceof EntityLivingBase) {
			if(checkCanAttack((EntityLivingBase)entity)) {
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
				if(this.world.rand.nextFloat() < this.slownessProbability) {
					((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, this.slownessTick, this.slownessStrength));
				}
				world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.arrow.hit")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}else if(null != blockPos && !this.world.isRemote && null != raytraceResultIn.sideHit){
			if(!canThrough(this.world.getBlockState(blockPos))){
				BlockPos sideBlockPos = blockPos.offset(raytraceResultIn.sideHit);
				Block sideBlock = this.world.getBlockState(sideBlockPos).getBlock();
				if(Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, sideBlockPos) && (sideBlock == Blocks.AIR || sideBlock == Blocks.TALLGRASS)) {
					this.world.setBlockState(sideBlockPos, Blocks.SNOW_LAYER.getDefaultState());
				}
				world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.arrow.hit")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}
	}
	
	private boolean checkCanAttack(EntityLivingBase entity) {
		boolean canAttackFlag = true;
		if(null != this.thrower) {
			if(this.thrower instanceof EntityFriendly || this.thrower instanceof EntityPlayer) {
				if(entity instanceof EntityFriendly || entity instanceof EntityPlayer) {
					canAttackFlag = false;
				}else if(checkFaction && (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityGolem)) {
					canAttackFlag = false;
				}
			}else if(this.thrower instanceof EntityEvil) {
				if(entity instanceof EntityEvil) {
					canAttackFlag = false;
				}else if(checkFaction && entity instanceof IMob) {
					canAttackFlag = false;
				}
			}
		}
		return canAttackFlag;
	}
	
	private boolean canThrough(IBlockState blockState) {
		boolean canThroughFlag = false;
		Material material = blockState.getMaterial();
		if(material == Material.AIR || material == Material.GRASS || material == Material.WATER
				|| material == Material.LAVA || material == Material.PLANTS || material == Material.FIRE
				|| material == Material.VINE || material == Material.CIRCUITS || material == Material.WEB
				|| material == Material.CARPET) {
			canThroughFlag = true;
		}
		return canThroughFlag;
	}
	/**
	 if(block != Blocks.TALLGRASS && block != Blocks.WEB && block != Blocks.DEADBUSH && block != Blocks.RED_FLOWER 
				&& block != Blocks.YELLOW_FLOWER && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.TORCH 
				&& block != Blocks.LADDER && block != Blocks.SNOW_LAYER && block != Blocks.VINE && block != Blocks.WATERLILY 
				&& block != Blocks.CARPET && block != Blocks.DOUBLE_PLANT && block != Blocks.END_ROD && block != Blocks.STANDING_SIGN 
				&& block != Blocks.WALL_SIGN && block != Blocks.FLOWER_POT && block != Blocks.STANDING_BANNER && block != Blocks.WALL_BANNER 
				&& block != Blocks.RAIL && block != Blocks.ACTIVATOR_RAIL && block != Blocks.DETECTOR_RAIL && block != Blocks.GOLDEN_RAIL 
				&& block != Blocks.WHEAT && block != Blocks.REEDS && block != Blocks.CARROTS && block != Blocks.POTATOES 
				&& block != Blocks.BEETROOTS && block != Blocks.REDSTONE_TORCH && block != Blocks.UNLIT_REDSTONE_TORCH && block != Blocks.WOODEN_BUTTON 
				&& block != Blocks.STONE_BUTTON && block != Blocks.POWERED_REPEATER && block != Blocks.UNPOWERED_REPEATER && block != Blocks.POWERED_COMPARATOR 
				&& block != Blocks.UNPOWERED_COMPARATOR && block != Blocks.REDSTONE_BLOCK && block != Blocks.SAPLING){
	 */

	public float getAttackDamage() {
		return attackDamage;
	}
}
