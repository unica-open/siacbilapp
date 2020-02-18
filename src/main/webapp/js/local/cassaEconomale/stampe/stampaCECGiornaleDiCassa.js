/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var modaleConferma1 = $("#modaleConfermaStampaGiornaleCassa1");
    var modaleConferma2 = $("#modaleConfermaStampaGiornaleCassa2");
    var tipoStampa=$("#tipoStampa").val();
    var dataUltimaStampaDef=$("#dataUltimaStampa").val();
    var validatoSenzaErrori=true;

    /**
     * verifica se ci sono degli errori
     */
    function validazioneCampi() {

        dataUltimaStampaDef=$("#dataUltimaStampa").val();
        tipoStampa=$("#tipoStampa").val();
        var dataDaElaborare=$("#dataDaElaborare").val();
        $.postJSON("cassaEconomaleStampe_stampaGiornaleDiCassaValidazione.do",

                {dataDaElaborare:dataDaElaborare,tipoStampa:tipoStampa,dataUltimaStampaDef:dataUltimaStampaDef},

                function(data) {
                     var res = impostaDatiNegliAlert(data.errori, alertErrori);
                     if (res) {
                         modaleConferma1.modal("hide");
                         validatoSenzaErrori=false;

                         return;
                     }
                     else{
                         validatoSenzaErrori=true;
                         return;

                     }
                },
             // Imposto la chiamata come sincrona per essere certo che il dato della validazione sia popolato prima di proseguire con l'esecuzione
             false);
    }
    /*
     * mostro la modale di conferma stampa
     */
    function mostraModaleConfermaStampa1() {
        modaleConferma1.modal("show");

     }
    /*
     * mostro la modale di conferma stampa
     */
    function mostraModaleConfermaStampa2() {
         validazioneCampi();


        // se la data di ultima stampa =null --->il giornale di cassa non e' mai stato stampato non mostro la modale
        // l'analisi non specifica che cosa fare in questo caso ---ahmad--->>>non mostro la modale e stampo subito
        if(!validatoSenzaErrori) {
            return;
        }

        dataUltimaStampaDef=$("#dataUltimaStampa").val();
        tipoStampa=$("#tipoStampa").val();
        dataUltimaStampaDef=formattaData(dataUltimaStampaDef);
        var dataDaElaborare=$("#dataDaElaborare").val();
        dataDaElaborare=formattaData(dataDaElaborare);
        var timeDiff = Math.abs(dataDaElaborare - dataUltimaStampaDef);
        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

        if(!!dataUltimaStampaDef && tipoStampa==="DEFINITIVA" && diffDays>1) {
            modaleConferma1.modal("hide");

            modaleConferma2.modal("show");
        } else {
            confermaStampaGiornaleDiCassa();
        }

    }
    /**
     * formatta la data togliendo il time se c'e  (xx/xx/xxxx)
     * @param stringa di formato gg/mm/yyy
     * @returns
     */
    function formattaData(dataDaFormattare){
        // es:13/03/2015
        var res = dataDaFormattare.split("/");
        var giorno = parseInt(res[0]);
        // -1 perche quando faccio new Date --->il mese e' da 0 a 11
        var mese = parseInt(res[1])-1;
        var anno = parseInt(res[2]);

        dataDaFormattare = new Date(dataDaFormattare);
        // This will write a Date with time set to 00:00:00 so you kind of have date only
        return new Date(anno, mese, giorno);
    }
    /**
     * tasto prosegui ---->stampo la ricevuta
     */
    function confermaStampaGiornaleDiCassa() {
        var form = $("#formStampaGiornaleDicassa");

        form.submit();
        modaleConferma1.modal('hide');
        modaleConferma2.modal('hide');
    }


    function caricaUltimaStampaDefinitiva() {
        var uidCassaEconomale = parseInt($("#cassaEconomale").val(), 10);

        if(!uidCassaEconomale) {
            return;
        }
        $("#dataUltimaStampa").overlay("show");
        $.postJSON("cassaEconomaleStampe_caricaUltimaDefinitivaGiornaleDiCassa.do",
                {uidCassaEconomale : uidCassaEconomale},
                function(data) {
                    if(impostaDatiNegliAlert(data.errori, alertErrori)){
                        return;
                    }
                    var dataUltimaStampa= formatDate(data.stampaGiornale.dataUltimaStampa);
                    $("#dataUltimaStampa").val(dataUltimaStampa);
                    $("#dataUltimaStampa").overlay("hide");
                    //imposto il messaggiodi conferma x le definitive
                    $("#msg2").html(dataUltimaStampa);
                });
    }

    $(function() {
        caricaUltimaStampaDefinitiva();
        $("#cassaEconomale").substituteHandler("change", caricaUltimaStampaDefinitiva);
        $("#pulsanteConfermaStampaGiornaleDiCassa1").substituteHandler("click", mostraModaleConfermaStampa2);
        $("#pulsanteConfermaStampaGiornaleDiCassa2").substituteHandler("click", confermaStampaGiornaleDiCassa);
        $("#pulsanteStampa").substituteHandler("click", mostraModaleConfermaStampa1);
    });

}(jQuery);