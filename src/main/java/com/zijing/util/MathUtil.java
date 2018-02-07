package com.zijing.util;

public class MathUtil {
	
	public static double getUpgradeK(double level, double upLevel) {
		double K = upLevel * level  + upLevel * (upLevel - 1) / 2.0;
		return K;
	}
}
