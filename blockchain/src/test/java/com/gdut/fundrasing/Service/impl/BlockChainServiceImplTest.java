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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class BlockChainServiceImplTest {

    @InjectMocks
    BlockChainServiceImpl blockChainService;

    @Mock
    TransactionService transactionService;

    @Mock
    MerkleTreeService merkleTreeService;


    MerkleTreeServiceImpl merkleTreeServiceImpl=new MerkleTreeServiceImpl();

    TransactionServiceImpl transactionServiceImpl=new TransactionServiceImpl();


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

    @Test
    public void testAddBlockToChain() {
        List<Block> blockChain=new ArrayList<>();
        Block block1=buildBlock(0,null);
        Block block2=buildBlock(1,block1.getPreBlockHash());
        blockChain.add(block1);
        blockChain.add(block2);

        Peer peer=new Peer();
        peer.setBlockChain(blockChain);

        Block block3=buildBlock(2,block2.getHash());

        boolean result= blockChainService.addBlockToChain(peer,block3);
        Assert.assertTrue(result);

    }

    private Block buildBlock(int height,String preHash){
        Block block=new Block();
        List<Transaction> txs=new ArrayList<>();
        txs.add(buildTransaction(false,0,new Date()));
        txs.add(buildTransaction(true,1,new Date()));
        block.setTxs(txs);

        block.setMerkleRootHash(merkleTreeServiceImpl.getMerkleRoot(txs));
        block.setHeight(height);
        block.setPreBlockHash(preHash);
        block.setVersion(BlockChainConstant.VERSION);
        block.setTime(new Date());
        block.setHash(Sha256Util.doubleSHA256(block.getHeader()));
        return block;
    }


    private Transaction buildTransaction(boolean coinBase, long fee, Date lockTime){
        Transaction transaction =new Transaction();
        transaction.setLockTime(lockTime);
        transaction.setFee(fee);
        transaction.setCoinBase(coinBase);
        transaction.setId(Sha256Util.doubleSHA256(transaction.toString()));
        return  transaction;
    }


    private Peer getPeer() {
        Peer peer = new Peer();
        Wallet wallet = new Wallet();
        wallet.generateKeyAndAddress();
        peer.setWallet(wallet);
        return peer;
    }

    private UTXO getUTXO(PublicKey pk, String address) {
        Pointer pointer = new Pointer();
        UTXO utxo = new UTXO();
        Vout vout = new Vout();

        pointer.setN(1);
        pointer.setTxId(generateRandomStr(32));

        vout.setMoney(10000L);
        vout.setToAddress(address);

        utxo.setPointer(pointer);
        utxo.setConfirmed(true);
        utxo.setSpent(false);
        utxo.setVout(vout);

        return utxo;
    }

    /**
     * 随机产生一个length长度的a-Z和0-9混合字符串
     *
     * @param length 长度
     * @return 随机字符串
     */
    private String generateRandomStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


}