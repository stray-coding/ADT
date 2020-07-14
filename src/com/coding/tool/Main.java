package com.coding.tool;

import com.coding.tool.view.MainWindow;

import java.io.File;

/**
 * @author: Coding.He
 * @date: 2020/7/14
 * @emil: 229101253@qq.com
 * @des:
 */
public class Main {
    public static void main(String[] arg) {
        System.out.println(new File("./").getAbsolutePath());
        new MainWindow();
    }
}
