/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.fms.util;

//---------------------------------------------------------------------------
//Filename:        EncryptUtils.java
//Date:            2012-02-23
//Author:          
//Function:        加密
//History:
//
//---------------------------------------------------------------------------

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * String encryption utility methods.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class EncryptUtils {

	/**
	 * Encrypt byte array.
	 */
	private final static byte[] encrypt(byte[] source, String algorithm)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.reset();
		md.update(source);
		return md.digest();
	}

	/**
	 * Encrypt string
	 */
	public final static String encrypt(String source, String algorithm)
			throws NoSuchAlgorithmException {
		byte[] resByteArray = encrypt(source.getBytes(), algorithm);
		return StringUtil.toHexString(resByteArray);
	}

	/**
	 * Encrypt string using MD5 algorithm
	 */
	public final static String encryptMD5(String source) {
		if (source == null) {
			source = "";
		}

		String result = "";
		try {
			result = encrypt(source, "MD5");
		} catch (NoSuchAlgorithmException ex) {
			// this should never happen
			throw new RuntimeException(ex);
		}
		return result;
	}

	/**
	 * Encrypt string using SHA algorithm
	 */
	@SuppressWarnings("unused")
	public final static String encryptSHA(String source) {
		if (source == null) {
			source = "";
		}

		String result = "";
		try {
			result = encrypt(source, "SHA");
		} catch (NoSuchAlgorithmException ex) {
			// this should never happen
			throw new RuntimeException(ex);
		}
		return result;
	}



	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	private static final String HMAC_SHA1 = "HmacSHA1";
	private static final String THREE_DES = "DESede";


	/**
	 * ���ܺ���
	 * @param src ���ĵ��ֽ�����
	 * @return
	 */
	public static byte[] decryptThreeDES(byte[] src,String secretkey) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(secretkey), THREE_DES);
			Cipher c1 = Cipher.getInstance(THREE_DES);
			c1.init(Cipher.DECRYPT_MODE, deskey);    //��ʼ��Ϊ����ģʽ
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ܷ���
	 * @param src Դ���ݵ��ֽ�����
	 * @return
	 */
	public static byte[] encryptThreeDES(byte[] src,String secretkey) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(secretkey), THREE_DES);    //������Կ
			Cipher c1 = Cipher.getInstance(THREE_DES);    //ʵ�����������/���ܵ�Cipher������
			c1.init(Cipher.ENCRYPT_MODE, deskey);    //��ʼ��Ϊ����ģʽ
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}


	/*
     * �����ַ���������Կ�ֽ�����
     * @param keyStr ��Կ�ַ���
     * @return
     * @throws UnsupportedEncodingException
     */
	public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
		byte[] key = new byte[24];    //����һ��24λ���ֽ����飬Ĭ�����涼��0
		byte[] temp = keyStr.getBytes("UTF-8");    //���ַ���ת���ֽ�����

        /*
         * ִ�����鿽��
         * System.arraycopy(Դ���飬��Դ�������￪ʼ������Ŀ�����飬��������λ)
         */
		if(key.length > temp.length){
			//���temp����24λ���򿽱�temp�����������ȵ����ݵ�key������
			System.arraycopy(temp, 0, key, 0, temp.length);
		}else{
			//���temp����24λ���򿽱�temp����24�����ȵ����ݵ�key������
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	/**
	 * MAC�㷨��ѡ���¶����㷨
	 *
	 * <pre>
	 * HmacMD5
	 * HmacSHA1
	 * HmacSHA256
	 * HmacSHA384
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * BASE64����
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64����
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * MD5����
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);

		return md5.digest();

	}

	/**
	 * SHA����
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);

		return sha.digest();

	}

	/**
	 * ��ʼ��HMAC��Կ
	 *
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);

		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC����
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {

		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);

		return mac.doFinal(data);

	}



	/**
	 * ����ǩ������
	 *
	 * @param data �����ܵ�����
	 * @param key  ����ʹ�õ�key
	 * @return ����MD5������ַ���
	 */
	public static String encryptHMAC_SHA1(byte[] data, byte[] key) throws Exception {
		SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data);
		return new String(encryptMD5(rawHmac));
	}



}
