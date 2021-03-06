package com.zijing.items.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingChan extends ItemSpade{

	public ItemToolZijingChan() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingChan");
		setRegistryName(ConstantUtil.MODID + ":itemtoolzijingchan");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
		if (!world.isRemote){
			if (entity.isSneaking() && "shovel".equals(state.getBlock().getHarvestTool(state))){
				int damage = itemstack.getMaxDamage() - itemstack.getItemDamage();
				int maxAmount = damage > ZijingMod.config.getTOOL_DMAMOUNT()? ZijingMod.config.getTOOL_DMAMOUNT() : damage;
				int amount = dropBlock(world, state, pos, 0, maxAmount);
				itemstack.damageItem(amount/2 >= 1 ? amount/2 : 1, entity);
			}else if((double)state.getBlockHardness(world, pos) != 0.0D){
				itemstack.damageItem(1, entity);
			}
		}
		return true;
	}

	private int dropBlock(World world, IBlockState state, BlockPos pos, int amount, int maxAmount) {
		for(int y = 1; y >= -1; y--) {
			for(int x = 1; x >= -1; x--) {
				for(int z = 1; z >= -1; z--) {
					if(amount >= maxAmount) { return amount; }
					BlockPos thisPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
					IBlockState thisState = world.getBlockState(thisPos);
					if((x == 0 && y == 0 && z == 0) || thisState.getBlock() == Blocks.AIR) {
						continue;
					}else if(thisState.getBlock() == state.getBlock() && thisState.getBlock().getMetaFromState(thisState) == state.getBlock().getMetaFromState(state)) {
						state.getBlock().dropBlockAsItem(world, thisPos, world.getBlockState(thisPos), 1);
						world.setBlockToAir(thisPos);
						amount ++;
						amount = dropBlock(world, state, thisPos, amount, maxAmount);
					}
				}
			}
		}
		return amount;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemToolZijingChan.skill1", new Object[] {ZijingMod.config.getTOOL_DMAMOUNT()}));
	}
}
