package com.zijing.waigua;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.itf.MagicConsumer;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffBuilding extends Item  implements MagicConsumer{

	public ItemStaffBuilding() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemStaffBuilding");
		setRegistryName(ConstantUtil.MODID + ":itemstaffbuilding");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		if(!world.isRemote) {
			if(!player.isSneaking()) {
				Building.getinstance().buildArableLand2(world, player.getPosition().down(), BaseControl.blockZilingCao);
			}else {
				Building.getinstance().buildArableLand(world, player.getPosition().down(), BaseControl.blockZilingCao);
			}
		}
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote) {
			if(!player.isSneaking()) {
				Building.getinstance().buildArableLand2(world, pos, BaseControl.blockZilingCao);
			}else {
				Building.getinstance().buildArableLand(world, pos, BaseControl.blockZilingCao);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public int getMaxMagicEnergyValue() {
		return ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY();
	}

	@Override
	public int getMinMagicEnergyValue() {
		return 0;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffWaigua.skill1", new Object[0]));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffWaigua.skill2", new Object[0]));
    }
}
