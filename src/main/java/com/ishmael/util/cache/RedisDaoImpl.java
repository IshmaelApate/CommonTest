package com.ishmael.util.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisDaoImpl implements RedisDao {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public RedisTemplate<String, Object> getTemplate() {
        return redisTemplate;
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Set<String> getObscureKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setList(List<Map<String, Object>> list) {
//        if (StringUtils.hasText(SysConfig.getSysConfig("spring.redis.cluster.nodes"))) {
            list.forEach(map -> redisTemplate.opsForValue().set(map.get("key").toString(), map.get("value")));
//        } else {
            SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    list.forEach(map -> operations.opsForValue().set(map.get("key"), map.get("value")));
                    return operations.exec();
                }
            };
            redisTemplate.execute(sessionCallback);
//        }
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<Object> multiGet(Collection keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void set(String key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void setex(String key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value, long seconds, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, seconds, timeUnit);
    }

    @Override
    public Long increment(String key, long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public Long autoIncrementOne(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Boolean deleteList(List<String> keys) {
        try {
            SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    for (String key : keys) {
                        operations.delete(key);
                    }
                    return operations.exec();
                }
            };
            redisTemplate.execute(sessionCallback);
        } catch (Exception e) {
            return false;
        }
        return true;
        //return redisTemplate.delete(keys);
    }

    @Override
    public Map<Object, Object> hashgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public boolean hashexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public Set<Object> hashFields(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public void hashPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void hashset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public Object hashget(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public Object hashget(String key, Object field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public long hashdel(String key, String... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    @Override
    public long hashIncrement(String key, String field, long value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public long hashIncrement(String key, Object field, long value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public long hashAutoIncrementOne(String key, String field) {
        return redisTemplate.opsForHash().increment(key, field, 1);
    }

    @Override
    public long hashAutoIncrementOne(String key, Object field) {
        return redisTemplate.opsForHash().increment(key, field, 1);
    }

    /***
     * 从左边添加list
     * @param key 键
     * @param value 值
     */
    @Override
    public void listLeftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /****
     * 从左边添加list
     * @param key 键
     * @param value 值
     * @param time 过期时间，单位秒
     */
    @Override
    public void listLeftPush(String key, Object value, long time) {
        redisTemplate.opsForList().leftPush(key, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /***
     * 从右边添加list
     * @param key 键
     * @param value 值
     */
    @Override
    public void listRightPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /***
     * 从右边添加list
     * @param key 键
     * @param value 值
     * @param time 过期时间，单位秒
     */
    @Override
    public void listRightPush(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /****
     * 从左边添加list集合
     * @param key 键
     * @param list 值
     */
    @Override
    public void listLeftPushAll(String key, List<?> list) {
        redisTemplate.opsForList().leftPushAll(key, list);
    }

    /***
     * 从左边添加list集合
     * @param key 键
     * @param list 值
     * @param time 过期时间，单位秒
     */
    @Override
    public void listLeftPushAll(String key, List<?> list, long time) {
        redisTemplate.opsForList().leftPushAll(key, list);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /***
     * 从右边添加list集合
     * @param key 键
     * @param list 值
     */
    @Override
    public void listRightPushAll(String key, List<?> list) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        list.forEach(value -> {
            listOperations.rightPush(key, value);
        });
    }

    /***
     * 从右边添加list集合
     * @param key 键
     * @param list 值
     * @param time 过期时间，单位秒
     */
    @Override
    public void listRightPushAll(String key, List<?> list, long time) {
        redisTemplate.opsForList().rightPushAll(key, list);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /****
     * 获取list长度
     * @param key
     * @return
     */
    @Override
    public long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /***
     * 获取list的内容 （0 到 -1 代表所有值）
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return
     */
    @Override
    public List<Object> listGetValue(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /****
     * 通过索引 获取list中的值
     * （index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推）
     * @param key 键
     * @param index 索引
     * @return
     */
    @Override
    public Object listGetValue(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /***
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    @Override
    public void listUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /***
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /***
     * 删除整个列表
     * @param key
     */
    @Override
    public void listRemoveAll(String key) {
        redisTemplate.delete(key);
    }

    /****
     * 获取lis中的所有值
     * @param key
     * @return
     */
    @Override
    public List<Object> listGetAllValue(String key) {
        return listGetValue(key, 0, -1);
    }


    /***
     * 根据key获取Set中的所有值
     * @param key 键
     * @return set集合所有值
     */
    @Override
    public Set<?> setGetAllValue(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /***
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false 不存在
     */
    @Override
    public boolean setHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /***
     * 将数据放入set集合
     * @param key 键
     * @param values 值 可以多个
     * @return 成功个数
     */
    @Override
    public long setSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /***
     * 将数据放入set集合
     * @param key 键
     * @param time 失效时间，单位秒
     * @param values 值 可以多个
     * @return 成功个数
     */
    @Override
    public long setSet(String key, long time, Object... values) {
        long count = redisTemplate.opsForSet().add(key, values);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
        return count;
    }

    /***
     * 获取set集合长度
     * @param key
     * @return
     */
    @Override
    public long setGetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /***
     * 移除值为value的元素
     * @param key 键
     * @param values 值 可以多个
     * @return 移除个数
     */
    @Override
    public long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /***
     * 删除整个set集合
     * @param key 键
     */
    @Override
    public void setRemoveAll(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public void setMessageInTopic(String topicName, Object message) {
        redisTemplate.convertAndSend(topicName, message);
    }
}
