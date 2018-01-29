package com.zijing.util;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Building {
	
	public boolean buildASmallHouse(World world, BlockPos baseBlockPos) {
		IBlockState floor = blocksUtil.end_bricks;
		IBlockState wall = blocksUtil.planks1;
		IBlockState roof = blocksUtil.planks5;
		IBlockState roof2 = blocksUtil.wooden_slab5.withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);

		for(int x = 0; x <= 7; x++) {
			for(int y = 0; y <= 5; y++) {
				for(int z = 0; z <= 5; z++) {
					if((x == 0 || x == 7 || z == 0 || z == 5) && y < 4) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), wall);
					}else if(x != 0 && x != 7 && z != 0 && z != 5 && y == 0) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), floor);
					}else if(y == 4) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), roof);
					}else if(x == 3 && (z == 1 || z == 4)) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), wall);
					}else if(x == 1 && z == 1 && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.bedHead);
					}else if(x == 1 && z == 2 && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.bedFoot);
					}else if(x == 2 && z == 1 && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.jukebox);
					}else if(((x == 1 || x == 2) && z == 4 && y == 1)) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.chest);
					}else if(x == 6 && (z == 1 || z == 2) && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.chest.withProperty(BlockChest.FACING, EnumFacing.WEST));
					}else if(x ==6 && z == 3 && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.anvil.withProperty(BlockAnvil.FACING, EnumFacing.NORTH));
					}else if(x ==6 && z == 4 && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.cauldron.withProperty(BlockCauldron.LEVEL, Integer.valueOf(3)));
					}else if(x == 6 && (z == 1 || z == 2 || z == 3 || z == 4) && y == 2) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.wooden_slab1);
					}else if(x == 6 && z == 1 && y == 3) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.furnace.withProperty(BlockFurnace.FACING, EnumFacing.WEST));
					}else if(x == 6 && z == 2 && y == 3) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.craftingTable);
					}else if(x ==6 && z == 3 && y == 3) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.brewingStand);
					}else if(x ==6 && z == 4 && y == 3) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.diamond_block);
					}else if((x == 0 || x == 7 || z == 0 || z == 5) && y == 5) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), roof2);
					}else {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), Blocks.AIR.getDefaultState());
					}
					//²¹¶¡
					if((x == 4 || x == 5) && z == 0 && y == 2) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.glass_pane);
					}else if(x == 4 && z == 5 && y == 1) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.oak_door.withProperty(BlockDoor.FACING, EnumFacing.SOUTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER));
					}else if(x == 4 && z == 5 && y == 2) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.oak_door.withProperty(BlockDoor.FACING, EnumFacing.SOUTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
					}
				}
			}
		}
		return true;
	}
	
	
	public boolean buildBase(World world, BlockPos baseBlockPos) {
		for(int x = 0; x < 64; x++) {
			for(int y = 0; y < 5; y++) {
				for(int z = 0; z < 64; z++) {
					if(y == 0) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.obsidian);
					}else if(y == 1 || y == 2) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.stone);
					}else if(y == 3) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.dirt);
					}else if(y == 4) {
						world.setBlockState(new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z), blocksUtil.grass);
					}
				}
			}
		}
		return false;
	}
	//*********************************************************************************************************************************************************************
	private static BlocksUtil blocksUtil;
	private static Building instance;
	
	private Building() {}
	
	public static Building getinstance() {
		if(null == instance) {
			synchronized (Building.class) {
				if(null == instance) {
					instance = new Building();
					blocksUtil = BlocksUtil.getinstance();
				}
			}
		}
		return instance;
	}
}
