package cn.edu.cqupt.cluster.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartPanel;

import cn.edu.cqupt.cluster.object.Clustering;
import cn.edu.cqupt.cluster.object.ICluster;

/* 这个包用于添加表格和按键
 * 这个包如果修改，首先需要修改的是swtch-case处（ClusterTableModel类），此处支持三列数据的显示
 */

/*
 * 构建显示结果的表的框架
 */
class ClusterPanel {

	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	static public JPanel tmpPanel = new JPanel();
	static public ChartPanel panel=null;

	static int pageNum = 1;// 默认设置当前页为第一页
	int pageSize; // 每页显示的记录数
	String[] columnName; // 列名
	List<Clustering> sourceDataList; // 一个聚类结果文件中所有的数据



	public JPanel getTablePanel() {
		return this.tablePanel;
	}

	public JPanel getButtonPanel() {
		return this.buttonPanel;
	}

	/**
	 * @param pageSize
	 *            每页显示的记录数
	 * @param columnName
	 *            列名
	 * @param sourceDataList
	 *            原数据
	 * @throws Exception
	 */
	public ClusterPanel(int pageSize, String[] columnName, List<Clustering> sourceDataList) throws Exception {
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
		List<Clustering> result = (List<Clustering>) page.getDataList(); // 获取待显示的数据
		pageNum = page.getCurrentPage(); // 防止页码向上溢出

		// 添加表格

		TableModel model = new ClusterTableModel(result, columnName);
		final JTable table = new JTable(model);

		// 为表格单元格添加监听器

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {// 仅当鼠标单击时响应
				if(panel!=null)
					{
					ShowInTable.panel3.remove(panel);
					}

				// 得到选中的行列的索引值
				int r = table.getSelectedRow();
				int c = table.getSelectedColumn();
				// 得到选中的单元格的值，表格中都是字符串
				Object value = table.getValueAt(r, c);
				if (c == 0) {
					new DetailShowInTable().printDetail(value);
				}
//				ShowInTable.tabbedPane.setSelectedIndex(1);
				ShowInTable.panel.updateUI();
//				String date[]={"data1","data2","data3","data4","data5"};
				panel=new ChartPanel(cn.edu.cqupt.cluster.object.piechart.pie(value, 1));
				ShowInTable.panel3.add(panel,BorderLayout.SOUTH);

				ShowInTable.panel.updateUI();

			}
		});
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
		GridLayout layout=new GridLayout(2,1);
		tmpPanel.setLayout(layout);
		tmpPanel.add(tablePanel);
		tmpPanel.add(buttonPanel);
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

class ClusterPanel2 {

	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	static public JPanel tmpPanel = new JPanel();
	static public ChartPanel panel=null;
	static int pageNum = 1;// 默认设置当前页为第一页
	int pageSize; // 每页显示的记录数
	String[] columnName; // 列名
	List<Clustering> sourceDataList; // 一个聚类结果文件中所有的数据

	public JPanel getTablePanel() {
		return this.tablePanel;
	}

	public JPanel getButtonPanel() {
		return this.buttonPanel;
	}

	/**
	 * @param pageSize
	 *            每页显示的记录数
	 * @param columnName
	 *            列名
	 * @param sourceDataList
	 *            原数据
	 * @throws Exception
	 */
	public ClusterPanel2(int pageSize, String[] columnName, List<Clustering> sourceDataList) throws Exception {
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
		List<Clustering> result = (List<Clustering>) page.getDataList(); // 获取待显示的数据
		pageNum = page.getCurrentPage(); // 防止页码向上溢出

		// 添加表格

		TableModel model = new ClusterTableModel(result, columnName);
		final JTable table = new JTable(model);

		// 为表格单元格添加监听器

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {// 仅当鼠标单击时响应
				// 得到选中的行列的索引值
				if(panel!=null)
				{
					ShowInTable.panel4.remove(panel);
				}
				int r = table.getSelectedRow();
				int c = table.getSelectedColumn();
				// 得到选中的单元格的值，表格中都是字符串
				Object value = table.getValueAt(r, c);
				if (c == 0) {
					new DetailShowInTable().printDetail2(value);
				}
				//ShowInTable.tabbedPane.setSelectedIndex(1);
				ShowInTable.panel.updateUI();
//				String date[]={"data1","data2","data3","data4","data5"};//输入要传入显示的数据，可以自己改类型，把方法参数改下就ok。
				//可以设置一个List或者集合类型，通过数据传入来给饼图设置参数。
				panel=new ChartPanel(cn.edu.cqupt.cluster.object.piechart.pie(value, 2));
				ShowInTable.panel4.add(panel,BorderLayout.SOUTH);
				//还要设置条件如果点击事件获取的数据相同就不用修改饼图，如果不相同就把原来的饼图覆盖掉重新传参。
			}
		});
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
		GridLayout layout=new GridLayout(2,1);
		tmpPanel.setLayout(layout);
		tmpPanel.add(tablePanel);
		tmpPanel.add(buttonPanel);
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
 * 设置表的模型
 */
class ClusterTableModel extends AbstractTableModel {
	private int rowCount;
	private int columnCount;
	private ArrayList<String> columnName;
	private List<Clustering> result;

	public ClusterTableModel() {
	}

	public ClusterTableModel(List<Clustering> result, String[] columnName) throws Exception {
		this.result = result; // 所有聚类结果
		this.rowCount = result.size(); // 行数
		this.columnCount = columnName.length; // 列数
		this.columnName = new ArrayList<String>(); // 列名
		for (String name : columnName) {
			this.columnName.add(name);
		}
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	/*
	 * 为每一个单元格设置值，如果重新在另外情况使用该model则需要修改switch-case，这里只适用于三列的情况
	 */
	public Object getValueAt(int r, int c) {
		Object value = null;
		try {
			switch (c) {
			case 0:
				value = result.get(r).getId();
				// JButton id = new JButton(value.toString());
				// JPanel buttonPanel = new JPanel();
				// value = buttonPanel.add(id);
				break;

			case 1:
				value = result.get(r).getAvPrecursorMz();
				break;
			case 2:
				value = result.get(r).getAvPrecursorIntens();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/*
	 * 设置列名
	 */
	public String getColumnName(int c) {
		return columnName.get(c);
	}
}
