package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
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
        if (stack.getSubCompound("BlockEntityTag") != null){
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("item.itemToolZijingDun." + enumdyecolor.getUnlocalizedName() + ".name");
        } else {
            return I18n.translateToLocal("item.itemToolZijingDun.name");
        }
    }

	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return true;
    }
}
