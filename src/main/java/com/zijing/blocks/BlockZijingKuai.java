package com.zijing.blocks;

import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockZijingKuai extends Block{

	public BlockZijingKuai() {
		super(Material.IRON);
        setHardness(50f);
        setResistance(1000.0f);
        setLightLevel(1.0f);
        setHarvestLevel("pickaxe", 2);
        setSoundType(SoundType.METAL);
		setUnlocalizedName("blockZijingKuai");
		setRegistryName(ConstantUtil.MODID + ":blockzijingkuai");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
