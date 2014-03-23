package com.slothproductions.riskybusiness.model;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by riv on 23.03.14.
 */

public class SecureWrapper<T extends java.io.Serializable> implements java.io.Serializable {
    private static final long serialVersionUID = 32360427813499L;
    final private String algorithm;
    final protected SealedObject so;

    protected SecureWrapper(String password, T o) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, InvalidKeyException {
        Cipher c;
        SecretKeySpec ks;

        try {
            c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch(NoSuchAlgorithmException e) {
            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        algorithm = c.getAlgorithm();
        ks = new SecretKeySpec(password.getBytes(), "AES");
        c.init(Cipher.ENCRYPT_MODE, ks);
        so = new SealedObject(o, c);
    }

    @SuppressWarnings("unchecked")
    public T unwrap(String password) {
        Cipher c;
        SecretKeySpec ks;

        try {
            c = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException();
        }

        ks = new SecretKeySpec(password.getBytes(), "AES");

        try {
            c.init(Cipher.DECRYPT_MODE, ks);
        } catch (InvalidKeyException e) {
            throw new RuntimeException();
        }

        try {
            return (T) so.getObject(c);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException();
        } catch (BadPaddingException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}