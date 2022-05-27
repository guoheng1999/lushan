package edu.cuit.lushan.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import edu.cuit.lushan.config.LushanConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class FileDeleteTask {
    @Autowired
    LushanConfig lushanConfig;

//    @Scheduled(cron="0 0 */1 * * ?")
    @Scheduled(cron="* */1 * * * ?")
    public void deleteFiles(){
        DateTime dateTime = DateUtil.date();
        String path = String.format("%s_%s_%s_%s",
                dateTime.year(), dateTime.month(), dateTime.dayOfMonth(), dateTime.hour(true));
        File file = new File(String.format("%s/%s",lushanConfig.getBufferDataRoot(), path));
        if (file.exists()){
            log.info(String.format("%s/%s",lushanConfig.getBufferDataRoot(), path));
            delFolder(String.format("%s/%s",lushanConfig.getBufferDataRoot(), path));
        }
    }
    /***
     * 删除指定文件夹下所有文件
     *
     * @param path
     *            文件夹相对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /***
     * 删除文件夹
     *
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
