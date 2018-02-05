package com.zijing.player;

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

public class ShepherdMessage implements IMessage {
	private NBTTagCompound nbtTag;

	public ShepherdMessage() {}
	public ShepherdMessage(NBTBase nbtTag) {
		this.nbtTag = (NBTTagCompound)nbtTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbtTag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbtTag);
	}

	public static class Handler implements IMessageHandler<ShepherdMessage, IMessage> {
		@Override
		public IMessage onMessage(final ShepherdMessage message, final MessageContext ctx) {
			if (ctx.side == Side.CLIENT){
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					@Override
					public void run() {
						EntityPlayer player = Minecraft.getMinecraft().player;
				    	if(player.hasCapability(ShepherdProvider.SHE_CAP, null)) {
							ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
							shepherdCapability.readNBT(null, message.nbtTag);
				    	}
					}
				});
			}
			return null;
		}
	}
}