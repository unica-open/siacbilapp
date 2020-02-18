/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
************************************************
**** Ricerca Capitolo Di Entrata Previsione ****
************************************************
*/

/* Document ready */

$(
    function () {
        var Categoria = $("#categoriaTipologiaTitolo");
        var caricamentoPianoDeiConti = $.Deferred().resolve();

        CapitoloEntrata.caricaListaClassificatoriGerarchici("Previsione");
        CapitoloEntrata.caricaListaClassificatoriGenerici("Previsione");
        
        // Lego le azioni
        $("#titoloEntrata").on("change", CapitoloEntrata.caricaTipologia);
        $("#tipologiaTitolo").on("change", CapitoloEntrata.caricaCategoria);
        $("#categoriaTipologiaTitolo").on("change", function() {
            Capitolo.caricaPianoDeiConti(this, true);
        });
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);

        /* Carica lo zTree relativo alla Struttura Amministrativo Contabile */
        Capitolo.caricaStrutturaAmministrativoContabile();

        /* Carica, eventualmente, lo zTree relativo al piano dei conti */
        if (Categoria.val()) {
            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti(Categoria, true, true);
        }
        /* Carica, eventualmente, lo zTree relativo al SIOPE */
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (!isNaN(elementoPdC) && elementoPdC) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }

        // CR SIAC-2559
        $('#siopeEntrata').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeEntrata', '#descrizioneSiopeEntrata', '#HIDDEN_idSiopeEntrata',
            '#HIDDEN_descrizioneSiopeEntrata', '#HIDDEN_SIOPECodiceTipoClassificatore', 'ricercaClassificatoreGerarchicoByCodice_siopeEntrata.do'));
        RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeEntrata', '#descrizioneSiopeEntrata', '#HIDDEN_idSiopeEntrata','#HIDDEN_descrizioneSiopeEntrata', '#HIDDEN_SIOPECodiceTipoClassificatore');

        $("form").on("reset", Capitolo.puliziaReset);
        submitFormOnEnterPress("form");
    }
);