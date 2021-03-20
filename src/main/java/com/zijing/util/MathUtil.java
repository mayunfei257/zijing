package com.zijing.util;

import com.zijing.ZijingMod;

public class MathUtil {
	
	
	public static int getMaxBlood(int level) {
		int bloodValue = (int) ((level * level + level) * ZijingMod.config.getUPGRADE_MAXBLOOD_K() / 2.0);	// default key = 2;
		return bloodValue;
	}

	public static int getMaxMagic(int level) {
		int magicValue = (int) ((level * level + level) * ZijingMod.config.getUPGRADE_MAXMAGIC_K() / 2.0);	// default key = 20;
		return magicValue;
	}

	public static double getAttack(int level) {
		double attackValue = (level * level + level) * ZijingMod.config.getUPGRADE_ATTACK_K() / 2.0;		// default key = 0.05;
		return attackValue;
	}

	public static double getPhysicalDefense(int level) {
		double physicalDefense = (level * level + level) * ZijingMod.config.getUPGRADE_PHYSICALDEFENSE_K() / 2.0;	// default key = 0.1;
		return physicalDefense;
	}

	public static double getBloodRestore(int level) {
		double bloodRestore = (level * level + level) * ZijingMod.config.getUPGRADE_BLOODRESTORE_K() / 2.0;		// default key = 0.0002;
		return bloodRestore;
	}

	public static double getMagicRestore(int level) {
		double magicRestore = (level * level + level) * ZijingMod.config.getUPGRADE_MAGICRESTORE_K() / 2.0;		// default key = 0.004;
		return magicRestore;
	}

	public static int getNeedXP(int level) {
		int needXP = (int) ((level * level + level) * ZijingMod.config.getUPGRADE_NEED_XP_K() / 2.0);			// default key = 50;
		return needXP;
	}
	
	public static int getNeedXP(int nowLevel, int upLevel) {
		int needXP = 0;
		for(int n = 0; n < upLevel; n++) {
			needXP += getNeedXP(nowLevel + n - 1);
		}
		return needXP;
	}
}
