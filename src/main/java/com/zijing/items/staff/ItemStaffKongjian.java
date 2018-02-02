package com.zijing.items.staff;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicConsumer;
import com.zijing.util.PlayerUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class ItemStaffKongjian extends Item implements MagicConsumer{

	public ItemStaffKongjian() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemStaffKongjian");
		setRegistryName(ZijingMod.MODID + ":itemstaffkongjian");
		setCreativeTab(ZijingTab.zijingTab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger(MagicConsumer.MAGIC_ENERGY_STR, ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			nbt.setInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR, ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			itemStack.setTagCompound(nbt);
		}
		if (!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()) {
			if(itemStack.getTagCompound().getInteger(MagicConsumer.MAGIC_ENERGY_STR) >= 3) {
				if(player.isSneaking()) {
					double y = 1 + Math.random() * 255;
					double x = player.posX + Math.random() * ZijingMod.config.getSTAFF_MAX_DISTANCE() * (Math.random() < 0.5 ? -1 : 1);
					double z = player.posZ + Math.random() * ZijingMod.config.getSTAFF_MAX_DISTANCE() * (Math.random() < 0.5 ? -1 : 1);
					BlockPos baseBlockPos = new BlockPos(x, y, z);
					boolean flag = false;
					if(world.getBlockState(baseBlockPos).getBlock() == Blocks.AIR && world.getBlockState(baseBlockPos.up()).getBlock() == Blocks.AIR && world.getBlockState(baseBlockPos.down()).getBlock() != Blocks.AIR) {//first
						if(world.getBlockState(baseBlockPos.down()).getBlock() == Blocks.LAVA || world.getBlockState(baseBlockPos.down()).getBlock() == Blocks.FLOWING_LAVA) {
							world.setBlockState(baseBlockPos.down(), Blocks.STONE.getDefaultState());
						}
						player.setPositionAndUpdate(baseBlockPos.getX() + 0.5D, baseBlockPos.getY(), baseBlockPos.getZ() + 0.5D);
						flag = true;
					}
					if(!flag){//second
						for(int j = 255; j > 0; j--) {
							for(int i = 3; i >= -3; i--) {
								for(int k = 3; k >= -3; k--) {
									BlockPos blockPos = new BlockPos(baseBlockPos.getX() + i, j, baseBlockPos.getZ() + k);
									if(world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.getBlockState(blockPos.up()).getBlock() == Blocks.AIR && world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR) {
										if(world.getBlockState(blockPos.down()).getBlock() == Blocks.LAVA || world.getBlockState(blockPos.down()).getBlock() == Blocks.FLOWING_LAVA) {
											world.setBlockState(blockPos.down(), Blocks.STONE.getDefaultState());
										}
										player.setPositionAndUpdate(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
										flag = true;
										break;
									}
								}
							}
						}
					}
					if(!flag) {//third
						for(int j = 2; j >= -1; j--) {
							for(int i = 1; i >= -1; i--) {
								for(int k = 1; k >= -1; k--) {
									BlockPos blockPos = new BlockPos(baseBlockPos.getX() + i, baseBlockPos.getY() + j, baseBlockPos.getZ() + k);
									if(world.getBlockState(blockPos).getBlock() == Blocks.LAVA || world.getBlockState(blockPos).getBlock() == Blocks.FLOWING_LAVA) {
										world.setBlockState(blockPos, Blocks.NETHER_BRICK_FENCE.getDefaultState());
									}
									if((j == 0 || j == 1)&& i == 0 && k == 0) {
										world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
									}
									if(j == -1 && (world.getBlockState(blockPos).getBlock() == Blocks.AIR || world.getBlockState(blockPos).getBlock() == Blocks.LAVA || world.getBlockState(blockPos).getBlock() == Blocks.FLOWING_LAVA)) {
										world.setBlockState(blockPos, Blocks.STONE.getDefaultState());
									}
								}
							}
						}
						player.setPositionAndUpdate(baseBlockPos.getX() + 0.5D, baseBlockPos.getY(), baseBlockPos.getZ() + 0.5D);
					}
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					PlayerUtil.minusMagic(itemStack, 3);
				}else {
					EntityArrowXukongDan xukongDan = new EntityArrowXukongDan(world, player);
					xukongDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
					world.spawnEntity(xukongDan);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					PlayerUtil.minusMagic(itemStack, 1);
				}
			}else {
				player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 3!"));
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
		if(stack.hasTagCompound() && null != stack.getTagCompound()){
			NBTTagCompound nbt = stack.getTagCompound();
			tooltip.add("Magic energy: " + nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR) + "/" + nbt.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR));
		}else{
			tooltip.add("TagCompound Data Error!");
		}
	}
}