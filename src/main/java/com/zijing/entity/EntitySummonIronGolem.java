package com.zijing.entity;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.zijing.ZijingMod;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.entity.ai.EntityAIDefendVillageZJ;
import com.zijing.entity.ai.EntityAILookAtVillagerZJ;
import com.zijing.items.staff.ItemStaffKongjian;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.playerdata.ShepherdCapability;
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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySummonIronGolem extends EntityGolem implements EntityHasShepherdCapability, IRangedAttackMob{
    private int homeCheckTimer;
    @Nullable
    Village village;
    private int attackTimer;
    private int holdRoseTick;
    
	private int baseLevel = 1;
	private int nextLevelNeedExperience;
	private double experience;
	private ShepherdCapability shepherdCapability;
	private double swordDamage;
	private double armorValue;

    public EntitySummonIronGolem(World worldIn){
        super(worldIn);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(1.4F, 2.7F);
    }

	public EntitySummonIronGolem(World world, int baseLevel) {
		this(world);
		this.baseLevel = baseLevel;
	}

	@Override
    protected void initEntityAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackRangedZJ(this, 1.0D, 20, 6D, 32.0F, ItemStaffKongjian.MagicSkill1));
        this.tasks.addTask(2, new EntityAIAttackMeleeZJ(this, 1.0D, 10, true));
        this.tasks.addTask(3, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(4, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAILookAtVillagerZJ(this));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
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
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4.0D);
    }

	private void setBaseShepherdCapability() {
		this.shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(this.shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K()/2;
		EntityUtil.upEntityGrade(this, baseLevel - 1);
		this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonIronGolem.name", new Object[] {this.shepherdCapability.getLevel()}));
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
        if (entityIn instanceof IMob && !(entityIn instanceof EntityCreeper) && this.getRNG().nextInt(20) == 0){
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
		if(!this.world.isRemote && !this.isDead && this.getHealth() > 0) {
			if(this.nextLevelNeedExperience <= this.experience) {
				EntityUtil.upEntityGrade(this, 1);
				this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonIronGolem.name", new Object[] {this.shepherdCapability.getLevel()}));
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

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
        if (EntityPlayer.class.isAssignableFrom(cls) || EntityHasShepherdCapability.class.isAssignableFrom(cls)){
        	return false;
        }else if(cls == EntitySkeleton.class && this.shepherdCapability.getLevel() < 20){
        	return false;
        }else if(cls == EntityCreeper.class && this.shepherdCapability.getLevel() < 40){
            return false;
        }else if(cls == EntityEnderman.class && this.shepherdCapability.getLevel() < 60){
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
        compound.setDouble(ZijingMod.MODID + ":swordDamage", this.swordDamage);
        compound.setDouble(ZijingMod.MODID + ":armorValue", this.armorValue);
        compound.setDouble(ZijingMod.MODID + ":experience", this.experience);
        compound.setInteger(ZijingMod.MODID + ":nextLevelNeedExperience", this.nextLevelNeedExperience);
        compound.setTag(ZijingMod.MODID + ":shepherdCapability", this.shepherdCapability.writeNBT(null));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.swordDamage = compound.getDouble(ZijingMod.MODID + ":swordDamage");
        this.armorValue = compound.getDouble(ZijingMod.MODID + ":armorValue");
        this.experience = compound.getDouble(ZijingMod.MODID + ":experience");
        this.nextLevelNeedExperience = compound.getInteger(ZijingMod.MODID + ":nextLevelNeedExperience");
        this.shepherdCapability.readNBT(null, compound.getTag(ZijingMod.MODID + ":shepherdCapability"));
    }

	@Override
    public boolean attackEntityAsMob(Entity entityIn){
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        
    	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
    	ItemStack itemStack = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    	if(null != itemStack && ItemStack.EMPTY != itemStack && itemStack.getItem() instanceof ItemSword) {
    		attackDamage += ((ItemSword)itemStack.getItem()).getAttackDamage();
    	}
    	boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);
		this.experience += attackDamage;
		
        if (flag){
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
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
		DecimalFormat df1 = new DecimalFormat("#0.0");
		DecimalFormat df2 = new DecimalFormat("#0.00");
		DecimalFormat df4 = new DecimalFormat("#0.0000");
		player.sendMessage(new TextComponentString("level: " + this.shepherdCapability.getLevel()));
		player.sendMessage(new TextComponentString("blood: " + df1.format(this.shepherdCapability.getBlood()) + "/" + df1.format(this.shepherdCapability.getMaxBlood())));
		player.sendMessage(new TextComponentString("magic: " + df1.format(this.shepherdCapability.getMagic()) + "/" + df1.format(this.shepherdCapability.getMaxMagic())));
		player.sendMessage(new TextComponentString("speed: " + df2.format(this.shepherdCapability.getSpeed())));
		player.sendMessage(new TextComponentString("power: " + df2.format(this.shepherdCapability.getPower()) + " + " + df2.format(this.swordDamage)));
		player.sendMessage(new TextComponentString("bloodRestore: " + df4.format(this.shepherdCapability.getBloodRestore()) + "/T"));
		player.sendMessage(new TextComponentString("magicRestore: " + df4.format(this.shepherdCapability.getMagicRestore()) + "/T"));
		player.sendMessage(new TextComponentString("physicalDefense: " + df2.format(this.shepherdCapability.getPhysicalDefense())  + " + " + df2.format(this.armorValue)));
		player.sendMessage(new TextComponentString("experience: " + df2.format(this.experience) + "/" + this.nextLevelNeedExperience));
		return true;
    }
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if(!this.world.isRemote && this.shepherdCapability.getMagic() >= ItemStaffKongjian.MagicSkill1) {
        	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        	EntityArrowXukongDan xukongDan = new EntityArrowXukongDan(world, this, attackDamage);
    		xukongDan.shoot(target.posX - this.posX, target.getEntityBoundingBox().minY + target.height * 0.75D - xukongDan.posY, target.posZ - this.posZ, 3.0F, 0);
    		this.world.spawnEntity(xukongDan);
    		this.world.playSound((EntityPlayer) null, this.posX, this.posY + 1D, this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    		this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - ItemStaffKongjian.MagicSkill1);
			this.experience += attackDamage;
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
		EntityUtil.setEntityArmorValueAndSwordDamage(this);
		return true;
	}
}