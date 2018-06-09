package com.zijing.entity;

import com.google.common.collect.Sets;
import com.zijing.BaseControl;
import com.zijing.entity.ai.EntityAIAttackMeleeZJ;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.entity.ai.EntityAIMoveToHomeZJ;
import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;
import com.zijing.itf.ItemStaff;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;
import com.zijing.util.EnumGender;
import com.zijing.util.SkillEntity;

import net.minecraft.entity.Entity;
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
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDisciple extends EntityFriendly implements IRangedAttackMob{
	private EnumGender gender = EnumGender.FEMALE;
	
	public EntityDisciple(World world) {
		super(world);
//		this.gender = this.getRNG().nextInt(2) == 0 ?  EnumGender.FEMALE :  EnumGender.MALE;
		this.setNoAI(false);
		this.enablePersistence();
		if(EnumGender.FEMALE.equals(this.gender)) {
			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityFemaleDisciple.name", new Object[0]));
		}else {
			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityMaleDisciple.name", new Object[0]));
		}
		this.setAlwaysRenderNameTag(true);
	}

	public EntityDisciple(World world, int baseLevel, String genderType) {
		super(world, baseLevel);
		this.gender = EnumGender.getEnumWithType(genderType);
		this.setNoAI(false);
		this.enablePersistence();
		if(EnumGender.FEMALE.equals(this.gender)) {
			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityFemaleDisciple.name", new Object[0]));
		}else {
			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityMaleDisciple.name", new Object[0]));
		}
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing, BaseControl.itemDanZiling, Item.getItemFromBlock(Blocks.RED_FLOWER), Item.getItemFromBlock(Blocks.YELLOW_FLOWER))));
        this.tasks.addTask(2, new EntityAIAttackRangedZJ(this, 1.0D, 15, 3.0D, 16.0F, SkillEntity.MagicSkill_BingDan));
        this.tasks.addTask(3, new EntityAIAttackMeleeZJ(this, 1.0D, 10, false));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsTarget(this, 1D, 16.0F));
        this.tasks.addTask(6, new EntityAIMoveToHomeZJ(this, 1.0D, 32, 8));
		this.tasks.addTask(7, new EntityAIWander(this, 0.9D));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityEvil.class, 2, true, false, IMob.MOB_SELECTOR));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 2, true, false, IMob.MOB_SELECTOR));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
	}

	@Override
	protected void setBaseShepherdCapability() {
		super.setBaseShepherdCapability();
//		if(EnumGender.FEMALE.equals(this.gender)) {
//			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityFemaleDisciple.name", new Object[0]));
//		}else {
//			this.setCustomNameTag(I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityMaleDisciple.name", new Object[0]));
//		}
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setString(ConstantUtil.MODID + ":gender", EnumGender.FEMALE.getType());
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.gender = EnumGender.getEnumWithType(compound.getString(ConstantUtil.MODID + ":gender"));
        this.gender = null == this.gender ? EnumGender.FEMALE : EnumGender.MALE;
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
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		super.processInteract(player, hand);
		player.sendMessage(new TextComponentString("genderType: " + this.gender.getType()));
		return true;
	}
//	else if(!this.world.isRemote) {// && player instanceof EntityPlayerMP
//		EntityPlayerMP playerMp = (EntityPlayerMP)player;
//		playerMp.getNextWindowId();
//		playerMp.openContainer = new GuiEntityCapability.MyContainer(world, this, playerMp);
//		playerMp.openContainer.windowId = playerMp.currentWindowId;
//		playerMp.openContainer.addListener(playerMp);
//      MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(playerMp, playerMp.openContainer));
//      BaseControl.netWorkWrapper.sendTo(new OpenClientGUIMessage(GuiEntityCapability.GUIID, this.getEntityId()), (EntityPlayerMP)player);
//      player.openGui(ZijingMod.instance, GuiEntityCapability.GUIID, world, this.getEntityId(), this.getEntityId(), this.getEntityId());
//	}
    
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
		if((cls == EntitySkeleton.class || cls == EntityStray.class) && this.shepherdCapability.getLevel() < 15){
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
		if(!this.world.isRemote && null != this.getHeldItemOffhand()) {
			Item item = this.getHeldItemOffhand().getItem();
			if(item instanceof ItemStaff) {
				double attackDamage = ((ItemStaff) item).skill1(this, target);
				this.experience += attackDamage * ConstantUtil.EXPERIENCE_MAGNIFICATION;
			}
		}
	}

    @Override
	protected void upEntityGrade(int upLevel) {
		EntityUtil.upEntityGrade(this, upLevel);
		if(this.shepherdCapability.getLevel() >= SkillEntity.IMMUNE_FIRE_LEVEL) {
			this.isImmuneToFire = true;
		}
		EntityUtil.setEntityAllValue(this);
	}
	
	public EnumGender getGender() {
		return gender;
	}

	@Override
    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions() {
		if(EnumGender.FEMALE.getType().equals(this.gender.getType())) {
			return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityFemaleDisciple.special", new Object[0]);
		}else {
			return I18n.translateToLocalFormatted(ConstantUtil.MODID + ".entityMaleDisciple.special", new Object[0]);
		}
	}
}