package com.zijing.util;

import java.text.DecimalFormat;

import net.minecraft.util.text.TextComponentTranslation;

public class StringUtil {

	public static TextComponentTranslation magicIsNotEnough(int value) {
		return new TextComponentTranslation("zijingmod.magic.notenough", new Object[] {value});
	}
	
	public static TextComponentTranslation damageReduction(double value, float amount) {
		DecimalFormat df2 = new DecimalFormat("#0.00");
		return new TextComponentTranslation("zijingmod.damage.reduction", new Object[] {df2.format(value), df2.format(amount)});
	}
}
