package comune;

import java.util.List;

public class Job {
    public Integer id;
    // Durata del lavoro
    private int duration;
    // Lavoratori idonei per svolgere il lavoro -> contadini che possono eseguire il compito
    private List<Integer> eligibleWorkers;

    private List<String> qualifications;

    @Override
    public String toString() {
        return "Job{" +
                "duration=" + duration +
                ", eligibleWorkers=" + eligibleWorkers +
                '}';
    }



    public Job(int id, int duration, List<Integer> eligibleWorkers) {
        this.id = id;
        this.duration = duration;
        this.eligibleWorkers = eligibleWorkers;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Integer> getEligibleWorkers() {
        return eligibleWorkers;
    }

    public void setEligibleWorkers(List<Integer> eligibleWorkers) {
        this.eligibleWorkers = eligibleWorkers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getQualifications() {
        return qualifications;
    }

}