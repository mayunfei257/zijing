package com.zijing.itf;

import com.zijing.util.ConstantUtil;

public interface MagicConsumer {
	public static final String MAGIC_ENERGY_STR = ConstantUtil.MODID + ":magicEnergy";
	public static final String MAX_MAGIC_ENERGY_STR = ConstantUtil.MODID + ":maxMagicEnergy";
	public int getMaxMagicEnergyValue();
	public int getMinMagicEnergyValue();
}
