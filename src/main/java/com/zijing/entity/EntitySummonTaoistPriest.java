package com.zijing.entity;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.zijing.ZijingMod;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.items.staff.ItemStaffBingxue;
import com.zijing.main.BaseControl;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.itf.MagicSource;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.util.MathUtil;
import com.zijing.util.PlayerUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntitySummonTaoistPriest extends EntityCreature implements EntityHasShepherdCapability, IRangedAttackMob{
	private static final int baseLevel = 1;
	private int nextLevelNeedExperience;
	private double experience;
	private ShepherdCapability shepherdCapability;
	
	public EntitySummonTaoistPriest(World world) {
		super(world);
		this.experience = 0D;
		this.experienceValue = 0;
		this.isImmuneToFire = false;
		this.setBaseShepherdCapability();
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(BaseControl.itemToolZijingJian));
		this.setNoAI(false);
		this.setAI();
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
//		if(this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED) == null)
//			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
//		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(1024D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4.0D);
	}
	
	private void setBaseShepherdCapability() {
		shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K();
		PlayerUtil.upGradeFromEntity(this, baseLevel - 1);
		PlayerUtil.setAllValueToEntity(this, shepherdCapability);
		this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonTaoistPriest.name", new Object[] {shepherdCapability.getLevel()}));
		shepherdCapability.setMagic(shepherdCapability.getMaxMagic());
		this.nextLevelNeedExperience = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ZijingMod.config.getUPGRADE_NEED_XP_K();
	}
	
	private void setAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing)));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.25D, 15, 32.0F) {
            public boolean shouldExecute(){
                EntityLivingBase entitylivingbase = getAttackTarget();
                if(null != entitylivingbase) {
                    double distance = Math.sqrt(Math.pow(entitylivingbase.posX - posX, 2) + Math.pow(entitylivingbase.posY - posY, 2) + Math.pow(entitylivingbase.posZ - posZ, 2));
                    if(distance > 2.83) {
                		return shepherdCapability.getMagic() >= ItemStaffBingxue.MagicSkill1 ? super.shouldExecute() : false;
                    }
                }
        		return false;
            }
        });
        this.tasks.addTask(3, new EntityAIAttackMeleeZJ(this, 1D, 10, false));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsTarget(this, 1D, 32.0F));
		this.tasks.addTask(5, new EntityAIWander(this, 0.9D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1D));
		this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, new Predicate<EntityLiving>(){
            public boolean apply(@Nullable EntityLiving target){
                return target != null && IMob.VISIBLE_MOB_SELECTOR.apply(target);
            }
        }));
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setDouble(ZijingMod.MODID + ":experience", this.experience);
        compound.setTag(ZijingMod.MODID + ":shepherdCapability", shepherdCapability.writeNBT(null));
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.experience = compound.getDouble(ZijingMod.MODID + ":experience");
        shepherdCapability.readNBT(null, compound.getTag(ZijingMod.MODID + ":shepherdCapability"));
		this.nextLevelNeedExperience = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ZijingMod.config.getUPGRADE_NEED_XP_K();
    }

	@Override
	public double getExperience() {
		return this.experience;
	}

	@Override
	public void setExperience(double xp) {
		this.experience = xp;
	}

	@Override
	public ShepherdCapability getShepherdCapability() {
		return this.shepherdCapability;
	}
	
	@Override
	protected Item getDropItem() {
		return Items.GHAST_TEAR;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation(""));
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("game.neutral.hurt"));
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("game.neutral.die"));
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt entityLightningBolt) {
		super.onStruckByLightning(entityLightningBolt);
	}

	@Override
	public void fall(float l, float d) {
		super.fall(l, d);
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		world.playSound((EntityPlayer) null, this.posX, this.posY + 1D, this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(("entity.villager.death"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		if(itemStack.getItem() instanceof MagicSource && shepherdCapability.getMagic() < shepherdCapability.getMaxMagic()) {
			shepherdCapability.setMagic(Math.min(shepherdCapability.getMagic(), shepherdCapability.getMagic() + ((MagicSource)itemStack.getItem()).getMagicEnergy()));
			itemStack.shrink(1);
		}
		DecimalFormat df1 = new DecimalFormat("#0.0");
		DecimalFormat df2 = new DecimalFormat("#0.00");
		DecimalFormat df4 = new DecimalFormat("#0.0000");
		player.sendMessage(new TextComponentString("level: " + shepherdCapability.getLevel()));
		player.sendMessage(new TextComponentString("blood: " + df1.format(shepherdCapability.getBlood()) + "/" + df1.format(shepherdCapability.getMaxBlood())));
		player.sendMessage(new TextComponentString("magic: " + df1.format(shepherdCapability.getMagic()) + "/" + df1.format(shepherdCapability.getMaxMagic())));
		player.sendMessage(new TextComponentString("speed: " + df2.format(shepherdCapability.getSpeed())));
		player.sendMessage(new TextComponentString("power: " + df2.format(shepherdCapability.getPower())));
		player.sendMessage(new TextComponentString("bloodRestore: " + df4.format(shepherdCapability.getBloodRestore()) + "/T"));
		player.sendMessage(new TextComponentString("magicRestore: " + df4.format(shepherdCapability.getMagicRestore()) + "/T"));
		player.sendMessage(new TextComponentString("physicalDefense: " + df2.format(shepherdCapability.getPhysicalDefense())));
		player.sendMessage(new TextComponentString("experience: " + df2.format(this.experience) + "/" + this.nextLevelNeedExperience));
		return true;
	}
    
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
        if (EntityPlayer.class.isAssignableFrom(cls)){
            return false;
        }else if(EntityHasShepherdCapability.class.isAssignableFrom(cls)){
        	return false;
        }else if(cls == EntitySkeleton.class && shepherdCapability.getLevel() < 10){
        	return false;
        }else if(cls == EntityCreeper.class && shepherdCapability.getLevel() < 20){
            return false;
        }else if(cls == EntityEnderman.class && shepherdCapability.getLevel() < 30){
            return false;
        }else {
            return super.canAttackClass(cls);
        }
    }

	@Override
    public boolean attackEntityAsMob(Entity entityIn){
		if(!this.world.isRemote) {
	        this.world.setEntityState(this, (byte)4);
	    	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
	    	ItemStack itemStack = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
	    	if(itemStack.getItem() instanceof ItemSword) {
	    		attackDamage += ((ItemSword)itemStack.getItem()).getAttackDamage();
	    	}
	    	boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);
			this.experience += attackDamage;
	        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
	        if (flag){
	            entityIn.motionY += 0.4000000059604645D;
	            this.applyEnchantments(this, entityIn);
	        }
	        return flag;
		}else {
			return true;
		}
    }

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(!this.world.isRemote && !this.isDead && this.getHealth() > 0) {
			boolean changed = false;
			if(this.nextLevelNeedExperience <= this.experience) {
				PlayerUtil.upGradeFromEntity(this, 1);
				PlayerUtil.setAllValueToEntity(this, shepherdCapability);
				this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonTaoistPriest.name", new Object[] {shepherdCapability.getLevel()}));
				this.nextLevelNeedExperience = (int) MathUtil.getUpgradeK(shepherdCapability.getLevel(), 1) * ZijingMod.config.getUPGRADE_NEED_XP_K();
				changed = true;
			}
			if(this.getHealth() < this.getMaxHealth()) {
				this.setHealth(this.getHealth() + (float)shepherdCapability.getBloodRestore());
				shepherdCapability.setBlood(this.getHealth());
				changed = true;
			}
			if(shepherdCapability.getMagic() < shepherdCapability.getMaxMagic()) {
				shepherdCapability.setMagic(Math.min(shepherdCapability.getMagic() + shepherdCapability.getMagicRestore(), shepherdCapability.getMaxMagic()));
				changed = true;
			}
			if(this.getHealth() != shepherdCapability.getBlood()) {
				shepherdCapability.setBlood(this.getHealth());
				changed = true;
			}
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if(!this.world.isRemote && shepherdCapability.getMagic() >= ItemStaffBingxue.MagicSkill1) {
        	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
    		EntityArrowBingDan bingDan = new EntityArrowBingDan(world, this, attackDamage);
//	        double d0 = target.posX - this.posX;
//	        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - bingDan.posY;
//	        double d2 = target.posZ - this.posZ;
//	        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
//	        bingDan.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, 3.0F, 0);
    		bingDan.shoot(target.posX - this.posX, target.getEntityBoundingBox().minY + target.height * 0.75D - bingDan.posY, target.posZ - this.posZ, 3.0F, 0);
//    		bingDan.shoot(this.getLookVec().x, this.getLookVec().y, this.getLookVec().z, 3.0F, 0);
    		this.world.spawnEntity(bingDan);
    		this.world.playSound((EntityPlayer) null, this.posX, this.posY + 1D, this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    		shepherdCapability.setMagic(shepherdCapability.getMagic() - ItemStaffBingxue.MagicSkill1);
			this.experience += attackDamage;
        }
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
	}
}