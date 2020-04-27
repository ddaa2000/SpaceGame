package Web;

/**
 * 服务器响应结果类
 * @param <T> 数据泛型
 *
 * @author Mr.YAO
 */
public class Result<T> {

    private Boolean flag;
    private Integer code;
    private String msg;

    private T data;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
