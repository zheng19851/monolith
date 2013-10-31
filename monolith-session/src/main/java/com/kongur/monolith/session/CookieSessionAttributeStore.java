package com.kongur.monolith.session;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kongur.monolith.session.attibute.AttributeConfigDO;
import com.kongur.monolith.session.attibute.AttributeDO;
import com.kongur.monolith.session.util.BlowfishUtils;

/**
 * 用cookie实现的session store
 * 
 * @author zhengwei
 * @date：2011-6-15
 */

public class CookieSessionAttributeStore implements SessionAttributeStore {

    private static final Logger      log       = Logger.getLogger(CookieSessionAttributeStore.class);

    /**
     * 存放已解析的属性
     */
    private Map<String, AttributeDO> attributes;

    /**
     * 存放未解析的属性
     */
    private Map<String, String>      cookies;

    /**
     * 标记修改过的属性
     */
    private Set<String>              dirty;

    private MonoHttpSession          monoHttpSession;

    /**
     * 用于标记编码失败
     */
    private static final String      ERROR     = new String("ERROR");

    /**
     * 加密key
     */
    public static final String       CRYPT_KEY = "com.kongur.monolith.session";

    @Override
    public Object getAttribute(AttributeConfigDO ac) {
        String key = ac.getKey();

        // 先从已解析中获取
        AttributeDO attribute = attributes.get(key);
        if (attribute == null) {
            attribute = getAttributeFromCookies(ac);

            // 保存到已解析map
            attributes.put(key, attribute);
        }

        return attribute.getValue();
    }

    /**
     * 从cookie里取
     * 
     * @param ac
     * @return
     */
    private AttributeDO getAttributeFromCookies(AttributeConfigDO ac) {
        String clientKey = ac.getClientKey();
        String cookieValue = cookies.get(clientKey);
        String v = decodeValue(cookieValue, ac);
        AttributeDO attribute = new AttributeDO(ac, v);
        return attribute;
    }

    /**
     * 解码
     * 
     * @param value
     * @param ac
     * @return
     */
    private String decodeValue(String value, AttributeConfigDO ac) {
        if (value == null) {
            return value;
        }

        try {
            value = URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            // logger.error("解码失败", e);
            // 解码失败时直接返回，不继续解析
            return value;
        }

        if (ac.isEncrypt()) {
            value = BlowfishUtils.decryptBlowfish(value, CRYPT_KEY);
        }

        return value;
    }

    @Override
    public void setAttribute(AttributeConfigDO ac, Object value) {
        String key = ac.getKey();
        // XXX 注意，这里调用了value上的toString()，而不是保存value对象本身
        String v = value == null ? null : value.toString();
        AttributeDO attribute = new AttributeDO(ac, v);
        attributes.put(key, attribute);
        dirty.add(key); // 标记修改
    }

    @Override
    public void commit() {
        String[] originalDirty = dirty.toArray(new String[dirty.size()]);
        for (String key : originalDirty) {
            if (dirty.contains(key)) { // 该key可能已经在之前的组合cookie中处理过，因此需要先检查是否还在dirty中
                AttributeDO attribute = attributes.get(key);
                encodeAttribute(attribute);
            }
        }
    }

    /**
     * 编码
     * 
     * @param attribute
     */
    private void encodeAttribute(AttributeDO attribute) {
        String value = attribute != null ? attribute.getValue() : StringUtils.EMPTY;

        value = encodeValue(value, attribute.getAttributeConfigDO());

        if (value == ERROR) {
            return;
        }

        addCookieToResponse(attribute.getAttributeConfigDO(), value, attribute == null);
    }

    /**
     * 将cookie保存到response中
     * 
     * @param ac
     * @param value
     * @param removed
     */
    private void addCookieToResponse(AttributeConfigDO ac, String value, boolean removed) {
        String name = ac.getClientKey();
        String domain = ac.getDomain();
        // XXX 删除cookie时将maxAge设为0，但原来的实现只是将值设为空而已
        int maxAge = !removed ? ac.getLifeCycle() : 0;
        String path = ac.getCookiePath();
        boolean httpOnly = ac.isHttpOnly();
        addCookieToResponse(name, value, domain, maxAge, path, httpOnly);
    }

    private void addCookieToResponse(String name, String value, String domain, int maxAge, String path, boolean httpOnly) {
        MonoHttpServletResponse response = getResponse();
        MonoCookie cookie = buildCookie(name, value, domain, maxAge, path, httpOnly);
        response.addCookie(cookie);
    }

    private MonoCookie buildCookie(String name, String value, String domain, int maxAge, String path, boolean httpOnly) {
        MonoCookie cookie = new MonoCookie(name, value);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }

    private MonoHttpServletResponse getResponse() {
        return this.monoHttpSession.getResponse();
    }

    /**
     * 编码
     * 
     * @param value
     * @param ac
     * @return
     */
    private String encodeValue(String value, AttributeConfigDO ac) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        if (ac.isEncrypt()) {
            value = BlowfishUtils.encryptBlowfish(value, CRYPT_KEY);
        }

        try {
            // XXX 总是编码，与原实现不同，原实现在组合cookie时只对加密的属性编码
            value = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            log.error("编码失败", e);
            // 编码失败时，返回错误标记，且不保存到cookies中
            return ERROR;
        }
        return value;
    }

    @Override
    public void init(MonoHttpSession session) {
        this.monoHttpSession = session;
        attributes = new HashMap<String, AttributeDO>();
        cookies = new HashMap<String, String>();
        dirty = new HashSet<String>();
        fetchCookies();
    }

    private void fetchCookies() {
        Cookie[] cookies = getRequest().getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                this.cookies.put(cookie.getName(), cookie.getValue());
            }
        }
    }

    private MonoHttpServletRequest getRequest() {
        return monoHttpSession.getRequest();
    }

}
