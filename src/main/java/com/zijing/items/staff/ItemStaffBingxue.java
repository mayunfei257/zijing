package com.zijing.items.staff;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.entity.EntityArrowBingDan;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicConsumer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.monster.EntitySnowman;
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

public class ItemStaffBingxue extends Item implements MagicConsumer{

	public ItemStaffBingxue() {
		super();
		maxStackSize = 1;
		setMaxDamage(ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		setUnlocalizedName("itemStaffBingxue");
		setRegistryName(ZijingMod.MODID + ":itemstaffbingxue");
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
			NBTTagCompound nbt = itemStack.getTagCompound();
			if(nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR) >= 3) {
				if(player.isSneaking()) {
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
					EntitySnowman snowman = new EntitySnowman(world);
					BlockPos blockPos = blockPosList.get((int)(Math.random() * (blockPosList.size() - 1)));
					snowman.setLocationAndAngles(blockPos.getX(), blockPos.getY(), blockPos.getZ(), world.rand.nextFloat() * 360F, 0.0F);
					world.spawnEntity(snowman);
					snowman.playLivingSound();
					if(world.rand.nextFloat() < 0.125D) {
						EntitySnowman snowman2 = new EntitySnowman(world);
						BlockPos blockPos2 = blockPosList.get((int)(Math.random() * (blockPosList.size() - 1)));
						snowman2.setLocationAndAngles(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), world.rand.nextFloat() * 360F, 0.0F);
						world.spawnEntity(snowman2);
						snowman2.playLivingSound();
					}
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					nbt.setInteger(MagicConsumer.MAGIC_ENERGY_STR, nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR) - 3);
					itemStack.setItemDamage(nbt.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR) - nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR));
				}else {
					EntityArrowBingDan bingDan = new EntityArrowBingDan(world, player);
					bingDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
					world.spawnEntity(bingDan);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					nbt.setInteger(MagicConsumer.MAGIC_ENERGY_STR, nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR) - 1);
					itemStack.setItemDamage(nbt.getInteger(MagicConsumer.MAX_MAGIC_ENERGY_STR) - nbt.getInteger(MagicConsumer.MAGIC_ENERGY_STR));
				}
			}else {
				player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 3!"));
			}
			return new ActionResult(EnumActionResult.SUCCESS, itemStack);
		}
		return new ActionResult(EnumActionResult.PASS, itemStack);
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