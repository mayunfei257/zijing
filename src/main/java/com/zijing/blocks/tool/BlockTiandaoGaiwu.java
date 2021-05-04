package com.zijing.blocks.tool;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.entity.TileEntityTiandaoGaiwu;
import com.zijing.gui.GuiTiandaoGaiwu;
import com.zijing.gui.GuiZhulingTai;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTiandaoGaiwu extends BlockContainer{

	public BlockTiandaoGaiwu(){
		super(Material.ROCK);
		setHardness(50f);
		setResistance(1000.0f);
		setSoundType(SoundType.METAL);
		setHarvestLevel("pickaxe", 0);
		setUnlocalizedName("blockTiandaoGaiwu");
		setRegistryName(ConstantUtil.MODID + ":blocktiandaogaiwu");
		setCreativeTab(ZijingTab.zijingTab);
		this.setDefaultState(this.blockState.getBaseState());
	}
	
	/**
     * Called when the block is right clicked by a player.
     */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if (worldIn.isRemote){
            return true;
        } else{
        	TileEntityTiandaoGaiwu tileEntityTiandaoGaiwu = (TileEntityTiandaoGaiwu)worldIn.getTileEntity(pos);
            if (tileEntityTiandaoGaiwu != null){
            	playerIn.openGui(ZijingMod.instance, GuiTiandaoGaiwu.GUIID, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
	}
	 
	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileEntityTiandaoGaiwu();
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		if (stack.hasDisplayName()){
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityTiandaoGaiwu){
				((TileEntityTiandaoGaiwu)tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileEntityTiandaoGaiwu){
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityTiandaoGaiwu)tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state){
		return true;
	}

	@Override
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