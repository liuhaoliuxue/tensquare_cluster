package entity;

import java.io.Serializable;

/**
 * 封装后端返回给前端数据
 *
 */
public class Result implements Serializable{

	private Boolean flag;//是否执行成功
	private Integer code;//状态码
	private String message;//返回提示信息
	private Object data;// 查询结果
	

	public Result(Boolean flag, Integer code, String message) {
		super();
		this.flag = flag;
		this.code = code;
		this.message = message;
	}
	
	
	public Result(Boolean flag, Integer code, String message, Object data) {
		super();
		this.flag = flag;
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	
	public Result() {
		super();
	}
	
	
	
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
}
