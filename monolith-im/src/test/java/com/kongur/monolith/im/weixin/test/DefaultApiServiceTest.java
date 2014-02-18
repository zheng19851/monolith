package com.kongur.monolith.im.weixin.test;

import java.text.MessageFormat;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.weixin.service.DefaultApiService;

/**
 * @author zhengwei
 * @date 2014-2-17
 */
public class DefaultApiServiceTest {

    static String accessToken = "MiwbwU5HB27M1_t6_KJnzj5SEg1MgmAuchynzdQVL6n2VaBScYVpo63e2_LQ5sfzu57ECNdvdYl1lxNwFXCtQPfL__dZoaHOwGEPMdZR0CmGzCD9RamqdruUvVPa1O7kheYwkiCD7Gy5ofAAPfCOvg";

    public static void main(String[] args) {

        // testGetUsers();

        // testCreateUserGroup();

        // testCreateMenu();

        testGetAccessToken();

    }

    /**
     * 获取 access token
     */
    private static void testGetAccessToken() {

        String appId = "wxe58afcd99f7a997e";
        String appSecret = "5dcf8eac1e99e983fc58e42376ab0267";

        String apiUrlPattern = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
        String apiUrl = MessageFormat.format(apiUrlPattern, appId, appSecret);

        DefaultApiService apiService = new DefaultApiService();

        ServiceResult<String> result = apiService.executeGet(apiUrl);

        System.out.println(result.getResult());

    }

    /**
     * 创建菜单
     */
    private static void testCreateMenu() {

        String apiUrlPattern = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={0}";
        String apiUrl = MessageFormat.format(apiUrlPattern, accessToken);

        DefaultApiService apiService = new DefaultApiService();

        // String postParams = "{\"button\":[{  \"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"}]}";
        String postParams = " { \"button\":["
                            + "{  \"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"}" + ","
                            + "{ \"name\":\"菜单\",\"sub_button\":["
                            + "{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},"
                            + "{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"}" + "]" + "}" + "]}";

        ServiceResult<String> result = apiService.executePost(apiUrl, postParams);

        System.out.println(result.getResult());

    }

    /**
     * 创建分组
     */
    private static void testCreateUserGroup() {
        // String accessToken =
        // "GtEee_vTEVbyd0a5M9G8YtTxGL6WGcDypHqj5LdCP9ODtHzve9ZNz-CNLlX1ZI9dD0wvAKC9UgsOscUixl1knhSDxXulmB8BF74W7GtNpXOE3hj1tM6QNU29Dy9GDCRW5EafCavu1b1EdfGuemuTcQ";

        String apiUrlPattern = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token={0}";
        String apiUrl = MessageFormat.format(apiUrlPattern, accessToken);

        String postParams = "{\"group\":{\"name\":\"test\"}}";

        DefaultApiService apiService = new DefaultApiService();

        ServiceResult<String> result = apiService.executePost(apiUrl, postParams);

        System.out.println(result.getResult());

    }

    /**
     * 获取关注用户
     */
    private static void testGetUsers() {
        // String accessToken =
        // "GtEee_vTEVbyd0a5M9G8YtTxGL6WGcDypHqj5LdCP9ODtHzve9ZNz-CNLlX1ZI9dD0wvAKC9UgsOscUixl1knhSDxXulmB8BF74W7GtNpXOE3hj1tM6QNU29Dy9GDCRW5EafCavu1b1EdfGuemuTcQ";

        String apiUrlPattern = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";
        String apiUrl = MessageFormat.format(apiUrlPattern, accessToken, "");

        DefaultApiService apiService = new DefaultApiService();

        ServiceResult<String> result = apiService.executeGet(apiUrl);

        System.out.println(result.getResult());
    }

}
