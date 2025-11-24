package com.sunny.pattern.deisgn;

import java.util.*;

class LFUCache {

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println( cache.get(2));
        System.out.println( cache.get(3));
        cache.put(4, 4);
        System.out.println( cache.get(1));
        System.out.println( cache.get(3));
        System.out.println(cache.get(4));
    }
    int capacity =2;
    Map<Integer,Integer> keytoval ;
    Map<Integer,Integer> keytofreq;
    Map<Integer, LinkedHashSet<Integer>> freqtokey;
    int minfreq;

    public LFUCache(int capacity) {
           // capacity = this.capacity;
            keytoval = new HashMap<>();
            keytofreq = new HashMap<>();
            freqtokey = new HashMap<>();
            minfreq=0;
    }


    public int get(int key) {
       if(!keytoval.containsKey(key)) return -1;
       int freq = keytofreq.get(key);
        if(freq == minfreq && freqtokey.get(key).isEmpty()) {
            freqtokey.remove(freq);
            minfreq++;
        }
        keytofreq.put(key,keytofreq.getOrDefault(key,0)+1);
        freqtokey.get(freq).remove(key);
        updateFreq(1+freq,key);


        return  keytoval.get(key);
    }

    private void updateFreq(Integer freq, int key) {
        LinkedHashSet<Integer> set = freqtokey.get(freq);
        if(set == null){
            set = new LinkedHashSet<>();
            set.add(key);
        }
        freqtokey.put(freq,set);
    }

    public void put(int key, int value) {
        if(keytoval.containsKey(key)){
            keytoval.put(key,value);
            get(key);
            return;
        }
            if(capacity == keytoval.size()){
                evictLFUKey();
            }
            minfreq=1;
            putFreq(minfreq,key);
            keytoval.put(key,value);
    }

    public void evictLFUKey(){
         LinkedHashSet<Integer> set  = freqtokey.get(minfreq);
         if(set == null) return;
         int key = set.iterator().next();
         set.remove(key);
         keytofreq.remove(key);
         keytoval.remove(key);

    }
    public void putFreq(int freq,int key){
        keytofreq.put(key,freq);
        freqtokey.putIfAbsent(freq,new LinkedHashSet<>());
        freqtokey.get(freq).add(key);

    }}
