package com.gdut.fundrasing.Service;

import com.gdut.fundrasing.Transaction;

import java.util.List;

/**
 * 梅克尔树加密服务
 */
public interface MerkleTreeService {
    String getMerkleRoot(List<Transaction> txs);
}
