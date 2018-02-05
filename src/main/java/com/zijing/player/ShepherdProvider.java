package com.zijing.player;

import com.zijing.main.BaseControl;

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
    
    public static ShepherdCapability getCapabilityFromPlayer(EntityPlayer player) {
        return player.hasCapability(SHE_CAP, null)? player.getCapability(SHE_CAP, null): null;
    }

    public static void updateChange(EntityPlayer player) {
        if(player != null){
        	BaseControl.netWorkWrapper.sendTo(new ShepherdMessage(player.getCapability(SHE_CAP, null).writeNBT(null)), (EntityPlayerMP) player);
        }
    }
}