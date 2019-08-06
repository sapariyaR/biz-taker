package com.bt.biztaker.utils;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;

import com.bt.biztaker.exception.BizTakerException;
import com.bt.biztaker.qrcode.QRObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class BizTakerUtils {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat();
	
	public static SimpleDateFormat getDateFormater(String format) {
		dateFormat.applyPattern(format);
		return dateFormat;
	}
	
	public static Boolean hasString(String textString) {
		if(StringUtils.isEmpty(textString)) {
			return true;
		}
		return false;
	}
	
	public static String getCommas(List<Long> mylist) {
		return StringUtils.join(mylist, ',');
	}
	
	public static String getSHA256EncryptedString(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(password.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder(2 * hash.length);

		for (byte b : hash) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
	
	public static String getStringFromLong(Long longDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(longDate);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		return df.format(calendar.getTime());
	}
	
	public static String calculateCheckSum(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		FileInputStream fis = new FileInputStream(file);
		byte[] dataBytes = new byte[1024];
		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		};

		byte[] mdbytes = md.digest();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		fis.close();
		return sb.toString();
	}
	
	public static File stream2file(InputStream in,String prefix,String postFix) throws IOException {
        final File tempFile = File.createTempFile(prefix, postFix);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
	
	public static String getFileExtension(String fileNameWithExtension){
	      String fileName = fileNameWithExtension;
	      String ext="";
	      int mid= fileName.lastIndexOf(".");
	      ext= fileName.substring(mid+1,fileName.length());
	      return ext.toUpperCase();
	}
	
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map,Boolean isDes) {
		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());
		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				if(isDes) {
					return e2.getValue().compareTo(e1.getValue());
				}else {
					return e1.getValue().compareTo(e2.getValue());
				}
			}
		});
		return sortedEntries;
	}
	
	public static String getRandomToken() {
		return RandomStringUtils.randomAlphanumeric(50);
	}
	
	public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);
			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}
	
	public static String getRandomColor() {
		Color clr = new Color(Color.HSBtoRGB((float) Math.random(), (float) Math.random(), 0.5F + ((float) Math.random())/2F));
		return  "#"+Integer.toHexString(clr.getRGB()).substring(2);
	} 
	
	public static String generateOTP(int len) {
		String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++){
            otp[i] =numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return new String(otp);
	}
	
	public static String encCoded(String item) throws UnsupportedEncodingException {
		 byte[] encryptArray = Base64.encodeBase64(item.getBytes());        
	     String encstr = new String(encryptArray,"UTF-8");   
	     return encstr;
	}
	
	public static String deCoded(String item) throws UnsupportedEncodingException {
		byte[] dectryptArray = item.getBytes();
	     byte[] decarray = Base64.decodeBase64(dectryptArray);
	     String decstr = new String(decarray,"UTF-8"); 
	     return decstr;
	}
	public static byte[] getQRCodeImage(QRObject qrObject, int width, int height) throws WriterException, IOException, NoSuchAlgorithmException {
		String encryptedString = BizTakerUtils.encCoded(JsonUtils.getJson(qrObject).toString());
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(encryptedString, BarcodeFormat.QR_CODE, width, height);
	    
	    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
	    byte[] pngData = pngOutputStream.toByteArray(); 
	    return pngData;
	}
	
	public static QRObject getQRObjectFromQRString(String qrString) throws UnsupportedEncodingException, BizTakerException {
		String deCodedString = BizTakerUtils.deCoded(qrString);
		if(StringUtils.isNoneEmpty(deCodedString)) {
			try {
				return JsonUtils.getGsonInstance().fromJson(deCodedString, QRObject.class);
			}catch(Exception e){
				throw new BizTakerException("Something went wrong with QR code,Regenerate and scan again.");
			}
		}
		return null;
	}
	
}
