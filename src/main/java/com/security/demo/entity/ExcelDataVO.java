package com.security.demo.entity;

import com.google.common.collect.ImmutableMap;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class ExcelDataVO {
    public static final Map<String,String> DTO = ImmutableMap.<String,String>builder()
            .put("姓名","name")
            .put("年龄","age")
            .put("居住地","location")
            .put("职业","job")
            .build();

    /**
     * 姓名
     */
    @NotNull(message = "姓名_不能为空")
    private String name;

    /**
     * 年龄
     */
    @NotNull(message = "年龄_不能为空")
    private Integer age;

    /**
     * 居住地
     */
    @NotNull(message = "居住地_不能为空")
    private String location;

    /**
     * 职业
     */
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}