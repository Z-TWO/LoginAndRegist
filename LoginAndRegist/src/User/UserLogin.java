package User;

import Dao.Login;
import Server.Account;
import Server.Driver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/*
 * @Description:    用户登陆和操作类
 *
 *                  1.登陆（账号/QQ/邮箱+密码检验） 检验账号是否存在，再检验对应账号是否被冻结/是否超过10天为登陆（可自助解冻），再检验对应密码
 *                  2.非绑定用户操作（个人信息查看，绑定QQ、邮箱）
 *                  3.绑定用户操作（个人信息查看，修改密码，修改账号，改绑/绑定邮箱/QQ）
 *
 * @CreateDate:     2020/10/12
 */
public class UserLogin implements Login {

    ArrayList<Account> UserList = new UserMain().UserList;//用户信息列表
    HashMap<String, String> UnPwList = new UserMain().UnPwList;//username+password
    HashMap<String, String> EmPwList = new UserMain().EmPwList;//email+passowrd
    HashMap<String, String> QQPwList = new UserMain().QQPwList;//QQ+password
    HashMap<String, String> UnEmList = new UserMain().UnEmList;//email+username
    HashMap<String, String> UnQQList = new UserMain().UnQQList;//QQ+username
    HashMap<String, Boolean> UnPrList = new UserMain().UnPrList;//username+privilege
    private String onLineName;//储存成功登陆的用户名
    private String onLineEmali;//储存成功登陆的emial
    private String onLineQQ;//储存成功登陆的QQ
    private String onLinePassword;//储存成功登陆的密码

    Driver driver;
    Scanner sc;

    @Override
    public void Login() {

        //选择登陆菜单
        driver = new Driver();
        sc = new Scanner(System.in);

        UserMain userMain = new UserMain();//主类
        boolean flag = true;

        while (flag) {

            //菜单调用
            driver.userSecMenu();
            //获取选择命令
            int command = sc.nextInt();
            switch (command) {

                case 1://账号登陆
                    login("账号");
                    break;

                case 2://邮箱登陆
                    login("邮箱");
                    break;

                case 3://QQ登陆
                    login("QQ");
                    break;

                case 4://返回上一级
                    userMain.setUpSwitch();
                    flag = false;


                default:
                    System.out.println("输入错误,请重新输入");
                    break;
            }
        }

    }

    /*
     * 不同登陆方式的操作类
     */
    void login(String order) {

        boolean onLine = false;//是否成功登陆标志

        if (order == "账号") {
            onLine = check(order, UnPwList);
            userOperation(order, onLine);
        }
        if (order == "邮箱") {
            onLine = check(order, EmPwList);
            userOperation(order, onLine);
        }
        if (order == "QQ") {
            onLine = check(order, QQPwList);
            userOperation(order, onLine);
        }
    }

    /*
     * 获取键盘输入的账号和密码
     */
    String[] getAccount(String order) {

        System.out.print(order + ": ");
        String username = sc.next();
        System.out.print("密码: ");
        String password = sc.next();
        System.out.print("返回上一级输入(输入\"back\"返回上一级,否则输入其它):");
        String goback = sc.next();


        String[] list = {username, password, goback};
        return list;
    }

    /*
     * 账号/邮箱/QQ+密码 的逻辑判断是否能够登陆
     */
    boolean check(String order, HashMap<String, String> imformationMap) {

        /**
         * 1.输入3次就冻结，并且将冻结写入UserList,将privilege改成false
         * 2.输入正确账号但是密码错了统计次数
         * 3.通过布尔值进行判断是否成功登陆
         */


        //变量
        Date today = new Date();           //创建今天的日期
        String userdateString = null;      //存储上一次登陆时间(String)
        Date userLastLoginTime = null;     //存储上一次登陆时间(Date)
        int passwordNum = 0;               //统计输入密码错误次数
        boolean loginfalg = false;         //是否成功登陆
        boolean temp = true;               //循环标志
        String account = "";               //存储账号
        String password = "";              //存储密码
        String[] list;                     //获取返回值
        String goback;                     //返回二级菜单标志

        //清空储存
        onLineName = "";
        onLineEmali = "";
        onLineQQ = "";

        while (temp) {

            String tempUsername = "";
            //通过方法获取输入的username和password
            list = getAccount(order);
            account = list[0];
            password = list[1];

            //跳出登陆返回用户二级菜单
            goback = list[2];
            if (goback.equals("back")) {
                break;
            }

            //获取username，方便判断是否存在后再判断是否被冻结
            if (order == "账号") {
                tempUsername = account;
            } else if (order == "邮箱") {
                tempUsername = UnEmList.get(account);
            } else if (order == "QQ") {
                tempUsername = UnQQList.get(account);
            }

            //判断该用户数是否存在
            if (!imformationMap.containsKey(account)) {
                System.out.println("\n" + order + "不存在,请重新输入！\n");
                temp = false;
                continue;
            }

            //获取该用户可以输入错误密码次数（必须要用户存在）
            for (Account user : UserList) {
                if (user.getUsername().equals(tempUsername)) {
                    passwordNum = user.getPasswordNum();
                }
            }

            //获取用户登陆时间差（必须要用户存在）
            for (Account user : UserList) {
                if (user.getUsername().equals(tempUsername)) {
                    userdateString = user.getRecentLoginDate();
                }
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //将获取到的String转换Date
            try {
                userLastLoginTime = format.parse(userdateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //获取时间差
            long diffeTime = ((today.getTime() - userLastLoginTime.getTime()) / (24 * 60 * 60 * 1000));


            //检验密码，是否冻结，多次输入错误密码冻结，是否登陆时间差是否超过10天
            if (diffeTime < 10 && imformationMap.containsKey(account) && imformationMap.get(account).equals(password) && UnPrList.get(tempUsername).equals(true) && passwordNum > 0) {

                System.out.println("\n登陆成功！\n");
                loginfalg = true;
                temp = false;

                //将登录时间修改nowtime
                for (Account user : UserList) {
                    if (user.getUsername().equals(tempUsername)) {
                        user.setRecentLoginDate(new Date());
                    }
                }

                //获取成功登陆的账号/emial/QQ
                getOnLineAccount(order, account);
                onLinePassword = password;
            } else {
                //存在判断
                if (!imformationMap.containsKey(account)) {//不存在判断
                    System.out.println("\n" + order + "不存在,请重新输入！\n");
                    temp = false;
                    continue;
                }

                //账号是否冻结判断
                if (UnPrList.get(tempUsername) == false) {//冻结
                    for (Account user : UserList) {
                        System.out.println(user);
                    }
                    System.out.println("\n该账号已经被冻结，无法登陆！\n");
                    selfThaw(tempUsername);//自助解冻(验证个人信息)
                    temp = false;
                    continue;
                }

                //判断是否能登陆
                if (diffeTime >= 10) {
                    System.out.println("\n该账号在10天内没有登陆，请进行验证信息自助解冻！");
                    frozenAccount(order, account);//将可登陆状态设置为false
                    selfThaw(tempUsername, 10);//自助解冻(验证密码+年龄)
                    temp = false;
                    continue;
                }

                //密码判断
                if (!imformationMap.get(account).equals(password)) {
                    System.out.println("\n密码错误,请重新输入！\n");
                    //在用户列表里面改变passwordNum值
                    for (Account user : UserList) {
                        if (user.getUsername().equals(tempUsername)) {
                            user.setPasswordNum(passwordNum - 1);
                        }
                    }
                    passwordNum -= 1;
                    //冻结处理
                    if (passwordNum <= 0) {
                        System.out.println("\n密码输入错误已经达到3次，账号被冻结，请联系管理员解冻！\n");
                        frozenAccount(order, account);//冻结
                        temp = false;
                        UserMain.updateList();
                        continue;
                    }
                }
            }
        }
        return loginfalg;
    }

    /*
     * 冻结账号
     */
    void frozenAccount(String order, String account) {
        for (Account user : UserList) {
            if (order == "账号" && user.getUsername().equals(account)) {
                user.setPrivilege(false);
            }
            if (order == "邮箱" && user.getEmail().equals(account)) {
                user.setPrivilege(false);
            }
            if (order == "QQ" && user.getQQ().equals(account)) {
                System.out.println(user.getPrivilege());
            }

        }
    }

    /*
     * 账号冻结，自助解冻
     * 检验密码+年龄
     */
    void selfThaw(String username) {
        System.out.print("是否继续自助解冻(Y/N): ");
        char command = sc.next().charAt(0);//获取Y/N
        if (command == 'Y') {
            //存储信息列表用户对应的个人信息
            String sex = "";
            int age = 0;
            String career = "";
            //获取键盘输入的个人信息
            System.out.println("\n请输入以下个人信息进行验证！");
            System.out.print("性别: ");
            String tempSex = sc.next();
            System.out.print("年龄: ");
            int tempAge = sc.nextInt();
            System.out.print("职业: ");
            String tempCareer = sc.next();
            //获取信息列表用户对应的个人信息
            for (Account user : UserList) {
                if (user.getUsername().equals(username)) {
                    sex = user.getSex();
                    age = user.getAge();
                    career = user.getCareer();
                }
            }
            //验证信息是否一致
            if (sex.equals(tempSex) && career.equals(tempCareer) && age == tempAge) {
                //解冻操作
                driver.thawAccount(username);
                new UserMain().getUnPrList();
                System.out.print("请重新登录！\n");
            } else {
                System.out.println("\n信息不一致，请再次自助或联系管理员进行解冻！");
                //限制登录
                for (Account user : UserList) {
                    if (user.getUsername().equals(username)) {
                        user.setPrivilege(false);
                    }
                }
            }

        }

    }

    /*
     * 10天未登陆，自助解冻（方法重载）
     * 检验性别+年龄+职业
     */
    void selfThaw(String username, int date) {
        System.out.print("是否继续自助解冻(Y/N): ");
        char command = sc.next().charAt(0);//获取Y/N
        if (command == 'Y') {
            //存储信息列表用户对应的个人信息
            String listPassword = "";
            int age = 0;
            //获取信息列表用户对应的个人信息
            for (Account user : UserList) {
                if (user.getUsername().equals(username)) {
                    listPassword = user.getPassword();
                    age = user.getAge();
                }
            }

            //获取键盘输入的个人信息
            System.out.println("\n请输入以下个人信息进行验证！");
            System.out.print("密码: ");
            String tempPassword = sc.next();
            System.out.print("年龄: ");
            int tempAge = sc.nextInt();

            //验证信息是否一致
            if (listPassword.equals(tempPassword) && age == tempAge) {
                //解冻操作
                driver.thawAccount(username);
                new UserMain().getUnPrList();
                System.out.print("请重新登录！\n");
            } else {
                System.out.println("\n信息不一致，请再次自助或联系管理员进行解冻！");
                //限制登录
                for (Account user : UserList) {
                    if (user.getUsername().equals(username)) {
                        user.setPrivilege(false);
                    }
                }
            }

        }

    }


    /*
     * 获取成功登陆的账号/emial/QQ
     */
    void getOnLineAccount(String order, String account) {

        if (order == "账号") {
            onLineName = account;
        }
        if (order == "邮箱") {
            onLineEmali = account;
        }
        if (order == "QQ") {
            onLineQQ = account;
        }
    }


    /**
     * 用户三级菜单操作
     * 1.分两类（未绑定+已经绑定QQ和邮箱）
     * 2.基本功能（查看个人信息、修改账号） 缺失功能（修改密码）
     */
    void userOperation(String order, boolean onLine) {

        String username = "";          //三级菜单操作搜索下标
        boolean bound = false;         //绑定用户和非绑定用户判断标准,初始化是false

        //判断到没有登陆成功则直接返回
        if (!onLine) {
            return;
        }

        //绑定用户与非绑定用户判断
        for (Account user : UserList) {
            //将登录成功的账号赋值给username
            username = onLineName;
            //对于账号登陆的进行是否已经绑定判断
            if (order == "账号" && user.getUsername().equals(onLineName) && !(user.getEmail() == "" && user.getQQ() == "")) {

                bound = true;
            }
        }
        if (order == "邮箱") {
            username = UnEmList.get(onLineEmali);
            bound = true;
        }
        if (order == "QQ") {
            username = UnQQList.get(onLineQQ);
            bound = true;
        }

        //将可输入错误密码次数初始化
        driver.passwordNUMSet3(username);
        //加载三级菜单
        driver.userThridMenu(bound);
        //进入三级菜单选择操作
        thirdMenuSwitch(bound, username);


    }

    /*
     * 用户三级菜单(分绑定和非绑定)
     */
    void thirdMenuSwitch(boolean bound, String username) {

        if (bound) { //进入绑定用操作

            while (true) {

                System.out.print("->请选择操作指令: ");
                int command = sc.nextInt();//选择操作命令

                switch (command) {

                    case 1://修改密码
                        onLinePassword = driver.changePassword(username, onLinePassword, sc, driver);//返回已经修改过的密码
                        int temp = 0; //存储次数
                        for (Account user : UserList) {//获取次数
                            if (user.getUsername().equals(username)) {
                                temp = user.getPasswordNum();
                            }
                        }
                        if (temp == 0) {//判断输入密码错误是否已经达到3次
                            driver.frozenAccount(username);//冻结账号，限制登录
                            System.out.println("密码输入错误已经达到3次，账号被冻结，请联系管理员解冻！");
                            return;
                        }
                        break;

                    case 2://修改账号
                        username = driver.changeUsername(username, sc, driver);//返回修改过的账号
                        onLineName = username;//并且将onLinename也进行修改，防止bug
                        break;

                    case 3://查看个人信息
                        driver.getUserInformation(username);
                        break;

                    case 4://绑定或改绑邮箱/QQ
                        boundEmailAndQQ("绑定或改绑", username);
                        break;

                    case 5://退出登陆
                        return;

                    default:
                        System.out.println("输入错误请重新输入！\n");
                        break;
                }
            }
        } else { //没有绑定用户操作

            while (true) {

                System.out.print("->请选择操作指令: ");
                int command = sc.nextInt();//选择操作命令

                switch (command) {

                    case 1://查看个人信息
                        driver.getUserInformation(username);
                        break;

                    case 2://绑定或改绑邮箱/QQ
                        boolean falg = boundEmailAndQQ("绑定", username);
                        if (falg) {
                            this.thirdMenuSwitch(onLineName);
                        }
                        break;

                    case 3://退出登陆
                        return;

                    default:
                        System.out.println("输入错误请重新输入！\n");
                        break;
                }
            }
        }
    }

    /*
     * 用户三级菜单（方法重载）
     * 绑定用户操作
     */
    void thirdMenuSwitch(String username) {


        System.out.println("\n自动更新用户操作界面！");
        new Driver().userThridMenu(true);
        while (true) {

            System.out.print("->请选择操作指令: ");
            int command = sc.nextInt();//选择操作命令

            switch (command) {

                case 1://修改密码
                    onLinePassword = driver.changePassword(username, onLinePassword, sc, driver);//返回已经修改过的密码
                    int temp = 0; //存储次数
                    for (Account user : UserList) {//获取次数
                        if (user.getUsername().equals(username)) {
                            temp = user.getPasswordNum();
                        }
                    }
                    if (temp == 0) {//判断输入密码错误是否已经达到3次
                        driver.frozenAccount(username);//冻结账号，限制登录
                        System.out.println("密码输入错误已经达到3次，账号被冻结，请联系管理员解冻！");
                        return;
                    }
                    break;

                case 2://修改账号
                    username = driver.changeUsername(username, sc, driver);//返回修改过的账号
                    onLineName = username;//并且将onLinename也进行修改，防止bug
                    break;

                case 3://查看个人信息
                    driver.getUserInformation(username);
                    break;

                case 4://绑定或改绑邮箱/QQ
                    boundEmailAndQQ("绑定或改绑", username);
                    break;

                case 5://退出登陆
                    UserMain userMain = new UserMain();
                    userMain.setUpSwitch();

                default:
                    System.out.println("输入错误请重新输入！\n");
                    break;
            }
        }
    }


    /*
     * 用户已登陆绑定操作
     */
    boolean boundEmailAndQQ(String word, String username) {

        String order;
        String tempContainer;
        String getPassword = "";
        String inputPassword;

        //输入密码校验
        System.out.print("\n密码校验！\n密码: ");
        inputPassword = sc.next();
        //校验密码继续绑定
        for (Account user : UserList) {//获取用户密码
            if (user.getUsername().equals(username)) {
                getPassword = user.getPassword();
            }
        }
        if (!getPassword.equals(inputPassword)) {
            System.out.println("密码错误，校验失败，请重新操作！\n");
            return false;
        } else {
            System.out.println("密码一致，校验成功，请继续操作！");
        }

        //获取输入绑定类型
        System.out.print("\n请输入绑定类型(邮箱/QQ): ");
        order = sc.next();
        //获取输入的QQ或者邮箱
        System.out.print("请输入要" + word + order + ": ");
        tempContainer = sc.next();

        //判断格式
        if ((order.equals("邮箱") && !driver.testEamil(tempContainer)) || order.equals("QQ") && !driver.testQQ(tempContainer)) {
            System.out.println("\n" + order + "格式错误！\n");
            return false;
        }
        //判断是否已经被绑定
        if ((order == "QQ" && UnQQList.containsKey(tempContainer)) || (order == "邮箱" && UnEmList.containsKey(tempContainer))) {
            System.out.println("\n" + order + "已经被绑定！\n");
            return false;
        }
        //添加进用户信息列表
        for (Account user : UserList) {
            if (user.getUsername().equals(username)) {
                if (order.equals("邮箱")) {
                    user.setEmail(tempContainer);
                }
                if (order.equals("QQ")) {
                    user.setQQ(tempContainer);
                }
            }
        }
        System.out.println("\n" + word + "成功！");
        System.out.println("提醒：之前为未绑定用户请重新登录进入新界面！！！\n");
        return true;
    }

}
