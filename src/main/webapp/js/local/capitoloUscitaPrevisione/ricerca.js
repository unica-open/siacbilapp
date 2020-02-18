/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
***********************************************
**** Ricerca Capitolo Di Uscita Previsione ****
***********************************************
*/

/* Document ready */

$(
    function () {
        var Macroaggregato = $("#macroaggregato");
        var caricamentoPianoDeiConti = $.Deferred().resolve();

        CapitoloUscita.caricaListaClassificatoriGerarchici("Previsione", true);
        CapitoloUscita.caricaListaClassificatoriGenerici("Previsione");
        
        // Lego le azioni
        $("#missione").on("change", CapitoloUscita.caricaProgramma.bind(null, true));
        $("#programma").on("change", CapitoloUscita.caricaCofogTitolo.bind(null, true));
        $("#classificazioneCofog").on("change", CapitoloUscita.caricaCodiceCofog);
        $("#titoloSpesa").on("change", CapitoloUscita.caricaMacroaggregato);
        $("#macroaggregato").on("change", function() {
            Capitolo.caricaPianoDeiConti(this, true);
        });
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);

        /* Carica lo zTree relativo alla Struttura Amministrativo Contabile */
        Capitolo.caricaStrutturaAmministrativoContabile();

        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        if (Macroaggregato.val()) {
            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti(Macroaggregato, true, true);
        }
        /* Carica, eventualmente, lo zTree relativo al SIOPE */
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (elementoPdC && !isNaN(elementoPdC)) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }

        // CR SIAC-2559
        $('#siopeSpesa').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa',
            '#HIDDEN_descrizioneSiopeSpesa', '#HIDDEN_SIOPECodiceTipoClassificatore', 'ricercaClassificatoreGerarchicoByCodice_siopeSpesa.do'));
        RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa','#HIDDEN_descrizioneSiopeSpesa','#HIDDEN_SIOPECodiceTipoClassificatore');

        $("form").on("reset", Capitolo.puliziaReset);
        submitFormOnEnterPress("form");
    }
);