package edu.cuit.lushan.thread;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import edu.cuit.lushan.utils.EmailUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
        System.err.println(files.length);
        File zip = ZipUtil.zip(FileUtil.file(root, String.format("%s", targetFileName)), true, files);
        System.err.println("文件存在： " +  zip.exists());
        System.err.println("文件存在： " +  zip.getPath());
        String link = String.format("%s/%s", linkRoot, targetFileName);
        try {
            EmailUtil.sendDataPackageMail(email, dataName, fromDay, endDay, link);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
