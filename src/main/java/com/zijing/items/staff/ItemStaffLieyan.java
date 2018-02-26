package com.zijing.items.staff;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffLieyan extends Item implements MagicConsumer{
	public static final int MagicSkill1 = 1;
	public static final int MagicSkill2 = 3;

	public ItemStaffLieyan() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemStaffLieyan");
		setRegistryName(ZijingMod.MODID + ":itemstafflieyan");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if (!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(player.isSneaking()) {
				if(shepherdCapability.getMagic() >= MagicSkill2) {
					if(world.rand.nextFloat() < 0.125D) {
						player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 0));
						for(int i = -5; i <= 5; i++) {
							for(int j = -3; j <= 3; j++) {
								for(int k = -5; k <= 5; k++) {
									BlockPos blockPos = new BlockPos(player.posX + i, player.posY + j, player.posZ + k);
									if(world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR) {
										if(i == 0 && (j == 0 || j == 1) && k == 0) {
											continue;
										}
										world.createExplosion(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1F, true);
									}
								}
							}
						}
					}else {
						player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0));
						for(int i = -5; i <= 5; i++) {
							for(int j = -3; j <= 3; j++) {
								for(int k = -5; k <= 5; k++) {
									BlockPos blockPos = new BlockPos(player.posX + i, player.posY + j, player.posZ + k);
									if(world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR) {
										if(i == 0 && (j == 0 || j == 1) && k == 0) {
											continue;
										}
										world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
									}
								}
							}
						}
					}
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.lightning.impact")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.lightning.thunder")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill2);
					ShepherdProvider.updateChangeToClient(player);
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill2 + " !"));
				}
			}else {
				if(shepherdCapability.getMagic() >= MagicSkill1) {
					EntityArrowHuoDan huoDan = new EntityArrowHuoDan(world, player);
					huoDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
					huoDan.setFire(5);
					world.spawnEntity(huoDan);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(("entity.snowball.throw"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill1);
					ShepherdProvider.updateChangeToClient(player);
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
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemStaffLieyan.skill1", new Object[] {MagicSkill1}));
		tooltip.add(I18n.translateToLocalFormatted(ZijingMod.MODID + ".itemStaffLieyan.skill2", new Object[] {MagicSkill2}));
	}
}