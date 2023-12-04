/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function() {
	"use strict";
    var alertErrori = $('#ERRORI');
    var suffix="Progetto";
    
    var baseOptsDataTable = {           
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sZeroRecords: "Nessun impegno associato al cronoprogramma",
                sProcessing: "Attendere prego...",
                oPaginate : {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }};

    /**
     * Imposta i totali degli stanziamenti.
     *
     * @param tipoCapitolo (String) il tipo di capitolo
     * @param data         (Object) i dati con gli stanziamenti
     */
    function impostaTotali(tipoCapitolo, data) {
        var i;
        // Imposto i totali
        for (i = 0; i < 3; i++) {
            $('#totaleStanziamento' + tipoCapitolo + i).html(data['totaleStanziamento' + tipoCapitolo + i].formatMoney());
        }
    }
    
    function defaultAnno(anno) {
        return +anno === -1 ? 'Senza anno' : anno;
    }

    /**
     * Redirige alla action di aggiornamento del cronoprogramma.
     *
     * @param crono (Number) il cronoprogramma
     */
    function aggiornaCronoprogramma(crono) {
        // Redirigo alla action corrispondente
        document.location.href = 'aggiornaCronoprogramma.do?uidCronoprogramma=' + crono.uid;
    }

    /**
     * Annulla il cronoprogramma
     *
     * @param event (Event)  l'evento invocante la funzione
     * @param uid   (Number) l'uid del cronoprogramma
     */
    function annullaCronoprogramma() {
        var uid = $('#HIDDEN_UidDaAnnullare').val();

        $.postJSON('aggiornaProgettoAnnullaCronoprogramma.do', {uidCronoprogrammaDaAnnullare : uid}, function(data) {
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                $('#msgAnnulla').modal('hide');
                return;
            }
            impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
            $('#msgAnnulla').modal('hide');

            location.reload();
        });

    }

    /**
     * Associa il cronoprogramma ad fpv
     *
     * @param event (Event)  l'evento invocante la funzione
     * @param uid   (Number) l'uid del cronoprogramma
     */
    function associaCronoprogrammaFPV(url){
        var uid = $('#HIDDEN_UidDaAssociare').val();
        if(!url){
        	return;
        }

        $.postJSON(url, {uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV : uid}, function(data) {
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                $('#msgAssociaCronoprogrammaFPV').modal('hide');
                return;
            }
            impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
            $('#msgAssociaCronoprogrammaFPV').modal('hide');

            // Refresho la tabella
            tabellaCronoprogrammiAssociati('cronoprogrammiNelProgetto', data);
        });

    }
 

	 /**
     * Disassocia il cronoprogramma ad fpv (SIAC-8870)
     *
     * @param event (Event)  l'evento invocante la funzione
     * @param uid   (Number) l'uid del cronoprogramma
     */
    function disassociaCronoprogrammaFPV(url){
        var uid = $('#HIDDEN_UidDaDisassociare').val();
        if(!url){
        	return;
        }

        $.postJSON(url, {uidCronoprogrammaDaDisassociareComeUsatoPerCalcoloFPV : uid}, function(data) {
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                $('#msgDisassociaCronoprogrammaFPV').modal('hide');
                return;
            }
            impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
            $('#msgDisassociaCronoprogrammaFPV').modal('hide');

            // Refresho la tabella
            tabellaCronoprogrammiAssociati('cronoprogrammiNelProgetto', data);
        });

    }
    /**
     * Mostra la modale di annullamento del cronoprogramma con i 2 bottoni
     * @param crono (Object) il cronoprogramma
     */
    function showModaleAnnullaCronoprogramma(crono) {
        $('#HIDDEN_UidDaAnnullare').val(crono.uid);
        $('#msgAnnulla').modal('show');
    }

    /**
     * Mostra il modale di associazione cronoprogramma FPV.
     * 
     * @param crono (Object) il cronoprogramma
     */
    function showModaleAssociaCronoprogrammaFPV(crono, msgAzione, urlSalva) {
        // Per ora mostro solo la versione del cronoprogramma volendo si puo aggiungere anche la descrizione dipende dall'analisi
        // In questo caso non dice niente ho inserito solo la versione
        var spanCronoDett = ' ' + crono.versione + ' ';
        var innerMsgAzione = msgAzione || '';
        $('#HIDDEN_UidDaAssociare').val(crono.uid);
        $('#msgAssociaCronoprogrammaFPVspan').text(msgAzione + spanCronoDett);
      
        $('#pulsanteAssociaCronoprogrammaFPV').eventPreventDefault('click',associaCronoprogrammaFPV.bind(undefined, urlSalva));
       
        $('#msgAssociaCronoprogrammaFPV').modal('show');
    }

    /**
     * Mostra il modale di disassociazione cronoprogramma FPV (SIAC-8870).
     * 
     * @param crono (Object) il cronoprogramma
     */
    function showModaleDisassociaCronoprogrammaFPV(crono, msgAzione, urlSalva) {
        // Per ora mostro solo la versione del cronoprogramma volendo si puo aggiungere anche la descrizione dipende dall'analisi
        // In questo caso non dice niente ho inserito solo la versione
        var spanCronoDett = ' ' + crono.versione + ' ';
        var innerMsgAzione = msgAzione || '';
        $('#HIDDEN_UidDaDisassociare').val(crono.uid);
        $('#msgDisassociaCronoprogrammaFPVspan').text(msgAzione + spanCronoDett);
      
        $('#pulsanteDisassociaCronoprogrammaFPV').eventPreventDefault('click',disassociaCronoprogrammaFPV.bind(undefined, urlSalva));
       
        $('#msgDisassociaCronoprogrammaFPV').modal('show');
    }
    
    
    /**
     * Chiamate ajax per il caricamento degli impegni in base ai parametri di ricerca
     */
    function impostaImpegniNellaTabella(listaImpegni) {
        var optionsImpegni = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaImpegniAjax.do",
            sServerMethod: "POST",
            iDisplayLength: 5,
//            oLanguage: {            
//                sZeroRecords: "Nessun impegno associato al cronoprogramma",
//            },
            fnDrawCallback:defaultDrawCallback,
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    if(source){
                        return source.annoMovimento + "/"+ source.numero;
                    }
                }},/*, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalImpegnoFromImpegni", oData);
                }},*/
                {aTargets: [1], mData: function(source){
                    if(source.capitoloUscitaGestione && source.capitoloUscitaGestione.uid !== 0){
                    	var cap = source.capitoloUscitaGestione;
                        return  cap.annoCapitolo + "/" + cap.numeroCapitolo + "/" + cap.numeroArticolo;
                    }
                }},
                {aTargets: [2], mData: function(source){
                	var provv = source.attoAmministrativo;
                	var res = '';
                	if(!provv){
                		return '';
                	}
                	res += provv.anno + "/"+ provv.numero;
                    res = provv.tipoAtto ? res + "/" + provv.tipoAtto.codice : res;
                    return res;
                }},
                {aTargets: [3],  mData: function(source){
                	var soggetto = source.soggetto;
                	var classeSoggetto = source.classeSoggetto;
                	if(!soggetto && !classeSoggetto){
                		return '';
                	}
                	if(soggetto){
                		return soggetto.codiceSoggetto + ' - '  + soggetto.denominazione;
                	}
                	return classeSoggetto.codice  + ' - ' + classeSoggetto.descrizione; 
                }},
                {aTargets: [4], mData: defaultPerDataTable('importoAttuale', 0, formatMoney), fnCreatedCell: doAddClassToElement('text-right')}
            ]
        };
        var options = $.extend({}, baseOptsDataTable, optionsImpegni);
        $('#tabellaImpegniCollegatiAlCronoprogramma').dataTable(options);
    }
    
    /**
     * Mostra il modale di associazione cronoprogramma FPV.
     * 
     * @param crono (Object) il cronoprogramma
     */
    function showImpegniCollegati(crono, nTd) {
    	var row = $(nTd).closest('tr').overlay('show'); 
    	var $modal = $('#modaleImpegniCollegatiCronoprogramma');
        // Per ora mostro solo la versione del cronoprogramma volendo si puo aggiungere anche la descrizione dipende dall'analisi
        var spanCronoDett = crono? crono.versione : "";
        
        var obj = {}
        obj.mostraErroreNessunDatoTrovato = false;
        obj.caricaDisponibilitaLiquidare = false;
        
        obj.cronoprogramma = {};
        obj.cronoprogramma.uid = crono.uid;
        
        return $.postJSON( "ricercaSinteticaImpegniSubImpegni.do", qualify(obj))
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaImpegniNellaTabella(data);
            $modal.modal('show');
        }).always(row.overlay.bind(row,'hide'));
        
    }

    /**
     * Permette di consultare il cronoprogramma.
     *
     * @param crono (Object) il cronoprogramma
     * @param td    (Node)   il td su cui si agisce
     */
    function consultaCronoprogramma(crono, td) {
        var tr = $(td).closest('tr').overlay('show');
        var promise;
        
        // Maschera cambiata se il cronoprogramma da gestione
        if (crono.uid === -1) {
            promise = consultaCronoprogrammaGestione(crono);
        } else {
            promise = consultaCronoprogrammaNonGestione(crono);
        }
        promise.always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Permette di consultare il cronoprogramma di gestione ahmad.
     *
     * @param crono (Object) il cronoprogramma
     * @return (Promise) la promise legata all'invocazione della funzionalita'
     */
    function consultaCronoprogrammaGestione(crono) {
        return $.postJSON('aggiornaProgettoConsultaCronoprogrammaGestione.do', {uidCronoprogrammaDaConsultare : crono.uid}, function(data) {
            var tabella = $('#consultazioneCronoprogrammaDaGestioneTabella tbody');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            $('#consultazioneCronoprogrammaDaGestioneCodice').html(crono.versione + ' - ' + crono.descrizione);
            
            $.each(data.listaProspettoRiassuntivoCronoprogramma, function(index, el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(el.anno) + '</td>';
                str += '<td class="tab_Right">' + el.totaliSpese.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.totaliEntrate.formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleConsultaCronoprogrammaDaGestione').modal('show');
        });
    }
    
    /**
     * Permette di consultare il cronoprogramma non di gestione.
     *
     * @param crono (Object) il cronoprogramma
     * @return (Promise) la promise legata all'invocazione della funzionalita'
     */
    function consultaCronoprogrammaNonGestione(crono) {
        return $.postJSON('aggiornaProgettoConsultaCronoprogramma.do', {uidCronoprogrammaDaConsultare : crono.uid}, function(data) {
            var tabella = $('#consultazioneCronoprogrammaTabella tbody');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            $('#consultazioneCronoprogrammaCodice').html(crono.versione + ' - ' + crono.descrizione);

            // Carico i totali in tabella
            $.each(data.mappaTotaliCronoprogramma, function(index, el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(index) + '</td>';
                str += '<td class="tab_Right">' + el[0].formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el[1].formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleConsultaCronoprogramma').modal('show');
        });
    }

    /**
     * Carica la quadratura nel modale corrispondente.
     *
     * @param entrata    (Number) il valore di entrata
     * @param uscita     (Number) il valore di uscita
     * @param differenza (Number) il valore di differenza
     */
    function caricaQuadratura(entrata, uscita, differenza) {
        $('#quadraturaEntrata').html(entrata.formatMoney());
        $('#quadraturaUscita').html(uscita.formatMoney());
        $('#quadraturaDifferenza').html(differenza.formatMoney());

        $('#modaleVerificaQuadratura').modal('show');
    }
    
    function doFormatMoney(el) {
        if(typeof el === 'number') {
            return el.formatMoney();
        }
        return el;
    }
    
    function addClass(className) {
        return function(el) {
            $(el).addClass(className);
        };
    }
    
    function booleanToString(value) {
        return !value ? 'NO' : 'S&Igrave;';
    }

    /**
     * Caricamento via Ajax della tabella dei programmi
     *
     * @param idTabella (String) l'id della tabella
     * @param data     (Array)  i dati con la lista da impostare
     */
    function tabellaCronoprogrammiAssociati(idTabella, data) {
        var tabella = $('#' + idTabella);
        var lista = data.listaElementiVersioneCronoprogramma || [];
        var options = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: false,
            bFilter: false,
            bProcessing: true,
            aaData: lista,
            bDestroy: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti capitoli associati',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: 'prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnDrawCallback: function() {
                $('a[rel="popover"]', tabella).popover().on('click', function(e) {
                    e.preventDefault();
                });
                $('.dropdown-toggle', tabella).dropdown();
            },            
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: function(source, type) {
                    return !source.quadraturaCorretta ? '<a href="#" data-content="Attenzione clicca per verificare la quadratura" data-title="Avviso" data-trigger="hover" rel="popover"><span class="icon-warning-sign icon-red alRight"></span></a>' : '';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('a', nTd).substituteHandler('click', function(e) {
                        // Chiudo il popover
                        $(this).popover('hide');
                        caricaQuadratura(oData.quadraturaEntrata, oData.quadraturaUscita, oData.quadraturaDifferenza);
                    });
                }},
                {aTargets: [1], mData: defaultPerDataTable('versioneConStatoCronoProgramma')},
                {aTargets: [2], mData: defaultPerDataTable('descrizioneStatoOperativoCronoprogramma')},
                {aTargets: [3], mData: function(source){
                	return source.collegatoAProgettoGestione?
                			'<a class="tooltip-test impegniCollegati" href=\"#\" data-original-title=\"Impegni collegati\"><i class=\"icon-info-sign\">&nbsp;<span class=\"nascosto\">Impegni collegati</span></i></a>'
                			: '<abbr title="cronoprogramma non associabile ad impegni">N.D.</abbr>';
                },fnCreatedCell: function(nTd, sData, oData){
                	$('a.impegniCollegati', nTd).eventPreventDefault('click', showImpegniCollegati.bind(undefined, oData, nTd));
                }},
                {aTargets: [4], mData: defaultPerDataTable('htmlFPV')},
                {aTargets: [5], mData: defaultPerDataTable('competenzaEntrataAnnoPrec', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [6], mData: defaultPerDataTable('competenzaEntrataAnno0', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [7], mData: defaultPerDataTable('competenzaEntrataAnno1', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [8], mData: defaultPerDataTable('competenzaEntrataAnno2', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [9], mData: defaultPerDataTable('competenzaEntrataAnnoSucc', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [10], mData: defaultPerDataTable('competenzaSpesaAnnoPrec', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [11], mData: defaultPerDataTable('competenzaSpesaAnno0', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [12], mData: defaultPerDataTable('competenzaSpesaAnno1', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [13], mData: defaultPerDataTable('competenzaSpesaAnno2', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [14], mData: defaultPerDataTable('competenzaSpesaAnnoSucc', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
                {aTargets: [15], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    $('.pulsanteAggiornaCronoprogramma', nTd).eventPreventDefault('click', aggiornaCronoprogramma.bind(undefined, oData));
                    $('.pulsanteAnnullaCronoprogramma', nTd).eventPreventDefault('click', showModaleAnnullaCronoprogramma.bind(undefined, oData));
                    $('.pulsanteConsultaCronoprogramma', nTd).eventPreventDefault('click', consultaCronoprogramma.bind(undefined, oData, nTd));
                    $('.pulsanteAssociaCronoprogrammaFPV', nTd).eventPreventDefault('click', showModaleAssociaCronoprogrammaFPV.bind(undefined, oData, 'Stai per collegare il cronoprogramma ', 'aggiornaProgettoAssociaCronoprogrammaPerFPV.do'));
					$('.pulsanteDisassociaCronoprogrammaFPV', nTd).eventPreventDefault('click', showModaleDisassociaCronoprogrammaFPV.bind(undefined, oData, 'Stai per disassociare il cronoprogramma ', 'aggiornaProgettoDisassociaCronoprogrammaPerFPV.do'));
                    $('.pulsanteSimulaCronoprogrammaFPV', nTd).eventPreventDefault('click', showModaleAssociaCronoprogrammaFPV.bind(undefined, oData, 'Stai per simulare il collegamento del cronoprogramma ', 'aggiornaProgetto_simulaFPV.do'));
                    $('.pulsanteAnnullaSimulaCronoprogrammaFPV', nTd).eventPreventDefault('click', showModaleAssociaCronoprogrammaFPV.bind(undefined, oData, 'Stai annullare il collegamento provvisorio del cronoprogramma ', 'aggiornaProgetto_annullaSimulaFPV.do'));
                }}
            ]
        };

        var startPos = parseInt($('#HIDDEN_startPosition').val(), 10);
        if (!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }

        tabella.dataTable(options);
    }

    /**
     * Redirige allaa creazione di un nuovo cronoprogramma.
     */
    function redirezioneNuovoCronoprogramma() {
        $('form').attr('action', 'aggiornaProgettoInserimentoCronoprogramma.do').submit();
    }

    /**
     * Popola il fondo pluriennale vincolato.
     */
    function popolaFPV() {
        var $versioneCrono = $('#versCrono');
        var visualizzaPulsanti = $('#visualizzaPulsantiFPV');
        var visualizzaPulsantiGestioneBilancio = $('#visualizzaPulsantiFPVGestioneBilancio');
        var uid = parseInt($versioneCrono.val());

        visualizzaPulsanti.slideUp();
        visualizzaPulsantiGestioneBilancio.slideUp();

        if (isNaN(uid)) {
            return;
        }
        $versioneCrono.overlay('show');
        //verifico se l'utente ha scelto il crononoprogramma da gestione da bilancio
        if (uid === -1) {
            visualizzaPulsantiGestioneBilancio.slideDown();
            $('#versCrono').overlay('hide');
            return;
        }
        
        $.postJSON('aggiornaProgettoPopolaFondoPluriennaleVincolato.do', {uidCronoprogrammaPerProspettoFPV : uid}, function(data) {
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            visualizzaPulsanti.slideDown();
            $('#fpve').val(data.fondoPluriennaleVincolatoEntrata.formatMoney());
        }).always($versioneCrono.overlay.bind($versioneCrono, 'hide'));
    }
    
    function formatMoney(el) {
    	return typeof el === 'number' ? el.formatMoney() : '';
    }

    /**
     * Calcola il prospetto dell'FPV complessivo per il cronoprogramma selezionato.
     */
    function calcolaFPVComplessivo() {
        var pulsante = $('#pulsanteVisualizzaProspettoFPVComplessivo').overlay('show');
        alertErrori.slideUp();

        $.postJSON('aggiornaProgettoCalcolaFondoPluriennaleVincolatoComplessivo.do', function(data) {
            var tabella = $('tbody', '#tabellaFPVComplessivo');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Svuoto la tabella
            tabella.empty();
            // Carico i totali in tabella
            $.each(data.mappaFondoPluriennaleVincolatoPerAnno, function(index, el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(index) + '</td>';
                str += '<td class="tab_Right">' + el[0].importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el[0].importoFPV.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el[1].importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el[1].importoFPV.formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleFPVComplessivo').modal('show');
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }

    /**
     * Calcola il prospetto dell'FPV complessivo per il cronoprogramma da Gestione Da Bilancio selezionato.
     */
    function calcolaFPVComplessivoCronoprogrammaDaGestioneDaBilancio() {
        var pulsante = $('#pulsanteVisualizzaProspettoFPVComplessivoGestioneBilancio').overlay('show');
        alertErrori.slideUp();

        $.postJSON('aggiornaProgettoCalcolaFondoPluriennaleVincolatoComplessivoCronoprogrammaDaGestioneDaBilancio.do', function(data) {
            var tabella = $('tbody', '#tabellaFPVComplessivoDaGestioneDaBilancio');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            // Carico i totali in tabella
            $.each(data.listaFondoPluriennaleVincolatoTotale, function(el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(el.anno) + '</td>';
                str += '<td class="tab_Right">' + el.fpvEntrata.importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvEntrata.importoFPV.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvUscita.importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvUscita.importoFPV.formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleFPVComplessivoDaGestioneDaBilancio').modal('show');
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }


    /**
     * Calcola il prospetto dell'FPV di uscita per il cronoprogramma da gestione da bilancio selezionato.
     */
    function calcolaFPVUscitaCronoprogrammaDaGestioneDaBilancio() {
        var pulsante = $('#pulsanteVisualizzaProspettoFPVUscitaGestioneBilancio').overlay('show');
        alertErrori.slideUp();

        $.postJSON('aggiornaProgettoCalcolaFondoPluriennaleVincolatoSpesaCronoprogrammaDaGestioneDaBilancio.do', function(data) {
            var tabella = $('tbody', '#tabellaFPVUscitaDaGestioneDaBilancio');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            // Carico i totali in tabella
            $.each(data.listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio, function(idx, el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(el.anno) + '</td>';
                str += '<td class="tab_Right">' + el.missione.codice + '</td>';
                str += '<td class="tab_Right">' + el.programma.codice + '</td>';
                str += '<td class="tab_Right">' + el.titoloSpesa.codice + '</td>';
                str += '<td class="tab_Right">' + el.importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.importoFPV.formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleFPVUscitaDaGestioneDaBilancio').modal('show');
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }
    /**
     * Calcola il prospetto dell'FPV di entrata per il cronoprogramma Da Bilancio Da Gestione selezionato.
     */
    function calcolaFPVCronoprogrammaEntrataDaGestioneDaBilancio() {
        var pulsante = $('#pulsanteVisualizzaProspettoFPVEntrataGestioneBilancio').overlay('show');
        alertErrori.slideUp();

        $.postJSON('aggiornaProgettoCalcolaFondoPluriennaleVincolatoEntrataCronoprogrammaDaGestioneDaBilancio.do', function(data) {
            var tabella = $('tbody', '#tabellaFPVEntrataDaGestioneDaBilancio');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            // Carico i totali in tabella
            $.each(data.listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate, function(idx, el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(el.anno) + '</td>';
                str += '<td class="tab_Right">' + el.importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvEntrataSpesaCorrente.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvEntrataSpesaContoCapitale.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.totale.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.importoFPV.formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleFPVEntrataDaGestioneDaBilancio').modal('show');
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }

    /**
     * Calcola il prospetto dell'FPV di uscita per il cronoprogramma selezionato.
     */
    function calcolaFPVUscita() {
        var pulsante = $('#pulsanteVisualizzaProspettoFPVUscita').overlay('show');
        alertErrori.slideUp();

        $.postJSON('aggiornaProgettoCalcolaFondoPluriennaleVincolatoUscita.do', function(data) {
            var tabella = $('tbody', '#tabellaFPVUscita');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            // Carico i totali in tabella
            data.listaFondoPluriennaleVincolatoUscitaCronoprogramma.forEach(function(el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(el.anno) + '</td>';
                str += '<td class="tab_Right">' + getValueWithDefault(el, 'missione.codice') + '</td>';
                str += '<td class="tab_Right">' + getValueWithDefault(el, 'programma.codice') + '</td>';
                str += '<td class="tab_Right">' + getValueWithDefault(el, 'titoloSpesa.codice') + '</td>';
                str += '<td class="tab_Right">' + getValueWithDefault(el, 'importo', 0, formatMoney) + '</td>';
                str += '<td class="tab_Right">' + getValueWithDefault(el, 'importoFPV', '', formatMoney) + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleFPVUscita').modal('show');
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }


    /**
     * Calcola il prospetto dell'FPV di entrata per il cronoprogramma selezionato.
     */
    function calcolaFPVEntrata() {
        var pulsante = $('#pulsanteVisualizzaProspettoFPVEntrata').overlay('show');
        alertErrori.slideUp();

        $.postJSON('aggiornaProgettoCalcolaFondoPluriennaleVincolatoEntrata.do', function(data) {
            var tabella = $('tbody', '#tabellaFPVEntrata');
            var str = '';
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            $.each(data.listaFondoPluriennaleVincolatoEntrata, function(idx, el) {
                str += '<tr>';
                str += '<td class="colored">' + defaultAnno(el.anno) + '</td>';
                str += '<td class="tab_Right">' + el.importo.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvEntrataSpesaCorrente.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.fpvEntrataSpesaContoCapitale.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.totale.formatMoney() + '</td>';
                str += '<td class="tab_Right">' + el.importoFPV.formatMoney() + '</td>';
                str += '</tr>';
            });
            tabella.html(str);
            // Apro il modale
            $('#modaleFPVEntrata').modal('show');
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }
    
    function getValueWithDefault(source, sel, defaultValue, operation) {
        var split = sel.split('.');
        var length = split.length;
        var defVal = defaultValue !== undefined ? defaultValue : '';
        var oper = operation !== undefined && typeof operation === 'function' ? operation : passThrough;
        var res = source;
        var i;
        for(i = 0 ; i < length; i++) {
            if(res[split[i]] === undefined || res[split[i]] === null) {
                return oper(defVal);
            }
            res = res[split[i]];
        }
        return oper(res);
    }
    
    
    function caricaStrutture(){
        // Pulsante per il modale della Struttura Amministrativo Contabile
        var spinner = $('#SPINNER_StrutturaAmministrativoContabile' + suffix);

        // Non permettere di accedere al modale finche' il caricamento non e' avvenuto
        $('#bottoneSAC' + suffix).removeAttr('href');
        // Attiva lo spinner
        spinner.addClass('activated');

        $.postJSON('ajax/strutturaAmministrativoContabileAjax.do',{})
        .then(caricaStruttureCallback)
        .always(spinner.removeClass.bind(spinner, 'activated'));

    }

    function caricaStruttureCallback(e) {
    	var listaStrutturaAmministrativoContabile = (e && e.lista) || [];
    	var ztree = new Ztree('StrutturaAmministrativoContabile',suffix, "Nessun servizio selezionato");
        ztree.inizializza(listaStrutturaAmministrativoContabile);
        //ZTree.imposta('treeStruttAmm' + this.suffix, ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
        // Ripristina l'apertura del modale
        $('#accordionPadreStrutturaAmministrativoContabileProgetto').attr('href', '#strutturaAmministrativoContabile' + suffix);
    }
    
    function annulla(){
    	 var $form = $('form');   	 
    	 var uid = $('#aggiornamentoProgetto_progetto_uid').val(); 
    	 $form.attr('action', 'aggiornaProgetto.do?uidDaAggiornare='+ uid);
    	 $form.submit();
    }

    function setFieldsToReadonly() {
    	if ($("#disabilitaAggiornaCampi").data('disabilita')) {
    	   	$('.canBeReadonly').prop('readonly', true);
    	   	$(".canBeReadonly > option:not(:selected)").remove();
    	   	$(".accordion-body.canBeReadonly").remove();
    	}
    }

    
    $(function() {
        $('#versCrono').eventPreventDefault('change', popolaFPV).change();
//
        // Imposto le funzionalità dei pulsanti
        $('#pulsanteVisualizzaProspettoFPVComplessivo').eventPreventDefault('click', calcolaFPVComplessivo);
        $('#pulsanteVisualizzaProspettoFPVEntrata').eventPreventDefault('click', calcolaFPVEntrata);
        $('#pulsanteVisualizzaProspettoFPVUscita').eventPreventDefault('click', calcolaFPVUscita);

        // imposto le funzionalita dei pulsanti per il cronoprogramma da bilancio da gestione
        $('#pulsanteVisualizzaProspettoFPVComplessivoGestioneBilancio').eventPreventDefault('click', calcolaFPVComplessivoCronoprogrammaDaGestioneDaBilancio);
        $('#pulsanteVisualizzaProspettoFPVEntrataGestioneBilancio').eventPreventDefault('click', calcolaFPVCronoprogrammaEntrataDaGestioneDaBilancio);
        $('#pulsanteVisualizzaProspettoFPVUscitaGestioneBilancio').eventPreventDefault('click', calcolaFPVUscitaCronoprogrammaDaGestioneDaBilancio);

        $('#pulsanteApriCalcoloDelta').eventPreventDefault('click');
        $('#pulsanteInserimentoCronoprogramma').eventPreventDefault('click', redirezioneNuovoCronoprogramma);
        $('#pulsanteProseguiAnnullamentoCronoprogramma').eventPreventDefault('click', annullaCronoprogramma);
        
        $('#pulsanteAnnulla').substituteHandler('click', annulla);

        // Carico i dati per le tabelle dal servizio
        $.postJSON('aggiornaProgettoLeggiCronoprogrammi.do', {}, tabellaCronoprogrammiAssociati.bind(undefined, 'cronoprogrammiNelProgetto'));
//
//        //SIAC-4091
        var provvedimento = new Provvedimento(undefined, 'provvedimento', 'provvedimento: ');
    	provvedimento.inizializza();
//        
    	 $(document).on("struttureAmministrativeCaricate", caricaStruttureCallback);
//    	
        $('form').on('reset', function() {
            // NATIVO
            this.reset();
        });

        
        // SIAC-8703
        
        setFieldsToReadonly();
        
    });
}());
