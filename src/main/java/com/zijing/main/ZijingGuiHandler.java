package com.zijing.main;

import com.zijing.main.gui.GuiBookChuansong;
import com.zijing.main.gui.GuiBookChuansongUse;
import com.zijing.main.gui.GuiCardChuansong;
import com.zijing.main.gui.GuiZhulingTai;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ZijingGuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GuiCardChuansong.GUIID)//1
			return new GuiCardChuansong.MyContainer(world, x, y, z, player);
		if (id == GuiBookChuansong.GUIID)//2
			return new GuiBookChuansong.MyContainer(world, x, y, z, player);
		if (id == GuiBookChuansongUse.GUIID)//3
			return new GuiBookChuansongUse.MyContainer(world, x, y, z, player);
		if (id == GuiZhulingTai.GUIID)//4
			return new GuiZhulingTai.MyContainer(world, x, y, z, player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GuiCardChuansong.GUIID)//1
			return new GuiCardChuansong.MyGuiContainer(world, x, y, z, player);
		if (id == GuiBookChuansong.GUIID)//2
			return new GuiBookChuansong.MyGuiContainer(world, x, y, z, player);
		if (id == GuiBookChuansongUse.GUIID)//3
			return new GuiBookChuansongUse.MyGuiContainer(world, x, y, z, player);
		if (id == GuiZhulingTai.GUIID)//4
			return new GuiZhulingTai.MyGuiContainer(world, x, y, z, player);
		return null;
	}
}
