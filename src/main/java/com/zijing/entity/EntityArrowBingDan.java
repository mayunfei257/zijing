package com.zijing.entity;

import com.zijing.itf.EntityArrowDan;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowBingDan extends EntityArrowDan {
	protected float slownessProbability = 0.125F;
	protected int slownessStrength = 2;
	protected int slownessTick = 80;

	public EntityArrowBingDan(World world) {
		super(world);
	}
	
//	public EntityArrowBingDan(World world, double x, double y, double z) {
//		super(world, x, y, z);
//	}
//
//	public EntityArrowBingDan(World world, EntityLivingBase shooter) {
//		super(world, shooter);
//	}
	
	public EntityArrowBingDan(World worldIn, double x, double y, double z, float attackDamage, float slownessProbability, int slownessTick, int slownessStrength, boolean checkFaction) {
		super(worldIn, x, y, z, attackDamage, checkFaction);
		this.slownessProbability = slownessProbability;
		this.slownessTick = slownessTick;
		this.slownessStrength = slownessStrength;
	}

	public EntityArrowBingDan(World worldIn, EntityLivingBase shooter, float attackDamage, float slownessProbability, int slownessTick, int slownessStrength, boolean checkFaction) {
		super(worldIn, shooter, attackDamage, checkFaction);
		this.slownessProbability = slownessProbability;
		this.slownessTick = slownessTick;
		this.slownessStrength = slownessStrength;
	}

	@Override
	protected void init() {
		setNoGravity(true);
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
				IBlockState blockState = this.world.getBlockState(sideBlockPos);
				if(Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, sideBlockPos) && blockState.getMaterial() == Material.AIR) {
					this.world.setBlockState(sideBlockPos, Blocks.SNOW_LAYER.getDefaultState());
				}
				
				sideBlockPos = blockPos.offset(EnumFacing.UP);
				blockState = this.world.getBlockState(sideBlockPos);
				if(Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, sideBlockPos) && blockState.getMaterial() == Material.AIR) {
					this.world.setBlockState(sideBlockPos, Blocks.SNOW_LAYER.getDefaultState());
				}
				
				world.playSound((EntityPlayer) null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.arrow.hit")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}
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
}
