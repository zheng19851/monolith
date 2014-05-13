package com.kongur.monolith.weixin.core.domain;

import java.util.List;

import com.kongur.monolith.common.DomainBase;

/**
 * 图文
 * 
 * @author zhengwei
 * @date 2014年2月20日
 */
public class ItemDO extends DomainBase {

    /**
	 * 
	 */
    private static final long serialVersionUID = -818033585251661290L;

    private String            id;

    /**
     * 图文消息标题
     */
    private String            title;

    /**
     * 图文消息描述
     */
    private String            description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String            picUrl;

    /**
     * 点击图文消息跳转链接
     */
    private String            url;

    private List<String>      errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean hasErrors() {
        return this.errors != null && !this.errors.isEmpty();
    }

}
