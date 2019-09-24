package com.zhao.community_zhao2.community.provider;

import com.alibaba.fastjson.JSON;
import com.zhao.community_zhao2.community.dto.AccessTokenDTO;
//import life.majiang.community.dto.AccessTokenDTO;
//import life.majiang.community.dto.GithubUser;
//import lombok.extern.slf4j.Slf4j;
import com.zhao.community_zhao2.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

/**
 * Created by codedrinker on 2019/4/24.
 *
 * github 官方文档的一个方法用于湖的access id
 */
@Component
//@Slf4j
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        System.out.println("123123");

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            String token = string.split("&")[0].split("=")[1];

            System.out.println("返回的token是:   "+token);
            return token;
//            return string;
        } catch (Exception e) {
//            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }


    /**通过accsess_token从github的网站取我想想要的数据,把数据封装到dto里面*/

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();

            //把string自动转换成java类对象
            //吧string的json的对象自动,自动转换解析成java的类对象快捷键是:"ctrl+alt+V"
            //不用一点一点解系了,JSON这个功能封装了
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
//            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }

}
