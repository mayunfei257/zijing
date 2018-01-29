package com.zijing.items.card;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.gui.GuiCardChuansong;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCardChuansong extends Item{
	public static final int foodConsume = 5;
	
	public ItemCardChuansong() {
		super();
		maxStackSize = 1;
		setUnlocalizedName("itemCardChuansong");
		setRegistryName(ZijingMod.MODID + ":itemcardchuansong");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setDouble(ZijingMod.MODID + ":lx", 0);
			nbt.setDouble(ZijingMod.MODID + ":ly", 0);
			nbt.setDouble(ZijingMod.MODID + ":lz", 0);
			nbt.setInteger(ZijingMod.MODID + ":world", -999);
			nbt.setString(ZijingMod.MODID + ":name", "NULL");
			nbt.setBoolean(ZijingMod.MODID + ":isbind", false);
			itemStack.setTagCompound(nbt);
		}
		if(!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()){
			NBTTagCompound chuansongCardTag = itemStack.getTagCompound();
			FoodStats playerFoodStats = player.getFoodStats();
			if(player.isSneaking()){
				if(playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel() >= foodConsume) {
					player.openGui(ZijingMod.instance, GuiCardChuansong.GUIID, world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
				}else {
					player.sendMessage(new TextComponentString("You are hungry!"));
				}
			}else {
				if(player.dimension == chuansongCardTag.getInteger(ZijingMod.MODID + ":world") && chuansongCardTag.getBoolean(ZijingMod.MODID + ":isbind") && playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel() >= foodConsume) {
					double x = chuansongCardTag.getDouble(ZijingMod.MODID + ":lx");
					double y = chuansongCardTag.getDouble(ZijingMod.MODID + ":ly");
					double z = chuansongCardTag.getDouble(ZijingMod.MODID + ":lz");
					player.setPositionAndUpdate(x, y, z);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					if(playerFoodStats.getSaturationLevel() >= foodConsume) {
						playerFoodStats.setFoodSaturationLevel(playerFoodStats.getSaturationLevel() - foodConsume);
					}else {
						playerFoodStats.setFoodLevel(playerFoodStats.getFoodLevel() - foodConsume + (int)playerFoodStats.getSaturationLevel());
						playerFoodStats.setFoodSaturationLevel(0);
					}
				}else if(!chuansongCardTag.getBoolean(ZijingMod.MODID + ":isbind")){
					player.sendMessage(new TextComponentString("Not yet bound!"));
				}else if(player.dimension != chuansongCardTag.getInteger(ZijingMod.MODID + ":world")){
					player.sendMessage(new TextComponentString("Not the same world!"));
				}else if(playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel() < foodConsume){
					player.sendMessage(new TextComponentString("You are hungry!"));
				}
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		if(stack.hasTagCompound() && null != stack.getTagCompound() && stack.getTagCompound().getBoolean(ZijingMod.MODID + ":isbind")){
			NBTTagCompound nbt  = stack.getTagCompound();
			tooltip.add("Position X:" + (int)nbt.getDouble(ZijingMod.MODID + ":lx") + " Y:" + (int)nbt.getDouble(ZijingMod.MODID + ":ly") + " Z:" + (int)nbt.getDouble(ZijingMod.MODID + ":lz") + " World:" + nbt.getInteger(ZijingMod.MODID + ":world"));
			tooltip.add("Name:"+ nbt.getString(ZijingMod.MODID + ":name"));
		}else{
			tooltip.add("Is not binded!");
		}
	}
}
