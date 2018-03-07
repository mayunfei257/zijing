package com.zijing.entity;

import com.zijing.main.itf.EntityHasShepherdCapability;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

public class EntityArrowHuoDan extends EntityThrowable {
	private float attackDamage = 3;
	private float explosionProbability = 0.125F;
	private float explosionStrength = 1F;
	
	public EntityArrowHuoDan(World a) {
		super(a);
	}

	public EntityArrowHuoDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}
	
	public EntityArrowHuoDan(World worldIn, EntityLivingBase shooter, float attackDamage, float explosionProbability, float explosionStrength) {
		super(worldIn, shooter);
		this.attackDamage = attackDamage;
		this.explosionProbability = explosionProbability;
		this.explosionStrength = explosionStrength;
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
			entity.setFire(3);
			if(this.world.rand.nextFloat() < this.explosionProbability) {
				this.world.createExplosion(this, entity.posX, entity.posY, entity.posZ, this.explosionStrength, true);
			}
			world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			this.setDead();
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
				if(null != raytraceResultIn.sideHit) {
					IBlockState blockState = this.world.getBlockState(blockPos.offset(raytraceResultIn.sideHit));
					if(blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.TALLGRASS) {
						this.world.setBlockState(blockPos.offset(raytraceResultIn.sideHit), Blocks.FIRE.getDefaultState());
					}
				}
				if(this.world.rand.nextFloat() < this.explosionProbability) {
					BlockPos blockPosTemp = blockPos.offset(raytraceResultIn.sideHit);
					this.world.createExplosion(this, blockPosTemp.getX(), blockPosTemp.getY(), blockPosTemp.getZ(), this.explosionStrength, true);
				}
				world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}
    }
}
