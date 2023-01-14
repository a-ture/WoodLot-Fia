package algotitmoBFS;

import comune.Job;
import comune.Worker;

import java.util.*;

import static algoritmoGreedy.BestFitGreedy.assignJobs;

public class BFS {

    public static Map<Integer, Integer> assignJobsBFS(List<Job> jobs, List<Worker> workers) {
        Map<Integer, Integer> jobAssignments = new HashMap<>();
        Queue<WorkerNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Aggiungiamo i lavoratori alla coda
        for (Worker worker : workers) {
            queue.add(new WorkerNode(worker, 0, 0));
        }

        while (!queue.isEmpty()) {
            WorkerNode node = queue.poll();
            Worker worker = node.worker;

            // Generiamo una chiave univoca per questo stato
            String key = worker.getId() + "," + node.penaltyDays + "," + node.jobsCompleted;
            if (!visited.contains(key)) {
                visited.add(key);

                // Se non ci sono più lavori da assegnare, restituiamo l'assegnamento corrente
                if (node.jobsCompleted == jobs.size()) {
                    return jobAssignments;
                }

                // Cerchiamo il prossimo lavoro da assegnare al lavoratore
                Job nextJob = null;
                for (int i = node.jobsCompleted; i < jobs.size(); i++) {
                    Job job = jobs.get(i);
                    if (job.getEligibleWorkers().contains(worker.getId())) {
                        nextJob = job;
                        break;
                    }
                }

                // Se il lavoratore è idoneo per il prossimo lavoro, lo assegniamo e aggiungiamo un nuovo stato per il lavoratore con i giorni di penalità aggiornati
                if (nextJob != null) {
                    jobAssignments.put(nextJob.id, worker.getId());
                    queue.add(new WorkerNode(worker, node.penaltyDays + nextJob.getDuration(), node.jobsCompleted + 1));
                }
            }
        }

        // Se siamo arrivati qui, non c'è una soluzione valida
        return null;
    }

    public static void main(String[] args) {
        // Creiamo una lista di lavori
        List<Job> jobs = Arrays.asList(
                new Job(1, 2, Arrays.asList(1, 2)),
                new Job(2, 2, Arrays.asList(1, 3)),
                new Job(3, 3, Arrays.asList(2, 3)),
                new Job(4, 5, Arrays.asList(3))
        );

        // Creiamo una lista di lavoratori
        List<Worker> workers = Arrays.asList(
                new Worker(1, 2, 5),
                new Worker(2, 4, 4),
                new Worker(3, 6, 6)
        );

        // Assegniamo i lavori utilizzando l'algoritmo BFS
        System.out.println("Assegnamento dei lavori con l'algoritmo BFS:");
        Map<Integer, Integer> jobAssignments = assignJobsBFS(jobs, workers);
        if (jobAssignments != null) {
            jobAssignments.forEach((jobId, workerId) -> System.out.println("Lavoro " + jobId + " assegnato a worker " + workerId));
        } else {
            System.out.println("Nessun assegnamento dei lavori trovato");

        }
    }

}