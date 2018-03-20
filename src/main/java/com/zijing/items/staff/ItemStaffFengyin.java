package com.zijing.items.staff;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityDisciple;
import com.zijing.itf.MagicConsumer;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffFengyin extends Item implements MagicConsumer{
	public static final int MagicSkill1 = 1;
	public static final int MagicSkill2 = 1000;

	public ItemStaffFengyin() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemStaffFengyin");
		setRegistryName(ConstantUtil.MODID + ":itemstafffengyin");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if (!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(player.isSneaking()) {
				if(shepherdCapability.getMagic() >= MagicSkill2 || player.isCreative()) {
					List<BlockPos> blockPosList = new ArrayList<BlockPos>();
					for(int i = -5; i <= 5; i++) {
						for(int j = -3; j <= 3; j++) {
							for(int k = -5; k <= 5; k++) {
								BlockPos blockPos = new BlockPos(player.posX + i, player.posY + j, player.posZ + k);
								if(world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.getBlockState(blockPos.up()).getBlock() == Blocks.AIR) {
									blockPosList.add(blockPos);
								}
							}
						}
					}
					BlockPos blockPos = blockPosList.get((int)(Math.random() * (blockPosList.size() - 1)));
					EntityDisciple entity = new EntityDisciple(world);
					entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
					entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
					entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
					entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
					entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
					entity.setLocationAndAngles(blockPos.getX(), blockPos.getY(), blockPos.getZ(), world.rand.nextFloat() * 360F, 0.0F);
					entity.setHomePosAndDistance(blockPos, 64);
					entity.updataSwordDamageAndArmorValue();
					world.spawnEntity(entity);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 1D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					if(!player.isCreative()) {
						shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill2);
						ShepherdProvider.updateChangeToClient(player);
					}
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill2 + " !"));
				}
			}else {
				if(shepherdCapability.getMagic() >= MagicSkill1 || player.isCreative()) {
					EntityArrowFengyinDan fengyinDan = new EntityArrowFengyinDan(world, player);
					fengyinDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
					world.spawnEntity(fengyinDan);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 1D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(("entity.snowball.throw"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					if(!player.isCreative()) {
						shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill1);
						ShepherdProvider.updateChangeToClient(player);
					}
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill1 + " !"));
				}
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		return EnumActionResult.PASS;
	}

	@Override
	public int getMaxMagicEnergyValue() {
		return ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY();
	}

	@Override
	public int getMinMagicEnergyValue() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffFengyin.skill1", new Object[] {MagicSkill1}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemStaffFengyin.skill2", new Object[] {MagicSkill2}));
	}
}