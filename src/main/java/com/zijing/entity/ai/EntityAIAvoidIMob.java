package com.zijing.entity.ai;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.zijing.entity.EntityEvilTaoist;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidIMob extends EntityAIBase{
	private final Predicate <Entity> avoidTargetSelector = Predicates.alwaysTrue();
	private final Predicate<Entity> canBeSeenSelector;
	//Entity
	private final static List<Class<? extends Entity>> classToAvoid;
	protected EntityCreature entity;
	protected Entity closestLivingEntity;
	//Path
	private final float avoidDistance;
	private final PathNavigate navigation;
	private final double speed;
	private Path path;

	static {
		classToAvoid = new ArrayList<Class<? extends Entity>>();
		classToAvoid.add(EntityVindicator.class);
		classToAvoid.add(EntityZombie.class);
		classToAvoid.add(EntityZombieVillager.class);
		classToAvoid.add(EntityHusk.class);
		classToAvoid.add(EntityWitherSkeleton.class);
		classToAvoid.add(EntityCreeper.class);
		classToAvoid.add(EntitySpider.class);
		classToAvoid.add(EntityCaveSpider.class);
		classToAvoid.add(EntityEnderman.class);
		classToAvoid.add(EntityEndermite.class);
		classToAvoid.add(EntityGiantZombie.class);
		classToAvoid.add(EntitySilverfish.class);
		classToAvoid.add(EntityVex.class);
		classToAvoid.add(EntityPigZombie.class);
		classToAvoid.add(EntitySlime.class);
		classToAvoid.add(EntityMagmaCube.class);
		classToAvoid.add(EntityDragon.class);
		classToAvoid.add(EntityShulker.class);
		classToAvoid.add(EntityEvilTaoist.class);
	}
	
	public EntityAIAvoidIMob(EntityCreature entityIn, float avoidDistanceIn, double speedIn){
		this.entity = entityIn;
		this.canBeSeenSelector = new Predicate<Entity>(){
			public boolean apply(@Nullable Entity iMobEntity){
				return iMobEntity.isEntityAlive() && EntityAIAvoidIMob.this.entity.getEntitySenses().canSee(iMobEntity) && !EntityAIAvoidIMob.this.entity.isOnSameTeam(iMobEntity);
			}
		};
		//Path
		this.avoidDistance = avoidDistanceIn;
		this.navigation = entityIn.getNavigator();
		this.speed = speedIn;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute(){
		List<Entity> entityList = new ArrayList<Entity>();

		for(Class<? extends Entity> entityType : classToAvoid) {
			List<Entity> list = this.entity.world.<Entity>getEntitiesWithinAABB(entityType, this.entity.getEntityBoundingBox().grow(this.avoidDistance, 3.0D, this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));
			if(null != list && list.size() > 0) {
				Entity nearestEntity = list.get(0);
				double distace = this.entity.getDistance(nearestEntity);
				for(Entity entity: list) {
					double distanceTemp = this.entity.getDistance(entity);
					if(distace > distanceTemp) {
						nearestEntity = entity;
						distace = distanceTemp;
					}
				}
				entityList.add(nearestEntity);
			}
		}
		
		if (entityList.isEmpty()){
			return false;
		} else {
			
			Entity nearestEntity = entityList.get(0);
			double distace = this.entity.getDistance(nearestEntity);
			for(Entity entity: entityList) {
				double distanceTemp = this.entity.getDistance(entity);
				if(distace > distanceTemp) {
					nearestEntity = entity;
					distace = distanceTemp;
				}
			}
			this.closestLivingEntity = nearestEntity;
			
			Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
			if (vec3d == null){
				return false;
			} else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity)){
				return false;
			} else {
				this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
				return this.path != null;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting(){
		return !this.navigation.noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting(){
		this.navigation.setPath(this.path, this.speed);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask(){
		this.closestLivingEntity = null;
	}
}
