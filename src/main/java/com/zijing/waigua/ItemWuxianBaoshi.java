package com.zijing.waigua;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWuxianBaoshi extends Item{
	public final static int brunTick = Integer.MAX_VALUE;
	public final static int upExperience = 1000;
	public ItemWuxianBaoshi() {
		super();
		setMaxStackSize(64);
		setUnlocalizedName("itemWuxianBaoshi");
		setRegistryName(ConstantUtil.MODID + ":itemwuxianbaoshi");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		if(!world.isRemote) {
			if(!player.isSneaking()) {
				InventoryPlayer inventory = player.inventory;
				int emeraldCount = 0;
				for (int i = 0; i < inventory.mainInventory.size(); ++i){
					ItemStack itemstack = inventory.mainInventory.get(i);
					if(itemstack.getItem() == Items.EMERALD) {
						emeraldCount += itemstack.getCount();
						itemstack.setCount(0);
						inventory.mainInventory.set(i, itemstack.EMPTY);
					}
				}
				ItemStack emeraldBlockItemStack = new ItemStack(Blocks.EMERALD_BLOCK, emeraldCount / 9);
				if(!inventory.addItemStackToInventory(emeraldBlockItemStack)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, emeraldBlockItemStack));
				}
				ItemStack emeraldItemStack = new ItemStack(Items.EMERALD, emeraldCount % 9);
				if(!inventory.addItemStackToInventory(emeraldItemStack)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, emeraldItemStack));
				}
			}else {
			}
		}
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote) {
			if(!player.isSneaking()) {
			}else {
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack){
		if(itemStack.getItem() == BaseControl.itemWuxianBaoshi)
			return brunTick;
		return 0;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill1", new Object[] {brunTick/20}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill2", new Object[] {upExperience}));
    }
}