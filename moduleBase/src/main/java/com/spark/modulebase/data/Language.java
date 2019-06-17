package com.spark.modulebase.data;

import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.R;

import java.util.Locale;


public enum Language {
    zh(1, java.util.Locale.CHINESE),
    en(2, java.util.Locale.ENGLISH),
    ja(3, java.util.Locale.JAPAN);

    private int status;
    private Locale Locale;

    Language(int code, Locale locale) {
        this.status = status;
        this.Locale = Locale;
    }

    public java.util.Locale getLocale() {
        return Locale;
    }
}