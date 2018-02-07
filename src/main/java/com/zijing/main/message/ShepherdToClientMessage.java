package com.zijing.main.message;

import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ShepherdToClientMessage implements IMessage {
	NBTTagCompound dataTag;

	public ShepherdToClientMessage() {}
	public ShepherdToClientMessage(NBTBase shepherdCapabilityTag) {
		this.dataTag = (NBTTagCompound)shepherdCapabilityTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dataTag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, dataTag);
	}

	public static class Handler implements IMessageHandler<ShepherdToClientMessage, IMessage> {
		@Override
		public IMessage onMessage(final ShepherdToClientMessage message, final MessageContext ctx) {
			if (ctx.side == Side.CLIENT){
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					@Override
					public void run() {
						EntityPlayer player = Minecraft.getMinecraft().player;
				    	if(player.hasCapability(ShepherdProvider.SHE_CAP, null)) {
							ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
							shepherdCapability.readNBT(null, message.dataTag);
				    	}
					}
				});
			}
			return null;
		}
	}
}