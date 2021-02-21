package com.gdut.fundrasing.Service.impl;

import com.gdut.fundrasing.Service.MerkleTreeService;
import com.gdut.fundrasing.Sha256Util;
import com.gdut.fundrasing.Transaction;
import com.gdut.fundrasing.Vout;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MerkleTreeServiceImplTest {

    MerkleTreeServiceImpl merkleTreeService=new MerkleTreeServiceImpl();

    @Test
    void getMerkleRoot() {
        List<Transaction> transactions=new ArrayList<>();
        Transaction transaction1=buildTransaction(false,0,new Date());
        Transaction transaction2=buildTransaction(true,1,new Date());
        transactions.add(transaction1);
        transactions.add(transaction2);

        String s= merkleTreeService.getMerkleRoot(transactions);
        System.out.println(s);
        Assert.assertNotNull(s);

        Transaction transaction3=buildTransaction(false,10,new Date());
        transactions.add(transaction3);
        s=merkleTreeService.getMerkleRoot(transactions);

        System.out.println(s);
        Assert.assertNotNull(s);
    }

    private Transaction buildTransaction(boolean coinBase, long fee,Date lockTime){
        Transaction transaction =new Transaction();
        transaction.setLockTime(lockTime);
        transaction.setFee(fee);
        transaction.setCoinBase(coinBase);
        transaction.setId(Sha256Util.doubleSHA256(transaction.toString()));
        return  transaction;
    }
}