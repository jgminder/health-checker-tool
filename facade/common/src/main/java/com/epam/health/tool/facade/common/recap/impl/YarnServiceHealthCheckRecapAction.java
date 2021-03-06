package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component("yarn")
public class YarnServiceHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        try {
            ServiceStatusHolder serviceHealthCheckResult = healthCheckResultsAccumulator.getServiceHealthCheckResult(ServiceTypeEnum.YARN);
            if (!isYarnCheckSuccess(serviceHealthCheckResult)) {
                clusterHealthValidationResult.setClusterHealthy(false);
                clusterHealthValidationResult.appendErrorSummary(getYarnCheckErrorsAsString(serviceHealthCheckResult));
            }
        }
        catch ( InvalidResponseException ex ) {
            //logging here
        }
    }

    private boolean isYarnCheckSuccess(ServiceStatusHolder yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().anyMatch(JobResultProjection::isSuccess);
    }

    private String getYarnCheckErrorsAsString(ServiceStatusHolder yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().map(JobResultProjection::getAlerts)
                .flatMap(Collection::stream).filter(alert -> !alert.isEmpty()).collect(Collectors.joining("\n"));
    }
}
