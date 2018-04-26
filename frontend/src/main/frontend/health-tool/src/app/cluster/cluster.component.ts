import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

//Models
import { Cluster } from '../shared/cluster/cluster.model';
import { JobExample } from './health/job-example.model';
import { ServiceStatus } from "../service/service-status.model";
import { ClusterSnapshot } from './cluster-snapshot.model';
import { CheckHealthToken } from './health/check-health-token.model';
//Services
import { ClusterService } from './cluster.service';
import { ClusterHealthCheckService } from './health/cluster-health-check.service';
import { RouteService } from '../shared/menu/side/route.service';
import { ErrorReportingService } from '../shared/error/error-reporting.service';

@Component({
  selector: 'cluster-info',
  templateUrl: './cluster.component.html',
})
export class ClusterComponent implements OnInit, OnDestroy {
  yarnAppsCount: number;
  //For Inputs should use complex types
  private _checkHealthToken: CheckHealthToken;
  //Reports
  private _hdfsHealthReport: ServiceStatus;
  private _yarnHealthReport: ServiceStatus;
  //Sub notification chanel
  private _sub: Subscription;

  constructor( private router: Router, private route: ActivatedRoute,
    private clusterHealthCheckService: ClusterHealthCheckService, private routeService: RouteService,
    private errorReportingService: ErrorReportingService ) {
      this._sub = routeService.healthCheckMessage$.subscribe(
        clusterName => this.checkHealthToken = this.createHealthCheckToken( clusterName )
      );
      this.makeHealthCheckOnInit();
  }

  ngOnInit() {
    // this.route.paramMap.subscribe( (params: ParamMap) => {
    //   this.checkHealthToken = new CheckHealthToken( params.get( 'id' ), this.createHealthCheckToken() );
    // });
  }

  ngOnDestroy() {
    this._sub.unsubscribe();
  }

  onYarnAppsCountChange( newYarnAppsCount: number ) {
    this.yarnAppsCount = newYarnAppsCount;
  }

  onHdfsHealthCheckRefresh( isRefresh: boolean ) {
    if ( isRefresh ) {
      this.ascForHdfsReports();
    }
  }

  onYarnHealthCheckRefresh( isRefresh: boolean ) {
    if ( isRefresh ) {
      this.ascForYarnReports();
    }
  }

  checkClusterHealth() {
    this.checkHealthToken = this.createNewHealthCheckToken( this._checkHealthToken.clusterName );
  }

  set checkHealthToken( checkHealthToken: CheckHealthToken ) {
    if ( checkHealthToken ) {
      this._checkHealthToken = checkHealthToken;
      this.ascForHdfsAndYarnReports();
    }
  }

  get clusterName(): String {
    return this._checkHealthToken ? this._checkHealthToken.clusterName : null;
  }

  get checkHealthToken(): CheckHealthToken {
    return this._checkHealthToken;
  }

  get hdfsHealthReport(): ServiceStatus {
    return this._hdfsHealthReport;
  }

  get yarnHealthReport(): ServiceStatus {
    return this._yarnHealthReport;
  }

  private ascForHdfsAndYarnReports() {
    this.ascForHdfsReports();
    this.ascForYarnReports();
  }

  private ascForHdfsReports() {
    this._hdfsHealthReport = null;
    this.clusterHealthCheckService.getHdfsClusterState( this._checkHealthToken.clusterName, this._checkHealthToken.token ).subscribe(
      data => this._hdfsHealthReport = data,
      error => this.errorReportingService.reportHttpError( error )
    );
  }

  private ascForYarnReports() {
    this._yarnHealthReport = null;
    this.clusterHealthCheckService.getYarnClusterState( this._checkHealthToken.clusterName, this._checkHealthToken.token ).subscribe(
      data => this._yarnHealthReport = data,
      error => this.errorReportingService.reportHttpError( error )
    )
  }

  private makeHealthCheckOnInit() {
    this.route.paramMap.subscribe( (params: ParamMap) => {
      this.checkHealthToken = this.createHealthCheckToken( params.get( 'id' ) )
    });
  }

  private createHealthCheckToken( clusterName: string ) : CheckHealthToken {
    var routeClusterName = this.routeService.clusterName;
    return routeClusterName ? new CheckHealthToken( routeClusterName, this.createTokenString(), true )
      : new CheckHealthToken( clusterName );
  }

  private createNewHealthCheckToken( clusterName: string ) : CheckHealthToken {
    return new CheckHealthToken( clusterName, this.createTokenString(), true );
  }

  private createTokenString(): string {
    return new Date().getTime().toString();
  }
}
