package com.zijing.entity;

import com.zijing.ZijingMod;
import com.zijing.main.BaseControl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityArrowFengyinDan extends EntityThrowable {
	
	public EntityArrowFengyinDan(World a) {
		super(a);
	}

	public EntityArrowFengyinDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowFengyinDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
    protected void onImpact(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;
		BlockPos blockPos = raytraceResultIn.getBlockPos();
		if(null != entity && !this.world.isRemote && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
			if(this.world.rand.nextFloat() < 0.125D) {
				ItemStack itemStack = new ItemStack(BaseControl.itemCardFengyin);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setTag(ZijingMod.MODID + ":entityNBT", entity.writeToNBT(new NBTTagCompound()));
				nbt.setString(ZijingMod.MODID + ":entityName", entity.getClass().getName());
				itemStack.setTagCompound(nbt);
	            EntityItem entityitem = new EntityItem(this.world, entity.posX, entity.posY, entity.posZ, itemStack);
	            entityitem.setDefaultPickupDelay();
				this.world.spawnEntity(entityitem);
	            entity.setDead();
			}
			this.setDead();
		}else if(null != blockPos && !this.world.isRemote){
			this.setDead();
		}
    }
}
