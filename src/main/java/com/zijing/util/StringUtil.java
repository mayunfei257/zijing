package com.zijing.util;

import net.minecraft.util.text.TextComponentTranslation;

public class StringUtil {

	public static TextComponentTranslation MagicIsNotEnough(int value) {
		return new TextComponentTranslation("zijingmod.magic.notenough", new Object[] {value});
	}
}
