/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
************************************************
**** Inserisci Capitolo Di Entrata Gestione ****
************************************************
*/

/* Document ready */

$(
    function () {
        var Categoria = $("#categoriaTipologiaTitolo");
        var caricamentoPianoDeiConti = $.Deferred().resolve();

        $("#categoriaCapitolo").change();
        
        // Lego le azioni
        $("#titoloEntrata").on("change", CapitoloEntrata.caricaTipologia);
        $("#tipologiaTitolo").on("change", CapitoloEntrata.caricaCategoria);
        $("#categoriaTipologiaTitolo").on("change", function() {
            Capitolo.caricaPianoDeiConti(this, false);
        });
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);
        if(!$("form[data-disabled-form]").length) {
            $("#ModalAltriDati").on("click", CapitoloEntrata.validaForm);
        }

        /* Carica lo zTree relativo alla Struttura Amministrativo Contabile */
        Capitolo.caricaStrutturaAmministrativoContabile();

        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        if (Categoria.val()) {
            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti(Categoria, false, true);
        }
        /* Carica, eventualmente, lo zTree relativo al SIOPE */
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (!isNaN(elementoPdC) && elementoPdC) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }
        // CR SIAC-2559
        $('#siopeEntrata').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeEntrata', '#descrizioneSiopeEntrata', '#HIDDEN_idSiopeEntrata',
            '#HIDDEN_descrizioneSiopeEntrata', '', 'ricercaClassificatoreGerarchicoByCodice_siopeEntrata.do'));
        RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeEntrata', '#descrizioneSiopeEntrata', '#HIDDEN_idSiopeEntrata','#HIDDEN_descrizioneSiopeEntrata','#HIDDEN_SIOPECodiceTipoClassificatore');

        $("form").on("reset", Capitolo.puliziaReset);
    }
);