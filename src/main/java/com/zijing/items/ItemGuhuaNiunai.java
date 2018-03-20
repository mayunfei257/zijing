package com.zijing.items;

import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.item.Item;

public class ItemGuhuaNiunai extends Item{

	public ItemGuhuaNiunai() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemGuhuaNiunai");
		setRegistryName(ConstantUtil.MODID + ":itemguhuaniunai");
		setCreativeTab(ZijingTab.zijingTab);
	}

}
