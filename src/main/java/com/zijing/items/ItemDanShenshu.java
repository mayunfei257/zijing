package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDanShenshu extends ItemFood {
	public static final int effectTick = 480;

	public ItemDanShenshu() {
		super(1, 1F, true);
		setAlwaysEdible();
		maxStackSize = 64;
		setUnlocalizedName("itemDanShenshu");
		setRegistryName(ZijingMod.MODID + ":itemdanshenshu");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entity) {
		super.onFoodEaten(itemStack, world, entity);
		if (!world.isRemote) {
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, effectTick, 20));
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemDanShenshu.skill", new Object[] {effectTick/20}));
	}
}