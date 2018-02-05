package com.zijing.message;

import java.util.UUID;

import com.zijing.items.card.ItemCardChuansong;
import com.zijing.util.PlayerUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChuansongCardMessage implements IMessage{
	private NBTTagCompound dataTag;
	
	public ChuansongCardMessage() {}
	public ChuansongCardMessage(NBTTagCompound chuansongCardTag, EnumHand hand, UUID uuid) {
		this.dataTag = new NBTTagCompound();
		dataTag.setTag("ChuansongCardTag", chuansongCardTag);
		dataTag.setString("Hand", hand == EnumHand.MAIN_HAND ? "MAIN_HAND" : "OFF_HAND");
		dataTag.setString("UUID", uuid.toString());
	}
	
	@Override
	public void fromBytes(ByteBuf buf){
		dataTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf){
		ByteBufUtils.writeTag(buf, dataTag);
	}

	public static class Handler implements IMessageHandler<ChuansongCardMessage, IMessage>{
		@Override
		public IMessage onMessage(ChuansongCardMessage message, MessageContext ctx){
			if (ctx.side == Side.SERVER){
				NBTTagCompound chuansongCardTag = (NBTTagCompound)message.dataTag.getTag("ChuansongCardTag");
				EnumHand hand = "MAIN_HAND".equals(message.dataTag.getString("Hand")) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
				((WorldServer) ctx.getServerHandler().player.world).addScheduledTask(new Runnable(){
					@Override
					public void run(){
						EntityPlayer player = ctx.getServerHandler().player;
						try {
							if(player.getHeldItem(hand).getItem() instanceof ItemCardChuansong) {
								player.getHeldItem(hand).setTagCompound(chuansongCardTag);
								PlayerUtil.minusFoodlevel(player, ItemCardChuansong.foodConsume);
							}
						}catch(Exception e) {
							player.sendMessage(new TextComponentString("Exception :" + e.getMessage()));
						}
					}
				});
			}
			return null;
		}
	}
//	UUID uuid = UUID.fromString(message.dataTag.getString("UUID"));//throw Exception
//	IThreadListener mainThread = ctx.side.isClient() ? Minecraft.getMinecraft() : (WorldServer) ctx.getServerHandler().player.world;
}