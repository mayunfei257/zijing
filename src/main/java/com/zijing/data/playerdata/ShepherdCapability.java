package com.zijing.data.playerdata;

import com.zijing.ZijingMod;
import com.zijing.util.ConstantUtil;
import com.zijing.util.MathUtil;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ShepherdCapability{
	public static ShepherdStorage storage = new ShepherdCapability.ShepherdStorage();
	public static final String NAME = ConstantUtil.MODID + "shepherd_capability";
	public static final int BASE_RACE = 0;
	public static final int BASE_LEVEL = 1;
	public static final double BASE_SPEED = 0D;
	public static final double BASE_INTELLECT = 0D;
	public static final double BASE_PHYSICALDEFENSE = 0D;
	public static final double BASE_MAGICDEFENSE = 0D;

	private double blood;
	private double magic;
	private int race;
	private int level;
	private double maxBlood;
	private double maxMagic;
	private double speed;
	private double attack;
	private double intellect;
	private double bloodRestore;
	private double magicRestore;
	private double physicalDefense;
	private double magicDefense;

	public ShepherdCapability(){
		this.level = ShepherdCapability.BASE_LEVEL;
		this.race = ShepherdCapability.BASE_RACE;
		this.maxBlood = MathUtil.getMaxBlood(this.level);
		this.maxMagic = MathUtil.getMaxMagic(this.level);;
		this.speed = ShepherdCapability.BASE_SPEED;
		this.attack = MathUtil.getAttack(this.level);
		this.intellect = ShepherdCapability.BASE_INTELLECT;
		this.bloodRestore = MathUtil.getBloodRestore(this.level);
		this.magicRestore = MathUtil.getMagicRestore(this.level);
		this.physicalDefense = MathUtil.getPhysicalDefense(this.level);
		this.magicDefense = ShepherdCapability.BASE_MAGICDEFENSE;

		this.blood = this.maxBlood;
		this.magic = this.maxMagic;
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
	public double getAttack() {return attack;}
	public void setAttack(double attack) {this.attack = attack;}
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
			instance.setAttack(((NBTTagCompound)nbtTag).getDouble("attack"));
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
			nbtTag.setDouble("attack", instance.getAttack());
			nbtTag.setDouble("intellect", instance.getIntellect());
			nbtTag.setDouble("bloodRestore", instance.getBloodRestore());
			nbtTag.setDouble("magicRestore", instance.getMagicRestore());
			nbtTag.setDouble("physicalDefense", instance.getPhysicalDefense());
			nbtTag.setDouble("magicDefense", instance.getMagicDefense());
			return nbtTag;
		}
	}
}
