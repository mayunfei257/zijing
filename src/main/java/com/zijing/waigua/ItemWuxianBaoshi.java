package com.zijing.waigua;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWuxianBaoshi extends Item{
	public final static int brunTick = Integer.MAX_VALUE;
	public final static int upExperience = 1000;
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
			return brunTick;
		return 0;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill1", new Object[] {brunTick/20}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill2", new Object[] {upExperience}));
    }
}