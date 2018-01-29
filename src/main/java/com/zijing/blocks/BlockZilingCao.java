package com.zijing.blocks;

import com.zijing.ZijingMod;

import net.minecraftforge.common.EnumPlantType;

public class BlockZilingCao extends BlockGrowthTypePlant{

	public BlockZilingCao() {
		super();
        this.high = 8;
        this.growthKey = 1;
        this.dropAmount = 1;
        this.enumPlantType = EnumPlantType.Crop;
        setLightLevel(1.0f);
		setUnlocalizedName("blockZilingCao");
		setRegistryName(ZijingMod.MODID + ":blockzilingcao");
	}
}