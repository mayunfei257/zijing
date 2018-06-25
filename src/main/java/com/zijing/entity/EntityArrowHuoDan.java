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
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowHuoDan extends EntityThrowable {
	private float attackDamage = 3;
	private float explosionProbability = 0.125F;
	private float explosionStrength = 1F;
	private boolean canExplosionOnBlock = true;
	private boolean checkFaction = false;
	
	public EntityArrowHuoDan(World a) {
		super(a);
	}

	public EntityArrowHuoDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
	
	public EntityArrowHuoDan(World worldIn, double x, double y, double z, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock) {
		this(worldIn, x, y, z);
		this.attackDamage = attackDamage;
		this.explosionProbability = explosionProbability;
		this.explosionStrength = explosionStrength;
		this.canExplosionOnBlock = canExplosionOnBlock;
	}
	public EntityArrowHuoDan(World worldIn, double x, double y, double z, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		this(worldIn, x, y, z, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock);
		this.checkFaction = checkFaction;
	}

	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}
	
	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock) {
		this(worldIn, shooter);
		this.attackDamage = attackDamage;
		this.explosionProbability = explosionProbability;
		this.explosionStrength = explosionStrength;
		this.canExplosionOnBlock = canExplosionOnBlock;
	}

	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		this(worldIn, shooter, attackDamage, explosionProbability, explosionStrength, canExplosionOnBlock);
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
		if(null != entity && !this.world.isRemote && entity instanceof EntityLivingBase) {
			if(checkCanAttack((EntityLivingBase)entity)) {
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
				entity.setFire(3);
				if(this.world.rand.nextFloat() < this.explosionProbability) {
					this.world.createExplosion(this, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, this.explosionStrength, true);
				}
				world.playSound((EntityPlayer) null, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}else if(null != blockPos && !this.world.isRemote){
			if(!canThrough(this.world.getBlockState(blockPos))){
				BlockPos sideBlockPos = blockPos.offset(raytraceResultIn.sideHit);
				Block sideBlock = this.world.getBlockState(sideBlockPos).getBlock();
				if(sideBlock == Blocks.AIR || sideBlock == Blocks.TALLGRASS) {
					this.world.setBlockState(sideBlockPos, Blocks.FIRE.getDefaultState());
				}
				if(this.world.rand.nextFloat() < this.explosionProbability && this.canExplosionOnBlock) {
					this.world.createExplosion(this, sideBlockPos.getX(), sideBlockPos.getY(), sideBlockPos.getZ(), this.explosionStrength, true);
				}
				world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
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

	public float getAttackDamage() {
		return attackDamage;
	}
}
