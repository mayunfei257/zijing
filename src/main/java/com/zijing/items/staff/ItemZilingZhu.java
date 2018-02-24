package com.zijing.items.staff;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZilingZhu extends Item implements MagicConsumer{
	public static final int MagicSkill1 = 1;
	public static final int MagicSkill2 = 2;
	public static final int MagicSkill3 = 2;
	public static final int MagicSkill4 = 2;
	public static final int MagicSkill5 = 1;

	public ItemZilingZhu() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemZilingZhu");
		setRegistryName(ZijingMod.MODID + ":itemzilingzhu");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if(!world.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(player.isSneaking()) {
				if(shepherdCapability.getMagic() >= MagicSkill2) {
					Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
					for(PotionEffect potionEffect:potionEffects) {
						player.removePotionEffect(potionEffect.getPotion());
					}
					shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill2);
					ShepherdProvider.updateChangeToClient(player);
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill2 + " !"));
				}
			}else {
				if(shepherdCapability.getMagic() >= MagicSkill1) {
					player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 80, 0));
					shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill1);
					ShepherdProvider.updateChangeToClient(player);
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill1 + " !"));
				}
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(!worldIn.isRemote && ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			if(player.isSneaking()) {
				if(shepherdCapability.getMagic() >= MagicSkill4) {
					for(; y > 0; y--) {
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.BEDROCK) { break; }
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != Blocks.AIR) {
							if(worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.LAVA || worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.FLOWING_LAVA) {
								worldIn.setBlockState(new BlockPos(x, y - 1, z), Blocks.STONE.getDefaultState());
							}
							player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
							shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill4);
							ShepherdProvider.updateChangeToClient(player);
							break;
						}
					}
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill4 + " !"));
				}
			}else{
				if(shepherdCapability.getMagic() >= MagicSkill3) {
					for(; y <= 513; y++) {
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.BEDROCK) { break; }
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != Blocks.AIR) {
							if(worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.LAVA || worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.FLOWING_LAVA) {
								worldIn.setBlockState(new BlockPos(x, y - 1, z), Blocks.STONE.getDefaultState());
							}
							player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
							shepherdCapability.setMagic(shepherdCapability.getMagic() - MagicSkill3);
							ShepherdProvider.updateChangeToClient(player);
							break;
						}
					}
				}else {
					player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + MagicSkill3 + " !"));
				}
			}
		}
		return EnumActionResult.SUCCESS;
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
		tooltip.add("Skill 1: Levitation. (M : " + MagicSkill1 + ")");
		tooltip.add("Skill 2: Remove all PotionEffect. (M : " + MagicSkill2 + ")(Sneaking)");
		tooltip.add("Skill 3: Send up. (M : " + MagicSkill3 + ")");
		tooltip.add("Skill 4: Send down. (M : " + MagicSkill4 + ")(Sneaking)");
		tooltip.add("Skill 5: Immune drop injury. (M : " + MagicSkill5 + ")");
	}
}
