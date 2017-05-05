package cn.edu.cqupt.cluster.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import cn.edu.cqupt.cluster.dao.ReadFromSQLiteService;
import cn.edu.cqupt.cluster.object.Clustering;
import cn.edu.cqupt.cluster.object.ICluster;

public class ShowInTable {
	static ReadFromSQLiteService readFromSQLiteService = ReadFromSQLiteService.getInstance();

	public static JFrame frame = new JFrame();
	public static JTabbedPane tabbedPane = new JTabbedPane();
	public static JPanel panel=new JPanel();
	public static JPanel panel2=new JPanel();
	public static JPanel panel3=new JPanel();
	public static JPanel panel4=new JPanel();

	public static void main(String[] args) {
		tabbedPane.add(panel3,"Table I");
		tabbedPane.add(panel4,"Table II");
		ShowOnceClusterResult clusterResult = null;
		ShowOnceClusterResult clusterResult2 = null;
		GridLayout gridLayout=new GridLayout(1,2);
		panel.add(ClusterPanel.tmpPanel);
		panel.add(DetailClusterPanel.tmpPanel1);
		panel2.add(ClusterPanel2.tmpPanel);
		panel2.add(DetailClusterPanel2.tmpPanel1);
		panel3.setLayout(gridLayout);
		panel3.add(panel);
		panel4.setLayout(gridLayout);
		panel4.add(panel2);
		try {
			clusterResult = new ShowOnceClusterResult("./compare/cli_clustering.pxd000021.0.7_4.clustering");
			int pageSize = 20;
			String[] columnName = new String[] { "ID", "av_precursor_mz", "av_precursor_intens" };
			List<Clustering> sourceDataList = readFromSQLiteService.getClustersByRunId(1) ;
//			List<Clustering> sourceDataList = clusterResult.readCluster();
			ClusterPanel clusterTablePanel = new ClusterPanel(pageSize, columnName, sourceDataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			clusterResult2 = new ShowOnceClusterResult("./compare/hdp_clustering.pxd000021.0.7_4.clustering");
			int pageSize = 20;
			String[] columnName = new String[] { "ID", "av_precursor_mz", "av_precursor_intens" };
//			List<ICluster> sourceDataList2 = clusterResult2.readCluster();
			List<Clustering> sourceDataList2 = readFromSQLiteService.getClustersByRunId(2) ;
			ClusterPanel2 clusterTablePanel = new ClusterPanel2(pageSize, columnName, sourceDataList2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ShowInTable.frame.add(ShowInTable.tabbedPane);
		ShowInTable.frame.setTitle("Cluster Result");
		ShowInTable.frame.setSize(800, 600);
		ShowInTable.frame.setVisible(true);
	}
}