package com.zijing.blocks.tool;

import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockZilingMieshaZhen extends Block{
    protected static final AxisAlignedBB ZLMSZ_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.125D, 1D);
    protected static int MAX_INTERVAL = 5;
    private double randomR = 0.2D;

	public BlockZilingMieshaZhen() {
		super(Material.IRON);
		setHardness(50f);
		setResistance(1000.0f);
		setLightLevel(1.0f);
		setHarvestLevel("pickaxe", 0);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("blockZilingMieshaZhen");
		setRegistryName(ConstantUtil.MODID + ":blockzilingmieshazhen");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(entity instanceof EntityLivingBase) {
			EntityLivingBase entityLive = (EntityLivingBase) entity;
			if(entity instanceof IMob){
				if(entityLive.getHealth() > 0){
					if(null == entityLive.getActivePotionEffect(MobEffects.SLOWNESS))
						entityLive.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 2));
					entityLive.setFire(3);
					entityLive.attackEntityFrom(DamageSource.MAGIC, 10);
					world.playSound((EntityPlayer) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
//					world.createExplosion(entityLive, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, 1, true);
					if(Math.random() <= this.randomR) {
						world.spawnEntity(new EntityLightningBolt(world, entity.posX, entity.posY, entity.posZ, false));
					}
				}
			}else if(entity instanceof EntityPlayer){
				if(null == entityLive.getActivePotionEffect(MobEffects.SPEED))
					entityLive.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 2));
				if(null == entityLive.getActivePotionEffect(MobEffects.ABSORPTION))
					entityLive.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 60, 2));
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
