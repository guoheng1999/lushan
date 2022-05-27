package edu.cuit.lushan.thread;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import edu.cuit.lushan.utils.EmailUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DownLoadFileThread implements Runnable{
    private String email;
    private File[] files;
    private String targetFileName;
    private String root;
    private String linkRoot;
    private String fromDay;
    private String endDay;
    private String dataName;
    @Override
    public void run() {
//        System.err.println(files.length);
        DateTime dateTime = DateUtil.date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.add(Calendar.DATE, 1);
        DateTime outOfDate = DateUtil.date(calendar.getTime());
        // 按照当前时间生成文件夹名
        String timeStr = String.format("%s_%s_%s_%s",
                outOfDate.year(), outOfDate.month(), outOfDate.dayOfMonth(), outOfDate.hour(true));
        // 生成文件即将存放到的文件夹名
        String path = String.format("%s/%s", root, timeStr);
        log.info("path= " + path);
        // 判断文件夹是否存在，不存在则创建
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        File zip = ZipUtil.zip(FileUtil.file(path, String.format("%s", targetFileName)), true, files);
        System.err.println("文件存在： " +  zip.exists());
        System.err.println("文件存在： " +  zip.getPath());
        String link = String.format("%s/%s/%s", linkRoot, timeStr,targetFileName);
        try {
            EmailUtil.sendDataPackageMail(email, dataName, fromDay, endDay, link);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
