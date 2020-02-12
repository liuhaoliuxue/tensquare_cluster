package entity;
/**
 * 常量类，存放后端返回给前端状态码常量
 *
 */
public class StatusCode {

	public static final int OK = 20000; // 成功
    public static final int ERROR = 20001;//失败
    public static final int USER_PASS_ERROR = 20002;//用户或密码错误
    public static final int ACCESS_ERROR = 20003;//权限不足
    public static final int REMOTE_ERROR = 20004;//远程调用失败
    public static final int REPEATE_ERROR = 20005;//重复操作
	
}
