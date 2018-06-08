package com.zijing.entity.ai;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidIMobZJ<T extends Entity> extends EntityAIBase{
	private final Predicate<Entity> targetSelector;
    protected final Class<T> targetClass;
	protected EntityCreature entity;
	protected Entity closestLivingEntity;
	private final float avoidDistance;
	private final double speed;
	private Path path;

	
	public EntityAIAvoidIMobZJ(EntityCreature entityIn, Class<T> classTarget, float avoidDistanceIn, double speedIn, @Nullable final Predicate <Entity> targetSelector){
		this.entity = entityIn;
		this.targetClass = classTarget;
		this.avoidDistance = avoidDistanceIn;
		this.speed = speedIn;
		this.targetSelector = targetSelector;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute(){
		Entity nearestEntity = null;

		List<Entity> list = this.entity.world.<Entity>getEntitiesWithinAABB(this.targetClass, this.entity.getEntityBoundingBox().grow(this.avoidDistance, 3.0D, this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.targetSelector));
		if(null != list && list.size() > 0) {
			nearestEntity = list.get(0);
			double distace = this.entity.getDistance(nearestEntity);
			for(Entity entity: list) {
				double distanceTemp = this.entity.getDistance(entity);
				if(distace > distanceTemp) {
					nearestEntity = entity;
					distace = distanceTemp;
				}
			}
		}
		
		if (null == nearestEntity){
			return false;
		} else {
			this.closestLivingEntity = nearestEntity;
			Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 8, 8, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
			if (vec3d == null){
				return false;
			} else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity)){
				return false;
			} else {
				this.path = this.entity.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
				return this.path != null;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting(){
		return !this.entity.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting(){
		this.entity.getNavigator().setPath(this.path, this.speed);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask(){
		this.closestLivingEntity = null;
	}
}
