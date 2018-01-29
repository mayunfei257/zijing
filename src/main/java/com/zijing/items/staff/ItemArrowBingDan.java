package com.zijing.items.staff;

import com.zijing.ZijingMod;
import com.zijing.entity.EntityArrowBingDan;
import com.zijing.main.ZijingTab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ItemArrowBingDan extends Item{

	public ItemArrowBingDan() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemArrowBingDan");
		setRegistryName(ZijingMod.MODID + ":itemarrowbingdan");
		setCreativeTab(ZijingTab.zijingTab);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(!world.isRemote && player.inventory.hasItemStack(new ItemStack(this))) {
			EntityArrowBingDan bingDan = new EntityArrowBingDan(world, player);
			bingDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
			world.spawnEntity(bingDan);
			world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			player.inventory.clearMatchingItems(this, -1, 1, null);
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}
}
