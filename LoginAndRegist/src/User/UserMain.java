package User;

import Manger.ManagerLogin;
import Server.Account;
import Server.Driver;
import Server.FileUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/*
* @Description:    主类
* @UpdateDate:     2020/10/12 19:30
*/
public class UserMain {

    public static ArrayList<Account> UserList = new ArrayList<Account>();    //存放用户信息列表
    public static HashMap<String, String> UnPwList = new HashMap<String, String>(); //username+password
    public static HashMap<String, String> EmPwList = new HashMap<String, String>(); //email+password
    public static HashMap<String, String> QQPwList = new HashMap<String, String>(); //QQ+password
    public static HashMap<String, String> UnEmList = new HashMap<String, String>();  //username+email
    public static HashMap<String, String> UnQQList = new HashMap<String, String>();  //username+email
    public static HashMap<String, Boolean> UnPrList = new HashMap<String, Boolean>();  //username+email
    static ArrayList<Account> UserRegistList = new UserRegist().UserRegistList;//注册列表

    private static final String LINE = "-----------------------------------------";//界面分割线

    static Driver driver;

    public static void main(String[] args) {

        driver = new Driver();
        UserMain userMain = new UserMain();
        userMain.setUp();
    }

    /*
     * 加载初始界面
     */
    public void setUp() {

        //初始化用户信息
        addUserAccount();

        //一级菜单switch操作
        setUpSwitch();

    }

    public static void setUpSwitch() {

        //更新列表
        updateList();
        //创建对象
        Scanner sc = new Scanner(System.in);
        UserLogin userLogin = new UserLogin();//用户登陆类
        UserRegist userRegist = new UserRegist();//用户注册类
        final ManagerLogin managerLogin = new ManagerLogin();//管理员登陆

        driver.FristMenu(); //一级菜单

        //一级菜单选择操作
        while (true) {

            System.out.print("->请输入要操作的指令序号: ");
            int command = sc.nextInt();

            switch (command) {

                case 1://用户登陆
                    userLogin.Login();
                    break;

                case 2://用户注册
                    userRegist.Regist();
                    break;

                case 3://管理员登陆
                    managerLogin.Login();
                    break;

                case 4://退出
                    new FileUtil().WriteUserList();//退出系统前将数据写入文件
                    driver.exit();
                    break;

                default:
                    System.out.println("输入错误,请重新输入！\n");
                    break;
            }
        }

    }

    /*
     * 初始化用户信息
     */
    public static void addUserAccount() {

        //赋值Account对象的date属性
        Date user1Date = null;
        Date user2Date = null;
        //赋值给Date的String日期
        String user1Login = "2020-9-27 00:00:00";
        String user3Login = "2020-9-20 09:58:23";
        //Date对象格式化对象
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            user1Date = format.parse(user1Login);
            user2Date = format.parse(user3Login);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //读取用户信息文件里面了的数据
        FileUtil fileUtil = new FileUtil();
        fileUtil.ReadUserList();
    }

    /**
     * 对于所有需要被使用的列表在switch前每次都要被更新
     */
    public static void updateList() {

        UserMain userMain = new UserMain();
        userMain.registListToUserList();
        userMain.getUnPwList();
        userMain.getEmPwList();
        userMain.getQQPwList();
        userMain.getUnEmList();
        userMain.getUnQQList();
        userMain.getUnPrList();

    }

    /*
     * 建立 key：username，value：password 的HashMap
     */
    private void getUnPwList() {

        for (Account user : UserList) {

            UnPwList.put(user.getUsername(), user.getPassword());
        }
    }

    /*
     * 建立 key：email，value：password 的HashMap
     */
    private void getEmPwList() {

        for (Account user : UserList) {

            EmPwList.put(user.getEmail(), user.getPassword());
        }
    }

    /*
     * 建立 key：QQ，value：password 的HashMap
     */
    private void getQQPwList() {

        for (Account user : UserList) {

            QQPwList.put(user.getQQ(), user.getPassword());
        }
    }

    /*
     * 建立 key：username，value：email 的HashMap
     */
    private void getUnEmList() {

        for (Account user : UserList) {
            if (user.getEmail() != null) {
                UnEmList.put(user.getEmail(), user.getUsername());
            }
        }
    }

    /*
     * 建立 key：username，value：QQ 的HashMap
     */
    private void getUnQQList() {

        for (Account user : UserList) {
            if (user.getQQ() != null) {
                UnQQList.put(user.getQQ(), user.getUsername());
            }
        }
    }

    /*
     * 建立 key：username，value：privilege 的HashMap
     */
    public void getUnPrList() {
        for (Account user : UserList) {
            UnPrList.put(user.getUsername(), user.getPrivilege());
        }
    }

    /*
     * 将注册列表导到可登陆成员列表
     */
    private void registListToUserList() {
        UserList.addAll(UserRegistList);
        UserRegistList.clear();
    }
}
