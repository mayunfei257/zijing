package com.zijing.blocks.tool;

import java.util.Random;

import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockZilingZhaohuanZhen extends Block{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB ZLZHZ_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.125D, 1D);

	public BlockZilingZhaohuanZhen() {
		super(Material.IRON);
        setHardness(50f);
        setResistance(1000.0f);
        setLightLevel(0f);
        setTickRandomly(true);
        setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        setHarvestLevel("pickaxe", 0);
        setSoundType(SoundType.METAL);
		setUnlocalizedName("blockZilingZhaohuanZhen");
		setRegistryName(ConstantUtil.MODID + ":blockzilingzhaohuanzhen");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		int age = ((Integer) state.getValue(AGE)).intValue();
		if (age >= 15) {
			EntityIronGolem entity = new EntityIronGolem(worldIn);
			entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, worldIn.rand.nextFloat() * 360F, 0.0F);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 400, 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 400, 1));
			entity.setPlayerCreated(true);
			entity.setHealth(200F);
			entity.playLivingSound();
			worldIn.spawnEntity(entity);
			worldIn.spawnEntity(new EntityLightningBolt(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, false));
			worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
			setLightLevel(0.0F);
		} else if(Math.random() <= 0.25){
			worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(age + 1)), 4);
			setLightLevel((age + 1) * 0.066F);
		}
	}

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }

	@Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

	@Override
    public int getMetaFromState(IBlockState state){
        return ((Integer)state.getValue(AGE)).intValue();
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return ZLZHZ_AABB;
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
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos){
        return true;
    }

	@Override
    public boolean canSpawnInBlock(){
        return true;
    }

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return BlockFaceShape.UNDEFINED;
    }
}