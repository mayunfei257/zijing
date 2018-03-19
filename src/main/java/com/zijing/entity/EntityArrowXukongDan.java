package com.zijing.entity;

import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.itf.EntityMobHasShepherdCapability;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	
	public EntityArrowXukongDan(World a) {
		super(a);
	}

	public EntityArrowXukongDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowXukongDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowXukongDan(World worldIn, EntityLivingBase shooter, float attackDamage) {
		super(worldIn, shooter);
		this.attackDamage = attackDamage;
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
			boolean canAttackFlag = false;
			if((this.thrower instanceof EntityHasShepherdCapability || this.thrower instanceof EntityPlayer) && !(entity instanceof EntityHasShepherdCapability || entity instanceof EntityPlayer)) {
				canAttackFlag = true;
			}else if((this.thrower instanceof EntityMobHasShepherdCapability) && !(entity instanceof EntityMobHasShepherdCapability)) {
				canAttackFlag = true;
			}
			if(canAttackFlag) {
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
				this.thrower.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
				world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}else if(null != blockPos && !this.world.isRemote){
			Block block = this.world.getBlockState(blockPos).getBlock();
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
}
