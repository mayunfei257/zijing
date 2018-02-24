package com.zijing.items.card;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.gui.GuiBookChuansong;
import com.zijing.main.gui.GuiBookChuansongUse;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBookChuansong extends Item{
	public static final int MagicSkill1 = 5;
	public static final int MagicSkill2 = 0;

	public ItemBookChuansong() {
		super();
		maxStackSize = 1;
		setUnlocalizedName("itemBookChuansong");
		setRegistryName(ZijingMod.MODID + ":itembookchuansong");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
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
			}else {
				player.openGui(ZijingMod.instance, GuiBookChuansongUse.GUIID, world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		NBTTagCompound compound = stack.getTagCompound();
		tooltip.add("Skill 1: Open book GUI. (M : " + MagicSkill1 + ")");
		tooltip.add("Skill 2: Transmission. (M : " + MagicSkill2 + ")(Sneaking)");
	}
}
