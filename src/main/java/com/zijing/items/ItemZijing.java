package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicSource;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZijing extends ItemFood implements MagicSource{

	public ItemZijing() {
		super(0, 0F, false);
		setAlwaysEdible();
		setMaxStackSize(64);
		setUnlocalizedName("itemZijing");
		setRegistryName(ZijingMod.MODID + ":itemzijing");
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
//						offHandStack.setItemDamage(offHandNBT.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR) - offHandNBT.getInteger(MagicConsumer.MAGIC_ENERGY_STR));
//						offHandNBT.setInteger(MagicConsumer.MAGIC_ENERGY_STR,  offHandMagic + magic);
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
		return ZijingMod.config.getZIQI_MAGIC_ENERGY() * 9;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("Magic energy: " + getMagicEnergy());
	}
}
