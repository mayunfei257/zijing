package com.zijing.entity.render;

import com.zijing.entity.EntitySuperSnowman;
import com.zijing.entity.layer.LayerSuperSnowmanHead;
import com.zijing.util.ConstantUtil;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSuperSnowman extends RenderLiving<EntitySuperSnowman>{
    private static final ResourceLocation SUPER_SNOW_MAN_TEXTURES = new ResourceLocation(ConstantUtil.MODID + ":entitysupersnowman.png");

    public RenderSuperSnowman(RenderManager renderManagerIn){
        super(renderManagerIn, new ModelSnowMan(), 0.5F);
        this.addLayer(new LayerSuperSnowmanHead(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
	@Override
    protected ResourceLocation getEntityTexture(EntitySuperSnowman entity){
        return SUPER_SNOW_MAN_TEXTURES;
    }

	@Override
    public ModelSnowMan getMainModel(){
        return (ModelSnowMan)super.getMainModel();
    }
}