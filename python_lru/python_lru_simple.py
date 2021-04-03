from collections import OrderedDict


class LRU(object):
    """
    LRU Cache
    """
    def __init__(self, cache_size=128):
        self.cache_size = cache_size
        self.cache = OrderedDict()

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


test()
