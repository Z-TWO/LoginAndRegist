package Server;

import User.UserMain;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/*
 * @Description:    方法混合类
 *
 *                  1.菜单显示
 *                  2.修改用户信息方法
 *                  3.检验QQ、邮箱、账号、密码的方法类
 *
 * @CreateDate:     2020/10/12
 */
public class Driver {

    private static final String REGEX_PASSWORD = "^[A-Z][A-Za-z0-9<>,.?\\[\\]{}\\|~!@#$%^&*()_+-=]{9,}$";//密码-第一位为大写字母,可以包含空白字符
    private static final String REGEX_USERNAME = "^[A-Z][A-Za-z0-9_]{9,}$";//用户名-可以包含大小写字母以及下划线
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";//邮箱检验
    private static final String REGEX_QQ = "[1-9][0-9]{4,14}";//QQ检验
    private static final String LINE = "-----------------------------------------";
    private static final String LINES = "------------------------------";
    static ArrayList<Account> UserList = new UserMain().UserList;//用户信息列表


    /*
     * 菜单集合
     */

    //一级菜单
    public void FristMenu() {
        System.out.println("\n               ***主界面***");
        System.out.println("1.用户登陆   2.用户注册   3.管理员登陆   4.退出");
        System.out.println(LINE);
    }

    //用户登陆二级菜单
    public void userSecMenu() {
        System.out.println("\n             ***用户登录界面***");
        System.out.println("1.账号登陆   2.邮箱登陆   3.QQ登陆   4.返回主界面");
        System.out.println(LINE);
        System.out.print("->请选择登陆方式: ");
    }

    //用户三级菜单(分绑定和非绑定)
    public void userThridMenu(boolean flag) {  //flag指是否已经绑定邮箱或者QQ

        if (flag == false) {
            System.out.println("\n         ***非绑定用户操作界面***");
            System.out.println("1.查看个人信息   2.绑定QQ/邮箱   3.退出登陆");
            System.out.println(LINE);
        } else {
            System.out.println("\n                        ***绑定用户操作界面***");
            System.out.println("1.修改密码   2.修改账号   3.查看个人信息   4.绑定或改绑邮箱/QQ   5.退出登陆");
            System.out.println(LINE+LINES);
        }
    }

    //用户注册菜单
    public void userRegistfirstMenu() {
        System.out.println("\n       ***用户注册界面***");
        System.out.println("1.账号注册   2.邮箱注册   3.QQ注册");
        System.out.println(LINES);
    }

    //绑定菜单
    public void userRegistBoundMune() {
        System.out.println("        ***绑定界面***");
        System.out.println("1.绑定邮箱   2.绑定QQ   3.离开");
        System.out.println(LINES);
    }

    //管理员二级菜单
    public void managerSecondMenu() {

        System.out.println("                         ***管理员操作界面***");
        System.out.println("1.查看用户列表   2.更改指定用户个人信息   3.解冻用户   4.注销用户   5.返回主界面");
        System.out.println(LINE+LINES);
    }

    //管理员修改信息操作
    public void changeUserInformationMenu() {
        System.out.println("\n\n                  ***更改指定用户个人信息***");
        System.out.println("1.修改账号   2.更换密码   3.改绑/绑定QQ   4.改绑/绑定邮箱   5.返回上一级");
        System.out.println(LINE+LINES);
        System.out.print("->请输入修改方式: ");
    }



    /*
     * 检验类
     */

    //密码检验
    public boolean testPassword(String password) {
        return password.matches(REGEX_PASSWORD);
    }

    //用户名检验
    public boolean testUsername(String username) {
        return username.matches(REGEX_USERNAME);
    }

    //邮箱检验
    public boolean testEamil(String email) {
        return email.matches(REGEX_EMAIL);
    }

    //QQ检验
    public boolean testQQ(String QQ) {
        return QQ.matches(REGEX_QQ);
    }


    /*
     * 员工信息列表操作类
     */

    //修改账号（用户/管理员）
    public String changeUsername(String username, Scanner sc,Driver driver) {

        System.out.print("\n新的账号: ");
        String newUsername = sc.next();

        //判断格式是否正确
        boolean usernameP = driver.testUsername(newUsername);
        if (!usernameP) {
            System.out.println("\n您输入的新账号不符合格式要求，请重新操作！\n");
            return username;
        }

        //遍历修改账号
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setUsername(newUsername);
                System.out.println("\n账号修改成功！\n");
            }
        }
        return newUsername;
    }

    //修改邮箱(用户)
    public String changeEmail(String username,String nowEmail, Scanner sc, Driver driver) {

        System.out.print("\n新的邮箱: ");
        String newEmail = sc.next();

        //判断格式是否正确
        boolean userEmailP = driver.testEamil(newEmail);
        if (!userEmailP) {
            System.out.println("\n您输入的新邮箱不符合格式要求，请重新操作！\n");
            return nowEmail;
        }

        //遍历修改账号
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setEmail(newEmail);
                System.out.println("\n邮箱修改成功！\n");
            }
        }
        return newEmail;
    }

    //修改邮箱(管理员)
    public void changeEmailByM(String username, Scanner sc) {
        //获取新的邮箱
        System.out.print("输入绑定/改绑的邮箱: ");
        String nowEmail = sc.next();

        //在成员列表里面修改QQ
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setEmail(nowEmail);
                System.out.println("\n邮箱修改成功!\n");
            }
        }
    }

    //修改QQ(用户)
    public String changeQQ(String username,String nowQQ, Scanner sc, Driver driver) {

        System.out.print("\n新的QQ: ");
        String newQQ = sc.next();

        //判断格式是否正确
        boolean userEmailP = driver.testEamil(newQQ);
        if (!userEmailP) {
            System.out.println("\n您输入的新QQ不符合格式要求，请重新操作！\n");
            return nowQQ;
        }

        //遍历修改账号
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setQQ(newQQ);
                System.out.println("\nQQ修改成功！\n");
            }
        }
        return newQQ;
    }

    //修改QQ(管理员)
    public void changeQQByM(String username, Scanner sc) {

        //获取新的密码
        System.out.print("输入绑定/改绑的QQ: ");
        String nowQQ = sc.next();

        //在成员列表里面修改QQ
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setQQ(nowQQ);
                System.out.println("\nQQ修改成功!\n");
            }
        }


    }

    //修改密码(用户)
    public String changePassword(String username,String password, Scanner sc,Driver driver) {

        String nowPssword = password;//现在的密码
        String oldPassword;//用户输入的原密码
        String firstNewPassword;//用户第一次输入的新密码
        String secondNewPassword;//用户第二次输入的新密码
        boolean passwordB;//判断新密码是否符合要求

        System.out.print("\n原密码: ");
        oldPassword = sc.next();
        System.out.print("新的密码: ");
        firstNewPassword = sc.next();
        System.out.print("再次输入新的密码: ");
        secondNewPassword = sc.next();

        //输入新密码修改的格式是否符合格式要求
        passwordB = driver.testPassword(firstNewPassword);
        //修改成功，条件：原正确，输入的新密码符合格式要求，两个新密码一致
        if (oldPassword.equals(nowPssword) && firstNewPassword.equals(secondNewPassword) && passwordB) {
            for (Account user : UserList) {//遍历在信息列表修改指定用户密码
                if (user.getUsername().equals(username)) {
                    user.setPassword(firstNewPassword);
                }
            }
            System.out.println("\n密码修改成功！\n");
            nowPssword = firstNewPassword;//返回新密码，以便其他操作执行
        } else {//错误排查问题输出

            //原始密码错误
            if (!oldPassword.equals(nowPssword)) {
                for (Account user : UserList) {//当原密码输入错误，则在信息列表将可输入错误密码次数
                    if (user.getUsername().equals(username)) {
                        int temp = user.getPasswordNum() - 1;
                        user.setPasswordNum(temp);
                    }
                }
                System.out.println("\n原密码输入错误！\n");
                return nowPssword;
            }

            //新格式不对
            if (!passwordB) {
                System.out.println("\n您输入的新密码不符合格式要求，请重新操作！\n");
                return nowPssword;
            }

            //两次输入的新密码不一致
            if (!firstNewPassword.equals(secondNewPassword)) {
                System.out.println("\n两次输入的新密码不一致！\n");
                return nowPssword;
            }
        }
        return nowPssword;

    }

    //修改密码(管理员)
    public String changePasswordByM(String username, Scanner sc) {

        System.out.print("新的密码: ");
        String nowPssword = sc.next();

        //在成员列表里面修改密码
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setPassword(nowPssword);
                System.out.println("\n密码修改成功!\n");
            }
        }
        return nowPssword;
    }

    //查看个人信息（用户）
    public void getUserInformation(String username) {

        System.out.print("\n个人信息: ");
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                System.out.println(user+"\n");
            }
        }
    }

    //打印所有用户信息（管理员）
    public void printUserList() {

        System.out.println("\n                                                        用户信息列表");
        System.out.println(LINE + LINE + LINE + LINE);
        for (Account user : UserList) {
            System.out.println(user);
        }
        System.out.println(LINE + LINE + LINE + LINE + "\n");
    }

    //注销账号（管理员）
    public void cancelAccount(String username) {

        //创建UserList迭代器
        Iterator<Account> user = UserList.iterator();
        while(user.hasNext()) {//判断后面是否为空
            Account value = user.next();
            if (value.getUsername().equals(username)) {
                user.remove();
                System.out.println("\n账号: " + username + " 注销成功！\n");
            }
        }



    }

    //解冻账号（管理员）
    public void thawAccount(String username) {

        //对账号进行解冻
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setPrivilege(true);
                user.setRecentLoginDate(new Date());
                System.out.println("\n解冻成功!\n");
            }
        }
    }

    //冻结账号（管理员/用户操作失误冻结）
    public void frozenAccount(String username) {
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setPrivilege(false);
            }
        }
        UserMain userMain = new UserMain();
        userMain.getUnPrList();//冻结账号后将map更新
    }

    //初始化话输出错误密码次数（用户）
    public void passwordNUMSet3(String username) {
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                user.setPasswordNum(3);
            }
        }
    }

    //退出系统
    public void exit() {
        System.out.println("感谢您的使用，已经退出系统！");
        System.exit(0);
    }
}
