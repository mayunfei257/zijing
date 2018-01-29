package com.zijing.blocks;

import com.zijing.ZijingMod;
import com.zijing.main.ZijingTab;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGuhuaNiunaiKuai extends Block{

	public BlockGuhuaNiunaiKuai() {
		super(Material.ICE);
        setHardness(1f);
        setResistance(3.0f);
        setSoundType(SoundType.SNOW);
		setUnlocalizedName("blockGuhuaNiunaiKuai");
		setRegistryName(ZijingMod.MODID + ":blockguhuaniunaikuai");
		setCreativeTab(ZijingTab.zijingTab);
	}
}
