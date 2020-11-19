package Server;

import User.UserMain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * @Description:    文件操作类（写入和读取）
 *
 *                  1.读取：将“用户信息.csv”中的信息通过处理将，数据通过创建Account对象添加到UserList中
 *                  2.写入：将UserList中的信息写入到“用户信息.csv”
 *
 * @CreateDate:     2020/10/12
 */
public class FileUtil {

    static ArrayList<Account> UserList = new UserMain().UserList;
    private static final String SEPARATE_FIELD = ",";//字段分割符
    private static final String SEPARATE_LINE = "\r\n";//行分割符
    private static final String TITLE = "账号,密码,邮箱,QQ,性别,年龄,职业,可登陆状态,最后一次登陆时间";
    private static File file = new File("用户信息.csv");

    public static void WriteUserList() {

        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//字节输出流
            StringBuffer sb = new StringBuffer();//拼接接受容器

            sb.append(TITLE);//添加第一行
            for (Account user : UserList) {
                //改变QQ和emil数据形式
                String email = user.getEmail();
                String QQ = user.getQQ();
                if (QQ == "") {
                    QQ = "未绑定";
                }
                if (email == "") {
                    email = "未绑定";
                }
                //将数据进行拼接，并添加到StringBuffer
                sb.append(SEPARATE_LINE + user.getUsername() + SEPARATE_FIELD +
                        user.getPassword() + SEPARATE_FIELD +
                        email + SEPARATE_FIELD +
                        QQ + SEPARATE_FIELD +
                        user.getSex() + SEPARATE_FIELD +
                        user.getAge() + SEPARATE_FIELD +
                        user.getCareer() + SEPARATE_FIELD +
                        user.getPrivilege() + SEPARATE_FIELD +
                        user.getRecentLoginDate() + SEPARATE_FIELD);
            }

            try {
                osw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));//添加BOM信息避免输出的csv出现乱码
                osw.write(sb.toString());
                osw.flush();
                osw.close();//关闭输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void ReadUserList() {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + SEPARATE_FIELD);
            }
            br.close();//关闭输入流

            String[] strOne = sb.toString().split(SEPARATE_LINE);//将获取到字符串去掉\r\n
            String[] strTwo = strOne[0].split(SEPARATE_FIELD);//用","将字符分割
            String[] strThree;//存储没有“”的字符串

            //去除""
            StringBuffer strThreeBUffer = new StringBuffer();
            String strTemp;
            for (String str : strTwo) {
                if (!str.equals("")) {
                    strThreeBUffer.append(str + SEPARATE_FIELD);
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Date对象格式化对象
            strTemp = strThreeBUffer.toString();
            strThree = strTemp.split(SEPARATE_FIELD);//最终得到只有“，”分割的信息数组


            //将数组进行拆分，并创建成对象
            int num = strThree.length / 9 - 1;//用户数
            for (int i = 9; i < strThree.length; ) {

                //分别获取信息
                String username = strThree[i];
                String password = strThree[i + 1];
                String email = strThree[i + 2];
                String QQ = strThree[i + 3];
                String sex = strThree[i + 4];
                int age = Integer.parseInt(strThree[i + 5]);
                String career = strThree[i + 6];
                Boolean privilege = Boolean.parseBoolean(strThree[i + 7]);
                Date date = format.parse(strThree[i + 8]);

                //修改QQ和email
                if (email.equals("未绑定")) {
                    email = "";
                }
                if (QQ.equals("未绑定")) {
                    QQ = "";
                }

                //将信息用来创建对象，并加入到列表
                Account user = new Account(username, password, email, QQ, sex, age, career, date, 3, privilege);
                UserList.add(user);
                i += 9;
            }


        } catch (Exception e) {
            System.out.println("还没有用户注册！！！");
        }


    }


}
