package bin;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

/**
 * @Description: tcpSocketClient
 * @Author: ZTwo
 * @CreateDate: 2020/11/14 11:04
 */
public class TcpSocketClient {

    //用来实现通信的Socket
    private TcpSetting tcpSocket;

    /**
     * 构造函数
     * 获取Tcpsetting对象
     */
    public TcpSocketClient(TcpSetting tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    //建立socket连接
    public Socket connectTcpClient(JFrame frame) {
        //存储服务器Socket
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(tcpSocket.getServerAddr(), tcpSocket.getPort());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,"连接失败！");
            return null;
        }
        return serverSocket;
    }

    //关闭连接
    public void Close(Socket socket) {
        try {
            socket.close();
            System.out.println("关闭成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
