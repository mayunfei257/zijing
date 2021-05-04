package com.zijing.entity;

import org.jline.utils.Log;

import com.zijing.util.ConstantUtil;
import com.zijing.util.SkillEntity;
import com.zijing.util.StringUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityTiandaoGaiwu extends TileEntity implements ITickable, ISidedInventory {
	private static final int[] SLOTS_TOP = new int[] {1};
	private static final int[] SLOTS_SIDES = new int[] {0};
	private static final int[] SLOTS_BOTTOM = new int[] {2};
	
	/** The ItemStacks that hold the items currently being used in the furnace */
	private NonNullList<ItemStack> itemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	private String customName;
	private String inputNbtStr;
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory(){
		return this.itemStacks.size();
	}

	public boolean isEmpty(){
		for (ItemStack itemstack : this.itemStacks){
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
		return this.itemStacks.get(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int index, int count){
		return ItemStackHelper.getAndSplit(this.itemStacks, index, count);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int index){
		return ItemStackHelper.getAndRemove(this.itemStacks, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int index, ItemStack stack){
		ItemStack itemstack = this.itemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.itemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()){
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	public String getName(){
		return this.hasCustomName() ? this.customName : "tiandaogaiwu";
	}

	/**
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName(){
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomInventoryName(String p_145951_1_){
		this.customName = p_145951_1_;
	}

	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		this.itemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.itemStacks);

		if (compound.hasKey("CustomName", 8)){
			this.customName = compound.getString("CustomName");
		}
		if (compound.hasKey("InputNbtStr", 8)){
			this.inputNbtStr = compound.getString("InputNbtStr");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.itemStacks);

		if (this.hasCustomName()){
			compound.setString("CustomName", this.customName);
		}
		if (null != this.inputNbtStr){
			compound.setString("InputNbtStr", this.inputNbtStr);
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
		return index == 1;
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
		return direction == EnumFacing.DOWN && index == 2;
	}

	public int getField(int id){
		switch (id){
		default:
			return 0;
		}
	}

	public void setField(int id, int value){
	}

	public int getFieldCount(){
		return 0;
	}

	public void clear(){
		this.itemStacks.clear();
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
    
    public String getInputNbtStr() {
    	return this.inputNbtStr;
    }
    public void setInputNbtStr(String inputNbtStr) {
    	this.inputNbtStr = inputNbtStr;
    }
    
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
  	public void execute(EntityPlayerMP player, String operationType, String nbtStr) {
  		final World world = player.getEntityWorld();
  		if(!world.isRemote) {
  			if(!itemStacks.get(0).isEmpty() && !itemStacks.get(1).isEmpty() && itemStacks.get(2).isEmpty()) {
  				if ("CleanNBT".equals(operationType)) {
						
		  				ItemStack itemStack = itemStacks.get(1);
		  				if(itemStack.hasTagCompound()) {
							System.out.println("nbtStr=" + itemStack.getTagCompound().toString());
		  					itemStack.setTagCompound(new NBTTagCompound());

	  			  			itemStacks.get(0).shrink(1);
	  			  			itemStacks.set(2, itemStack);
	  			  			itemStacks.set(1, ItemStack.EMPTY);
	  			  			world.playSound(null, this.pos, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.use")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		  				}else {
  							player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".message.blockTiandaoGaiwu.error3", new Object[] {})));
		  				}
  				} else if ("AddNBT".equals(operationType)) {
  	  				if(null != nbtStr && !"".equals(nbtStr.trim())) {
  	  					try {
  	  		  				ItemStack itemStack = itemStacks.get(1);
  	  		  				NBTTagCompound nbt = itemStack.getTagCompound() == null ? new NBTTagCompound() : itemStack.getTagCompound();
  	  		  				
  	  						nbt.merge(JsonToNBT.getTagFromJson(nbtStr));
  	  						itemStack.setTagCompound(nbt);
  	  		
  	  			  			itemStacks.get(0).shrink(1);
  	  			  			itemStacks.set(2, itemStack);
  	  			  			itemStacks.set(1, ItemStack.EMPTY);
  	  			  			world.playSound(null, this.pos, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.use")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
  	  					} catch (NBTException e) {
  	  						Log.error(e);
  	  						player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".message.blockTiandaoGaiwu.error2", new Object[] {e.getMessage()})));
  	  					}
  	  				} else {
  							player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".message.blockTiandaoGaiwu.error1", new Object[] {})));
  	  				}
  				}
  			}
  		}
  	}
  	
}