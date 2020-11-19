package Server;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * @Description:    用户信息实体类
 *
 *                  1.信息包括（账号，密码，QQ，邮箱，性别，年龄，职业，登陆权限，最近登录时间，可输入错误密码次数）
 *                  2.全变变量的get和set方法
 *                  3.重写toString
 *
 * @CreateDate:     2020/10/12
 */
public class Account {

    private String username;        //账号
    private String password;        //密码
    private String QQ;              //QQ
    private String email;           //邮箱
    private Date date;              //最近登录时间
    private Boolean privilege;      //访问权限
    private String sex;             //性别
    private String career;          //职业
    private int age;                //年龄
    private int passwordNum;        //修改密码次数


    /*
     * 有参构造函数
     * 用于创建用户对象
     * @date 2020/10/1
     */
    public Account(String username, String password, String email, String QQ, String sex, int age, String career, Date date, int passwordNum, Boolean privilege) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.QQ = QQ;
        this.sex = sex;
        this.age = age;
        this.career = career;
        this.date = date;
        this.passwordNum = passwordNum;
        this.privilege = privilege;
    }

    //get 和 set
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getRecentLoginDate() {
        return dateToString(this.date);
    }

    public void setRecentLoginDate(Date recentLoginDate) {
        this.date = recentLoginDate;
    }

    public Boolean getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(Boolean privilege) {
        this.privilege = privilege;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getPasswordNum() {
        return passwordNum;
    }

    public void setPasswordNum(int passwordNum) {
        this.passwordNum = passwordNum;

    }

    //Date格式转化
    public String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//hh-12小时制  HH-24小时制
        return format.format(date);
    }

    @Override
    public String toString() {
        String tempQQ = this.QQ;
        String tempEmail = this.email;
        if (tempQQ == "") {
            tempQQ = "未绑定";
        }
        if (tempEmail == "") {
            tempEmail = "未绑定";
        }
        return "账号: " + username + ' ' +
                ", 密码: " + password + ' ' +
                ", QQ: " + tempQQ + ' ' +
                ", email: " + tempEmail + ' ' +
                ", 性别: " + sex + ' ' +
                ", 年龄: " + age + ' ' +
                ", 职业: " + career + ' ' +
                ", 可登陆状态: " + privilege + ' ' +
                ", 最近登录时间: " + getRecentLoginDate();
    }

}
