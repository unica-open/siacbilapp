/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    $(init);

    /**
     * Effettua la stampa
     */
    function stampa() {
        $('form').submit();
    }
    /**
     * Inizializzazione delle SAC
     * @return (Promise) una promise legata al caricamento delle SAC
     */
    function inizializzaSAC() {
        return $.postJSON('ajax/strutturaAmministrativoContabileAjax.do')
        .then(inizializzaSACCallback);
    }
    /**
     * Caricamento delle SAC come callback del servizio
     * @param data (any) la response del servizio
     */
    function inizializzaSACCallback(data) {
        var ztree = new Ztree('', 'treeStruttAmm');
        ztree.inizializza(data.listaElementoCodifica);
    }

    /**
     * Inizializzazione
     */
    function init() {
        var idSoggetto;
        // Compilazioni guidate
        $('#pulsanteConfermaStampaRendicontoCassa').substituteHandler('click', stampa);

        // Soggetto
        idSoggetto = Soggetto.inizializza('#codiceSoggettoSoggetto', '#HIDDEN_codiceFiscaleSoggetto', '#HIDDEN_denominazioneSoggetto', '#datiRiferimentoSoggettoSpan',
                '#pulsanteCompilazioneSoggetto', '', '', '', '#accordionModalitaPagamentoSoggetto');
        Soggetto.bindCaricaDettaglioSoggetto(idSoggetto, true);
        // SAC
        inizializzaSAC();
    }

}(jQuery);