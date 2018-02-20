package com.zijing.main.message;

import java.util.UUID;

import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.card.ItemCardChuansong;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChuansongBookToServerMessage implements IMessage{
	private NBTTagCompound dataTag;

	public ChuansongBookToServerMessage() {}
	public ChuansongBookToServerMessage(NBTTagCompound chuansongBookTag, NBTTagCompound chuansongCardTag, EnumHand hand, UUID uuid) {
		this.dataTag = new NBTTagCompound();
		dataTag.setTag("ChuansongBookTag", chuansongBookTag);
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

	public static class Handler implements IMessageHandler<ChuansongBookToServerMessage, IMessage>{
		@Override
		public IMessage onMessage(ChuansongBookToServerMessage message, MessageContext ctx){
			if (ctx.side == Side.SERVER){
				NBTTagCompound chuansongBookTag = (NBTTagCompound)message.dataTag.getTag("ChuansongBookTag");
				NBTTagCompound chuansongCardTag = (NBTTagCompound)message.dataTag.getTag("ChuansongCardTag");
				EnumHand hand = "MAIN_HAND".equals(message.dataTag.getString("Hand")) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
				((WorldServer) ctx.getServerHandler().player.world).addScheduledTask(new Runnable(){
					@Override
					public void run(){
						EntityPlayer player = ctx.getServerHandler().player;
						if(null != chuansongCardTag && chuansongCardTag.getBoolean(ItemCardChuansong.IS_BIND) && player.dimension == chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)) {
							if(ShepherdProvider.hasCapabilityFromPlayer(player) && ShepherdProvider.getCapabilityFromPlayer(player).getMagic() >= 3) {
								ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
								try {
									if(player.getHeldItem(hand).getItem() instanceof ItemBookChuansong) {
										player.getHeldItem(hand).setTagCompound(chuansongBookTag);
										double x = chuansongCardTag.getDouble(ItemCardChuansong.BIND_LX);
										double y = chuansongCardTag.getDouble(ItemCardChuansong.BIND_LY);
										double z = chuansongCardTag.getDouble(ItemCardChuansong.BIND_LZ);
										player.setPositionAndUpdate(x, y, z);
										player.world.playSound((EntityPlayer) null, player.posX, player.posY + 0.5D, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
										shepherdCapability.setMagic(shepherdCapability.getMagic() - 3.0D);
										ShepherdProvider.updateChangeToClient(player);
									}
								}catch(Exception e) {
									player.sendMessage(new TextComponentString("Exception :" + e.getMessage()));
								}
							}else {
								player.sendMessage(new TextComponentString("Magic energy is not enough, need at least 3!"));
							}
						}else if(player.dimension != chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)){
							player.sendMessage(new TextComponentString("Not the same world! -1 = the Nether, 0 = normal world , this is " + chuansongCardTag.getInteger(ItemCardChuansong.BIND_WORLD)));
						}else {
							player.sendMessage(new TextComponentString("Not yet bound!"));
						}
					}
				});
			}
			return null;
		}
	}
	//	UUID uuid = UUID.fromString(message.dataTag.getString("UUID"));
}
