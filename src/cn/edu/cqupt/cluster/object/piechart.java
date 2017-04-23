package cn.edu.cqupt.cluster.object;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

import cn.edu.cqupt.cluster.dao.GraphDao;
import cn.edu.cqupt.cluster.dao.ReadFromSQLiteService;
import cn.edu.cqupt.cluster.object.ICluster;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.data.general.DefaultPieDataset;
/**
 * Created by 61924 on 2017/4/21.
 */
public class piechart {
    public piechart(){};
    static public JFreeChart pie(Object value, Integer runId)//这里的参数可以随便修改
    {
        Integer clusterId = (Integer) value;
        Integer otherRunId = 3-runId;

        DefaultPieDataset dataset = new DefaultPieDataset();
        List<PieCut> pieCuts = GraphDao.getPieChartDataForCluster(clusterId,otherRunId);
//            System.out.println("**For cluster " + clusterId);
//            for (PieCut pieCut : pieCuts) {
//                System.out.println(pieCut.getClusterIdInotherRun() + ":" + pieCut.getPiePercent() + ":" + pieCut.getIntersectionSpectrumNo());
//            }
        for(int i=0;i < pieCuts.size();i++)
        {
            dataset.setValue(pieCuts.get(i).getClusterIdInotherRun() + ":" +pieCuts.get(i).getIntersectionSpectrumNo(), pieCuts.get(i).getIntersectionSpectrumNo());
        }
        JFreeChart chart=ChartFactory.createPieChart("chart",dataset,true,true,true);
        Plot cp=chart.getPlot();
        cp.setBackgroundPaint(ChartColor.WHITE);
        return chart;
    }

}
