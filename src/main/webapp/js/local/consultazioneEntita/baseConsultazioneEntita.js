/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
window.interazioni = window.interazioni || {};
(function($,global) {
    "use strict";

    var $document = $(document);
    var Base = function() {
    };

    global.impostaErroriNelBootbox = impostaErroriNelBootbox;
    global.endDownload = endDownload;
    global.popolaSelect = popolaSelect;
    global.createOptionAsValueUidStringCodiceDescrizione = createOptionAsValueUidStringCodiceDescrizione;

    Base.prototype.constructor = Base;
    Base.prototype.creaECaricaTabellaElencoEntita = creaECaricaTabellaElencoEntita;
    Base.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro = ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro;
    Base.prototype.scrollToMePlease = scrollToMePlease;
    Base.prototype.disabilitaElemento = disabilitaElemento;
    Base.prototype.riabilitaElemento = riabilitaElemento;


    //$(document).eventPreventDefault("click", "a[href='#']");
/*    *********************************************************************************
 *                          Funzioni utili base
 *  ********************************************************************************/

    /**
     * Popolamento della select
     * @param $select (jQuery)   la select da popolare
     * @param array   (array)    l'array tramite cui popolare la select
     * @param fnc     (function) la funzione tramite cui creare i dati di popolamento
     * @returns (jQuery) la select
     */
    function popolaSelect($select, array, fnc) {
        var str = array.reduce(fnc, '<option></option>');
        return $select.html(str);
    }

    /**
     * Crea una option con valore pari all'uid e contenuto pari a codice e descrizione
     * @param acc (string) l'accumulatore
     * @param el  (object) l'elemento
     * @returns (string) l'accumulatore per la prossima esecuzione
     */
    function createOptionAsValueUidStringCodiceDescrizione(acc, el) {
        return acc + '<option value="' + el.uid + '">' + el.codice + '-' + el.descrizione + '</option>';
    }

    /**
     * Popola e apre (se necessario) un bootbox di errore
     * @param errori l'array degli errori
     * @returns true se ha messo degli errori, false altrimenti
     * */
    function impostaErroriNelBootbox(errori){
        var stringaErrori;

        if(errori.length === 0) {
            return false;
        }

        stringaErrori = errori.map(function(value) {
            if(value.codice !== undefined) {
                return value.codice + ' - ' + value.descrizione;
            }
            return value;
        }).join('<br/>');

        bootboxAlert(stringaErrori);
        return true;
    }
    /**
     * Espande orizzontalmente la tabella dei risultati della ricerca fino a nascondere il div centrale
     * */
    function espandiOrizzontalmenteTabellaRisultatiSeAmmesso(){
        if($('#gestioneRisultatiConsultazioneEntitaCollegate').hasClass('span9')) {
            return;
        }
        $('#selezioneConsultazioneEntitaCollegate').animate({width:'toggle'},350,'linear')
        .promise()
        .then(function() {
            $('#gestioneRisultatiConsultazioneEntitaCollegate').toggleClass('span7 span9');
            $(".button-sliding").find('i.sliding-icon').toggleClass('icon-double-angle-right icon-double-angle-left');
            $('.button-sliding').find('i.sliding-icon').tooltip('hide').attr('data-original-title', 'MOSTRA SELEZIONE' );
        });
    }
    /**
     * Contrae orizzontalmente la tabella dei risultati di ricerca mostrando il div centrale di selezione
     *
     * */
    function contraiOrizzontalmenteTabellaRisultatiSeAmmesso(){
        if(!$("#gestioneRisultatiConsultazioneEntitaCollegate").hasClass('span9')) {
         return;
        }

        $("#gestioneRisultatiConsultazioneEntitaCollegate").toggleClass('span7 span9');
        $(".button-sliding").find('i.sliding-icon').toggleClass('icon-double-angle-right icon-double-angle-left');
        $(".button-sliding").find('i.sliding-icon').tooltip('hide').attr('data-original-title', 'NASCONDI SELEZIONE');

        $('#selezioneConsultazioneEntitaCollegate').animate({width:'toggle'},350,'linear');

    }

    /**
     * Contrae o espande la tabella di gestione dei risultati
     * */
    function impostaEspandiOContraiOrizzontalmenteTabellaRisultati(){
        var wasSpan9;

        if($("#gestioneRisultatiConsultazioneEntitaCollegate").is('.span9')) {
            wasSpan9 = true;
            $("#gestioneRisultatiConsultazioneEntitaCollegate").toggleClass('span7 span9');
            $(".button-sliding").find('i.sliding-icon').tooltip('hide').attr('data-original-title', 'NASCONDI SELEZIONE');
            $(".button-sliding").find('i.sliding-icon').toggleClass('icon-double-angle-right icon-double-angle-left');
        }
        $($(this).data('target')).animate({width:'toggle'},350,'linear')
        .promise()
        .then(function() {
            if(!wasSpan9) {
                $("#gestioneRisultatiConsultazioneEntitaCollegate").toggleClass('span7 span9');
                $(".button-sliding").find('i.sliding-icon').toggleClass('icon-double-angle-right icon-double-angle-left');
                $(".button-sliding").find('i.sliding-icon').tooltip('hide').attr('data-original-title', 'MOSTRA SELEZIONE' );
            }
        });
    }

    function disabilitaElemento($divDaDisabilitare){
        $divDaDisabilitare.addClass('loading-data');
        $divDaDisabilitare.addClass('form-submitted');
        $divDaDisabilitare.find('input.btn.btn-block').attr('disabled', 'disabled');

    }

    function riabilitaElemento($divDaRiabilitare){
        $divDaRiabilitare.removeClass('loading-data');
        $divDaRiabilitare.removeClass('form-submitted');
        $divDaRiabilitare.find('input.btn.btn-block').removeAttr('disabled', 'disabled');

    }


    /**
     * Ottiene il valore di un parametro dall'array dei parametri della request
     *
     * @param arrayOfRequestParameters array di parametri della request come voluto da datatables
     * @param nomeParametro il parametro da cercare
     *
     * @returns il valore del parametro
     *
     * */
    function ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro(arrayOfRequestParameters, nomeParametro){
        return arrayOfRequestParameters.filter(function(value) {
            return value.value && value.name === nomeParametro;
        }).map(function(value) {
            return value.value;
        })[0];
    }

    /**
     * Funzione per la 'messa a fuoco' della tabella di selezione delle entita'
     * */
    function scrollToMePlease(){
        var completionTime = 300;
            //$target il div da mettere a fuoco
        var $target = $('#risultatiConsultazioneEntitaCollegate');
        var container;
        var to;
        var altezza;
        if($target.css('display') ==='none'){
            $target.slideToggle();
        }
        container = $target.closest('.consultazione-entita');
        altezza = container.offset().top - container.scrollTop();
        to = $target.offset().top - altezza;

        container.animate({scrollTop: to}, completionTime);
    }

    function puliziaResetForm() {

            // Pulisco i vari campi
        $("form input[type='radio']").removeAttr("checked");
        $("form input").not("[data-maintain]").val("");
        $("form select").val("");
        $("form textarea").val("");
    }


    $.fn.startLoadData = function() {
        return this.addClass("loading-data").attr("disabled", 'disabled');
    };

    $.fn.endLoadData = function() {
        return this.removeAttr("disabled").removeClass("loading-data");
    };

    /*    *********************************************************************************
     *                  Gestione Base del div risultati di ricerca
     *  ********************************************************************************/

    function esportaRisultati(arrayOfRequestParameters, urlRicercaEntita, isXlsx, e){
        var container, params, url, html, arr;
        e.preventDefault();

        // TODO: Fallback per explorer
//        var formEsporta, inputForDownload;
//        if(window.navigator.appName === 'Microsoft Internet Explorer') {
//            formEsporta = $("#formEsportaRisultatiConsultazioneEntita");
//
//            inputForDownload = arrayOfRequestParameters.reduce(function(acc, el){
//                return acc + '<input type = "hidden" name ="' + el.name + '" value = "' + el.value + '"/>';
//            }, '');
//
//            formEsporta.find('input').remove();
//            formEsporta.append(inputForDownload);
//
//            formEsporta.attr("action", urlRicercaEntita + "_download.do");
//            formEsporta.submit();
//            return;
//        }

        container = $('#iframeContainer');
        arr = arrayOfRequestParameters.map(function(el) {
            return encodeURIComponent(el.name) + '=' + encodeURIComponent(el.value);
        });
        arr.push('isXlsx=' + isXlsx);
        params = arr.join('&');
        url = urlRicercaEntita + '_download.do?' + params;
        html = '<iframe src="' + url + '" onload="endDownload()"></iframe>';

        $("#formEsportaRisultatiConsultazioneEntita").addClass('form-submitted');
        container.html(html);
    }

    /**
     * Callback per la gestione del download.
     */
    function endDownload() {
        var cnt = $('#iframeContainer').find('iframe').contents();
        var html = cnt.find('body').html();
        $("#formEsportaRisultatiConsultazioneEntita").removeClass('form-submitted');
        if(!html) {
        	//Download completato correttamente. Esco.
            return;
        }
        if(/^<pre/.test(html) && /errori/.test(html)){
        	//Errore applicativo.
        	var parsed;
        	try {
        		parsed = JSON.parse(html.replace(/<\/?pre.*?>/g, ''));
        	} catch (syntaxError){
        		return bootboxAlert(html);
        	}
	        parsed.errori = parsed.errori || [];
	        return bootboxAlert('<ul><li>' + parsed.errori.map(function(el) {
	            return el.codice + ' - ' + el.descrizione;
	        }).join('</li><li>') + '</li></ul>');
   		}
        
        //ERROR 500 o 404 o altro.
   		return bootboxAlert(html);
   		
    }

    /**
     * Creazione colonne di datatables a partire da una lista di intestazione dei campi
     *
     * @param arrayIntestazioni l'array con le intestazioni dei campi ottenuto dal servizio
     * @returns the array of objects (ogni oggetto e' una colonna)
     * **/
    function creaColonne(arrayIntestazioni, urlNodo){
        var max = arrayIntestazioni.length;
        var colonneCreate = [];
        var obj;
        var i;
        for(i = 0; i < max; i++){
            obj = generaColonna(i, arrayIntestazioni);
            colonneCreate.push(obj);
        }
        obj = {
            aTargets: [max],
            sTitle: "",
            sClass: "span2",
            mData: function(source) {
                return '<input type="submit" class="btn btn-primary btn-block" value="Seleziona" >';
            }, fnCreatedCell: function(nTd, sData, oData) {
                $(nTd).find("input").substituteHandler("click", caricaListaEntitaSelezionabiliFromEntitaSelezionata.bind(undefined, oData.entitaConsultabile.tipoEntitaConsultabile._name, oData.entitaConsultabile.uid, urlNodo, oData.campiAccessori.testo));
                // in questo modo, la grandezza del pulsante non e' mai tagliata
                $(nTd).css("display", "block");
            }
        };
        colonneCreate.push(obj);
        return colonneCreate;
    }

    function generaColonna(idx, arrayIntestazioni) {
        return {
            aTargets: [idx],
            sTitle:arrayIntestazioni[idx].nome,
            mData: function(source) {
                return source.campiColonne[arrayIntestazioni[idx].name];
            }, fnCreatedCell: function(nTd){
                $("a", nTd).popover();
            }
        };
    }

    /**
     * Crea l'url da associare al nodo (in questa forma):
     * urlRicerca + ? + nomeParametroDellaRequest = valoreParametroDellaRequest&;
     * @param arrayOfRequestParameters l'array di parametri della request richiesto da datatable
     * @param urlRicerca the url da utilizzare per la ricerca delle entita
     *
     * @returns urlNodo l'url da associare al nodo per la gestione del riepilogo
     * */
    function creaUrlNodo(arrayOfRequestParameters, urlRicercaEntita){
        return arrayOfRequestParameters.reduce(function(previousVal, currentVal){
            return previousVal + currentVal.name + '=' + currentVal.value + '&';
         }, urlRicercaEntita + '?');
    }

    /**
     * Crea e gestisce le chiamate ajax per la tabella di risultati
     * @param urlRicercaEntita l'url da utilizzare per la ricerca dell'entita
     * @param arrayOfRequestParameters l'array ottenuto a partire dai parametri di ricerca (cosi' come richiesto da DataTable)
     * @param arrayIntestazioni l'array delle stringhe che rappresentano le intestazioni della tabella
     * */
    function gestioneTabella(urlRicercaEntita, arrayOfRequestParameters, arrayIntestazioni) {
        var urlNodo = creaUrlNodo(arrayOfRequestParameters, urlRicercaEntita);
        var colonne = creaColonne(arrayIntestazioni, urlNodo);
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : urlRicercaEntita + '.do',
            bPaginate: true,
            bLengthChange: false,
            bAutoWidth:false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bDestroy:true,
            bFilter: false,
            sDom: '<r><"row-fluid"<"span3"i><"span9"p>t<"row-fluid"<"span3"i><"span9"p>',
            fnServerParams: function ( aoData ) {
                arrayOfRequestParameters.forEach(function(value){
                    aoData.push(value);
                });
            },
            fnServerData: function ( sSource, aoData, fnCallback, oSettings ) {
                oSettings.jqXHR = $.ajax( {
                    dataType: 'json',
                    type: "POST",
                    url: sSource,
                    data: aoData,
                    beforeSend: function(jqXHR){
                        disabilitaElemento($('#tabellaRisultatiConsultazioneEntitaCollegate'));
                        $("#pulsanteEsportaRisultatiConsultazioneEntita").hide();
                        // SIAC-5643: nascosto l'XLSX
                        $("#pulsanteEsportaRisultatiConsultazioneEntitaXlsx").hide();
                    },
                    success: (function(array, url){
                        return function(data){
                            var evt = $.Event('table-data-loaded');
                            if(data.errori.length > 0){
                                impostaErroriNelBootbox(data.errori);
                            }
                            if (data.iTotalRecords >0){
                                $("#pulsanteEsportaRisultatiConsultazioneEntita")
                                    .substituteHandler("click", esportaRisultati.bind(this, array, url, false))
                                    .show();
                                $("#pulsanteEsportaRisultatiConsultazioneEntitaXlsx")
                                    .substituteHandler("click", esportaRisultati.bind(this, array, url, true))
                                    .show();
                                $("#formEsportaRisultatiConsultazioneEntita").removeClass("form-submitted");
                            }
                            $document.trigger(evt);
                            riabilitaElemento($('#tabellaRisultatiConsultazioneEntitaCollegate'));
                            fnCallback(data);
                        };
                    })(arrayOfRequestParameters,urlRicercaEntita),
                    error: function(jqXHR, textStatus, errorThrown) {
                        var errori = [jqXHR.responseText];
                        riabilitaElemento($('#tabellaRisultatiConsultazioneEntitaCollegate'));
                        impostaErroriNelBootbox(errori);
                     }
                });
            },
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $("#tabellaRisultatiConsultazioneEntitaCollegate_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (oSettings) {
                scrollToMePlease();
                espandiOrizzontalmenteTabellaRisultatiSeAmmesso();
                // Nascondo il div del processing
                $("#tabellaRisultatiConsultazioneEntitaCollegate_processing").parent("div").hide();

                //solo quando ho creato tutto permetto all'utente di cliccare sui pulsanti
                //riabilitaElemento($('#gestioneRisultatiConsultazioneEntitaCollegate'));

                $('#pulsanteRicercaEntitaDiPartenzaCapitolo').removeAttr("disabled");
                $('#pulsanteRicercaEntitaDiPartenzaSoggetto').removeAttr("disabled");
                $('#pulsanteRicercaEntitaDiPartenzaProvvedimento').removeAttr("disabled");
                $("#risultatiConsultazioneEntitaCollegate").find('input').removeAttr('disabled');
                $('#gestioneRisultatiConsultazioneEntitaCollegate').removeClass('loading-data');
            },
            aoColumnDefs: colonne
        };

        var tabella = $("#tabellaRisultatiConsultazioneEntitaCollegate").get(0);
        var test;
        var tabellaCreata;
        var settings;
        if ( $.fn.DataTable.fnIsDataTable( tabella ) ) {
            $(tabella).dataTable().fnDestroy();
        }

        // creo la tabella con le nuove opzioni
        test = $("#tabellaRisultatiConsultazioneEntitaCollegate").dataTable(opts);
        tabellaCreata = $("#tabellaRisultatiConsultazioneEntitaCollegate").dataTable();
        settings = tabellaCreata.fnSettings();
    }

    /***
     * A seconda di quante colonne ci sono, creo la stringa da inserire nel dom
     * @returns ths stringa da inserire
     *
     * */
    function creaStringaTabellaDaInserireNelDom(arrayIntestazioni){
        var stringaTh = '<table id ="tabellaRisultatiConsultazioneEntitaCollegate" class="table table-bordered"><thead>';
        var stringaTd = '</thead><tbody>';

        arrayIntestazioni.forEach(function(value){
            stringaTh = stringaTh + '<th>' + value.name + '</th>';
            stringaTd = stringaTd + '<td></td>';
        });

        return stringaTh +'<th class = "span2"></th>' + stringaTd + '<td class = "span2"></td></tbody></table>';
    }

    /**
     * Carica l'elenco di entita consultabili a partire da una delle entitadiPartenza
     *
     * @param urlRicercaEntita l'url da utilizzare per la ricerca dell'entita
     * @param arrayOfRequestParameters l'array ottenuto a partire dai parametri della request
     * @param tipoEntitaConsultabile il tipo di cui si vuole ottenere l'array di intestazioni
     * */

    function creaECaricaTabellaElencoEntita(urlRicercaEntita, arrayOfRequestParameters, tipoEntitaConsultabile){
        //disabilitaElemento($('#gestioneRisultatiConsultazioneEntitaCollegate'));

        return $.postJSON("ricercaIntestazioniCampiTabellaEntitaConsultabileAjax.do", {'tipoEntitaConsultabile': tipoEntitaConsultabile})
        .then(function (data) {
            var arrayIntestazioni = data.listaIntestazioniCampiTabella;
            var errori = data.errori;
            var tabella;

            if(impostaErroriNelBootbox(errori)){
                return;
            }

            tabella = creaStringaTabellaDaInserireNelDom(arrayIntestazioni);
            $('#perInserireTabellaRisultatiConsultazioneEntitaCollegate').html(tabella);

            gestioneTabella(urlRicercaEntita, arrayOfRequestParameters, arrayIntestazioni);
        });
    }

    /* *********************************************************************************
    *                  Gestione Base del div di selezione entita' successiva
    *  ********************************************************************************/

    /**
    * Carica l'elenco delle entita selezionabili a partire da una data entita;
    * viene richiamata al click sul pulsante 'Seleziona' della tabella dei risultati
    * @param tipoEntitaSelezionata il tipo di entita selezionata ("PROVVEDIMENTO", "IMPEGNO" ..)
    * @param uidEntitaSelezionata l'uid dell'entita selezionata
    *
    * */
    function caricaListaEntitaSelezionabiliFromEntitaSelezionata(tipoEntitaSelezionata, uidEntitaSelezionata, urlNodo, testo){

        var interazione = window.interazioni[tipoEntitaSelezionata];
        var uidEntitaSelezionataNotUndefined = uidEntitaSelezionata || '';

        $("#risultatiConsultazioneEntitaCollegate").find('input').attr('disabled', 'disabled');

        if(interazione){
            interazione.destroy();
        }
        disabilitaElemento($('#selezioneConsultazioneEntitaCollegate'));
        disabilitaElemento($('#riepilogoConsultazioneEntitaCollegate'));
        disabilitaElemento($('#tabellaRisultatiConsultazioneEntitaCollegate'));

        //rimuovo la lista precedente
        $('#entitaSelezionabili').find('li').remove().end();

        //chiamata Ajax per ottenere la lista di entita' successive
        return $.postJSON('ottieniFigliEntitaConsultabileAjax.do', {'tipoEntitaConsultabile' : tipoEntitaSelezionata})
        .then(function (data) {
            var listaFigliEntitaSelezionata = data.listaFigliEntitaConsultabile;
            var errori = data.errori;
            var stringaHtml = '';
            var customEvt = $.Event('entitaSelezionata');

            if(impostaErroriNelBootbox(errori)){
                return;
            }

            //rimuovo la lista precedente
            $('#entitaSelezionabili').find('li').remove().end();
            //chiudo tutti i div relativi alla selezione precedente
            $('#gestioneRisultatiConsultazioneEntitaCollegate .hide').slideUp();

            listaFigliEntitaSelezionata.forEach(function(value){
                stringaHtml = stringaHtml + '<li data-uid-padre="' + uidEntitaSelezionataNotUndefined + '" data-tipo-entita-padre="' + tipoEntitaSelezionata + '" data-tipo-entita="' + value._name + '"><a><span class="button ico_close"></span><span>' + value.descrizione + '</span> </a></li>';
            });

            contraiOrizzontalmenteTabellaRisultatiSeAmmesso();

            $('#entitaSelezionabili').append(stringaHtml);

            customEvt.tipoEntitaSelezionata = tipoEntitaSelezionata;
            customEvt.isParent = listaFigliEntitaSelezionata.length > 0;
            customEvt.testoNodo = testo;
            customEvt.urlNodo = urlNodo;

            $('#riepilogoConsultazioneEntitaCollegate').trigger(customEvt);

            riabilitaElemento($('#selezioneConsultazioneEntitaCollegate'));
            riabilitaElemento($('#riepilogoConsultazioneEntitaCollegate'));
            riabilitaElemento($('#tabellaRisultatiConsultazioneEntitaCollegate'));

        });
    }

    global.Base = Base;
    $(document).on("click", "[data-toggle='slidewidth']", impostaEspandiOContraiOrizzontalmenteTabellaRisultati);
})(jQuery, this);