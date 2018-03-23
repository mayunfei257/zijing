package com.zijing.entity;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.data.message.ShepherdEntityToClientMessage;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.gui.GuiEntityCapability;
import com.zijing.items.staff.ItemStaffBingxue;
import com.zijing.items.staff.ItemZilingZhu;
import com.zijing.itf.EntityHasShepherdCapability;
import com.zijing.itf.ItemDan;
import com.zijing.itf.MagicSource;
import com.zijing.util.ConstantUtil;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDisciple extends EntityCreature implements EntityHasShepherdCapability, IRangedAttackMob{
	private int nextConnectTick = ConstantUtil.CONNECT_TICK;
	private int checkHomeTick = ConstantUtil.CHECK_HOME_TICK;
	private int baseLevel = 1;
	private GENDER gender = GENDER.FEMALE;

	private int nextLevelNeedExperience;
	private double experience;
	private ShepherdCapability shepherdCapability;
	private double swordDamage;
	private double armorValue;
	
	public EntityDisciple(World world) {
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

	public EntityDisciple(World world, int baseLevel, GENDER gender) {
		super(world);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.experience = 0D;
		this.experienceValue = 0;
		this.isImmuneToFire = false;
		this.baseLevel = baseLevel;
		this.gender = gender;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing, BaseControl.itemDanZiling, Item.getItemFromBlock(Blocks.RED_FLOWER), Item.getItemFromBlock(Blocks.YELLOW_FLOWER))));
        this.tasks.addTask(2, new EntityAIAttackRangedZJ(this, 1.0D, 15, 2.83D, 32.0F, ItemStaffBingxue.MagicSkill1));
        this.tasks.addTask(3, new EntityAIAttackMeleeZJ(this, 1.0D, 10, false));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        
        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 1D, 32.0F));
		this.tasks.addTask(7, new EntityAIWander(this, 0.9D));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, true, new Predicate<EntityLiving>(){
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
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
	}
	
	private void setBaseShepherdCapability() {
		this.shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(this.shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K()/2;
		EntityUtil.upEntityGrade(this, baseLevel - 1);
		EntityUtil.setEntityAllValue(this);
		if(this.gender == GENDER.FEMALE) {
			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityFemaleDisciple.name", new Object[0]));
		}else {
			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityMaleDisciple.name", new Object[0]));
		}
		if(this.shepherdCapability.getLevel() >= ConstantUtil.IMMUNE_FIRE_LEVEL) {
			this.isImmuneToFire = true;
		}
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setBoolean("isImmuneToFire", isImmuneToFire);
        compound.setDouble(ConstantUtil.MODID + ":swordDamage", this.swordDamage);
        compound.setDouble(ConstantUtil.MODID + ":armorValue", this.armorValue);
        compound.setDouble(ConstantUtil.MODID + ":experience", this.experience);
        compound.setInteger(ConstantUtil.MODID + ":nextLevelNeedExperience", this.nextLevelNeedExperience);
        compound.setTag(ConstantUtil.MODID + ":shepherdCapability", this.shepherdCapability.writeNBT(null));
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.isImmuneToFire = compound.getBoolean("isImmuneToFire");
        this.swordDamage = compound.getDouble(ConstantUtil.MODID + ":swordDamage");
        this.armorValue = compound.getDouble(ConstantUtil.MODID + ":armorValue");
        this.experience = compound.getDouble(ConstantUtil.MODID + ":experience");
        this.nextLevelNeedExperience = compound.getInteger(ConstantUtil.MODID + ":nextLevelNeedExperience");
        this.shepherdCapability.readNBT(null, compound.getTag(ConstantUtil.MODID + ":shepherdCapability"));
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
	public void fall(float distance, float damageMultiplier) {
		boolean mainHandFlag = null != this.getHeldItemMainhand() && this.getHeldItemMainhand().getItem() == BaseControl.itemZilingZhu;
		boolean offHandFlag = null != this.getHeldItemOffhand() && this.getHeldItemOffhand().getItem() == BaseControl.itemZilingZhu;
		if(distance > 3 && (mainHandFlag || offHandFlag) && this.shepherdCapability.getMagic() >= ItemZilingZhu.MagicSkill5) {
			distance = 0;
			this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - ItemZilingZhu.MagicSkill5);
		}
		super.fall(distance, damageMultiplier);
	}

	@Override
	public void onDeath(DamageSource source) {
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
		super.onDeath(source);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		if(itemStack.getItem() instanceof MagicSource && this.shepherdCapability.getMagic() < this.shepherdCapability.getMaxMagic()) {
			this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMaxMagic(), this.shepherdCapability.getMagic() + ((MagicSource)itemStack.getItem()).getMagicEnergy()));
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			itemStack.shrink(1);
		}else if(itemStack.getItem() instanceof ItemDan){
			((ItemDan)itemStack.getItem()).onFoodEatenByEntityLivingBase(this);
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			itemStack.shrink(1);
		}else if(itemStack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER) || itemStack.getItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)){
			this.experience += 5;
			this.spawnParticles(EnumParticleTypes.HEART);
			itemStack.shrink(1);
		}else if(itemStack.getItem() == Items.DIAMOND && player.isSneaking()){
			this.experience += 10000;
		}else if(!this.world.isRemote) {// && player instanceof EntityPlayerMP
//			EntityPlayerMP playerMp = (EntityPlayerMP)player;
//			playerMp.getNextWindowId();
//			playerMp.openContainer = new GuiEntityCapability.MyContainer(world, this, playerMp);
//			playerMp.openContainer.windowId = playerMp.currentWindowId;
//			playerMp.openContainer.addListener(playerMp);
//	        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(playerMp, playerMp.openContainer));
//	        BaseControl.netWorkWrapper.sendTo(new OpenClientGUIMessage(GuiEntityCapability.GUIID, this.getEntityId()), (EntityPlayerMP)player);
	        player.openGui(ZijingMod.instance, GuiEntityCapability.GUIID, world, this.getEntityId(), this.getEntityId(), this.getEntityId());
		}
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
    	double attackDamage =  this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + this.swordDamage;
    	boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)attackDamage);
        if(this.shepherdCapability.getLevel() >= ConstantUtil.CAN_LIGHTNING_LEVEL) {
        	entityIn.world.spawnEntity(new EntityLightningBolt(entityIn.world, entityIn.posX, entityIn.posY, entityIn.posZ, false));
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
        if(this.shepherdCapability.getMagic() >= ItemStaffBingxue.MagicSkill1) {
        	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	if(!this.world.isRemote) {
            	EntityThrowable entityDan;
            	if(this.world.rand.nextFloat() < 0.5D && this.shepherdCapability.getLevel() >= ConstantUtil.CAN_SHOOT_HUODAN_LEVEL) {
            		entityDan = new EntityArrowHuoDan(world, this, attackDamage, this.shepherdCapability.getLevel() >= ConstantUtil.CAN_EXPLOSION_LEVEL ? (this.shepherdCapability.getLevel() - ConstantUtil.CAN_EXPLOSION_LEVEL) * ConstantUtil.EXPLOSION_PROBABILITY_K : 0F, 1F, false);
            	}else {
            		entityDan = new EntityArrowBingDan(world, this, attackDamage, this.shepherdCapability.getLevel() * ConstantUtil.SLOWNESS_PROBABILITY_K, 80, (int)(this.shepherdCapability.getLevel() * ConstantUtil.SLOWNESS_STRENGTH_K));
            	}
        		entityDan.shoot(target.posX - this.posX, target.getEntityBoundingBox().minY + target.height * 0.75D - entityDan.posY, target.posZ - this.posZ, 3.0F, 0);
        		this.world.spawnEntity(entityDan);
        	}
    		this.world.playSound((EntityPlayer) null, this.posX, this.posY + this.getEyeHeight(), this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    		this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - ItemStaffBingxue.MagicSkill1);
			this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
        }
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(!this.isDead && this.getHealth() > 0) {
			if(this.nextLevelNeedExperience <= this.experience) {
				EntityUtil.upEntityGrade(this, 1);
				if(this.shepherdCapability.getLevel() >= ConstantUtil.IMMUNE_FIRE_LEVEL) {
					this.isImmuneToFire = true;
				}
				EntityUtil.setEntityAllValue(this);
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
			if(!this.world.isRemote) {
				if(this.nextConnectTick <= 0) {
					BaseControl.netWorkWrapper.sendToAll(new ShepherdEntityToClientMessage(this.getEntityId(), this.shepherdCapability.writeNBT(null), this.nextLevelNeedExperience, this.experience, this.swordDamage, this.armorValue));
					this.nextConnectTick = ConstantUtil.CONNECT_TICK + this.getRNG().nextInt(ConstantUtil.CONNECT_TICK);
				}else {
					this.nextConnectTick--;
				}
			}
			if(this.checkHomeTick <= 0 && EntityUtil.checkAndTryMoveToHome(this)) {
				this.checkHomeTick = ConstantUtil.CHECK_HOME_TICK + this.getRNG().nextInt(ConstantUtil.CHECK_HOME_TICK);
			}else {
				this.checkHomeTick --;
			}
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
	}

	@Override
    public int getTalkInterval(){
        return 80;
    }
	
    @SideOnly(Side.CLIENT)
    private void spawnParticles(EnumParticleTypes particleType){
        for (int i = 0; i < 5; ++i){
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(particleType, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
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
		EntityUtil.setEntityArmorValueAndSwordDamage(this);
		return true;
	}
	
	public GENDER getGender() {
		return gender;
	}

	@Override
    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions() {
		if(this.gender == GENDER.FEMALE) {
			return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityFemaleDisciple.special", new Object[0]);
		}else {
			return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityMaleDisciple.special", new Object[0]);
		}
	}
	
	public enum GENDER{
		FEMALE,
		MALE;
	}
}