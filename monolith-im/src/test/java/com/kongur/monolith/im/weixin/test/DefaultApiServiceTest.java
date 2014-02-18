package com.kongur.monolith.im.weixin.test;

import java.text.MessageFormat;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.weixin.service.DefaultApiService;

/**
 * @author zhengwei
 * @date 2014-2-17
 */
public class DefaultApiServiceTest {

    public static void main(String[] args) {

        // testGetUsers();

        testCreateUserGroup();

    }

    /**
     * 创建分组
     */
    private static void testCreateUserGroup() {
        String accessToken = "GtEee_vTEVbyd0a5M9G8YtTxGL6WGcDypHqj5LdCP9ODtHzve9ZNz-CNLlX1ZI9dD0wvAKC9UgsOscUixl1knhSDxXulmB8BF74W7GtNpXOE3hj1tM6QNU29Dy9GDCRW5EafCavu1b1EdfGuemuTcQ";

        String apiUrlPattern = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token={0}";
        String apiUrl = MessageFormat.format(apiUrlPattern, accessToken);

        String postParams = "{\"group\":{\"name\":\"test\"}}";

        DefaultApiService apiService = new DefaultApiService();

        ServiceResult<String> result = apiService.executePost(apiUrl, postParams);

        System.out.println(result.getResult());

    }

    /**
     * 或者关注用户
     */
    private static void testGetUsers() {
        String accessToken = "GtEee_vTEVbyd0a5M9G8YtTxGL6WGcDypHqj5LdCP9ODtHzve9ZNz-CNLlX1ZI9dD0wvAKC9UgsOscUixl1knhSDxXulmB8BF74W7GtNpXOE3hj1tM6QNU29Dy9GDCRW5EafCavu1b1EdfGuemuTcQ";

        String apiUrlPattern = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";
        String apiUrl = MessageFormat.format(apiUrlPattern, accessToken, "");

        DefaultApiService apiService = new DefaultApiService();

        ServiceResult<String> result = apiService.executeGet(apiUrl);

        System.out.println(result.getResult());
    }

}
