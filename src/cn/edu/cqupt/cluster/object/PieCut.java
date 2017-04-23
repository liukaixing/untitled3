package cn.edu.cqupt.cluster.object;

/**
 * Created by mingze on 17-4-18.
 */
public class PieCut {
        Integer clusterId;
        Integer clusterIdInotherRun;
        Integer intersectionSpectrumNo;
        Float PiePercent;

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Float getPiePercent() {
        return PiePercent;
    }

    public Integer getClusterIdInotherRun() {
        return clusterIdInotherRun;
    }

    public void setClusterIdInotherRun(Integer clusterIdInotherRun) {
        this.clusterIdInotherRun = clusterIdInotherRun;
    }

    public Integer getIntersectionSpectrumNo() {
        return intersectionSpectrumNo;
    }

    public void setIntersectionSpectrumNo(Integer intersectionSpectrumNo) {
        this.intersectionSpectrumNo = intersectionSpectrumNo;
    }

    public void setPiePercent(Float piePercent) {
        PiePercent = piePercent;
    }
}
