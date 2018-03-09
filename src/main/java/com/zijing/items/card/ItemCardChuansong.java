package com.zijing.items.card;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.gui.GuiCardChuansong;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

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
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCardChuansong extends Item{
	public static final String BIND_LX = ZijingMod.MODID + ":lx";
	public static final String BIND_LY = ZijingMod.MODID + ":ly";
	public static final String BIND_LZ = ZijingMod.MODID + ":lz";
	public static final String BIND_WORLD = ZijingMod.MODID + ":world";
	public static final String BIND_NAME = ZijingMod.MODID + ":name";
	public static final String IS_BIND = ZijingMod.MODID + ":isbind";
	public static final int MagicSkill1 = 5;
	public static final int MagicSkill2 = 5;

	public ItemCardChuansong() {
		super();
		maxStackSize = 1;
		setUnlocalizedName("itemCardChuansong");
		setRegistryName(ZijingMod.MODID + ":itemcardchuansong");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		player.setActiveHand(hand);
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
		if(!world.isRemote && itemStack.hasTagCompound() && ShepherdProvider.hasCapabilityFromPlayer(player)){
			NBTTagCompound chuansongCardTag = itemStack.getTagCompound();
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(player.isSneaking()){
				if(shepherdCapability.getMagic() >= MagicSkill2) {
					player.openGui(ZijingMod.instance, GuiCardChuansong.GUIID, world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill2 + " !"));
				}
			}else {
				if(player.dimension == chuansongCardTag.getInteger(BIND_WORLD) && chuansongCardTag.getBoolean(IS_BIND) && shepherdCapability.getMagic() >= MagicSkill1) {
					double x = chuansongCardTag.getDouble(BIND_LX);
					double y = chuansongCardTag.getDouble(BIND_LY);
					double z = chuansongCardTag.getDouble(BIND_LZ);
					player.setPositionAndUpdate(x, y, z);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill1);
					ShepherdProvider.updateChangeToClient(player);
				}else if(!chuansongCardTag.getBoolean(IS_BIND)){
					player.sendMessage(new TextComponentString("Not yet bound!"));
				}else if(player.dimension != chuansongCardTag.getInteger(BIND_WORLD)){
					player.sendMessage(new TextComponentString("Not the same world! the world is " + player.dimension + ", this card is " + chuansongCardTag.getInteger(BIND_WORLD)));
				}else if(shepherdCapability.getMagic() < 3){
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill1 + " !"));
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
			tooltip.add(I18n.format(ZijingMod.MODID + ".itemCardChuansong.name", new Object[] {nbt.getString(BIND_NAME)}));
			tooltip.add(I18n.format(ZijingMod.MODID + ".itemCardChuansong.position", new Object[] {(int)nbt.getDouble(BIND_LX), (int)nbt.getDouble(BIND_LY), (int)nbt.getDouble(BIND_LZ), nbt.getInteger(BIND_WORLD)}));
		}else{
			tooltip.add(I18n.format(ZijingMod.MODID + ".itemCardChuansong.notbinded", new Object[] {}));
		}
		tooltip.add(I18n.format(ZijingMod.MODID + ".line.1", new Object[] {}));
		tooltip.add(I18n.format(ZijingMod.MODID + ".itemCardChuansong.skill1", new Object[] {MagicSkill1}));
		tooltip.add(I18n.format(ZijingMod.MODID + ".itemCardChuansong.skill2", new Object[] {MagicSkill2}));
	}
}
