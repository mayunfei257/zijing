package com.zijing.entity;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.data.message.ShepherdEntityToClientMessage;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIDefendVillageZJ;
import com.zijing.entity.ai.EntityAILookAtVillagerZJ;
import com.zijing.gui.GuiEntityCapability;
import com.zijing.items.staff.ItemStaffKongjian;
import com.zijing.itf.EntityHasShepherdCapability;
import com.zijing.itf.ItemDan;
import com.zijing.itf.MagicSource;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;
import com.zijing.util.MathUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySuperIronGolem extends EntityGolem implements EntityHasShepherdCapability, IRangedAttackMob{
    private int homeCheckTimer;
    @Nullable
    Village village;
    private int attackTimer;
    private int holdRoseTick;

	private int nextConnectTick = ConstantUtil.CONNECT_TICK;
	private int baseLevel = 1;
	private int burningTime = 2;

	private int nextLevelNeedExperience;
	private double experience;
	private ShepherdCapability shepherdCapability;
	private double swordDamage;
	private double armorValue;

    public EntitySuperIronGolem(World worldIn){
        super(worldIn);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(1.4F, 2.7F);
    }

	public EntitySuperIronGolem(World world, int baseLevel) {
        super(world);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.baseLevel = baseLevel;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(1.4F, 2.7F);
	}

	@Override
    protected void initEntityAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing, BaseControl.itemDanZiling)));
//		this.tasks.addTask(2, new EntityAIAttackRangedZJ(this, 1.0D, 20, 6D, 32.0F, ItemStaffKongjian.MagicSkill1));
        this.tasks.addTask(3, new EntityAIAttackMeleeZJ(this, 1.1D, 10, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookAtVillagerZJ(this));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillageZJ(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, new Predicate<EntityLiving>(){
            public boolean apply(@Nullable EntityLiving p_apply_1_){
                return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_);
            }
        }));
    }

	@Override
    protected void updateAITasks(){
        if (--this.homeCheckTimer <= 0){
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(this), 32);
            if (this.village == null){
                this.detachHome();
            } else {
                BlockPos blockpos = this.village.getCenter();
                this.setHomePosAndDistance(blockpos, (int)((float)this.village.getVillageRadius() * 0.6F));
            }
        }
        super.updateAITasks();
    }

	@Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
    }

	private void setBaseShepherdCapability() {
		this.shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(this.shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K()/2;
		EntityUtil.upEntityGrade(this, baseLevel - 1);
		this.shepherdCapability.setMaxBlood(this.shepherdCapability.getMaxBlood() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setBlood(this.shepherdCapability.getMaxBlood());
		this.shepherdCapability.setPhysicalDefense(this.shepherdCapability.getPhysicalDefense() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setBloodRestore(this.shepherdCapability.getBloodRestore() * ConstantUtil.SPECIAL_K);
		EntityUtil.setEntityAllValue(this);
		this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entitySuperIronGolem.name", new Object[0]));
	}
	
    /**
     * Decrements the entity's air supply when underwater
     */
	@Override
    protected int decreaseAirSupply(int air){
        return air;
    }

	@Override
    protected void collideWithEntity(Entity entityIn){
        if (entityIn instanceof IMob && this.getRNG().nextInt(20) == 0){
            this.setAttackTarget((EntityLivingBase)entityIn);
        }
        super.collideWithEntity(entityIn);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
	@Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        if (this.attackTimer > 0){
            --this.attackTimer;
        }
        if (this.holdRoseTick > 0){
            --this.holdRoseTick;
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0){
            IBlockState iblockstate = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY - 0.20000000298023224D), MathHelper.floor(this.posZ)));
            if (iblockstate.getMaterial() != Material.AIR){
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(iblockstate));
            }
        }
		if(!this.isDead && this.getHealth() > 0) {
			if(this.nextLevelNeedExperience <= this.experience) {
				EntityUtil.upEntityGrade(this, 1);
				this.shepherdCapability.setMaxBlood(this.shepherdCapability.getMaxBlood() * ConstantUtil.SPECIAL_K);
				this.shepherdCapability.setBlood(this.shepherdCapability.getMaxBlood());
				this.shepherdCapability.setPhysicalDefense(this.shepherdCapability.getPhysicalDefense() * ConstantUtil.SPECIAL_K);
				this.shepherdCapability.setBloodRestore(this.shepherdCapability.getBloodRestore() * ConstantUtil.SPECIAL_K);
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
		}
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
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
        }else if(EntityVillager.class.isAssignableFrom(cls) || EntityGhast.class.isAssignableFrom(cls) || EntityDragon.class.isAssignableFrom(cls) || EntityWither.class.isAssignableFrom(cls)) {
        	return false;
        }else{
            return true;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setDouble(ConstantUtil.MODID + ":swordDamage", this.swordDamage);
        compound.setDouble(ConstantUtil.MODID + ":armorValue", this.armorValue);
        compound.setDouble(ConstantUtil.MODID + ":experience", this.experience);
        compound.setInteger(ConstantUtil.MODID + ":nextLevelNeedExperience", this.nextLevelNeedExperience);
        compound.setTag(ConstantUtil.MODID + ":shepherdCapability", this.shepherdCapability.writeNBT(null));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.swordDamage = compound.getDouble(ConstantUtil.MODID + ":swordDamage");
        this.armorValue = compound.getDouble(ConstantUtil.MODID + ":armorValue");
        this.experience = compound.getDouble(ConstantUtil.MODID + ":experience");
        this.nextLevelNeedExperience = compound.getInteger(ConstantUtil.MODID + ":nextLevelNeedExperience");
        this.shepherdCapability.readNBT(null, compound.getTag(ConstantUtil.MODID + ":shepherdCapability"));
        this.updataSwordDamageAndArmorValue();
        EntityUtil.setEntityAllValue(this);
    }

	@Override
    public boolean attackEntityAsMob(Entity entityIn){
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);

    	double attackDamage =  this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + this.swordDamage;
    	boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)attackDamage);
		this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
		
        if (flag){
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
            if(this.shepherdCapability.getMagic() >= 1) {
            	entityIn.setFire(this.burningTime);
        		this.experience += this.burningTime * ConstantUtil.EXPERIENCE_MAGNIFICATION;
            	this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - 1);
            }
        }
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
	@Override
    public void handleStatusUpdate(byte id){
        if (id == 4){
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        } else if (id == 11){
            this.holdRoseTick = 400;
        } else if (id == 34){
            this.holdRoseTick = 0;
        } else{
            super.handleStatusUpdate(id);
        }
    }

    public Village getVillage(){
        return this.village;
    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer(){
        return this.attackTimer;
    }

    public void setHoldingRose(boolean p_70851_1_){
        if (p_70851_1_){
            this.holdRoseTick = 400;
            this.world.setEntityState(this, (byte)11);
        } else {
            this.holdRoseTick = 0;
            this.world.setEntityState(this, (byte)34);
        }
    }

	@Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return SoundEvents.ENTITY_IRONGOLEM_HURT;
    }

	@Override
    protected SoundEvent getDeathSound(){
        return SoundEvents.ENTITY_IRONGOLEM_DEATH;
    }

	@Override
    protected void playStepSound(BlockPos pos, Block blockIn){
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }

    @Nullable
	@Override
    protected ResourceLocation getLootTable(){
        return LootTableList.ENTITIES_IRON_GOLEM;
    }

    public int getHoldRoseTick(){
        return this.holdRoseTick;
    }
    
	@Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(itemStack.getItem() instanceof MagicSource && this.shepherdCapability.getMagic() < this.shepherdCapability.getMaxMagic()) {
			this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMaxMagic(), this.shepherdCapability.getMagic() + ((MagicSource)itemStack.getItem()).getMagicEnergy()));
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			itemStack.shrink(1);
		}else if(itemStack.getItem() instanceof ItemDan){
			((ItemDan)itemStack.getItem()).onFoodEatenByEntityLivingBase(this);
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			itemStack.shrink(1);
		}else if(!this.world.isRemote) {
	        player.openGui(ZijingMod.instance, GuiEntityCapability.GUIID, world, this.getEntityId(), this.getEntityId(), this.getEntityId());
		}
		return true;
    }
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if(this.shepherdCapability.getMagic() >= ItemStaffKongjian.MagicSkill1) {
        	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	if(!this.world.isRemote) {
            	EntityArrowXukongDan xukongDan = new EntityArrowXukongDan(world, this, attackDamage);
        		xukongDan.shoot(target.posX - this.posX, target.getEntityBoundingBox().minY + target.height * 0.75D - xukongDan.posY, target.posZ - this.posZ, 3.0F, 0);
        		this.world.spawnEntity(xukongDan);
        	}
    		this.world.playSound((EntityPlayer) null, this.posX, this.posY + this.getEyeHeight(), this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    		this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - ItemStaffKongjian.MagicSkill1);
			this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
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

	@Override
    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions() {
		return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entitySuperIronGolem.special", new Object[] {ConstantUtil.SPECIAL_K});
	}
}