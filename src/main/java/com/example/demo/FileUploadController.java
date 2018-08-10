package com.example.demo;

import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.*;
import java.util.*;

import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;

/**
 * Created by i-feng on 2018/7/3.
 */

//@RestController("/uploads")
@Controller
@RequestMapping("/uploads")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileService fileService;

    @GetMapping
    public String index() {
        return "WEB-INF/index";
    }

    @PostMapping("/upload1")
    @ResponseBody
    public Object upload1(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

        String str =  "2018-02-06 12:12:18";
        Date date = DateUtil.smartFormat(str);
        log.info(date.toString());

        log.info("[文件类型] - [{}]", file.getContentType());
        log.info("[文件名称] - [{}]", file.getOriginalFilename());
        log.info("[文件大小] - [{}]", file.getSize());
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理，此处只是为了演示故将地址写成本地电脑指定目录）
//        file.transferTo(new File("F:\\app\\chapter16\\" + file.getOriginalFilename()));

        String filePath = request.getSession().getServletContext().getRealPath("WEB-INF/excel/");
        log.info("[文件路径] - [{}]", filePath);

        if (!file.getOriginalFilename().equals("打卡.xls")) {
            return "请正确上传文件，文件名：打卡.xls";
        }

//        try {
//            FileUtil.uploadFile(file.getBytes(), filePath, file.getOriginalFilename());
//        } catch (Exception e) {
//            // TODO: handle exception
//            log.error(e.toString());
//        }

        Map<String, String> result = new HashMap<>(16);
        result.put("contentType", file.getContentType());
        result.put("fileName", file.getOriginalFilename());
        result.put("fileSize", file.getSize() + "");
        return result;
    }

    @PostMapping("/upload2")
    @ResponseBody
    public void upload2(@RequestParam("file") MultipartFile[] files, HttpServletResponse response) throws IOException {
        if (files == null || files.length == 0) {
            return;
        }
//        List<Map<String, String>> results = new ArrayList<>();
        MultipartFile file1 = null;
        MultipartFile file2 = null;
        MultipartFile file3 = null;
        for (MultipartFile file : files) {
            // TODO Spring Mvc 提供的写入方式
            if (file.getOriginalFilename().equals("打卡.xls")){
                file1 = file;
            }
            if (file.getOriginalFilename().equals("签卡.xls")){
                file2 = file;
            }
            if (file.getOriginalFilename().equals("请假.xls")){
                file3 = file;
            }
        }

        Map<String, List<Card>> map = fileService.handExcelByPunchCard(file1,file2,file3);

        // 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
// 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("员工表一");
// 添加表头行
        HSSFRow hssfRow = sheet.createRow(0);
// 设置单元格格式居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CENTER);

// 添加表头内容
        HSSFCell headCell = hssfRow.createCell(0);
        headCell.setCellValue("部门名称");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(1);
        headCell.setCellValue("员工编号");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(2);
        headCell.setCellValue("姓名");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(3);
        headCell.setCellValue("开始日期");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(4);
        headCell.setCellValue("结束日期");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(5);
        headCell.setCellValue("打卡次数");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(6);
        headCell.setCellValue("请假开始时间");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(7);
        headCell.setCellValue("请假时长");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(8);
        headCell.setCellValue("请假类别");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(9);
        headCell.setCellValue("描述");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(10);
        headCell.setCellValue("请假状态");
        headCell.setCellStyle(cellStyle);

        // 添加数据内容

        int i = 0;
        for (String key : map.keySet()) {
            List<Card> list = map.get(key);
            for (Card card : list) {
                hssfRow = sheet.createRow((int) i + 1);
                i++;
                // / 创建单元格，并设置值
                HSSFCell cell = hssfRow.createCell(0);
                String department = card.getDepartment();
                cell.setCellValue(department);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(1);
                String Id = card.getId().toString();
                cell.setCellValue(Id);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(2);
                String name = card.getName();
                cell.setCellValue(name);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(3);
                String startTime = card.getStartDateStr();
                cell.setCellValue(startTime);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(4);
                String endTime = card.getEndDateStr();
                cell.setCellValue(endTime);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(5);
                String count = card.getCount();
                if (count == null) {
                    count = " ";
                }
                cell.setCellValue(count);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(6);
                String timeOutStart = card.getTimeOutStart();
                if (timeOutStart == null) {
                    timeOutStart = " ";
                }
                cell.setCellValue(timeOutStart);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(7);
                String length = card.getTimeLength() + "";
                if (length == null) {
                    length = " ";
                }
                cell.setCellValue(length);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(8);
                String type = card.getLeaveType();
                if (type == null) {
                    type = " ";
                }
                cell.setCellValue(type);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(9);
                String description = card.getDescription();
                if (description == null) {
                    description = " ";
                }
                cell.setCellValue(description);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(10);
                String timeOutStatus = card.getTimeOutStatus();
                if (timeOutStatus == null) {
                    timeOutStatus = " ";
                }
                cell.setCellValue(timeOutStatus);
                cell.setCellStyle(cellStyle);
            }
        }


        String fileName = "LFang-" + new Date().getTime() + ".xls";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());

        return;
    }

    @PostMapping("/upload3")
    @ResponseBody
    public void upload2(String base64) throws IOException {
        // TODO BASE64 方式的 格式和名字需要自己控制（如 png 图片编码后前缀就会是 data:image/png;base64,）
        final File tempFile = new File("F:\\app\\chapter16\\test.jpg");
        // TODO 防止有的传了 data:image/png;base64, 有的没传的情况
        String[] d = base64.split("base64,");
        final byte[] bytes = Base64Utils.decodeFromString(d.length > 1 ? d[1] : d[0]);
        FileCopyUtils.copy(bytes, tempFile);

    }

    //处理文件上传
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImg(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        log.info("fileName-->" + fileName);
        log.info("getContentType-->" + contentType);
        String filePath = request.getSession().getServletContext().getRealPath("excel/");
//        try {
//            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
        //返回json
        return "upload success";
    }

}
