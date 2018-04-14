package com.zijing.entity;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zijing.BaseControl;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.entity.ai.EntityAIPanicZJ;
import com.zijing.items.staff.ItemStaffBingxue;
import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;
import com.zijing.util.SkillEntity;
import com.zijing.util.SkillEntityShepherd;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySuperSnowman extends EntityFriendly implements IRangedAttackMob, net.minecraftforge.common.IShearable{
    private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.<Byte>createKey(EntitySnowman.class, DataSerializers.BYTE);

    public EntitySuperSnowman(World worldIn){
        super(worldIn);
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(0.7F, 1.9F);
    }

	public EntitySuperSnowman(World world, int baseLevel) {
        super(world, baseLevel);
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(0.7F, 1.9F);
	}

	@Override
    protected void initEntityAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing, BaseControl.itemDanZiling)));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, IMob.class, 4.3F, 1.0D, 1.0D));
        this.tasks.addTask(3, new EntityAIPanicZJ(this, 1.5D, 16, 5, 8, 4, 4.3D));
        this.tasks.addTask(4, new EntityAIAttackRangedZJ(this, 1.0D, (int)(15/ConstantUtil.SPECIAL_K), 4.3D, 32.0F, SkillEntity.MagicSkill_BingDan));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D, 1.0000001E-5F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityEvil.class, true, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
    }

	@Override
    protected void entityInit(){
        super.entityInit();
        this.dataManager.register(PUMPKIN_EQUIPPED, Byte.valueOf((byte)16));
    }

	@Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
    }
	
	protected void setBaseShepherdCapability() {
		super.setBaseShepherdCapability();
		this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entitySuperSnowman.name", new Object[0]));
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setBoolean("Pumpkin", this.isPumpkinEquipped());
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Pumpkin")){
            this.setPumpkinEquipped(compound.getBoolean("Pumpkin"));
        }
    }

    @Override
	protected void upEntityGrade(int upLevel) {
		EntityUtil.upEntityGrade(this, upLevel);
		if(this.shepherdCapability.getLevel() >= SkillEntity.IMMUNE_FIRE_LEVEL) {
			this.isImmuneToFire = true;
		}
		this.shepherdCapability.setMaxMagic(this.shepherdCapability.getMaxMagic() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setMagic(this.shepherdCapability.getMagic());
		this.shepherdCapability.setSpeed(this.shepherdCapability.getSpeed() * ConstantUtil.SPECIAL_K);
		this.shepherdCapability.setMagicRestore(this.shepherdCapability.getMagicRestore() * ConstantUtil.SPECIAL_K);
		EntityUtil.setEntityAllValue(this);
	}
	
    @Nullable
    protected ResourceLocation getLootTable(){
        return LootTableList.ENTITIES_SNOWMAN;
    }

    @Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
		super.canAttackClass(cls);
		if(cls == EntitySkeleton.class && this.shepherdCapability.getLevel() < 15){
        	return false;
        }else if(cls == EntityCreeper.class && this.shepherdCapability.getLevel() < 30){
            return false;
        }else if(EntityVillager.class.isAssignableFrom(cls) || EntityEnderman.class.isAssignableFrom(cls)) {
        	return false;
        }else{
            return true;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor){
    	if(!this.world.isRemote) {
    		SkillEntityShepherd.shootBingDanSkill(this, target);
    		float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
    		this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
    	}
    }

	@Override
    public float getEyeHeight(){
        return 1.7F;
    }

    public boolean isPumpkinEquipped(){
        return (((Byte)this.dataManager.get(PUMPKIN_EQUIPPED)).byteValue() & 16) != 0;
    }

    public void setPumpkinEquipped(boolean pumpkinEquipped){
        byte b0 = ((Byte)this.dataManager.get(PUMPKIN_EQUIPPED)).byteValue();
        if (pumpkinEquipped){
            this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte)(b0 | 16)));
        } else {
            this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte)(b0 & -17)));
        }
    }

    @Nullable
	@Override
    protected SoundEvent getAmbientSound(){
        return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
    }

    @Nullable
	@Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return SoundEvents.ENTITY_SNOWMAN_HURT;
    }

    @Nullable
	@Override
    protected SoundEvent getDeathSound(){
        return SoundEvents.ENTITY_SNOWMAN_DEATH;
    }

    @Override 
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) { 
    	return this.isPumpkinEquipped(); 
    }
    
    @Override
    public java.util.List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune){
        this.setPumpkinEquipped(false);
        return Lists.newArrayList();
    }


	@Override
    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions() {
		return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entitySuperSnowman.special", new Object[] {ConstantUtil.SPECIAL_K});
	}
}