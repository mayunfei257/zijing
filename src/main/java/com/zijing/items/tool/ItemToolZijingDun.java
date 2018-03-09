package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.BaseControl;
import com.zijing.main.ZijingTab;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemToolZijingDun extends ItemShield{

	public ItemToolZijingDun() {
		super();
        setMaxDamage(ZijingMod.config.getTOOL_MAX_USES());
		setUnlocalizedName("itemToolZijingDun");
		setRegistryName(ZijingMod.MODID + ":itemtoolzijingdun");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return repair.getItem() == BaseControl.itemZiqi ? true : super.getIsRepairable(toRepair, repair);
    }
}
