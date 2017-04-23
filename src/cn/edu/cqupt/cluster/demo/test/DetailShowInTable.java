package cn.edu.cqupt.cluster.demo.test;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import cn.edu.cqupt.cluster.object.ICluster;
import cn.edu.cqupt.cluster.object.ISpectrumReference;

public class DetailShowInTable {
	public void printDetail(int r, Object value) {
		ShowOnceClusterResult clusterResult = null;
		try {
			int pageSize = 20;
			
			String[] columnName = new String[] { "SpectrumId", "PrecursorMz", "Charge", "SimilarityScore", "Species" };
			clusterResult = new ShowOnceClusterResult("./compare/mydata.clustering");
			List<ISpectrumReference> sourceDataList = clusterResult.readCluster().get(r).getSpectrumReferences();
			new DetailClusterPanel(pageSize, columnName, sourceDataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}