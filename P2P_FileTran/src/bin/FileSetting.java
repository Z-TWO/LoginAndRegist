package bin;

import java.io.Serializable;

/**
 * @Description:    文件实体类
 * @Author:         ZTwo
 * @CreateDate:     2020/11/14 10:45
 */
public class FileSetting implements Serializable {
    //文件名
    private String fileName;
    //文件类型
    private String fileType;
    //文件大小
    private long size;
    //文件内容
    private String stream;
    //文件源地址
    private String originPath;

    //get和set方法
    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getSize() {
        return size;
    }

    public String getStream() {
        return stream;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }
}
