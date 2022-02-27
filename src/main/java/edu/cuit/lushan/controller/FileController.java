package edu.cuit.lushan.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import edu.cuit.lushan.config.LushanConfig;
import edu.cuit.lushan.entity.DataFile;
import edu.cuit.lushan.entity.DownloadLog;
import edu.cuit.lushan.entity.UserProof;
import edu.cuit.lushan.service.IDataFileService;
import edu.cuit.lushan.service.IDownloadLogService;
import edu.cuit.lushan.service.IUserProofService;
import edu.cuit.lushan.utils.FileUtil;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.FileVO;
import edu.cuit.lushan.vo.UserProofVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/file")
@RequiresRoles({"USER"})
public class FileController {

    @Autowired
    IDataFileService dataFileService;
    @Autowired
    IDownloadLogService downloadLogService;
    @Autowired
    IUserProofService userProofService;
    @Autowired
    UserAgentUtil userAgentUtil;
    @Autowired
    LushanConfig lushanConfig;
    @Autowired
    FileUtil fileUtil;

    @PostMapping("/upload/data")
    @RequiresRoles("MANAGER")
    public ResponseMessage uploadData(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        FileVO fileVO = getFileVO(multipartFile, lushanConfig.getDataRoot(), false);
        return ResponseMessage.success(fileVO);
    }

    @PostMapping("/upload/user/proof")
    public ResponseMessage uploadUserProof(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        FileVO fileVO = getFileVO(multipartFile, lushanConfig.getProofRoot(), true);
        UserProof userProof = new UserProof();
        userProof.setUserId(userAgentUtil.getUserId(request))
                .setFilePathName(fileVO.getFileName())
                .setModifyUserId(userAgentUtil.getUserId(request))
                .setFileType(fileVO.getFileType());
        if (userProofService.save(userProof)) {
            UserProofVO userProofVO = UserProofVO.builder()
                    .filePathName(userProof.getFilePathName())
                    .userId(userProof.getUserId())
                    .fileType(userProof.getFileType())
                    .build();
            return ResponseMessage.successCodeMsgData(2000,"Upload proof file success!",userProofVO);
        }else {
            return ResponseMessage.errorMsg(2500, "Server error!");
        }
    }


    @PostMapping("/download/data")
    public void downloadData(String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {

            DataFile dataFile = dataFileService.getOneByDataFileName(fileName.substring(0, fileName.lastIndexOf(".")));
            if (dataFile == null){
                return;
            }
            // path是指想要下载的文件的路径
            File file = new File(lushanConfig.getDataRoot(), fileName);
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            DownloadLog downloadLog = DownloadLog.builder()
                    .dowmloadFileId(dataFile.getId().toString())
                    .downloadIp(userAgentUtil.getIpAddress(request))
                    .downloadTime(LocalDateTimeUtil.of(new Date()))
                    .downloadUserId(userAgentUtil.getUserId(request))
                    .build();
            downloadLogService.save(downloadLog);
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @PostMapping("/download/user/proof")
    public void downloadUserProof(String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {

            DataFile dataFile = dataFileService.getOneByDataFileName(fileName.substring(0, fileName.lastIndexOf(".")));
            if (dataFile == null){
                return;
            }
            // path是指想要下载的文件的路径
            File file = new File(lushanConfig.getDataRoot(), fileName);
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            DownloadLog downloadLog = DownloadLog.builder()
                    .dowmloadFileId(dataFile.getId().toString())
                    .downloadIp(userAgentUtil.getIpAddress(request))
                    .downloadTime(LocalDateTimeUtil.of(new Date()))
                    .downloadUserId(userAgentUtil.getUserId(request))
                    .build();
            downloadLogService.save(downloadLog);
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 讲存储文件的方法封装，通过传入multipartFile，返回DataFile对象
    private FileVO getFileVO(MultipartFile multipartFile, String rootPath, boolean requirePicture) throws Exception {
        File folder = new File(rootPath);
        String originalFilename = multipartFile.getOriginalFilename();
        String oldName = originalFilename.substring(0, originalFilename.indexOf("."));
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1)
                .replace(".", "").toLowerCase();
        String newName = UUID.randomUUID().toString();
        // requirePicture是确定是否需要判断为图片格式，当其为真时为需要判断，则进入isPicture方法中。
        // 否则表达式为假，跳过判断。
        if (requirePicture && !fileUtil.isPicture(ext)) {
            throw new Exception("User proof must be jpg, jpeg, png, gif.");
        }
        File file = new File(folder, newName + "." + ext);
        multipartFile.transferTo(file);
        return FileVO.builder()
                .dataName(oldName)
                .fileName(newName)
                .fileType(ext)
                .build();
    }
}
