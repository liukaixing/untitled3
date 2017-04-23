package cn.edu.cqupt.cluster.main;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import cn.edu.cqupt.cluster.dao.ReadFromSQLiteService;
import cn.edu.cqupt.cluster.object.ICluster;
import cn.edu.cqupt.cluster.object.ISpectrumReference;
import cn.edu.cqupt.cluster.object.Spectrum;

public class DetailShowInTable {
    static ReadFromSQLiteService readFromSQLiteService = ReadFromSQLiteService.getInstance();
//	public void printDetail(int r, Object value) {
//		ShowOnceClusterResult clusterResult = null;
//		try {
//			int pageSize = 20;
//
//			String[] columnName = new String[] { "SpectrumId", "PrecursorMz", "Charge", "SimilarityScore", "Species" };
//			clusterResult = new ShowOnceClusterResult("./compare/mydata.clustering");
//			List<ISpectrumReference> sourceDataList = clusterResult.readCluster().get(r).getSpectrumReferences();
//			new DetailClusterPanel(pageSize, columnName, sourceDataList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    //a new method to read detail info from DB
    public void printDetail(Object value) {

        Integer clusterIdInDB = (Integer) value;
        ShowOnceClusterResult clusterResult = null;
        try {
            int pageSize = 20;

            String[] columnName = new String[]{"SpectrumId", "PrecursorMz", "Charge", "SimilarityScore", "Species"};
            clusterResult = new ShowOnceClusterResult("./compare/mydata.clustering");
            List<Spectrum> sourceDataList = readFromSQLiteService.getSpectrumIdsByClustersId(clusterIdInDB);
            new DetailClusterPanel(pageSize, columnName, sourceDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void printDetail2(Object value) {
        Integer clusterIdInDB = (Integer) value;
    		ShowOnceClusterResult clusterResult2 = null;
		try {
			int pageSize = 20;

			String[] columnName = new String[]{"SpectrumId", "PrecursorMz", "Charge", "SimilarityScore", "Species"};
//			clusterResult2 = new ShowOnceClusterResult("./compare/hdp_clustering.pxd000021.0.7_4.clustering");
//			List<ISpectrumReference> sourceDataList2 = clusterResult2.readCluster().get(r).getSpectrumReferences();
			List<Spectrum> sourceDataList2 = readFromSQLiteService.getSpectrumIdsByClustersId(clusterIdInDB);
			new DetailClusterPanel2(pageSize, columnName, sourceDataList2);
		} catch (Exception e) {
			e.printStackTrace();

		}
    }
}
