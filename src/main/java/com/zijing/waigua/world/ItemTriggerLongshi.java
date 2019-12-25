package com.zijing.waigua.world;

import com.zijing.BaseControl;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTriggerLongshi extends Item {
	public Block frameBlock = Blocks.GOLD_BLOCK;
	
	public ItemTriggerLongshi() {
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(64);
		this.setUnlocalizedName("itemTriggerLongshi");
		this.setRegistryName(ConstantUtil.MODID + ":itemtriggerlongshi");
		this.setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			if(world.getBlockState(pos).getBlock() == frameBlock && world.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) {
				world.playSound(player, pos.offset(facing), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
//				tryToCreatePortal(world, pos, facing);
			}
			player.getHeldItem(hand).damageItem(1, player);
		}
		return EnumActionResult.SUCCESS;
	}
	
	
	public boolean tryToCreatePortal(World world, BlockPos pos, EnumFacing facing) {
		EnumFacing.Axis axis = null;
		BlockPos portalPos = pos.offset(facing);
		boolean isLeftPosFlag = true;
		if (world.getBlockState(portalPos.west()).getBlock() == frameBlock) {
			axis = EnumFacing.Axis.X;
			isLeftPosFlag = true;
		}else if (world.getBlockState(portalPos.east()).getBlock() == frameBlock) {
			axis = EnumFacing.Axis.X;
			isLeftPosFlag = false;
		}else if (world.getBlockState(portalPos.north()).getBlock() == frameBlock) {
			axis = EnumFacing.Axis.Z;
			isLeftPosFlag = true;
		}else if (world.getBlockState(portalPos.south()).getBlock() == frameBlock) {
			axis = EnumFacing.Axis.Z;
			isLeftPosFlag = false;
		}else {
			return false;
		}
		BlockPos portalBasePos = portalPos;
		if(!isLeftPosFlag) {
			portalBasePos = axis == EnumFacing.Axis.X ? portalBasePos.west() : portalBasePos.north();
		}
		if(world.getBlockState(portalPos.up()).getBlock() == frameBlock){
			portalBasePos = portalPos.down();
		}
		//check
		for(int x = -1; x <= 2; x++){
			for(int y = -1; y <= 2; y++) {
				int X = portalBasePos.getX() + (axis == EnumFacing.Axis.X ? x : 0);
				int Z = portalBasePos.getZ() + (axis == EnumFacing.Axis.Z ? x : 0);
				int Y = portalBasePos.getY() + y;
				if(x == -1 || x == 2 || y == -1 || y == 2) {
					if(!((x == -1 && y == -1) || (x == 2 && y == 2) || (x == -1 && y == 2) || (x == 2 && y == -1))) {
						if(world.getBlockState(new BlockPos(X, Y, Z)).getBlock() != frameBlock) {
							return false;
						}
					}
				}else{
					if(world.getBlockState(new BlockPos(X, Y, Z)).getBlock() != Blocks.AIR) {
						return false;
					}
				}
			}
		}
		//set portal
		for (int x = 0; x < 2; ++x) {
			for (int y = 0; y < 2; ++y) {
				int X = portalBasePos.getX() + (axis == EnumFacing.Axis.X ? x : 0);
				int Z = portalBasePos.getZ() + (axis == EnumFacing.Axis.Z ? x : 0);
				int Y = portalBasePos.getY() + y;
				IBlockState iblockstate = BaseControl.blockPortalLongjie.getDefaultState().withProperty(BlockPortal.AXIS, axis);
				world.setBlockState(new BlockPos(X, Y, Z), iblockstate, 3);
			}
		}
		return true;
	}
}