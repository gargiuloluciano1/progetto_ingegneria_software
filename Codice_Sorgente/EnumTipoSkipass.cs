using System;

// ENUMERATIVO PER LE OPERAZIONI SUGLI SKIPASS
enum TipoSkipass
{
    // SKIPASS PER GIORNI FESTIVI
    GIORNALIERO_FESTIVO,
    MATTINALE_FESTIVO,
    POMERIDIANO_FESTIVO,
    FESTIVO_2_GIORNI,
    FESTIVO_3_GIORNI,
    FESTIVO_4_GIORNI,
    FESTIVO_5_GIORNI,

    // SKIPASS PER GIORNI FERIALI
    GIORNALIERO_FERIALE,
    MATTINALE_FERIALE,
    POMERIDIANO_FERIALE,
    FERIALE_2_GIORNI,
    FERIALE_3_GIORNI,
    FERIALE_4_GIORNI,
    FERIALE_5_GIORNI,

    // SKIPASS STAGIONALE
    SKIPASS_STAGIONALE

}

/*
 *  NOTE:
 *  - possiamo aggiungere/rimuovere tipi di skipass a nostro piacimento
 *  - gli skipass a più giorni valgono per giorni consecutivi
 *  - per verificare la validità bisogna fare qualcosa con C#.time...
 */
