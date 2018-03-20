package com.zijing.items.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorZijingBoots extends ItemArmor{
	public static final int effectTick = 60;

	public ItemArmorZijingBoots() {
		super(ZijingMod.config.getZijingArmorMaterial(), 3, EntityEquipmentSlot.FEET);
		setUnlocalizedName("itemArmorZijingBoots");
		setRegistryName(ConstantUtil.MODID + ":itemarmorzijingboots");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemArmorZijingBoots.skill1", new Object[] {effectTick/20}));
	}
}
