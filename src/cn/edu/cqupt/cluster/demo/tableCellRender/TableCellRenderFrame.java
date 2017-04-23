package cn.edu.cqupt.cluster.demo.tableCellRender;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * This frame contains a table of planet data.
 */
public class TableCellRenderFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 400;

   public TableCellRenderFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      TableModel model = new PlanetTableModel(); // 实例化TableModel
      JTable table = new JTable(model); // 使用自定义的表格模型构建表格
      table.setRowSelectionAllowed(false); // 设置行不可编辑

      // set up renderers and editors

      /* setDefaultRenderer
       * Function：设定表格的绘制器
       * Para1:Class类实例
       * Para2：绘制器
       */
//      table.setDefaultRenderer(Color.class, new ColorTableCellRenderer());
//      table.setDefaultEditor(Color.class, new ColorTableCellEditor());

      JComboBox<Integer> moonCombo = new JComboBox<>();
      for (int i = 0; i <= 20; i++)
         moonCombo.addItem(i);

      TableColumnModel columnModel = table.getColumnModel(); // 获取列模型
      TableColumn moonColumn = columnModel.getColumn(PlanetTableModel.MOONS_COLUMN); // 获取列信息
      moonColumn.setCellEditor(new DefaultCellEditor(moonCombo)); // 设置单元格的
//      moonColumn.setHeaderRenderer(table.getDefaultRenderer(ImageIcon.class));
//      moonColumn.setHeaderValue(new ImageIcon(getClass().getResource("Moons.gif")));

      // show table

      table.setRowHeight(100);
      add(new JScrollPane(table), BorderLayout.CENTER);
   }
}

