package com.kongur.monolith.common.ip;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.kongur.monolith.common.result.Result;

/**
 * 默认用taobao ip api实现的服务
 * 
 * @author zhengwei
 */
//@Service("ipRegionService")
public class DefaultIpRegionService implements IpRegionService {

    private static final Logger log = Logger.getLogger(DefaultIpRegionService.class);

    /**
     * 淘宝ip服务url
     */
    private String              api = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    @Override
    public Result<IpRegionDO> getIpRegion(String ip) {

        Result<IpRegionDO> result = new Result<IpRegionDO>();

        if (!IpAddressUtils.isIPValid(ip)) {
            result.setError("10000", "ip地址不合法");
            return result;
        }

        if (IpAddressUtils.isInnerIP(ip)) {
            result.setError("10002", "此ip地址为局域网ip");
            return result;
        }

        HttpClient httpClient = new DefaultHttpClient();

        String internalApiUrl = api + ip;

        HttpGet httpGet = new HttpGet(internalApiUrl);

        // 淘宝返回的json数据
        String retData = null;
        try {

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                log.error("request error, There is no response data. apiUrl=" + internalApiUrl);
                result.setError("10001", "There is no response data");
                return result;
            }

            retData = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity); // Consume response content
            if (log.isDebugEnabled()) {
                log.debug("get response=" + retData);
            }

        } catch (Exception e) {
            log.error("http get error, apiUrl=" + internalApiUrl, e);
            throw new RuntimeException("http get error", e);
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }

        JSONObject jsonObj = JSONObject.fromObject(retData);
        if (!isValid(jsonObj)) {
            result.setError("10003", "获取ip位置信息出错");
            return result;
        }
        // {"code":0,"data":{"ip":"210.75.225.254","country":"\u4e2d\u56fd","area":"\u534e\u5317",
        // "region":"\u5317\u4eac\u5e02","city":"\u5317\u4eac\u5e02","county":"","isp":"\u7535\u4fe1",
        // "country_id":"86","area_id":"100000","region_id":"110000","city_id":"110000",
        // "county_id":"-1","isp_id":"100017"}}

        // ip地址信息
        JSONObject data = jsonObj.getJSONObject("data");
        String country = data.getString("country");
        String area = data.getString("area");
        String region = data.getString("region");
        String city = data.getString("city");
        IpRegionDO ipAddr = new IpRegionDO(country, area, region, city);
        ipAddr.setCountryId(data.getString("country_id")).setAreaId(data.getString("area_id")).setRegionId(data.getString("region_id")).setCityId(data.getString("city_id"));

        result.setResult(ipAddr);

        result.setSuccess(true);
        return result;
    }

    /**
     * 是否获取成功
     * 
     * @param jsonObj 返回的json数据
     * @return
     */
    private boolean isValid(JSONObject jsonObj) {
        String code = jsonObj.getString("code");
        return "0".equals(code);
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public static void main(String[] args) {
        DefaultIpRegionService ipAddressService = new DefaultIpRegionService();
        Result<IpRegionDO> result = ipAddressService.getIpRegion("115.236.173.66");

        System.out.println(result.getResult());
    }
}
