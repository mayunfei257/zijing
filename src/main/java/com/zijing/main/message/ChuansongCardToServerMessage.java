package com.zijing.main.message;

import java.util.UUID;

import com.zijing.items.card.ItemCardChuansong;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

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

public class ChuansongCardToServerMessage implements IMessage{
	private NBTTagCompound dataTag;
	
	public ChuansongCardToServerMessage() {}
	public ChuansongCardToServerMessage(NBTTagCompound chuansongCardTag, EnumHand hand, UUID uuid) {
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

	public static class Handler implements IMessageHandler<ChuansongCardToServerMessage, IMessage>{
		@Override
		public IMessage onMessage(ChuansongCardToServerMessage message, MessageContext ctx){
			if (ctx.side == Side.SERVER){
				NBTTagCompound chuansongCardTag = (NBTTagCompound)message.dataTag.getTag("ChuansongCardTag");
				EnumHand hand = "MAIN_HAND".equals(message.dataTag.getString("Hand")) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
				((WorldServer) ctx.getServerHandler().player.world).addScheduledTask(new Runnable(){
					@Override
					public void run(){
						EntityPlayer player = ctx.getServerHandler().player;
						if(ShepherdProvider.hasCapabilityFromPlayer(player) && player.getHeldItem(hand).getItem() instanceof ItemCardChuansong) {
							ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
							if(shepherdCapability.getMagic() >= ItemCardChuansong.MagicSkill2) {
								player.getHeldItem(hand).setTagCompound(chuansongCardTag);
								shepherdCapability.setMagic(shepherdCapability.getMagic() - ItemCardChuansong.MagicSkill2);
								ShepherdProvider.updateChangeToClient(player);
							}else {
								player.sendMessage(new TextComponentString("Magic energy is not enough, need at least " + ItemCardChuansong.MagicSkill2 + " !"));
							}
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