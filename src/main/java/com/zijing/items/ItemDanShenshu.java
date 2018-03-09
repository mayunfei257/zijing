package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.itf.ItemDan;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDanShenshu extends ItemFood implements ItemDan{
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
		tooltip.add(I18n.format(ZijingMod.MODID + ".itemDanShenshu.1", new Object[] {effectTick/20}));
	}

	@Override
	public void onFoodEatenByEntityLivingBase(EntityLivingBase entity) {
		if (!entity.world.isRemote && entity instanceof EntityHasShepherdCapability) {
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, effectTick, 20));
		}
	}
}