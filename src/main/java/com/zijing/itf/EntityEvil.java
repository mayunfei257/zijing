package com.zijing.itf;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;

public abstract class EntityEvil extends EntityShepherdCapability implements IMob{

	public EntityEvil(World world) {
		super(world);
	}
	
	public EntityEvil(World world, int baseLevel) {
		super(world, baseLevel);
	}

    
	@Override
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls){
        if (EntityEvil.class.isAssignableFrom(cls)){
        	return false;
        }else {
            return super.canAttackClass(cls);
        }
    }
}
