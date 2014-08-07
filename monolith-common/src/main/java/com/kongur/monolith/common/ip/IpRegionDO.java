package com.kongur.monolith.common.ip;


/**
 * ip 地址
 * 
 * @author zhengwei
 */
public class IpRegionDO extends com.kongur.monolith.common.DomainBase {

    /**
     * 
     */
    private static final long serialVersionUID = 5458935713585348466L;

    /**
     * 国家 eg 中国
     */
    private String            country;

    /**
     * 区，eg 华东
     */
    private String            area;

    /**
     * 省 eg 浙江省
     */
    private String            region;

    /**
     * 市 eg 杭州市
     */
    private String            city;

    /**
     * 国家对应的id
     */
    private String            countryId;

    /**
     * 区域对应的id
     */
    private String            areaId;

    /**
     * 省对应的id
     */
    private String            regionId;

    /**
     * 市对应的id
     */
    private String            cityId;

    public IpRegionDO(String country, String area, String region, String city) {
        this.country = country;
        this.area = area;
        this.region = region;
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public IpRegionDO setCountryId(String countryId) {
        this.countryId = countryId;
        return this;
    }

    public String getAreaId() {
        return areaId;
    }

    public IpRegionDO setAreaId(String areaId) {
        this.areaId = areaId;
        return this;
    }

    public String getRegionId() {
        return regionId;
    }

    public IpRegionDO setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public String getCityId() {
        return cityId;
    }

    public IpRegionDO setCityId(String cityId) {
        this.cityId = cityId;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
