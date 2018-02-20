package com.zijing.items.tool;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingChu extends ItemHoe{

	public ItemToolZijingChu() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingChu");
		setRegistryName(ZijingMod.MODID + ":itemtoolzijingchu");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		if(!world.isRemote && player.isSneaking() && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(shepherdCapability.getMagic() >= 1) {
				Random random = new Random();
				IBlockState iBlockState = world.getBlockState(pos);
				iBlockState.getBlock().updateTick(world, pos, iBlockState, random);
				world.spawnParticle(EnumParticleTypes.HEART, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat()/2.0D + 0.5D, pos.getZ() + random.nextFloat(), 0.0D, 0.5D, 0.0D);
				shepherdCapability.setMagic(shepherdCapability.getMagic() - 1.0D);
				ShepherdProvider.updateChangeToClient(player);
			}else {
				player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 1!"));
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add("Skill 1: Make the block updateTick once. (M : 1)");
    }
}
