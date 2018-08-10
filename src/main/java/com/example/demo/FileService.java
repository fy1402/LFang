package com.example.demo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by i-feng on 2018/7/3.
 */
public interface FileService {

//    public void cleanCard();

    public Map<String, List<Card>> handExcelByPunchCard(MultipartFile file1, MultipartFile file2, MultipartFile file3);

//    public void handExcelByLeaveCard(MultipartFile file);

//    public void handExcelByegistrationCard(MultipartFile file);

}
