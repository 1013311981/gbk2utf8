package top.rurenyinshui.gbk2utf8.DTO;

public enum ErrorCode {
    SUCCESS("0000","请求成功"),
    PARAMETER_IS_EMPTY("0001","参数为空"),
    NETWORK_ERROR("9999","网络错误，待会重试"),
    UPLOAD_FAIL("0002","上传失败"),
    DOWNLOAD_FAIL("0003","下载失败");

    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
