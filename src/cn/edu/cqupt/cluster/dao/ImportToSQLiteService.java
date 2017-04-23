package cn.edu.cqupt.cluster.dao;

import cn.edu.cqupt.cluster.object.ICluster;
import cn.edu.cqupt.cluster.object.ISpectrumReference;
import cn.edu.cqupt.cluster.object.Triplet;
import cn.edu.cqupt.cluster.access.ClusteringFileReader;
import cn.edu.cqupt.cluster.access.IClusterSourceReader;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by mingze on 17-4-17.
 */
public class ImportToSQLiteService {
    private static ImportToSQLiteService importToSQLiteService = new ImportToSQLiteService();
    private static Connection connection;

    public ImportToSQLiteService() {
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

    public static ImportToSQLiteService getInstance() {
        return importToSQLiteService;
    }

    public static void importFile(String inputfilePath1, String inputfilePath2) {

        String tempFilePath1 = inputfilePath1.trim();
        String fileName1 = tempFilePath1.substring(tempFilePath1.lastIndexOf("/") + 1);
        String runName1 = fileName1.replace(".clustering", "");

        String tempFilePath2 = inputfilePath2.trim();
        String fileName2 = tempFilePath2.substring(tempFilePath2.lastIndexOf("/") + 1);
        String runName2 = fileName2.replace(".clustering", "");

        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<ICluster> iClusters1 = new ArrayList<ICluster>();
        List<ICluster> iClusters2 = new ArrayList<ICluster>();
        int id = 0;
        iClusters1 = getClustersFromFile(inputfilePath1);
        iClusters2 = getClustersFromFile(inputfilePath2);

        removeIdenticalClusterPairs(iClusters1, iClusters2);
        List<Triplet<String, String, Integer>> intersectionList = getIntersections(iClusters1, iClusters2);

        //create the tables if not exist
        createRunTable(connection, preparedStatement);
        createClusterTable(connection, preparedStatement);
        createSpectrumTable(connection, preparedStatement);
        createClusterSpectrumTable(connection, preparedStatement);
        createRunClusterTable(connection, preparedStatement);
        createClusterSimilarityTable(connection, preparedStatement);

        HashMap<String, Integer> existSpectrumMap = new HashMap<String, Integer>();
        writeClusterIntoDB(runName1, iClusters1, connection, preparedStatement, existSpectrumMap);
        writeClusterIntoDB(runName2, iClusters2, connection, preparedStatement, existSpectrumMap);

        writeIntersectionInfoToDB(intersectionList, connection, preparedStatement);

        try {
            connection.commit();
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            // TODO Auto-generated catch block
            if (connection != null) {
                connection.close();
                connection = null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void writeIntersectionInfoToDB(List<Triplet<String, String, Integer>> intersectionList,
                                                  Connection connection, PreparedStatement preparedStatement) {

        for (Triplet<String, String, Integer> triplet : intersectionList) {
            StringBuilder sql1 = new StringBuilder("INSERT into cluster_similarity VALUES(")
                    .append(" '" + triplet.getFirst() + "',")
                    .append(" '" + triplet.getSecond() + "',")
                    .append(" " + triplet.getThird() + ")");
            try {
                preparedStatement = connection.prepareStatement(sql1.toString());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(0);
            }

        }

    }

    //insert the runs, clusters and spectra
    private static void writeClusterIntoDB(String runName, List<ICluster> iClusters,
                                           Connection connection, PreparedStatement preparedStatement, HashMap<String, Integer> existSpectrumMap) {

        ResultSet resultSet = null;
        boolean firstRun= false;
        if (existSpectrumMap.size() == 0) {
            firstRun = true;
        }

        try {

            //check if this run has been imported
            StringBuilder sql0 = new StringBuilder("SELECT * FROM run WHERE run_name = '" + runName + "'");
            preparedStatement = connection.prepareStatement(sql0.toString());
            ResultSet rs = preparedStatement.executeQuery();
            int run_id = 0;
            if (rs.next()) {
                run_id = rs.getInt(1);
                Date run_importe_datetime = rs.getDate(3);
                System.out.println("ERROR: this run " + runName + " has been imported as run id = " + run_id + " at " + run_importe_datetime);
                System.exit(1);
            } else {
                sql0 = new StringBuilder("INSERT INTO run VALUES(");
                sql0.append("NULL, ")
                        .append("'" + runName + "',")
                        .append("NULL)"); //current date time
                preparedStatement = connection.prepareStatement(sql0.toString(), Statement.RETURN_GENERATED_KEYS);
                preparedStatement.execute();
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    run_id = rs.getInt(1);
                    System.out.println("inserted run id = " + run_id);
                }
            }

            for (int i = 0; i < iClusters.size(); i++) {
                List<ISpectrumReference> spectrumReferences = (iClusters.get(i)).getSpectrumReferences();

                StringBuilder sql1 = new StringBuilder("INSERT into cluster VALUES(");
                sql1.append("NULL, '") //auto incremental
                        .append(iClusters.get(i).getId() + "',")
                        .append(iClusters.get(i).getAvPrecursorMz() + ",")
                        .append(iClusters.get(i).getAvPrecursorIntens() + ",")
                        .append(spectrumReferences.size() + ")");
                preparedStatement = connection.prepareStatement(sql1.toString(), Statement.RETURN_GENERATED_KEYS);
                preparedStatement.execute();
                rs = preparedStatement.getGeneratedKeys();
                int cluster_id = 0;
                if (rs.next()) {
                    cluster_id = rs.getInt(1);
                }

                sql1 = new StringBuilder("INSERT into run_cluster VALUES(");
                sql1.append(run_id + ",")
                        .append(cluster_id + "")
                        .append(")");
                preparedStatement = connection.prepareStatement(sql1.toString());
                preparedStatement.execute();


                for (ISpectrumReference spectrum : spectrumReferences) {
                    int spectrum_id = 0;

                    if (firstRun || !existSpectrumMap.containsKey(spectrum.getSpectrumId())) {
                        sql1 = new StringBuilder("INSERT INTO spectrum VALUES(");
                        sql1.append("NULL,'")//auto incremental
                                .append(spectrum.getSpectrumId() + "',")//spectrum title
                                .append(spectrum.getCharge() + ",")
                                .append(spectrum.getPrecursorMz() + ",'")
                                .append(spectrum.getSpecies() + "',")
                                .append(spectrum.getSimilarityScore() + ")");
//					System.out.println(sql2.toString());
                        preparedStatement = connection.prepareStatement(sql1.toString(), Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.execute();
                        rs = preparedStatement.getGeneratedKeys();

                        if (rs.next()) {
                            spectrum_id = rs.getInt(1);
                        }
                        existSpectrumMap.put(spectrum.getSpectrumId(), spectrum_id);
                    }else {
                        spectrum_id = existSpectrumMap.get(spectrum.getSpectrumId()); //get table id from title
                    }

                    sql1 = new StringBuilder("INSERT into cluster_spectrum VALUES(");
                    sql1.append("")
                            .append(cluster_id + ",")
                            .append(spectrum_id + ")");
                    preparedStatement = connection.prepareStatement(sql1.toString());
                    preparedStatement.execute();
/*
                    for (Integer clusterId : overlappedClusters.keySet()) {
                        Integer intersectionSize = overlappedClusters.get(clusterId);
                        float overlapScore1 = (float) intersectionSize / existClusterMap.get(clusterId);
                        float overlapScore2 = (float) intersectionSize / spectrumReferences.size();
                        sql1 = new StringBuilder("INSERT into cluster_similarity VALUES(");
                        sql1.append("")
                                .append(clusterId + ",")
                                .append(cluster_id + ",")
                                .append(overlapScore1 + ",")
                                .append(overlapScore2 + ")");
                        preparedStatement = connection.prepareStatement(sql1.toString());
                        preparedStatement.execute();
                    }*/
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                    resultSet = null;
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }
    }

    private static List<Triplet<String, String, Integer>> getIntersections(List<ICluster> iClusters1, List<ICluster> iClusters2) {
        List<Triplet<String, String, Integer>> intersectionList = new ArrayList<Triplet<String, String, Integer>>();
        for (ICluster cluster1 : iClusters1) {
            for (ICluster cluster2 : iClusters2) {
                Float dltPrecursorMz = cluster1.getAvPrecursorMz() - cluster2.getAvPrecursorMz();
                if (dltPrecursorMz > 10) {
                    continue;
                }

                List<ISpectrumReference> spectrums1 = cluster1.getSpectrumReferences();
                List<ISpectrumReference> spectrums2 = cluster2.getSpectrumReferences();
                List<String> spectrumTitles1 = new ArrayList<String>();
                List<String> spectrumTitles2 = new ArrayList<String>();

                for (ISpectrumReference spectrum1 : spectrums1) {
                    spectrumTitles1.add(spectrum1.getSpectrumId());
                }
                Collections.sort(spectrumTitles1);

                for (ISpectrumReference spectrum2 : spectrums2) {
                    spectrumTitles2.add(spectrum2.getSpectrumId());
                }
                Collections.sort(spectrumTitles2);

                Integer intersectionSize = 0;
                for (int i = 0; i < Math.min(spectrumTitles1.size(), spectrumTitles2.size()); i++) {
                    if (spectrumTitles1.get(i).equals(spectrumTitles2.get(i))) {
                        intersectionSize++;
                    } else {
                        break;
                    }
                }

                if (intersectionSize > 0) {
                    Triplet<String, String, Integer> clusterPair = new Triplet<String, String, Integer>(cluster1.getId(), cluster2.getId(), intersectionSize);
                    intersectionList.add(clusterPair);
                }

            }
        }
        return intersectionList;
    }

    private static void removeIdenticalClusterPairs(List<ICluster> iClusters1, List<ICluster> iClusters2) {
        int i, j;
        for (i = iClusters1.size() - 1; i >= 0; i--) {
            ICluster cluster1 = iClusters1.get(i);
            for (j = iClusters2.size() - 1; j >= 0; j--) {
                ICluster cluster2 = iClusters2.get(j);
                if (isIdentical(cluster1, cluster2)) {
                    iClusters1.remove(i);
                    iClusters2.remove(j);
                    break;
                }
            }
        }
    }

    private static boolean isIdentical(ICluster cluster1, ICluster cluster2) {
        boolean isIdentical = true;

        List<ISpectrumReference> spectrums1 = cluster1.getSpectrumReferences();
        List<ISpectrumReference> spectrums2 = cluster2.getSpectrumReferences();
        if (spectrums1.size() != spectrums2.size()) {
            isIdentical = false;
            return isIdentical;
        }

        List<String> spectrumTitles1 = new ArrayList<String>();
        List<String> spectrumTitles2 = new ArrayList<String>();

        for (ISpectrumReference spectrum1 : spectrums1) {
            spectrumTitles1.add(spectrum1.getSpectrumId());
        }
        Collections.sort(spectrumTitles1);

        for (ISpectrumReference spectrum2 : spectrums2) {
            spectrumTitles2.add(spectrum2.getSpectrumId());
        }
        Collections.sort(spectrumTitles2);

        for (int i = 0; i < spectrumTitles1.size(); i++) {
            if (!spectrumTitles1.equals(spectrumTitles2)) {
                isIdentical = false;
                return isIdentical;
            }
        }

        return isIdentical;
    }

    private static HashMap<Integer, Integer> getExistClusterMap() {

        HashMap<Integer, Integer> existCluseterMap = new HashMap<Integer, Integer>();
        StringBuilder sqlString = new StringBuilder("SELECT id, spectrum_no FROM cluster");
        PreparedStatement preparedStatement2 = null;
        try {
            preparedStatement2 = connection.prepareStatement(sqlString.toString());
            preparedStatement2.execute();
            ResultSet rs2 = preparedStatement2.getResultSet();
            while (rs2.next()) {
                existCluseterMap.put(rs2.getInt("id"), rs2.getInt("spectrum_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existCluseterMap;
    }

    private static HashMap<Integer, Integer> getExistSpectrumClusterMap(HashMap<String, Integer> existSpectrumMap) {
        HashMap<Integer, Integer> existSpectrumCluseterMap = new HashMap<Integer, Integer>();
        StringBuilder sqlString = new StringBuilder("SELECT spectrum_id, cluster_id FROM cluster_spectrum");
        PreparedStatement preparedStatement2 = null;
        try {
            preparedStatement2 = connection.prepareStatement(sqlString.toString());
            preparedStatement2.execute();
            ResultSet rs2 = preparedStatement2.getResultSet();
            while (rs2.next()) {
                existSpectrumCluseterMap.put(rs2.getInt("spectrum_id"), rs2.getInt("cluster_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existSpectrumCluseterMap;
    }

    private static HashMap<String, Integer> getExistSpectrumMap() {
        HashMap<String, Integer> existSpectrumMap = new HashMap<String, Integer>();

        StringBuilder sqlString = new StringBuilder("SELECT spectrum_id, spectrum_title FROM spectrum");
        PreparedStatement preparedStatement2 = null;
        try {
            preparedStatement2 = connection.prepareStatement(sqlString.toString());
            preparedStatement2.execute();
            ResultSet rs2 = preparedStatement2.getResultSet();
            while (rs2.next()) {
                existSpectrumMap.put(rs2.getString("spectrum_title"), rs2.getInt("spectrum_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existSpectrumMap;
    }


    private static void createClusterTable(Connection connection, PreparedStatement preparedStatement) {
        StringBuilder sqlString = new StringBuilder("CREATE TABLE IF NOT EXISTS `cluster` (");
        sqlString.append(" `id` INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlString.append(" `cluster_uni_id` char(40) NOT NULL,");
        sqlString.append(" `av_precursor_mz` float(8,3) DEFAULT NULL,");
        sqlString.append(" `av_precursor_intens` float(3,1) DEFAULT NULL,");
        sqlString.append(" `spectrum_no` INTEGER NOT NULL");
        sqlString.append(")");

        try {
            preparedStatement = connection.prepareStatement(sqlString.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createSpectrumTable(Connection connection, PreparedStatement preparedStatement) {
        StringBuilder sqlString = new StringBuilder("CREATE TABLE IF NOT EXISTS `spectrum` (");
        sqlString.append("`spectrum_id` INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlString.append("`spectrum_title` char(60) UNIQUE NOT NULL,");
        sqlString.append("`charge` float(2,1) DEFAULT NULL,");
        sqlString.append("`precursormz` double(10,5) NOT NULL,");
        sqlString.append("`species` varchar(20) DEFAULT NULL,");
        sqlString.append("`similarityscore` float(3,1) DEFAULT NULL");
        sqlString.append(")");
        try {
            preparedStatement = connection.prepareStatement(sqlString.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createClusterSpectrumTable(Connection connection, PreparedStatement preparedStatement) {
        StringBuilder sqlString = new StringBuilder("CREATE TABLE IF NOT EXISTS `cluster_spectrum` (");
        sqlString.append(" `cluster_id` int(20) NOT NULL,");
        sqlString.append(" `spectrum_id` int(20) NOT NULL,");
        sqlString.append("CONSTRAINT `cluster_id` FOREIGN KEY (`cluster_id`) REFERENCES `cluster` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,");
        sqlString.append("CONSTRAINT `spectrum_id` FOREIGN KEY (`spectrum_id`) REFERENCES `spectrum` (`id`) ON DELETE CASCADE ON UPDATE CASCADE");
        sqlString.append(")");

        try {
            preparedStatement = connection.prepareStatement(sqlString.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createRunClusterTable(Connection connection, PreparedStatement preparedStatement) {
        StringBuilder sqlString = new StringBuilder("CREATE TABLE IF NOT EXISTS `run_cluster` (");
        sqlString.append(" `run_id` int(20) NOT NULL,");
        sqlString.append(" `cluster_id` int(20) NOT NULL,");
        sqlString.append("CONSTRAINT `run_id` FOREIGN KEY (`run_id`) REFERENCES `run` (`run_id`) ON DELETE CASCADE ON UPDATE CASCADE,");
        sqlString.append("CONSTRAINT `cluster_id` FOREIGN KEY (`cluster_id`) REFERENCES `cluster` (`id`) ON DELETE CASCADE ON UPDATE CASCADE");
        sqlString.append(")");

        try {
            preparedStatement = connection.prepareStatement(sqlString.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createRunTable(Connection connection, PreparedStatement preparedStatement) {
        StringBuilder sqlString = new StringBuilder("CREATE TABLE IF NOT EXISTS `run` (");
        sqlString.append("`run_id` INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlString.append("`run_name` char(60) NOT NULL,");
        sqlString.append("`loadtime` DATETIME DEFAULT CURRENT_TIMESTAMP");
        sqlString.append(")");
        try {
//			System.out.println("Going to execute this sql string: " + sqlString);
            preparedStatement = connection.prepareStatement(sqlString.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<ICluster> getClustersFromFile(String path) {
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

    private static void createClusterSimilarityTable(Connection connection, PreparedStatement preparedStatement) {
        StringBuilder sqlString = new StringBuilder("CREATE TABLE IF NOT EXISTS `cluster_similarity` (");
        sqlString.append(" `cluster_id_run1` int(20) NOT NULL,");
        sqlString.append(" `cluster_id_run2` int(20) NOT NULL,");
        sqlString.append(" `cluster_share` int(20) NOT NULL,");
        sqlString.append("CONSTRAINT `cluster_id_run1` FOREIGN KEY (`cluster_id_run1`) REFERENCES `cluster` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,");
        sqlString.append("CONSTRAINT `cluster_id_run2` FOREIGN KEY (`cluster_id_run2`) REFERENCES `cluster` (`id`) ON DELETE CASCADE ON UPDATE CASCADE");
        sqlString.append(")");

        try {
            preparedStatement = connection.prepareStatement(sqlString.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
