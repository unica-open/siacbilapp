/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Gestisce tutte le funzionalit&agrave del div di riepilogo
 */
var Riepilogo = (function($) {
    "use strict";
    var settingTree = {
            check: {enable: false, chkStyle: "radio", radioType: "all"},
            data: { keep: {parent:true},
                key: {name: "testo", children: "sottoElementi"},
                simpleData: {enable: true}},
                callback: {onClick: clickCallback}
        };
    var home =[{ id:1, pId:0, testo:"...", isParent:true, open:true, url:"", target:"_self"}];

    //
    var exports = {};

//    exports.onDocumentLoad = onDocumentLoad;
//    window.xxx = exports;
//
//    function onDocumentLoad() {
//        $.fn.zTree.init($("#treeConsultaEntitaCollegate"), Riepilogo.settingTree,Riepilogo.home);
//        $('#riepilogoConsultazioneEntitaCollegate').on('entitaSelezionata', Riepilogo.aggiungiNodo);
//    }

    var InnerRiepilogo = function() {
        Base.call(this);
    };

    exports.aggiungiNodo = aggiungiNodo;
    exports.settingTree = settingTree;
    exports.home = home;

    InnerRiepilogo.prototype = Object.create(Base.prototype);
    InnerRiepilogo.prototype.constructor = InnerRiepilogo;

    /**
     * carica la lista di entita selezionabili a partire dall'entita selezionata
     * */
    function caricaListaEntitaSelezionabiliFromEntitaSelezionataDaNodo(tipoEntitaSelezionata, uidEntitaSelezionata, tipoEntitaFigliaDaSelezionare){
        var uidPadre = uidEntitaSelezionata || "";
        var objToSend = {};
        //solo nel caso che non sia un'entita di partenza non devo inviare un oggetto vuoto
        if(tipoEntitaSelezionata){
            objToSend = {'tipoEntitaConsultabile': tipoEntitaSelezionata};
        }
        $('#selezioneConsultazioneEntitaCollegate').addClass('loading-data');
        //rimuovo la lista precedente
        $('#entitaSelezionabili').find('li').remove().end();
        return $.postJSON('ottieniFigliEntitaConsultabileAjax.do', objToSend)
        .then(function (data) {
            var listaFigliEntitaSelezionata = (data.listaFigliEntitaConsultabile);
            var errori = data.errori;
            var stringaHtml = '';

            // tolgo momentaneamente la cliccabilita' dei folders
            if(impostaErroriNelBootbox(errori)){
                return;
            }
            listaFigliEntitaSelezionata.forEach(function(value){
                stringaHtml = stringaHtml + '<li data-uid-padre="' + uidPadre + '" data-tipo-entita-padre="' + tipoEntitaSelezionata +  '" data-tipo-entita="' + value._name + '"><a><span class="button ico_close"></span><span>' + value.descrizione + '</span> </a></li>';
            });
            $('#entitaSelezionabili').append(stringaHtml);
            $('#entitaSelezionabili li[data-tipo-entita ="' + tipoEntitaFigliaDaSelezionare + '"]').find('span.button').toggleClass('ico_close ico_open');
            $('#selezioneConsultazioneEntitaCollegate').removeClass('loading-data');
        });
    }


    /**
     * Funzione di callback al click su un nodo dell'albero di riepilogo
     *
     * **/

    function clickCallback(event, treeId, node){

        var albero;
        var urlNodo;
        var tipoEntitaSelezionataPrecedentemente;
        var tipoEntitaFigliaDaSelezionare;
        var uidEntitaSelezionataPrecedentemente;
        var arrayOfRequestParameters;
        var AjaxSource;
        var formSelector;

        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        albero = $.fn.zTree.getZTreeObj('treeConsultaEntitaCollegate');
        urlNodo = node.url;

        //se l'url del nodo e' una stringa vuota, ho cliccato per errore
        if(urlNodo === ''){
            $('#buttonSliding').trigger('click');
            return;
        }

        AjaxSource = creaAjaxSourceByUrlNodo(urlNodo);

        arrayOfRequestParameters = creaArrayOfRequestParametersByUrlNodo(urlNodo);

        tipoEntitaSelezionataPrecedentemente = InnerRiepilogo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro(arrayOfRequestParameters, 'tipoEntitaConsultabilePadre');
        uidEntitaSelezionataPrecedentemente = InnerRiepilogo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro(arrayOfRequestParameters, 'uidEntitaConsultabilePadre');

        if(node.level === 0){
            tipoEntitaFigliaDaSelezionare = node.tipoEntitaSelezionata;
            window.interazioni[tipoEntitaFigliaDaSelezionare].init();
            InnerRiepilogo.prototype.scrollToMePlease();
            formSelector = tipoEntitaFigliaDaSelezionare.indexOf('CAPITOLO')> -1 ? 'capitolo' : tipoEntitaFigliaDaSelezionare.toLowerCase();
            InnerRiepilogo.prototype.disabilitaElemento($('#formSelezionaEntitaDiPartenza_' + formSelector));
        } else {
            tipoEntitaFigliaDaSelezionare = InnerRiepilogo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro(arrayOfRequestParameters, 'tipoEntitaConsultabile');
        }

        //carico e popolo la lista di entita selezionabili del div centrale
        caricaListaEntitaSelezionabiliFromEntitaSelezionataDaNodo(tipoEntitaSelezionataPrecedentemente, uidEntitaSelezionataPrecedentemente, tipoEntitaFigliaDaSelezionare);

        //ricreo e ricarico la tabella
        InnerRiepilogo.prototype.creaECaricaTabellaElencoEntita(AjaxSource, arrayOfRequestParameters,tipoEntitaFigliaDaSelezionare);

        //una volta che ho rilanciato la ricerca rimuovo tutti i nodi successivi a quello su cui ho cliccato
        albero.removeChildNodes(node);
        node.url = "";
    }

    /**
     * Ricava la stringa da utilizzare nella chiamata Ajax della tabella di risultati ricerca
     * **/
    function creaAjaxSourceByUrlNodo(urlNodo){
        return (urlNodo.split('?'))[0];
    }

    /**
     * Crea l'array di oggetti (Datatable aoData) con i parametri di request da utilizzare nella chiamata Ajax della tabella di risultati ricerca
     * @param l'urlNodo la stringa legata al nodo
     * @returs ArrayOfRequestParameters l'array con i parametri della request
     * **/
    function creaArrayOfRequestParametersByUrlNodo(urlNodo){
        var parametriRequest = (urlNodo.split('?'))[1];
        var parametriRequestSplitted = parametriRequest.split('&');
        var arrayOfRequestParameters = [];

        //passo dall'array ["campo1=valore1", "campo2 = valore2"...] all'array desiderato da datatable
        //[{name:"campo1", value="valore1"},{name:"campo2", value="valore2"}...]
        parametriRequestSplitted.forEach(function(value){
            var arrayTemp = value.split('=');
            var temp = {};

            temp.name = arrayTemp[0];
            temp.value = arrayTemp[1];
            arrayOfRequestParameters.push(temp);
        });

        return arrayOfRequestParameters;
    }

    function creaObjOfRequestParametersByUrlNodoFake(arrayToSend){
        var obj = {};
        arrayToSend.forEach(function(element){
            obj['' + arrayToSend[i].name] = arrayToSend[i].value;
        });
        return obj;

    }

    /**
     * Trova l'ultimo nodo dell'albero nel caso in cui ogni elemento abbia un solo
     * sottoelemento
     * @param l'array di nodi dell'albero cos&igrave; come ottenuto da ztree
     * */
    function trovaUltimoNodoDellAlbero(nodi){
        var temp;
        while(nodi[0].sottoElementi){
            temp = nodi[0].sottoElementi;
            nodi = temp;
        }
        return nodi[0];
    }

    /**
     * Alla selezione di una data entita, aggiunge un nodo all'albero di riepilogo
     * @param e l'evento (custom) che ha scatenato l'interazione
     * */

    function aggiungiNodo(e){
        var albero = $.fn.zTree.getZTreeObj('treeConsultaEntitaCollegate');
        //si tratta di un array di obj
        var nodi = albero.getNodes();
        var nodo = trovaUltimoNodoDellAlbero(nodi);
        var newNodo;
        nodo.url = e.urlNodo;

        //Se il nodo a cui sto aggiungendo il mio nodo e' il root (level ===0), sono in presenza di una entita di partenza
        //if(nodo.level === 0){
        nodo.tipoEntitaSelezionata = e.tipoEntitaSelezionata;
        //}

        newNodo = {testo:e.testoNodo, url: "", isParent: e.isParent, open:true};
        albero.addNodes(nodo,newNodo);
        albero.expandAll(true);

    }


    return exports;
}(jQuery));
