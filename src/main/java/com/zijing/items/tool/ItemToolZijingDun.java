package com.zijing.items.tool;

import javax.annotation.Nullable;

import com.zijing.BaseControl;
import com.zijing.ZijingMod;
import com.zijing.ZijingTab;
import com.zijing.util.ConstantUtil;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolZijingDun extends ItemShield{

	public ItemToolZijingDun() {
        this.maxStackSize = 1;
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter(){
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
        
        setMaxDamage(ZijingMod.config.getTOOL_MAX_USES());
		setUnlocalizedName("itemToolZijingDun");
		setRegistryName(ConstantUtil.MODID + ":itemtoolzijingdun");
		setCreativeTab(ZijingTab.zijingTab);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return repair.getItem() == BaseControl.itemZiqi ? true : super.getIsRepairable(toRepair, repair);
    }

	@Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity){
        return stack.getItem() == this;
    }
}
