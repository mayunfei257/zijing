package com.zijing.entity;

import com.zijing.ZijingMod;
import com.zijing.main.BaseControl;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
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
	
	public EntityArrowFengyinDan(World a) {
		super(a);
	}

	public EntityArrowFengyinDan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityArrowFengyinDan(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowFengyinDan(World worldIn, EntityLivingBase shooter, float attackDamage) {
		super(worldIn, shooter);
		this.attackDamage = attackDamage;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
    protected void onImpact(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;
		BlockPos blockPos = raytraceResultIn.getBlockPos();
		if(null != entity && !this.world.isRemote && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && entity != this.thrower) {
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.attackDamage);
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
			world.playSound((EntityPlayer) null, entity.posX, entity.posY + 0.5D, entity.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			this.setDead();
		}else if(null != blockPos && !this.world.isRemote){
			Block block = this.world.getBlockState(blockPos).getBlock();
			if(block != Blocks.TALLGRASS && block != Blocks.WEB && block != Blocks.DEADBUSH && block != Blocks.RED_FLOWER 
				&& block != Blocks.YELLOW_FLOWER && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.TORCH 
				&& block != Blocks.LADDER && block != Blocks.SNOW_LAYER && block != Blocks.VINE && block != Blocks.WATERLILY 
				&& block != Blocks.CARPET && block != Blocks.DOUBLE_PLANT && block != Blocks.END_ROD && block != Blocks.STANDING_SIGN 
				&& block != Blocks.WALL_SIGN && block != Blocks.FLOWER_POT && block != Blocks.STANDING_BANNER && block != Blocks.WALL_BANNER 
				&& block != Blocks.RAIL && block != Blocks.ACTIVATOR_RAIL && block != Blocks.DETECTOR_RAIL && block != Blocks.GOLDEN_RAIL 
				&& block != Blocks.WHEAT && block != Blocks.REEDS && block != Blocks.CARROTS && block != Blocks.POTATOES 
				&& block != Blocks.BEETROOTS && block != Blocks.REDSTONE_TORCH && block != Blocks.UNLIT_REDSTONE_TORCH && block != Blocks.WOODEN_BUTTON 
				&& block != Blocks.STONE_BUTTON && block != Blocks.POWERED_REPEATER && block != Blocks.UNPOWERED_REPEATER && block != Blocks.POWERED_COMPARATOR 
				&& block != Blocks.UNPOWERED_COMPARATOR && block != Blocks.REDSTONE_BLOCK && block != Blocks.SAPLING){
				this.setDead();
			}
		}
    }
}
