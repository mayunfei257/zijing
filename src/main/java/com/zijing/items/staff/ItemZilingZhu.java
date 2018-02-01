package com.zijing.items.staff;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.util.PlayerUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger(MagicConsumer.MAGIC_ENERGY_STR, ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			nbt.setInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR, ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			itemStack.setTagCompound(nbt);
		}
		if(!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()) {
			if(itemStack.getTagCompound().getInteger(MagicConsumer.MAGIC_ENERGY_STR) >= 1) {
				if(player.isSneaking()) {
					Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
					for(PotionEffect potionEffect:potionEffects) {
						player.removePotionEffect(potionEffect.getPotion());
					}
					PlayerUtil.minusMagic(itemStack, 1);
				}else {
					player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 0));
					PlayerUtil.minusMagic(itemStack, 1);
				}
			}else {
				player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 1!"));
			}
			return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
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
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger(MagicConsumer.MAGIC_ENERGY_STR, ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			nbt.setInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR, ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			itemStack.setTagCompound(nbt);
		}
		if(!worldIn.isRemote && Blocks.AIR != worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() && itemStack.hasTagCompound() && null != itemStack.getTagCompound()) {
			if(itemStack.getTagCompound().getInteger(MagicConsumer.MAGIC_ENERGY_STR) >= 2) {
				if(player.isSneaking()) {
					for(; y > 0; y--) {
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.BEDROCK) {
							break;
						}
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != Blocks.AIR) {
							if(worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.LAVA || worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.FLOWING_LAVA) {
								worldIn.setBlockState(new BlockPos(x, y - 1, z), Blocks.STONE.getDefaultState());
							}
							player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
							PlayerUtil.minusMagic(itemStack, 2);
							break;
						}
					}
				}else{
					for(; y <= 513; y++) {
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.BEDROCK) {
							break;
						}
						if(worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR && worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != Blocks.AIR) {
							if(worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.LAVA || worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.FLOWING_LAVA) {
								worldIn.setBlockState(new BlockPos(x, y - 1, z), Blocks.STONE.getDefaultState());
							}
							player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
							PlayerUtil.minusMagic(itemStack, 2);
							break;
						}
					}
				}
			}else {
				player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 2!"));
			}
			return EnumActionResult.SUCCESS;
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
		if(stack.hasTagCompound() && null != stack.getTagCompound()){
			NBTTagCompound nbt = stack.getTagCompound();
			tooltip.add("Magic energy: " + nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR) + "/" + nbt.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR));
		}else{
			tooltip.add("TagCompound Data Error!");
		}
	}
}
