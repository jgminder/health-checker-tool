import { NodeSummary } from './health/node-summary.model';
import { NodeFs } from './health/common/node-fs.model';
import { HdfsUsage } from './health/common/hdfs.model';
import { Memory } from './health/common/memory.model';

export class ClusterSnapshot {
  name: string;
  host: string;
  clusterType: string;
  nodes: NodeFs[];
  hdfsUsage: HdfsUsage;
  memoryUsage: Memory;
  dateOfSnapshot: Date;
  secured: boolean;
}
