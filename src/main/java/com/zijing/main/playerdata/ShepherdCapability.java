package com.zijing.main.playerdata;

import com.zijing.ZijingMod;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ShepherdCapability{
	public static ShepherdStorage storage = new ShepherdCapability.ShepherdStorage();
	public static final String NAME = ZijingMod.MODID + "shepherd_capability";
	public static final int UPGRADE_NEED_XP_K = 200;
	public static final int UPGRADE_NEED_MAGIC_K = 20;
	public static final int UPGRADE_MAXMAGIC_K = 20;
	public static final int UPGRADE_MAXBLOOD_K = 2;
	public static final double UPGRADE_POWER_K = 0.5;
	public static final double UPGRADE_BLOODRESTORE_K = 0.01;
	public static final double UPGRADE_MAGICRESTORE_K = 0.01;
	
	public static final double BASE_BLOOD = 2;
	public static final double BASE_MAGIC = 20;
	public static final int BASE_RACE = 0;
	public static final int BASE_LEVEL = 1;
	public static final double BASE_MAXBLOOD = 2;
	public static final double BASE_MAXMAGIC = 20;
	public static final double BASE_SPEED = 0;
	public static final double BASE_POWER = 0;
	public static final double BASE_INTELLECT = 0;
	public static final double BASE_BLOODRESTORE = 0.01;
	public static final double BASE_MAGICRESTORE = 0.01;
	public static final double BASE_PHYSICALDEFENSE = 0;
	public static final double BASE_MAGICDEFENSE = 0;

	private double blood;
	private double magic;
	private int race;
	private int level;
	private double maxBlood;
	private double maxMagic;
	private double speed;
	private double power;
	private double intellect;
	private double bloodRestore;
	private double magicRestore;
	private double physicalDefense;
	private double magicDefense;

	public ShepherdCapability(){
		blood = ShepherdCapability.BASE_BLOOD;
		magic = ShepherdCapability.BASE_MAGIC;
		race = ShepherdCapability.BASE_RACE;
		level = ShepherdCapability.BASE_LEVEL;
		maxBlood = ShepherdCapability.BASE_MAXBLOOD;
		maxMagic = ShepherdCapability.BASE_MAXMAGIC;
		speed = ShepherdCapability.BASE_SPEED;
		power = ShepherdCapability.BASE_POWER;
		intellect = ShepherdCapability.BASE_INTELLECT;
		bloodRestore = ShepherdCapability.BASE_BLOODRESTORE;
		magicRestore = ShepherdCapability.BASE_MAGICRESTORE;
		physicalDefense = ShepherdCapability.BASE_PHYSICALDEFENSE;
		magicDefense = ShepherdCapability.BASE_MAGICDEFENSE;
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
	public double getMaxBlood() {return maxBlood;}
	public void setMaxBlood(double maxBlood) {this.maxBlood = maxBlood;}
	public double getBlood() {return blood;}
	public void setBlood(double blood) {this.blood = blood;}
	public double getMaxMagic() {return maxMagic;}
	public void setMaxMagic(double maxMagic) {this.maxMagic = maxMagic;}
	public double getMagic() {return magic;}
	public void setMagic(double magic) {this.magic = magic;}
	public double getSpeed() {return speed;}
	public void setSpeed(double speed) {this.speed = speed;}
	public double getPower() {return power;}
	public void setPower(double power) {this.power = power;}
	public double getIntellect() {return intellect;}
	public void setIntellect(double intellect) {this.intellect = intellect;}
	public double getBloodRestore() {return bloodRestore;}
	public void setBloodRestore(double bloodRestore) {this.bloodRestore = bloodRestore;}
	public double getMagicRestore() {return magicRestore;}
	public void setMagicRestore(double magicRestore) {this.magicRestore = magicRestore;}
	public double getPhysicalDefense() {return physicalDefense;}
	public void setPhysicalDefense(double physicalDefense) {this.physicalDefense = physicalDefense;}
	public double getMagicDefense() {return magicDefense;}
	public void setMagicDefense(double magicDefense) {this.magicDefense = magicDefense;}
	
	public static class ShepherdStorage implements Capability.IStorage<ShepherdCapability> {
		@Override
		public void readNBT(Capability<ShepherdCapability> capability, ShepherdCapability instance, EnumFacing side, NBTBase nbtTag) {
			instance.setRace(((NBTTagCompound)nbtTag).getInteger("race"));
			instance.setLevel(((NBTTagCompound)nbtTag).getInteger("level"));
			instance.setMaxBlood(((NBTTagCompound)nbtTag).getDouble("maxBlood"));
			instance.setBlood(((NBTTagCompound)nbtTag).getDouble("blood"));
			instance.setMaxMagic(((NBTTagCompound)nbtTag).getDouble("maxMagic"));
			instance.setMagic(((NBTTagCompound)nbtTag).getDouble("magic"));
			instance.setSpeed(((NBTTagCompound)nbtTag).getDouble("speed"));
			instance.setPower(((NBTTagCompound)nbtTag).getDouble("power"));
			instance.setIntellect(((NBTTagCompound)nbtTag).getDouble("intellect"));
			instance.setBloodRestore(((NBTTagCompound)nbtTag).getDouble("bloodRestore"));
			instance.setMagicRestore(((NBTTagCompound)nbtTag).getDouble("magicRestore"));
			instance.setPhysicalDefense(((NBTTagCompound)nbtTag).getDouble("physicalDefense"));
			instance.setMagicDefense(((NBTTagCompound)nbtTag).getDouble("magicDefense"));
		}
		@Override
		public NBTBase writeNBT(Capability<ShepherdCapability> capability, ShepherdCapability instance, EnumFacing side) {
			NBTTagCompound nbtTag = new NBTTagCompound();
			nbtTag.setInteger("race", instance.getRace());
			nbtTag.setInteger("level", instance.getLevel());
			nbtTag.setDouble("maxBlood", instance.getMaxBlood());
			nbtTag.setDouble("blood", instance.getBlood());
			nbtTag.setDouble("maxMagic", instance.getMaxMagic());
			nbtTag.setDouble("magic", instance.getMagic());
			nbtTag.setDouble("speed", instance.getSpeed());
			nbtTag.setDouble("power", instance.getPower());
			nbtTag.setDouble("intellect", instance.getIntellect());
			nbtTag.setDouble("bloodRestore", instance.getBloodRestore());
			nbtTag.setDouble("magicRestore", instance.getMagicRestore());
			nbtTag.setDouble("physicalDefense", instance.getPhysicalDefense());
			nbtTag.setDouble("magicDefense", instance.getMagicDefense());
			return nbtTag;
		}
	}
}
