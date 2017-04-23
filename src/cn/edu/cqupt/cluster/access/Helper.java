package cn.edu.cqupt.cluster.access;

import java.io.File;
import java.util.List;
import cn.edu.cqupt.cluster.object.ICluster;

public class Helper {
	public List<ICluster> getClusters(String path) {
		File myClusteringFile = new File(path);
		IClusterSourceReader reader = new ClusteringFileReader(myClusteringFile);
		List<ICluster> clusters = null;
		try {
			clusters = reader.readAllClusters();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clusters;
	}
}
