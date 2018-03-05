package com.zijing.main;

import java.io.File;

import com.zijing.ZijingMod;

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

	private int ZIQI_MAGIC_ENERGY;
	private int STAFF_MAX_MAGIC_ENERGY;
	private int STAFF_MAX_DISTANCE;
	
	private int MAX_LEVEL;
	private float RESTORE_NEED_FOOD;
	private int UPGRADE_NEED_XP_K;
	private int UPGRADE_NEED_MAGIC_K;
	private int UPGRADE_MAXMAGIC_K;
	private int UPGRADE_MAXBLOOD_K;
	private double UPGRADE_POWER_K;
	private double UPGRADE_BLOODRESTORE_K;
	private double UPGRADE_MAGICRESTORE_K;
	
	
	private Item.ToolMaterial zijingToolMaterial;
	private ItemArmor.ArmorMaterial zijingArmorMaterial;
	
	private Config(){
		configuration = new Configuration(new File( "./config/ziqi/ziqi.cfg"));
		configuration.load();
		load();
		configuration.save();
		zijingToolMaterial = EnumHelper.addToolMaterial("ZIJING", TOOL_HARVEST_LEVEL, TOOL_MAX_USES, (float)TOOL_EFFICIENCY, TOOL_DAMAGE, TOOL_ENCHANTABILITY);
		zijingArmorMaterial = EnumHelper.addArmorMaterial("ZIJING", ZijingMod.MODID + ":zijing", ARMOR_DURABILITY, ARMOR_REDUCTION_AMOUNTS, ARMOR_ENCHANTABILITY, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, (float)ARMOR_TOUGHNESS);
	}
	
	private Config(FMLPreInitializationEvent e){
		configuration = new Configuration(new File(e.getModConfigurationDirectory(), "/ziqi/ziqi.cfg"));
		configuration.load();
		load();
		configuration.save();
		zijingToolMaterial = EnumHelper.addToolMaterial("ZIJING", TOOL_HARVEST_LEVEL, TOOL_MAX_USES, (float)TOOL_EFFICIENCY, TOOL_DAMAGE, TOOL_ENCHANTABILITY);
		zijingArmorMaterial = EnumHelper.addArmorMaterial("ZIJING", ZijingMod.MODID + ":zijing", ARMOR_DURABILITY, ARMOR_REDUCTION_AMOUNTS, ARMOR_ENCHANTABILITY, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, (float)ARMOR_TOUGHNESS);
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
		this.TOOL_HARVEST_LEVEL = configuration.get("Tool", "TOOL_HARVEST_LEVEL", 3, "Tools level.").getInt();
		this.TOOL_MAX_USES = configuration.get("Tool", "TOOL_MAX_USES", 1024, "The maximum number of tools to use.").getInt();
		this.TOOL_EFFICIENCY = configuration.get("Tool", "TOOL_EFFICIENCY", 12, "The efficiency of tools.").getInt();
		this.TOOL_DAMAGE = configuration.get("Tool", "TOOL_DAMAGE", 10, "Tools damage value.").getInt();
		this.TOOL_ENCHANTABILITY = configuration.get("Tool", "TOOL_ENCHANTABILITY", 50, "Tools enchant probability.").getInt();
		this.TOOL_DMAMOUNT = configuration.get("Tool", "TOOL_DMAMOUNT", 64, "The number of chain reactions.").getInt();
		
		this.ARMOR_DURABILITY = configuration.get("Armor", "ARMOR_DURABILITY", 128, "Armor the maximum number of use of the ratio.").getInt();
		this.ARMOR_REDUCTION_AMOUNTS = configuration.get("Armor", "ARMOR_REDUCTION_AMOUNTS", new int[]{4, 7, 6, 3}, "Armor defense values, respectively, the head, body, legs, feet.").getIntList();
		this.ARMOR_ENCHANTABILITY = configuration.get("Armor", "ARMOR_ENCHANTABILITY", 50, "Tools enchant probability.").getInt();
		this.ARMOR_TOUGHNESS = configuration.get("Armor", "ARMOR_TOUGHNESS", 4, "Armor toughness value.").getInt();

		this.ZIQI_MAGIC_ENERGY = configuration.get("MAGIC", "ZIQI_MAGIC_ENERGY", 30, "The ZIQI magic energy.").getInt();
		this.STAFF_MAX_MAGIC_ENERGY = configuration.get("MAGIC", "STAFF_MAX_MAGIC_ENERGY", 512, "The max magic energy of the staff.").getInt();
		this.STAFF_MAX_DISTANCE = configuration.get("MAGIC", "STAFF_MAX_DISTANCE", 10000, "The max distance of staff to teleport.").getInt();
		
		this.MAX_LEVEL = configuration.get("PLAYER", "MAX_LEVEL", 512, "The player max level.").getInt();
		this.RESTORE_NEED_FOOD = (float) configuration.get("PLAYER", "RESTORE_NEED_FOOD", 0.004D, "The player restore need food every tick.").getDouble();
		this.UPGRADE_NEED_XP_K = configuration.get("PLAYER", "UPGRADE_NEED_XP_K", 50, "The player upgrade need xp K every level.").getInt();
		this.UPGRADE_NEED_MAGIC_K = configuration.get("PLAYER", "UPGRADE_NEED_MAGIC_K", 50, "The player upgrade need magic K every level.").getInt();
		this.UPGRADE_MAXMAGIC_K = configuration.get("PLAYER", "UPGRADE_MAXMAGIC_K", 50, "The player upgrade maxmagic K every level.").getInt();
		this.UPGRADE_MAXBLOOD_K = configuration.get("PLAYER", "UPGRADE_MAXBLOOD_K", 2, "The player upgrade maxblood K every level.").getInt();
		this.UPGRADE_POWER_K = configuration.get("PLAYER", "UPGRADE_POWER_K", 0.25D, "The player upgrade power K every level.").getDouble();
		this.UPGRADE_BLOODRESTORE_K = configuration.get("PLAYER", "UPGRADE_BLOODRESTORE_K", 0.0002D, "The player upgrade bloodrestore K every tick.").getDouble();
		this.UPGRADE_MAGICRESTORE_K = configuration.get("PLAYER", "UPGRADE_MAGICRESTORE_K", 0.001D, "The player upgrade magicrestore K every tick.").getDouble();
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
	
	public float getRESTORE_NEED_FOOD() {
		return RESTORE_NEED_FOOD;
	}

	public int getUPGRADE_NEED_XP_K() {
		return UPGRADE_NEED_XP_K;
	}

	public int getUPGRADE_NEED_MAGIC_K() {
		return UPGRADE_NEED_MAGIC_K;
	}

	public int getUPGRADE_MAXMAGIC_K() {
		return UPGRADE_MAXMAGIC_K;
	}

	public int getUPGRADE_MAXBLOOD_K() {
		return UPGRADE_MAXBLOOD_K;
	}

	public double getUPGRADE_POWER_K() {
		return UPGRADE_POWER_K;
	}

	public double getUPGRADE_BLOODRESTORE_K() {
		return UPGRADE_BLOODRESTORE_K;
	}

	public double getUPGRADE_MAGICRESTORE_K() {
		return UPGRADE_MAGICRESTORE_K;
	}
	
	public Item.ToolMaterial getZijingToolMaterial() {
		return zijingToolMaterial;
	}

	public ItemArmor.ArmorMaterial getZijingArmorMaterial() {
		return zijingArmorMaterial;
	}
}

