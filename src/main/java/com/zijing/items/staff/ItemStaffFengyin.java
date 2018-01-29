package com.zijing.items.staff;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.main.ZijingTab;
import com.zijing.main.itf.MagicEnergyConsumer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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

public class ItemStaffFengyin extends Item implements MagicEnergyConsumer{

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
		if(!itemStack.hasTagCompound() || null == itemStack.getTagCompound()){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger(ZijingMod.MODID + ":magicEnergy", ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			nbt.setInteger(ZijingMod.MODID + ":maxMagicEnergy", ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
			itemStack.setTagCompound(nbt);
		}
		if (!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			if(nbt.getInteger(ZijingMod.MODID + ":magicEnergy") >= 3) {
				if(player.isSneaking()) {
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					nbt.setInteger(ZijingMod.MODID + ":magicEnergy", nbt.getInteger(ZijingMod.MODID + ":magicEnergy") - 3);
					itemStack.setItemDamage(nbt.getInteger(ZijingMod.MODID + ":maxMagicEnergy") - nbt.getInteger(ZijingMod.MODID + ":magicEnergy"));
				}else {
					EntityArrowFengyinDan fengyinDan = new EntityArrowFengyinDan(world, player);
					fengyinDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
					world.spawnEntity(fengyinDan);
					world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(("entity.snowball.throw"))), SoundCategory.NEUTRAL, 1.0F, 1.0F);
					nbt.setInteger(ZijingMod.MODID + ":magicEnergy", nbt.getInteger(ZijingMod.MODID + ":magicEnergy") - 1);
					itemStack.setItemDamage(nbt.getInteger(ZijingMod.MODID + ":maxMagicEnergy") - nbt.getInteger(ZijingMod.MODID + ":magicEnergy"));
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
			tooltip.add("Magic energy: " + nbt.getInteger(ZijingMod.MODID + ":magicEnergy") + "/" + nbt.getInteger(ZijingMod.MODID + ":maxMagicEnergy"));
		}else{
			tooltip.add("TagCompound Data Error!");
		}
	}
}