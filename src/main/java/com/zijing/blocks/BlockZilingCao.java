package com.zijing.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced){
    	tooltip.add(I18n.format(ZijingMod.MODID + ".blockZilingCao.1", new Object[0]));
    }
}