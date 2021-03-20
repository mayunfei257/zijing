package com.zijing.entity;

import com.zijing.itf.EntityArrowDan;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowHuoDan extends EntityArrowDan {
	protected float explosionProbability = 0.125F;
	protected float explosionStrength = 1F;
	protected boolean canExplosionOnBlock = true;

	public EntityArrowHuoDan(World world) {
		super(world);
	}
	
//	public EntityArrowHuoDan(World world, double x, double y, double z) {
//		super(world, x, y, z);
//	}
//
//	public EntityArrowHuoDan(World world, EntityLivingBase shooter) {
//		super(world, shooter);
//	}
	
	public EntityArrowHuoDan(World worldIn, double x, double y, double z, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		super(worldIn, x, y, z, attackDamage, checkFaction);
		this.explosionProbability = explosionProbability;
		this.explosionStrength = explosionStrength;
		this.canExplosionOnBlock = canExplosionOnBlock;
	}

	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter, float attackDamage, float explosionProbability, float explosionStrength, boolean canExplosionOnBlock, boolean checkFaction) {
		super(worldIn, shooter, attackDamage, checkFaction);
		this.explosionProbability = explosionProbability;
		this.explosionStrength = explosionStrength;
		this.canExplosionOnBlock = canExplosionOnBlock;
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
	
}
