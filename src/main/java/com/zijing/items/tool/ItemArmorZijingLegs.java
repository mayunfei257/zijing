package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemArmorZijingLegs extends ItemArmor{

	public ItemArmorZijingLegs() {
		super(ZijingMod.config.getZijingArmorMaterial(), 3, EntityEquipmentSlot.LEGS);
		setUnlocalizedName("itemArmorZijingLegs");
		setRegistryName(ZijingMod.MODID + ":itemarmorzijinglegs");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
