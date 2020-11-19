package Client;

import FileServer.Fileserver;
import bin.FileSetting;
import bin.TcpSetting;
import bin.TcpSocketClient;
import org.apache.commons.io.FileUtils;


import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.Socket;

public class ClientMian extends ClientUI {

    //建立客户端socket连接
    private static TcpSocketClient conn;
    //设置文件信息
    private static FileSetting file;
    //启动文件工具类
    private static FileUtils fileUtils = new FileUtils();
    private static Fileserver otherfileUtils = new Fileserver();

    //连接返回的Socket对象
    private Socket socketConnect;
    //选择文件的File对象
    private File selectFileObject;
    //选择文件的路径
    private static String SelectFilePath = null;
    //输出流
    private OutputStream os;

    public static void main(String[] args) {

        ClientMian clientMian = new ClientMian();
        //设置窗口标题为“未连接”
        clientMian.setTitle("发送端 (未连接)");
        //显示窗口
        clientMian.setVisible(true);
    }


    /**
     * 连接方法
     */
    @Override
    public void doConnect() {
        //ip和端口输入是否为空判断
        boolean flagIp = ipValue.getText().equals("");
        boolean flagPort = portValue.getText().equals("");
        if (flagIp || flagPort) {
            JOptionPane.showMessageDialog(this,"IP或端口号输入有误，请重新检查！");
            return;
        }
        //创建一个TcpSetting再传入TcpSocketClient
        TcpSetting tcpObject = new TcpSetting(ipValue.getText(), Integer.parseInt(portValue.getText()));
        conn = new TcpSocketClient(tcpObject);
        //建立连接
        socketConnect = conn.connectTcpClient(this);
        //connect返回null则return
        if (socketConnect == null) {
            return;
        } else {
            //建立心跳
            sendHeartbeat();
            //连接成功则设置标题
            this.setTitle("发送端 (已连接)");
            //连接按钮状态变成false
            connect.setEnabled(false);
        }
    }

    /**
     * 断开连接方法
     */
    @Override
    public void doDisconnect() {
        socketConnect = null;
        connect.setEnabled(true);
    }

    /**
     * 选择传送文件
     */
    @Override
    public void doSelectFile() {
        //选择文件器获取文件或者文件夹
        JFileChooser jfc = new JFileChooser();
        //设置当前路径为桌面路径，否则将我的文档作为默认路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        jfc.setCurrentDirectory(fsv.getHomeDirectory());
        //JFileChooser.FILES_AND_DIRECTORIES 选择路径和文件
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //用户选择的路径或文件
        int returnVal = jfc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //获取选择文件的File对象
            selectFileObject = jfc.getSelectedFile();
            //获取到选择文件的地址
            SelectFilePath = jfc.getSelectedFile().getPath();
            //将文件地址填写到地址框
            filePath.setText(SelectFilePath);
        }
    }

    /**
     * 发送文件
     */
    @Override
    public void doSent() {
        //判断传送文件是否为空
        if (SelectFilePath == null) {
            JOptionPane.showMessageDialog(this, "未选择传送文件！");
            return;
        }
        //初始化传送文件信息
        initFile(selectFileObject);
        try {
            //获取输出流
            os = socketConnect.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            //发送文件
            oos.writeObject(file);
            oos.writeObject(null);
            oos.flush();
            socketConnect.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化文件并组装
     */
    private static void initFile(File fileObject) {
        String name = fileObject.getName();
        String fileOriginPath = fileObject.getPath();
        String type = name.substring(name.lastIndexOf(".") + 1);
        String content = otherfileUtils.encryptToBase64(SelectFilePath);
        long size = fileObject.length();
        System.out.println("***************文件基本信息***************");
        System.out.println("name:"+name);
        System.out.println("fileOriginPath:"+fileOriginPath);
        System.out.println("type:"+type);
        System.out.println("content:"+content);
        System.out.println("size:"+size);
        System.out.println("***************文件基本信息***************");
        file = new FileSetting();
        file.setFileName(name);
        file.setSize(size);
        file.setStream(content);
        file.setFileType(type);
        file.setOriginPath(fileOriginPath);
    }

    /**
     * 心跳包
     */
    public void sendHeartbeat() {
        try {
            String heartbeat = "heart:00001;";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(10 * 1000);// 10s发送一次心跳
                            os.write(heartbeat.getBytes());
                            os.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

