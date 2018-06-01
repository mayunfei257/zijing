package com.zijing.items.staff;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.util.ConstantUtil;
import com.zijing.util.SkillEntity;
import com.zijing.util.SkillEntityPlayer;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffBingxue extends Item{

	public ItemStaffBingxue() {
		super();
		maxStackSize = 1;
		setUnlocalizedName("itemStaffBingxue");
		setRegistryName(ConstantUtil.MODID + ":itemstaffbingxue");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if (!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			if(!player.isSneaking()) {
				SkillEntityPlayer.shootBingDanSkill(player);
			}else {
//				world.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffBingxue.skill1", new Object[] {SkillEntity.MagicSkill_BingDan}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffBingxue.skill2", new Object[] {0}));
	}
	//	
	//	@Override
	//	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
	//		return 72000;
	//	}
	//	
	//	@Override
	//	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
	//		return EnumAction.BOW;
	//	}
	//
	//	@Override
	//    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving){
	//        return stack;
	//    }
}