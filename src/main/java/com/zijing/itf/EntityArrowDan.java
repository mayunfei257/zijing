package com.zijing.itf;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public abstract class EntityArrowDan extends EntityThrowable {
	protected float attackDamage = 0;
	protected boolean checkFaction = false;

	public EntityArrowDan(World world) {
		super(world);
	}

	public EntityArrowDan(World world, EntityLivingBase shooter) {
		super(world, shooter);
	}

	public EntityArrowDan(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityArrowDan(World world, EntityLivingBase shooter, float attackDamage, boolean checkFaction) {
		this(world, shooter);
		this.attackDamage = attackDamage;
		this.checkFaction = checkFaction;
	}

	public EntityArrowDan(World world, double x, double y, double z, float attackDamage, boolean checkFaction) {
		this(world, x, y, z);
		this.attackDamage = attackDamage;
		this.checkFaction = checkFaction;
	}

	@Override
    public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_){
        return worldIn.getGameRules().getBoolean("mobGriefing");
    }
    
	//
	protected boolean checkCanAttack(EntityLivingBase entity) {
		boolean canAttackFlag = true;
		if(null != this.thrower) {
			if(this.thrower instanceof EntityPlayer) {
				if(entity instanceof EntityPlayer) {
					canAttackFlag = false;
				}else if(checkFaction && entity instanceof EntityFriendly) {
					canAttackFlag = false;
				}
			}else if(this.thrower instanceof EntityFriendly) {
				if(entity instanceof EntityFriendly || entity instanceof EntityPlayer) {
					canAttackFlag = false;
				}else if(checkFaction && (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityGolem)) {
					canAttackFlag = false;
				}
			} else if(this.thrower instanceof EntityEvil) {
				if(entity instanceof EntityEvil) {
					canAttackFlag = false;
				}else if(checkFaction && entity instanceof IMob) {
					canAttackFlag = false;
				}
			}
		}
		return canAttackFlag;
	}
	
	//
	protected boolean canThrough(IBlockState blockState) {
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
	
	//
	public float getAttackDamage() {
		return this.attackDamage;
	}
}
