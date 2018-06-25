package com.zijing.entity;

import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;

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

public class EntityArrowXukongDan extends EntityThrowable {
	private float attackDamage = 0;
	private boolean checkFaction = false;
	
	public EntityArrowXukongDan(World a) {
		super(a);
	}

	public EntityArrowXukongDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowXukongDan(World worldIn, double x, double y, double z, float attackDamage) {
		this(worldIn, x, y, z);
		this.attackDamage = attackDamage;
	}

	public EntityArrowXukongDan(World worldIn, double x, double y, double z, float attackDamage, boolean checkFaction) {
		this(worldIn, x, y, z, attackDamage);
		this.checkFaction = checkFaction;
	}

	public EntityArrowXukongDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowXukongDan(World worldIn, EntityLivingBase shooter, float attackDamage) {
		this(worldIn, shooter);
		this.attackDamage = attackDamage;
	}

	public EntityArrowXukongDan(World worldIn, EntityLivingBase shooter, float attackDamage, boolean checkFaction) {
		this(worldIn, shooter, attackDamage);
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
				this.thrower.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
				world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}else if(null != blockPos && !this.world.isRemote){
			if(!canThrough(this.world.getBlockState(blockPos))){
				if(checkCanStandBlock(this.world, blockPos)) {
					this.thrower.setPositionAndUpdate(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
				}else {
					BlockPos blockPosTem = null;
					double distends = 13;
					for(int i = 2; i >= -2; i--) {
						for(int j = 2; j >= -2; j--) {
							for(int k = 2; k >= -2; k--) {
								double tem = i * i + j * j + k * k;
								if(tem < distends && blockPos.getY() + j > 0 && checkCanStandBlock(this.world, new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k))) {
									distends = tem;
									blockPosTem = new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k);
								}
							}
						}
					}
					if(null != blockPosTem) {
						this.thrower.setPositionAndUpdate(blockPosTem.getX() + 0.5D, blockPosTem.getY(), blockPosTem.getZ() + 0.5D);
					}else {
						BlockPos blockPosTem2 = blockPos.offset(raytraceResultIn.sideHit);
						if(blockPosTem2.getY() > 0) {
							this.thrower.setPositionAndUpdate(blockPosTem2.getX() + 0.5D, blockPosTem2.getY(), blockPosTem2.getZ() + 0.5D);
						}
					}
				}
				world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}
    }
	
	private boolean checkCanStandBlock(World world, BlockPos blockPos) {
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
