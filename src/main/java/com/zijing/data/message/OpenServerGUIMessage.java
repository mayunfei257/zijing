package com.zijing.data.message;

import com.zijing.ZijingMod;
import com.zijing.gui.GuiPlayeryCapability;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class OpenServerGUIMessage implements IMessage {
	NBTTagCompound dataTag;

	public OpenServerGUIMessage() {}
	public OpenServerGUIMessage(int GUIID, int x, int y, int z) {
		this.dataTag = new NBTTagCompound();
		dataTag.setInteger("GUIID", GUIID);
		dataTag.setInteger("X", x);
		dataTag.setInteger("Y", y);
		dataTag.setInteger("Z", z);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dataTag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, dataTag);
	}

	public static class Handler implements IMessageHandler<OpenServerGUIMessage, IMessage> {
		@Override
		public IMessage onMessage(final OpenServerGUIMessage message, final MessageContext ctx) {
			if (ctx.side == Side.SERVER){
				EntityPlayerMP player = ctx.getServerHandler().player;
				int GUIID = message.dataTag.getInteger("GUIID");
				int x = message.dataTag.getInteger("X");
				int y = message.dataTag.getInteger("Y");
				int z = message.dataTag.getInteger("Z");
				((WorldServer) player.world).addScheduledTask(new Runnable(){
					@Override
					public void run() {
						if(GUIID == GuiPlayeryCapability.GUIID) {
							player.openGui(ZijingMod.instance, GuiPlayeryCapability.GUIID, player.world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
						}
					}
				});
			}
			return null;
		}
	}
}