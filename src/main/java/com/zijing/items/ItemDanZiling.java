package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.EntityHasShepherdCapability;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDanZiling extends ItemFood {
	public static final int effectTick = 400;
	public static final int magicRestore = 10;
	
	public ItemDanZiling() {
		super(2, 1F, true);
		setAlwaysEdible();
		setMaxStackSize(64);
		setUnlocalizedName("itemDanZiling");
		setRegistryName(ZijingMod.MODID + ":itemdanziling");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
		super.onFoodEaten(itemStack, world, player);
		if (!world.isRemote) {
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.HASTE, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, effectTick, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.LUCK, effectTick, 0));
			if(ShepherdProvider.hasCapabilityFromPlayer(player)) {
				ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
				shepherdCapability.setMagic(shepherdCapability.getMagic() + magicRestore);
				if(shepherdCapability.getMagic() > shepherdCapability.getMaxMagic()) {
					shepherdCapability.setMagic(shepherdCapability.getMaxMagic());
				}
				ShepherdProvider.updateChangeToClient(player);
			}
		}
	}

	public void onFoodEatenByEntityLivingBase(EntityLivingBase entity) {
		if (!entity.world.isRemote && entity instanceof EntityHasShepherdCapability) {
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, effectTick, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.LUCK, effectTick, 0));
    		ShepherdCapability shepherdCapability = ((EntityHasShepherdCapability)entity).getShepherdCapability();
			shepherdCapability.setMagic(Math.min(shepherdCapability.getMaxMagic(), shepherdCapability.getMagic() + this.magicRestore));
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemDanZiling.1", new Object[] {effectTick/20}));
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemDanZiling.2", new Object[] {magicRestore}));
	}
}