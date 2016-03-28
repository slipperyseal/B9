package net.catchpole.B9.codec.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCoder {
    private final byte[] iv;
    private final Cipher cipher;
    private final SecretKeySpec secretKeySpec;

    public AesCoder(byte[] key, byte[] iv) throws Exception {
        this.iv = iv;
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        this.secretKeySpec = new SecretKeySpec(key, "AES");
    }

    public byte[] encrypt(byte[] data) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }
}
