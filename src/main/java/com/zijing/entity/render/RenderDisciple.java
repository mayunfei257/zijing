package com.zijing.entity.render;

import com.zijing.entity.EntityDisciple;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDisciple extends RenderBiped<EntityDisciple>{
    private static final ResourceLocation FEMALE_DISCIPLE_TEXTURES = new ResourceLocation(ConstantUtil.MODID + ":entityfemaledisciple.png");
    private static final ResourceLocation MALE_DISCIPLE_TEXTURES = new ResourceLocation(ConstantUtil.MODID + ":entitymaledisciple.png");
    
	public RenderDisciple(RenderManager renderManagerIn, boolean useSmallArms) {
        super(renderManagerIn, new ModelPlayer(0.0F, useSmallArms), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerArrow(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDisciple entity) {
		if(entity.getGender() == EntityDisciple.GENDER.FEMALE) {
			return FEMALE_DISCIPLE_TEXTURES;
		}else {
			return MALE_DISCIPLE_TEXTURES;
		}
	}
}
