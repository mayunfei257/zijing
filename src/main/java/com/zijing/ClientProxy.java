package com.zijing;

import com.zijing.main.BaseControl;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		BaseControl.resourceLoad(event);
	}
	
	public void init(FMLInitializationEvent event){
		super.init(event);
		BaseControl.renderLoad(event);
	}
	
	public void Postinit(FMLPostInitializationEvent event){
		super.Postinit(event);
	}
	
	@Override
	public boolean isServerSider() {
		return false;
	}
}
