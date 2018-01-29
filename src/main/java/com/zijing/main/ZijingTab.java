package com.zijing.main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ZijingTab extends CreativeTabs{
	//CreativeTab
	public static final CreativeTabs zijingTab = new ZijingTab();

	public ZijingTab() {
		super("ZijingTab");
	}

	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(BaseControl.itemZijing);
	}
}