package cn.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @auther: LvSheng
 * @date: 2025/5/9
 * @description:
 */
@Data
@AllArgsConstructor
@ToString
public class Person implements Serializable {
    private String name;
    private int age;
}
