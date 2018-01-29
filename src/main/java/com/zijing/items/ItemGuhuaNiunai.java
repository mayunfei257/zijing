package com.zijing.items;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.item.Item;

public class ItemGuhuaNiunai extends Item{

	public ItemGuhuaNiunai() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemGuhuaNiunai");
		setRegistryName(ZijingMod.MODID + ":itemguhuaniunai");
		setCreativeTab(ZijingTab.zijingTab);
	}

}
