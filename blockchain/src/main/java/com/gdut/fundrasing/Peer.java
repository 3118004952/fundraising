package com.gdut.fundrasing;

import java.util.HashMap;


/**
 * 区块链节点
 */
public class Peer {
    /**
     * utxo 哈希集
     */
    private HashMap<Pointer, UTXO> UTXOHashMap;

    /**
     * 用户自身的utxo，方便计算余额
     */
    private HashMap<Pointer, UTXO> ownUTXOHashMap;

    /**
     * 钱包
     */
    private Wallet wallet;

    /**
     * 孤立交易池
     */
    private HashMap<String, Transaction> orphanPool;

    /**
     * 交易池
     */
    private HashMap<String, Transaction> transactionPool;

    public Peer(){
        UTXOHashMap=new HashMap<>();
        ownUTXOHashMap=new HashMap<>();
        orphanPool=new HashMap<>();
        transactionPool=new HashMap<>();
    }

    public long getBalance() {
        long money = 0;
        for (UTXO utxo : ownUTXOHashMap.values()) {
            if(utxo.isSpent()){
                continue;
            }
            money += utxo.getVout().getMoney();
        }
        return money;
    }


    public HashMap<String, Transaction> getTransactionPool() {
        return transactionPool;
    }

    public void setTransactionPool(HashMap<String, Transaction> transactionPool) {
        this.transactionPool = transactionPool;
    }

    public HashMap<String, Transaction> getOrphanPool() {
        return orphanPool;
    }

    public void setOrphanPool(HashMap<String, Transaction> orphanPool) {
        this.orphanPool = orphanPool;
    }

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
