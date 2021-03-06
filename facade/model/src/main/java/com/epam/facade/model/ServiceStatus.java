package com.epam.facade.model;

import com.epam.facade.model.accumulator.results.BaseActionResult;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * this class is common for showing on ui, implements project from database layer,
 * do not update this class for vendor specific transferring, names are used for ui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceStatus implements ServiceStatusHolder, BaseActionResult {
    private ServiceTypeEnum type;
    private ServiceStatusEnum healthSummary;
    private List<JobResultProjection> jobResults;
    private String logDirectory;
    private String clusterNode;

    //todo: delete health status
    public ServiceStatusEnum getHealthStatus() {
        return healthSummary;
    }

    public void setHealthStatus(ServiceStatusEnum healthSummary) {
        this.healthSummary = healthSummary;
    }

    @Override
    public ServiceTypeEnum getType() {
        return type;
    }

    public void setType(ServiceTypeEnum type) {
        this.type = type;
    }

    @Override
    public ServiceStatusEnum getHealthSummary() {
        return healthSummary;
    }

    public void setHealthSummary(ServiceStatusEnum healthSummary) {
        this.healthSummary = healthSummary;
    }

    @Override
    public ServiceTypeEnum getDisplayName() {
        return type;
    }

    @Override
    public String getLogDirectory() {
        return logDirectory;
    }

    @Override
    public String getClusterNode() {
        return clusterNode;
    }

    @Override
    public List<JobResultProjection> getJobResults() {
        return jobResults;
    }

    public void setJobResults(List<JobResultProjection> jobResults) {
        this.jobResults = jobResults;
    }

    @Override
    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    @Override
    public void setClusterNode(String clusterNode) {
        this.clusterNode = clusterNode;
    }

    @Override
    public String toString() {
        return "name: \"" + getDisplayName() + "\" " + getHealthStatus() + " " + getType();
    }

    @Override
    public HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.OTHER_SERVICES;
    }
}
