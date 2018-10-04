package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String file_server_url;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        //1.取文件的扩展名
        String originalFilename = file.getOriginalFilename();//获取文件名
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);//得到扩展名

        try {
            //2.创建一个FastDFS客户端
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");

            //3.执行上传处理
            String fileId = client.uploadFile(file.getBytes(), extName);

            //4.拼接返回的url和Ip地址，拼成完整的URL
            String url = file_server_url+fileId;//上传文件的完整地址

            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }

}
