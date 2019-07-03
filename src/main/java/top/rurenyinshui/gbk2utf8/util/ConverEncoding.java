package top.rurenyinshui.gbk2utf8.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 包名: top.rurenyinshui.gbk2utf8.util
 * 作者: Lx
 * 日期: 2019/7/2 22:02
 */
@Component
public class ConverEncoding {

    @Value("${lixin.code}")
    static String CODE ="UTF-8";
    static String FILE_SUFFIX = ".txt";//文件扩展名
    //  static String FILE_SUFFIX = ".css";
//  static String FILE_SUFFIX = ".js";
//  static String FILE_SUFFIX = ".htm";
    static String srcDir ="/usr/local/java/oldfile/";//文件所在目录

    static String realPath ="/usr/local/java/newfile/";//文件所在目录
    /**
     * @param args
     * @throws Exception
     */
//    public static void main(String[] args) throws Exception {
//        List<String> files = new ArrayList<String>();
//        fetchFileList(srcDir, files, FILE_SUFFIX);
//        String filecode = "";
//        for (String fileName : files) {
//            filecode = codeString(fileName);
//            if (!filecode.equals(CODE)) {
//                convert(fileName, filecode, "E:/lixintest/newfile/"+System.currentTimeMillis()+FILE_SUFFIX, CODE);
//            }
//        }
//    }

    public static void transcodingOperation(String fileName) throws Exception {
        String newFile = realPath + fileName ;
        fileName = srcDir + fileName;
        String fileCode = codeString(fileName);
        convert(fileName,fileCode,newFile,CODE);

    }
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static void convert(String oldFile, String oldCharset,
                               String newFlie, String newCharset) {
        BufferedReader bin;
        FileOutputStream fos;
        StringBuffer content = new StringBuffer();
        try {
            System.out.println("the old file is :" + oldFile);
            System.out.println("The oldCharset is : " + oldCharset);
            bin = new BufferedReader(new InputStreamReader(new FileInputStream(
                    oldFile), oldCharset));
            String line = null;
            while ((line = bin.readLine()) != null) {
                // System.out.println("content:" + content);
                content.append(line);
                content.append(System.getProperty("line.separator"));
            }
            bin.close();
            File dir = new File(newFlie.substring(0, newFlie.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(newFlie);
            Writer out = new OutputStreamWriter(fos, newCharset);
            out.write(content.toString());
            out.close();
            fos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fetchFileList(String strPath, List<String> filelist,
                                     final String regex) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        Pattern p = Pattern.compile(regex);
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                fetchFileList(files[i].getAbsolutePath(), filelist, regex);
            } else {
                String strFileName = files[i].getAbsolutePath().toLowerCase();
                Matcher m = p.matcher(strFileName);
                if (m.find()) {
                    filelist.add(strFileName);
                }
            }
        }
    }

    /**
     * 判断文件的编码格式
     *
     * @param fileName :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static String codeString(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(
                fileName));
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }

        return code;
    }
}