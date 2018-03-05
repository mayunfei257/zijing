package com.zijing.entity.render;

import com.zijing.ZijingMod;
import com.zijing.entity.EntitySummonIronGolem;
import com.zijing.entity.layer.LayerSummonIronGolemFlower;
import com.zijing.entity.model.ModelSummonIronGolem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSummonIronGolem extends RenderLiving<EntitySummonIronGolem>{
    private static final ResourceLocation SUMMON_IRON_GOLEM_TEXTURES = new ResourceLocation(ZijingMod.MODID + ":entitySummonIronGolem.png");

    public RenderSummonIronGolem(RenderManager renderManagerIn){
        super(renderManagerIn, new ModelSummonIronGolem(), 0.5F);
        this.addLayer(new LayerSummonIronGolemFlower(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
	@Override
    protected ResourceLocation getEntityTexture(EntitySummonIronGolem entity){
        return SUMMON_IRON_GOLEM_TEXTURES;
    }

	@Override
    protected void applyRotations(EntitySummonIronGolem entityLiving, float p_77043_2_, float rotationYaw, float partialTicks){
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
        if ((double)entityLiving.limbSwingAmount >= 0.01D){
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}