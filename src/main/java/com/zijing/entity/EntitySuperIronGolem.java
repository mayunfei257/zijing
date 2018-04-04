package com.zijing.entity;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.zijing.BaseControl;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIDefendVillageZJ;
import com.zijing.entity.ai.EntityAILookAtVillagerZJ;
import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;
import com.zijing.util.SkillEntity;

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
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySuperIronGolem extends EntityFriendly implements IRangedAttackMob{
    private int homeCheckTimer;
    @Nullable
    Village village;
    private int attackTimer;
    private int holdRoseTick;
	private int burningTime = 2;


    public EntitySuperIronGolem(World worldIn){
        super(worldIn);
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(1.4F, 2.7F);
    }

	public EntitySuperIronGolem(World world, int baseLevel) {
        super(world, baseLevel);
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
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEvil.class, true, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, new Predicate<EntityLiving>(){
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
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
    }

	protected void setBaseShepherdCapability() {
		super.setBaseShepherdCapability();
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
    }

    @Override
	protected void upEntityGrade(int upLevel) {
		EntityUtil.upEntityGrade(this, upLevel);
		if(this.shepherdCapability.getLevel() >= SkillEntity.IMMUNE_FIRE_LEVEL) {
			this.isImmuneToFire = true;
		}
		this.shepherdCapability.setMaxBlood(this.shepherdCapability.getMaxBlood() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setBlood(this.shepherdCapability.getMaxBlood());
		this.shepherdCapability.setPhysicalDefense(this.shepherdCapability.getPhysicalDefense() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setBloodRestore(this.shepherdCapability.getBloodRestore() * ConstantUtil.SPECIAL_K);
		EntityUtil.setEntityAllValue(this);
	}
    
    /**
     * Returns true if this entity can attack entities of the specified class.
     */
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
		super.canAttackClass(cls);
		if(cls == EntitySkeleton.class && this.shepherdCapability.getLevel() < 15){
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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		
	}

	@Override
    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions() {
		return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entitySuperIronGolem.special", new Object[] {ConstantUtil.SPECIAL_K});
	}
}