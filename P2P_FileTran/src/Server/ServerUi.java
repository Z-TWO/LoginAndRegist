package Server;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class ServerUi extends JFrame{

    protected JTextField portValue; //监听端口
    protected JButton monitor;//开始监听按钮
    protected JTextField filePath;//默认地址输入框
    protected JButton selectPath;//默认地址选择按钮

    //构造函数
    public ServerUi() {

        init();
        addComponent();
        addListener();
    }

    //初始化窗口
    private void init() {

        //设置窗口出现的位置和大小
        this.setBounds(300, 400, 490, 180);
        //固定窗口大小
        this.setResizable(false);
        //窗口关闭则程序关闭
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //清除布局
        this.setLayout(null);
    }

    //添加组件
    private void addComponent() {

        //初始化组件
        JLabel portLabel = new JLabel("监听端口:");
        portValue = new JTextField(6);
        monitor = new JButton("监听");
        JLabel pathLabel = new JLabel("默认地址:");
        filePath = new JTextField(25);
        selectPath = new JButton("选择");

        //初始化按钮状态和输入框内容
        monitor.setEnabled(true);
        selectPath.setEnabled(true);
        portValue.setText("9090");
        File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();//获取本机的桌面地址
        String desktopPath = desktopDir.getAbsolutePath();
        filePath.setText(desktopPath);

        //字号
        Font labelFont = new Font("黑体", Font.BOLD, 25);
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 20);
        Font other = new Font("黑体", Font.BOLD, 15);

        //排版和改变字号
        portLabel.setFont(labelFont);
        pathLabel.setFont(labelFont);
        portValue.setFont(other);
        filePath.setFont(other);
        monitor.setFont(buttonFont);
        selectPath.setFont(buttonFont);

        portLabel.setBounds(20, 10, 130, 50);
        portValue.setBounds(140,24,100,30);
        monitor.setBounds(370, 20, 80, 35);
        pathLabel.setBounds(20, 78, 130, 50);
        filePath.setBounds(140, 90, 200, 30);
        selectPath.setBounds(370, 87, 80, 35);

        //组件添加进窗口
        this.add(portLabel);
        this.add(portValue);
        this.add(monitor);
        this.add(pathLabel);
        this.add(filePath);
        this.add(selectPath);
    }

    //添加监听器
    private void addListener() {

        //开始监听
        monitor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doMonitor();
            }
        });

        //选择默认地址
        selectPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSelect();
            }
        });
    }

    //监听方法
    public abstract void doMonitor();

    //选择默认地址方法
    public abstract void doSelect();


}
