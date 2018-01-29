package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicEnergySource;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZijing extends Item implements MagicEnergySource{

	public ItemZijing() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemZijing");
		setRegistryName(ZijingMod.MODID + ":itemzijing");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public int getMagicEnergy() {
		return ZijingMod.config.getZIQI_MAGIC_ENERGY() * 9;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("Magic energy: " + (ZijingMod.config.getZIQI_MAGIC_ENERGY() * 9));
	}
}
