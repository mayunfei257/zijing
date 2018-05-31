package com.zijing;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		BaseControl.resourceLoad(event);
		ClientRegistry.registerKeyBinding(ZijingEvent.key1);
	}
	
	public void init(FMLInitializationEvent event){
		super.init(event);
		BaseControl.renderLoad(event);
	}
	
	public void postinit(FMLPostInitializationEvent event){
		super.postinit(event);
	}
	
	@Override
	public boolean isServerSider() {
		return false;
	}
}
