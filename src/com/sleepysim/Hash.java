package com.sleepysim;
import org.bouncycastle.crypto.digests.SHA256Digest;

public class Hash
{
    public static byte[] hash(byte[] input)
    {
        byte[] result;
        SHA256Digest sha256 = new SHA256Digest();
        sha256.update(input, 0, input.length);
        result = new byte[sha256.getDigestSize()];
        sha256.doFinal(result, 0);
        return result;
    }
}
