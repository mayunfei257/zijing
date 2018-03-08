package com.zijing.entity;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.zijing.ZijingMod;
import com.zijing.entity.ai.EntityAIAttackRangedZJ;
import com.zijing.entity.ai.EntityAIPanicZJ;
import com.zijing.items.ItemDanZiling;
import com.zijing.items.staff.ItemStaffBingxue;
import com.zijing.main.BaseControl;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.itf.MagicSource;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.util.EntityUtil;
import com.zijing.util.MathUtil;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySummonSnowman extends EntityGolem implements EntityHasShepherdCapability,IRangedAttackMob, net.minecraftforge.common.IShearable{
    private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.<Byte>createKey(EntitySnowman.class, DataSerializers.BYTE);
	private int baseLevel = 1;
	private int nextLevelNeedExperience;
	private double experience;
	private ShepherdCapability shepherdCapability;
	private double swordDamage;
	private double armorValue;

    public EntitySummonSnowman(World worldIn){
        super(worldIn);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(0.7F, 1.9F);
    }

	public EntitySummonSnowman(World world, int baseLevel) {
        super(world);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.baseLevel = baseLevel;
		this.setBaseShepherdCapability();
		this.setNoAI(false);
		this.enablePersistence();
		this.setAlwaysRenderNameTag(true);
        this.setSize(0.7F, 1.9F);
	}

	@Override
    protected void initEntityAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAITempt(this, 1.0D, false, Sets.newHashSet(BaseControl.itemZiqi, BaseControl.itemZijing, BaseControl.itemDanZiling)));
        this.tasks.addTask(2, new EntityAIPanicZJ(this, 1.5D, 16, 5, 8, 4, 4.3D));
        this.tasks.addTask(3, new EntityAIAttackRangedZJ(this, 1.0D, 15, 4.3D, 32.0F, ItemStaffBingxue.MagicSkill1));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D, 1.0000001E-5F));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
    }

	@Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

	@Override
    protected void entityInit(){
        super.entityInit();
        this.dataManager.register(PUMPKIN_EQUIPPED, Byte.valueOf((byte)16));
    }
	
	private void setBaseShepherdCapability() {
		this.shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(this.shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K()/2;
		EntityUtil.upEntityGrade(this, baseLevel - 1);
		this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonSnowman.name", new Object[] {this.shepherdCapability.getLevel()}));
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setBoolean("Pumpkin", this.isPumpkinEquipped());
        compound.setDouble(ZijingMod.MODID + ":swordDamage", this.swordDamage);
        compound.setDouble(ZijingMod.MODID + ":armorValue", this.armorValue);
        compound.setDouble(ZijingMod.MODID + ":experience", this.experience);
        compound.setInteger(ZijingMod.MODID + ":nextLevelNeedExperience", this.nextLevelNeedExperience);
        compound.setTag(ZijingMod.MODID + ":shepherdCapability", this.shepherdCapability.writeNBT(null));
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Pumpkin")){
            this.setPumpkinEquipped(compound.getBoolean("Pumpkin"));
        }
        this.swordDamage = compound.getDouble(ZijingMod.MODID + ":swordDamage");
        this.armorValue = compound.getDouble(ZijingMod.MODID + ":armorValue");
        this.experience = compound.getDouble(ZijingMod.MODID + ":experience");
        this.nextLevelNeedExperience = compound.getInteger(ZijingMod.MODID + ":nextLevelNeedExperience");
        this.shepherdCapability.readNBT(null, compound.getTag(ZijingMod.MODID + ":shepherdCapability"));
        this.updataSwordDamageAndArmorValue();
        EntityUtil.setEntityAllValue(this);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
	@Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        if (!this.world.isRemote){
            for (int l = 0; l < 4; ++l){
                BlockPos blockpos = new BlockPos(MathHelper.floor(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25F)), MathHelper.floor(this.posY), MathHelper.floor(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F)));
                if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR && this.world.getBiome(blockpos).getTemperature(blockpos) < 0.8F && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockpos)){
                    this.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
                }
            }
        }
		if(!this.world.isRemote && !this.isDead && this.getHealth() > 0) {
			if(this.nextLevelNeedExperience <= this.experience) {
				EntityUtil.upEntityGrade(this, 1);
				this.setCustomNameTag(I18n.translateToLocalFormatted(ZijingMod.MODID + ".entitySummonSnowman.name", new Object[] {this.shepherdCapability.getLevel()}));
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

    @Nullable
    protected ResourceLocation getLootTable(){
        return LootTableList.ENTITIES_SNOWMAN;
    }

    @Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
        if (EntityPlayer.class.isAssignableFrom(cls) || EntityHasShepherdCapability.class.isAssignableFrom(cls)){
        	return false;
        }else if(cls == EntitySkeleton.class && this.shepherdCapability.getLevel() < 15){
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
        if(!this.world.isRemote && this.shepherdCapability.getMagic() >= ItemStaffBingxue.MagicSkill1) {
        	float attackDamage =  (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
    		EntityArrowBingDan bingDan = new EntityArrowBingDan(world, this, attackDamage, 0.125F, 2);
    		bingDan.shoot(target.posX - this.posX, target.getEntityBoundingBox().minY + target.height * 0.75D - bingDan.posY, target.posZ - this.posZ, 3.0F, 0);
    		this.world.spawnEntity(bingDan);
    		this.world.playSound((EntityPlayer) null, this.posX, this.posY + 1D, this.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
    		this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - ItemStaffBingxue.MagicSkill1);
			this.experience += attackDamage;
        }
    }

	@Override
    public float getEyeHeight(){
        return 1.7F;
    }

	@Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand){
		if(!this.world.isRemote) {
			ItemStack itemStack = player.getHeldItem(hand);
			if(itemStack.getItem() instanceof MagicSource && this.shepherdCapability.getMagic() < this.shepherdCapability.getMaxMagic()) {
				this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMaxMagic(), this.shepherdCapability.getMagic() + ((MagicSource)itemStack.getItem()).getMagicEnergy()));
				itemStack.shrink(1);
			}else if(itemStack.getItem() == BaseControl.itemDanZiling){
				((ItemDanZiling)BaseControl.itemDanZiling).onFoodEatenByEntityLivingBase(this);
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
		player.sendMessage(new TextComponentString("power: " + df2.format(this.shepherdCapability.getPower())));
		player.sendMessage(new TextComponentString("bloodRestore: " + df4.format(this.shepherdCapability.getBloodRestore()) + "/T"));
		player.sendMessage(new TextComponentString("magicRestore: " + df4.format(this.shepherdCapability.getMagicRestore()) + "/T"));
		player.sendMessage(new TextComponentString("physicalDefense: " + df2.format(this.shepherdCapability.getPhysicalDefense())));
		player.sendMessage(new TextComponentString("experience: " + df2.format(this.experience) + "/" + this.nextLevelNeedExperience));
		return true;
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

    @Override public boolean isShearable(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos) { return this.isPumpkinEquipped(); }
    @Override
    public java.util.List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune){
        this.setPumpkinEquipped(false);
        return com.google.common.collect.Lists.newArrayList();
    }

	@Override
    public void setSwingingArms(boolean swingingArms){
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