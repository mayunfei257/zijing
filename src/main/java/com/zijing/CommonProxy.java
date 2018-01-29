package com.zijing;

import com.zijing.main.BaseControl;
import com.zijing.main.ZijingEvent;
import com.zijing.main.ZijingGuiHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class  CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		BaseControl.init(event);
		BaseControl.register(event);
		MinecraftForge.EVENT_BUS.register(new ZijingEvent());
	}
	
	public void init(FMLInitializationEvent event){
		BaseControl.registerRecipe(event);
		NetworkRegistry.INSTANCE.registerGuiHandler(ZijingMod.instance, new ZijingGuiHandler());
	}
	
	public void Postinit(FMLPostInitializationEvent event){
        
    }
	
	public boolean isServerSider() {
		return true;
	}
}
