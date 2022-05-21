package edu.cuit.lushan.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.config.LushanConfig;
import edu.cuit.lushan.entity.*;
import edu.cuit.lushan.exception.MyRuntimeException;
import edu.cuit.lushan.service.*;
import edu.cuit.lushan.thread.DownLoadFileThread;
import edu.cuit.lushan.utils.CodeUtil;
import edu.cuit.lushan.utils.FileUtils;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/file")
@CrossOrigin
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
    FileUtils fileUtils;
    @Autowired
    ICurrentDataService currentDataService;
    @Autowired
    IHistoryXlsxDataService historyXlsxDataService;
    @Autowired
    IHistoryPictureDataService historyPictureDataService;
    @Autowired
    ICommentFileService commentFileService;
    @Autowired
    ICommentService commentService;
    @Autowired
    IDeviceService deviceService;

    @PostMapping("/upload/user/proof")
    @DataLog
//    @WebLog
    @CrossOrigin
    @ApiOperation(value = "用户上传审核资料", tags = {"文件下载上传"})
    public ResponseMessage uploadUserProof(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        FileVO fileVO = getFileVO(multipartFile, lushanConfig.getProofRoot(), true);
        UserProof userProof = new UserProof();
        userProof.setUserId(userAgentUtil.getUserId(request))
                .setFileName(fileVO.getFileName())
                .setModifyUserId(userAgentUtil.getUserId(request))
                .setFileType(fileVO.getFileType());

        if (userProofService.save(userProof)) {
            UserProofVO userProofVO = UserProofVO.builder()
                    .filePathName(userProof.getFileName())
                    .userId(userProof.getUserId())
                    .fileType(userProof.getFileType())
                    .build();
            return ResponseMessage.successCodeMsgData(2000, "审核材料上传成功！", userProofVO);
        } else {
            throw new MyRuntimeException(2500, "资料上传失败，请重试！");
        }
    }


    @PostMapping("/upload/commentFile")
    @DataLog
//    @WebLog
    @CrossOrigin
    @ApiOperation(value = "用户上传数据反馈文件", tags = {"文件下载上传"})
//    @ApiImplicitParam(name = "commentId", value = "数据反馈id", required = true, dataType = "int", paramType = "header")
    public ResponseMessage uploadCommentFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("commentId") String commentId,HttpServletRequest request) throws Exception {
        FileVO fileVO = getFileVO(multipartFile, lushanConfig.getCommentRoot(), false);
        CommentFile commentFile = new CommentFile();
        commentFile.setCommentId(userAgentUtil.getUserId(request))
                .setFileName(fileVO.getFileName())
                .setModifyUserId(userAgentUtil.getUserId(request))
                .setCommentId(Integer.valueOf(commentId))
                .setFileType(fileVO.getFileType());
        if (commentFileService.save(commentFile)) {
            CommentFileVO commentFileVO = CommentFileVO.builder()
                    .fileName(commentFile.getFileName())
                    .commentId(commentFile.getCommentId())
                    .fileType(commentFile.getFileType())
                    .build();
            return ResponseMessage.successCodeMsgData(2000, "反馈文件上传成功！", commentFileVO);
        } else {
            return ResponseMessage.serverError(null);
        }
    }

    @PostMapping("/download/historyData")
    @DataLog
//    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载历史数据", tags = {"文件下载上传"})
    public void downloadHistoryData(@RequestBody HistoryDataDownloadVO historyDataDownloadVO, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (BeanUtil.hasNullField(historyDataDownloadVO)) {
                throw new MyRuntimeException("系统未知错误，请重启浏览器或与管理员联系。");
            }

            HistoryXlsxData historyXlsxData = historyXlsxDataService.getByName(historyDataDownloadVO.getDataName());
            if (historyXlsxData == null) {
                return;
            }

            File file = FileUtil.file(lushanConfig.getHistoryDataRoot(), historyXlsxData.getPath(), String.format("%s.xlsx", historyXlsxData.getDataName()));
            if(!file.exists()){
                file = FileUtil.file(lushanConfig.getHistoryDataRoot(), historyXlsxData.getPath(), String.format("%s.xls", historyXlsxData.getDataName()));
            }
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            OutputStream outputStream = getOutputStream(response,
                    file.getName(),
                    file,
                    "inline;filename=");

            DownloadLog downloadLog = DownloadLog.builder()
                    .downloadFileName(historyXlsxData.getDataName())
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


    @PostMapping("/download/historyData/picture")
    @DataLog
//    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载历史数据原始记录", tags = {"文件下载上传"})
    public void downloadHistoryData(@RequestBody HistoryPictureDownloadVO historyPictureDownloadVO,HttpServletResponse response, HttpServletRequest request) {
        try {
            if (BeanUtil.hasNullField(historyPictureDownloadVO)) {
                throw new MyRuntimeException("系统未知错误，请重启浏览器或与管理员联系。");
            }

            HistoryXlsxData historyXlsxData = historyXlsxDataService.getByName(historyPictureDownloadVO.getDataName());
            if (historyXlsxData == null) {
                return;
            }
            HistoryPictureData historyPictureData = historyPictureDataService.getByHistoryNameAndPicName(
                    historyPictureDownloadVO.getDataName(), historyPictureDownloadVO.getPicName());
            System.err.println(historyPictureData);
            File file = FileUtil.file(
                    lushanConfig.getHistoryDatasetPicture(),
                    historyPictureData.getHistoryXlsxDataName(),
                    historyPictureData.getPicName());
            System.err.println(file.getPath());
            if(!file.exists()){
                return;
            }
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            OutputStream outputStream = getOutputStream(response,
                    file.getName(),
                    file,
                    "inline;filename=");

            DownloadLog downloadLog = DownloadLog.builder()
                    .downloadFileName(historyXlsxData.getDataName())
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

    @PostMapping("/download/currentData")
    @DataLog
//    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载现代数据", tags = {"文件下载上传"})
    public ResponseMessage downloadCurrentData(@RequestBody CurrentDataDownloadRequestVO currentDataDownloadRequestVO,
                                    HttpServletResponse response, HttpServletRequest request) {
            if (BeanUtil.hasNullField(currentDataDownloadRequestVO)) {
                throw new MyRuntimeException("请求数据不可为空。", currentDataDownloadRequestVO);
            }
            // 判断时间区间是否超过一个月
        long fromTime = DateUtil.parseDate(currentDataDownloadRequestVO.getFromDay()).getTime();
        long endTime = DateUtil.parseDate(currentDataDownloadRequestVO.getEndDay()).getTime();
        if (endTime - fromTime > 31L * 24 * 60 * 60 * 1000) { throw new MyRuntimeException("请您将要下载数据的时间范围设定在一个月内。"); }
        // 查询设备名称
        Device device = deviceService.getById(currentDataDownloadRequestVO.getDeviceId());
        if (device == null) {
            throw new MyRuntimeException("设备不存在。");
        }
        List<CurrentData> currentDataList = currentDataService.getByDownloadVO(currentDataDownloadRequestVO);
        if (currentDataList.isEmpty()) {
            System.err.println(currentDataDownloadRequestVO);
            return ResponseMessage.errorMsg( 2404,
                    String.format("未找到%s到%s期间的观测数据。",
                        currentDataDownloadRequestVO.getFromDay().substring(0, 10),
                        currentDataDownloadRequestVO.getEndDay().substring(0, 10)),
                    currentDataDownloadRequestVO);
        }
        System.err.println(currentDataList.size());
        List<File> fileList = new LinkedList<>();
            currentDataList.forEach(e -> {
                File file = FileUtil.file(lushanConfig.getCurrentDataRoot(), String.format("%s/%s%s", e.getPath(), e.getDataName(), e.getFileType()));
                if (file.exists()) {
                    fileList.add(file);
                }
            });
            System.err.println("fileList.size()" + fileList.size());
            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            String targetFileName = String.format("%s.zip", CodeUtil.createCodeString(32));
            DownLoadFileThread downLoadFileThread = DownLoadFileThread.builder()
                    .email(currentDataDownloadRequestVO.getEmail())
                    .files(files)
                    .root(lushanConfig.getBufferDataRoot())
                    .targetFileName(targetFileName)
                    .linkRoot(lushanConfig.getLinkRoot())
                    .endDay(currentDataDownloadRequestVO.getEndDay())
                    .fromDay(currentDataDownloadRequestVO.getFromDay())
                    .dataName(device.getDeviceName())
                    .build();
            Thread thread = new Thread(downLoadFileThread);
            thread.start();
            return ResponseMessage.success(currentDataDownloadRequestVO);
    }


    @PostMapping("/download/comment")
    @DataLog
//    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载数据反馈文件", tags = {"文件下载上传"})
    public void downloadCommentFile(@RequestBody CommentFileDowmloadVO commentFileDowmloadVO, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (commentFileDowmloadVO == null) {
                return;
            }
            CommentFile commentFile = commentFileService.selectByFileName(commentFileDowmloadVO.getFileName());
            if (commentFile == null) {
                return;
            }
            File file = FileUtil.file(lushanConfig.getCommentRoot(), String.format("%s.%s",  commentFile.getFileName(), commentFile.getFileType()));
            if (!file.exists()) {
                System.err.println(file.getPath());
                return;
            }
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            OutputStream outputStream = getOutputStream(response,
                    String.format("%s.%s",  commentFile.getFileName(), commentFile.getFileType()),
                    file,
                    "inline;filename=");

            DownloadLog downloadLog = DownloadLog.builder()
                    .downloadFileName(commentFile.getFileName())
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
    @DataLog
//    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载用户审核资料", tags = {"文件下载上传"})
    public void downloadUserProof(@RequestBody UserProofDownloadVO userProofDownloadVO, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (userProofDownloadVO == null || BeanUtil.hasNullField(userProofDownloadVO)) {
                return;
            }
            UserProof userProof = userProofService.getByUserProofFileName(userProofDownloadVO.getFileName());
            if (userProof == null) {
                return;
            }
            File file = FileUtil.file(lushanConfig.getProofRoot(), String.format("%s.%s",  userProof.getFileName(), userProof.getFileType()));
            if (!file.exists()) {
                System.err.println(file.getPath());
                return;
            }
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            OutputStream outputStream = getOutputStream(response,
                    String.format("%s.%s",  userProof.getFileName(), userProof.getFileType()),
                    file,
                    "inline;filename=");

            DownloadLog downloadLog = DownloadLog.builder()
                    .downloadFileName(userProof.getFileName())
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



    private OutputStream getOutputStream(HttpServletResponse response, String fileFullName, File file, String s) throws IOException {
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", s + URLEncoder.encode(fileFullName, "UTF-8"));
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        return outputStream;
    }

    // 将存储文件的方法封装，通过传入multipartFile，返回DataFile对象
    private FileVO getFileVO(MultipartFile multipartFile, String rootPath, boolean requirePicture) throws Exception {
        File folder = new File(rootPath);
        String originalFilename = multipartFile.getOriginalFilename();
        String oldName = originalFilename.substring(0, originalFilename.indexOf("."));
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                .replace(".", "").toLowerCase();
        String newName = UUID.randomUUID().toString();
        // requirePicture是确定是否需要判断为图片格式，当其为真时为需要判断，则进入isPicture方法中。
        // 否则表达式为假，跳过判断。
        if (requirePicture && !fileUtils.isPicture(ext) && !fileUtils.isPdf(ext)) {
            throw new Exception("对不起，文件格式只能为jpg, jpeg, png, pdf！");
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
