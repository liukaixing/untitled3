package cn.edu.cqupt.cluster.test;

import cn.edu.cqupt.cluster.dao.ImportToSQLiteService;
import junit.framework.TestCase;

/**
 * Created by mingze on 17-4-17.
 */
public class ImportToSQLiteServiceTest extends TestCase {
    ImportToSQLiteService importToSQLiteService = ImportToSQLiteService.getInstance();
    public void testImportFile() throws Exception {
//        importToSQLiteService.importFile("/home/mingze/work/pride-cluster/cluster-comparer-viz/Material/compare/cli_clustering.pxd000021.0.7_4.clustering");
        importToSQLiteService.importFile("/home/mingze/work/pride-cluster/cluster-comparer-viz/Material/compare/cli_clustering.pxd000021.0.7_4.clustering",
                "/home/mingze/work/pride-cluster/cluster-comparer-viz/Material/compare/hdp_clustering.pxd000021.0.7_4.clustering");
    }
}