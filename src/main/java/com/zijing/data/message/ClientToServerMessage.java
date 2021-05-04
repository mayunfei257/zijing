package com.zijing.data.message;

import java.util.UUID;

import com.zijing.entity.TileEntityTiandaoGaiwu;
import com.zijing.entity.TileEntityZhulingTai;
import com.zijing.gui.GuiTiandaoGaiwu;
import com.zijing.gui.GuiZhulingTai;
import com.zijing.util.ConstantUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClientToServerMessage implements IMessage{

	private NBTTagCompound dataTag;
	
	public ClientToServerMessage() {}
	public ClientToServerMessage(UUID uuid, String messageType, NBTTagCompound messageDate) {
		this.dataTag = new NBTTagCompound();
		dataTag.setString("UUID", uuid.toString());
		dataTag.setString("MessageType", messageType);
		dataTag.setTag("MessageDate", messageDate);
	}
	
	@Override
	public void fromBytes(ByteBuf buf){
		dataTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf){
		ByteBufUtils.writeTag(buf, dataTag);
	}
	
	public static class Handler implements IMessageHandler<ClientToServerMessage, IMessage>{
		@Override
		public IMessage onMessage(final ClientToServerMessage message, MessageContext ctx){
			if (ctx.side == Side.SERVER){
				final EntityPlayerMP player = ctx.getServerHandler().player;
				final World world = player.getEntityWorld();
				final String uuid = message.dataTag.getString("UUID");
				final String messageType = message.dataTag.getString("MessageType");
				final NBTTagCompound messageDate = (NBTTagCompound)message.dataTag.getTag("MessageDate");
				
				if(GuiZhulingTai.GUINAME.equals(messageType)) {
					int dimensionId = messageDate.getInteger("DimensionId");
					int x = messageDate.getInteger("X");
					int y = messageDate.getInteger("Y");
					int z = messageDate.getInteger("Z");
					if(dimensionId == world.provider.getDimension()) {
						TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
						if(null != tileEntity && tileEntity instanceof TileEntityZhulingTai) {
							((TileEntityZhulingTai)tileEntity).execute(player);
						}else {
							player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".message.titleEntity.error2", new Object[] {null == tileEntity ? "NULL" : tileEntity.getClass().getSimpleName()})));
						}
					}else {
						player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".message.blockZhulingTai.error1", new Object[] {dimensionId, world.provider.getDimension()})));
					}
					
				} else if(GuiTiandaoGaiwu.GUINAME.equals(messageType)) {
					String operationType = messageDate.getString("OperationType");
					int dimensionId = messageDate.getInteger("DimensionId");
					int x = messageDate.getInteger("X");
					int y = messageDate.getInteger("Y");
					int z = messageDate.getInteger("Z");
					String nbtStr = messageDate.getString("nbtStr");
					
					TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
					if(null != tileEntity && tileEntity instanceof TileEntityTiandaoGaiwu) {
						((TileEntityTiandaoGaiwu)tileEntity).execute(player, operationType, nbtStr);
					}else {
						player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".message.titleEntity.error2", new Object[] {null == tileEntity ? "NULL" : tileEntity.getClass().getSimpleName()})));
					}
				} else {
					player.sendMessage(new TextComponentString(I18n.format(ConstantUtil.MODID + ".clientToServerMessage.error1", new Object[] {messageType})));
				}
			}
			return null;
		}
	}
}
