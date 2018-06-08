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

public class ItemZilingZhu extends ItemStaff{

	public ItemZilingZhu() {
		super();
		maxStackSize = 1;
		setUnlocalizedName("itemZilingZhu");
		setRegistryName(ConstantUtil.MODID + ":itemzilingzhu");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if(!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			if(!player.isSneaking()) {
				SkillEntityPlayer.levitationSkill(player);
			}else {
				SkillEntityPlayer.removeEffectSkill(player);
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(!worldIn.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			if(!player.isSneaking()) {
				SkillEntityPlayer.teleportUpSkill(player, pos);
			}else{
				SkillEntityPlayer.teleportDownSkill(player, pos);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	public double skill1(EntityShepherdCapability thrower, EntityLivingBase target) {
		SkillEntityShepherd.levitationSkill(thrower, target, 160);
		target.motionY += 0.6D;
		if(null != thrower.getRevengeTarget() && target != thrower.getRevengeTarget()) {
			thrower.setAttackTarget(thrower.getRevengeTarget());
		}else {
			thrower.setAttackTarget(null);
		}
		return 1;
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
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemZilingZhu.skill1", new Object[] {SkillEntity.MagicSkill_Levitation}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemZilingZhu.skill2", new Object[] {SkillEntity.MagicSkill_RemoveEffect}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemZilingZhu.skill3", new Object[] {SkillEntity.MagicSkill_TeleportUp}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemZilingZhu.skill4", new Object[] {SkillEntity.MagicSkill_TeleportDown}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemZilingZhu.skill5", new Object[] {SkillEntity.MagicSkill_ImmuneFallDamage}));
	}
}
