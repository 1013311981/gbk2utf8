package top.rurenyinshui.gbk2utf8.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.rurenyinshui.gbk2utf8.DTO.ErrorCode;
import top.rurenyinshui.gbk2utf8.DTO.Response;
import top.rurenyinshui.gbk2utf8.util.ConverEncoding;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 包名: top.rurenyinshui.gbk2utf8.controller
 * 作者: Lx
 * 日期: 2019/7/2 21:54
 */
@RestController
@Slf4j
public class FileController {

    @Value("${lixin.filePath}")
    String filePath;
    @Value("${lixin.realPath}")
    String realPath;

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
            fileName =  ConverEncoding.generateShortUuid()+ "_" + fileName;
            String path = filePath + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            //进行转码操作
            ConverEncoding.transcodingOperation(fileName);

            return  Response.SUCCESS(fileName);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Response.ERROR(ErrorCode.UPLOAD_FAIL);
    }

    @GetMapping("/download")
    public Response downloadFile(@RequestParam("fileName") String fileName,HttpServletRequest request, HttpServletResponse response) {
        if (fileName != null) {
            File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return Response.SUCCESS();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return Response.ERROR(ErrorCode.DOWNLOAD_FAIL);
    }

}
