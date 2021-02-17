package com.gdut.fundrasing.Service.impl;

import com.gdut.fundrasing.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {

    TransactionServiceImpl transactionService = new TransactionServiceImpl();

    @Test
    void createTransaction() {
        Peer peer = getPeer();
        Wallet wallet = peer.getWallet();
        HashMap<Pointer, UTXO> utxoMap = new HashMap<>();
        UTXO utxo = getUTXO(wallet.getKeyPairList().
                        get(wallet.getKeyPairList().size() - 1).getPublic(),
                wallet.getAddress().get(wallet.getAddress().size() - 1));
        utxoMap.put(utxo.getPointer(), utxo);

        peer.setUTXOHashMap(utxoMap);

        Peer peerA = getPeer();

        Transaction transaction1 = transactionService.createTransaction(peer, peerA.getWallet().getAddress().get(peerA.getWallet().getAddress().size() - 1), 1000);
        Assert.assertNull(transaction1);

        peer.setOwnUTXOHashMap(utxoMap);
        Transaction transaction = transactionService.createTransaction(peer, peerA.getWallet().getAddress().get(peerA.getWallet().getAddress().size() - 1), 1000);
        Assert.assertNotNull(transaction);

        Assert.assertEquals(transaction.getInList().get(0).getToSpent().getTxId(), utxo.getPointer().getTxId());
        Assert.assertEquals(transaction.getOutList().get(0).getToAddress(), peerA.getWallet().getAddress().get(0));
        Assert.assertEquals(transaction.getOutList().get(0).getMoney(), 1000 - BlockChainConstant.FEE);

        Assert.assertEquals(transaction.getOutList().get(1).getToAddress(), peer.getWallet().getAddress().get(0));

        UTXO utxo1 = getUTXO(peer.getWallet().getKeyPairList().get(0).getPublic(), peer.getWallet().getAddress().get(0));
        peer.getOwnUTXOHashMap().put(utxo1.getPointer(), utxo1);

        transaction = transactionService.createTransaction(peer, peerA.getWallet().getAddress().get(0), 10001);
        Assert.assertNull(transaction);

        UTXO utxo2 = getUTXO(peer.getWallet().getKeyPairList().get(0).getPublic(), peer.getWallet().getAddress().get(0));
        peer.getOwnUTXOHashMap().put(utxo2.getPointer(), utxo2);
        transaction = transactionService.createTransaction(peer, peerA.getWallet().getAddress().get(0), 10001);
        Assert.assertEquals(transaction.getOutList().get(0).getMoney(), 10001 - BlockChainConstant.FEE);

        Assert.assertEquals(transaction.getOutList().get(1).getMoney(), 20000 - 10001);

    }

    @Test
    void verifyTransaction() {
        Peer peer = getPeer();
        Wallet wallet = peer.getWallet();
        HashMap<Pointer, UTXO> utxoMap = new HashMap<>();
        UTXO utxo = getUTXO(wallet.getKeyPairList().
                        get(wallet.getKeyPairList().size() - 1).getPublic(),
                wallet.getAddress().get(wallet.getAddress().size() - 1));
        utxoMap.put(utxo.getPointer(), utxo);

        peer.setUTXOHashMap(utxoMap);

        Peer peerA = getPeer();
        peer.setOwnUTXOHashMap(utxoMap);
        Transaction transaction = transactionService.createTransaction(peer, peerA.getWallet().getAddress().get(peerA.getWallet().getAddress().size() - 1), 1000);
        Assert.assertNotNull(transaction);
        boolean result=transactionService.verifyTransaction(peer,transaction);
        Assert.assertFalse(result);

        peerA.setUTXOHashMap(utxoMap);

        Assert.assertTrue( transactionService.verifyTransaction(peerA,transaction));
    }

    @Test
    void testCode() {
        UTXO utxo = new UTXO();
        Pointer pointer = new Pointer();
        List<Vout> vouts = new ArrayList<>();
        Vout vout1 = new Vout();
        Vout vout2 = new Vout();

        vout1.setMoney(100L);
        vout1.setToAddress("sdfhsdhfsdgsd132413242");

        vout2.setToAddress("sdfhshf324324");
        vout2.setMoney(20L);

        vouts.add(vout1);
        vouts.add(vout2);

        pointer.setTxId("1434234234");
        pointer.setN(10L);

        utxo.setPointer(pointer);

        String s = utxo.getPointer().getTxId() + utxo.getPointer().getN() + vouts;

        System.out.println(s);

        System.out.println(Sha256Util.getSHA256(s));
    }

    @Test
    void createCoinBaseTransaction() {
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
        //获取公钥
        utxo.setPublicKey(pk);
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