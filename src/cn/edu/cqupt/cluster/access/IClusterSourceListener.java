package cn.edu.cqupt.cluster.access;
import cn.edu.cqupt.cluster.object.ICluster;

/**
 * Created by Johnny on 2017/3/23.
 */
    public interface IClusterSourceListener {
        public void onNewClusterRead(ICluster newCluster);
    }

