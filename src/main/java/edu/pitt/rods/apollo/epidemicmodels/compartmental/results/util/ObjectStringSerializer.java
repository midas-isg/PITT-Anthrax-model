package edu.pitt.rods.apollo.epidemicmodels.compartmental.results.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

public class ObjectStringSerializer {

	static public String SerializeObjectToString(Object obj) {
		String result = null;
		if (obj != null) {
			try {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
				result = Base64.encodeBase64String(byteArrayOutputStream
						.toByteArray());
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return result;
	}

	static public Object deserializeStringToObject(String str) {

		Object result = null;
		if (str != null) {
			try {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						Base64.decodeBase64(str));
				result = new ObjectInputStream(byteArrayInputStream)
						.readObject();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				System.exit(-1);
			}
		}

		return result;
	}
}
