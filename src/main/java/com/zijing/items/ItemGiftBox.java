package com.zijing.items;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.util.ConstantUtil;
import com.zijing.util.SkillEntity;
import com.zijing.util.SkillEntityPlayer;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGiftBox extends Item{
	private int level = 0;

	public ItemGiftBox(int level) {
		super();
		maxStackSize = 64;
		setUnlocalizedName("itemGiftBox" + level);
		setRegistryName(ConstantUtil.MODID + ":itemgiftbox" + level);
		setCreativeTab(ZijingTab.zijingTab);
		this.level = level;
	}
	

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(null == itemStack || ItemStack.EMPTY == itemStack || null == itemStack.getItem()) return super.onItemRightClick(world, player, hand);
		if (!world.isRemote) {
			
			player.addExperience(2000);
			
			Item[] items = {BaseControl.itemDanZiling, BaseControl.itemDanShenshu,
					Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
					Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_HOE,
					Items.BREAD
					};
			int [] itemCount = {16, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16};
			Block[] blocks = {};
			int [] blockCount = {};
			
			// item
			for(int index = 0; index < items.length; index++) {
				ItemStack giftItemStack = new ItemStack(items[index], itemCount[index]);
				if(!player.addItemStackToInventory(giftItemStack)) {
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, giftItemStack));
				}
			}
			// block
			for(int index = 0; index < blocks.length; index++) {
				ItemStack giftItemStack = new ItemStack(blocks[index], blockCount[index]);
				if(!player.addItemStackToInventory(giftItemStack)) {
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, giftItemStack));
				}
			}
			
			itemStack.shrink(1);
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		if(level == 0) {
			tooltip.add(I18n.format(ConstantUtil.MODID + ".itemGiftBox0.message", new Object[] {}));
		} else {
			tooltip.add(I18n.format(ConstantUtil.MODID + ".itemGiftBox.message", new Object[] {level}));
		}
	}
}
