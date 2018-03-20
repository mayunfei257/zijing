package com.zijing.data.message;

import java.util.UUID;

import com.zijing.util.EntityUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpgradeToServerMessage implements IMessage {
	private NBTTagCompound dataTag;

	public UpgradeToServerMessage() {}
	public UpgradeToServerMessage(NBTBase shepherdCapabilityTag, int upLevel, UUID uuid) {
		this.dataTag = new NBTTagCompound();
		this.dataTag.setTag("ShepherdCapabilityTag", (NBTTagCompound)shepherdCapabilityTag);
		this.dataTag.setInteger("UpLevel", upLevel);
		this.dataTag.setString("UUID", uuid.toString());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dataTag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, dataTag);
	}

	public static class Handler implements IMessageHandler<UpgradeToServerMessage, IMessage> {
		@Override
		public IMessage onMessage(final UpgradeToServerMessage message, final MessageContext ctx) {
			if (ctx.side == Side.SERVER){
				int upLevel = message.dataTag.getInteger("UpLevel");
				((WorldServer) ctx.getServerHandler().player.world).addScheduledTask(new Runnable(){
					@Override
					public void run() {
						EntityUtil.upPlayerGrade(ctx.getServerHandler().player, upLevel);
					}
				});
			}
			return null;
		}
	}
}