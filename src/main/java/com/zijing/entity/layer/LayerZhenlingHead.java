package com.zijing.entity.layer;

import com.zijing.entity.EntityZhenling;
import com.zijing.entity.render.RenderZhenling;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerZhenlingHead implements LayerRenderer<EntityZhenling>{
    private final RenderZhenling zhenlingRenderer;

    public LayerZhenlingHead(RenderZhenling zhenlingRendererIn){
        this.zhenlingRenderer = zhenlingRendererIn;
    }

	@Override
    public void doRenderLayer(EntityZhenling entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){
        if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isPumpkinEquipped()){
            GlStateManager.pushMatrix();
            this.zhenlingRenderer.getMainModel().head.postRender(0.0625F);
            float f = 0.625F;
            GlStateManager.translate(0.0F, -0.34375F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.625F, -0.625F, -0.625F);
            Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, new ItemStack(Blocks.PUMPKIN, 1), ItemCameraTransforms.TransformType.HEAD);
            GlStateManager.popMatrix();
        }
    }

	@Override
    public boolean shouldCombineTextures(){
        return true;
    }
}