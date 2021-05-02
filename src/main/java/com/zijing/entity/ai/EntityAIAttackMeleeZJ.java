package com.zijing.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class EntityAIAttackMeleeZJ extends EntityAIAttackMelee{
    protected int maxAttackTick;

	public EntityAIAttackMeleeZJ(EntityCreature creature, double speedIn,int maxAttackTick, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
		this.maxAttackTick = maxAttackTick;
	}
	
	@Override
    protected void checkAndPerformAttack(EntityLivingBase attackTarget, double p_190102_2_){
        double d0 = this.getAttackReachSqr(attackTarget);
        if (p_190102_2_ <= d0 && this.attackTick <= 0){
            this.attackTick = maxAttackTick;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(attackTarget);
        }
    }
	
}
