package com.zijing.entity.render;

import com.zijing.ZijingMod;
import com.zijing.entity.EntitySummonTaoistPriest;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSummonTaoistPriest extends RenderBiped<EntitySummonTaoistPriest>{
    private static final ResourceLocation TAOIST_PRIEST_TEXTURES = new ResourceLocation(ZijingMod.MODID + ":entitysummontaoistpriest.png");
    
	public RenderSummonTaoistPriest(RenderManager renderManagerIn, boolean useSmallArms) {
        super(renderManagerIn, new ModelPlayer(0.0F, useSmallArms), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerArrow(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySummonTaoistPriest entity) {
		return TAOIST_PRIEST_TEXTURES;
	}
}
