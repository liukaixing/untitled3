package cn.edu.cqupt.cluster.main;

import java.io.File;
import java.util.List;

import cn.edu.cqupt.cluster.access.ClusteringFileReader;
import cn.edu.cqupt.cluster.access.IClusterSourceReader;
import cn.edu.cqupt.cluster.object.ICluster;

class ShowOnceClusterResult {
	private String filePath;

	public ShowOnceClusterResult(String filePath) {
		// filePath = "./compare/cli_clustering.pxd000021.0.7_4.clustering";
		this.filePath = filePath;
	}

	/**
	 * @return 一个聚类结果文件中所有的聚类结果
	 * @throws Exception
	 */
	public List<ICluster> readCluster() throws Exception {
		File myClusteringFile = new File(filePath);
		IClusterSourceReader reader = new ClusteringFileReader(myClusteringFile);
		List<ICluster> clusters = reader.readAllClusters();
		return clusters;
	}
//
//	/**
//	 * @param index
//	 *            下标
//	 * @return 一个cluster的id
//	 * @throws Exception
//	 */
//	public String showId(int index) throws Exception {
//		List<ICluster> clusters = readCluster();
//		return clusters.get(index).getId();
//
//	}
//
//	/**
//	 * @param index
//	 *            下标
//	 * @return 一个Cluster的av_precursor_mz
//	 * @throws Exception
//	 */
//	public float showAvPrecursorMz(int index) throws Exception {
//		List<ICluster> clusters = readCluster();
//		return clusters.get(index).getAvPrecursorMz();
//	}
//
//	/**
//	 * @param index
//	 *            下标
//	 * @return 一个Cluster的av_precursor_intens
//	 * @throws Exception
//	 */
//	public float showAvPrecursorIntens(int index) throws Exception {
//		List<ICluster> clusters = readCluster();
//		return clusters.get(index).getAvPrecursorIntens();
//
//	}
}