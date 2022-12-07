package com.wjcollege.ruiji.controller;

import com.sun.deploy.net.HttpResponse;
import com.wjcollege.ruiji.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传和下载
 * @author chenWei
 * @date 2022/11/30 17:35
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${ruiji.path}")
    private String path;

    @PostMapping("/upload")
    public R upLoad(MultipartFile file){
        //初始文件名字
        String originalFilename = file.getOriginalFilename();

        //使用UUID生成名字
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString()+ substring;

        //文件不存在就创建
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(path+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }



        return R.success(fileName);

    }

    /**
     * 向浏览器响应图片
     * @param name
     */
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(path+name);

            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];

            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
