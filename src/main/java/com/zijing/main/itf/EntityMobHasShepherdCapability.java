package com.zijing.main.itf;

import com.zijing.main.playerdata.ShepherdCapability;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface EntityMobHasShepherdCapability {

	public double getExperience();
	
	public void setExperience(double xp);

	public int getNextLevelNeedExperience();

	public void setNextLevelNeedExperience(int nextLevelNeedExperience);
	
	public ShepherdCapability getShepherdCapability();

	public double getSwordDamage();

	public double getArmorValue();

	public void setSwordDamage(double swordDamage);

	public void setArmorValue(double armorValue);
	
	public boolean updataSwordDamageAndArmorValue();

    @SideOnly(Side.CLIENT)
	public String getSpecialInstructions();
}
