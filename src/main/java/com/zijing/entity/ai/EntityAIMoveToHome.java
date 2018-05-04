package com.zijing.entity.ai;

import com.zijing.itf.EntityShepherdCapability;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMoveToHome extends EntityAIBase{
    private final EntityShepherdCapability shepherdEntity;
    private final double speed;
    private final float stopDistance;
	
	public EntityAIMoveToHome(EntityShepherdCapability shepherdEntity, double speed, float stopDistance) {
        this.shepherdEntity = shepherdEntity;
        this.speed = speed;
        this.stopDistance = stopDistance;
        this.setMutexBits(1);
	}
	
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute(){
		if(this.shepherdEntity.getMaxDistance() > 0 && null != this.shepherdEntity.getHomePos()) {
			BlockPos pos = this.shepherdEntity.getHomePos();
			if(this.shepherdEntity.getDistance(pos.getX(), pos.getY(), pos.getZ()) > this.shepherdEntity.getMaxDistance()) {
				return true;
			}
		}
		return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting(){
    	if(!this.shepherdEntity.getNavigator().noPath()) {
    		BlockPos pos = this.shepherdEntity.getHomePos();
    		if(this.shepherdEntity.getDistance(pos.getX(), pos.getY(), pos.getZ()) > this.stopDistance) {
    			return true;
    		}
    	}
		return false;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask(){
    	this.shepherdEntity.getNavigator().clearPath();;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting(){
		BlockPos pos = this.shepherdEntity.getHomePos();
		this.shepherdEntity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), this.speed);
    }
}
