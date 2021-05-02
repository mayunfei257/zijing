package com.zijing.entity.ai;

import com.zijing.entity.EntityDisciple;
import com.zijing.entity.EntityEvilTaoist;
import com.zijing.itf.EntityFriendly;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public class EntityAIAttackRangedZJ2 extends EntityAIBase{
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
	private final int attackIntervalMin;
	/** The maximum time the AI has to wait before peforming another ranged attack. */
	private final int maxRangedAttackTime;
	private final float attackRadius;
	private final float maxAttackDistance;
	
	protected double minDistance;
	protected int needMagicValue;

	public EntityAIAttackRangedZJ2(IRangedAttackMob attacker, double movespeed, int maxAttackTime, double minDistance, float maxAttackDistanceIn, int needMagicValue){
		this(attacker, movespeed, maxAttackTime, maxAttackTime, minDistance, maxAttackDistanceIn, needMagicValue);
	}

	public EntityAIAttackRangedZJ2(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, double minDistance, float maxAttackDistanceIn, int needMagicValue){
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
        if(null != entitylivingbase && this.entityHost instanceof EntityFriendly) {
        	double distance = entityHost.getDistanceSq(entitylivingbase);
            if(distance > this.minDistance * this.minDistance && distance <= this.maxAttackDistance && ((EntityFriendly)this.entityHost).getShepherdCapability().getMagic() >= this.needMagicValue) {
            	if(this.entityHost.getClass() == EntityDisciple.class || this.entityHost.getClass() == EntityEvilTaoist.class) {
            		if(null != this.entityHost.getHeldItemOffhand() && ItemStack.EMPTY !=  this.entityHost.getHeldItemOffhand()) {
                    	this.attackTarget = entitylivingbase;
                		return  true;
            		}
            	}else {
                	this.attackTarget = entitylivingbase;
            		return  true;
            	}
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
		this.rangedAttackTime = -1;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask(){
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
		
		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		if(this.rangedAttackTime > 0) {
			this.rangedAttackTime --;
		}

		if (this.rangedAttackTime <= 0 && flag){
			float f = MathHelper.sqrt(d0) / this.attackRadius;
			float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
			((EntityCreature)this.rangedAttackEntityHost).swingArm(EnumHand.OFF_HAND);
			this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
			this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
		}
	}
}