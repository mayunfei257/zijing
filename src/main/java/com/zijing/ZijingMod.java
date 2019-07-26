package com.zijing;

import org.apache.logging.log4j.Logger;

import com.zijing.util.Config;
import com.zijing.util.ConstantUtil;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ConstantUtil.MODID, version = ConstantUtil.VERSION)
public class ZijingMod{
    
    @SidedProxy(clientSide = ConstantUtil.CLIENTSIDE, serverSide = ConstantUtil.SERVERSIDE)
    public static CommonProxy proxy;
    
    @Instance(ConstantUtil.MODID)
	public static ZijingMod instance;
    public static Config config;
    private static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
    	config = Config.getConfig(event);
    	((RangedAttribute)SharedMonsterAttributes.MAX_HEALTH).maximumValue = config.getMAX_BLOOD();
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	proxy.init(event);
    }
    
    @EventHandler
    public void Postinit(FMLPostInitializationEvent event){
    	proxy.postinit(event);
    }

}
