package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemArmorZijingBoots extends ItemArmor{

	public ItemArmorZijingBoots() {
		super(ZijingMod.config.getZijingArmorMaterial(), 3, EntityEquipmentSlot.FEET);
		setUnlocalizedName("itemArmorZijingBoots");
		setRegistryName(ZijingMod.MODID + ":itemarmorzijingboots");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
