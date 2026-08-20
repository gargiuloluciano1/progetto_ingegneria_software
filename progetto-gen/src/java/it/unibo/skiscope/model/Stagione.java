package it.unibo.skiscope.model;

import java.time.LocalDate;

/**
 * RFG2, RFG5: intervallo di date entro cui è possibile svolgere tutte le
 * operazioni del comprensorio; al di fuori è consentita solo la
 * registrazione dei clienti.
 *
 * La stagione è considerata attiva se e solo se la data odierna cade
 * effettivamente tra la data di inizio e quella di fine (estremi inclusi):
 * non esiste alcun modo di forzare questo stato dall'esterno.
 */
public class Stagione {

    private LocalDate dataInizio;
    private LocalDate dataFine;

    public Stagione(LocalDate dataInizio, LocalDate dataFine) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDate(LocalDate dataInizio, LocalDate dataFine) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    /**
     * @return true se le date sono state impostate e la data odierna cade
     *         nell'intervallo [dataInizio, dataFine], estremi inclusi.
     */
    public boolean isAttiva() {
        if (dataInizio == null || dataFine == null) {
            return false;
        }
        LocalDate oggi = LocalDate.now();
        return !oggi.isBefore(dataInizio) && !oggi.isAfter(dataFine);
    }
}
