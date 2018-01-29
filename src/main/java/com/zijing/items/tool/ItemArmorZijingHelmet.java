package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemArmorZijingHelmet extends ItemArmor{

	public ItemArmorZijingHelmet() {
		super(ZijingMod.config.getZijingArmorMaterial(), 3, EntityEquipmentSlot.HEAD);
		setUnlocalizedName("itemArmorZijingHelmet");
		setRegistryName(ZijingMod.MODID + ":itemarmorzijinghelmet");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
