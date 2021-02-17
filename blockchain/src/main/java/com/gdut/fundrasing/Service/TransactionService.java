package com.gdut.fundrasing.Service;

import com.gdut.fundrasing.Peer;
import com.gdut.fundrasing.Transaction;

/**
 * 交易服务
 */
public interface TransactionService {
    /**
     * 创建交易
     * @param peer
     * @param toAddress
     * @param money
     * @return
     */
    Transaction createTransaction(Peer peer,String toAddress,long money);

    /**
     * 创币服务
     * @param peer
     * @param toAddress
     * @param money
     * @return
     */
    Transaction createCoinBaseTransaction(Peer peer,String toAddress,long money);
}
