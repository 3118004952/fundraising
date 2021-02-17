package com.gdut.fundrasing.Service.impl;

import com.gdut.fundrasing.*;
import com.gdut.fundrasing.BlockChainConstant;
import com.gdut.fundrasing.Service.TransactionService;

import org.springframework.util.ObjectUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TransactionServiceImpl implements TransactionService {

    private static String ALGORITHM_NAME = "SHA256withECDSA";

    /**
     * @param peer
     * @param toAddress
     * @param money
     * @return
     */
    @Override
    public Transaction createTransaction(Peer peer, String toAddress, long money) {
        long balance = peer.getBalance();
        //余额不足
        if (balance < money) {
            return null;
        }
        List<UTXO> ownUTXOList = new ArrayList<>();
        long amount = 0;
        //把相关的输入单元加入到交易中
        for (UTXO utxo : peer.getOwnUTXOHashMap().values()) {
            //如果已经消费了则跳过
            if (utxo.isSpent()) {
                continue;
            }
            amount += utxo.getVout().getMoney();
            ownUTXOList.add(utxo);
            if (amount >= money) {
                break;
            }
        }

        Transaction transaction = new Transaction();
        Wallet ownWallet = peer.getWallet();
        Vout other = new Vout();
        //要减去手续费
        other.setMoney(money - BlockChainConstant.FEE);
        other.setToAddress(toAddress);
        transaction.getOutList().add(other);

        //如果剩余金额大于所需支付金额，那么会将多一笔vout，把多的钱给自己，找零
        if (amount > money) {
            Vout ownOut = new Vout();
            ownOut.setMoney(amount - money);
            //设置最新的地址
            ownOut.setToAddress(ownWallet.getAddress().get(ownWallet.getAddress().size() - 1));
            transaction.getOutList().add(ownOut);
        }

        //之前在ownUTXOList里的utxo将变成输入单元
        for (UTXO utxo : ownUTXOList) {
            String address = utxo.getVout().getToAddress();
            int index = -1;
            for (int i = 0; i < ownWallet.getAddress().size(); ++i) {
                if (address.equals(ownWallet.getAddress().get(i))) {
                    index = i;
                    break;
                }
            }
            //找不到该地址，该地址不合法
            if (index == -1) {
                return null;
            }
            //根据钱包地址找出对应的公钥和私钥
            PublicKey publicKey = ownWallet.getKeyPairList().get(index).getPublic();
            PrivateKey privateKey = ownWallet.getKeyPairList().get(index).getPrivate();

            Vin vin = new Vin();
            //设置公钥
            vin.setPublicKey(publicKey);
            //设置定位指针
            vin.setToSpent(utxo.getPointer());
            //设置待加密文本
            String s = utxo.getPointer().getTxId() + utxo.getPointer().getN() + transaction.getOutList();
            //得到加密内容
            byte[] data = EccUtil.buildMessage(s);
            try {
                //产生数字签名
                byte[] signature = EccUtil.signData(ALGORITHM_NAME, data, privateKey);
                vin.setSignature(signature);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //加入到交易的输入单元
            transaction.getInList().add(vin);
        }

        //区块都要标记为已被消费
        for (UTXO utxo : ownUTXOList) {
            utxo.setSpent(true);
        }
        //创建交易id,由交易内容的toString获取得到
        transaction.setId(Sha256Util.doubleSHA256(transaction.toString()));
        peer.getTransactionPool().put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public boolean verifyTransaction(Peer peer, Transaction transaction) {
        //校验基本信息
        if (!verifyBaseMsg(transaction)) {
            return false;
        }
        //校验是否存在双重支付
        if (verifyDoublePayment(peer, transaction)) {
            return false;
        }
        for (Vin vin : transaction.getInList()) {
            //查找该输入单元对应的utxo，如果不存在该输入单元的utxo，则加入到孤立交易池中
            UTXO utxo = peer.getUTXOHashMap().get(vin.getToSpent());
            if(utxo==null){
                peer.getOrphanPool().put(vin.getToSpent().getTxId(),transaction);
                return false;
            }
            //校验地址跟数字签名
            if (!verifyAddressAndSignature(vin, utxo, transaction.getOutList())) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyAddressAndSignature(Vin vin, UTXO utxo, List<Vout> voutList) {
        //根据输入单元的公钥生成地址
        String address = EccUtil.generateAddress(vin.getPublicKey().getEncoded());
        //如果地址不匹配，则校验失败
        if (!address.equals(utxo.getVout().getToAddress())) {
            return false;
        }
        boolean result = false;
        try {
            String s = utxo.getPointer().getTxId() + utxo.getPointer().getN() + voutList;
            //校验数字签名
            result = EccUtil.verifySign(ALGORITHM_NAME, EccUtil.buildMessage(s),
                    vin.getPublicKey(), vin.getSignature());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 校验基本信息
     *
     * @param transaction
     * @return
     */
    private boolean verifyBaseMsg(Transaction transaction) {
        //输入或者输出单元任一为空都是不符合标准要求
        if (ObjectUtils.isEmpty(transaction.getInList()) ||
                ObjectUtils.isEmpty(transaction.getOutList())) {
            return false;
        }
        return true;
    }

    /**
     * 校验是否存在双重支付
     *
     * @param peer
     * @param transaction
     * @return
     */
    private boolean verifyDoublePayment(Peer peer, Transaction transaction) {
        //判断交易池中是否已经存在该笔交易
        if (peer.getTransactionPool().containsKey(transaction.getId())) {
            return true;
        }

        //判断该交易单元的输入单元是否存在交易池中
        for (Vin vin : transaction.getInList()) {
            for (Transaction tx : peer.getTransactionPool().values()) {
                if (tx.getInList().contains(vin)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * @param peer
     * @param toAddress
     * @param money
     * @return
     */
    @Override
    public Transaction createCoinBaseTransaction(Peer peer, String toAddress, long money) {
        Transaction transaction = new Transaction();
        Vin vin = new Vin();
        List<Vin> vinList = new ArrayList<>();
        Vout vout = new Vout();
        List<Vout> voutList = new ArrayList<>();
        //设置地址
        vout.setToAddress(peer.getWallet().getAddress().get(peer.getWallet().getAddress().size() - 1));
        vout.setMoney(BlockChainConstant.INIT_MONEY);
        voutList.add(vout);
        vinList.add(vin);
        //创币交易没有输入单元，但为了保持一致，随便填充一个输入单元，签名默认使用32位随机字符串
        vin.setSignature(generateRandomStr(32).getBytes());
        //没有公钥
        vin.setPublicKey(null);
        transaction.setInList(vinList);
        transaction.setOutList(voutList);
        //创币交易
        transaction.setCoinBase(true);
        transaction.setId(Sha256Util.doubleSHA256(transaction.toString()));

        return transaction;
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
