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
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockYanQuan extends Block{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    private static final int ageK = 15;
    private static final int ageRandom = 1;
    
    public BlockYanQuan() {
		super(Material.IRON);
        setHardness(50f);
        setResistance(1000.0f);
        setLightLevel(1f);
        setTickRandomly(true);
        setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        setHarvestLevel("pickaxe", 0);
        setSoundType(SoundType.METAL);
		setUnlocalizedName("blockYanQuan");
		setRegistryName(ConstantUtil.MODID + ":blockyanquan");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		int age = ((Integer) state.getValue(AGE)).intValue();
		if (age + ageK >= 15) {
			boolean clean = checkAndSetLava(worldIn, pos, state);
			if(clean) {
				worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
		        setLightLevel(0F);
			}
		} else if(Math.random() <= ageRandom){
			worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(age + ageK)), 4);
			setLightLevel(Math.min(1, (age + 1) * 0.066F));
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
	
	private boolean checkAndSetLava(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos baseBlockPos = pos.up();
		Material blockMaterial = worldIn.getBlockState(baseBlockPos).getMaterial();
		
		if(blockMaterial == Material.AIR || blockMaterial == Material.LAVA || blockMaterial == Material.WATER || blockMaterial == Material.FIRE || blockMaterial == Material.SNOW) {
			for(int y = 0; y < 2; y ++) {
				for(int x = -1; x <= 1; x ++) {
					for(int z = -1; z <= 1; z ++) {
						BlockPos nowBlockPos = new BlockPos(baseBlockPos.getX() + x, baseBlockPos.getY() + y, baseBlockPos.getZ() + z);
						Block nowBlock = worldIn.getBlockState(nowBlockPos).getBlock();
						if(Blocks.LAVA != nowBlock && nowBlock.isReplaceable(worldIn, pos)) {
							worldIn.setBlockState(nowBlockPos, Blocks.LAVA.getDefaultState());
							worldIn.neighborChanged(nowBlockPos, Blocks.LAVA, nowBlockPos);
							return true;
						}
					}
				}
			}	
		}
		return false;
	}
	
}
