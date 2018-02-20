package com.zijing.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowXukongDan extends EntityThrowable {
	
	public EntityArrowXukongDan(World a) {
		super(a);
	}

	public EntityArrowXukongDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowXukongDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
    protected void onImpact(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;
		BlockPos blockPos = raytraceResultIn.getBlockPos();
		if(null != entity && !this.world.isRemote && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
			this.thrower.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
			this.setDead();
		}else if(null != blockPos && !this.world.isRemote){
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
			this.setDead();
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
