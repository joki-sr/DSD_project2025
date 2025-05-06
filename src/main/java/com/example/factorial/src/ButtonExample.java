package com.example.factorial.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonExample {

    // 这个方法会在按钮被点击时调用
    private static void myMethod() {
        System.out.println("按钮被点击了！");
        JOptionPane.showMessageDialog(null, "你点击了按钮！", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // 创建主窗口
        JFrame frame = new JFrame("按钮示例");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // 创建按钮
        JButton button = new JButton("点击我");

        // 添加按钮点击事件监听器
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myMethod(); // 调用我们的方法
            }
        });

        // 将按钮添加到窗口
        frame.add(button);

        // 显示窗口
        frame.setVisible(true);
    }
}