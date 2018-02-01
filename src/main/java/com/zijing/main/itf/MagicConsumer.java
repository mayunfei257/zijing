package com.zijing.main.itf;

import com.zijing.ZijingMod;

public interface MagicConsumer {
	public static final String MAGIC_ENERGY_STR = ZijingMod.MODID + ":magicEnergy";
	public static final String MAX_MAGIC_ENERGY_STR = ZijingMod.MODID + ":maxMagicEnergy";
	public int getMaxMagicEnergyValue();
	public int getMinMagicEnergyValue();
}
