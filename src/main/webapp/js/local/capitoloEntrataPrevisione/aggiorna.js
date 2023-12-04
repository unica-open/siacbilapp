/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*************************************************
**** Aggiorna Capitolo Di Entrata Previsione ****
*************************************************
*/

;(function($, w, c, cap) {
    "use strict";

    /**
     * Ricarica le select gerarchiche
     */
    function ricaricamentoSelectGerarchiche(e) {
        e.target.reset();

        c.ricaricaSelectGerarchica("tipologiaTitolo", "titoloEntrata", "caricaTipologia", "titoloEntrataCaricato", "tipologiaTitoloCaricato", cap);
        c.ricaricaSelectGerarchica("categoriaTipologiaTitolo", "tipologiaTitolo", "caricaCategoria", "tipologiaTitoloCaricato", "categoriaTipologiaTitoloCaricato", cap);
        c.ricaricaTreeElementoPianoDeiConti("categoriaTipologiaTitolo");
        c.ricaricaTreeSiope();
        c.ricaricaTreeStrutturaAmministrativoContabile();

        // Inizio del triggering degli eventi
        c.ricaricaSelectBase("titoloEntrata", "titoloEntrataCaricato");
    }

    $(function () {
        var categoria = $("#categoriaTipologiaTitolo");
        var editabilitaStrutturaAmministrativaContabile = $("#HIDDEN_StrutturaAmministrativoContabileEditabile").val();
        var editabilitaSiope = $("#HIDDEN_SIOPEEditabile").val();

        // Lego le azioni
        $("#titoloEntrata").on("change", CapitoloEntrata.caricaTipologia);
        $("#tipologiaTitolo").on("change", CapitoloEntrata.caricaCategoria);
        categoria.on("change", function() {
            Capitolo.caricaPianoDeiConti(this, false);
        });
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        $("#pulsanteCalcoloImportoCassa").on("click", Capitolo.calcolaImportoCassa);
        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);

        /* Carica lo zTree relativo alla Struttura Amministrativo Contabile */
        if(editabilitaStrutturaAmministrativaContabile === "true") {
            Capitolo.caricaStrutturaAmministrativoContabile();
        }

        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        // CR SIAC-2559
//        var editabilitaElementoPianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiEditabile").val();
//        var caricamentoPianoDeiConti = $.Deferred().resolve();
//        if (editabilitaElementoPianoDeiConti === "true" && categoria.val()) {
//            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti(categoria, false, true);
//        }

        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (editabilitaSiope === "true" && !isNaN(elementoPdC)) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }
        // CR SIAC-2559
        if (editabilitaSiope === "true") {
            $('#siopeEntrata').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeEntrata', '#descrizioneSiopeEntrata', '#HIDDEN_idSiopeEntrata',
                '#HIDDEN_descrizioneSiopeEntrata', '', 'ricercaClassificatoreGerarchicoByCodice_siopeEntrata.do'));
            RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeEntrata', '#descrizioneSiopeEntrata', '#HIDDEN_idSiopeEntrata','#HIDDEN_descrizioneSiopeEntrata','#HIDDEN_SIOPECodiceTipoClassificatore');
        }

        $("form").substituteHandler("reset", ricaricamentoSelectGerarchiche);
        
        //task-244
        var codiceCategoriaCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
	    if(codiceCategoriaCapitolo != null){
			if(codiceCategoriaCapitolo.match(/FPV.*/) || codiceCategoriaCapitolo === "AAM"){
				$("#flagImpegnabile").attr("disabled", "disabled");
			}
	    }


    });
}(jQuery, window, Capitolo, CapitoloEntrata));