package com.zijing.entity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class EntityArrowBingDan extends EntityThrowable {
	
	public EntityArrowBingDan(World a) {
		super(a);
	}

	public EntityArrowBingDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter) {
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
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 4);
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, this.world.rand.nextInt(6)));
			if(this.world.rand.nextFloat() < 0.125D) {
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 4);
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 7));
			}
			this.setDead();
		}else if(null != blockPos && !this.world.isRemote){
			IBlockState blockState = this.world.getBlockState(blockPos);
			if(blockState.getBlock().getMaterial(blockState) == Material.PLANTS || blockState.getBlock().getMaterial(blockState) == Material.FIRE) {
				return ;
			}else if(blockState.getBlock() instanceof IPlantable) {
				return ;
			}
			this.setDead();
		}
    }
}
