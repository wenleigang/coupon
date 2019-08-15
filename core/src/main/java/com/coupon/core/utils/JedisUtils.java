package com.coupon.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.core.utils
 * @ClassName: JedisUtils
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/8/15/015 10:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/15/015 10:26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class JedisUtils {
    protected final static Logger logger = Logger.getLogger(JedisUtils.class);

    private static  JedisPool jedisPool;

    @Autowired(required = true)
    public void setJedisPool(JedisPool jedisPool) {
        JedisUtils.jedisPool = jedisPool;
    }
    /**
     * 对某个键的值自增
     * @author liboyi
     * @param key 键
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setIncr(String key, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result =jedis.incr(key);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("set "+ key + " = " + result);
        } catch (Exception e) {
            logger.warn("set "+ key + " = " + result);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }
    public static Set<String> keys(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.keys(key);
                logger.debug("keys "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("get "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("get "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                logger.debug("get "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("get "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Object getObject(String key,Class<?> clazz) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                value = JSON.parseObject(((JSONObject) JSON.parse(jedis.get(JSON.toJSONBytes(key)))).toString(), clazz);
                logger.debug("getObject "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getObject "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String set(String key, String value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("set "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("set "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }
    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String set(byte[] key, byte[] value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("set "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("set "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(JSON.toJSONBytes(key), JSON.toJSONBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObject "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setObject "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public static List<String> getList(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
                logger.debug("getList "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getList "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public static List<Object> getObjectList(String key,Class<?> clazz) {
        List<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                List<byte[]> list = jedis.lrange(JSON.toJSONBytes(key), 0, -1);
                value = new ArrayList<>();
                for (byte[] bs : list){
                    value.add(JSON.parseObject(((JSONObject) JSON.parse(bs)).toString(), clazz));
                }
                logger.debug("getObjectList "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getObjectList "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.rpush(key, (String[])value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setList "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setList "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                jedis.del(key);
            }
            List<byte[]> list = new ArrayList<>();
            for (Object o : value){
                list.add(JSON.toJSONBytes(o));
            }
            result = jedis.rpush(JSON.toJSONBytes(key), (byte[][])list.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectList "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setObjectList "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long listAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.rpush(key, value);
            logger.debug("listAdd "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("listAdd "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long listObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<byte[]> list = new ArrayList<>();
            for (Object o : value){
                list.add(JSON.toJSONBytes(o));
            }
            result = jedis.rpush(JSON.toJSONBytes(key), (byte[][])list.toArray());
            logger.debug("listObjectAdd "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("listObjectAdd "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Set<String> getSet(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);
                logger.debug("getSet "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getSet "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                value = new HashSet<>();
                Set<byte[]> set = jedis.smembers(JSON.toJSONBytes(key));
                for (byte[] bs : set){
                    value.add(JSON.toJSONBytes(bs));
                }
                logger.debug("getObjectSet "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getObjectSet "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.sadd(key, (String[])value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setSet "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setSet "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                jedis.del(key);
            }
            Set<byte[]> set = new HashSet<>();
            for (Object o : value){
                set.add(JSON.toJSONBytes(o));
            }
            result = jedis.sadd(JSON.toJSONBytes(key), (byte[][])set.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectSet "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setObjectSet "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long setSetAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.sadd(key, value);
            logger.debug("setSetAdd "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setSetAdd "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long setSetObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<byte[]> set = new LinkedHashSet<>();
            for (Object o : value){
                set.add(JSON.toJSONBytes(o));
            }
            result = jedis.rpush(JSON.toJSONBytes(key), (byte[][])set.toArray());
            logger.debug("setSetObjectAdd "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setSetObjectAdd "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public static Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);
                logger.debug("getMap "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getMap "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public static Map<String, Object> getObjectMap(String key) {
        Map<String, Object> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                value = new HashMap<>();
                Map<byte[], byte[]> map = jedis.hgetAll(JSON.toJSONBytes(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()){
                    value.put(e.getKey().toString(), JSON.parseObject(((JSONObject) JSON.parse(e.getValue())).toString(), HashMap.class));
                }
                logger.debug("getObjectMap "+ key + " = " + value);
            }
        } catch (Exception e) {
            logger.warn("getObjectMap "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setMap "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setMap "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(JSON.toJSONBytes(key))) {
                jedis.del(key);
            }
            Map<byte[], byte[]> map = new HashMap<>();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(JSON.toJSONBytes(e.getKey()), JSON.toJSONBytes(e.getValue()));
            }
            result = jedis.hmset(JSON.toJSONBytes(key), (Map<byte[], byte[]>)map);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectMap "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("setObjectMap "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static String mapPut(String key, Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hmset(key, value);
            logger.debug("mapPut "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("mapPut "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Map<byte[], byte[]> map = new HashMap<>();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(JSON.toJSONBytes(e.getKey()), JSON.toJSONBytes(e.getValue()));
            }
            result = jedis.hmset(JSON.toJSONBytes(key), (Map<byte[], byte[]>)map);
            logger.debug("mapObjectPut "+ key + " = " + value);
        } catch (Exception e) {
            logger.warn("mapObjectPut "+ key + " = " + value);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public static long mapRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hdel(key, mapKey);
            logger.debug("mapRemove "+ key + " = " + mapKey);
        } catch (Exception e) {
            logger.warn("mapRemove "+ key + " = " + mapKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public static long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hdel(JSON.toJSONBytes(key), JSON.toJSONBytes(mapKey));
            logger.debug("mapObjectRemove "+ key + " = " + mapKey);
        } catch (Exception e) {
            logger.warn("mapObjectRemove "+ key + " = " + mapKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public static boolean mapExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hexists(key, mapKey);
            logger.debug("mapExists "+ key + " = " + mapKey);
        } catch (Exception e) {
            logger.warn("mapExists "+ key + " = " + mapKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public static boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hexists(JSON.toJSONBytes(key), JSON.toJSONBytes(mapKey));
            logger.debug("mapObjectExists "+ key + " = " + mapKey);
        } catch (Exception e) {
            logger.warn("mapObjectExists "+ key + " = " + mapKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public static long del(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)){
                result = jedis.del(key);
                logger.debug("del "+ key );
            }else{
                logger.debug("del "+ key + "not exists");
            }
        } catch (Exception e) {
            logger.warn("del "+ key);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public static long del(String[] key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.del(key);
            logger.debug("del "+ key );
        } catch (Exception e) {
            logger.warn("del "+ key);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public static long delObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)){
                result = jedis.del(key);
                logger.debug("delObject "+ key );
            }else{
                logger.debug("delObject "+ key + "not exists");
            }
        } catch (Exception e) {
            logger.warn("delObject "+ key + "not exists");
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public static boolean exists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.exists(key);
            logger.debug("exists "+ key );
        } catch (Exception e) {
            logger.warn("exists "+ key );
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }
    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public static boolean existsObject(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.exists(JSON.toJSONBytes(key));
            logger.debug("existsObject "+ key );
        } catch (Exception e) {
            logger.warn("existsObject "+ key );
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }
}
