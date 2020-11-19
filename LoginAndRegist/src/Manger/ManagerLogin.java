package Manger;

import Dao.Login;
import Server.Account;
import Server.Driver;
import User.UserMain;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * @Description:    管理员登陆和操作类
 *
 *                  1.查看用户列表---打印全部用户的个人信息
 *                  2.更改指定用户信息---a.修改账号 b.更换密码 c.改绑/绑定QQ d.改绑/绑定邮箱 e.返回上一级
 *                  3.解冻用户---将可登陆状态改为true和最近登录日期改为现在，使该用户可直接登陆
 *                  4.注销用户---通过账号将该用户在用户信息列表里面进行移除
 *                  5.返回主界面
 *
 * @CreateDate:     2020/10/12
 */
public class ManagerLogin implements Login {

    private String ManagerAccount = "1";//管理员登陆账号
    private String ManagerPassword = "1";// 管理员登陆密码
    ArrayList<Account> UserList = new UserMain().UserList;//用户信息列表

    Driver driver;
    Scanner sc;

    @Override
    public void Login() {

        //初始化需要用到的对象
        driver = new Driver();
        //登陆
        boolean loginFalg = login();
        //登陆后进入操作菜单
        ManageruserOperation(loginFalg);

    }

    /*
     * 输入账号和密码检验登陆，返回是否成功登陆
     */
    boolean login() {

        sc = new Scanner(System.in);
        boolean loginFlag = false;

        //获取键盘输入账号密码
        System.out.print("账号: ");
        String account = sc.next();
        System.out.print("密码: ");
        String password = sc.next();

        //判断是否一致
        if (!account.equals(ManagerAccount)) {
            System.out.println("\n账号不存在！");
        }
        if (!password.equals(ManagerPassword)) {
            System.out.println("密码错误！");
        }
        if (account.equals(ManagerAccount) && password.equals(ManagerPassword)) {
            loginFlag = true;
            System.out.println("\n登陆成功!\n\n");
        }
        return loginFlag;
    }

    /*
     * 管理员操作
     */
    void ManageruserOperation(boolean loginFlag) {

        //没有登陆成功返回主界面
        if (!loginFlag) {
            driver.FristMenu();
            return;
        }
        //循环标志
        boolean flag = true;
        //显示操作菜单
        driver.managerSecondMenu();
        //while+switch循环操作
        while (true && loginFlag) {

            //存储username，初始化为空
            String username = null;

            //获取键盘输入
            System.out.print("->请选择操作指令: ");
            int command = sc.nextInt();

            switch (command) {
                case 1://查看用户列表
                    driver.printUserList();
                    break;

                case 2://更改用用户个人信息
                    changeUserInformation();
                    break;

                case 3://解冻用户
                    username = getUsername("解冻");
                    driver.thawAccount(username);
                    break;

                case 4://注销用户
                    username = getUsername("注销");
                    driver.cancelAccount(username);
                    break;

                case 5://返回主界面
                    new UserMain().setUpSwitch();
                    return;


                default:
                    System.out.println("\n输入错误，请重新输入！\n");
                    break;
            }

        }
    }//close method

    /*
     * 更改个人信息
     */
    void changeUserInformation() {

        boolean flag = true;

        while (flag) {

            //加载菜单
            driver.changeUserInformationMenu();
            //获取键盘输入
            int commmand = sc.nextInt();
            //存储需要修改信息的username
            String username = getUsername("修改信息");
            //当输入账号不存再则返回管理员二级菜单
            if (username == null) {
                driver.managerSecondMenu();
                return;
            }//如果username==null返回到上一级列表
            switch (commmand) {
                case 1://修改密码
                    driver.changeUsername(username, sc, driver);
                    break;
                case 2://更换账号
                    username = driver.changePasswordByM(username, sc);
                    break;
                case 3://改绑/绑定QQ
                    driver.changeQQByM(username, sc);
                    break;
                case 4://改绑/绑定邮箱
                    driver.changeEmailByM(username, sc);
                    break;
                case 5://返回上一级
                    System.out.println("\n");
                    flag = false;
                    new UserMain().setUpSwitch();//返回到主界面，并且将其他HashMap进行更新
                    break;

                default:
                    System.out.println("输入错误错误，请重新输入！\n");
                    break;
            }

        }
    }//close method

    /*
     * 获取需要修改信息的账号
     */
    String getUsername(String command) {

        //键盘获取输入的username
        System.out.print("\n输入要进行" + command + "的账号: ");
        String username = sc.next();

        //判断用户列表里面是否存在
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                return username;//找到一致账户，返回用户名
            }
        }

        //在列表里面找不到对应的用户名,返回null
        System.out.println("\n账号不存在，请重新操作！\n");
        return null;

    }


}
