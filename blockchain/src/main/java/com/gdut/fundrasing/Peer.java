package com.gdut.fundrasing;

import java.util.HashMap;


/**
 * 区块链节点
 */
public class Peer {
    /**
     * utxo 哈希集
     */
    private HashMap<Pointer,UTXO> UTXOHashMap;

    /**
     * 用户自身的utxo，方便计算余额
     */
    private HashMap<Pointer,UTXO> ownUTXOHashMap;

    /**
     * 钱包
     */
    private Wallet wallet;

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public HashMap<Pointer, UTXO> getUTXOHashMap() {
        return UTXOHashMap;
    }

    public void setUTXOHashMap(HashMap<Pointer, UTXO> UTXOHashMap) {
        this.UTXOHashMap = UTXOHashMap;
    }

    public HashMap<Pointer, UTXO> getOwnUTXOHashMap() {
        return ownUTXOHashMap;
    }

    public void setOwnUTXOHashMap(HashMap<Pointer, UTXO> ownUTXOHashMap) {
        this.ownUTXOHashMap = ownUTXOHashMap;
    }
}
