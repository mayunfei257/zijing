package com.zijing.items.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;
import com.zijing.util.SkillEntityPlayer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingChan extends ItemSpade{

	public ItemToolZijingChan() {
		super(ZijingMod.config.getZijingToolMaterial());
		setUnlocalizedName("itemToolZijingChan");
		setRegistryName(ConstantUtil.MODID + ":itemtoolzijingchan");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
		if (!world.isRemote){
			if (entity.isSneaking()){
    			SkillEntityPlayer.chainDropSkill(itemstack, world, state, pos, (EntityPlayer)entity);
			}else if((double)state.getBlockHardness(world, pos) != 0.0D){
				itemstack.damageItem(1, entity);
			}
		}
		return true;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemToolZijingChan.skill1", new Object[] {ZijingMod.config.getTOOL_DMAMOUNT()}));
	}
}
