package pers.taoyao.tyaicodemother.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

/**
 *
 * @author admin
 * @date 2026/1/22
 * @Version v1.0
 * @description 缓存 key 生成工具类
 */
public class CacheKeyUtils {

    /**
     * 根据对象生成缓存key (JSON + MD5)
     *
     * @param object 要生成 key 的对象
     * @return MD5哈希后的缓存key
     */
    public static String generateKey(Object object) {
        if (object == null) {
            return DigestUtil.md5Hex("null");
        }
        // 先转 JSON，再转 MD5
        String jsonStr = JSONUtil.toJsonStr(object);
        return DigestUtil.md5Hex(jsonStr);
    }
}
