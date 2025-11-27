package com.sunny.pattern.deisgn;

import java.util.*;

class LFUCache {

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1); //1==1
        cache.put(2, 2); // 1=1,2=2
        System.out.println("first get: "+cache.get(1));
        cache.put(3, 3);// 2=2,3=3
        System.out.println("second get: "+ cache.get(2));
        System.out.println("third get: "+ cache.get(3));
        cache.put(4, 4); // 3=3,4=4
        System.out.println("fourth get: "+ cache.get(1));
        System.out.println( cache.get(3));
        System.out.println(cache.get(4));
    }
    private int capacity =2 ;
    Map<Integer,Integer> keytoval ;
    Map<Integer,Integer> keytofreq;
    Map<Integer, LinkedHashSet<Integer>> freqtokey;
    private int minfreq;

    public LFUCache(int capacity) {
            capacity = this.capacity;
            keytoval = new HashMap<>();
            keytofreq = new HashMap<>();
            freqtokey = new HashMap<>();
            minfreq=0;
    }


    public int get(int key) {
        //check key exist
        if(!keytoval.containsKey(key) || capacity == 0)return -1;

        //update freq
        int oldfreq = keytofreq.get(key);
        int newfreq = oldfreq + 1;
        keytofreq.put(key, newfreq);
        //get old keyset
        LinkedHashSet<Integer> oldset = freqtokey.get(oldfreq);
        oldset.remove(key);
        if(oldset.isEmpty()) {
            if(oldfreq == minfreq){
                minfreq=newfreq;
            }
            freqtokey.remove(oldfreq);
        }

        freqtokey.computeIfAbsent(newfreq, f->new LinkedHashSet<>()).add(key);
        return keytoval.get(key);

    }



    public void put(int key, int value) {
        if(keytoval.containsKey(key) ){
            keytoval.put(key,value);
            get(key);
            return;
        }
        if(keytoval.size() >= capacity){
            evictLFU();
        }

        minfreq=1;
        keytoval.put(key,value);
        keytofreq.put(key,1);
        freqtokey.computeIfAbsent(1,f-> new LinkedHashSet<>()).add(key);

    }

    private void evictLFU() {
        //get min freq set
        LinkedHashSet<Integer> minfreqset = freqtokey.get(minfreq);
        int minfreqkey = minfreqset.iterator().next();
        minfreqset.remove(minfreqkey);
        keytofreq.remove(minfreqkey);
        keytoval.remove(minfreqkey);
    }


   }
/*
first get: 1
printset:[1, 2]
second get: 2
third get: 3
printset:[2, 3]
fourth get: -1
3
4
 */