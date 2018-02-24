package com.tongwei.auth.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author		yangz
 * @date		2018年1月24日 下午6:48:07
 * @description	aes对称加密
 */
public class AESUtil {

	// 盐与sso服务端一致
	private static String encodeSalt;
	
	private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);

	private static byte[] encode0(String encodeRules, String content)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(encodeRules.getBytes());
		keygen.init(128, random);
		SecretKey original_key = keygen.generateKey();
		byte[] raw = original_key.getEncoded();
		SecretKey key = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] byte_encode = content.getBytes("utf-8");
		byte[] byte_AES = cipher.doFinal(byte_encode);
		return byte_AES;
	}

	private static Cipher decode0(String encodeRules)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(encodeRules.getBytes());
		keygen.init(128, random);
		SecretKey original_key = keygen.generateKey();
		byte[] raw = original_key.getEncoded();
		SecretKey key = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher;
	}

	public static String encodeToHex(String encodeSalt,String content) throws Exception {
		byte[] encode0 = encode0(encodeSalt, content);
		return byte2hex(encode0);
	}
	
	public static String encodeToHex(String content) throws Exception {
		byte[] encode0 = encode0(encodeSalt, content);
		return byte2hex(encode0);
	}
	
	public static String decodeFromHex(String encodeSalt,String content) throws Exception {
		Cipher cipher = decode0(encodeSalt);
		byte[] byte_content = hex2byte(content);
		byte[] byte_decode = cipher.doFinal(byte_content);
		return new String(byte_decode,"UTF-8");
	}
	
	public static String decodeFromHex(String content) throws Exception {
		Cipher cipher = decode0(encodeSalt);
		byte[] byte_content = hex2byte(content);
		byte[] byte_decode = cipher.doFinal(byte_content);
		return new String(byte_decode,"UTF-8");
	}

	private static String byte2hex(byte[] b) {
		StringBuilder sb = new StringBuilder();

		for (int n = 0; n < b.length; ++n) {
			String stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				sb.append("0");
			}
			sb.append(stmp);
		}
		return sb.toString().toUpperCase();
	}

	private static byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		int i = 0;
		int j = 0;
		for (int l = hex.length(); i < l; ++j) {
			String swap = new StringBuilder().append("").append(arr[(i++)]).append(arr[i]).toString();
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();

			++i;
		}
		return b;
	}
	
	public static void setEncodeSalt(String encodeSalt) {
		if(AESUtil.encodeSalt==null){
			AESUtil.encodeSalt = encodeSalt;
			logger.info("加密盐值设置完毕!");
		}else{
			logger.error("加密盐值已经存在,请勿重复设置!");
		}
	}

	public static void main(String[] args) throws Exception {
		setEncodeSalt("yangz@919573416@qq.com");
		String decodeFromHex = decodeFromHex("77F95A056B93A9AFFE9135D84DE872D8ADE3099F59783C3FF2E2A333330AC11D");
		System.out.println(decodeFromHex);
		System.err.println(encodeToHex("88888888888888_2"));
	}

}
