package com.security.demo.util;

import cn.hutool.core.util.StrUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author fanglingxiao
 */
public class MD5Util {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String code32(String origin, String charset) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if ((charset == null) || (charset.isEmpty())) {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charset)));
            }
        } catch (Exception ignored) {
        }
        return resultString;
    }

    public static String code16(String origin, String charset) {
        String resultString = code32(origin, charset);
        return resultString.substring(8, 24);
    }

    public static String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("GBK"));
            StringBuilder buf = new StringBuilder();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xFF));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toMD5(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            StringBuilder buf = new StringBuilder();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xFF));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();

            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];

                str[(k++)] = hexDigits[(b >> 4 & 0xF)];
                str[(k++)] = hexDigits[(b & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }

    public static String generateDigestWithMd5AndBase64(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(md5.digest());
        } catch (Exception ex) {
            // error("generateDigest error, data={}", data, ex);
            return StrUtil.EMPTY;
        }
    }

    public static void main(String[] args) {
        String source = "Request=<RequestOrder><WaybillNo>CJSD002</WaybillNo><ClientCode>CJSD002</ClientCode><Holiday>0</Holiday><ReplCost>0</ReplCost><StmtForm>1</StmtForm><TrustClientCode>100058</TrustClientCode><TrustPerson>君联速递</TrustPerson><TrustUnit>广州君联速递有限公司</TrustUnit><TrustZipCode>510000</TrustZipCode><TrustCity>广东省广州市</TrustCity><TrustAddress>广东省广州市白云区新科村新科工业区弘森国际物流中心A115号</TrustAddress><TrustMobile></TrustMobile><TrustTel>020-36539611</TrustTel><GetPerson>城际速递</GetPerson><GetUnit></GetUnit><GetZipCode>022</GetZipCode><GetCity>天津市</GetCity><GetAddress>测试城际速递对接</GetAddress><GetTel></GetTel><GetMobile>7894654</GetMobile><InsForm>60</InsForm><InsureValue>0.00</InsureValue><GoodsValue>0.00</GoodsValue><WorkType>0</WorkType><OrderTypeItem></OrderTypeItem><GoodsInfo><Good><GoodsName></GoodsName><GoodsValue>0.00</GoodsValue><GoodsBarCode></GoodsBarCode><ListType>0</ListType><ISInvoice>0</ISInvoice></Good></GoodsInfo><GoodsNum>1</GoodsNum><GoodsHav>1.00</GoodsHav></RequestOrder>2011";
    }
}