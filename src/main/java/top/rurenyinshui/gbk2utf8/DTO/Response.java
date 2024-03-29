package top.rurenyinshui.gbk2utf8.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 请求返回类
 * Created by cg on 2019/3/26
 */
@Getter
@Setter
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -4505655308965878999L;

    //请求成功返回码为：0000
    private static final String successCode = "0000";
    //返回数据
    private T data;
    //返回码
    private String code;
    //返回描述
    private String msg;

    public Response(){
        this.code = successCode;
        this.msg = "请求成功";
    }

    public Response(String code,String msg){
        this();
        this.code = code;
        this.msg = msg;
    }
    public Response(String code,String msg,T data){
        this();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Response(T data){
        this();
        this.data = data;
    }
    public static Response SUCCESS() {
        return new Response();
    }

    public static <T> Response SUCCESS(T data) {
        return new Response(data);
    }

    public static Response ERROR(ErrorCode code) {
        return new Response(code.getCode(), code.getMsg());
    }

    public static <T> Response ERROR(ErrorCode code, T data) {
        return new Response(code.getCode(), code.getMsg(), data);
    }
}
