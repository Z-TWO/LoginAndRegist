package bin;

import java.net.InetAddress;

public class ToolMe {

    private String filePaht;
    private String fileName;
    private InetAddress inetAddress;

    //构造函数(解析文件名）
    public ToolMe(String filePath) {

        this.filePaht = filePath;
    }

    //构造函数(获取本机ip)
    public ToolMe(InetAddress inetAddress) {

        this.inetAddress = inetAddress;
    }

    //地址解析文件名字
    public String getFileName() {

        String[] strs = filePaht.split("\\\\");
        int length = strs.length;
        fileName = strs[length-1];

        return fileName;
    }

    //解析本机ip地址
    public String getIp() {

        String ip = inetAddress.getHostAddress();
        return ip;
    }
}
