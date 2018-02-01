package com.zijing.items.card;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.gui.GuiCardChuansong;
import com.zijing.util.PlayerUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCardChuansong extends Item{
	public static final int foodConsume = 5;
	public static final String BIND_LX = ZijingMod.MODID + ":lx";
	public static final String BIND_LY = ZijingMod.MODID + ":ly";
	public static final String BIND_LZ = ZijingMod.MODID + ":lz";
	public static final String BIND_WORLD = ZijingMod.MODID + ":world";
	public static final String BIND_NAME = ZijingMod.MODID + ":name";
	public static final String IS_BIND = ZijingMod.MODID + ":isbind";

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
			nbt.setDouble(BIND_LX, 0);
			nbt.setDouble(BIND_LY, 0);
			nbt.setDouble(BIND_LZ, 0);
			nbt.setInteger(BIND_WORLD, -999);
			nbt.setString(BIND_NAME, "NULL");
			nbt.setBoolean(IS_BIND, false);
			itemStack.setTagCompound(nbt);
		}
		if(!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()){
			NBTTagCompound chuansongCardTag = itemStack.getTagCompound();
			if(player.isSneaking()){
				if(PlayerUtil.getAllFoodlevel(player) >= foodConsume) {
					player.openGui(ZijingMod.instance, GuiCardChuansong.GUIID, world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
				}else {
					player.sendMessage(new TextComponentString("You are hungry!"));
				}
			}else {
				if(player.dimension == chuansongCardTag.getInteger(BIND_WORLD) && chuansongCardTag.getBoolean(IS_BIND) && PlayerUtil.getAllFoodlevel(player) >= foodConsume) {
					double x = chuansongCardTag.getDouble(BIND_LX);
					double y = chuansongCardTag.getDouble(BIND_LY);
					double z = chuansongCardTag.getDouble(BIND_LZ);
					player.setPositionAndUpdate(x, y, z);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					PlayerUtil.minusFoodlevel(player, foodConsume);
				}else if(!chuansongCardTag.getBoolean(IS_BIND)){
					player.sendMessage(new TextComponentString("Not yet bound!"));
				}else if(player.dimension != chuansongCardTag.getInteger(BIND_WORLD)){
					player.sendMessage(new TextComponentString("Not the same world!"));
				}else if(PlayerUtil.getAllFoodlevel(player) < foodConsume){
					player.sendMessage(new TextComponentString("You are hungry!"));
				}
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		if(stack.hasTagCompound() && null != stack.getTagCompound() && stack.getTagCompound().getBoolean(IS_BIND)){
			NBTTagCompound nbt  = stack.getTagCompound();
			tooltip.add("Position X:" + (int)nbt.getDouble(BIND_LX) + " Y:" + (int)nbt.getDouble(BIND_LY) + " Z:" + (int)nbt.getDouble(BIND_LZ) + " World:" + nbt.getInteger(BIND_WORLD));
			tooltip.add("Name:"+ nbt.getString(BIND_NAME));
		}else{
			tooltip.add("Is not binded!");
		}
	}
}
