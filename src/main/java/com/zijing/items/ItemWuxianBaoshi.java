package com.zijing.items;

import com.zijing.BaseControl;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWuxianBaoshi extends Item{

	public ItemWuxianBaoshi() {
		super();
		setMaxStackSize(64);
		setUnlocalizedName("itemWuxianBaoshi");
		setRegistryName(ConstantUtil.MODID + ":itemwuxianbaoshi");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack){
		if(itemStack.getItem() == BaseControl.itemWuxianBaoshi)
			return Integer.MAX_VALUE;
		return 0;
    }
}