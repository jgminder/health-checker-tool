package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.resolver.CommonFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RunningClusterFacadeImplResolver extends CommonFacadeImplResolver<IRunningClusterParamReceiver> {
    @Autowired
    private Map<String, IRunningClusterParamReceiver> clusterSnapshotFacadeMap;

    @Override
    protected Map<String, IRunningClusterParamReceiver> getFacadeImplBeansMap() {
        return clusterSnapshotFacadeMap;
    }
}
