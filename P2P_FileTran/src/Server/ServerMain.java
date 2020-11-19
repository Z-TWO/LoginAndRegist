package Server;

import FileServer.Fileserver;
import bin.FileSetting;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerMain extends ServerUi {

    //启动文件工具类
    private static FileUtils fileUtils = new FileUtils();
    private static Fileserver otherfileUtils = new Fileserver();
    //接受传送过来的对象
    private static FileSetting fileSetting = new FileSetting();


    //默认地址
    private String filepath;

    public static void main(String[] args) {

        ServerMain serverMain = new ServerMain();
        serverMain.setVisible(true);
        serverMain.setTitle("接受端 (未启动)");
    }

    //开始监听
    @Override
    public void doMonitor() {
        //获取输入的端口
        String socketPort = portValue.getText();
        //判断端口是否为空
        boolean flagPortE = socketPort.equals("");

        if (flagPortE ) {
            JOptionPane.showMessageDialog(this, "端口号输入有，请检查！");
            return;
        } else {
            monitor.setEnabled(false);
            selectPath.setEnabled(false);
            //改变窗口名字
            try {
                String hostIP = InetAddress.getLocalHost().getHostAddress();
                this.setTitle("接受端 (本地ip:"+hostIP+")");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        new getFile(this).start();
    }

    //选择默认地址
    @Override
    public void doSelect() {
        //选择文件器获取文件或者文件夹
        JFileChooser jfc = new JFileChooser();
        //设置当前路径为桌面路径，否则将我的文档作为默认路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        jfc.setCurrentDirectory(fsv.getHomeDirectory());
        //JFileChooser.FILES_AND_DIRECTORIES 选择路径和文件
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //用户选择的路径或文件
        int returnVal = jfc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //获取到选择文件的地址
            filepath = jfc.getSelectedFile().getPath();
            //将文件地址填写到地址框
            filePath.setText(filepath);
        }
    }

    //开启接受文件线程
    class getFile extends Thread {

        JFrame frame;

        //构造函数
        public getFile(JFrame frame) {
            this.frame = frame;
        }
        @Override
        public void run() {
            try {
                Object obj = null;
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(portValue.getText()));
                System.out.println("服务器启动，等待客户端的连接");
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                obj = ois.readObject();
                if (obj != null) {
                    //obj转换成FileSetting对象
                    fileSetting = (FileSetting) obj;
                    //开始储蓄工作
                    File dir = new File(filepath + "\\" + socket.getInetAddress().getHostAddress());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, fileSetting.getFileName());
                    //获取base64流转为指定文件
                    String stream = fileSetting.getStream();
                    otherfileUtils.decryptByBase64(stream, file.getPath());
                    JOptionPane.showMessageDialog(frame, "接受到[" + socket.getInetAddress().getHostAddress() + "]的文件，并保存成");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "接受文件失败");
            }
        }
    }


}
