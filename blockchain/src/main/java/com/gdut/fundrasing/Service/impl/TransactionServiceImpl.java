package com.gdut.fundrasing.Service.impl;

import com.gdut.fundrasing.Peer;
import com.gdut.fundrasing.Service.TransactionService;
import com.gdut.fundrasing.Transaction;

public class TransactionServiceImpl implements TransactionService {

    /**
     *
     * @param peer
     * @param toAddress
     * @param money
     * @return
     */
    @Override
    public Transaction createTransaction(Peer peer, String toAddress, long money) {
        return null;
    }

    /**
     *
     * @param peer
     * @param toAddress
     * @param money
     * @return
     */
    @Override
    public Transaction createCoinBaseTransaction(Peer peer, String toAddress, long money) {
        return null;
    }
}
