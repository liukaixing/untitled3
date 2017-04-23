package cn.edu.cqupt.cluster.demo.test;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cqupt.cluster.object.ICluster;
import cn.edu.cqupt.cluster.object.ISpectrumReference;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

class DetailClusterPanel {

	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel tmpPanel = new JPanel();

	static int pageNum = 1;// 默认设置当前页为第一页
	int pageSize; // 每页显示的记录数
	String[] columnName; // 列名
	List<ISpectrumReference> sourceDataList; // 一个聚类结果文件中所有的数据

	// public DetailClusterPanel(List<ISpectrumReference> spectrumReferences,
	// String[] columnName) throws Exception {
	// TableModel model = new DetailClusterTableModel(spectrumReferences,
	// columnName);
	// JTable table = new JTable(model);
	// add(new JScrollPane(table), BorderLayout.CENTER);
	// }

	public DetailClusterPanel(int pageSize, String[] columnName, List<ISpectrumReference> sourceDataList)
			throws Exception {
		this.pageSize = pageSize;
		this.columnName = columnName;
		this.sourceDataList = sourceDataList;
		printNextPage();
	}

	public void printNextPage() throws Exception {

		tablePanel.removeAll();
		buttonPanel.removeAll();
		tmpPanel.removeAll();
		Page page = new Page(pageSize, pageNum, sourceDataList);
		List<ISpectrumReference> result = (List<ISpectrumReference>) page.getDataList(); // 获取待显示的数据
		pageNum = page.getCurrentPage(); // 防止页码向上溢出

		// 添加表格

		TableModel model = new DetailClusterTableModel(result, columnName);
		JTable table = new JTable(model);

//		// 为表格单元格添加监听器
//
//		table.addMouseListener(new java.awt.event.MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {// 仅当鼠标单击时响应
//				// 得到选中的行列的索引值
//				int r = table.getSelectedRow();
//				int c = table.getSelectedColumn();
//				// 得到选中的单元格的值，表格中都是字符串
//				Object value = table.getValueAt(r, c);
//				if (c == 0) {
//					new DetailShowInTable().printDetail(r, value);
//				}
//
//			}
//		});
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

		// 增加按钮及响应事件

		// create buttons
		JButton previousPage = new JButton("上一页");
		JButton nextPage = new JButton("下一页");
		// add buttons to panel
		buttonPanel.add(previousPage);
		buttonPanel.add(nextPage);
		// create button action
		TurnPageAction previousPageAction = new TurnPageAction(-1); // -1表上一页
		TurnPageAction nextPageAction = new TurnPageAction(0); // 0 表下一页
		// associate action with button
		previousPage.addActionListener(previousPageAction);
		nextPage.addActionListener(nextPageAction);

		tmpPanel.add(tablePanel, BorderLayout.CENTER);
		tmpPanel.add(buttonPanel, BorderLayout.SOUTH);
		ShowInTable.tabbedPane.setComponentAt(1, tmpPanel);
	}

	// 使用内部类实现事件监听器

	private class TurnPageAction implements ActionListener {
		private int option; // 按钮被点击传过来的值

		public TurnPageAction(int option) {
			this.option = option;
		}

		public void actionPerformed(ActionEvent event) {
			if (option == -1)

			{
				System.out.println("你点击了上一页");
				pageNum = pageNum - 1 > 0 ? pageNum - 1 : 1; // 防止页码向下溢出
			} else if (option == 0) {
				pageNum = pageNum + 1;
				System.out.println("你点击了下一页");
			}

			// 创建分页对象，获得要显示的数据集

			System.out.println("pageNum:" + pageNum);
			try {
				printNextPage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// });

		// }
	}
}

/*
 * 设置表模型
 */
class DetailClusterTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<ISpectrumReference> spectrumReferences;
	// private ArrayList<String> columnName;
	private String[] columnName;
	private int columnCount;
	private int rowCount;

	public DetailClusterTableModel(List<ISpectrumReference> spectrumReferences, String[] columnName) throws Exception {
		this.spectrumReferences = spectrumReferences; // 所有聚类结果
		this.rowCount = spectrumReferences.size(); // 行数
		this.columnCount = columnName.length; // 列数
		this.columnName = columnName; // 列名
		// this.columnName = new ArrayList<String>(); // 列名
		// for (String name : columnName) {
		// this.columnName.add(name);
		// }
	}

	// 设置列名
	public String getColumnName(int c) {
		// return columnName.get(c);
		return columnName[c];
	}

	// 设置列数
	public int getColumnCount() {
		return this.columnCount;
	}

	// 设置行数
	public int getRowCount() {
		return this.rowCount;
	}

	// 为每一个单元格设置值
	public Object getValueAt(int r, int c) {
		Object value = null; // 显示值
		// try {
		switch (c) {
		case 0:
			value = spectrumReferences.get(r).getSpectrumId();
			break;
		case 1:
			value = spectrumReferences.get(r).getPrecursorMz();
			break;
		case 2:
			value = spectrumReferences.get(r).getCharge();
			break;
		case 3:
			value = spectrumReferences.get(r).getSimilarityScore();
			break;
		case 4:
			value = spectrumReferences.get(r).getSpecies();
		}
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return value;
	}
}