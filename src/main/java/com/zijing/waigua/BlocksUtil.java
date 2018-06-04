package com.zijing.waigua;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class BlocksUtil {

	public IBlockState cake = Blocks.CAKE.getDefaultState().withProperty(BlockCake.BITES, Integer.valueOf(0));
	public IBlockState hay_block = Blocks.HAY_BLOCK.getDefaultState();
	public IBlockState melon_block = Blocks.MELON_BLOCK.getDefaultState();
	public IBlockState pumpkin = Blocks.PUMPKIN.getDefaultState();
	
	public IBlockState bookShelf = Blocks.BOOKSHELF.getDefaultState();
	public IBlockState brewingStand = Blocks.BREWING_STAND.getDefaultState();
	public IBlockState craftingTable = Blocks.CRAFTING_TABLE.getDefaultState();
	public IBlockState enchantingTable = Blocks.ENCHANTING_TABLE.getDefaultState();
	public IBlockState furnace = Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
	public IBlockState cauldron = Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, Integer.valueOf(0));
	public IBlockState anvil = Blocks.ANVIL.getDefaultState().withProperty(BlockAnvil.FACING, EnumFacing.NORTH).withProperty(BlockAnvil.DAMAGE, Integer.valueOf(0));//ÌúÕè

	public IBlockState chest = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH);//Ïä×Ó
	public IBlockState enderChest = Blocks.ENDER_CHEST.getDefaultState().withProperty(BlockEnderChest.FACING, EnumFacing.NORTH);
	public IBlockState jukebox = Blocks.JUKEBOX.getDefaultState().withProperty(BlockJukebox.HAS_RECORD, Boolean.valueOf(false));
	public IBlockState bedHead = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD).withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false));
	public IBlockState bedFoot = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT).withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false));
	
	public IBlockState diamond_block = Blocks.DIAMOND_BLOCK.getDefaultState();
	public IBlockState emerald_block = Blocks.EMERALD_BLOCK.getDefaultState();
	public IBlockState gold_block = Blocks.GOLD_BLOCK.getDefaultState();
	public IBlockState iron_block = Blocks.IRON_BLOCK.getDefaultState();
	public IBlockState redstone_block = Blocks.REDSTONE_BLOCK.getDefaultState();
	public IBlockState coal_block = Blocks.COAL_BLOCK.getDefaultState();
	public IBlockState lapis_block = Blocks.LAPIS_BLOCK.getDefaultState();
	
	public IBlockState CLAY = Blocks.CLAY.getDefaultState();
	
	public IBlockState planks0 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
	public IBlockState planks1 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
	public IBlockState planks2 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
	public IBlockState planks3 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE);
	public IBlockState planks4 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
	public IBlockState planks5 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
	
	public IBlockState wooden_slab0 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
	public IBlockState wooden_slab1 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
	public IBlockState wooden_slab2 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
	public IBlockState wooden_slab3 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
	public IBlockState wooden_slab4 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
	public IBlockState wooden_slab5 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
	
	public IBlockState oak_door = Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	public IBlockState spruce_door = Blocks.SPRUCE_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	public IBlockState birch_door = Blocks.BIRCH_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	public IBlockState jungle_door = Blocks.JUNGLE_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	public IBlockState acacia_door = Blocks.ACACIA_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	public IBlockState dark_oak_door = Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	public IBlockState iron_door = Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(false)).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER);
	
	public IBlockState trapdoor = Blocks.TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.FACING, EnumFacing.NORTH).withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockTrapDoor.HALF, BlockTrapDoor.DoorHalf.TOP);
	public IBlockState iron_trapdoor = Blocks.IRON_TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.FACING, EnumFacing.NORTH).withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(false)).withProperty(BlockTrapDoor.HALF, BlockTrapDoor.DoorHalf.TOP);
	
	public IBlockState oak_fence = Blocks.OAK_FENCE.getDefaultState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false));
	public IBlockState spruce_fence = Blocks.SPRUCE_FENCE.getDefaultState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false));
	public IBlockState birch_fence = Blocks.BIRCH_FENCE.getDefaultState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false));
	public IBlockState jungle_fence = Blocks.JUNGLE_FENCE.getDefaultState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false));
	public IBlockState acacia_fence = Blocks.ACACIA_FENCE.getDefaultState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false));
	public IBlockState dark_oak_fence = Blocks.DARK_OAK_FENCE.getDefaultState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false));
	
	public IBlockState iron_bars = Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false));
	public IBlockState cobblestone_wall = Blocks.COBBLESTONE_WALL.getDefaultState().withProperty(BlockWall.UP, Boolean.valueOf(false)).withProperty(BlockWall.NORTH, Boolean.valueOf(false)).withProperty(BlockWall.EAST, Boolean.valueOf(false)).withProperty(BlockWall.SOUTH, Boolean.valueOf(false)).withProperty(BlockWall.WEST, Boolean.valueOf(false)).withProperty(BlockWall.VARIANT, BlockWall.EnumType.NORMAL);
	
	public IBlockState oak_fence_gate = Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
	public IBlockState spruce_fence_gate = Blocks.SPRUCE_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
	public IBlockState birch_fence_gate = Blocks.BIRCH_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
	public IBlockState jungle_fence_gate = Blocks.JUNGLE_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
	public IBlockState acacia_fence_gate = Blocks.ACACIA_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
	public IBlockState dark_oak_fence_gate = Blocks.DARK_OAK_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.OPEN, Boolean.valueOf(false)).withProperty(BlockFenceGate.POWERED, Boolean.valueOf(false)).withProperty(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
	
	public IBlockState oak_stairs = Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
	public IBlockState spruce_stairs = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
	public IBlockState birch_stairs = Blocks.BIRCH_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
	public IBlockState jungle_stairs = Blocks.JUNGLE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
	public IBlockState acacia_stairs = Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
	public IBlockState dark_oak_stairs = Blocks.DARK_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
	
	public IBlockState obsidian = Blocks.OBSIDIAN.getDefaultState();
	public IBlockState end_stone = Blocks.END_STONE.getDefaultState();
	public IBlockState end_bricks = Blocks.END_BRICKS.getDefaultState();
	public IBlockState glowstone = Blocks.GLOWSTONE.getDefaultState();
	public IBlockState glass = Blocks.GLASS.getDefaultState();
	public IBlockState glass_pane = Blocks.GLASS_PANE.getDefaultState();
	public IBlockState stone = Blocks.STONE.getDefaultState();
	public IBlockState grass = Blocks.GRASS.getDefaultState();
	public IBlockState dirt = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).withProperty(BlockDirt.SNOWY, Boolean.valueOf(false));

	public IBlockState MOB_SPAWNER = Blocks.MOB_SPAWNER.getDefaultState();
//	public IBlockState DARK_OAK_BOAT
	//*********************************************************************************************************************************************************************
	private static BlocksUtil instance;
	private BlocksUtil() {}
	public static BlocksUtil getinstance() {
		if(null == instance) {
			synchronized (BlocksUtil.class) {
				if(null == instance) {
					instance = new BlocksUtil();
				}
			}
		}
		return instance;
	}
}
