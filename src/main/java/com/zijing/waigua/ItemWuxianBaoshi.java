package com.zijing.waigua;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.util.ConstantUtil;
import com.zijing.util.EntityUtil;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class ItemWuxianBaoshi extends Item{
	public final static int brunTick = Integer.MAX_VALUE;
	public final static int upExperience = 1000;
	public ItemWuxianBaoshi() {
		super();
		setMaxStackSize(64);
		setUnlocalizedName("itemWuxianBaoshi");
		setRegistryName(ConstantUtil.MODID + ":itemwuxianbaoshi");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, final EntityPlayer player, EnumHand hand){
		if(!world.isRemote) {
			if(!player.isSneaking()) {
				if(player instanceof EntityPlayerMP)
					upLevel((EntityPlayerMP)player, 10);
//				this.compoundItem1ToItem2(world, player, Items.EMERALD, Blocks.EMERALD_BLOCK, 9);
//				this.compoundItem1ToItem2(world, player, BaseControl.itemZiqi, BaseControl.itemZijing, 9);
			}else {
				if(player instanceof EntityPlayerMP)
					upLevel((EntityPlayerMP)player, -10);
			}
		}
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote) {
			if(!player.isSneaking()) {
				
			}else {
				
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack){
		if(itemStack.getItem() == BaseControl.itemWuxianBaoshi)
			return brunTick;
		return 0;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill1", new Object[] {brunTick/20}));
		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill2", new Object[] {upExperience}));
//		tooltip.add(I18n.format(ConstantUtil.MODID + ".itemWuxianBaoshi.skill3", new Object[] {}));
    }
	
	private boolean compoundItem1ToItem2(World world, EntityPlayer player, Object item1, Object item2, int countK) {
		if((item1 instanceof Item || item1 instanceof Block) && (item2 instanceof Item || item2 instanceof Block)) {
			//get all item1 count
			InventoryPlayer inventory = player.inventory;
			int itemCount = 0;
			for (int i = 0; i < inventory.mainInventory.size(); ++i){
				ItemStack itemstack = inventory.mainInventory.get(i);
				if(itemstack.getItem() == item1) {
					itemCount += itemstack.getCount();
					itemstack.setCount(0);
					inventory.mainInventory.set(i, itemstack.EMPTY);
				}
			}
			//add item2
			if(item2 instanceof Item) {
				ItemStack blockStack = new ItemStack((Item)item2, itemCount / countK);
				if(!inventory.addItemStackToInventory(blockStack)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, blockStack));
				}
			}else {
				ItemStack blockStack = new ItemStack((Block)item2, itemCount / countK);
				if(!inventory.addItemStackToInventory(blockStack)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, blockStack));
				}
			}
			//add surplus item1
			if(item1 instanceof Item) {
				ItemStack itemStack = new ItemStack((Item)item1, itemCount % countK);
				if(!inventory.addItemStackToInventory(itemStack)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, itemStack));
				}
			}else {
				ItemStack itemStack = new ItemStack((Block)item1, itemCount % countK);
				if(!inventory.addItemStackToInventory(itemStack)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, itemStack));
				}
			}
			return true;
		}
		return false;
	}
	
	private void upLevel(EntityPlayerMP player, int upLevel) {
    	if(ShepherdProvider.hasCapabilityFromPlayer(player)) {
			ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
			
			int level = shepherdCapability.getLevel() + upLevel;
			if(level <= ZijingMod.config.getMAX_LEVEL() && level >= 0) {
				shepherdCapability.setLevel(level);
				shepherdCapability.setMaxBlood(level * ZijingMod.config.getUPGRADE_MAXBLOOD_K());
				shepherdCapability.setMaxMagic(level * ZijingMod.config.getUPGRADE_MAXMAGIC_K());
				shepherdCapability.setAttack((level - 1) * ZijingMod.config.getUPGRADE_ATTACK_K());
				shepherdCapability.setBloodRestore(level * ZijingMod.config.getUPGRADE_BLOODRESTORE_K() + 0.002D);
				shepherdCapability.setMagicRestore(level * ZijingMod.config.getUPGRADE_MAGICRESTORE_K() + 0.01D);
	        	shepherdCapability.setPhysicalDefense((level - 1) * ZijingMod.config.getUPGRADE_PHYSICALDEFENSE_K());
				player.world.playSound((EntityPlayer) null, player.posX, player.posY + player.getEyeHeight(), player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.end_portal.spawn")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				
				EntityUtil.setPlayerAllValue(player, shepherdCapability);
				ShepherdProvider.updateChangeToClient(player);
			}else {
				player.sendMessage(new TextComponentString("level error : " + level));
			}
    	}
	}
	
}