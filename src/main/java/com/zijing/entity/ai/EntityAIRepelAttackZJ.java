package com.zijing.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.entity.EntityDisciple;
import com.zijing.entity.EntityEvilTaoist;
import com.zijing.itf.EntityFriendly;
import com.zijing.itf.EntityShepherdCapability;
import com.zijing.util.ConstantUtil;
import com.zijing.util.SkillEntityShepherd;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class EntityAIRepelAttackZJ extends EntityAIBase{
	protected EntityShepherdCapability shepherdEntity;
    protected int maxAttackTick;
    protected double maxDistance;
	protected int needMagicValue;
	
	protected int attackTick;

	public EntityAIRepelAttackZJ(EntityShepherdCapability shepherdEntity, int maxAttackTick, double maxDistance, int needMagicValue) {
		this.shepherdEntity = shepherdEntity;
		this.maxAttackTick = maxAttackTick;
		this.maxDistance = maxDistance;
		this.needMagicValue = needMagicValue;
    	this.attackTick = -1;
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
    public boolean shouldExecute(){
		ShepherdCapability shepherdCapability = this.shepherdEntity.getShepherdCapability();
        if(shepherdCapability.getMagic() < this.needMagicValue) {
    		return false;
        }

    	AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.shepherdEntity.posX - maxDistance, this.shepherdEntity.posY - maxDistance, this.shepherdEntity.posZ - maxDistance, this.shepherdEntity.posX + maxDistance, this.shepherdEntity.posY + maxDistance, this.shepherdEntity.posZ + maxDistance);
    	Predicate predicate = (entity) -> {return entity instanceof EntityLiving || entity instanceof IAnimals;};
    	
    	List<EntityLiving> entityLivingList = (List<EntityLiving>) this.shepherdEntity.world.getEntitiesWithinAABB(EntityLiving.class, axisAlignedBB, predicate);
		if(null != entityLivingList && entityLivingList.size() > 0) {
			return  true;
		}
		
		return false;
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask(){
    	this.attackTick = -1;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask(){
    	if(this.attackTick > 0) {
    		this.attackTick--;
    	}

    	if(this.attackTick <= 0) {
        	AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.shepherdEntity.posX - maxDistance, this.shepherdEntity.posY - maxDistance, this.shepherdEntity.posZ - maxDistance, this.shepherdEntity.posX + maxDistance, this.shepherdEntity.posY + maxDistance, this.shepherdEntity.posZ + maxDistance);
        	Predicate predicate = (entity) -> {return entity instanceof IMob || entity instanceof IAnimals;};
        	
        	List<EntityLiving> entityLivingList = (List<EntityLiving>) this.shepherdEntity.world.getEntitiesWithinAABB(EntityLiving.class, axisAlignedBB, predicate);
    		if(null != entityLivingList && entityLivingList.size() > 0) {
    			
    			ShepherdCapability shepherdCapability = this.shepherdEntity.getShepherdCapability();
    	        if(shepherdCapability.getMagic() >= this.needMagicValue) {
    	        	
        	        this.shepherdEntity.swingArm(EnumHand.MAIN_HAND);
                	float attackDamage = (float)this.shepherdEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	        for(EntityLiving entityLiving: entityLivingList) {
        				if(entityLiving instanceof IMob) {
            				entityLiving.attackEntityFrom(DamageSource.causeMobDamage(this.shepherdEntity), attackDamage);
            				entityLiving.motionX = entityLiving.posX - this.shepherdEntity.posX > 0 ? 3 : -3;
            				entityLiving.motionZ = entityLiving.posZ - this.shepherdEntity.posZ > 0 ? 3 : -3;
            				entityLiving.motionY = entityLiving.posY - this.shepherdEntity.posY > 0 ? 2 : -2;
        				}else if(entityLiving instanceof IAnimals) {
            				entityLiving.motionX = entityLiving.posX - this.shepherdEntity.posX > 0 ? 4 : -4;
            				entityLiving.motionZ = entityLiving.posZ - this.shepherdEntity.posZ > 0 ? 4 : -4;
            				entityLiving.motionY = entityLiving.posY - this.shepherdEntity.posY > 0 ? 1 : -1;
        				}
        			}

        	        this.shepherdEntity.setExperience(this.shepherdEntity.getExperience() + attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION);
        	        this.shepherdEntity.world.playSound((EntityPlayer) null, this.shepherdEntity.posX, this.shepherdEntity.posY, this.shepherdEntity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            		shepherdCapability.setMagic(shepherdCapability.getMagic() - this.needMagicValue);
            		this.attackTick = this.maxAttackTick;
    	        }
    		}
    	}
    }
}
