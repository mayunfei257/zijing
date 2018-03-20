package com.zijing.entity.render;

import com.zijing.entity.EntitySuperIronGolem;
import com.zijing.entity.layer.LayerSuperIronGolemFlower;
import com.zijing.entity.model.ModelSuperIronGolem;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSuperIronGolem extends RenderLiving<EntitySuperIronGolem>{
    private static final ResourceLocation SUPER_IRON_GOLEM_TEXTURES = new ResourceLocation(ConstantUtil.MODID + ":entitySuperIronGolem.png");

    public RenderSuperIronGolem(RenderManager renderManagerIn){
        super(renderManagerIn, new ModelSuperIronGolem(), 0.5F);
        this.addLayer(new LayerSuperIronGolemFlower(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
	@Override
    protected ResourceLocation getEntityTexture(EntitySuperIronGolem entity){
        return SUPER_IRON_GOLEM_TEXTURES;
    }

	@Override
    protected void applyRotations(EntitySuperIronGolem entityLiving, float p_77043_2_, float rotationYaw, float partialTicks){
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
        if ((double)entityLiving.limbSwingAmount >= 0.01D){
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}