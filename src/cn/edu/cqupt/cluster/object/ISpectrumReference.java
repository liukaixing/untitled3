package cn.edu.cqupt.cluster.object; /**
 * Created by Johnny on 2017/3/20.
 */
    import java.util.List;

import cn.edu.cqupt.cluster.object.ClusteringFileSpectrumReference;
import cn.edu.cqupt.cluster.object.IPeptideSpectrumMatch;

    /**
     * Created by jg on 01.08.14.
     */
    public interface ISpectrumReference {
        public String getSpectrumId();

        public float getPrecursorMz();

        public int getCharge();

        public float getSimilarityScore();

        public String getSpecies();

        public boolean isIdentifiedAsMultiplePeptides();

        public boolean isIdentified();

        public IPeptideSpectrumMatch getMostCommonPSM();

        public List<IPeptideSpectrumMatch> getPSMs();

        public boolean hasPeaks();

        public List<ClusteringFileSpectrumReference.Peak> getPeaks();

        public void addPeaksFromString(String mzString, String intensityString) throws Exception;
    }

