package top.rurenyinshui.gbk2utf8.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.rurenyinshui.gbk2utf8.DTO.Response;

import java.io.File;
import java.io.IOException;

/**
 * 包名: top.rurenyinshui.gbk2utf8.controller
 * 作者: Lx
 * 日期: 2019/7/2 21:54
 */
@RestController
@Slf4j
public class FileController {


    @RequestMapping("/upload")
    public Response upload(@RequestParam("file") MultipartFile file){
        try {
            if (file.isEmpty()) {
                return new Response("文件为空");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            String filePath = "/Users/dalaoyang/Downloads/";
            String path = filePath + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            return new Response("上传成功");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response("上传失败");
    }



}
