package com.zijing.itf;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;

public abstract class ItemStaff extends Item{
	public abstract double skill1(EntityShepherdCapability thrower, EntityLivingBase target);
	public abstract double skill2(EntityShepherdCapability thrower, EntityLivingBase target);
	public abstract double skill3(EntityShepherdCapability thrower, EntityLivingBase target);
	public abstract double skill4(EntityShepherdCapability thrower, EntityLivingBase target);
	public abstract double skill5(EntityShepherdCapability thrower, EntityLivingBase target);
}
