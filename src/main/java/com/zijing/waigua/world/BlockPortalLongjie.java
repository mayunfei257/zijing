package com.zijing.waigua.world;

import java.util.Random;

import com.zijing.util.ConstantUtil;
import com.zijing.waigua.world.DimensionLongjie.TeleporterDimensionMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPortalLongjie extends Block {
	public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.<EnumFacing.Axis> create("axis", EnumFacing.Axis.class, new EnumFacing.Axis[]{EnumFacing.Axis.X, EnumFacing.Axis.Z});
	protected static final AxisAlignedBB field_185683_b = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
	protected static final AxisAlignedBB field_185684_c = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
	protected static final AxisAlignedBB field_185685_d = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

	public BlockPortalLongjie() {
		super(Material.PORTAL);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Z));
		this.setTickRandomly(true);
		this.setHardness(-1.0F);
		this.setLightLevel(1F);
		this.setUnlocalizedName("blockPortalLongjie");
		this.setRegistryName(ConstantUtil.MODID + ":blockportallongjie");
	}

	@javax.annotation.Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing.Axis) state.getValue(AXIS)) {
			case X :
				return field_185683_b;
			case Z :
				return field_185684_c;
			case Y :
			default :
				return field_185685_d;
		}
	}

	public static int getMetaForAxis(EnumFacing.Axis axis) {
		return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public int getMetaFromState(IBlockState state) {
		return getMetaForAxis((EnumFacing.Axis) state.getValue(AXIS));
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
	}

	

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know
	 * which neighbor changed (coordinates passed are their own) Args: x, y,
	 * z, neighbor blockID
	 */
	@Override
	public void neighborChanged(IBlockState state, World par1World, BlockPos pos, Block neighborBlock, BlockPos fromPos) {

		int par2 = pos.getX();
		int par3 = pos.getY();
		int par4 = pos.getZ();

		byte b0 = 0;
		byte b1 = 1;
		if (getBlock(par1World, par2 - 1, par3, par4) == this || getBlock(par1World, par2 + 1, par3, par4) == this) {
			b0 = 1;
			b1 = 0;
		}
		int i1;
		for (i1 = par3; getBlock(par1World, par2, i1 - 1, par4) == this; --i1) {
			;
		}
		if (getBlock(par1World, par2, i1 - 1, par4) != Blocks.GOLD_BLOCK) {
			par1World.setBlockToAir(new BlockPos(par2, par3, par4));
		} else {
			int j1;
			for (j1 = 1; j1 < 4 && getBlock(par1World, par2, i1 + j1, par4) == this; ++j1) {
				;
			}
			if (j1 == 3 && getBlock(par1World, par2, i1 + j1, par4) == Blocks.GOLD_BLOCK) {
				boolean flag = getBlock(par1World, par2 - 1, par3, par4) == this || getBlock(par1World, par2 + 1, par3, par4) == this;
				boolean flag1 = getBlock(par1World, par2, par3, par4 - 1) == this || getBlock(par1World, par2, par3, par4 + 1) == this;
				if (flag && flag1) {
					par1World.setBlockToAir(new BlockPos(par2, par3, par4));
				} else {
					if ((getBlock(par1World, par2 + b0, par3, par4 + b1) != Blocks.GOLD_BLOCK || getBlock(par1World, par2 - b0, par3, par4 - b1) != this)
							&& (getBlock(par1World, par2 - b0, par3, par4 - b1) != Blocks.GOLD_BLOCK || getBlock(par1World, par2 + b0, par3, par4
									+ b1) != this)) {
						par1World.setBlockToAir(new BlockPos(par2, par3, par4));
					}
				}
			} else {
				par1World.setBlockToAir(new BlockPos(par2, par3, par4));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		EnumFacing.Axis axis = null;
		if (blockAccess.getBlockState(pos).getBlock() == this) {
			axis = blockAccess.getBlockState(pos).getValue(AXIS);
			if (axis == null) { return false; }
			if (axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) { return false; }
			if (axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) { return false; }
		}

		boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this && blockAccess.getBlockState(pos.west(2)).getBlock() != this;
		boolean flag1 = blockAccess.getBlockState(pos.east()).getBlock() == this && blockAccess.getBlockState(pos.east(2)).getBlock() != this;
		boolean flag2 = blockAccess.getBlockState(pos.north()).getBlock() == this && blockAccess.getBlockState(pos.north(2)).getBlock() != this;
		boolean flag3 = blockAccess.getBlockState(pos.south()).getBlock() == this && blockAccess.getBlockState(pos.south(2)).getBlock() != this;
		boolean flag4 = flag || flag1 || axis == EnumFacing.Axis.X;
		boolean flag5 = flag2 || flag3 || axis == EnumFacing.Axis.Z;
		return flag4 && side == EnumFacing.WEST ? true : (flag4 && side == EnumFacing.EAST ? true : (flag5 && side == EnumFacing.NORTH ? true : flag5 && side == EnumFacing.SOUTH));
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{AXIS});
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, BlockPos pos, IBlockState blockState, Entity entity) {
		if (entity.getRidingEntity() == null && !entity.isBeingRidden() && entity instanceof EntityPlayerMP) {
			EntityPlayerMP thePlayer = (EntityPlayerMP) entity;
			if (thePlayer.timeUntilPortal > 0) {
				thePlayer.timeUntilPortal = 10;
			} else if (thePlayer.dimension != DimensionLongjie.DIMID) {
				thePlayer.timeUntilPortal = 10;
				thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, DimensionLongjie.DIMID, new TeleporterDimensionMod(thePlayer.mcServer.getWorld(DimensionLongjie.DIMID)));
			} else {
				thePlayer.timeUntilPortal = 10;
				thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, new TeleporterDimensionMod(thePlayer.mcServer.getWorld(0)));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random random) {
		if (random.nextInt(100) == 0) {
			world.playSound(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.portal.ambient"))), SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
		}
		for (int l = 0; l < 4; ++l) {
			double x = blockPos.getX() + 1D - random.nextFloat();
			double y = blockPos.getY() + 1D - random.nextFloat();
			double z = blockPos.getZ() + 1D - random.nextFloat();
			double xSpeed = (random.nextFloat() - 0.5D) * 0.5D;
			double ySpeed = (random.nextFloat() - 0.5D) * 0.5D;
			double zSpeed = (random.nextFloat() - 0.5D) * 0.5D;
			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}

	public static Block getBlock(IBlockAccess world, int i, int j, int k) {
		return world.getBlockState(new BlockPos(i, j, k)).getBlock();
	}
}