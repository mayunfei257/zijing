package com.zijing.entity;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.zijing.ZijingMod;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.items.ItemDanZiling;
import com.zijing.items.staff.ItemStaffBingxue;
import com.zijing.main.BaseControl;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.itf.MagicSource;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.util.EntityUtil;
import com.zijing.util.MathUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	private int baseLevel = 1;
	private int nextLevelNeedExperience;
	private double experience;
	private ShepherdCapability shepherdCapability;
	private double swordDamage;
	private double armorValue;
	
	public EntitySummonTaoistPriest(World world) {
		super(world);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.experience = 0D;
		this.experienceValue = 0;
		this.isImmuneToFire = false;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
	}

	public EntitySummonTaoistPriest(World world, int baseLevel) {
		this(world);
		this.baseLevel = baseLevel;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing, BaseControl.itemDanZiling)));
        this.tasks.addTask(2, new EntityAIAttackRangedZJ(this, 1.0D, 15, 2.83D, 32.0F, ItemStaffBingxue.MagicSkill1));
        this.tasks.addTask(3, new EntityAIAttackMeleeZJ(this, 1.0D, 10, false));
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
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4.0D);
	}
	
	private void setBaseShepherdCapability() {
		this.shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(this.shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K()/2;
		EntityUtil.upEntityGrade(this, baseLevel - 1);
		this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonTaoistPriest.name", new Object[] {this.shepherdCapability.getLevel()}));
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setDouble(ZijingMod.MODID + ":swordDamage", this.swordDamage);
        compound.setDouble(ZijingMod.MODID + ":armorValue", this.armorValue);
        compound.setDouble(ZijingMod.MODID + ":experience", this.experience);
        compound.setInteger(ZijingMod.MODID + ":nextLevelNeedExperience", this.nextLevelNeedExperience);
        compound.setTag(ZijingMod.MODID + ":shepherdCapability", this.shepherdCapability.writeNBT(null));
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.swordDamage = compound.getDouble(ZijingMod.MODID + ":swordDamage");
        this.armorValue = compound.getDouble(ZijingMod.MODID + ":armorValue");
        this.experience = compound.getDouble(ZijingMod.MODID + ":experience");
        this.nextLevelNeedExperience = compound.getInteger(ZijingMod.MODID + ":nextLevelNeedExperience");
        this.shepherdCapability.readNBT(null, compound.getTag(ZijingMod.MODID + ":shepherdCapability"));
        this.updataSwordDamageAndArmorValue();
        EntityUtil.setEntityAllValue(this);
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
		if(!this.world.isRemote) {
			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)) {
				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)));
			}
			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND)) {
				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND)));
			}
			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.HEAD)) {
				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.HEAD)));
			}
			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.CHEST)) {
				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.CHEST)));
			}
			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.LEGS)) {
				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.LEGS)));
			}
			if(ItemStack.EMPTY != this.getItemStackFromSlot(EntityEquipmentSlot.FEET)) {
				this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItemStackFromSlot(EntityEquipmentSlot.FEET)));
			}
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(!this.world.isRemote) {
			ItemStack itemStack = player.getHeldItem(hand);
			if(itemStack.getItem() instanceof MagicSource && this.shepherdCapability.getMagic() < this.shepherdCapability.getMaxMagic()) {
				this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMaxMagic(), this.shepherdCapability.getMagic() + ((MagicSource)itemStack.getItem()).getMagicEnergy()));
				itemStack.shrink(1);
			}else if(itemStack.getItem() == BaseControl.itemDanZiling){
				this.shepherdCapability.setBlood(Math.min(this.shepherdCapability.getMaxBlood(), this.shepherdCapability.getBlood() + ((ItemDanZiling)itemStack.getItem()).effectTick/40.0D));
				this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMaxMagic(), this.shepherdCapability.getMagic() + ((ItemDanZiling)itemStack.getItem()).magicRestore));
				this.setHealth((float)this.shepherdCapability.getBlood());
				itemStack.shrink(1);
			}else {
				
			}
		}
		DecimalFormat df1 = new DecimalFormat("#0.0");
		DecimalFormat df2 = new DecimalFormat("#0.00");
		DecimalFormat df4 = new DecimalFormat("#0.0000");
		player.sendMessage(new TextComponentString("level: " + this.shepherdCapability.getLevel()));
		player.sendMessage(new TextComponentString("blood: " + df1.format(this.shepherdCapability.getBlood()) + "/" + df1.format(this.shepherdCapability.getMaxBlood())));
		player.sendMessage(new TextComponentString("magic: " + df1.format(this.shepherdCapability.getMagic()) + "/" + df1.format(this.shepherdCapability.getMaxMagic())));
		player.sendMessage(new TextComponentString("speed: " + df2.format(this.shepherdCapability.getSpeed())));
		player.sendMessage(new TextComponentString("power: " + df2.format(this.shepherdCapability.getPower() + this.swordDamage)  + " ( " + df2.format(this.shepherdCapability.getPower()) + " + " + df2.format(this.swordDamage) + " )"));
		player.sendMessage(new TextComponentString("bloodRestore: " + df4.format(this.shepherdCapability.getBloodRestore()) + "/T"));
		player.sendMessage(new TextComponentString("magicRestore: " + df4.format(this.shepherdCapability.getMagicRestore()) + "/T"));
		player.sendMessage(new TextComponentString("physicalDefense: " + df2.format(shepherdCapability.getPhysicalDefense() + this.armorValue) + " ( " + df2.format(this.shepherdCapability.getPhysicalDefense())  + " + " + df2.format(this.armorValue) + " )"));
		player.sendMessage(new TextComponentString("experience: " + df2.format(this.experience) + "/" + this.nextLevelNeedExperience));
		return true;
	}
    
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
        if (EntityPlayer.class.isAssignableFrom(cls) || EntityHasShepherdCapability.class.isAssignableFrom(cls)){
        	return false;
        }else if(cls == EntitySkeleton.class && this.shepherdCapability.getLevel() < 15){
        	return false;
        }else if(cls == EntityCreeper.class && this.shepherdCapability.getLevel() < 30){
            return false;
        }else if(cls == EntityEnderman.class && this.shepherdCapability.getLevel() < 45){
            return false;
        }else {
            return super.canAttackClass(cls);
        }
    }

	@Override
    public boolean attackEntityAsMob(Entity entityIn){
		if(!this.world.isRemote) {
	    	double attackDamage =  this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + this.swordDamage;
	    	boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)attackDamage);
			this.experience += attackDamage;
	        if (flag){
	            entityIn.motionY += 0.4000000059604645D;
	            this.applyEnchantments(this, entityIn);
		        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
	        }
	        return flag;
		}else {
			return true;
		}
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if(!this.world.isRemote && this.shepherdCapability.getMagic() >= ItemStaffBingxue.MagicSkill1) {
        	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	EntityThrowable entityDan;
        	if(this.world.rand.nextFloat() < 0.25D) {
        		entityDan = new EntityArrowHuoDan(world, this, attackDamage, 0F, 0F);
        	}else {
        		entityDan = new EntityArrowBingDan(world, this, attackDamage);
        	}
    		entityDan.shoot(target.posX - this.posX, target.getEntityBoundingBox().minY + target.height * 0.75D - entityDan.posY, target.posZ - this.posZ, 3.0F, 0);
    		this.world.spawnEntity(entityDan);
    		this.world.playSound((EntityPlayer) null, this.posX, this.posY + 1D, this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    		this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - ItemStaffBingxue.MagicSkill1);
			this.experience += attackDamage;
        }
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(!this.world.isRemote && !this.isDead && this.getHealth() > 0) {
			if(this.nextLevelNeedExperience <= this.experience) {
				EntityUtil.upEntityGrade(this, 1);
				this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonTaoistPriest.name", new Object[] {this.shepherdCapability.getLevel()}));
			}
			if(this.getHealth() < this.getMaxHealth()) {
				this.setHealth(this.getHealth() + (float)this.shepherdCapability.getBloodRestore());
				this.shepherdCapability.setBlood(this.getHealth());
			}
			if(this.shepherdCapability.getMagic() < this.shepherdCapability.getMaxMagic()) {
				this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMagic() + this.shepherdCapability.getMagicRestore(), this.shepherdCapability.getMaxMagic()));
			}
			if(this.getHealth() != this.shepherdCapability.getBlood()) {
				this.shepherdCapability.setBlood(this.getHealth());
			}
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
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
	public int getNextLevelNeedExperience() {
		return nextLevelNeedExperience;
	}

	@Override
	public void setNextLevelNeedExperience(int nextLevelNeedExperience) {
		this.nextLevelNeedExperience = nextLevelNeedExperience;
	}

	@Override
	public ShepherdCapability getShepherdCapability() {
		return this.shepherdCapability;
	}

	@Override
	public double getSwordDamage() {
		return swordDamage;
	}

	@Override
	public double getArmorValue() {
		return armorValue;
	}

	@Override
	public void setSwordDamage(double swordDamage) {
		this.swordDamage = swordDamage;
	}

	@Override
	public void setArmorValue(double armorValue) {
		this.armorValue = armorValue;
	}

	@Override
	public boolean updataSwordDamageAndArmorValue() {
		if(!this.world.isRemote) {
			EntityUtil.setEntityArmorValueAndSwordDamage(this);
		}
		return true;
	}
}