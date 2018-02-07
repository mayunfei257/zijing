package com.zijing.main.playerdata;

import com.zijing.main.BaseControl;
import com.zijing.main.message.ShepherdToClientMessage;
import com.zijing.main.message.UpgradeToServerMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class ShepherdProvider implements ICapabilityProvider, INBTSerializable {

    @CapabilityInject(ShepherdCapability.class)
    public static Capability<ShepherdCapability> SHE_CAP;
    private ShepherdCapability shepherdCapability;

    public ShepherdProvider() {
    	this.shepherdCapability = new ShepherdCapability();
    }
    
    public ShepherdProvider(ShepherdCapability shepherdCapability) {
    	this.shepherdCapability = shepherdCapability;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return null != SHE_CAP && capability == SHE_CAP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (null != SHE_CAP && capability == SHE_CAP) return (T)shepherdCapability;
        return null;
    }
    
    @Override
    public NBTBase serializeNBT() {
        return shepherdCapability.writeNBT(null);
    }
    
    @Override
    public void deserializeNBT(NBTBase nbt) {
        shepherdCapability.readNBT(null, nbt);
    }

    public static boolean hasCapabilityFromPlayer(Entity player) {
    	if(null != player) {
            return player.hasCapability(SHE_CAP, null);
    	}
        return false;
    }
    
    public static ShepherdCapability getCapabilityFromPlayer(Entity player) {
    	if(null != player && player.hasCapability(SHE_CAP, null)) {
            return  player.getCapability(SHE_CAP, null);
    	}
    	return null;
    }

    public static void updateChangeToClient(EntityPlayer player) {
        if(null != player && player instanceof EntityPlayerMP && player.hasCapability(SHE_CAP, null)){
        	BaseControl.netWorkWrapper.sendTo(new ShepherdToClientMessage(player.getCapability(SHE_CAP, null).writeNBT(null)), (EntityPlayerMP) player);
        }
    }
    
    public static void upgradeToServer(EntityPlayer player, int upLevel) {
        if(null != player && player.hasCapability(SHE_CAP, null)){
        	BaseControl.netWorkWrapper.sendToServer(new UpgradeToServerMessage(player.getCapability(SHE_CAP, null).writeNBT(null), upLevel, player.getUniqueID()));
        }
    }
}