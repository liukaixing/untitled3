package cn.edu.cqupt.cluster.object; /**
 * Created by Johnny on 2017/3/22.
 */
import java.util.List;

import cn.edu.cqupt.cluster.object.IModification;

/**
 * Created by jg on 24.09.14.
 */
public interface IPeptideSpectrumMatch {
    public String getSequence();

    public List<IModification> getModifications();


}