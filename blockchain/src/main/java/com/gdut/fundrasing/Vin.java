package com.gdut.fundrasing;

/**
 * 交易输入单元
 */
public class Vin {
    /**
     * 交易创建者的数字签名
     */
    byte[] signature;

    /**
     * 交易创建者者的公钥
     */
    private byte[] publicKey;

    /**
     * 指向交易创建者的UTXO
     */
    private Pointer toSpent;

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public Pointer getToSpent() {
        return toSpent;
    }

    public void setToSpent(Pointer toSpent) {
        this.toSpent = toSpent;
    }
}
