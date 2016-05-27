package com.eugene.webchatapp.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by eugene on 27.04.16.
 */
public class HashCode {

    public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String sha1 = "";

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("UTF-8"));
        Formatter formatter = new Formatter();

        for(byte b : crypt.digest()){
            formatter.format("%02x", b);
        }

        sha1 = formatter.toString();
        formatter.close();

        return sha1;

    }

}
