package com.zijing.entity;

import com.zijing.BaseControl;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;
import com.zijing.util.SkillEntity;
import com.zijing.util.SkillEntityShepherd;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEvilTaoist extends EntityEvil{
	
	public EntityEvilTaoist(World world) {
		super(world);
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
	}

	public EntityEvilTaoist(World world, int baseLevel) {
		super(world, baseLevel);
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackRangedZJ(this, 1.2D, 10, 3.6D, 32.0F, SkillEntity.MagicSkill_BingDan));
        this.tasks.addTask(2, new EntityAIAttackMeleeZJ(this, 1.2D, 10, false));
        this.tasks.addTask(3, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsTarget(this, 1.2D, 32.0F));
		this.tasks.addTask(5, new EntityAIWander(this, 0.9D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityFriendly.class, true, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
	}
	
	protected void setBaseShepherdCapability() {
		super.setBaseShepherdCapability();
		this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityEvilTaoist.name", new Object[0]));
	}
	
	@Override
	protected Item getDropItem() {
		return BaseControl.itemZijing;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.witch.ambient"));
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.witch.hurt"));
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.witch.death"));
	}

	@Override
    public boolean attackEntityAsMob(Entity entityIn){
    	double attackDamage =  this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + this.swordDamage;
    	boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)attackDamage);
        if(this.shepherdCapability.getLevel() >= SkillEntity.CAN_LIGHTNING_LEVEL) {
        	entityIn.world.spawnEntity(new EntityLightningBolt(entityIn.world, entityIn.posX, entityIn.posY, entityIn.posZ, true));
        }
        if (flag){
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
	        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        }
		this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
        return flag;
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if(!this.world.isRemote) {
			if(this.world.rand.nextFloat() < 0.5D) {
				SkillEntityShepherd.shootHuoDanSkill(this, target, false);
			}else {
				SkillEntityShepherd.shootBingDanSkill(this, target);
			}
        	double attackDamage = this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
			this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
		}
	}

    @Override
	protected void upEntityGrade(int upLevel) {
		EntityUtil.upEntityGrade(this, upLevel);
		if(this.shepherdCapability.getLevel() >= SkillEntity.IMMUNE_FIRE_LEVEL) {
			this.isImmuneToFire = true;
		}
		this.shepherdCapability.setSpeed(this.shepherdCapability.getSpeed() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setMaxBlood(this.shepherdCapability.getMaxBlood() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setBlood(this.shepherdCapability.getMaxBlood());
		this.experienceValue = this.nextLevelNeedExperience;
		EntityUtil.setEntityAllValue(this);
	}
    
	@Override
    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions() {
		return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityEvilTaoist.special", new Object[0]);
	}
}
