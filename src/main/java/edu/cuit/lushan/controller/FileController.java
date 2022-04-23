package edu.cuit.lushan.controller;

import cn.hutool.core.bean.BeanUtil;
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
import edu.cuit.lushan.vo.CommentFileVO;
import edu.cuit.lushan.vo.CurrentDataDownloadRequestVO;
import edu.cuit.lushan.vo.FileVO;
import edu.cuit.lushan.vo.UserProofVO;
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

//    @PostMapping("/upload/data")
//    @DataLog
//    @WebLog
//    @CrossOrigin
//    public ResponseMessage uploadData(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
//        FileVO fileVO = getFileVO(multipartFile, lushanConfig.getDataRoot(), false);
//        DataFile dataFile = DataFile.builder()
//                .dataName(fileVO.getDataName())
//                .fileName(fileVO.getFileName())
//                .fileType(fileVO.getFileType())
//                .modifyUserId(userAgentUtil.getUserId(request))
//                .deviceId(request.getIntHeader("deviceId"))
//                .build();
//        if (dataFileService.save(dataFile)) {
//            return ResponseMessage.success(fileVO);
//        } else {
//            return ResponseMessage.errorMsg(2500, "Server Error!", fileVO);
//        }
//    }

    @PostMapping("/upload/user/proof")
    @DataLog
    @WebLog
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
            return ResponseMessage.successCodeMsgData(2000, "Upload proof file success!", userProofVO);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!");
        }
    }


    @PostMapping("/upload/commentFile")
    @DataLog
    @WebLog
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
            return ResponseMessage.successCodeMsgData(2000, "Upload comment file success!", commentFileVO);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!");
        }
    }

/*
    @GetMapping("/download/data")
    @DataLog
    @WebLog
    @CrossOrigin
    public void downloadData(String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {
            DataFile dataFile = dataFileService.getOneByDataFileName(fileName);
            if (dataFile == null) {
                log.info("not found");
                return;
            }
            // path是指想要下载的文件的路径
            File file = new File(lushanConfig.getDataRoot(),
                    dataFile.getFileName() + "." + dataFile.getFileType());
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            OutputStream outputStream = getOutputStream(response,
                    dataFile.getDataName() + "." + dataFile.getFileType(),
                    file,
                    "attachment;filename=");
            DownloadLog downloadLog = DownloadLog.builder()
                    .downloadFileName(dataFile.getFileName())
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
*/


    @GetMapping("/download/historyData")
    @DataLog
    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载历史数据", tags = {"文件下载上传"})
    public void downloadHistoryData(String dataName, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (dataName == null || dataName.equals("")) {
                throw new MyRuntimeException("The request data can not be empty!");
            }

            HistoryXlsxData historyXlsxData = historyXlsxDataService.getByName(dataName);
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

    @PostMapping("/download/currentData")
    @DataLog
    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载现代数据", tags = {"文件下载上传"})
    public ResponseMessage downloadCurrentData(@RequestBody CurrentDataDownloadRequestVO currentDataDownloadRequestVO,
                                    HttpServletResponse response, HttpServletRequest request) {
            if (BeanUtil.hasNullField(currentDataDownloadRequestVO)) {
                throw new MyRuntimeException("Request data can not be empty!", currentDataDownloadRequestVO);
            }
            // 判断时间区间是否超过一个月
            if (DateUtil.betweenMonth(DateUtil.parseDate(currentDataDownloadRequestVO.getFromDay()).toJdkDate(),
                    DateUtil.parseDate(currentDataDownloadRequestVO.getFromDay()).toJdkDate(), false) > 1) {
                throw new MyRuntimeException("The time interval can not be more than one month!");
            }
            List<CurrentData> currentDataList = currentDataService.getByDownloadVO(currentDataDownloadRequestVO);
            if (currentDataList.isEmpty()) {
                System.err.println(currentDataDownloadRequestVO);
                return ResponseMessage.notFound(currentDataDownloadRequestVO);
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
                    .build();
            Thread thread = new Thread(downLoadFileThread);
            thread.start();
            return ResponseMessage.success(currentDataDownloadRequestVO);
    }


    @GetMapping("/download/comment/{commentFileName}")
    @DataLog
    @WebLog(hasToken = false)
    @CrossOrigin
    @ApiOperation(value = "下载数据反馈文件", tags = {"文件下载上传"})
    public void downloadUserProof(@PathVariable String commentFileName, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (commentFileName == null) {
                return;
            }
            CommentFile commentFile = commentFileService.selectByFileName(commentFileName);
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
                    commentFileName + commentFile.getFileType(),
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
        if (requirePicture && !fileUtils.isPicture(ext)) {
            throw new Exception("User proof must be jpg, jpeg, png, pdf.");
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
