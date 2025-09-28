package com.company.scm.util;

public class CLIPrinter {
    public static void printHeader(String title) {
        LoggerUtil.info("--- " + title + " ---");
    }

    public static void printDivider() {
        LoggerUtil.info("----------------------");
    }
}
