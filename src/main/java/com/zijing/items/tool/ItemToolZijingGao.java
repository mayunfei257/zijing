package com.zijing.items.tool;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolZijingGao extends ItemPickaxe{

	public ItemToolZijingGao() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingGao");
		setRegistryName(ZijingMod.MODID + ":itemtoolzijinggao");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
		if (entity.isSneaking() && "pickaxe".equals(state.getBlock().getHarvestTool(state))){
			int damage = itemstack.getMaxDamage() - itemstack.getItemDamage();
			int maxAmount = damage > ZijingMod.config.getTOOL_DMAMOUNT()? ZijingMod.config.getTOOL_DMAMOUNT() : damage;
			int amount = dropBlock(world, state, pos, 0, maxAmount);
			itemstack.damageItem(amount/2 >= 1 ? amount/2 : 1, entity);
		}else{
			itemstack.damageItem(1, entity);
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
					}else if(Blocks.LIT_REDSTONE_ORE == state.getBlock() && thisState.getBlock() == Blocks.REDSTONE_ORE) {
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
}
