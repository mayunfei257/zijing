package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.BaseControl;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.main.itf.MagicSource;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZiqi extends Item implements MagicSource{

	public ItemZiqi() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemZiqi");
		setRegistryName(ZijingMod.MODID + ":itemziqi");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack){
		if(itemStack.getItem() == BaseControl.itemZiqi)
			return 16000;
		return 0;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		if(player.isSneaking()) {
			if(!world.isRemote) {
				ItemStack mainHandStack = player.getHeldItemMainhand();
				ItemStack offHandStack = player.getHeldItemOffhand();
				if(!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof MagicSource && !offHandStack.isEmpty() && offHandStack.getItem() instanceof MagicConsumer && offHandStack.hasTagCompound()) {
					NBTTagCompound offHandNBT = offHandStack.getTagCompound();
					int offHandMagic = offHandNBT.getInteger(MagicConsumer.MAGIC_ENERGY_STR);
					int offHandMaxMagic = offHandNBT.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR);
					if(offHandMaxMagic > offHandMagic) {
						int magic = getMagicEnergy() > offHandMaxMagic - offHandMagic ? offHandMaxMagic - offHandMagic : getMagicEnergy();
						offHandNBT.setInteger(MagicConsumer.MAGIC_ENERGY_STR,  offHandMagic + magic);
						offHandStack.setItemDamage(offHandNBT.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR) - offHandNBT.getInteger(MagicConsumer.MAGIC_ENERGY_STR));
						mainHandStack.shrink(1);
						player.sendMessage(new TextComponentString("Magic injection success! + " + magic));
					}else {
						player.sendMessage(new TextComponentString("Magic is full!"));
					}
				}
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
	
	@Override
	public int getMagicEnergy() {
		return ZijingMod.config.getZIQI_MAGIC_ENERGY();
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("Magic energy: " + getMagicEnergy());
	}
}
