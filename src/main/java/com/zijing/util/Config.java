package com.zijing.util;

import java.io.File;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	private static Configuration configuration;
	private static Config config;
	
	private int TOOL_HARVEST_LEVEL;
	private int TOOL_MAX_USES;
	private int TOOL_EFFICIENCY;
	private int TOOL_DAMAGE;
	private int TOOL_ENCHANTABILITY;
	private int TOOL_DMAMOUNT;
	private float TOOL_FU_SPEED = - 3.0F;//zijingFu
	
	private int ARMOR_DURABILITY;
	private int[] ARMOR_REDUCTION_AMOUNTS;
	private int ARMOR_ENCHANTABILITY;
	private int ARMOR_TOUGHNESS;

	private int ZILINGCAO_MAX_HIGHT;
	private double ZILINGCAO_GROWTH_PROBABILITY;
	private int ZILINGCAO_GROWTH_KEY;
	private int ZIQI_BURN_TICK;
	private int ZIQI_MAGIC_ENERGY;
	private int STAFF_MAX_MAGIC_ENERGY;
	private int STAFF_MAX_DISTANCE;
	
	private int MAX_LEVEL;
	private int MAX_BLOOD;
	private float RESTORE_NEED_FOOD;
	private int UPGRADE_NEED_XP_K;
	private int UPGRADE_MAXMAGIC_K;
	private int UPGRADE_MAXBLOOD_K;
	private double UPGRADE_ATTACK_K;
	private double UPGRADE_BLOODRESTORE_K;
	private double UPGRADE_MAGICRESTORE_K;
	private double UPGRADE_PHYSICALDEFENSE_K;
	
	private int ALLOWFLYING_LEVEL;
	private double DAMAGE_REDUCTION_K;
	
	private int LONGJIE_DIMID;
	
	
	private Item.ToolMaterial zijingToolMaterial;
	private ItemArmor.ArmorMaterial zijingArmorMaterial;
	
	private Config(){
		configuration = new Configuration(new File( "./config/ziqi/ziqi.cfg"));
		configuration.load();
		load();
		configuration.save();
		zijingToolMaterial = EnumHelper.addToolMaterial("ZIJING", TOOL_HARVEST_LEVEL, TOOL_MAX_USES, (float)TOOL_EFFICIENCY, TOOL_DAMAGE, TOOL_ENCHANTABILITY);
		zijingArmorMaterial = EnumHelper.addArmorMaterial("ZIJING", ConstantUtil.MODID + ":zijing", ARMOR_DURABILITY, ARMOR_REDUCTION_AMOUNTS, ARMOR_ENCHANTABILITY, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, (float)ARMOR_TOUGHNESS);
	}
	
	private Config(FMLPreInitializationEvent e){
		configuration = new Configuration(new File(e.getModConfigurationDirectory(), "/ziqi/ziqi.cfg"));
		configuration.load();
		load();
		configuration.save();
		zijingToolMaterial = EnumHelper.addToolMaterial("ZIJING", TOOL_HARVEST_LEVEL, TOOL_MAX_USES, (float)TOOL_EFFICIENCY, TOOL_DAMAGE, TOOL_ENCHANTABILITY);
		zijingArmorMaterial = EnumHelper.addArmorMaterial("ZIJING", ConstantUtil.MODID + ":zijing", ARMOR_DURABILITY, ARMOR_REDUCTION_AMOUNTS, ARMOR_ENCHANTABILITY, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, (float)ARMOR_TOUGHNESS);
	}

	public static Config getConfig() {
		if(null == config) {
			synchronized (Config.class){
				if(null == config) {
					config = new Config();
				}
			}
		}
		return config;
	}
	
	public static Config getConfig(FMLPreInitializationEvent e) {
		if(null == config) {
			synchronized (Config.class){
				if(null == config) {
					config = new Config(e);
				}
			}
		}
		return config;
	}
	
	
	private void load(){
		try {
			this.TOOL_HARVEST_LEVEL = configuration.get("Tool", "TOOL_HARVEST_LEVEL", 3, new String(new byte[]{-2,-1,125,43,102,118,93,-27,81,119,118,-124,123,73,126,-89}, "unicode")).getInt();
			this.TOOL_MAX_USES = configuration.get("Tool", "TOOL_MAX_USES", 8192, new String(new byte[]{-2,-1,125,43,102,118,93,-27,81,119,118,-124,103,0,89,39,79,127,117,40,107,33,101,112}, "unicode")).getInt();
			this.TOOL_EFFICIENCY = configuration.get("Tool", "TOOL_EFFICIENCY", 16, new String(new byte[]{-2,-1,125,43,102,118,93,-27,81,119,118,-124,99,22,99,-104,101,72,115,-121}, "unicode")).getInt();
			this.TOOL_DAMAGE = configuration.get("Tool", "TOOL_DAMAGE", 10, new String(new byte[]{-2,-1,125,43,102,118,93,-27,81,119,118,-124,87,-6,120,64,79,36,91,-77}, "unicode")).getInt();
			this.TOOL_ENCHANTABILITY = configuration.get("Tool", "TOOL_ENCHANTABILITY", 50, new String(new byte[]{-2,-1,125,43,102,118,93,-27,81,119,118,-124,83,-17,-106,68,-101,84,96,39}, "unicode")).getInt();
			this.TOOL_DMAMOUNT = configuration.get("Tool", "TOOL_DMAMOUNT", 64, new String(new byte[]{-2,-1,125,43,102,118,93,-27,81,119,118,-124,103,0,89,39,-113,-34,-107,1,101,112,-111,-49}, "unicode")).getInt();
			
			this.ARMOR_DURABILITY = configuration.get("Armor", "ARMOR_DURABILITY", 1024, new String(new byte[]{-2,-1,125,43,102,118,-120,-59,89,7,118,-124,103,0,89,39,79,127,117,40,107,33,101,112,107,-44,79,-117}, "unicode")).getInt();
			this.ARMOR_REDUCTION_AMOUNTS = configuration.get("Armor", "ARMOR_REDUCTION_AMOUNTS", new int[]{4, 7, 6, 3}, new String(new byte[]{-2,-1,125,43,102,118,-120,-59,89,7,118,-124,-106,50,95,-95,82,-101,-1,12,82,6,82,43,102,47,-1,26,89,52,118,-44,48,1,-128,-8,117,50,48,1,-120,-28,91,80,48,1,-105,116,91,80}, "unicode")).getIntList();
			this.ARMOR_ENCHANTABILITY = configuration.get("Armor", "ARMOR_ENCHANTABILITY", 50, new String(new byte[]{-2,-1,125,43,102,118,-120,-59,89,7,118,-124,83,-17,-106,68,-101,84,96,39,}, "unicode")).getInt();
			this.ARMOR_TOUGHNESS = configuration.get("Armor", "ARMOR_TOUGHNESS", 5, new String(new byte[]{-2,-1,125,43,102,118,-120,-59,89,7,118,-124,-105,-25,96,39}, "unicode")).getInt();

			this.ZILINGCAO_MAX_HIGHT = configuration.get("MAGIC", "ZILINGCAO_MAX_HIGHT", 8, new String(new byte[]{-2,-1,125,43,112,117,-125,73,103,0,-102,-40,117,31,-107,127,-102,-40,94,-90}, "unicode")).getInt();
			this.ZILINGCAO_GROWTH_PROBABILITY = configuration.get("MAGIC", "ZILINGCAO_GROWTH_PROBABILITY", 1D, new String(new byte[]{-2,-1,125,43,112,117,-125,73,118,-124,117,31,-107,127,105,-126,115,-121,0,48,0,126,0,49}, "unicode")).getDouble();
			this.ZILINGCAO_GROWTH_KEY = configuration.get("MAGIC", "ZILINGCAO_GROWTH_KEY", 1, new String(new byte[]{-2,-1,125,43,112,117,-125,73,107,-49,107,33,117,31,-107,127,118,-124,-126,-126,101,112}, "unicode")).getInt();
			this.ZIQI_BURN_TICK = configuration.get("MAGIC", "ZIQI_BURN_TICK", 160000, new String(new byte[]{-2,-1,125,43,108,20,113,-61,112,-25,101,-10,-107,-12}, "unicode")).getInt();
			this.ZIQI_MAGIC_ENERGY = configuration.get("MAGIC", "ZIQI_MAGIC_ENERGY", 300, new String(new byte[]{-2,-1,125,43,108,20,-123,116,84,43,118,-124,108,-43,82,-101,80,60}, "unicode")).getInt();
			this.STAFF_MAX_MAGIC_ENERGY = configuration.get("MAGIC", "STAFF_MAX_MAGIC_ENERGY", 512, new String(new byte[]{-2,-1,95,3,117,40}, "unicode")).getInt();
			this.STAFF_MAX_DISTANCE = configuration.get("MAGIC", "STAFF_MAX_DISTANCE", 10000, new String(new byte[]{-2,-1,122,122,-107,-12,108,-43,103,86,103,0,89,39,79,32,-112,1,-115,-35,121,-69}, "unicode")).getInt();
			
			this.MAX_LEVEL = configuration.get("PLAYER", "MAX_LEVEL", 100, new String(new byte[]{-2,-1,98,64,-128,-3,-113,-66,82,48,118,-124,103,0,89,39,123,73,126,-89,78,10,-106,80}, "unicode")).getInt();
			this.MAX_BLOOD = configuration.get("PLAYER", "MAX_BLOOD", 2147483647, new String(new byte[]{-2,-1,98,64,-128,-3,-113,-66,82,48,118,-124,103,0,89,39,117,31,84,125,80,60,78,10,-106,80}, "unicode")).getInt();
			this.RESTORE_NEED_FOOD = (float) configuration.get("PLAYER", "RESTORE_NEED_FOOD", 0.004D, new String(new byte[]{-2,-1,115,-87,91,-74,96,98,89,13,101,-10,107,-49,0,116,0,105,0,99,0,107,98,64,-105,0,118,-124,-104,-33,114,105,-111,-49}, "unicode")).getDouble();
			this.UPGRADE_NEED_XP_K = configuration.get("PLAYER", "UPGRADE_NEED_XP_K", 50, new String(new byte[]{-2,-1,83,71,126,-89,-105,0,-119,-127,118,-124,126,-49,-102,-116,80,60,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,53,0,48}, "unicode")).getInt();
			this.UPGRADE_MAXMAGIC_K = configuration.get("PLAYER", "UPGRADE_MAXMAGIC_K", 20, new String(new byte[]{-2,-1,103,0,89,39,108,-43,82,-101,80,60,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,50,0,48}, "unicode")).getInt();
			this.UPGRADE_MAXBLOOD_K = configuration.get("PLAYER", "UPGRADE_MAXBLOOD_K", 2, new String(new byte[]{-2,-1,103,0,89,39,117,31,84,125,80,60,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,50}, "unicode")).getInt();
			this.UPGRADE_ATTACK_K = configuration.get("PLAYER", "UPGRADE_ATTACK_K", 0.05D, new String(new byte[]{-2,-1,101,59,81,-5,82,-101,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,48,0,46,0,48,0,53}, "unicode")).getDouble();
			this.UPGRADE_BLOODRESTORE_K = configuration.get("PLAYER", "UPGRADE_BLOODRESTORE_K", 0.0002D, new String(new byte[]{-2,-1,117,31,84,125,80,60,86,-34,89,13,-112,31,94,-90,0,40,112,-71,0,47,0,84,0,105,0,99,0,107,0,41,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,48,0,46,0,48,0,48,0,48,0,50}, "unicode")).getDouble();
			this.UPGRADE_MAGICRESTORE_K = configuration.get("PLAYER", "UPGRADE_MAGICRESTORE_K", 0.004D, new String(new byte[]{-2,-1,108,-43,82,-101,80,60,86,-34,89,13,-112,31,94,-90,0,40,112,-71,0,47,0,84,0,105,0,99,0,107,0,41,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,48,0,46,0,48,0,48,0,52}, "unicode")).getDouble();
			this.UPGRADE_PHYSICALDEFENSE_K = configuration.get("PLAYER", "UPGRADE_PHYSICALDEFENSE_K", 0.2D, new String(new byte[]{-2,-1,114,105,116,6,-106,50,95,-95,80,60,0,32,0,61,0,32,0,40,123,73,126,-89,0,32,0,42,0,32,123,73,126,-89,0,32,0,43,0,32,123,73,126,-89,0,41,0,32,0,42,0,32,0,75,0,32,0,47,0,32,0,50,-1,12,0,75,-98,-40,-117,-92,80,60,0,48,0,46,0,50}, "unicode")).getDouble();
			this.ALLOWFLYING_LEVEL = configuration.get("PLAYER", "ALLOWFLYING_LEVEL", 40, new String(new byte[]{-2,-1,-113,-66,82,48,89,26,92,17,126,-89,83,-17,78,-27,-104,-34,-120,76}, "unicode")).getInt();
			this.DAMAGE_REDUCTION_K = configuration.get("PLAYER", "DAMAGE_REDUCTION_K", 0.06, new String(new byte[]{-2,-1,79,36,91,-77,81,-49,81,77,107,-44,79,-117,0,75,-1,12,-106,50,95,-95,82,77,79,36,91,-77,0,32,0,61,0,32,83,-97,79,36,91,-77,0,32,0,45,0,32,0,77,0,65,0,88,0,40,0,48,0,44,0,32,0,40,-106,50,95,-95,80,60,0,32,0,45,0,32,0,51,0,48,0,41,0,32,0,42,0,32,0,75,0,41,-1,12,-98,-40,-117,-92,78,58,0,48,0,46,0,48,0,54}, "unicode")).getDouble();
			
			this.LONGJIE_DIMID = configuration.get("LONGJIE", "LONGJIE_DIMID", 5, "Long Jie Dimension ID.").getInt();
		}catch(Exception e) {
			
		}
	}


	public int getZIQI_BURN_TICK() {
		return ZIQI_BURN_TICK;
	}

	public int getZILINGCAO_MAX_HIGHT() {
		return ZILINGCAO_MAX_HIGHT;
	}

	public double getZILINGCAO_GROWTH_PROBABILITY() {
		return ZILINGCAO_GROWTH_PROBABILITY;
	}

	public int getZILINGCAO_GROWTH_KEY() {
		return ZILINGCAO_GROWTH_KEY;
	}

	public int getZIQI_MAGIC_ENERGY() {
		return ZIQI_MAGIC_ENERGY;
	}

	public int getTOOL_HARVEST_LEVEL() {
		return TOOL_HARVEST_LEVEL;
	}

	public int getTOOL_MAX_USES() {
		return TOOL_MAX_USES;
	}

	public int getTOOL_EFFICIENCY() {
		return TOOL_EFFICIENCY;
	}

	public int getTOOL_DAMAGE() {
		return TOOL_DAMAGE;
	}

	public int getTOOL_ENCHANTABILITY() {
		return TOOL_ENCHANTABILITY;
	}

	public int getTOOL_DMAMOUNT() {
		return TOOL_DMAMOUNT;
	}

	public float getTOOL_FU_SPEED() {
		return TOOL_FU_SPEED;
	}

	public int getARMOR_DURABILITY() {
		return ARMOR_DURABILITY;
	}

	public int[] getARMOR_REDUCTION_AMOUNTS() {
		return ARMOR_REDUCTION_AMOUNTS;
	}

	public int getARMOR_ENCHANTABILITY() {
		return ARMOR_ENCHANTABILITY;
	}

	public int getARMOR_TOUGHNESS() {
		return ARMOR_TOUGHNESS;
	}

	public int getSTAFF_MAX_MAGIC_ENERGY() {
		return STAFF_MAX_MAGIC_ENERGY;
	}

	public int getSTAFF_MAX_DISTANCE() {
		return STAFF_MAX_DISTANCE;
	}

	public int getMAX_LEVEL() {
		return MAX_LEVEL;
	}
	
	public int getMAX_BLOOD() {
		return MAX_BLOOD;
	}

	public float getRESTORE_NEED_FOOD() {
		return RESTORE_NEED_FOOD;
	}

	public int getUPGRADE_NEED_XP_K() {
		return UPGRADE_NEED_XP_K;
	}

	public int getUPGRADE_MAXMAGIC_K() {
		return UPGRADE_MAXMAGIC_K;
	}

	public int getUPGRADE_MAXBLOOD_K() {
		return UPGRADE_MAXBLOOD_K;
	}

	public double getUPGRADE_ATTACK_K() {
		return UPGRADE_ATTACK_K;
	}

	public double getUPGRADE_BLOODRESTORE_K() {
		return UPGRADE_BLOODRESTORE_K;
	}

	public double getUPGRADE_MAGICRESTORE_K() {
		return UPGRADE_MAGICRESTORE_K;
	}
	
	public double getUPGRADE_PHYSICALDEFENSE_K() {
		return UPGRADE_PHYSICALDEFENSE_K;
	}

	public Item.ToolMaterial getZijingToolMaterial() {
		return zijingToolMaterial;
	}

	public ItemArmor.ArmorMaterial getZijingArmorMaterial() {
		return zijingArmorMaterial;
	}

	public int getLONGJIE_DIMID() {
		return LONGJIE_DIMID;
	}

	public int getALLOWFLYING_LEVEL() {
		return ALLOWFLYING_LEVEL;
	}

	public double getDAMAGE_REDUCTION_K() {
		return DAMAGE_REDUCTION_K;
	}
}

