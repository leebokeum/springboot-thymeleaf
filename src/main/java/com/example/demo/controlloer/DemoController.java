package com.example.demo.controlloer;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by 이복음 on 2017-06-04.
 */

@Controller
@RequestMapping("/")
public class DemoController {

    private Facebook facebook; //페이스북 API 객체
    private ConnectionRepository connectionRepository; // 페이스북 커넥트 정보

    public DemoController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        //페이스북 로그인되어 있지 않을 때
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("feed", feed);
        return "hello";
    }

    @RequestMapping("/facebookList")
    @ResponseBody
    public PagedList<Post> facebookPageList(){

        return facebook.feedOperations().getFeed();
    }

    @RequestMapping("/facebookUser")
    @ResponseBody
    public User facebookUser(){

        InputStream in = new ByteArrayInputStream(facebook.userOperations().getUserProfileImage());
        BufferedImage bImageFromConvert = null;
        try {
            bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "jpg", new File(
                    "c:/facebook/new-darksouls.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return facebook.userOperations().getUserProfile();
    }

    @RequestMapping("/init")
    @ResponseBody
    public String init(){
        return "helloworld";
    }
}
