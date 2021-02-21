package com.gdut.fundrasing.Service.impl;

import com.gdut.fundrasing.*;
import com.gdut.fundrasing.Service.BlockChainService;
import com.gdut.fundrasing.Service.MerkleTreeService;
import com.gdut.fundrasing.Service.TransactionService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class BlockChainServiceImplTest {

    @InjectMocks
    BlockChainServiceImpl blockChainService;

    @Mock
    TransactionService transactionService;

    @Mock
    MerkleTreeService merkleTreeService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCandidateBlock() {
        List<Transaction> txs=new ArrayList<>();
        txs.add(buildTransaction(false,0,new Date()));
        txs.add(buildTransaction(true,1,new Date()));
        Block block=new Block();
        block.setHash("dsfsdfsd21e12r12rqwrqrqewr");
        block.setHeight(1);

        when(merkleTreeService.getMerkleRoot(any())).thenReturn("23r4dafdsfsdf32423432432refdfsdf");
        Block result= blockChainService.createCandidateBlock(txs,block);
        Assert.assertEquals(result.getHeight(),block.getHeight()+1);
        Assert.assertEquals(result.getMerkleRootHash(),"23r4dafdsfsdf32423432432refdfsdf");
        Assert.assertEquals(result.getPreBlockHash(),"dsfsdfsd21e12r12rqwrqrqewr");
        Assert.assertTrue(result.getHash().equals(Sha256Util.doubleSHA256(result.getHeader())));
    }

    @Test
    public void testVerifyBlock() {
        Block block=new Block();
        List<Transaction> txs=new ArrayList<>();
        txs.add(buildTransaction(false,0,new Date()));
        txs.add(buildTransaction(true,1,new Date()));
        KeyPair keyPair = EccUtil.generateKeys();
        Pointer pointer=new Pointer();
        Vin vin=new Vin();
        Vin vin1=new Vin();
        txs.get(0).getInList().add(vin);
        txs.get(1).getInList().add(vin1);
        block.setTxs(txs);
        when(transactionService.verifyTransaction(any(),any())).thenReturn(true);
        boolean result= blockChainService.verifyBlock(new Peer(),block);
        Assert.assertTrue(result);

        txs.get(1).getInList().add(vin);
        result= blockChainService.verifyBlock(new Peer(),block);
        Assert.assertFalse(result);

    }

    private Transaction buildTransaction(boolean coinBase, long fee, Date lockTime){
        Transaction transaction =new Transaction();
        transaction.setLockTime(lockTime);
        transaction.setFee(fee);
        transaction.setCoinBase(coinBase);
        transaction.setId(Sha256Util.doubleSHA256(transaction.toString()));
        return  transaction;
    }
}