package com.zijing.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowHuoDan extends EntityThrowable {
	
	public EntityArrowHuoDan(World a) {
		super(a);
	}

	public EntityArrowHuoDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter) {
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
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 3);
			entity.setFire(3);
			if(this.world.rand.nextFloat() < 0.125D) {
				this.world.createExplosion(this, entity.posX, entity.posY, entity.posZ, 1F, true);
			}
			this.setDead();
		}else if(null != blockPos && !this.world.isRemote){
			if(null != raytraceResultIn.sideHit) {
				IBlockState blockState = this.world.getBlockState(blockPos.offset(raytraceResultIn.sideHit));
				if(blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.TALLGRASS) {
					this.world.setBlockState(blockPos.offset(raytraceResultIn.sideHit), Blocks.FIRE.getDefaultState());
				}
			}
			if(this.world.rand.nextFloat() < 0.125D) {
				BlockPos blockPosTemp = blockPos.offset(raytraceResultIn.sideHit);
				this.world.createExplosion(this, blockPosTemp.getX(), blockPosTemp.getY(), blockPosTemp.getZ(), 1F, true);
			}
			this.setDead();
		}
    }
}
