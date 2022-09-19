package com.kaede.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author kaede
 * @create 2022-09-19
 */

@RestController
public class VerityCodeController {

    private final Producer producer;

    @Autowired
    public VerityCodeController(Producer producer) {
        this.producer = producer;
    }

    @RequestMapping("/vc.jpg")
    public String getVerityCode(HttpServletResponse response, HttpSession session) throws IOException {
        //1.生成验证码
        String text = producer.createText();
        //2.保存到session中（此处可用redis实现）
        session.setAttribute("kaptcha", text);
        //3.生成图片
        BufferedImage bi = producer.createImage(text);
        //将图片转为内存中的byte数组
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(bi,"jpg",fos);
        //4.返回base64，前端通过加上 data:image/jpg;base64, 前缀可用解析图片
        return Base64.encodeBase64String(fos.toByteArray());
    }

}
