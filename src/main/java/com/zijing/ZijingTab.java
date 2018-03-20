package com.zijing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ZijingTab extends CreativeTabs{
	//CreativeTab
	public static final CreativeTabs zijingTab = new ZijingTab();

	public ZijingTab() {
		super("zijingTab");
	}

	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(BaseControl.itemZijing);
	}
}