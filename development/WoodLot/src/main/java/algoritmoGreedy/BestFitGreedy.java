package algoritmoGreedy;

import comune.Job;
import comune.Worker;

import java.util.*;

//TODO aggiungere il collegamento con il DB
public class BestFitGreedy {

    public static Map<Integer, Integer> assignJobs(List<Job> jobs, List<Worker> workers) {

        long startTime = System.currentTimeMillis();
        long startNanoTime = System.nanoTime();

        // Mescoliamo la lista dei lavoratori per aggiungere aleatorietà all'algoritmo
        Collections.shuffle(workers);

        // Mescoliamo la lista dei lavoratori per aggiungere aleatorietà all'algoritmo
        Collections.shuffle(jobs);

        // Ordiniamo i lavoratori in base alla durata stimata per completare il lavoro
        Collections.sort(workers, (w1, w2) -> w1.getEstimatedDuration() - w2.getEstimatedDuration());

        for (Job job : jobs) {
            int minDuration = Integer.MAX_VALUE;
            Worker bestWorker = null;
            // Troviamo il lavoratore idoneo che può completare il lavoro nel minor tempo possibile
            for (Worker worker : workers) {
                if (job.getEligibleWorkers().contains(worker.getId()) && worker.getPenaltyDays() == 0 && worker.getEstimatedDuration() < minDuration) {
                    minDuration = worker.getEstimatedDuration();
                    bestWorker = worker;
                }
            }
            // Assegniamo il lavoro al lavoratore selezionato
            if (bestWorker != null) {
                bestWorker.setPenaltyDays(job.getDuration());
                System.out.println("Lavoratori ordinati: " + workers);
                System.out.println("Lavoro " + job.getId() + ": Assegnato a worker " + bestWorker.getId());
            } else {
                System.out.println("Lavoro " + job.getId() + ": Nessun lavoratore idoneo disponibile");
            }
        }

        long endTime = System.currentTimeMillis();
        long endNanoTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        long elapsedNanoTime = endNanoTime - startNanoTime;

        System.out.println("Tempo di esecuzione in millisecondi: " + elapsedTime);
        System.out.println("Tempo di esecuzione in nanosecondi: " + elapsedNanoTime);
        return null;
    }


    public static void main(String[] args) {
        List<Worker> workers = new ArrayList<>();
        workers.add(new Worker(1, 0, 5));
        workers.add(new Worker(2, 0, 3));
        workers.add(new Worker(3, 2, 4));
        workers.add(new Worker(4, 0, 4));
        workers.add(new Worker(5, 0, 6));

        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(1, 4, Arrays.asList(1, 2, 4)));
        jobs.add(new Job(2, 5, Arrays.asList(1, 3, 5)));
        jobs.add(new Job(3, 2, Arrays.asList(2, 3, 5)));
        jobs.add(new Job(4, 5, Arrays.asList(3, 4)));
        jobs.add(new Job(5, 1, Arrays.asList(1, 2, 3, 4, 5)));

        assignJobs(jobs, workers);
    }
}
