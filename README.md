# Progetto di Ingegneria Del Software

# deps
java 21
javafx 21

# appunti sul docker 
 Per risparmiare tempo di build ho diviso l'immagine in due, ingsof-image e ingsof-build.
 __ingsof-image__ dovrebbe settare solo l'environment (pacchetti necessari ecc..) 
 __ingsof-build__ copia ./src e ./modules e compila il codice sorgente

 più avanti si potrebbe fare una terza per il solo runtime, dovrebbe far risparmiare spazio

 Per build e run del codice
`sudo ./build.sh`
`sudo ./app`
