package com.zijing.main.itf;

import com.zijing.main.playerdata.ShepherdCapability;

public interface EntityHasShepherdCapability{

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
}
