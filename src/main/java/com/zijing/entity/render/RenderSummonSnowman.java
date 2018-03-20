package com.zijing.entity.render;

import com.zijing.entity.EntitySummonSnowman;
import com.zijing.entity.layer.LayerSummonSnowmanHead;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSummonSnowman extends RenderLiving<EntitySummonSnowman>{
    private static final ResourceLocation SUMMON_SNOW_MAN_TEXTURES = new ResourceLocation(ConstantUtil.MODID + ":entitysummonsnowman.png");

    public RenderSummonSnowman(RenderManager renderManagerIn){
        super(renderManagerIn, new ModelSnowMan(), 0.5F);
        this.addLayer(new LayerSummonSnowmanHead(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
	@Override
    protected ResourceLocation getEntityTexture(EntitySummonSnowman entity){
        return SUMMON_SNOW_MAN_TEXTURES;
    }

	@Override
    public ModelSnowMan getMainModel(){
        return (ModelSnowMan)super.getMainModel();
    }
}