package com.zijing.itf;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.data.message.ShepherdEntityToClientMessage;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;
import com.zijing.util.MathUtil;
import com.zijing.util.SkillEntity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityShepherdCapability extends EntityCreature implements IRangedAttackMob{
	protected int nextConnectTick = ConstantUtil.CONNECT_TICK;
	protected int checkHomeTick = ConstantUtil.CHECK_HOME_TICK;

	protected BlockPos homePos = BlockPos.ORIGIN;
	protected int maxDistance = -1;
	protected int baseLevel = 1;

	protected ShepherdCapability shepherdCapability;
	protected int nextLevelNeedExperience;
	protected double experience;
	protected double swordDamage;
	protected double armorValue;

    public EntityShepherdCapability(World world){
        super(world);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.setBaseShepherdCapability();
    }

	public EntityShepherdCapability(World world, int baseLevel) {
        super(world);
		this.swordDamage = 0;
		this.armorValue = 0;
		this.baseLevel = baseLevel;
		this.setBaseShepherdCapability();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}
	
	protected void setBaseShepherdCapability() {
		this.shepherdCapability = new ShepherdCapability();
		this.experience = (int) MathUtil.getUpgradeK(this.shepherdCapability.getLevel(), baseLevel - 1) * ZijingMod.config.getUPGRADE_NEED_XP_K();
		this.upEntityGrade(baseLevel - 1);
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setBoolean("isImmuneToFire", this.isImmuneToFire);
        compound.setInteger(ConstantUtil.MODID + ":checkHomeTick", this.checkHomeTick);
        compound.setInteger(ConstantUtil.MODID + ":homePosX", this.homePos.getX());
        compound.setInteger(ConstantUtil.MODID + ":homePosY", this.homePos.getY());
        compound.setInteger(ConstantUtil.MODID + ":homePosZ", this.homePos.getZ());
        compound.setInteger(ConstantUtil.MODID + ":maxDistance", this.maxDistance);
        
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
        this.checkHomeTick = compound.getInteger(ConstantUtil.MODID + ":checkHomeTick");
        int x = compound.getInteger(ConstantUtil.MODID + ":homePosX");
        int y = compound.getInteger(ConstantUtil.MODID + ":homePosY");
        int z = compound.getInteger(ConstantUtil.MODID + ":homePosZ");
        this.homePos = new BlockPos(x, y, z);
        this.maxDistance = compound.getInteger(ConstantUtil.MODID + ":maxDistance");
        
        this.swordDamage = compound.getDouble(ConstantUtil.MODID + ":swordDamage");
        this.armorValue = compound.getDouble(ConstantUtil.MODID + ":armorValue");
        this.experience = compound.getDouble(ConstantUtil.MODID + ":experience");
        this.nextLevelNeedExperience = compound.getInteger(ConstantUtil.MODID + ":nextLevelNeedExperience");
        this.shepherdCapability.readNBT(null, compound.getTag(ConstantUtil.MODID + ":shepherdCapability"));
        this.updataSwordDamageAndArmorValue();
        EntityUtil.setEntityAllValue(this);
    }

	@Override
	public void fall(float distance, float damageMultiplier) {
		boolean mainHandFlag = null != this.getHeldItemMainhand() && this.getHeldItemMainhand().getItem() == BaseControl.itemZilingZhu;
		boolean offHandFlag = null != this.getHeldItemOffhand() && this.getHeldItemOffhand().getItem() == BaseControl.itemZilingZhu;
		if(distance > 3 && (mainHandFlag || offHandFlag) && this.shepherdCapability.getMagic() >= SkillEntity.MagicSkill_ImmuneFallDamage) {
			distance = 0;
			this.shepherdCapability.setMagic(this.shepherdCapability.getMagic() - SkillEntity.MagicSkill_ImmuneFallDamage);
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
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(!this.isDead && this.getHealth() > 0) {
			if(this.nextLevelNeedExperience <= this.experience && this.shepherdCapability.getLevel() < ZijingMod.config.getMAX_LEVEL()) {
				this.upEntityGrade(1);
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
					BaseControl.netWorkWrapper.sendToAll(new ShepherdEntityToClientMessage(this.getEntityId(), this.shepherdCapability.writeNBT(null), this.nextLevelNeedExperience, this.experience, this.swordDamage, this.armorValue, this.homePos, this.maxDistance));
					this.nextConnectTick = ConstantUtil.CONNECT_TICK + this.getRNG().nextInt(ConstantUtil.CONNECT_TICK);
				}else {
					this.nextConnectTick--;
				}
			}
			if(this.maxDistance > 0) {
//				if(this.checkHomeTick <= 0 && EntityUtil.checkAndTryMoveToHome(this)) {
//					this.checkHomeTick = ConstantUtil.CHECK_HOME_TICK + this.getRNG().nextInt(ConstantUtil.CHECK_HOME_TICK);
//				}else {
//					this.checkHomeTick --;
//				}
			}
		}
	}

	//Over write this function to add special about level.
	protected void upEntityGrade(int upLevel) {
		EntityUtil.upEntityGrade(this, 1);
		if(this.shepherdCapability.getLevel() >= SkillEntity.IMMUNE_FIRE_LEVEL) {
			this.isImmuneToFire = true;
		}
		EntityUtil.setEntityAllValue(this);
	}
	
	@Override
	public void setSwingingArms(boolean swingingArms) {
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
	}
	
    @SideOnly(Side.CLIENT)
    protected void spawnParticles(EnumParticleTypes particleType){
        for (int i = 0; i < 5; ++i){
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(particleType, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }
	
	public BlockPos getHomePos() {
		return homePos;
	}

	public void setHomePos(BlockPos homePos) {
		this.homePos = homePos;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}
	
	public double getExperience() {
		return this.experience;
	}

	public void setExperience(double xp) {
		this.experience = xp;
	}

	public int getNextLevelNeedExperience() {
		return nextLevelNeedExperience;
	}

	public void setNextLevelNeedExperience(int nextLevelNeedExperience) {
		this.nextLevelNeedExperience = nextLevelNeedExperience;
	}

	public ShepherdCapability getShepherdCapability() {
		return this.shepherdCapability;
	}

	public double getSwordDamage() {
		return swordDamage;
	}
	
	public void setSwordDamage(double swordDamage) {
		this.swordDamage = swordDamage;
	}

	public double getArmorValue() {
		return armorValue;
	}

	public void setArmorValue(double armorValue) {
		this.armorValue = armorValue;
	}

	public boolean updataSwordDamageAndArmorValue() {
		EntityUtil.setEntityArmorValueAndSwordDamage(this);
		return true;
	}
	
    @SideOnly(Side.CLIENT)
	public abstract String getSpecialInstructions();
}
