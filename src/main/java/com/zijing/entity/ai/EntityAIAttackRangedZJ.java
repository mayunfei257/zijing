package com.zijing.entity.ai;

import com.zijing.main.itf.EntityHasShepherdCapability;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

public class EntityAIAttackRangedZJ extends EntityAIBase{
	/** The entity the AI instance has been applied to */
	private final EntityLiving entityHost;
	/** The entity (as a RangedAttackMob) the AI instance has been applied to. */
	private final IRangedAttackMob rangedAttackEntityHost;
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	private int seeTime;
	private final int attackIntervalMin;
	/** The maximum time the AI has to wait before peforming another ranged attack. */
	private final int maxRangedAttackTime;
	private final float attackRadius;
	private final float maxAttackDistance;
	
	protected double minDistance;
	protected int needMagicValue;

	public EntityAIAttackRangedZJ(IRangedAttackMob attacker, double movespeed, int maxAttackTime, double minDistance, float maxAttackDistanceIn, int needMagicValue){
		this(attacker, movespeed, maxAttackTime, maxAttackTime, minDistance, maxAttackDistanceIn, needMagicValue);
	}

	public EntityAIAttackRangedZJ(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, double minDistance, float maxAttackDistanceIn, int needMagicValue){
		this.rangedAttackTime = -1;
		if (!(attacker instanceof EntityLivingBase)){
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}else{
			this.rangedAttackEntityHost = attacker;
			this.entityHost = (EntityLiving)attacker;
			this.entityMoveSpeed = movespeed;
			this.attackIntervalMin = p_i1650_4_;
			this.maxRangedAttackTime = maxAttackTime;
			this.attackRadius = maxAttackDistanceIn;
			this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
			this.minDistance = minDistance;
			this.needMagicValue = needMagicValue;
			this.setMutexBits(3);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
    public boolean shouldExecute(){
		EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
        if(null != entitylivingbase && this.entityHost instanceof EntityHasShepherdCapability) {
            double distance = Math.sqrt(Math.pow(entitylivingbase.posX - this.entityHost.posX, 2) + Math.pow(entitylivingbase.posY - this.entityHost.posY, 2) + Math.pow(entitylivingbase.posZ - this.entityHost.posZ, 2));
            if(distance > this.minDistance && ((EntityHasShepherdCapability)this.entityHost).getShepherdCapability().getMagic() >= this.needMagicValue) {
            	this.attackTarget = entitylivingbase;
        		return  true;
            }
        }
		return false;
    }

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting(){
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask(){
		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask(){
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
		
		if (flag){
			++this.seeTime;
		} else {
			this.seeTime = 0;
		}

		if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20){
			this.entityHost.getNavigator().clearPath();
		} else {
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);

		if (--this.rangedAttackTime == 0){
			if (!flag){
				return;
			}
			float f = MathHelper.sqrt(d0) / this.attackRadius;
			float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
			this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
			this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
		} else if (this.rangedAttackTime < 0){
			float f2 = MathHelper.sqrt(d0) / this.attackRadius;
			this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
		}
	}
}