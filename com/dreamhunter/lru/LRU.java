package com.dreamhunter.lru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 链表节点
class LRUNode<K, V> {

    public K key;
    public V value;
    public LRUNode<K, V> pre;
    public LRUNode<K, V> post;

    public LRUNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public LRUNode(K key, V value, LRUNode<K, V> pre, LRUNode<K, V> post) {
        this.key = key;
        this.value = value;
        this.pre = pre;
        this.post = post;
    }

    public String toString() {
        return String.join("=", String.valueOf(key), String.valueOf(value));
    }
}

// 链表
class LinkedLRUNodes<K, V> {

    // 缓存数据
    private HashMap<K, LRUNode<K, V>> data;
    // 链表头尾
    private LRUNode<K, V> root;
    private LRUNode<K, V> tail;

    // 初始化链表
    public LinkedLRUNodes() {
        this.data = new HashMap<>();
        this.root = new LRUNode<>(null, null);
        this.tail = new LRUNode<>(null, null);
        this.root.post = this.tail;
        this.tail.pre = this.root;
    }

    // 获取值时更新顺序
    public V get(K key) {
        LRUNode<K, V> curNode = data.get(key);
        LRUNode<K, V> preNode = curNode.pre;
        LRUNode<K, V> postNode = curNode.post;

        // 将当前节点插入末尾节点前
        curNode.pre = tail.pre;
        curNode.post = tail;
        tail.pre.post = curNode;
        tail.pre = curNode;

        // 将移动 curNode 断开的链表连接起来
        preNode.post = postNode;
        postNode.pre = preNode;

        return curNode.value;
    }

    // 新增或更新 key, 新建节点插入末尾节点前
    public void put(K key, V value) {
        // 删除旧节点
        remove(key);
        LRUNode<K, V> lastNode = tail.pre;
        LRUNode<K, V> curNode = new LRUNode<>(key, value, lastNode, tail);
        lastNode.post = curNode;
        tail.pre = curNode;
        data.put(key, curNode);
    }

    // 删除节点
    public void remove(K key) {
        LRUNode<K, V> curNode = data.get(key);
        removeNode(curNode);
    }

    // 删除头节点后的第一个
    public void removeHead() {
        removeNode(root.post);
    }

    // 删除某个节点
    public void removeNode(LRUNode<K, V> curNode) {
        if (curNode == null) return;

        LRUNode<K, V> preNode = curNode.pre;
        LRUNode<K, V> postNode = curNode.post;

        if (preNode == null || postNode == null) return;

        // 将此节点前后节点连接起来
        preNode.post = postNode;
        postNode.pre = preNode;
        // 移除节点数据
        data.remove(curNode.key);

    }

    public int size() {
        return data.size();
    }

    public String toString() {
        List<String> res = new ArrayList<>();
        LRUNode<K, V> curNode = root.post;
        while (curNode.post != null) {
            if (curNode.key != null)
                res.add(curNode.toString());
            curNode = curNode.post;
        }
        return "{" + String.join(", ", res) + "}";
    }
}


public class LRU<K, V> {

    private int cacheSize;
    private LinkedLRUNodes<K, V> linkedLRUNodes;

    public LRU(int cacheSize) {
        this.cacheSize = cacheSize;
        this.linkedLRUNodes = new LinkedLRUNodes<>();
    }

    public static void main(String[] args) {
        LRU<Integer, Integer> testLRU = new LRU<>(4);
        testLRU.put(1, 1);
        System.out.println(testLRU);
        testLRU.put(2, 2);
        System.out.println(testLRU);
        testLRU.put(3, 3);
        System.out.println(testLRU);
        testLRU.put(4, 4);
        System.out.println(testLRU);
        testLRU.put(5, 5);
        System.out.println(testLRU);
        testLRU.put(4, 4);
        System.out.println(testLRU);
        testLRU.get(3);
        System.out.println(testLRU);
    }

    private void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public void get (K key) {
        linkedLRUNodes.get(key);
    }

    public void put(K key, V value) {
        linkedLRUNodes.put(key, value);
        if (linkedLRUNodes.size() > cacheSize) {
            linkedLRUNodes.removeHead();
        }
    }

    public String toString() {
        return linkedLRUNodes.toString();
    }
}