package User;

import Dao.Regist;
import Server.Account;
import Server.Driver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/*
 * @Description:    用户注册类（实现Regist接口）
 *
 *                  1.账号/邮箱/QQ三种类型注册
 *                   a.账号注册需要检验账号格式和检查列表是否已经存在该用户，注册成功后可以进行绑定 邮箱，QQ，也可以离开不绑定
 *                   b.邮箱/QQ注册需要检验账号和检查是否已经被绑定或注册，注册成功后必须绑定账号
 *                  2.注册和绑定操作完后，要进行填写个人信息（性别，年龄，职业）
 *                  3.最后将所有注册信息通过创建Account对象，添加进RegistList；展示账号和密码，密码中间5位为“*”
 *                  4.回到主界面，将RegistList添加进UserList再更新其他HashMap，并且将RegistList进行清空，防止二次添加
 *
 * @CreateDate:     2020/10/12
 */
public class UserRegist implements Regist {

    static ArrayList<Account> UserRegistList = new ArrayList<Account>();//用户注册信息列表
    static ArrayList<Account> UserList = new UserMain().UserList;//用户信息列表
    static HashMap<String, String> UnEmList = new UserMain().UnEmList;//email+username
    static HashMap<String, String> UnQQList = new UserMain().UnQQList;//QQ+username

    //创建对象
    Driver driver;

    //存储成功注册的信息
    private String username = "";
    private String password = "";
    private String email = "";
    private String QQ = "";
    private String sex = "";
    private int age = 0;
    private String career = "";

    @Override
    public void Regist() {

        //加载注册菜单
        driver = new Driver();
        driver.userRegistfirstMenu();
        regist();
    }

    /*
     * 注册switch操作选择
     */
    void regist() {

        boolean flag = true;//循环标志
        boolean registFlag;//是否注册成功标志
        int command;//获取输入选项
        Scanner sc = new Scanner(System.in);

        while (flag) {

            System.out.print("->请选择注册方式: ");
            command = sc.nextInt();
            switch (command) {
                case 1://账号注册
                    registFlag = registSwitch("账号", sc);
                    flag = !boundOperaction("账号", registFlag, sc);
                    if (!flag && registFlag) {
                        addPersonImformation(sc);
                        addUserRegist();//获取注册和绑定信息，添加进列表
                        registToLogin();//自动登录
                        flag = false;
                    }
                    break;

                case 2://邮箱注册
                    registFlag = registSwitch("邮箱", sc);
                    flag = !boundOperaction("邮箱", registFlag, sc);
                    if (!flag && registFlag) {
                        addPersonImformation(sc);
                        addUserRegist();//获取注册和绑定信息，添加进列表
                        registToLogin();//自动登录
                        flag = false;
                    }
                    break;

                case 3://QQ注册
                    registFlag = registSwitch("QQ", sc);
                    flag = !boundOperaction("QQ", registFlag, sc);
                    if (!flag && registFlag) {
                        addPersonImformation(sc);
                        addUserRegist();//获取注册和绑定信息，添加进列表
                        registToLogin();//自动登录
                        flag = false;
                    }
                    break;
            }
        }
        new UserMain().setUpSwitch();
    }

    /*
     * 注册switch操作过程
     */
    boolean registSwitch(String order, Scanner sc) {

        //获取键盘输入的account（账号/邮箱/QQ）和password
        String[] list = getAccount(sc, order);
        String account = list[0];//account
        String password = list[1];//password
        boolean testAccount = false;//account布尔值
        boolean testPassword;//password布尔值
        boolean exist = true;// 账号/邮箱/QQ是否在用户列表里面已经存在或已经被绑定

        //密码检验
        testPassword = driver.testPassword(password);
        //account检验
        if (order == "账号") {
            testAccount = driver.testUsername(account);
        } else if (order == "邮箱") {
            testAccount = driver.testEamil(account);
        } else if (order == "QQ") {
            testAccount = driver.testQQ(account);
        }

        //判断 账号/QQ/邮箱+密码是否格式有误
        System.out.println();//请忽略
        if (!testAccount) {
            System.out.println(order + "格式有误，请重新注册！");
        }
        if (!testPassword) {
            System.out.println("密码格式有误，请重新注册！\n");
            return false;//如果检测到password有格式错误，则直接返回，不在进行账号是否存在判断
        }


        //用户列表判断是否存在，存在则直接返回
        for (Account user : UserList) {
            if (user.getUsername().equals(account) && order == "账号") {
                exist = false;
                System.out.println("\n该账号已经存在，无法进行注册！");
            }
        }
        if (UnEmList.containsKey(account) && order == "邮箱") {
            exist = false;
            System.out.println("\n该邮箱已经被绑定，无法进行注册！");
        }
        if (UnQQList.containsKey(account) && order == "QQ") {
            exist = false;
            System.out.println("\n该QQ已经被绑定，无法进行注册！");
        }


        if (testAccount && testPassword && exist) {

            //储存第一次注册成功信息
            if (order == "账号") {
                username = account;
            } else if (order == "邮箱") {
                email = account;
            } else if (order == "QQ") {
                QQ = account;
            }
            this.password = password;

            System.out.println("注册成功!\n");
            return true;//注册成功返回值
        }

        return false;
    }

    /*
     * 绑定操作
     */
    boolean boundOperaction(String order, boolean registFlag, Scanner sc) {

        //对是邮箱或QQ注册的用户进行账号绑定（必须）
        if ((order == "邮箱" || order == "QQ") && registFlag) {

            boolean tempFlag = true;//循环标志
            System.out.println("绑定账号!");

            while (tempFlag) {
                //是否存在判断标志，不存在true，存在未false
                boolean exist = true;
                //进行账号绑定
                System.out.print("账号: ");
                String boundUsername = sc.next();
                //检验格式
                boolean usernameB = driver.testUsername(boundUsername);
                //检验该账号在用户列表是否存在
                for (Account user : UserList) {
                    if (user.getUsername().equals(boundUsername)) {
                        exist = false;
                        System.out.println("\n该账号已经存在，无法进行绑定！");
                        System.out.println("请重新输入账号进行绑定！！\n");
                    }
                }
                if (exist == false) {
                    continue;
                }
                //储存username
                if (usernameB && exist) {
                    username = boundUsername;
                    System.out.println("绑定成功！\n");
                    return true;//返回绑定成功
                } else {
                    System.out.println("账号格式有误，请重新输入！\n");
                    return false;
                }
            }
        }

        //对账号注册的用户进行选择性邮箱或QQ绑定（非必须）-缺失部分功能
        if (order == "账号" && registFlag) {

            driver.userRegistBoundMune();//加载使用用户名注册的绑定菜单
            boolean flag = true;

            while (flag) {
                System.out.print("->请输入绑定类型: ");
                int command = sc.nextInt();
                switch (command) {
                    case 1://绑定邮箱
                        email = boundEQ("邮箱", sc);
                        if (email == "") {
                            break;
                        }
                        return true;


                    case 2://绑定QQ
                        QQ = boundEQ("QQ", sc);
                        if (QQ == "") {
                            break;
                        }
                        return true;

                    case 3://离开（不绑定）
//                        /addUserRegist();
                        return true;
                }
            }
        }

        return true;
    }//close method

    /*
     * 绑定邮箱和QQ
     */
    String boundEQ(String order, Scanner sc) {

        boolean flag = false;//循环和检验格式对错标志
        String tempAccount = "";//存储邮箱/QQ
        String inputPassword = "";//存储输入的校验密码

        while (!flag) {//循环输入直到绑定成功

            //输入密码校验
            System.out.print("\n密码校验！\n密码: ");
            inputPassword = sc.next();
            //校验密码继续绑定
            if (!password.equals(inputPassword)) {
                System.out.println("密码错误，校验失败，请重新操作！\n");
                return "";
            } else {
                System.out.println("密码一致，校验成功，请继续操作！\n");
            }


            //获取输入邮箱/QQ
            System.out.print("绑定的" + order + ": ");
            tempAccount = sc.next();

            //检验是否已经被绑定
            if ((order == "邮箱" && UnEmList.containsKey(tempAccount)) || (order == "QQ" && UnQQList.containsKey(tempAccount))) {
                System.out.println("\n该" + order + "已经被其它账号绑定，请重新输入进行绑定！\n");
                return "";
            }

            //格式检验
            if (order == "邮箱") {
                flag = driver.testEamil(tempAccount);
            }
            if (order == "QQ") {
                flag = driver.testQQ(tempAccount);
            }

            //是否注册成功检验
            if (flag) {
                System.out.println("\n" + order + "绑定成功！\n");
            } else {
                System.out.println("\n输入的" + order + "格式有误，请重新输入！\n");
                return "";
            }
        }
        return tempAccount;
    }//close method

    /*
     * 填写个人信息
     */
    void addPersonImformation(Scanner sc) {
        System.out.println("\n请填写个人信息！");
        System.out.print("性别: ");
        sex = sc.next();
        System.out.print("年龄: ");
        age = sc.nextInt();
        System.out.print("职业: ");
        career = sc.next();
        System.out.println("\n填写完毕！");
    }

    /*
     * 经过注册和绑定两个环节，将最终得到的账号+邮箱/QQ+密码写入注册信息列表
     */
    void addUserRegist() {

        Account userRegist = new Account(username, password, email, QQ, sex, age, career, new Date(), 3, true);
        UserRegistList.add(userRegist);
    }

    /*
     * 获取键盘输入的账号和密码
     */
    String[] getAccount(Scanner sc, String order) {

        System.out.print(order + ": ");
        String username = sc.next();
        System.out.print("密码: ");
        String password = sc.next();

        String[] list = {username, password};
        return list;
    }

    /*
     * 成功注册后自动登录，显示密码和账号以
     */
    void registToLogin() {

        //密码中间5位“*”显示
        int lenth = password.length();
        int centerIndex = (lenth - 5) / 2;//获得小数的最大整数
        String str = "*****";
        String secretPassword = password.substring(0, centerIndex) + str + password.substring(centerIndex + 5, lenth);

        //账号和密码展示
        System.out.println("\n\n账号和密码展示！");
        System.out.println("账号: " + username);
        System.out.println("密码: " + secretPassword);


    }

}
