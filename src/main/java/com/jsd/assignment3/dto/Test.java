package com.jsd.assignment3.dto;

public class Test {

    public static void main(String[] args) {
        String fileName = "image.png";
        String[] seperateFileName = fileName.split("\\.");
        System.out.println("first part : " + seperateFileName[0]);
    }
}
