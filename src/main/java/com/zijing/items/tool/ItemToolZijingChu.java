package com.zijing.items.tool;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingChu extends ItemHoe{

	public ItemToolZijingChu() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingChu");
		setRegistryName(ZijingMod.MODID + ":itemtoolzijingchu");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		if(!world.isRemote) {
			IBlockState iBlockState = world.getBlockState(pos);
			Block block = iBlockState.getBlock();
			block.updateTick(world, pos, iBlockState, new Random());
			player.getHeldItem(hand).damageItem(1, player);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("Make the block updateTick once!");
    }
}
