package com.zijing;

import com.zijing.main.Config;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ZijingMod.MODID, version = ZijingMod.VERSION)
public class ZijingMod{
    public static final String MODID = "zijingmod";
    public static final String VERSION = "1.0";
    
    @SidedProxy(clientSide = "com.zijing.ClientProxy",serverSide = "com.zijing.CommonProxy")
    public static CommonProxy proxy;
    
    @Instance(MODID)
	public static ZijingMod instance;
    public static Config config;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	config = Config.getConfig(event);
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	proxy.init(event);
    }
    
    @EventHandler
    public void Postinit(FMLPostInitializationEvent event){
    	proxy.Postinit(event);
    }

}
