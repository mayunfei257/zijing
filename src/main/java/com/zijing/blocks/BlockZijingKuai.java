package com.zijing.blocks;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockZijingKuai extends Block{

	public BlockZijingKuai() {
		super(Material.IRON);
        setHardness(50f);
        setResistance(500.0f);
        setLightLevel(1.0f);
        setHarvestLevel("pickaxe", 2);
        setSoundType(SoundType.METAL);
		setUnlocalizedName("blockZijingKuai");
		setRegistryName(ZijingMod.MODID + ":blockzijingkuai");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
