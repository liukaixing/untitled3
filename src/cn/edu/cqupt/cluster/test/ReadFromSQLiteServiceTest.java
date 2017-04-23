package cn.edu.cqupt.cluster.test;

import cn.edu.cqupt.cluster.dao.ReadFromSQLiteService;
import junit.framework.TestCase;
import cn.edu.cqupt.cluster.object.Clustering;
import cn.edu.cqupt.cluster.object.Spectrum;

import java.util.List;

/**
 * Created by mingze on 17-4-17.
 */
public class ReadFromSQLiteServiceTest extends TestCase {
    ReadFromSQLiteService readFromSQLiteService = ReadFromSQLiteService.getInstance();
    public void setUp() throws Exception {
        super.setUp();

    }

    public void testGetRuns() throws Exception {
        List<String> runs = readFromSQLiteService.getRuns();
        for(String run: runs){
            System.out.println(run);
        }

    }

    public void testGetClusterIdsByRunId() throws Exception {
        List<Integer> clusterIds = readFromSQLiteService.getClusterIdsByRunId(1);
        for (Integer clusterId : clusterIds) {
            System.out.println(clusterId);
        }
    }

    public void testGetSpectrumIdsByClusterId() throws Exception {
        List<Integer> spectrumIds = readFromSQLiteService.getSpectrumIdsByClusterId(1);
        for (Integer spectrumId: spectrumIds) {
            System.out.println(spectrumId);
        }

    }

    public void testGetSpectrumById() throws Exception {
        Spectrum spectrum = readFromSQLiteService.getSpectrumById(1);
        System.out.print("spectrum_title : " + spectrum.getSpectrumId() + "; ");
        System.out.print("spectrum_charge: " + spectrum.getCharge() + "; ");
        System.out.print("spectrum_precursorMz: " + spectrum.getPrecursorMz() + "; ");
        System.out.print("spectrum_species: " + spectrum.getSpecies() + "; ");
        System.out.println("spectrum_similarity_score: " + spectrum.getSimilarityScore() + "; ");
    }

    public void testGetClusterById() throws Exception {
        Clustering cluster = readFromSQLiteService.getClusterById(1);
        System.out.print("cluster_uni_id : " + cluster.getClusterId() + "; ");
        System.out.print("cluster_avPrecursorMz : " + cluster.getAvPrecursorMz() + "; ");
        System.out.print("cluster_avPrecursorInt : " + cluster.getAvPrecursorIntens() + "; ");
    }

}