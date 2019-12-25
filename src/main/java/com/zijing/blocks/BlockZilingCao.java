package com.zijing.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.zijing.ZijingMod;
import com.zijing.util.ConstantUtil;

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
        this.maxHigh = ZijingMod.config.getZILINGCAO_MAX_HIGHT();
        this.growthKey = ZijingMod.config.getZILINGCAO_GROWTH_KEY();
        this.growthProbability = ZijingMod.config.getZILINGCAO_GROWTH_PROBABILITY();
        this.dropAmount = 1;
        this.enumPlantType = EnumPlantType.Crop;
        setLightLevel(1.0f);
		setUnlocalizedName("blockZilingCao");
		setRegistryName(ConstantUtil.MODID + ":blockzilingcao");
	}
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced){
    	tooltip.add(I18n.format(ConstantUtil.MODID + ".blockZilingCao.1", new Object[0]));
    }
}