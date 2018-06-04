package com.zijing.itf;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.gui.GuiEntityCapability;
import com.zijing.waigua.ItemWuxianBaoshi;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class EntityFriendly extends EntityShepherdCapability{

	public EntityFriendly(World world) {
		super(world);
	}
	
	public EntityFriendly(World world, int baseLevel) {
		super(world, baseLevel);
	}

	@Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(itemStack.getItem() instanceof MagicSource && this.shepherdCapability.getMagic() < this.shepherdCapability.getMaxMagic()) {
			this.shepherdCapability.setMagic(Math.min(this.shepherdCapability.getMaxMagic(), this.shepherdCapability.getMagic() + ((MagicSource)itemStack.getItem()).getMagicEnergy()));
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			itemStack.shrink(1);
		}else if(itemStack.getItem() instanceof ItemFoodDan){
			((ItemFoodDan)itemStack.getItem()).onFoodEatenByEntityFriendly(this);
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			itemStack.shrink(1);
		}else if(itemStack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER) || itemStack.getItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)){
			this.experience += 5;
			this.spawnParticles(EnumParticleTypes.HEART);
			itemStack.shrink(1);
		}else if(itemStack.getItem() == BaseControl.itemWuxianBaoshi && player.isSneaking()){//Test mode
			this.experience += ItemWuxianBaoshi.upExperience;
		}else if(!this.world.isRemote) {
	        player.openGui(ZijingMod.instance, GuiEntityCapability.GUIID, world, this.getEntityId(), this.getEntityId(), this.getEntityId());
		}
		player.sendMessage(new TextComponentString("Home: X:" + this.homePos.getX() + " Y:" + this.homePos.getY() + " Z:" + this.homePos.getZ() + " MaxDistance:" + this.maxDistance + " nowDistance£º" + this.getDistance(this.homePos.getX(), this.homePos.getY(), this.homePos.getZ())));
		return true;
    }
    
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
        if (EntityPlayer.class.isAssignableFrom(cls) || EntityFriendly.class.isAssignableFrom(cls)){
        	return false;
        }else {
            return super.canAttackClass(cls);
        }
    }
}
