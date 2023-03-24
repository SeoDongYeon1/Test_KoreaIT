package com.KoreaIT.AM.exam.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static String getNowDateStr() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
        Date now = new Date();
 
        String nowTime1 = sdf1.format(now);
		return nowTime1;
	}
}
