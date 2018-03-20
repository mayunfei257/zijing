package com.zijing.items.staff;

import com.zijing.ZijingTab;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.util.ConstantUtil;

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

public class ItemArrowXukongDan extends Item{

	public ItemArrowXukongDan() {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemArrowXukongDan");
		setRegistryName(ConstantUtil.MODID + ":itemarrowxukongdan");
		setCreativeTab(ZijingTab.zijingTab);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(!world.isRemote && player.inventory.hasItemStack(new ItemStack(this))) {
			EntityArrowXukongDan xukongDan = new EntityArrowXukongDan(world, player);
			xukongDan.shoot(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z, 4.0F, 0);
			world.spawnEntity(xukongDan);
			world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.snowball.throw")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			player.inventory.clearMatchingItems(this, -1, 1, null);
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}
}
