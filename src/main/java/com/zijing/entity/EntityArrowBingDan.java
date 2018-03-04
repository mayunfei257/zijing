package com.zijing.entity;

import com.zijing.main.itf.EntityHasShepherdCapability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

	public EntityArrowBingDan(World a) {
		super(a);
	}

	public EntityArrowBingDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter, float attackDamage) {
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
		if(null != entity && !this.world.isRemote && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && entity != this.thrower && !(entity instanceof EntityHasShepherdCapability)) {
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, this.world.rand.nextInt(4) + 2));
			if(this.world.rand.nextFloat() < 0.125D) {
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 7));
			}
			world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.arrow.hit")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			this.setDead();
		}else if(null != blockPos && !this.world.isRemote && null != raytraceResultIn.sideHit){
			if(Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockPos.offset(raytraceResultIn.sideHit))) {
				this.world.setBlockState(blockPos.offset(raytraceResultIn.sideHit), Blocks.SNOW_LAYER.getDefaultState());
			}
			world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.arrow.hit")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			this.setDead();
		}
	}
}
