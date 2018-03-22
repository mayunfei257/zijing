package com.zijing.items.tool;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingChu extends ItemHoe{

	public ItemToolZijingChu() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingChu");
		setRegistryName(ConstantUtil.MODID + ":itemtoolzijingchu");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		if(!world.isRemote && player.isSneaking() && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(shepherdCapability.getMagic() >= 1) {
				Random random = new Random();
				IBlockState iBlockState = world.getBlockState(pos);
				iBlockState.getBlock().updateTick(world, pos, iBlockState, random);
				shepherdCapability.setMagic(shepherdCapability.getMagic() - 1.0D);
				ShepherdProvider.updateChangeToClient(player);
			}else {
				player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 1!"));
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
        if (!world.isRemote){
        	Block block = state.getBlock();
			if(entity.isSneaking() && block instanceof IPlantable) {
				for(;;) {
					Block thisBlock = world.getBlockState(pos).getBlock();
					Block thisBlockDown = world.getBlockState(pos.down()).getBlock();
					if((thisBlock == Blocks.AIR || thisBlock == block) && (thisBlockDown != block && thisBlockDown != Blocks.AIR)) {
						dropBlock(world, block, pos);
						break;
					}
					pos = pos.down();
				}
			}
        }
    	return true;
    }

	private void dropBlock(World world, Block block, BlockPos pos) {
		for(int x = 1; x >= -1; x--) {
			for(int z = 1; z >= -1; z--) {
				if(x == 0 && z == 0) { continue; }
				BlockPos posTem = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
				if(dropIPlant(world, block, posTem)) {
					dropBlock(world, block, posTem);
				}
			}
		}
	}
	
	private boolean dropIPlant(World world, Block block, BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != block) { return false; }
		if(world.getBlockState(pos.down()).getBlock() == block) {
			for(;;) {
				if(world.getBlockState(pos).getBlock() != block) { return false; }
				if(world.getBlockState(pos.up()).getBlock() == block && world.getBlockState(pos).getBlock() == block && world.getBlockState(pos.down()).getBlock() != block) {
					block.dropBlockAsItem(world, pos.up(), world.getBlockState(pos.up()), 1);
					world.setBlockToAir(pos.up());
//					world.notifyNeighborsOfStateChange(pos.up(), block, true);
					return true;
				}
				pos = pos.down();
			}
		}else {
			if(world.getBlockState(pos.up()).getBlock() == block && world.getBlockState(pos).getBlock() == block && world.getBlockState(pos.down()).getBlock() != block) {
				block.dropBlockAsItem(world, pos.up(), world.getBlockState(pos.up()), 1);
				world.setBlockToAir(pos.up());
//				world.notifyNeighborsOfStateChange(pos.up(), block, true);
			}
		}
		return false;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemToolZijingChu.skill1", new Object[] {1}));
    }
}
