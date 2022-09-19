package com.kaede.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author kaede
 * @create 2022-09-19
 */

@Controller
public class VerityCodeController {

    @Autowired
    private Producer producer;

    @RequestMapping("/vc.jpg")
    public void verityCode(HttpServletResponse response, HttpSession session) throws IOException {
        //1.生成验证码
        String verityCode = producer.createText();
        //2.保存到session中
        session.setAttribute("kaptcha", verityCode);
        //3.生成图片
        BufferedImage image = producer.createImage(verityCode);
        //4.响应图片
        response.setContentType("image/png");
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(image,"jpg",sos);
    }

}
