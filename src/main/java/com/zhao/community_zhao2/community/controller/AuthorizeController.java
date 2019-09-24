package com.zhao.community_zhao2.community.controller;


import com.zhao.community_zhao2.community.dto.AccessTokenDTO;
import com.zhao.community_zhao2.community.dto.GithubUser;
import com.zhao.community_zhao2.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.util.UUID;

/**
 * Created by codedrinker on 2019/4/24.
 */
@Controller
public class AuthorizeController {


    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private GithubProvider githubProvider;


    /**用于认证的contrroller*/

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {

        //获得access_token
        System.out.println("code是:   "+code);
        System.out.println("state是:   "+state);
        System.out.println("clientSecret是:    "+clientSecret);
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //利用access_token请求github的api获得登录信息(用户的登录信息)
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println("accessToken内容是(外层)"+accessToken);

        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println("githubUser对象是:   "+githubUser);


        System.out.println(githubUser.getName());
//
//        if (githubUser != null && githubUser.getId() != null) {
//            User user = new User();
//            String token = UUID.randomUUID().toString();
//            user.setToken(token);
//            user.setName(githubUser.getName());
//            user.setAccountId(String.valueOf(githubUser.getId()));
//            user.setAvatarUrl(githubUser.getAvatarUrl());
//            userService.createOrUpdate(user);
//            Cookie cookie = new Cookie("token", token);
//            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
//            response.addCookie(cookie);
//            return "redirect:/";
//        } else {
//            log.error("callback get github error,{}", githubUser);
//            // 登录失败，重新登录
//            return "redirect:/";
//        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
