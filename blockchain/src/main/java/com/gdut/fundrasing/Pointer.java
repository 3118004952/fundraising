package com.gdut.fundrasing;

/**
 * 交易定位指针
 */
public class Pointer {
    /**
     * 交易id
     */
    private String txId;
    /**
     * 交易的第n个输出单元
     */
    private long n;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }
}
