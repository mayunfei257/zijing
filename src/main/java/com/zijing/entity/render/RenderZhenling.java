package com.zijing.entity.render;

import com.zijing.entity.EntityZhenling;
import com.zijing.entity.layer.LayerZhenlingHead;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZhenling extends RenderLiving<EntityZhenling>{
    private static final ResourceLocation ZHEN_LING_TEXTURES = new ResourceLocation(ConstantUtil.MODID + ":entityzhenling.png");

    public RenderZhenling(RenderManager renderManagerIn){
        super(renderManagerIn, new ModelSnowMan(), 0.5F);
        this.addLayer(new LayerZhenlingHead(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
	@Override
    protected ResourceLocation getEntityTexture(EntityZhenling entity){
        return ZHEN_LING_TEXTURES;
    }

	@Override
    public ModelSnowMan getMainModel(){
        return (ModelSnowMan)super.getMainModel();
    }
}