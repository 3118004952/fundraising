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

//    @Override
//    public boolean equals(Object obj) {
//        if(!(obj instanceof Pointer)){
//            return false;
//        }
//        Pointer p=(Pointer) obj;
//        //必须交易id相等 并且第n个输出单元也相等
//        return (p.txId.equals(txId)&&p.n==n);
//    }

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
