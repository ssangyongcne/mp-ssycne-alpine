package kr.co.sscm.common.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kr.co.sscm.common.Constants;

/**
 * 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스
 */
public class AES256Util {

	// 사용자 지정 키로 AES256 암호화
    public static String encrypt(String plainText) throws Exception {
    	byte[] tmp = new byte[32];
    	byte[] keyData = Constants.KEY.getBytes();
    	System.arraycopy(keyData, 0, tmp, 0, keyData.length);
        return encByKey(tmp, plainText.getBytes());
    }

    // 사용자 지정 키로 AES256 암호화
    public static String encByKey(byte[] key, byte[] plainByte) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(Constants.IV));
        byte[] randomKey = cipher.doFinal(plainByte);
        return Base64.getEncoder().encodeToString(randomKey);
    }

	// 사용자 지정 키로 AES256 복호화
    public static String decrypt(String encText) throws Exception {
    	byte[] tmp = new byte[32];
    	byte[] keyData = Constants.KEY.getBytes();
    	System.arraycopy(keyData, 0, tmp, 0, keyData.length);
        return decByKey(tmp, Base64.getDecoder().decode(encText));
    }

    // 사용자 지정 키로 AES256 복호화
    public static String decByKey(byte[] key, byte[] encText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(Constants.IV));
        byte[] secureKey = cipher.doFinal(encText);

        return new String(secureKey);
    }

    public static void main(String[] args) throws Exception {

    	System.out.println(AES256Util.encrypt("fjfjfj"));
    	System.out.println(AES256Util.decrypt("CM2AjPEHlCqx33XW5lFp/w=="));
    	System.out.println(AES256Util.decrypt("gyXJvEscnEAiHEPrEDxkcw=="));
    	System.out.println(AES256Util.decrypt("R2SYen1Gt3eVXzmeAFdTpw=="));
	}
}
