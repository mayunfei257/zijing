package com.zijing.blocks.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockZilingMieshaZhen extends Block{
    protected static final AxisAlignedBB ZLMSZ_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.125D, 1D);

	public BlockZilingMieshaZhen() {
		super(Material.IRON);
		setHardness(50f);
		setResistance(500.0f);
		setLightLevel(1.0f);
		setHarvestLevel("pickaxe", 2);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("blockZilingMieshaZhen");
		setRegistryName(ZijingMod.MODID + ":blockzilingmieshazhen");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		if(!world.isRemote &&  entity instanceof EntityLivingBase) {
			EntityLivingBase entityLive = (EntityLivingBase) entity;
			if(entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGhast){
				if(entityLive.getHealth() > 0){
					if(null == entityLive.getActivePotionEffect(MobEffects.SLOWNESS))
						entityLive.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 2));
					entityLive.setFire(2);
					world.spawnEntity(new EntityLightningBolt(world, i, j, k, true));
					entityLive.attackEntityFrom(DamageSource.MAGIC, 3);
				}
			}else if(entity instanceof EntityPlayer){
				if(null == entityLive.getActivePotionEffect(MobEffects.SPEED))
					entityLive.addPotionEffect(new PotionEffect(MobEffects.SPEED, 40, 2));
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return ZLMSZ_AABB;
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
