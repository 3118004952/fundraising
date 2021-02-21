package com.gdut.fundrasing.Service.impl;

import com.gdut.fundrasing.*;
import com.gdut.fundrasing.Service.BlockChainService;
import com.gdut.fundrasing.Service.MerkleTreeService;
import com.gdut.fundrasing.Service.TransactionService;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ComponentScan(value = "com.gdut.fundrasing")
public class BlockChainServiceImpl implements BlockChainService {

    MerkleTreeService merkleTreeService;


    TransactionService transactionService;

    /**
     * 创建候选区块
     * @param txs
     * @param preBlock
     * @return
     */
    @Override
    public Block createCandidateBlock(List<Transaction> txs, Block preBlock) {
        Block block=new Block();
        block.setPreBlockHash(preBlock.getHash());
        block.setHeight(preBlock.getHeight()+1);
        block.setTime(new Date());
        block.setTxs(txs);
        block.setVersion(BlockChainConstant.VERSION);
        block.setMerkleRootHash(merkleTreeService.getMerkleRoot(txs));
        block.setHash(Sha256Util.doubleSHA256(block.getHeader()));
        return block;
    }

    /**
     * 校验区块
     * @param peer
     * @param block
     * @return
     */
    @Override
    public boolean verifyBlock(Peer peer, Block block) {
        //验证该区块中是否存在多重支付
        if(doublePayCheck(block.getTxs())){
            return false;
        }

        //验证每一笔交易
        for(Transaction transaction:block.getTxs()){
            if(!transactionService.verifyTransaction(peer,transaction)){
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否存在多重支付
     * @param txs
     * @return
     */
    private boolean doublePayCheck(List<Transaction> txs){
        List<Vin> vinList=new ArrayList<>();
        Set<Vin> vinSet=new HashSet<>();

        for(Transaction tx:txs){
             vinList.addAll(tx.getInList());
             vinSet.addAll(tx.getInList());
        }

        //如果不存在多重支付，那么用set去重的结果肯定跟list的长度一样
        return vinList.size() !=vinSet.size();
    }

    public void setMerkleTreeService(MerkleTreeService merkleTreeService) {
        this.merkleTreeService = merkleTreeService;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
