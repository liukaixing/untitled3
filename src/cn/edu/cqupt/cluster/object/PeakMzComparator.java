package cn.edu.cqupt.cluster.object; /**
 * Created by Johnny on 2017/3/22.
 */
import java.util.Comparator;

import cn.edu.cqupt.cluster.object.ClusteringFileSpectrumReference;

/**
 * Created by jg on 05.01.15.
 */
public class PeakMzComparator implements Comparator<ClusteringFileSpectrumReference.Peak> {
    @Override
    public int compare(ClusteringFileSpectrumReference.Peak o1, ClusteringFileSpectrumReference.Peak o2) {
        return Float.compare(o1.getMz(), o2.getMz());
    }
}