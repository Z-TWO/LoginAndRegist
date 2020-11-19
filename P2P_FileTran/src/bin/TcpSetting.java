package bin;
/**
* @Description:    tcp连接实体类
* @Author:         ZTwo
* @CreateDate:     2020/11/14 11:03
*/
public class TcpSetting {

    //设置上传服务器
    private String serverAddr;
    //设置上传端口
    private int port;

    //构造函数
    public TcpSetting(String serverAddr, int port) {
        this.serverAddr = serverAddr;
        this.port = port;
    }

    //get和set方法
    public int getPort() {
        return port;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
}
