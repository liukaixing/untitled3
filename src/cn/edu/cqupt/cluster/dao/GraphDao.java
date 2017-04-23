package cn.edu.cqupt.cluster.dao;

import cn.edu.cqupt.cluster.dao.ReadFromSQLiteService;
import cn.edu.cqupt.cluster.object.PieCut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mingze on 17-4-18.
 */
public class GraphDao {
    public static List<PieCut> getPieChartDataForCluster(Integer clusterId, Integer otherRunId) {
        ReadFromSQLiteService readFromSQLiteService = ReadFromSQLiteService.getInstance();
        ArrayList<PieCut> pieCuts = new ArrayList<PieCut>();
        ArrayList<Integer> clusterIds = new ArrayList<Integer>();


        List<Integer> spectrumIds = readFromSQLiteService.getSpectrumIdsByClusterId(clusterId);
        HashMap<Integer, Integer> clusterMap = new HashMap<Integer, Integer>();

        for (Integer spectrumId : spectrumIds) {
            Integer clusterIdInOtherRun = readFromSQLiteService.getClusterIdBySpectrumInRun(spectrumId, otherRunId);
            if (clusterIdInOtherRun == 0) { //got no cluster info in the other run
                continue;
            }
            if (!clusterMap.containsKey(clusterIdInOtherRun)) {
                clusterMap.put(clusterIdInOtherRun,1);
            }
            else{
                clusterMap.put(clusterIdInOtherRun, clusterMap.get(clusterIdInOtherRun) + 1);
            }
        }

        Integer totalSharedSpectrumNo = 0;
        for(Integer clusterIdInOtherRun : clusterMap.keySet()) {
            Integer intersectionSpectrumNo = clusterMap.get(clusterIdInOtherRun);
            PieCut piecut = new PieCut();
            piecut.setClusterId(clusterId);
            piecut.setClusterIdInotherRun(clusterIdInOtherRun);
            piecut.setIntersectionSpectrumNo(intersectionSpectrumNo);
            piecut.setPiePercent(((float)intersectionSpectrumNo / spectrumIds.size()));
            totalSharedSpectrumNo += intersectionSpectrumNo;

            pieCuts.add(piecut);
        }
        PieCut piecut = new PieCut();
        piecut.setClusterId(clusterId);
        piecut.setClusterIdInotherRun(0);
        piecut.setIntersectionSpectrumNo(spectrumIds.size() - totalSharedSpectrumNo);
        piecut.setPiePercent((float) ((spectrumIds.size() - totalSharedSpectrumNo) / spectrumIds.size()));
        if (piecut.getIntersectionSpectrumNo() > 0) {
            pieCuts.add(piecut);
        }

        return pieCuts;
    }
}

