package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.BaseControl;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicEnergySource;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZiqi extends Item implements MagicEnergySource{

	public ItemZiqi() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemZiqi");
		setRegistryName(ZijingMod.MODID + ":itemziqi");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack){
		if(itemStack.getItem() == BaseControl.itemZiqi)
			return 16000;
		return 0;
    }

	@Override
	public int getMagicEnergy() {
		return ZijingMod.config.getZIQI_MAGIC_ENERGY();
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("Magic energy: " + ZijingMod.config.getZIQI_MAGIC_ENERGY());
	}
}
