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
     * ???????????????list
     * @param key ???
     * @param value ???
     */
    @Override
    public void listLeftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /****
     * ???????????????list
     * @param key ???
     * @param value ???
     * @param time ????????????????????????
     */
    @Override
    public void listLeftPush(String key, Object value, long time) {
        redisTemplate.opsForList().leftPush(key, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /***
     * ???????????????list
     * @param key ???
     * @param value ???
     */
    @Override
    public void listRightPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /***
     * ???????????????list
     * @param key ???
     * @param value ???
     * @param time ????????????????????????
     */
    @Override
    public void listRightPush(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /****
     * ???????????????list??????
     * @param key ???
     * @param list ???
     */
    @Override
    public void listLeftPushAll(String key, List<?> list) {
        redisTemplate.opsForList().leftPushAll(key, list);
    }

    /***
     * ???????????????list??????
     * @param key ???
     * @param list ???
     * @param time ????????????????????????
     */
    @Override
    public void listLeftPushAll(String key, List<?> list, long time) {
        redisTemplate.opsForList().leftPushAll(key, list);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /***
     * ???????????????list??????
     * @param key ???
     * @param list ???
     */
    @Override
    public void listRightPushAll(String key, List<?> list) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        list.forEach(value -> {
            listOperations.rightPush(key, value);
        });
    }

    /***
     * ???????????????list??????
     * @param key ???
     * @param list ???
     * @param time ????????????????????????
     */
    @Override
    public void listRightPushAll(String key, List<?> list, long time) {
        redisTemplate.opsForList().rightPushAll(key, list);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /****
     * ??????list??????
     * @param key
     * @return
     */
    @Override
    public long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /***
     * ??????list????????? ???0 ??? -1 ??????????????????
     * @param key ???
     * @param start ??????
     * @param end ??????
     * @return
     */
    @Override
    public List<Object> listGetValue(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /****
     * ???????????? ??????list?????????
     * ???index>=0?????? 0 ?????????1 ?????????????????????????????????index<0??????-1????????????-2???????????????????????????????????????
     * @param key ???
     * @param index ??????
     * @return
     */
    @Override
    public Object listGetValue(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /***
     * ??????????????????list??????????????????
     * @param key ???
     * @param index ??????
     * @param value ???
     */
    @Override
    public void listUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /***
     * ??????N?????????value
     * @param key ???
     * @param count ???????????????
     * @param value ???
     * @return ???????????????
     */
    @Override
    public long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /***
     * ??????????????????
     * @param key
     */
    @Override
    public void listRemoveAll(String key) {
        redisTemplate.delete(key);
    }

    /****
     * ??????lis???????????????
     * @param key
     * @return
     */
    @Override
    public List<Object> listGetAllValue(String key) {
        return listGetValue(key, 0, -1);
    }


    /***
     * ??????key??????Set???????????????
     * @param key ???
     * @return set???????????????
     */
    @Override
    public Set<?> setGetAllValue(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /***
     * ??????value?????????set?????????,????????????
     * @param key ???
     * @param value ???
     * @return true ?????? false ?????????
     */
    @Override
    public boolean setHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /***
     * ???????????????set??????
     * @param key ???
     * @param values ??? ????????????
     * @return ????????????
     */
    @Override
    public long setSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /***
     * ???????????????set??????
     * @param key ???
     * @param time ????????????????????????
     * @param values ??? ????????????
     * @return ????????????
     */
    @Override
    public long setSet(String key, long time, Object... values) {
        long count = redisTemplate.opsForSet().add(key, values);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
        return count;
    }

    /***
     * ??????set????????????
     * @param key
     * @return
     */
    @Override
    public long setGetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /***
     * ????????????value?????????
     * @param key ???
     * @param values ??? ????????????
     * @return ????????????
     */
    @Override
    public long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /***
     * ????????????set??????
     * @param key ???
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
