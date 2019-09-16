package com.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseCode<T> implements  Serializable {
    private Integer status;
    private T data;
    private String msg;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //获取成功对象，包括成功的状态码和数据
    private ResponseCode(T data) {
        this.status = 0;
        this.data = data;
    }

    //获取成功的对象只要返回状态和成功数据
    private ResponseCode(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    //获取成功的对象，返回成功状态码，数据，状态信息
    private ResponseCode(Integer status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    //获取失败对象，返回状态码和失败信息
    private ResponseCode(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    //获取失败对象，返回失败信息
    private ResponseCode(String msg) {
        this.status = 100;
        this.msg = msg;
    }

    //成功的时候只要返回状态和成功信息
    public static <T> ResponseCode seccessRs(Integer status, T data) {
        return new ResponseCode(Const.SUCESS, data);
    }

    //成功返回数据
    public static <T> ResponseCode seccessRs(T data) {
        return new ResponseCode(data);
    }

    //成功全部信息
    public static <T> ResponseCode seccessRs(Integer status, T data, String msg) {
        return new ResponseCode(Const.SUCESS, data, msg);
    }
    //成功返回状态码和信息
    public static <T> ResponseCode seccessRs(Integer status,String msg ) {
        return new ResponseCode(Const.SUCESS, msg);
    }


    //失败返回状态码和失败信息
    public static <T> ResponseCode notseccessRs(Integer status, String msg) {
        return new ResponseCode(Const.ERROR, msg);
    }

    //失败返失败信息
    public static <T> ResponseCode notseccessRs(String msg) {
        return new ResponseCode(msg);
    }

    @JsonIgnore
    public   boolean isSuccess(){
        return  this.status==0;
    }
}
