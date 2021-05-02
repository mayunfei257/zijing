package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.itf.EntityFriendly;
import com.zijing.itf.ItemFoodDan;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDanZiling extends ItemFood implements ItemFoodDan{
	public static final int effectTick = 600;
	public static final int magicRestore = 30;
	
	public ItemDanZiling() {
		super(2, 1F, true);
		setAlwaysEdible();
		setMaxStackSize(64);
		setUnlocalizedName("itemDanZiling");
		setRegistryName(ConstantUtil.MODID + ":itemdanziling");
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

	@Override
	public void onFoodEatenByEntityFriendly(EntityFriendly entity) {
		if (!entity.world.isRemote && entity instanceof EntityFriendly) {
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
    		ShepherdCapability shepherdCapability = ((EntityFriendly)entity).getShepherdCapability();
			shepherdCapability.setMagic(Math.min(shepherdCapability.getMaxMagic(), shepherdCapability.getMagic() + this.magicRestore));
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemDanZiling.1", new Object[] {effectTick/20}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemDanZiling.2", new Object[] {magicRestore}));
	}
}