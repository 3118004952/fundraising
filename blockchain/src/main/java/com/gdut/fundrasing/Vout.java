package com.gdut.fundrasing;

/**
 * 交易输出单元
 */
public class Vout {
    /**
     * 交易输出地址
     */
    private String toAddress;

    /**
     * 交易金额
     */
    private long money;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
