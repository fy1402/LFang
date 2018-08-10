package com.example.demo;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;

/**
 * Created by i-feng on 2018/7/3.
 */

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public Map<String, List<Card>> handExcelByPunchCard(MultipartFile file1, MultipartFile file2, MultipartFile file3) {

        //打卡
        List<Card> cardList1 = this.handPunchCard(file1);
        if (cardList1 == null) {
            return null;
        }

        //签卡
        List<Card> cardList2 = this.handRegistrationCard(file2, cardList1);

        for (Card card : cardList2) {
            log.info(card.toString());
        }

        // 分组
        Map<String, List<Card>> map = this.handCardList(cardList2);


        for (String key : map.keySet()) {
            List<Card> cardList = map.get(key);
            for (Card card : cardList) {
                log.info(card.toString());
            }
        }

        // 请假
        List<Card> cardList3 = this.handLeaveCard(file3);

        // 处理请假
        Map<String, List<Card>> map1 = this.handMapCardList(map, cardList3);

        // 排序
        Map<String, List<Card>> map2 = this.handArray(map1);

        return map1;
    }

    private Map<String, List<Card>> handArray(Map<String, List<Card>> map) {

        for (String key : map.keySet()) {
            List<Card> cardList = map.get(key);

            Collections.sort(cardList, new Comparator<Card>() {
                @Override
                public int compare(Card o1, Card o2) {

                    int i = 0;
                    if (o1.getStartDate().getTime() > o2.getStartDate().getTime()) {
                        i = 1;
                    } else {
                        i = -1;
                    }
                    return i;
                }
            });
        }
        return map;
    }


    private HSSFWorkbook createExcel(Map<String, List<Card>> map) {
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
//            logger.info(startTime);
                cell.setCellValue(startTime);
                cell.setCellStyle(cellStyle);

                cell = hssfRow.createCell(4);
                String endTime = card.getEndDateStr();
//            logger.info(endTime);
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

        return workbook;
    }

    //打卡
    private List<Card> handPunchCard(MultipartFile file) {

        try {

            //创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));

            //获取一共有多少sheet，然后遍历
            int numberOfSheets = workbook.getNumberOfSheets();

            if (numberOfSheets < 1) {
                return null;
            }

            HSSFSheet sheet = workbook.getSheetAt(0);

            //  获取sheet中一共有多少行，遍历行（注意第一行是标题）
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

            List<Card> cardList = new ArrayList<Card>();

            for (int j = 0; j < physicalNumberOfRows; j++) {
                if (j == 0 || j == 1) {
                    continue;//标题行
                }
                HSSFRow row = sheet.getRow(j);

                //.获取每一行有多少单元格，遍历单元格
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();

                Card card = new Card();
                for (int k = 0; k < physicalNumberOfCells; k++) {
                    HSSFCell cell = row.getCell(k);
                    if (k == 0) { // 部门
                        card.setDepartment(cell.getStringCellValue());
                    } else if (k == 1) { // id
                        card.setId(Long.parseLong(cell.getStringCellValue()));
                    } else if (k == 2) { // 姓名
                        card.setName(cell.getStringCellValue());
                    } else if (k == 3) { // 日期
                        String dateStr = cell.getStringCellValue();
                        card.setDateStr(dateStr);
                    } else if (k == 4) { // 打卡次数
                        card.setCount(cell.getStringCellValue());
                    } else if (k == 5) { // 打卡时间
                        String[] dates = cell.getStringCellValue().split(",");
                        if (dates.length > 0) {
                            card.setStartDateStr(card.getDateStr() + " " + dates[0]);
                            card.setStartDate(DateUtil.smartFormat(card.getStartDateStr()));
                            card.setEndDateStr(card.getDateStr() + " " + dates[dates.length - 1]);
                            card.setEndDate(DateUtil.smartFormat(card.getEndDateStr()));
                        }
                    }
                }

                cardList.add(card);

//                log.info(card.getName());
            }
            return cardList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 签卡
    private List<Card> handRegistrationCard(MultipartFile file, List<Card> list) {
        try {

            //创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));

            //获取一共有多少sheet，然后遍历
            int numberOfSheets = workbook.getNumberOfSheets();

            if (numberOfSheets < 1) {
                return null;
            }

            HSSFSheet sheet = workbook.getSheetAt(0);

            //  获取sheet中一共有多少行，遍历行（注意第一行是标题）
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

            for (int j = 0; j < physicalNumberOfRows; j++) {
                if (j == 0 || j == 1) {
                    continue;//标题行
                }
                HSSFRow row = sheet.getRow(j);

                //.获取每一行有多少单元格，遍历单元格
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();

                Card card = new Card();
                card.setLeaveType("签卡");
                for (int k = 0; k < physicalNumberOfCells; k++) {
                    HSSFCell cell = row.getCell(k);
                    if (k == 0) {
                        card.setName(cell.getStringCellValue());
                    } else if (k == 1) {
                        card.setDepartment(cell.getStringCellValue());
                    } else if (k == 2) {
                        card.setDateStr(cell.getStringCellValue());
                    } else if (k == 3) {
                        String str = cell.getStringCellValue();
                        if (cell.getStringCellValue().length() == 4){
                            str = "0" + cell.getStringCellValue();
                        }
                        card.setStartDateStr(card.getDateStr() + " " + str);
                        card.setStartDate(DateUtil.smartFormat(card.getStartDateStr()));
                    } else if (k == 4) {
                        card.setDescription(cell.getStringCellValue());
                    }
                }

                for (Card item : list) {
                    String str = item.getDateStr();
                    if (item.getName().equals(card.getName()) && item.getDateStr().equals(card.getDateStr())) {

                        log.info(item.toString());

                        Long startInt = item.getStartDate().getTime();
                        Long endInt = item.getEndDate().getTime();
                        Long dateInt = card.getStartDate().getTime();
                        item.setDescription(card.getDescription());
                        if (startInt >= dateInt) {
                            item.setStartDate(card.getStartDate());
                            item.setStartDateStr(card.getStartDateStr());
                        } else if (endInt <= dateInt){
                            item.setEndDate(card.getStartDate());
                            item.setEndDateStr(card.getStartDateStr());
                        }
                        break;
                    }
                }
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 请假
    private List<Card> handLeaveCard(MultipartFile file) {
        try {

            //创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));

            //获取一共有多少sheet，然后遍历
            int numberOfSheets = workbook.getNumberOfSheets();

            if (numberOfSheets < 1) {
                return null;
            }

            HSSFSheet sheet = workbook.getSheetAt(0);

            //  获取sheet中一共有多少行，遍历行（注意第一行是标题）
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

            List<Card> cardList = new ArrayList<Card>();
            for (int j = 0; j < physicalNumberOfRows; j++) {
                if (j == 0 || j == 1) {
                    continue;//标题行
                }
                HSSFRow row = sheet.getRow(j);

                //.获取每一行有多少单元格，遍历单元格
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();

                Card card = new Card();
                card.setLeaveType("请假");
                for (int k = 0; k < physicalNumberOfCells; k++) {
                    HSSFCell cell = row.getCell(k);

                    if (k == 0) { // 名称
                        card.setName(cell.getStringCellValue());
                    } else if (k == 2) { // 部门
                        card.setDepartment(cell.getStringCellValue());
                    } else if (k == 3) { // 请假类别
                        card.setLeaveType(cell.getStringCellValue());
                    } else if (k == 4) { // 请假事由
                        card.setDescription(cell.getStringCellValue());
                    } else if (k == 5) { // 请假开始日期
                        card.setTimeOutDateStart(cell.getStringCellValue());
                    } else if (k == 6) { // 请假开始时间
                        card.setTimeOutStart(cell.getStringCellValue());
                    } else if (k == 7) { // 请假结束日期
                        card.setTimeOutDateEnd(cell.getStringCellValue());
                    } else if (k == 8) { // 请假结束时间
                        if (cell.getCellTypeEnum() == NUMERIC) {
                            log.info(cell.getNumericCellValue() + "");
                            card.setTimeOutEnd(String.valueOf(cell.getNumericCellValue()));
                        } else {
                        log.info(cell.getStringCellValue());
                            card.setTimeOutEnd(String.valueOf(cell.getStringCellValue()));
                        }
//                        card.setTimeOutEnd(String.valueOf(cell.getStringCellValue()));
                    } else if (k == 9) { // 累计小时数
                        BigDecimal val = new BigDecimal(cell.getNumericCellValue());
                        card.setTimeLength(val.floatValue());
//                    } else if (k == 10) { // 申请日期
//                        card.set(cell.getStringCellValue());
                    } else if (k == 11) { // 明细
                        card.setTimeOutStatus(cell.getStringCellValue());
                    }
                }

                card.setStartDateStr(card.getTimeOutDateStart() + " " + card.getTimeOutStart());
                card.setStartDate(DateUtil.smartFormat(card.getStartDateStr()));
                card.setEndDateStr(card.getTimeOutDateEnd() + " " + card.getTimeOutEnd());
                log.info(card.getEndDateStr());
                card.setEndDate(DateUtil.smartFormat(card.getEndDateStr()));

                cardList.add(card);
            }
            return cardList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    // 处理分组(name)
    private Map<String, List<Card>> handCardList(List<Card> list) {

        Map<String, List<Card>> map = new HashMap<>();
        String[] keys;

        for (Card card : list) {
            map.put(card.getName(), new ArrayList<Card>());
        }

        for (Card card : list) {
            for (String key : map.keySet()) {
                if (card.getName().equals(key)) {
                    List<Card> list1 = map.get(key);
                    list1.add(card);
                }
            }
        }

        return map;
    }

    // 将请假记录插入
    private Map<String, List<Card>> handMapCardList(Map<String, List<Card>> map, List<Card>list) {

        Map<String, List<Card>> map1 = new HashMap<>();

        map1.putAll(map);

        /*for (String key:map.keySet()) {
            List<Card> cardList = map.get(key);
            map.put(key, cardList);
        }*/
//        HashMap<String, List<Card>> hashMap = map;
//        map1 = CloneUtils.clone(map);
//        map1 = ObjectUtil.cloneByStream(map);

        for (Card card : list) {
            for (String key : map.keySet()) {
                if (card.getName().equals(key)) {
                    List<Card> cardList = map.get(key);

                    Integer count = cardList.size();
                    Integer i = 0;

                    for (Card card1 : cardList) {

                        Card newCard = new Card();
                        i++;

                        // 请假在打卡的同一天内
                        if (card1.getDateStr().equals(card.getTimeOutDateStart()) && card1.getDateStr().equals(card.getTimeOutDateEnd())) {
                            if (card.getStartDate().getTime() < card1.getStartDate().getTime()) {
                                card1.setStartDate(card.getStartDate());
                                card1.setStartDateStr(card.getStartDateStr());
                                card1.setTimeOutStatus(card.getTimeOutStatus());
                                if (card1.getDescription() == null && card.getDescription() != null) {
                                    card1.setDescription(card.getDescription());
                                } else if (card1.getDescription() != null && card.getDescription() == null) {
                                    card1.setDescription(card1.getDescription());
                                } else if (card1.getDescription() != null && card.getDescription() != null){
                                    card1.setDescription(card1.getDescription() + card.getDescription());
                                } else {
                                    card1.setDescription("");
                                }
                                card1.setTimeLength(card.getTimeLength());
                                if (StrUtil.isEmpty(card1.getLeaveType())) {
                                    card1.setLeaveType(card.getLeaveType());
                                } else {
                                    card1.setLeaveType(card1.getLeaveType() + "&" + card.getLeaveType());
                                }
                                card1.setTimeOutStatus(card.getTimeOutStatus());
                                card1.setTimeOutStart(card.getStartDateStr());
//                                tempArrayM.add(card1);
                                newCard = card1;
                            } else if (card.getEndDate().getTime() > card1.getEndDate().getTime()) {
                                card1.setEndDate(card.getEndDate());
                                card1.setEndDateStr(card.getEndDateStr());
                                card1.setTimeOutStatus(card.getTimeOutStatus());
                                if (card1.getDescription() == null && card.getDescription() != null) {
                                    card1.setDescription(card.getDescription());
                                } else if (card1.getDescription() != null && card.getDescription() == null) {
                                    card1.setDescription(card1.getDescription());
                                } else if (card1.getDescription() != null && card.getDescription() != null){
                                    card1.setDescription(card1.getDescription() + card.getDescription());
                                } else {
                                    card1.setDescription("");
                                }
//                                card1.setTimeLength(card.getTimeLength());
                                if (StrUtil.isEmpty(card1.getLeaveType())) {
                                    card1.setLeaveType(card.getLeaveType());
                                } else {
                                    card1.setLeaveType(card1.getLeaveType() + "&" + card.getLeaveType());
                                }
                                card1.setTimeLength(card.getTimeLength());
                                card1.setLeaveType(card.getLeaveType());
                                card1.setTimeOutStatus(card.getTimeOutStatus());
                                card1.setTimeOutStart(card.getStartDateStr());
                                newCard = card1;
                            } else {
                                log.info(card.toString());
                                newCard = card1;
                            }

                            List<com.example.demo.Card> cardList1 = map1.get(key);
                            log.info(cardList1.get(0).getEndDateStr());
                            for (Card card2 : cardList1) {
                                if (card2.getDateStr().equals(newCard.getDateStr())) {
                                    card2 = newCard;
                                }
                            }
                            break;
                            // 请假开始时间在同一天内,结束日期不在
                        } else if (card1.getDateStr().equals(card.getTimeOutDateStart()) && !card1.getDateStr().equals(card.getTimeOutDateEnd())) {
                            card1.setEndDateStr(card.getEndDateStr());
                            card1.setEndDate(card.getEndDate());
                            card1.setTimeOutStatus(card.getTimeOutStatus());
//                            card1.setDescription(card1.getDescription() + card.getDescription());
                            if (card1.getDescription() == null && card.getDescription() != null) {
                                card1.setDescription(card.getDescription());
                            } else if (card1.getDescription() != null && card.getDescription() == null) {
                                card1.setDescription(card1.getDescription());
                            } else if (card1.getDescription() != null && card.getDescription() != null){
                                card1.setDescription(card1.getDescription() + card.getDescription());
                            } else {
                                card1.setDescription("");
                            }
                            card1.setTimeLength(card.getTimeLength());
//                            card1.setLeaveType(card.getLeaveType());
                            if (StrUtil.isEmpty(card1.getLeaveType())) {
                                card1.setLeaveType(card.getLeaveType());
                            } else {
                                card1.setLeaveType(card1.getLeaveType() + "&" + card.getLeaveType());
                            }
                            card1.setTimeOutStatus(card.getTimeOutStatus());
                            card1.setTimeOutStart(card.getStartDateStr());
                            newCard = card1;
                            List<Card> cardList1 = map1.get(key);
                            for (Card card2 : cardList1) {
                                if (card2.getDateStr().equals(newCard.getDateStr())) {
                                    card2 = newCard;
                                }
                            }
                            break;
                            // 请假结束时间在同一天内，开始日期不在
                        } else if (card1.getDateStr().equals(card.getTimeOutDateEnd())) {
                            card1.setStartDate(card.getStartDate());
                            card1.setStartDateStr(card.getStartDateStr());
                            card1.setTimeOutStatus(card.getTimeOutStatus());
//                            card1.setDescription(card1.getDescription() + card.getDescription());
                            if (card1.getDescription() == null && card.getDescription() != null) {
                                card1.setDescription(card.getDescription());
                            } else if (card1.getDescription() != null && card.getDescription() == null) {
                                card1.setDescription(card1.getDescription());
                            } else if (card1.getDescription() != null && card.getDescription() != null){
                                card1.setDescription(card1.getDescription() + card.getDescription());
                            } else {
                                card1.setDescription("");
                            }
                            card1.setTimeLength(card.getTimeLength());
//                            card1.setLeaveType(card.getLeaveType());
                            if (StrUtil.isEmpty(card1.getLeaveType())) {
                                card1.setLeaveType(card.getLeaveType());
                            } else {
                                card1.setLeaveType(card1.getLeaveType() + "&" + card.getLeaveType());
                            }
                            card1.setTimeOutStatus(card.getTimeOutStatus());
                            card1.setTimeOutStart(card.getStartDateStr());
                            newCard = card1;
                            List<Card> cardList1 = map1.get(key);
                            for (Card card2 : cardList1) {
                                if (card2.getDateStr().equals(newCard.getDateStr())) {
                                    card2 = newCard;
                                }
                            }
                            break;
                            // 请假不在同一天内
                        } else if (i == count && card.getTimeLength() > 7.4) { // 打卡
                            newCard.setCount("");
                            newCard.setStartDate(card.getStartDate());
                            newCard.setStartDateStr(card.getStartDateStr());
                            newCard.setEndDateStr(card.getEndDateStr());
                            newCard.setEndDate(card.getEndDate());
                            if (card.getDescription() != null){
                                newCard.setDescription(card.getDescription());
                            } else {
                                newCard.setDescription("");
                            }
                            newCard.setName(card.getName());
                            newCard.setTimeLength(card.getTimeLength());
//                            newCard.setLeaveType(card.getLeaveType());
                            if (StrUtil.isEmpty(newCard.getLeaveType())) {
                                newCard.setLeaveType(card.getLeaveType());
                            } else {
                                newCard.setLeaveType(newCard.getLeaveType() + "&" + card.getLeaveType());
                            }
                            newCard.setTimeOutStatus(card.getTimeOutStatus());
                            newCard.setDepartment(card1.getDepartment());
                            newCard.setId(card1.getId());
                            newCard.setDateStr(card.getTimeOutDateStart());
                            newCard.setTimeOutStart(card.getStartDateStr());
                            List<Card> cardList1 = map1.get(key);
                            cardList1.add(newCard);
                            break;
                        } else {
                            log.info(card.toString());
                        }

                    }

                    break;
                }
            }
        }


        return map1;
    }











}
