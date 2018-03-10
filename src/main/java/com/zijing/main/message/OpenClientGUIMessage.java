package com.zijing.main.message;

import com.zijing.main.gui.GuiEntityCapability;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class OpenClientGUIMessage implements IMessage {
	NBTTagCompound dataTag;

	public OpenClientGUIMessage() {}
	public OpenClientGUIMessage(int GUIID, int EntityId) {
		this.dataTag = new NBTTagCompound();
		dataTag.setInteger("GUIID", GUIID);
		dataTag.setInteger("EntityId", EntityId);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dataTag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, dataTag);
	}

	public static class Handler implements IMessageHandler<OpenClientGUIMessage, IMessage> {
		@Override
		public IMessage onMessage(final OpenClientGUIMessage message, final MessageContext ctx) {
			if (ctx.side == Side.CLIENT){
				int GUIID = message.dataTag.getInteger("GUIID");
				int EntityId = message.dataTag.getInteger("EntityId");
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					@Override
					public void run() {
						EntityPlayer player = Minecraft.getMinecraft().player;
						if(GUIID == GuiEntityCapability.GUIID) {
//							Minecraft.getMinecraft().displayGuiScreen(new GuiEntityCapability.MyGuiContainer(player.world, (EntityLiving)player.world.getEntityByID(EntityId), player));
						}
					}
				});
			}
			return null;
		}
	}
}