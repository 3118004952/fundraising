package com.gdut.fundrasing;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * 区块链钱包
 */
public class Wallet {
    /**
     * 密钥对列表
     */
    private List<KeyPair> keyPairList;

    /**
     * 钱包地址列表
     */
    private List<String> address;

    public Wallet() {
        keyPairList = new ArrayList<>();
        address = new ArrayList<>();
    }

    /**
     * 产生钱包新的地址跟密钥对
     */
    public void generateKeyAndAddress() {
        KeyPair keyPair = generateKey();
        generateAddress(keyPair.getPublic());
    }

    /**
     * 产生密钥对
     */
    private KeyPair generateKey() {
        KeyPair keyPair = EccUtil.generateKeys();
        keyPairList.add(keyPair);
        return keyPair;
    }

    /**
     * 产生新的钱包地址
     *
     * @param publicKey
     */
    private void generateAddress(PublicKey publicKey) {
        address.add(EccUtil.generateAddress(publicKey.getEncoded()));
    }

    public List<KeyPair> getKeyPairList() {
        return keyPairList;
    }

    public void setKeyPairList(List<KeyPair> keyPairList) {
        this.keyPairList = keyPairList;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
