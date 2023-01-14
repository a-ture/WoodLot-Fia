package comune;

import java.util.List;

public class Worker {
    // ID del lavoratore
    private int id;
    // Numero di giorni di penalità rimanenti
    private int penaltyDays;
    // Durata stimata per completare il lavoro
    private int estimatedDuration;
    private List<String> qualifications;

    public Worker(int id, List<String> qualifications) {
        this.id = id;
        this.qualifications = qualifications;
    }

    public Worker(int id, int penaltyDays, int estimatedDuration) {
        this.id = id;
        this.penaltyDays = penaltyDays;
        this.estimatedDuration = estimatedDuration;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", penaltyDays=" + penaltyDays +
                ", estimatedDuration=" + estimatedDuration +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPenaltyDays() {
        return penaltyDays;
    }

    public void setPenaltyDays(int penaltyDays) {
        this.penaltyDays = penaltyDays;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public boolean canComplete(Job job) {
        // Verifichiamo se il lavoratore possiede tutte le qualifiche richieste dal lavoro
        for (String qualification : job.getQualifications()) {
            if (!qualifications.contains(qualification)) {
                return false;
            }
        }
        return true;
    }

}