package com.zijing.blocks.tool;

import java.util.Random;

import com.zijing.ZijingMod;
import com.zijing.entity.TileEntityZhulingTai;
import com.zijing.main.ZijingTab;
import com.zijing.main.gui.GuiZhulingTai;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockZhulingTai extends BlockContainer{
	private final boolean isBurning;

	public BlockZhulingTai(boolean isBurning){
		super(Material.ROCK);
		setHardness(50f);
		setResistance(500.0f);
		setSoundType(SoundType.METAL);
		setHarvestLevel("pickaxe", 2);
		setUnlocalizedName("blockZhulingTai");
		setRegistryName(ZijingMod.MODID + ":blockzhulingtai");
		setCreativeTab(ZijingTab.zijingTab);
		this.setDefaultState(this.blockState.getBaseState());
		this.isBurning = isBurning;
	}

	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
		if(!worldIn.isRemote) {
			if (this.isBurning){
				setLightLevel(1.0f);
			}else {
				setLightLevel(0.0f);
			}
		}else {
			if (this.isBurning){
				double x = pos.getX() + 0.5D;
				double y = pos.getY() + 0.5D + rand.nextDouble() / 2.0D;
				double z = pos.getZ() + 0.5D;
				if (rand.nextDouble() < 0.2D){
					worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
				}
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.52D, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, x - 0.52D, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if (!worldIn.isRemote){
			playerIn.openGui(ZijingMod.instance, GuiZhulingTai.GUIID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileEntityZhulingTai();
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		if (stack.hasDisplayName()){
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityZhulingTai){
				((TileEntityZhulingTai)tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileEntityZhulingTai){
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityZhulingTai)tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(worldIn, pos, state);
	}

	public boolean hasComparatorInputOverride(IBlockState state){
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos){
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	/**
	 * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
	 * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
	 */
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state){
		return 0;
	}
}