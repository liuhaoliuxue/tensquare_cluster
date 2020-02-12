package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 封装后端返回给前端分页结果数据
 *
 */
public class PageResult<T> implements Serializable{

	private Long total;//总记录数
	private List<T> rows;//当前页数据列表
	
	
	public PageResult(Long total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	

	public PageResult() {
		super();
	}


	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	
}
