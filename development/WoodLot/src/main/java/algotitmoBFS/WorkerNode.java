package algotitmoBFS;

import comune.Worker;

class WorkerNode {
    Worker worker;
    int penaltyDays;
    int jobsCompleted;

    public WorkerNode(Worker worker, int penaltyDays, int jobsCompleted) {
        this.worker = worker;
        this.penaltyDays = penaltyDays;
        this.jobsCompleted = jobsCompleted;
    }
}