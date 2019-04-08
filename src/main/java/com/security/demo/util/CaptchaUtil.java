package com.security.demo.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CaptchaUtil {
    private static char mapTable[] = {
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'W', 'J', 'K', 'L',
            'M', 'N', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y',
            '2', '3', '4', '5', '8', '9',
            '6', '7', '8', '9', '9', '5',
            '2', '3', '4', '5', '6', '7',
    };

    public static Map<String, Object> getImageCode(int width, int height, OutputStream os, String strEnsure) {
        Map<String, Object> returnMap = new HashMap<>(16);
        if (width <= 0) {
            width = 60;
        }
        if (height <= 0) {
            height = 20;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        //生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        // 随机产生168条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 168; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        //取随机产生的码
        //4代表4位验证码,如果要生成更多位的认证码,则加大数值
        for (int i = 0; i < 4; ++i) {
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 直接生成
            String str = strEnsure.substring(i, i + 1);
            // 设置随便码在背景图图片上的位置
            g.drawString(str, 13 * i + 20, 25);
        }
        // 释放图形上下文
        g.dispose();
        returnMap.put("image", image);
        returnMap.put("strEnsure", strEnsure);
        return returnMap;
    }

    public static String getCaptchaString() {
        String strEnsure = "";
        for (int i = 0; i < 4; i++) {

            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
        }
        return strEnsure;
    }

    //给定范围获得随机颜色
    static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}