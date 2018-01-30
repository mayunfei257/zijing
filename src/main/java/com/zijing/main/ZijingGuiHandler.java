package com.zijing.main;

import com.zijing.main.gui.GuiBookChuansong;
import com.zijing.main.gui.GuiBookChuansongUse;
import com.zijing.main.gui.GuiCardChuansong;
import com.zijing.main.gui.GuiMagicEnergySwitch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ZijingGuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GuiCardChuansong.GUIID)//1
			return new GuiCardChuansong.GuiCardChuansongContainer(world, x, y, z, player);
		if (id == GuiBookChuansong.GUIID)//2
			return new GuiBookChuansong.GuiBookChuansongContainer(world, x, y, z, player);
		if (id == GuiBookChuansongUse.GUIID)//3
			return new GuiBookChuansongUse.GuiBookChuansongUseContainer(world, x, y, z, player);
		if (id == GuiMagicEnergySwitch.GUIID)//4
			return new GuiMagicEnergySwitch.GuiMagicEnergySwitchContainer(world, x, y, z, player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GuiCardChuansong.GUIID)//1
			return new GuiCardChuansong.GuiCardChuansongWindow(world, x, y, z, player);
		if (id == GuiBookChuansong.GUIID)//2
			return new GuiBookChuansong.GuiBookChuansongWindow(world, x, y, z, player);
		if (id == GuiBookChuansongUse.GUIID)//3
			return new GuiBookChuansongUse.GuiBookChuansongUseWindow(world, x, y, z, player);
		if (id == GuiMagicEnergySwitch.GUIID)//4
			return new GuiMagicEnergySwitch.GuiMagicEnergySwitchWindow(world, x, y, z, player);
		return null;
	}
}
