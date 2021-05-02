package com.zijing.blocks.tool;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.zijing.BaseControl;
import com.zijing.ZijingTab;
import com.zijing.entity.EntitySuperIronGolem;
import com.zijing.entity.EntitySuperSnowman;
import com.zijing.entity.EntityZhenling;
import com.zijing.util.ConstantUtil;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
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

    private BlockPattern zhenlingPattern;

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
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state){
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
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
	
	private void trySpawnGolem(World worldIn, BlockPos pos){
        BlockPattern.PatternHelper blockpattern$patternhelper = this.getZhenlingPattern().match(worldIn, pos);

        if (blockpattern$patternhelper != null){
            for (int i = 0; i < this.getZhenlingPattern().getThumbLength(); ++i){
                BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                worldIn.setBlockState(blockworldstate.getPos(), Blocks.AIR.getDefaultState(), 2);
            }

            EntityZhenling entitysnowman = new EntityZhenling(worldIn);
            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            entitysnowman.setHomePos(blockpos1);
            entitysnowman.updataSwordDamageAndArmorValue();
            worldIn.spawnEntity(entitysnowman);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitysnowman.getEntityBoundingBox().grow(5.0D))){
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitysnowman);
            }

            for (int l = 0; l < 120; ++l){
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int i1 = 0; i1 < this.getZhenlingPattern().getThumbLength(); ++i1){
                BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(0, i1, 0);
                worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
            }
        }
    }
	
    protected BlockPattern getZhenlingPattern(){
        if (this.zhenlingPattern == null){

            Predicate<IBlockState> IS_PUMPKIN = new Predicate<IBlockState>(){
                public boolean apply(@Nullable IBlockState iBlockState){
                    return iBlockState != null && iBlockState.getBlock() == BaseControl.blockSuperNangua;
                }
            };
            this.zhenlingPattern = FactoryBlockPattern.start().aisle("~", "^", "#", "$")
            		.where('~', BlockWorldState.hasState(BlockStateMatcher.forBlock(BaseControl.blockZilingMieshaZhen)))
            		.where('^', BlockWorldState.hasState((iBlockState) -> {return iBlockState != null && iBlockState.getBlock() == BaseControl.blockSuperNangua;}))
            		.where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW)))
            		.where('$', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK)))
            		.build();
        }

        return this.zhenlingPattern;
    }
}
