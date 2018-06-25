package com.zijing.entity;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.itf.EntityEvil;
import com.zijing.itf.EntityFriendly;
import com.zijing.itf.EntityShepherdCapability;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowFengyinDan extends EntityThrowable {
	private float attackDamage = 0;
	private boolean checkFaction = false;
	
	public EntityArrowFengyinDan(World a) {
		super(a);
	}

	public EntityArrowFengyinDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
	
	public EntityArrowFengyinDan(World worldIn, double x, double y, double z, float attackDamage) {
		this(worldIn, x, y, z);
		this.attackDamage = attackDamage;
	}

	public EntityArrowFengyinDan(World worldIn, double x, double y, double z, float attackDamage, boolean checkFaction) {
		this(worldIn, x, y, z, attackDamage);
		this.checkFaction = checkFaction;
	}

	public EntityArrowFengyinDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowFengyinDan(World worldIn, EntityLivingBase shooter, float attackDamage) {
		this(worldIn, shooter);
		this.attackDamage = attackDamage;
	}

	public EntityArrowFengyinDan(World worldIn, EntityLivingBase shooter, float attackDamage, boolean checkFaction) {
		this(worldIn, shooter, attackDamage);
		this.checkFaction = checkFaction;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
    protected void onImpact(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;
		BlockPos blockPos = raytraceResultIn.getBlockPos();
		if(null != entity && !this.world.isRemote && entity instanceof EntityLivingBase) {
			if(checkCanAttack((EntityLivingBase)entity)) {
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
				int throwerLevel = 1;
				int entityLevel = 1;
				
				if(this.thrower instanceof EntityPlayer && ShepherdProvider.hasCapabilityFromPlayer(this.thrower)) {
					throwerLevel = ShepherdProvider.getCapabilityFromPlayer(this.thrower).getLevel();
				}else if(this.thrower instanceof EntityShepherdCapability) {
					throwerLevel = ((EntityShepherdCapability)this.thrower).getShepherdCapability().getLevel();
				}
				
				if(entity instanceof EntityPlayer || !entity.isNonBoss()) {
					entityLevel = ZijingMod.config.getMAX_LEVEL();
				}else if(entity instanceof EntityFriendly) {
					entityLevel = ((EntityFriendly)entity).getShepherdCapability().getLevel();
				}else if(entity instanceof EntityEvil) {
					entityLevel = ((EntityEvil)entity).getShepherdCapability().getLevel();
				}
				
				float randomValue = (throwerLevel - entityLevel) / 100.0F;
				
				if(this.world.rand.nextFloat() < randomValue) {
					ItemStack itemStack = new ItemStack(BaseControl.itemCardFengyin);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setTag(ConstantUtil.MODID + ":entityNBT", entity.writeToNBT(new NBTTagCompound()));
					nbt.setString(ConstantUtil.MODID + ":entityName", entity.getClass().getName());
					itemStack.setTagCompound(nbt);
		            EntityItem entityitem = new EntityItem(this.world, entity.posX, entity.posY, entity.posZ, itemStack);
		            entityitem.setDefaultPickupDelay();
					this.world.spawnEntity(entityitem);
		            entity.setDead();
				}
				world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				this.setDead();
			}
		}else if(null != blockPos && !this.world.isRemote){
			if(!canThrough(this.world.getBlockState(blockPos))){
				this.setDead();
			}
		}
    }
	
	private boolean checkCanAttack(EntityLivingBase entity) {
		boolean canAttackFlag = true;
		if(null != this.thrower) {
			if(this.thrower instanceof EntityPlayer) {
				if(entity instanceof EntityPlayer) {
					canAttackFlag = false;
				}else if(checkFaction && (entity instanceof EntityFriendly || entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityGolem)) {
					canAttackFlag = false;
				}
			}else if(this.thrower instanceof EntityFriendly) {
				if(entity instanceof EntityFriendly || entity instanceof EntityPlayer) {
					canAttackFlag = false;
				}else if(checkFaction && (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityGolem)) {
					canAttackFlag = false;
				}
			}else if(this.thrower instanceof EntityEvil) {
				if(entity instanceof EntityEvil) {
					canAttackFlag = false;
				}else if(checkFaction && entity instanceof IMob) {
					canAttackFlag = false;
				}
			}
		}
		return canAttackFlag;
	}
	
	private boolean canThrough(IBlockState blockState) {
		boolean canThroughFlag = false;
		Material material = blockState.getMaterial();
		if(material == Material.AIR || material == Material.GRASS || material == Material.WATER
				|| material == Material.LAVA || material == Material.PLANTS || material == Material.FIRE
				|| material == Material.VINE || material == Material.CIRCUITS || material == Material.WEB
				|| material == Material.CARPET) {
			canThroughFlag = true;
		}
		return canThroughFlag;
	}

	public float getAttackDamage() {
		return attackDamage;
	}
}
