package com.gdut.fundrasing;

import java.security.PublicKey;

/**
 * 未消费交易输出模块
 */
public class UTXO {
    /**
     * 未消费输入在区块链中的地址
     */
    private Pointer pointer;

    /**
     * 输出单元
     */
    Vout vout;

    /**
     * 交易创建者者的公钥
     */
    private PublicKey publicKey;

    /**
     * 是否已经被消费
     */
    private boolean isSpent;

    /**
     * 是否被确认
     */
    private boolean isConfirmed;

    public String encodeString(){
        return pointer.getTxId()+pointer.getN()+new String(publicKey.getEncoded());
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    public Vout getVout() {
        return vout;
    }

    public void setVout(Vout vout) {
        this.vout = vout;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public boolean isSpent() {
        return isSpent;
    }

    public void setSpent(boolean spent) {
        isSpent = spent;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
