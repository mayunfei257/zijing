package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.itf.MagicSource;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZiqi extends ItemFood implements MagicSource{

	public ItemZiqi() {
		super(0, 0F, false);
		setAlwaysEdible();
		setMaxStackSize(64);
		setUnlocalizedName("itemZiqi");
		setRegistryName(ConstantUtil.MODID + ":itemziqi");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
		super.onFoodEaten(itemStack, world, player);
		if (!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			shepherdCapability.setMagic(Math.min(shepherdCapability.getMagic() + getMagicEnergy(), shepherdCapability.getMaxMagic()));
			ShepherdProvider.updateChangeToClient(player);
		}
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack){
		if(itemStack.getItem() == BaseControl.itemZiqi)
			return ZijingMod.config.getZIQI_BURN_TICK();
		return 0;
    }
	
//	@Override
//	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
//		if(player.isSneaking()) {
//			if(!world.isRemote) {
//				ItemStack mainHandStack = player.getHeldItemMainhand();
//				ItemStack offHandStack = player.getHeldItemOffhand();
//				if(!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof MagicSource && !offHandStack.isEmpty() && offHandStack.getItem() instanceof MagicConsumer && offHandStack.hasTagCompound()) {
//					NBTTagCompound offHandNBT = offHandStack.getTagCompound();
//					int offHandMagic = offHandNBT.getInteger(MagicConsumer.MAGIC_ENERGY_STR);
//					int offHandMaxMagic = offHandNBT.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR);
//					if(offHandMaxMagic > offHandMagic) {
//						int magic = getMagicEnergy() > offHandMaxMagic - offHandMagic ? offHandMaxMagic - offHandMagic : getMagicEnergy();
//						offHandNBT.setInteger(MagicConsumer.MAGIC_ENERGY_STR,  offHandMagic + magic);
//						offHandStack.setItemDamage(offHandNBT.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR) - offHandNBT.getInteger(MagicConsumer.MAGIC_ENERGY_STR));
//						mainHandStack.shrink(1);
//						player.sendMessage(new TextComponentString("Magic injection success! + " + magic));
//					}else {
//						player.sendMessage(new TextComponentString("Magic is full!"));
//					}
//				}
//			}
//			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
//		}
//		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
//	}
	
	@Override
	public int getMagicEnergy() {
		return ZijingMod.config.getZIQI_MAGIC_ENERGY();
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemZiqi.magic", new Object[] {getMagicEnergy()}));
	}
}
