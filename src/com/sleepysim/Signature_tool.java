package com.sleepysim;

import java.security.*;

/**
 * Tool class for sign
 */
public class Signature_tool
{
    /**
     * generate a signature
     * @param key privatekey
     * @param data message
     * @return the signature
     */
    static public byte[] generate_signature(PrivateKey key, byte[] data)
    {
        //some code goes here
        //for framework team
        Signature signer;
        try
        {
            signer = Signature.getInstance("SHA256WithRSA", "BC");
            signer.initSign(key);
            signer.update(data);
            return signer.sign();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * check if the signature is signed by node_id
     * @param pk public key
     * @param sig signature
     * @param data
     */
    static public boolean check_signature(PublicKey pk, byte[] sig, byte[] data)
    {
        //some code goes here
        //for framework team
        Signature signer;
        try
        {
            signer = Signature.getInstance("SHA256WithRSA", "BC");
            signer.initVerify(pk);
            signer.update(data);
            return signer.verify(sig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
