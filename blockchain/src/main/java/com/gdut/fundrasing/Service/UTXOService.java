package com.gdut.fundrasing.Service;

import com.gdut.fundrasing.Pointer;
import com.gdut.fundrasing.UTXO;

import java.util.HashMap;
import java.util.List;

public interface UTXOService {
    /**
     * 根据定位指针删除对应的utxo集合
     * @param utxoHashMap
     * @param pointerList
     * @return
     */
    HashMap<Pointer, UTXO> deleteUTXOByPointer(HashMap<Pointer, UTXO> utxoHashMap, List<Pointer> pointerList);

    void addUTXOToMap(HashMap<Pointer,UTXO>utxoHashMap,List<UTXO> utxoList);

}
