package FileServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
//文件工具类

public class Fileserver {
    /**
     * 获取文件，以及文件后缀
     * @param fileName
     */
    public static String GetFile(String fileName){
        File file = new File(fileName);
        try {
            /**
             * 判断文件是否存在
             */
            if(!file.exists()){
                System.err.println("文件不存在");
            }
            System.out.println(file.toString());
        } catch (Exception e) {
            new Exception("找不到文件");
        }
        return file.toString();
    }

    /**
     * 将压缩成base64格式
     * @param filePath
     * @return
     */
    public String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将base64格式的文件解压，还原成文件
     * @param base64
     * @param filePath
     * @return
     */
    public String decryptByBase64(String base64, String filePath) {
        if (base64 == null && filePath == null) {
            return "生成文件失败，请给出相应的数据。";
        }
        try {
            Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "指定路径下生成文件成功！";
    }
}
