package com.zijing.items.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.gui.GuiBookChuansong;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound bookTag = new NBTTagCompound();
			ItemStackHelper.saveAllItems(bookTag, NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY), true);
			itemStack.setTagCompound(bookTag);
		}
		if(!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()){
			if(player.isSneaking()){
				player.openGui(ZijingMod.instance, GuiBookChuansong.GUIID, world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
				return new ActionResult(EnumActionResult.SUCCESS, itemStack);
			}else {
				return super.onItemRightClick(world, player, hand);
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote && player.isSneaking() && world.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
			if(player.getHeldItem(hand).hasTagCompound()) {
				NBTTagCompound chuansongBookTag = player.getHeldItem(hand).getTagCompound();
				NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(chuansongBookTag, items);
				setPlant(world, items, pos.up(), 1, 1);
				ItemStackHelper.saveAllItems(chuansongBookTag, items, true);
			}
            return EnumActionResult.SUCCESS;
		}else {
			return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
        if (!world.isRemote){
        	Block block = state.getBlock();
			if(entity.isSneaking() && block instanceof IPlantable) {
				if(block instanceof BlockCrops) {
					dropBlock2(world, block, pos, 1, 1);
				}else {
					for(;pos.getY() > 0;) {
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
				return true;
			}
		}
		return false;
	}
	
	private void dropBlock2(World world, Block block, BlockPos pos, int maxSkip, int skip) {
		for(int x = 1; x >= -1; x--) {
			for(int z = 1; z >= -1; z--) {
				if(x == 0 && z == 0) { continue; }
				int skipTemp = skip;
				BlockPos posTem = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
				IBlockState state = world.getBlockState(posTem);
				if(state.getBlock() == block) {
					if(((BlockCrops)block).isMaxAge(state)) {
						block.dropBlockAsItem(world, posTem, world.getBlockState(posTem), 1);
						world.setBlockToAir(posTem);
					}
					skipTemp = maxSkip;
				}else {
					skipTemp--;
				}
				if(skipTemp >= 0) {
					dropBlock2(world, block, posTem, maxSkip, skipTemp);
				}
			}
		}
	}
	
	public void setPlant(World world, NonNullList<ItemStack> items, BlockPos pos, int maxSkip, int skip) {
		int skipTemp = skip;
		for(int z = 0; z < 1024; z++) {
			BlockPos posTem = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + z);
			if(world.getBlockState(posTem).getBlock() == Blocks.AIR && world.getBlockState(posTem.down()).getBlock() == Blocks.FARMLAND) {
				boolean success = planting(world, items, posTem);
				skipTemp = success ? maxSkip : skipTemp--;
			}else {
				skipTemp--;
			}
			if(skipTemp < 0) break;
		}
		for(int z = -1; z > -1024; z--) {
			BlockPos posTem = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + z);
			if(world.getBlockState(posTem).getBlock() == Blocks.AIR && world.getBlockState(posTem.down()).getBlock() == Blocks.FARMLAND) {
				boolean success = planting(world, items, posTem);
				skipTemp = success ? maxSkip : skipTemp--;
			}else {
				skipTemp--;
			}
			if(skipTemp < 0) break;
		}
	}
	
	public boolean planting(World world, NonNullList<ItemStack> items, BlockPos pos) {
		ItemStack itemStack = items.get(0);
		for(ItemStack itemStacktemp: items) {
			if(!itemStacktemp.isEmpty() && itemStacktemp.getItem() instanceof ItemSeeds) {
				itemStack = itemStacktemp;
				break;
			}
		}
		if(itemStack.isEmpty()) return false;
		
		world.setBlockState(pos, ((ItemSeeds)(itemStack.getItem())).getPlant(world, pos));
		itemStack.shrink(1);
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemToolZijingChu.skill1", new Object[] {}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemToolZijingChu.skill2", new Object[] {}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemToolZijingChu.skill3", new Object[] {}));
    }
}
