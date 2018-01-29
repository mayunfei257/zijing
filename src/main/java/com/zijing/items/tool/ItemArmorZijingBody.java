package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemArmorZijingBody extends ItemArmor{

	public ItemArmorZijingBody() {
		super(ZijingMod.config.getZijingArmorMaterial(), 3, EntityEquipmentSlot.CHEST);
		setUnlocalizedName("itemArmorZijingBody");
		setRegistryName(ZijingMod.MODID + ":itemarmorzijingbody");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
