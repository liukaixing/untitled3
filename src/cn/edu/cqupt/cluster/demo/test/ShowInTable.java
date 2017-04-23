package cn.edu.cqupt.cluster.demo.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cn.edu.cqupt.cluster.object.ICluster;

public class ShowInTable {

	public static JFrame frame = new JFrame();
	public static JTabbedPane tabbedPane = new JTabbedPane();
	

	public static void main(String[] args) {
		tabbedPane.addTab("Table I", null);
		tabbedPane.addTab("Table II", null);

		ShowOnceClusterResult clusterResult = null;
		try {
			clusterResult = new ShowOnceClusterResult("./compare/cli_clustering.pxd000021.0.7_4.clustering");
			int pageSize = 20;
			String[] columnName = new String[] { "ID", "av_precursor_mz", "av_precursor_intens" };
			List<ICluster> sourceDataList = clusterResult.readCluster();
			ClusterPanel clusterTablePanel = new ClusterPanel(pageSize, columnName, sourceDataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ShowInTable.frame.pack();
		ShowInTable.frame.add(ShowInTable.tabbedPane);
		ShowInTable.frame.setTitle("Cluster Result");
		ShowInTable.frame.setSize(800, 600);
		ShowInTable.frame.setLocationRelativeTo(null); // 让窗体居中显示
		ShowInTable.frame.setVisible(true);
	}
}