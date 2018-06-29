package com.zijing.items.staff;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.itf.EntityShepherdCapability;
import com.zijing.itf.ItemStaff;
import com.zijing.util.ConstantUtil;
import com.zijing.util.SkillEntity;
import com.zijing.util.SkillEntityPlayer;
import com.zijing.util.SkillEntityShepherd;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffBingxue extends ItemStaff{

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
				SkillEntityPlayer.thousandsFrozenSkill(player);
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		return EnumActionResult.PASS;
	}

	@Override
	public double skill1(EntityShepherdCapability thrower, EntityLivingBase target) {
		return SkillEntityShepherd.shootBingDanSkill(thrower, target).getAttackDamage();
	}

	@Override
	public double skill2(EntityShepherdCapability thrower, EntityLivingBase target) {
		return 0;
	}

	@Override
	public double skill3(EntityShepherdCapability thrower, EntityLivingBase target) {
		return 0;
	}

	@Override
	public double skill4(EntityShepherdCapability thrower, EntityLivingBase target) {
		return 0;
	}

	@Override
	public double skill5(EntityShepherdCapability thrower, EntityLivingBase target) {
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffBingxue.skill1", new Object[] {SkillEntity.MagicSkill_BingDan}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffBingxue.skill2", new Object[] {SkillEntity.MagicSkill_ThousandsFrozen}));
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