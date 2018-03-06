package com.zijing.entity.render;

import com.zijing.ZijingMod;
import com.zijing.entity.EntitySummonTaoistPriest;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSummonTaoistPriest extends RenderBiped<EntitySummonTaoistPriest>{
    private static final ResourceLocation TAOIST_PRIEST_TEXTURES = new ResourceLocation(ZijingMod.MODID + ":entitysummontaoistpriest.png");
    
	public RenderSummonTaoistPriest(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelZombie(), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this){
            protected void initArmor(){
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
        this.addLayer(new LayerHeldItem(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySummonTaoistPriest entity) {
		return TAOIST_PRIEST_TEXTURES;
	}
}
