package com.zijing.data.message;

import com.zijing.itf.EntityShepherdCapability;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ShepherdEntityToClientMessage implements IMessage {
	NBTTagCompound dataTag;

	public ShepherdEntityToClientMessage() {}
	public ShepherdEntityToClientMessage(int EntityId, NBTBase shepherdCapabilityTag, int nextLevelNeedExperience, double experience, double swordDamage, double armorValue, BlockPos homePos, int maxDistance) {
		this.dataTag = new NBTTagCompound();
		this.dataTag.setInteger("EntityId", EntityId);
		this.dataTag.setTag("shepherdCapabilityTag", shepherdCapabilityTag);
		this.dataTag.setInteger("nextLevelNeedExperience", nextLevelNeedExperience);
		this.dataTag.setDouble("experience", experience);
		this.dataTag.setDouble("swordDamage", swordDamage);
		this.dataTag.setDouble("armorValue", armorValue);
		this.dataTag.setInteger("homePosX", homePos.getX());
		this.dataTag.setInteger("homePosY", homePos.getY());
		this.dataTag.setInteger("homePosZ", homePos.getZ());
		this.dataTag.setInteger("maxDistance", maxDistance);
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
						NBTTagCompound dataTag =  message.dataTag;
						int EntityId = dataTag.getInteger("EntityId");
						NBTBase shepherdCapabilityTag = dataTag.getTag("shepherdCapabilityTag");
						int nextLevelNeedExperience = dataTag.getInteger("nextLevelNeedExperience");
						double experience = dataTag.getDouble("experience");
						double swordDamage = dataTag.getDouble("swordDamage");
						double armorValue = dataTag.getDouble("armorValue");
						BlockPos homePos = new BlockPos(dataTag.getInteger("homePosX"), dataTag.getInteger("homePosY"), dataTag.getInteger("homePosZ"));
						int maxDistance = dataTag.getInteger("maxDistance");
						Entity entity = Minecraft.getMinecraft().player.world.getEntityByID(EntityId);
						
				    	if(null != entity && entity instanceof EntityShepherdCapability) {
				    		EntityShepherdCapability shepherdEntity = ((EntityShepherdCapability)entity);
				    		shepherdEntity.getShepherdCapability().readNBT(null, shepherdCapabilityTag);
				    		shepherdEntity.setNextLevelNeedExperience(nextLevelNeedExperience);
				    		shepherdEntity.setExperience(experience);
				    		shepherdEntity.setSwordDamage(swordDamage);
				    		shepherdEntity.setArmorValue(armorValue);
				    		shepherdEntity.setHomePos(homePos);
				    		shepherdEntity.setMaxDistance(maxDistance);
				    	}
					}
				});
			}
			return null;
		}
	}
}