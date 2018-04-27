package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.gui.GuiQiankunDai;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemQiankunDai extends Item {
	private String keyName = ConstantUtil.MODID + ":invsize";
	
	public static final int QKBagBaseSize = 1;
	public static final int QKBagMaxSize = 54;
	
	public ItemQiankunDai(){
		maxStackSize = 1;
		setUnlocalizedName("itemQiankunDai");
		setRegistryName(ConstantUtil.MODID + ":itemqiankundai");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound bagTag = new NBTTagCompound();
			bagTag.setInteger(keyName, QKBagBaseSize);
			NonNullList<ItemStack> itemList = NonNullList.<ItemStack>withSize(bagTag.getInteger(keyName), ItemStack.EMPTY);
			ItemStackHelper.saveAllItems(bagTag, itemList, true);
			itemStack.setTagCompound(bagTag);
		}
		
		if(itemStack.hasTagCompound() && null != itemStack.getTagCompound() && !world.isRemote){
			player.openGui(ZijingMod.instance, GuiQiankunDai.GUIID, world, (int)player.posX, (int)(player.posY + 1.62D), (int)player.posZ);
		}
		return super.onItemRightClick(world, player, hand);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		NBTTagCompound bagTag = stack.getTagCompound();
		tooltip.add("bagCount:" + (null == bagTag ? 0 : bagTag.getTagList("Items", 10).tagCount()));
	}
	//*****************************************************************************************************
	public int getQKBagSize(ItemStack itemStack) {
		if(itemStack.getItem() instanceof ItemQiankunDai) {
			if(itemStack.hasTagCompound() && null != itemStack.getTagCompound()){
				NBTTagCompound bagTag = itemStack.getTagCompound();
				return bagTag.getInteger(keyName);
			}else {
				return 0;
			}
		}
		return 0;
	}
	
	public boolean modifyBagSize(ItemStack itemStack, int size) {
		if(null != itemStack && itemStack.getItem() instanceof ItemQiankunDai && size > 0) {
			NBTTagCompound bagTag = itemStack.getTagCompound();
			if(itemStack.hasTagCompound() && null != bagTag && bagTag.getInteger(keyName) + size <= QKBagMaxSize){
				bagTag.setInteger(keyName, bagTag.getInteger(keyName) + size);
				NonNullList<ItemStack> itemList = NonNullList.<ItemStack>withSize(bagTag.getInteger(keyName), ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(bagTag, itemList);
				ItemStackHelper.saveAllItems(bagTag, itemList, true);
				return true;
			}
		}
		return false;
	}
}