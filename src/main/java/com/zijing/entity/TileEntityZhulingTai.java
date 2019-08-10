package com.zijing.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityZhulingTai extends TileEntity implements ITickable, ISidedInventory
{
	private static final int[] SLOTS_TOP = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
	private static final int[] SLOTS_BOTTOM = new int[] {10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] SLOTS_SIDES = new int[] {0};
	/** The ItemStacks that hold the items currently being used in the furnace */
	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(19, ItemStack.EMPTY);
	private String furnaceCustomName;

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory(){
		return this.furnaceItemStacks.size();
	}

	public boolean isEmpty(){
		for (ItemStack itemstack : this.furnaceItemStacks){
			if (!itemstack.isEmpty()){
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int index){
		return this.furnaceItemStacks.get(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int index, int count){
		return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int index){
		return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int index, ItemStack stack){
		ItemStack itemstack = this.furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.furnaceItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()){
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	public String getName(){
		return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
	}

	/**
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName(){
		return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
	}

	public void setCustomInventoryName(String p_145951_1_){
		this.furnaceCustomName = p_145951_1_;
	}

	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);

		if (compound.hasKey("CustomName", 8)){
			this.furnaceCustomName = compound.getString("CustomName");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

		if (this.hasCustomName()){
			compound.setString("CustomName", this.furnaceCustomName);
		}
		return compound;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	public int getInventoryStackLimit(){
		return 64;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update(){
		boolean flag1 = false;

		if (flag1){
			this.markDirty();
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean isUsableByPlayer(EntityPlayer player){
		if (this.world.getTileEntity(this.pos) != this){
			return false;
		}else{
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	public void openInventory(EntityPlayer player){
	}

	public void closeInventory(EntityPlayer player){
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack){
		if (1 <= index && index <= 9){
			return true;
		}else if (10 <= index && index <= 18){
			return false;
		}else if(index == 0) {
			return false;
		}
		return false;
	}

	public int[] getSlotsForFace(EnumFacing side){
		if (side == EnumFacing.DOWN){
			return SLOTS_BOTTOM;
		}else{
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from the given side.
	 */
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction){
		return this.isItemValidForSlot(index, itemStackIn);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 */
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction){
		if (direction == EnumFacing.DOWN && index == 1){
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET){
				return false;
			}
		}
		return true;
	}

	public int getField(int id){
		switch (id){
//		case 0:
//			return this.furnaceBurnTime;
//		case 1:
//			return this.currentItemBurnTime;
//		case 2:
//			return this.cookTime;
//		case 3:
//			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public void setField(int id, int value){
//		switch (id){
//		case 0:
//			this.furnaceBurnTime = value;
//			break;
//		case 1:
//			this.currentItemBurnTime = value;
//			break;
//		case 2:
//			this.cookTime = value;
//			break;
//		case 3:
//			this.totalCookTime = value;
//		}
	}

	public int getFieldCount(){
		return 0;
	}

	public void clear(){
		this.furnaceItemStacks.clear();
	}

	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

	@Override
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing){
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing == EnumFacing.DOWN)
				return (T) handlerBottom;
			else if (facing == EnumFacing.UP)
				return (T) handlerTop;
			else
				return (T) handlerSide;
		return super.getCapability(capability, facing);
	}

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing){
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
    
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
  	public void execute(EntityPlayerMP player) {
  		final World world = player.getEntityWorld();
  		if(!world.isRemote) {
  			if(!isItemStackEmpty(furnaceItemStacks.get(0))) {
				if(executeFuZhiCrystal2(player)) {
					world.playSound(null, this.pos, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.use")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
				}
//  				//Crystal
//  				if(itemCrystal.getItem() == BaseControl.itemHunDunCrystal) {
//  					if(executeHunDunCrystal(player))
//  						world.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "block.end_portal.spawn", 1.0F, 1.0F);
//  				}else if(itemCrystal.getItem() == BaseControl.itemFuZhiCrystal) {
//  					if(executeFuZhiCrystal(player))
//  						world.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "block.end_portal.spawn", 1.0F, 1.0F);
//  				}else if(itemCrystal.getItem() == BaseControl.itemHuiMieCrystal) {
//  					if(executeHuiMieCrystal(player))
//  						world.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.anvil_land", 1.0F, 1.0F);
//  				}else if(itemCrystal.getItem() == BaseControl.itemShengMingCrystal) {
//  					if(executeShengMingCrystal(player))
//  						world.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.anvil_land", 1.0F, 1.0F);
//  				}
//  				//Set Empty
//  				for(int index = 0; index < furnaceItemStacks.size(); index++) {
//  					if(isItemStackEmpty(furnaceItemStacks.get(index)))
//  						furnaceItemStacks.set(index, ItemStack.EMPTY);
//  				}
  			}else {
//  				ContainerWorkbench
  			}
  		}
  	}
  	
//  	private boolean executeHunDunCrystal(EntityPlayerMP player) {//HunDun
//  		final World world = player.getEntityWorld();
//  		int hunDunCrystalNum = hunDunTableItemStacks[0].stackSize;
//  		
//  		int needStackNum = (hunDunCrystalNum / 64) * 3 + (hunDunCrystalNum % 64) > 0 ? 3 : 0;
//  		if(emptyStackNumOfOutputStack() >= needStackNum) {
//  			int remaining1 = putItemIntoOutputStack(BaseControl.itemHuiMieCrystal, hunDunCrystalNum);
//  			if(remaining1 > 0) {
//  				EntityItem entityitem1 = new EntityItem(world, this.xCoord, this.yCoord, this.zCoord, new ItemStack(BaseControl.itemHuiMieCrystal, remaining1));
//  				world.spawnEntityInWorld(entityitem1);
//  			}
//  			int remaining2 = putItemIntoOutputStack(BaseControl.itemShengMingCrystal, hunDunCrystalNum);
//  			if(remaining2 > 0) {
//  				EntityItem entityitem2 = new EntityItem(world, this.xCoord, this.yCoord, this.zCoord, new ItemStack(BaseControl.itemShengMingCrystal, remaining1));
//  				world.spawnEntityInWorld(entityitem2);
//  			}
//  			int remaining3 = putItemIntoOutputStack(BaseControl.itemFuZhiCrystal, hunDunCrystalNum);
//  			if(remaining3 > 0) {
//  				EntityItem entityitem3 = new EntityItem(world, this.xCoord, this.yCoord, this.zCoord, new ItemStack(BaseControl.itemFuZhiCrystal, remaining1));
//  				world.spawnEntityInWorld(entityitem3);
//  			}
//  			hunDunTableItemStacks[0].stackSize = 0;
//  			return true;
//  		}else {
//  			player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.hunDunCrystal1", new Object[] {})));
//  		}
//  		
//  		return false;
//  	}
  	
//  	private boolean executeFuZhiCrystal(EntityPlayerMP player) {//FuZhi
//  		ItemStack sourceItemStack = null;
//  		for(int index = 1; index < 10; index++) {//The first one
//  			if(!isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				sourceItemStack = hunDunTableItemStacks[index];
//  				break;
//  			}
//  		}
//  		if(null != sourceItemStack){
//  			if(sourceItemStack.getItem() instanceof CrystalItemType || sourceItemStack.getItem() instanceof CrystalBlockType) {
//  				player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.fuZhiCrystal4", new Object[] {})));
//  				return false;
//  			}
//  			int index = getTheEmptyIndexOfOutputStack();
//  			if(isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				hunDunTableItemStacks[index] = sourceItemStack.copy();
//  				hunDunTableItemStacks[0].stackSize -= 1;
//  				player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.fuZhiCrystal1", new Object[] {})));
//  				return true;
//  			}else {
//  				player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.fuZhiCrystal2", new Object[] {})));
//  			}
//  		}else {
//  			player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.fuZhiCrystal3", new Object[] {})));
//  		}
//  		return false;
//  	}

  	private boolean executeFuZhiCrystal2(EntityPlayerMP player) {//FuZhi
  		boolean inputNotEmpty = false;
  		boolean outputEmpty = true;
  		
  		for(int index = 1; index <= 9; index++) {
  			inputNotEmpty = inputNotEmpty || !isItemStackEmpty(furnaceItemStacks.get(index));
  		}
  		for(int index = 10; index <= 18; index++) {
  			outputEmpty = outputEmpty && isItemStackEmpty(furnaceItemStacks.get(index));
  		}

  		if(inputNotEmpty && outputEmpty) {
  			furnaceItemStacks.get(0).shrink(1);
  	  		for(int index = 10; index <= 18; index++) {
  	  			furnaceItemStacks.set(index, isItemStackEmpty(furnaceItemStacks.get(index - 9)) ? ItemStack.EMPTY : furnaceItemStacks.get(index - 9).copy());
  	  		}
  	  		return true;
  		}

  		return false;
  	}
//  	private boolean executeHuiMieCrystal(EntityPlayerMP player) {//HuiMie
//  		ItemStack sourceItemStack = null;
//  		for(int index = 1; index < 10; index++) {//The first tool
//  			ItemStack itemStack = hunDunTableItemStacks[index];
//  			if(!isItemStackEmpty(itemStack) && !(itemStack.getItem() instanceof ItemArmor) && itemStack.getMaxStackSize() == 1) {
//  				sourceItemStack = itemStack;
//  				break;
//  			}
//  		}
//  		if(null != sourceItemStack) {
//  			int index = getTheEmptyIndexOfOutputStack();
//  			if(isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				NBTTagCompound stackTagCompound = sourceItemStack.getTagCompound();
//  				if(null == stackTagCompound){
//  					stackTagCompound = new NBTTagCompound();
//  					stackTagCompound.setString(Config.NBTTAG_TYPE, Config.NBTTAG_TYPE_ATTACK);
//  					stackTagCompound.setInteger(Config.NBTTAG_LEVEL, 0);
//  					stackTagCompound.setFloat(Config.NBTTAG_STRENGTH, 0F);
//  					sourceItemStack.setTagCompound(stackTagCompound);
//  				}
//  				int quantity = (int) ((stackTagCompound.getInteger(Config.NBTTAG_LEVEL) + 1) * FuZhuMod.config.getNbttag_Type_Attack_CK() + FuZhuMod.config.getNbttag_Type_Attack_CB());
//  				if(hunDunTableItemStacks[0].stackSize >= quantity) {
//  					stackTagCompound.setString(Config.NBTTAG_TYPE, Config.NBTTAG_TYPE_ATTACK);
//  					stackTagCompound.setInteger(Config.NBTTAG_LEVEL, stackTagCompound.getInteger(Config.NBTTAG_LEVEL) + 1);
//  					stackTagCompound.setFloat(Config.NBTTAG_STRENGTH, (float) (stackTagCompound.getInteger(Config.NBTTAG_LEVEL) * FuZhuMod.config.getNbttag_Type_Attack_K()));
//  					hunDunTableItemStacks[index] = sourceItemStack.copy();
//  					sourceItemStack.stackSize = 0;
//  					hunDunTableItemStacks[0].stackSize -= quantity;
//  					player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.huiMieCrystal1", new Object[] {stackTagCompound.getInteger(Config.NBTTAG_LEVEL)})));
//  					return true;
//  				}else {
//  					player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.huiMieCrystal2", new Object[] {quantity})));
//  				}
//  			}else {
//  				player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.huiMieCrystal3", new Object[] {})));
//  			}
//  		}else {
//  			player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.huiMieCrystal4", new Object[] {})));
//  		}
//  		return false;
//  	}
  	
//  	private boolean executeShengMingCrystal(EntityPlayerMP player) {//ShengMing
//  		ItemStack sourceItemStack = null;
//  		for(int index = 1; index < 10; index++) {//The first armor
//  			if(!isItemStackEmpty(hunDunTableItemStacks[index]) && hunDunTableItemStacks[index].getItem() instanceof ItemArmor) {
//  				sourceItemStack = hunDunTableItemStacks[index];
//  				break;
//  			}
//  		}
//  		if(null != sourceItemStack) {
//  			int index = getTheEmptyIndexOfOutputStack();
//  			if(isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				NBTTagCompound stackTagCompound = sourceItemStack.getTagCompound();
//  				if(null == stackTagCompound){
//  					stackTagCompound = new NBTTagCompound();
//  					stackTagCompound.setString(Config.NBTTAG_TYPE, Config.NBTTAG_TYPE_DEFENSE);
//  					stackTagCompound.setInteger(Config.NBTTAG_LEVEL, 0);
//  					stackTagCompound.setFloat(Config.NBTTAG_STRENGTH, 0F);
//  					sourceItemStack.setTagCompound(stackTagCompound);
//  				}
//  				int quantity = (int) ((stackTagCompound.getInteger(Config.NBTTAG_LEVEL) + 1) * FuZhuMod.config.getNbttag_Type_Defense_CK() + FuZhuMod.config.getNbttag_Type_Defense_CB());
//  				if(hunDunTableItemStacks[0].stackSize >= quantity) {
//  					stackTagCompound.setString(Config.NBTTAG_TYPE, Config.NBTTAG_TYPE_DEFENSE);
//  					stackTagCompound.setInteger(Config.NBTTAG_LEVEL, stackTagCompound.getInteger(Config.NBTTAG_LEVEL) + 1);
//  					stackTagCompound.setFloat(Config.NBTTAG_STRENGTH, (float) (stackTagCompound.getInteger(Config.NBTTAG_LEVEL) * FuZhuMod.config.getNbttag_Type_Defense_K()));
//  					hunDunTableItemStacks[index] = sourceItemStack.copy();
//  					sourceItemStack.stackSize = 0;
//  					hunDunTableItemStacks[0].stackSize -= quantity;
//  					player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.shengMingCrystal1", new Object[] {stackTagCompound.getInteger(Config.NBTTAG_LEVEL)})));
//  					return true;
//  				}else {
//  					player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.shengMingCrystal2", new Object[] {quantity})));
//  				}
//  			}else {
//  				player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.shengMingCrystal3", new Object[] {})));
//  			}
//  		}else {
//  			player.addChatMessage(new ChatComponentText(I18n.format(Config.MODID + ".tileEntityHunDunTable.shengMingCrystal4", new Object[] {})));
//  		}
//  		return false;
//  	}
  	
  	//------------------------------------------------------------------------------------------------------------------
  	
  	private boolean isItemStackEmpty(ItemStack itemStack) {
  		return null == itemStack || itemStack.isEmpty();
  	}
  	
//  	private int emptyStackNumOfOutputStack() {
//  		int emptyNum = 0;
//  		for(int index = 10; index < furnaceItemStacks.size(); index++) {
//  			if(isItemStackEmpty(furnaceItemStacks[index])) {
//  				emptyNum++;
//  			}
//  		}
//  		return emptyNum;
//  	}
  	
//  	private int getTheEmptyIndexOfOutputStack() {
//  		for(int index = 10; index < hunDunTableItemStacks.length; index++) {
//  			if(isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				return index;
//  			}
//  		}
//  		return hunDunTableItemStacks.length - 1;
//  	}
  	
//  	private boolean hasItemOfInputStack(Item item) {
//  		for(int index = 1; index < 10; index++) {
//  			if(!isItemStackEmpty(hunDunTableItemStacks[index]) && hunDunTableItemStacks[index].getItem() == item && hunDunTableItemStacks[index].stackSize > 0) {
//  				return true;
//  			}
//  		}
//  		return false;
//  	}
  	
//  	private boolean hasEmptyOfOutputStack(Item item) {
//  		for(int index = 10; index < hunDunTableItemStacks.length; index++) {
//  			if(isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				return true;
//  			}else if(hunDunTableItemStacks[index].getItem() == item && hunDunTableItemStacks[index].stackSize < hunDunTableItemStacks[index].getMaxStackSize()){
//  				return true;
//  			}
//  		}
//  		return false;
//  	}
  	
//  	private int removeItemFromInputStack(Item item, int num) {
//  		for(int index = 1; index < 10; index++) {
//  			if(num <= 0) break;
//  			if(!isItemStackEmpty(hunDunTableItemStacks[index]) && hunDunTableItemStacks[index].getItem() == item) {
//  				if(hunDunTableItemStacks[index].stackSize > num) {
//  					hunDunTableItemStacks[index].stackSize -= num;
//  					num = 0;
//  				}else {
//  					num -= hunDunTableItemStacks[index].stackSize;
//  					hunDunTableItemStacks[index].stackSize = 0;
//  				}
//  			}
//  		}
//  		return num;
//  	}
  	
//  	private int putItemIntoOutputStack(Item item, int num) {
//  		for(int index = 10; index < hunDunTableItemStacks.length; index++) {
//  			if(num <= 0) break;
//  			if(isItemStackEmpty(hunDunTableItemStacks[index])) {
//  				if(num > item.getItemStackLimit()) {
//  					num -= item.getItemStackLimit();
//  					hunDunTableItemStacks[index] = new ItemStack(item, item.getItemStackLimit());
//  				}else {
//  					hunDunTableItemStacks[index] = new ItemStack(item, num);
//  					num = 0;
//  				}
//  			}else if(hunDunTableItemStacks[index].getItem() == item && hunDunTableItemStacks[index].stackSize < item.getItemStackLimit()){
//  				if(num > item.getItemStackLimit() - hunDunTableItemStacks[index].stackSize) {
//  					num -= item.getItemStackLimit() - hunDunTableItemStacks[index].stackSize;
//  					hunDunTableItemStacks[index].stackSize = item.getItemStackLimit();
//  				}else {
//  					hunDunTableItemStacks[index].stackSize += num;
//  					num = 0;
//  				}
//  			}
//  		}
//  		return num;
//  	}
}