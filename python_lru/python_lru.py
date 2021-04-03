class Node(object):
    """链表节点"""

    def __init__(self, key, value, pre=None, post=None):
        self.key = key
        self.value = value
        # 上一节点
        self.pre = pre
        # 下一节点
        self.post = post

    def __repr__(self):
        return "=".join([repr(self.key), repr(self.value)])


class LinkedNodes(object):
    """缓存链表"""

    def __init__(self):
        # 存储在 dict 中
        self.data = {}
        # 首部节点
        self.root = Node(None, None)
        # 尾部节点
        self.tail = Node(None, None)
        # 初始化，头尾连接起来
        self.root.post = self.tail
        self.tail.pre = self.root

    def __setitem__(self, key, value):
        # 移除 key
        self.pop(key, None)
        # 新建节点并插入尾部节点前
        last_node = self.tail.pre
        self.data[key] = Node(key, value, last_node, self.tail)
        last_node.post = self.data[key]
        self.tail.pre = self.data[key]

    def pop(self, key, default=None):
        # 移除 key
        cur_node = self.data.pop(key, None)
        if not cur_node:
            return default
        # 将移除 key 的前后节点连起来
        pre_node = cur_node.pre
        post_node = cur_node.post
        pre_node.post = post_node
        post_node.pre = pre_node
        return cur_node.value

    def popitem(self, last=True):
        # 移除第一个节点
        cur_node = self.root.post
        pre_node = self.root
        post_node = cur_node.post
        pre_node.post = post_node
        post_node.pre = pre_node
        del self.data[cur_node.key]
        del cur_node

    def __len__(self):
        return len(self.data)

    def __repr__(self):
        res = []
        cur_node = self.root
        while cur_node.post:
            cur_node = cur_node.post
            if cur_node.key:
                res.append(repr(cur_node))
        return "LinkedNodes({" + ", ".join(res) + "})"


class LRU(object):
    """
    LRU Cache
    """
    def __init__(self, cache_size=128):
        self.cache_size = cache_size
        self.cache = LinkedNodes()

    def get(self, key):
        """
        获取 key 并更新到最后
        """
        res = self.cache.pop(key, None)
        if res:
            self.cache[key] = res
        return res

    def put(self, key, value):
        """
        更新 key 并放到最后
        长度上限时移除头部
        """
        res = self.cache.pop(key, None)
        if not res and len(self.cache) == self.cache_size:
            self.cache.popitem(False)
        self.cache[key] = value


def test():
    test_cache = LRU(4)
    print(test_cache.cache)
    test_cache.put(1, 1)
    print(test_cache.cache)
    test_cache.put(2, 2)
    print(test_cache.cache)
    test_cache.put(3, 3)
    print(test_cache.cache)
    test_cache.put(4, 4)
    print(test_cache.cache)
    test_cache.put(1, 1)
    print(test_cache.cache)
    test_cache.put(3, 3)
    print(test_cache.cache)
    test_cache.put(5, 5)
    print(test_cache.cache)
    test_cache.get(4)
    print(test_cache.cache)


test()
