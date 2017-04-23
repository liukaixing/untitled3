package cn.edu.cqupt.cluster.demo.tableCellRender;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * This renderer renders a color value as a panel with the given color.
 */

/**
 * 定义一个表格的单元格的绘制器，需要实现TableCellRenderer，
 * 覆写getTableCellRendererComponent方法，该方法在绘制一个单元格是调用
 */
public class ColorTableCellRenderer extends JPanel implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setBackground((Color) value);
		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {

					JFrame frame = new TableCellRenderFrame();
					frame.setTitle("hahaha");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
			});

		} else
			setBorder(null);
		return this;
	}
}