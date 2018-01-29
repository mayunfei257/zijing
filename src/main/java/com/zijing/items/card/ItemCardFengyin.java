package com.zijing.items.card;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCardFengyin extends Item{

	public ItemCardFengyin() {
		super();
		maxStackSize = 1;
//		setMaxDamage(ZijingMod.config.getCARD_MAX_USES());
		setUnlocalizedName("itemCardFengyin");
		setRegistryName(ZijingMod.MODID + ":itemcardfengyin");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		return new ActionResult(EnumActionResult.PASS, itemStack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack itemStack = player.getHeldItem(hand);
		if(!world.isRemote && itemStack.hasTagCompound() && null != itemStack.getTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			String entityName = nbt.getString(ZijingMod.MODID + ":entityName");
			NBTTagCompound entityNBT = (NBTTagCompound)nbt.getTag(ZijingMod.MODID + ":entityNBT");
			try {
				Class entityClass = Class.forName(entityName);
				Class[] parameterTypes = {net.minecraft.world.World.class}; 
				Object[] parameters = {world};
				Constructor constructor = entityClass.getConstructor(parameterTypes);
				EntityLivingBase entity = (EntityLivingBase) constructor.newInstance(parameters);
				entity.readFromNBT(entityNBT);
				BlockPos entityPos = pos.offset(facing);
				entity.setLocationAndAngles(entityPos.getX() + 0.5D, entityPos.getY(), entityPos.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
				world.spawnEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			player.inventory.deleteStack(itemStack);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		if(stack.hasTagCompound() && null != stack.getTagCompound()){
			tooltip.add("EntityName : " + stack.getTagCompound().getString(ZijingMod.MODID + ":entityName"));
		}else{
			tooltip.add("NULL!");
		}
    }
}
