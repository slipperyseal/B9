package net.catchpole.B9.codec.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCoder {
    private final byte[] iv;
    private final Cipher cipher;
    private final SecretKeySpec secretKeySpec;

    public AesCoder(byte[] key, byte[] iv) {
        try {
            this.iv = iv;
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
