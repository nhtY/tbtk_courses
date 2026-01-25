package com.nht.aop_demo.dto;

public record ResponseData(int status, String message, Object data) { }