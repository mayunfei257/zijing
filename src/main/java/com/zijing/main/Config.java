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

	private int ZIQI_MAGIC_ENERGY;
	
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
	
	private int STAFF_MAX_MAGIC_ENERGY;
	private int STAFF_MAX_DISTANCE;
	
	private int CARD_MAX_USES;
	
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
		this.ZIQI_MAGIC_ENERGY = configuration.get("BASE", "ZIQI_MAGIC_ENERGY", 30, "The ZIQI magic energy.").getInt();
		
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

		this.STAFF_MAX_MAGIC_ENERGY = configuration.get("STAFF", "STAFF_MAX_MAGIC_ENERGY", 512, "The max magic energy of the staff.").getInt();
		this.STAFF_MAX_DISTANCE = configuration.get("STAFF", "STAFF_MAX_DISTANCE", 10000, "The max distance of staff to teleport.").getInt();
		
		this.CARD_MAX_USES = configuration.get("CARD", "CARD_MAX_USES", 0, "The maximum number of card to use.").getInt();
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

	public int getCARD_MAX_USES() {
		return CARD_MAX_USES;
	}

	public Item.ToolMaterial getZijingToolMaterial() {
		return zijingToolMaterial;
	}

	public ItemArmor.ArmorMaterial getZijingArmorMaterial() {
		return zijingArmorMaterial;
	}
}

