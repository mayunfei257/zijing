package com.zijing.data.message;

import com.zijing.itf.EntityHasShepherdCapability;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ShepherdEntityToClientMessage implements IMessage {
	NBTTagCompound dataTag;

	public ShepherdEntityToClientMessage() {}
	public ShepherdEntityToClientMessage(int EntityId, NBTBase shepherdCapabilityTag, int nextLevelNeedExperience, double experience, double swordDamage, double armorValue) {
		this.dataTag = new NBTTagCompound();
		this.dataTag.setInteger("EntityId", EntityId);
		this.dataTag.setTag("shepherdCapabilityTag", shepherdCapabilityTag);
		this.dataTag.setInteger("nextLevelNeedExperience", nextLevelNeedExperience);
		this.dataTag.setDouble("experience", experience);
		this.dataTag.setDouble("swordDamage", swordDamage);
		this.dataTag.setDouble("armorValue", armorValue);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dataTag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, dataTag);
	}

	public static class Handler implements IMessageHandler<ShepherdEntityToClientMessage, IMessage> {
		@Override
		public IMessage onMessage(final ShepherdEntityToClientMessage message, final MessageContext ctx) {
			if (ctx.side == Side.CLIENT){
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					@Override
					public void run() {
						int EntityId = message.dataTag.getInteger("EntityId");
						NBTBase shepherdCapabilityTag = message.dataTag.getTag("shepherdCapabilityTag");
						int nextLevelNeedExperience = message.dataTag.getInteger("nextLevelNeedExperience");
						double experience = message.dataTag.getDouble("experience");
						double swordDamage = message.dataTag.getDouble("swordDamage");
						double armorValue = message.dataTag.getDouble("armorValue");
						Entity entity = Minecraft.getMinecraft().player.world.getEntityByID(EntityId);
						
				    	if(null != entity && entity instanceof EntityHasShepherdCapability) {
				    		EntityHasShepherdCapability shepherdEntity = ((EntityHasShepherdCapability)entity);
				    		shepherdEntity.getShepherdCapability().readNBT(null, shepherdCapabilityTag);
				    		shepherdEntity.setNextLevelNeedExperience(nextLevelNeedExperience);
				    		shepherdEntity.setExperience(experience);
				    		shepherdEntity.setSwordDamage(swordDamage);
				    		shepherdEntity.setArmorValue(armorValue);
				    	}
					}
				});
			}
			return null;
		}
	}
}