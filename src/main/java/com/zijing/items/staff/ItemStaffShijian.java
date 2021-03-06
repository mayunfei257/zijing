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

public class ItemStaffShijian extends ItemStaff{

	public ItemStaffShijian() {
		super();
		maxStackSize = 1;
		setUnlocalizedName("itemStaffShijian");
		setRegistryName(ConstantUtil.MODID + ":itemstaffshijian");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if (!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			if(!player.isSneaking()) {
				SkillEntityPlayer.shootFengyinDanSkill(player);
			}else {
				SkillEntityPlayer.growAreaBlockSkill(player, player.getPosition());
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(!worldIn.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			if(!player.isSneaking()) {
				
			}else{
				SkillEntityPlayer.growBlockSkill(player, pos);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	public double skill1(EntityShepherdCapability thrower, EntityLivingBase target) {
		return SkillEntityShepherd.shootFengyinDanSkill(thrower, target).getAttackDamage();
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
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffShijian.skill1", new Object[] {SkillEntity.MagicSkill_FengyinDan}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffShijian.skill2", new Object[] {SkillEntity.MagicSkill_GrowAreaBlock}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffShijian.skill3", new Object[] {SkillEntity.MagicSkill_GrowBlock}));
	}

//	entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
//	entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
//	entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
//	entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
//	entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
}