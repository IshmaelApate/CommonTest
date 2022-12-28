package com.ishmael.util.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public interface RedisDao {

    RedisTemplate<String, Object> getTemplate();

    /***
     *
     * 判断key是否存在
     * @param key
     * @return
     */
    Boolean exists(String key);

    /***
     * 模糊查询相关keys
     * @param pattern 模糊条件
     * @return
     */
    Set<String> getObscureKeys(String pattern);

    /***
     * 设置key的有效时间
     * @param key 键
     * @param timeout 时间
     * @param unit 时间单位
     * @return
     */
    Boolean expire(String key, long timeout, TimeUnit unit);

    /***
     * 设置key的有效时间
     * @param key 键
     * @param date 时间
     * @return
     */
    Boolean expireAt(String key, Date date);

    /**
     * 缓存set值
     * @param key 键
     * @param value 值
     * @param seconds 缓存时间，不设置则为0,单位秒
     */
    void set(String key, Object value, long seconds);

    /**
     * 缓存set值
     * @param key 键
     * @param value 值
     * @param seconds 缓存时间，不设置则为0,单位秒
     */
    void setex(String key, Object value, long seconds);

    /**
     * 缓存set值
     *
     * @param key      键
     * @param value    值
     * @param time     缓存时间，不设置则为0
     * @param timeUnit 时间单位
     */
    void set(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * 缓存set值
     */
    void set(String key, Object value);

    void setList(List<Map<String, Object>> list);

    /***
     * 设置值自增值
     * @param key 键
     * @param value 自增值
     * @return
     */
    Long increment(String key, long value);

    /**
     * 设置值自增1
     *
     * @param key 键
     * @return
     */
    Long autoIncrementOne(String key);

    /***
     * 获取缓存值
     * @param key 缓存key
     */
    Object get(String key);

    /**
     * @Description 批量获取 (注[wuxiaohui]：只能批量获取hash表里面的属性)
     * @author xiangchao
     * @createDate 2018/11/15 16:18
     */
    List<Object> multiGet(Collection keys);

    /***
     * 删除KEY
     * @param key
     */
    Boolean delete(String key);

    Boolean deleteList(List<String> keys);

    /***
     * 获取hash表所有键值对
     * @param key
     * @return
     */
    Map<Object, Object> hashgetAll(String key);

    /***
     * 判断hash表是否存在字段
     * @param key
     * @param field
     * @return
     */
    boolean hashexists(String key, String field);

    /****
     * 获取hash表中的所有键
     * @param key
     * @return
     */
    Set<Object> hashFields(String key);


    void hashPutAll(String key, Map<String, Object> map);

    /***
     * 添加数据至hash表中
     * @param key
     * @param field
     * @param value
     */
    void hashset(String key, String field, Object value);

    /***
     * 获取hash中的某个键的值
     * @param key
     * @param field
     * @return
     */
    Object hashget(String key, String field);

    /***
     * 获取hash中的某个键的值
     * @param key
     * @param field
     * @return
     */
    Object hashget(String key, Object field);

    /****
     * 删除hash中的多个键
     * @param key
     * @param fields
     * @return
     */
    long hashdel(String key, String... fields);

    /***
     * 设置哈希字段自增
     * @param key 键
     * @param field hash字段 String 类型
     * @param value 自增值
     * @return
     */
    long hashIncrement(String key, String field, long value);

    /***
     * 设置哈希字段自增
     * @param key 键
     * @param field hash字段 Object 类型
     * @param value 自增值
     * @return
     */
    long hashIncrement(String key, Object field, long value);

    /***
     * 设置哈希字段自增1
     * @param key 键
     * @param field hash字段 String 类型
     * @return
     */
    long hashAutoIncrementOne(String key, String field);

    /**
     * 设置哈希字段自增1
     *
     * @param key   键
     * @param field hash字段 Object 类型
     * @return
     */
    long hashAutoIncrementOne(String key, Object field);

    /***
     * 从左边添加list
     * @param key 键
     * @param value 值
     */
    void listLeftPush(String key, Object value);


    /****
     * 从左边添加list
     * @param key 键
     * @param value 值
     * @param time 过期时间，单位秒
     */
    void listLeftPush(String key, Object value, long time);

    /***
     * 从右边添加list
     * @param key 键
     * @param value 值
     */
    void listRightPush(String key, Object value);

    /***
     * 从右边添加list
     * @param key 键
     * @param value 值
     * @param time 过期时间，单位秒
     */
    void listRightPush(String key, Object value, long time);

    /****
     * 从左边添加list集合
     * @param key 键
     * @param list 值
     */
    void listLeftPushAll(String key, List<?> list);

    /***
     * 从左边添加list集合
     * @param key 键
     * @param list 值
     * @param time 过期时间，单位秒
     */
    void listLeftPushAll(String key, List<?> list, long time);

    /***
     * 从右边添加list集合
     * @param key 键
     * @param list 值
     */
    void listRightPushAll(String key, List<?> list);

    /***
     * 从右边添加list集合
     * @param key 键
     * @param list 值
     * @param time 过期时间，单位秒
     */
    void listRightPushAll(String key, List<?> list, long time);

    /****
     * 获取list长度
     * @param key
     * @return
     */
    long listSize(String key);

    /***
     * 获取list的内容 （0 到 -1 代表所有值）
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return
     */
    List<Object> listGetValue(String key, long start, long end);

    /****
     * 通过索引 获取list中的值
     * （index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推）
     * @param key 键
     * @param index 索引
     * @return
     */
    Object listGetValue(String key, long index);

    /***
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    void listUpdateIndex(String key, long index, Object value);

    /***
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    long listRemove(String key, long count, Object value);

    /***
     * 删除整个list
     * @param key 键
     */
    void listRemoveAll(String key);


    /****
     * 获取lis中的所有值
     * @param key 键
     * @return
     */
    List<Object> listGetAllValue(String key);

    /***
     * 根据key获取Set中的所有值
     * @param key 键
     * @return set集合所有值
     */
    Set<?> setGetAllValue(String key);

    /***
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false 不存在
     */
    boolean setHasKey(String key, Object value);

    /***
     * 将数据放入set集合
     * @param key 键
     * @param values 值 可以多个
     * @return 成功个数
     */
    long setSet(String key, Object... values);

    /***
     * 将数据放入set集合
     * @param key 键
     * @param time 失效时间，单位秒
     * @param values 值 可以多个
     * @return 成功个数
     */
    long setSet(String key, long time, Object... values);

    /***
     * 获取set集合长度
     * @param key 键
     * @return
     */
    long setGetSize(String key);

    /***
     * 移除值为value的元素
     * @param key 键
     * @param values 值 可以多个
     * @return 移除个数
     */
    long setRemove(String key, Object... values);

    /***
     * 移除整个set集合
     * @param key 键
     */
    void setRemoveAll(String key);

    /**
     * @param
     * @Description 获取给定pattern的keys
     * @author xiangchao
     * @createDate 2018/11/15 15:45
     */
    Set<String> keys(String pattern);

    /***
     * 发送消息队列
     * @param topicName 队列名称
     * @param message 消息
     */
    void setMessageInTopic(String topicName, Object message);
}
