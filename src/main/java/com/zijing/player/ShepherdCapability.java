package com.zijing.player;

import com.zijing.ZijingMod;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ShepherdCapability{
	public static ShepherdStorage storage = new ShepherdCapability.ShepherdStorage();
	public static final String name = ZijingMod.MODID + "shepherd_capability";
	public static final int BLOOD = 20;
	public static final int MAGIC = 0;
	public static final int RACE = 0;
	public static final int LEVEL = 0;
	public static final int MAXBLOOD = 20;
	public static final int MAXMAGIC = 0;
	public static final int SPEED = 0;
	public static final int POWER = 0;
	public static final int INTELLECT = 0;
	public static final int BLOODRESTORE = 0;
	public static final int MAGICRESTORE = 0;
	public static final int PHYSICALDEFENSE = 0;
	public static final int MAGICDEFENSE = 0;
		
	private int race;
	private int level;
	private int maxBlood;
	private int blood;
	private int maxMagic;
	private int magic;
	private int speed;
	private int power;
	private int intellect;
	private int bloodRestore;
	private int magicRestore;
	private int physicalDefense;
	private int magicDefense;

	public ShepherdCapability(){
		blood = ShepherdCapability.BLOOD;
		magic = ShepherdCapability.MAGIC;
		race = ShepherdCapability.RACE;
		level = ShepherdCapability.LEVEL;
		maxBlood = ShepherdCapability.MAXBLOOD;
		maxMagic = ShepherdCapability.MAXMAGIC;
		speed = ShepherdCapability.SPEED;
		power = ShepherdCapability.POWER;
		intellect = ShepherdCapability.INTELLECT;
		bloodRestore = ShepherdCapability.BLOODRESTORE;
		magicRestore = ShepherdCapability.MAGICRESTORE;
		physicalDefense = ShepherdCapability.PHYSICALDEFENSE;
		magicDefense = ShepherdCapability.MAGICDEFENSE;
	}
	
	public void readNBT(EnumFacing side, NBTBase nbt){
		storage.readNBT(ShepherdProvider.SHE_CAP, this, side, nbt);
	}
	
	public NBTBase writeNBT(EnumFacing side){
		return storage.writeNBT(ShepherdProvider.SHE_CAP, this, side);
	}

	public int getRace() {return race;}
	public void setRace(int race) {this.race = race;}
	public int getLevel() {return level;}
	public void setLevel(int level) {this.level = level;}
	public int getMaxBlood() {return maxBlood;}
	public void setMaxBlood(int maxBlood) {this.maxBlood = maxBlood;}
	public int getBlood() {return blood;}
	public void setBlood(int blood) {this.blood = blood;}
	public int getMaxMagic() {return maxMagic;}
	public void setMaxMagic(int maxMagic) {this.maxMagic = maxMagic;}
	public int getMagic() {return magic;}
	public void setMagic(int magic) {this.magic = magic;}
	public int getSpeed() {return speed;}
	public void setSpeed(int speed) {this.speed = speed;}
	public int getPower() {return power;}
	public void setPower(int power) {this.power = power;}
	public int getIntellect() {return intellect;}
	public void setIntellect(int intellect) {this.intellect = intellect;}
	public int getBloodRestore() {return bloodRestore;}
	public void setBloodRestore(int bloodRestore) {this.bloodRestore = bloodRestore;}
	public int getMagicRestore() {return magicRestore;}
	public void setMagicRestore(int magicRestore) {this.magicRestore = magicRestore;}
	public int getPhysicalDefense() {return physicalDefense;}
	public void setPhysicalDefense(int physicalDefense) {this.physicalDefense = physicalDefense;}
	public int getMagicDefense() {return magicDefense;}
	public void setMagicDefense(int magicDefense) {this.magicDefense = magicDefense;}
	
	public static class ShepherdStorage implements Capability.IStorage<ShepherdCapability> {
		@Override
		public void readNBT(Capability<ShepherdCapability> capability, ShepherdCapability instance, EnumFacing side, NBTBase nbtTag) {
			instance.setRace(((NBTTagCompound)nbtTag).getInteger("race"));
			instance.setLevel(((NBTTagCompound)nbtTag).getInteger("level"));
			instance.setMaxBlood(((NBTTagCompound)nbtTag).getInteger("maxBlood"));
			instance.setBlood(((NBTTagCompound)nbtTag).getInteger("blood"));
			instance.setMaxMagic(((NBTTagCompound)nbtTag).getInteger("maxMagic"));
			instance.setMagic(((NBTTagCompound)nbtTag).getInteger("magic"));
			instance.setSpeed(((NBTTagCompound)nbtTag).getInteger("speed"));
			instance.setPower(((NBTTagCompound)nbtTag).getInteger("power"));
			instance.setIntellect(((NBTTagCompound)nbtTag).getInteger("intellect"));
			instance.setBloodRestore(((NBTTagCompound)nbtTag).getInteger("bloodRestore"));
			instance.setMagicRestore(((NBTTagCompound)nbtTag).getInteger("magicRestore"));
			instance.setPhysicalDefense(((NBTTagCompound)nbtTag).getInteger("physicalDefense"));
			instance.setMagicDefense(((NBTTagCompound)nbtTag).getInteger("magicDefense"));
		}
		@Override
		public NBTBase writeNBT(Capability<ShepherdCapability> capability, ShepherdCapability instance, EnumFacing side) {
			NBTTagCompound nbtTag = new NBTTagCompound();
			nbtTag.setInteger("race", instance.getRace());
			nbtTag.setInteger("level", instance.getLevel());
			nbtTag.setInteger("maxBlood", instance.getMaxBlood());
			nbtTag.setInteger("blood", instance.getBlood());
			nbtTag.setInteger("maxMagic", instance.getMaxMagic());
			nbtTag.setInteger("magic", instance.getMagic());
			nbtTag.setInteger("speed", instance.getSpeed());
			nbtTag.setInteger("power", instance.getPower());
			nbtTag.setInteger("intellect", instance.getIntellect());
			nbtTag.setInteger("bloodRestore", instance.getBloodRestore());
			nbtTag.setInteger("magicRestore", instance.getMagicRestore());
			nbtTag.setInteger("physicalDefense", instance.getPhysicalDefense());
			nbtTag.setInteger("magicDefense", instance.getMagicDefense());
			return nbtTag;
		}
	}
}
