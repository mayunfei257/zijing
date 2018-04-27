package com.zijing;

import com.zijing.gui.GuiBookChuansong;
import com.zijing.gui.GuiBookChuansongUse;
import com.zijing.gui.GuiCardChuansong;
import com.zijing.gui.GuiEntityCapability;
import com.zijing.gui.GuiPlayeryCapability;
import com.zijing.gui.GuiQiankunDai;
import com.zijing.gui.GuiZhulingTai;

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
		if (id == GuiPlayeryCapability.GUIID)//5
			return new GuiPlayeryCapability.MyContainer(world, x, y, z, player);
		if (id == GuiEntityCapability.GUIID)//6
			return new GuiEntityCapability.MyContainer(world, x, player);
		if (id == GuiQiankunDai.GUIID)//7
			return new GuiQiankunDai.MyContainer(world, x, y, z, player);
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
		if (id == GuiPlayeryCapability.GUIID)//5
			return new GuiPlayeryCapability.MyGuiContainer(world, x, y, z, player);
		if (id == GuiEntityCapability.GUIID)//6
			return new GuiEntityCapability.MyGuiContainer(world, x, player);
		if (id == GuiQiankunDai.GUIID)//7
			return new GuiQiankunDai.MyGuiContainer(world, x, y, z, player);
		return null;
	}
}
