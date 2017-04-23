package cn.edu.cqupt.cluster.dao;

import cn.edu.cqupt.cluster.object.Clustering;
import cn.edu.cqupt.cluster.object.Spectrum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartPanel;

/**
 * Created by mingze on 17-4-17.
 */
public class ReadFromSQLiteService {
    private static ReadFromSQLiteService readFromSQLiteService = new ReadFromSQLiteService();
    private static Connection connection;
    static public ChartPanel panel=null;

    private ReadFromSQLiteService() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:compare-clustering.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ReadFromSQLiteService getInstance() {
        return readFromSQLiteService;
    }

    public static List<String> getRuns() {
        List<String> runStrings = new ArrayList<String>();
        StringBuilder sql0 = new StringBuilder("SELECT * FROM run");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int run_id = rs.getInt(1);
                String run_name = rs.getString(2);
                Date run_importe_datetime = rs.getDate(3);
                String run = "'runId':" + run_id + "; 'runName':" + run_name + "; 'loadDate':" + run_importe_datetime;
                runStrings.add(run);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return runStrings;
    }

    public static List<Integer> getClusterIdsByRunId(Integer runId) {
        List<Integer> clusterIds = new ArrayList<Integer>();
        StringBuilder sql0 = new StringBuilder("SELECT cluster_id FROM run_cluster WHERE run_id = " + runId.toString());
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer clusterId = rs.getInt(1);
                clusterIds.add(clusterId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clusterIds;
    }

    public static List<Clustering> getClustersByRunId(Integer runId) {
        List<Clustering> clusterings = new ArrayList<Clustering>();

        List<Integer> clusteringIds = getClusterIdsByRunId(runId);
        for (Integer clusteringId : clusteringIds) {
            StringBuilder sql0 = new StringBuilder("SELECT * FROM cluster WHERE id = " + clusteringId.toString());
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(sql0.toString());
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()) {
                    Clustering clustering1 = new Clustering();
                    clustering1.setId(rs.getInt("id"));
                    clustering1.setClusterId(rs.getString("cluster_uni_id"));
                    clustering1.setAvPrecursorMz(rs.getFloat("av_precursor_mz"));
                    clustering1.setAvPrecursorIntens(rs.getFloat("av_precursor_intens"));

                    Integer clusterId = rs.getInt(1);
                    clusterings.add(clustering1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return clusterings;
    }

    public static List<Integer> getSpectrumIdsByClusterId(Integer clusterId) {
        List<Integer> spectrumIds = new ArrayList<Integer>();
        StringBuilder sql0 = new StringBuilder("SELECT spectrum_id FROM cluster_spectrum WHERE cluster_id = " + clusterId.toString());
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer spectrumId = rs.getInt(1);
                spectrumIds.add(spectrumId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spectrumIds;
    }

    public static Spectrum getSpectrumById(Integer spectrumId) {
        StringBuilder sql0 = new StringBuilder("SELECT * FROM spectrum WHERE spectrum_id = " + spectrumId.toString());
        PreparedStatement preparedStatement = null;
        Spectrum spectrum = new Spectrum();
        try {
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                spectrum.setSpectrumId(rs.getString("spectrum_title"));//spectrum title
                spectrum.setCharge(rs.getFloat("charge"));
                spectrum.setPrecursorMz(rs.getFloat("precursormz"));
                spectrum.setSpecies(rs.getString("species"));
                spectrum.setSimilarityScore(rs.getFloat("similarityscore"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spectrum;
    }

    public static Clustering getClusterById(Integer clusterId) {
        StringBuilder sql0 = new StringBuilder("SELECT * FROM cluster WHERE id = " + clusterId.toString());
        PreparedStatement preparedStatement = null;
        Clustering cluster = new Clustering();
        try {
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                cluster.setClusterId(rs.getString("cluster_uni_id"));//
                cluster.setAvPrecursorIntens(rs.getFloat("av_precursor_mz"));
                cluster.setAvPrecursorMz(rs.getFloat("av_precursor_intens"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cluster;
    }

    public static Integer getClusterIdBySpectrumInRun(Integer spectrumId, Integer runId) {
        Integer clusterId = 0;
        StringBuilder sql0 = new StringBuilder("SELECT * FROM cluster_spectrum INNER JOIN run_cluster")
                .append(" ON spectrum_id = " + spectrumId.toString())
                .append(" AND run_cluster.run_id = " + runId)
                .append(" AND run_cluster.cluster_id = cluster_spectrum.cluster_id");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                clusterId = rs.getInt("cluster_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clusterId;
    }

    public List<Spectrum> getSpectrumIdsByClustersId(Integer clusterIdInDB) {
        List<Spectrum> spectrums = new ArrayList<>();
        List<Integer> spectrumids = getSpectrumIdsByClusterId(clusterIdInDB);
        for (Integer spectrumid : spectrumids) {
            spectrums.add(getSpectrumById(spectrumid));
        }
        return spectrums;
    }

//	public static Integer getIntersectionSpectrumNo(Integer clusterId1, Integer clusterId2) {
//		Integer IntersectionSpectrumNo = 0;
//		StringBuilder sql0 = new StringBuilder("SELECT * FROM cluster_spectrum INNER JOIN run_cluster")
//			.append(" ON spectrum_id = " + spectrumId.toString())
//			.append(" run_cluster.run_id = " + runId)
//			.append(" run_cluster.cluster_id = cluster_spectrum.cluster_id");
//		PreparedStatement preparedStatement = null;
//		try {
//			preparedStatement = connection.prepareStatement(sql0.toString());
//			ResultSet rs = preparedStatement.executeQuery();
//			if (rs.next()) {
//				clusterId = rs.getInt("cluster_id");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return clusterId;
//	}
}
