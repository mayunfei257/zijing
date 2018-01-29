package com.zijing.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.zijing.main.ZijingTab;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrowthTypePlant extends Block implements net.minecraftforge.common.IPlantable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB GROWTHTYPE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
    protected EnumPlantType enumPlantType;
    protected int high;
    protected int growthKey;
    protected int dropAmount;

	public BlockGrowthTypePlant() {
        super(Material.PLANTS);
        this.high = 3;
        this.growthKey = 1;
        this.dropAmount = 1;
        this.enumPlantType = EnumPlantType.Crop;
		setHardness(0.0F);
		setResistance(2.0F);
        setTickRandomly(true);
        setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		setSoundType(SoundType.PLANT);
		setCreativeTab(ZijingTab.zijingTab);
	}
	
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return GROWTHTYPE_AABB;
    }

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		if (worldIn.getBlockState(pos.down()).getBlock() == this || this.checkForDrop(worldIn, pos, state)) {
			if (worldIn.isAirBlock(pos.up())) {
				int l;
				for (l = 1; worldIn.getBlockState(pos.down(l)).getBlock() == this; ++l) {
					;
				}
				if (l < high) {
					int age = ((Integer) state.getValue(AGE)).intValue();
					if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)){
						if (age >= 15) {
							worldIn.setBlockState(pos.up(), this.getDefaultState());
							worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
						} else {
							worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(age + growthKey)), 4);
						}
						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
					}
				}
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.down()).getBlock();
		return block.canSustainPlant(worldIn.getBlockState(pos.down()), worldIn, pos.down(), EnumFacing.UP, this) || block == this;
	}

	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
        this.checkForDrop(worldIn, pos, state);
    }

    protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state){
        if (this.canPlaceBlockAt(worldIn, pos)){
            return true;
        }else{
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos){
        return NULL_AABB;
    }
    
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}
	
	@Override
	public int quantityDropped(Random par1Random) {
		return dropAmount;
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

	@Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state){
		return new ItemStack(Item.getItemFromBlock(this), 1);
    }

	@Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state){
        return ((Integer)state.getValue(AGE)).intValue();
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return enumPlantType;
	}
	
    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos){
        return this.getDefaultState();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return BlockFaceShape.UNDEFINED;
    }
}