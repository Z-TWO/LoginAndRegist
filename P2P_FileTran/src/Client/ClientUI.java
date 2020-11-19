package Client;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ClientUI extends JFrame {

    protected JTextField ipValue; //ip输入框
    protected JTextField portValue; //端口输入框
    protected JButton connect; //连接按钮
    protected JButton disconnect; //断开按钮
    protected JTextField filePath; //传送文件地址
    protected JButton selectFile; //选择文件按钮
    protected JButton send;//发送文件按钮

    //构造函数
    public ClientUI() {

        init();
        addComponent();
        addListener();
        new doChangeButton().start();
    }

    //窗口初始化
    private void init() {

        //设置窗口大小和出现位置
        this.setBounds(950, 400, 660, 180);
        //清除布局
        this.setLayout(null);
        //关闭窗口后自动关闭程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口大小固定
        this.setResizable(false);
    }

    //添加组件
    private void addComponent() {

        //初始化组件
        JLabel ip = new JLabel("IP:");
        ipValue = new JTextField(15);
        JLabel port = new JLabel("端口:");
        portValue = new JTextField(6);
        connect = new JButton("连接");
        disconnect = new JButton("断开");
        filePath = new JTextField(25);
        selectFile = new JButton("选择");
        send = new JButton("发送");

        //初始化按钮状态和文本框内容
        connect.setEnabled(true);
        disconnect.setEnabled(false);
        selectFile.setEnabled(true);
        send.setEnabled(false);
        ipValue.setText("127.0.0.1");
        portValue.setText("9090");

        //字号
        Font labelFont = new Font("黑体", Font.BOLD, 25);
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 20);
        Font other = new Font("黑体", Font.BOLD, 15);

        //排版和改变字号
        ip.setFont(labelFont);
        port.setFont(labelFont);
        ipValue.setFont(other);
        portValue.setFont(other);
        connect.setFont(buttonFont);
        disconnect.setFont(buttonFont);
        filePath.setFont(other);
        selectFile.setFont(buttonFont);
        send.setFont(buttonFont);

        ip.setBounds(20, 10, 45, 50);
        ipValue.setBounds(65,24,150,30);
        port.setBounds(250, 0, 80, 70);
        portValue.setBounds(325,24,100,30);
        connect.setBounds(450, 20, 80, 35);
        disconnect.setBounds(550,20,80,35);
        filePath.setBounds(20, 90, 405, 30);
        selectFile.setBounds(450, 87, 80, 35);
        send.setBounds(550, 87, 80, 35);

        //组件添加进窗口
        this.add(ip);
        this.add(ipValue);
        this.add(port);
        this.add(portValue);
        this.add(connect);
        this.add(disconnect);
        this.add(filePath);
        this.add(selectFile);
        this.add(send);


    }

    //添加监听器
    public void addListener() {

        //连接
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doConnect();
            }
        });

        //断开连接
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDisconnect();
            }
        });

        //选择文件
        selectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSelectFile();
            }
        });

        //发生文件
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSent();
            }
        });
    }


    //连接
    public abstract void doConnect();

    //断开
    public abstract void doDisconnect();

    //选择文件
    public abstract void doSelectFile();

    //发送文件
    public abstract void doSent();

    //按钮判断线程
    private class doChangeButton extends Thread {

        @Override
        public void run() {
            while (true) {
                //连接与断开和发送互斥
                if (connect.isEnabled()) {
                    disconnect.setEnabled(false);
                    send.setEnabled(false);
                } else {
                    disconnect.setEnabled(true);
                    send.setEnabled(true);
                }
                //线程休眠
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
