package cn.edu.cqupt.cluster.demo.test;

import java.util.List;

import cn.edu.cqupt.cluster.object.ICluster;

public class Page {
	private int totalRecord; // 总共多少条记录
	private int pageSize; // 每页显示记录
	private int totalPage; // 一共多少页记录
	private int currentPage; // 当前页
	private List<?> dataList; // 要显示的数据
	
	public Page(int pageSize, int pageNum, List<?> sourceDataList) {
		super();
		// 总共多少条记录
		this.totalRecord = sourceDataList.size();
		// 每页显示记录
		this.pageSize = pageSize;
		// 一共多少页记录
		this.totalPage = totalRecord / pageSize; 
		this.totalPage = totalRecord % pageSize != 0 ? this.totalPage + 1 : this.totalPage;
		// 当前页
		if(pageNum <= 1){
			this.currentPage = 1;
		}else if(pageNum >= totalPage){
			this.currentPage = totalPage;
		}else{
			this.currentPage = pageNum;
		}
		// 起始索引
		int fromIndex = (this.currentPage - 1) * pageSize;
		// 结束索引
		int toIndex = this.pageSize * this.currentPage;
		toIndex = toIndex > totalRecord ? this.totalRecord: toIndex;
		
		this.dataList = sourceDataList.subList(fromIndex, toIndex);
	}
	
	
	
	public List<?>  getDataList(){
		return this.dataList;
	}
	
	public int getCurrentPage(){
		return this.currentPage;
	}
}
