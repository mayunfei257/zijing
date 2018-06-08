package com.zijing.entity.ai;

import com.zijing.itf.EntityShepherdCapability;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveToHomeZJ extends EntityAIBase{
    private final EntityShepherdCapability shepherdEntity;
    private final double speed;
    private Path path;
    private final float maxDistance;
    private final float stopDistance;
	
	public EntityAIMoveToHomeZJ(EntityShepherdCapability shepherdEntity, double speed, float maxDistance, float stopDistance) {
        this.shepherdEntity = shepherdEntity;
        this.speed = speed;
        this.maxDistance = maxDistance;
        this.stopDistance = stopDistance;
        this.setMutexBits(1);
	}
	
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute(){
		BlockPos pos = this.shepherdEntity.getHomePos();
		if(null != pos && this.maxDistance > 0 && this.stopDistance >= 0) {
			if(pos.getY() > 0 && this.shepherdEntity.getDistanceSq(pos) > this.maxDistance * this.maxDistance) {
                PathNavigateGround pathnavigateground = (PathNavigateGround)this.shepherdEntity.getNavigator();
                boolean flag = pathnavigateground.getEnterDoors();
                pathnavigateground.setBreakDoors(false);
                this.path = pathnavigateground.getPathToPos(pos);
                pathnavigateground.setBreakDoors(flag);
                if (this.path != null){
                    return true;
                } else {
                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.shepherdEntity, 10, 7, new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()));
                    if (vec3d == null) {
                        return false;
                    } else {
                        pathnavigateground.setBreakDoors(false);
                        this.path = this.shepherdEntity.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                        pathnavigateground.setBreakDoors(flag);
                        return this.path != null;
                    }
                }
			}
		}
		return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting(){
		BlockPos pos = this.shepherdEntity.getHomePos();
    	if(null != pos && !this.shepherdEntity.getNavigator().noPath()) {
    		if(this.shepherdEntity.getDistanceSq(pos) > this.stopDistance * this.stopDistance) {
    			return true;
    		}
    	}
		return false;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask(){
    	this.path = null;
    	this.shepherdEntity.getNavigator().clearPath();;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting(){
        this.shepherdEntity.getNavigator().setPath(this.path, this.speed);
    }
}
