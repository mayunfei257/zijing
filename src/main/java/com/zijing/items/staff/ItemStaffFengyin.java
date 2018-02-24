package com.zijing.items.staff;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityIronGolem;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffFengyin extends Item implements MagicConsumer{
	public static final int MagicSkill1 = 1;
	public static final int MagicSkill2 = 10;

	public ItemStaffFengyin() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemStaffFengyin");
		setRegistryName(ZijingMod.MODID + ":itemstafffengyin");
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
					EntityIronGolem entity = new EntityIronGolem(world);
					entity.setPlayerCreated(true);
					entity.setLocationAndAngles(blockPos.getX(), blockPos.getY(), blockPos.getZ(), world.rand.nextFloat() * 360F, 0.0F);
					if(world.rand.nextFloat() < 0.125D) {
						entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
						entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.7D);
						entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
						entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2.0D);
						entity.tasks.addTask(9, new EntityAISwimming(entity));
					}
					entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 400, 1));
					entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 400, 1));
					entity.playLivingSound();
					world.spawnEntity(entity);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill2);
					ShepherdProvider.updateChangeToClient(player);
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill2 + " !"));
				}
			}else {
				if(shepherdCapability.getMagic() >= MagicSkill1) {
					EntityArrowFengyinDan fengyinDan = new EntityArrowFengyinDan(world, player);
					fengyinDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
					world.spawnEntity(fengyinDan);
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
		tooltip.add("Skill 1: Throw seal ball. (M : " + MagicSkill1 + ")");
		tooltip.add("Skill 2: Summons ironGolem. (M : " + MagicSkill2 + ")(Sneaking)");
	}
}