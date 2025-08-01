---
title: 填充Excel
description: 填充Excel
---

## **简单填充**

### 概述
基于模板文件，通过对象或 Map 填充数据到 Excel 中。

### 示例填充模板
![img](../../images/fill/simpleFill_file.png)
### 示例填充结果
![img](../../images/fill/simpleFill_result.png)
#### 示例对象
```java
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
}
```

#### 示例代码
```java
@Test
public void simpleFill() {
    String templateFileName = "path/to/simple.xlsx";

    // 方案1：基于对象填充
    FillData fillData = new FillData();
    fillData.setName("张三");
    fillData.setNumber(5.2);
    FastExcel.write("simpleFill.xlsx")
        .withTemplate(templateFileName)
        .sheet()
        .doFill(fillData);

    // 方案2：基于 Map 填充
    Map<String, Object> map = new HashMap<>();
    map.put("name", "张三");
    map.put("number", 5.2);
    FastExcel.write("simpleFillMap.xlsx")
        .withTemplate(templateFileName)
        .sheet()
        .doFill(map);
}
```

---

## **填充列表**

### 概述
填充多个数据项到模板列表中，支持内存批量操作和文件缓存分批填充。

### 示例填充模板
![img](../../images/fill/listFill_file.png)
### 示例填充结果
![img](../../images/fill/listFill_result.png)

#### 示例代码
```java
@Test
public void listFill() {
    String templateFileName = "path/to/list.xlsx";

    // 方案1：一次性填充所有数据
    FastExcel.write("listFill.xlsx")
        .withTemplate(templateFileName)
        .sheet()
        .doFill(data());

    // 方案2：分批填充
    try (ExcelWriter writer = FastExcel.write("listFillBatch.xlsx").withTemplate(templateFileName).build()) {
        WriteSheet writeSheet = FastExcel.writerSheet().build();
        writer.fill(data(), writeSheet);
        writer.fill(data(), writeSheet);
    }
}
```

---

## **复杂的填充**

### 概述
在模板中填充多种数据类型，包括列表和普通变量。

### 示例填充模板
![img](../../images/fill/complexFill_file.png)
### 示例填充结果
![img](../../images/fill/complexFill_result.png)

#### 示例代码
```java
@Test
public void complexFill() {
    String templateFileName = "path/to/complex.xlsx";

    try (ExcelWriter writer = FastExcel.write("complexFill.xlsx").withTemplate(templateFileName).build()) {
        WriteSheet writeSheet = FastExcel.writerSheet().build();

        // 填充列表数据，开启 forceNewRow
        FillConfig config = FillConfig.builder().forceNewRow(true).build();
        writer.fill(data(), config, writeSheet);

        // 填充普通变量
        Map<String, Object> map = new HashMap<>();
        map.put("date", "2024年11月20日");
        map.put("total", 1000);
        writer.fill(map, writeSheet);
    }
}
```

---

## **数据量大的复杂填充**

### 概述
优化大数据量填充性能，确保模板列表在最后一行，后续数据通过 `WriteTable` 填充。

### 示例填充模板
![img](../../images/fill/complexFillWithTable_file.png)
### 示例填充结果
![img](../../images/fill/complexFillWithTable_result.png)

#### 示例代码
```java
@Test
public void complexFillWithTable() {
    String templateFileName = "path/to/complexFillWithTable.xlsx";

    try (ExcelWriter writer = FastExcel.write("complexFillWithTable.xlsx").withTemplate(templateFileName).build()) {
        WriteSheet writeSheet = FastExcel.writerSheet().build();

        // 填充列表数据
        writer.fill(data(), writeSheet);

        // 填充其他变量
        Map<String, Object> map = new HashMap<>();
        map.put("date", "2024年11月20日");
        writer.fill(map, writeSheet);

        // 填充统计信息
        List<List<String>> totalList = new ArrayList<>();
        totalList.add(Arrays.asList(null, null, null, "统计: 1000"));
        writer.write(totalList, writeSheet);
    }
}
```

---

## **横向填充**

### 概述
将列表数据横向填充，适用于动态列数场景。

### 示例填充模板
![img](../../images/fill/horizontalFill_file.png)
### 示例填充结果
![img](../../images/fill/horizontalFill_result.png)

#### 示例代码
```java
@Test
public void horizontalFill() {
    String templateFileName = "path/to/horizontal.xlsx";

    try (ExcelWriter writer = FastExcel.write("horizontalFill.xlsx").withTemplate(templateFileName).build()) {
        WriteSheet writeSheet = FastExcel.writerSheet().build();

        FillConfig config = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
        writer.fill(data(), config, writeSheet);

        Map<String, Object> map = new HashMap<>();
        map.put("date", "2024年11月20日");
        writer.fill(map, writeSheet);
    }
}
```

---

## **多列表组合填充**

### 概述
支持多个列表同时填充，列表之间通过前缀区分。

### 示例填充模板
![img](../../images/fill/compositeFill_file.png)
### 示例填充结果
![img](../../images/fill/compositeFill_result.png)

#### 示例代码
```java
@Test
public void compositeFill() {
    String templateFileName = "path/to/composite.xlsx";

    try (ExcelWriter writer = FastExcel.write("compositeFill.xlsx").withTemplate(templateFileName).build()) {
        WriteSheet writeSheet = FastExcel.writerSheet().build();

        // 使用 FillWrapper 进行多列表填充
        writer.fill(new FillWrapper("data1", data()), writeSheet);
        writer.fill(new FillWrapper("data2", data()), writeSheet);
        writer.fill(new FillWrapper("data3", data()), writeSheet);

        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date());
        writer.fill(map, writeSheet);
    }
}
```

---

## **填充使用场景总结**

| 功能               | 描述                                                                                      | 示例代码              |
|--------------------|-------------------------------------------------------------------------------------------|-----------------------|
| 最简单的填充        | 通过对象或 Map 填充简单数据                                                              | [simpleFill](#1)      |
| 填充列表           | 将列表数据填充到模板，支持分批填充                                                        | [listFill](#2)        |
| 复杂填充            | 在同一模板中填充多种数据类型，包括列表与普通变量                                           | [complexFill](#3)     |
| 大数据量复杂填充    | 优化大数据量填充性能，模板列表需放在最后一行                                               | [complexFillWithTable](#4) |
| 横向填充            | 将列表数据横向排列填充，适合动态列数据                                                    | [horizontalFill](#5)  |
| 多列表组合填充      | 支持多个列表同时填充，列表间通过前缀区分                                                   | [compositeFill](#6)   |
