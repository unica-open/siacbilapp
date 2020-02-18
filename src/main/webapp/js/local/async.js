/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
(function (window, $) {
    'use strict';
    var Async,
        defaultMaxPollingTimeout = 120000,
        alertErrori = $('#ERRORI');

    /**
     * Controlla se il tipo dell'oggetto &eacute; pari a quello specificato. In caso contrario, rilancia un'eccezione.
     *
     * @param obj   (Object) l'oggetto da controllare
     * @param type  (String) il tipo desiderato
     * @param error (String) la stringa di errore da utilizzare.
     *
     * @throws TypeError nel caso in cui l'oggetto non sia del tipo desiderato
     */
    function checkTypeOf(obj, type, error) {
        if (typeof obj !== type) {
            throw new TypeError(error);
        }
    }

    /**
     * Logga lo stack trace dell'errore.
     *
     * @param err (Error) l'errore da loggare.
     */
    function logError(err) {
        if(err.stack) {
            doLog(err.stack, 'error');
        }
    }

    /**
     * Effettua il poll dei dati.
     *
     * @params self     (Async)  l'oggetto asincrono
     * @params params   (Object) i parametri di poll
     * @params initTime (Number) il tempo di inizio del polling
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione asincrona
     */
    function innerPoll(self, params, initTime) {
        if (!initTime) {
            initTime = new Date().getTime();
        }
        return $.postJSON(self.pollingUrl, params)
        .then(function(data) {
            var event;
            if(data.errori && data.errori.length) {
                impostaDatiNegliAlert(data.errori, alertErrori);
                return $.Deferred().reject(data).promise();
            }

            if (new Date().getTime() - initTime > self.maxPollingTimeout) {
                doLog('Timeout di polling ottenuto');
                // Lancio l'evento nel namespace 'async'
                event = $.Event('pollingTimeout.async');
                $(document).trigger(event);
                return;
            }
            if (self.pollingCallback(data)) {
                // Lancio l'evento nel namespace 'async'
                event = $.Event('pollingDone.async');
                event.pollingData = data;
                event.asyncOptions = self._options;
                $(document).trigger(event);
            } else {
                setTimeout(function () {
                    self.poll(params, initTime);
                }, self.delay);
            }
        });
    }

    /**
     * Esegue la chiamata asincrona, eseguendo al termine il polling dei dati con un dato delay.
     *
     * @params self (Async) l'oggetto asincrono
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione asincrona
     */
    function innerExecute(self) {
        checkTypeOf(self.asyncUrl, 'string', "L'URL dell'invocazione asincrona deve essere specificato");
        checkTypeOf(self.asyncParams, 'object', "I parametri dell'invocazione asincrona devono essere specificati");
        checkTypeOf(self.asyncCallback, 'function', "La funzione di callback dell'invocazione asincrona deve essere specificata");
        checkTypeOf(self.pollingUrl, 'string', "L'URL di polling deve essere specificato");
        checkTypeOf(self.pollingCallback, 'function', "La funzione di callback per il polling deve essere specificata");

        if (self.delay && self.delay < 0) {
            throw new TypeError("Il delay deve essere numerico e positivo");
        }
        
        return $.postJSON(self.asyncUrl, self.asyncParams)
        .then(function(data) {
            var pollingParams;
            if(data.errori && data.errori.length) {
                impostaDatiNegliAlert(data.errori, alertErrori);
                return $.Deferred().reject(data).promise();
            }
            try {
                pollingParams = self.asyncCallback(data);
            } catch (error) {
                return $.Deferred().reject({errori: [error]}).promise();
            }
            self.poll(pollingParams, new Date().getTime());
        });
    }

    /**
     * Il costrutture dell'utility di Async.
     *
     * @param asyncUrl          (String)           l'URL dell'invocazione asincrona
     * @param asyncParams       (Object)           i parametri da utilizzare per l'invocazione asincrona
     * @param asyncCallback     (Function)         la funzione di callback dell'invocazione asincrona. Per definire l'interruzione dell'esecuzione
     *                                                e impedire il polling, si lanci un'eccezione all'interno di tale funzione. Tale funzione
     *                                                restituisce i parametri da utilizzare nell'invocazione del polling.
     *                                                Alla funzione &eacute; passato come unico parametro l'oggetto 'data' ottenuto dal jQuery
     * @param pollingUrl        (String)           l'URL di polling
     * @param pollingCallback   (Function)         la funzione di callback di polling. Per definire l'interruzione dell'esecuzione,
     *                                                si ritorni un risultato <code>r</code> tale che <code>!!r === true</code>.
     *                                                Alla funzione &eacute; passato come unico parametro l'oggetto 'data' ottenuto dal jQuery
     * @param delay             (Number, optional) il ritardo tra le chiamate di polling
     * @param maxPollingTimeout (Number, optional) il timeout di polling
     */
    Async = function (asyncUrl, asyncParams, asyncCallback, pollingUrl, pollingCallback, /* Optional */ delay, /* Optional */ maxPollingTimeout) {
        this.asyncUrl = asyncUrl;
        this.asyncParams = asyncParams;
        this.asyncCallback = asyncCallback;
        this.pollingUrl = pollingUrl;
        this.pollingCallback = pollingCallback;
        this.delay = delay || 4000;
        this.maxPollingTimeout = maxPollingTimeout || defaultMaxPollingTimeout;
        this._options = {
            asyncUrl: asyncUrl,
            asyncParams: asyncParams,
            asyncCallback: asyncCallback,
            pollingUrl: pollingUrl,
            pollingCallback: pollingCallback,
            delay: this.delay,
            maxPollingTimeout: this.maxPollingTimeout
        };
    };

    // Costruttore
    Async.prototype.constructor = Async;

    /**
     * Estrae o imposta una una property all'interno dell'oggetto.
     *
     * @param propertyName  (String) il nome della property da cercare
     * @param propertyValue (any)    il valore della property da settare
     *
     * @throws ReferenceError nel caso in cui la propertyName non sia presente nell'oggetto
     */
    Async.prototype.property = function (propertyName, propertyValue) {
        if (!this.hasOwnProperty(propertyName)) {
            // Se non ho la property, lancio un'eccezione
            throw new ReferenceError('No property with name ' + propertyName + ' is present');
        }
        if (propertyValue === undefined) {
            return this[propertyName];
        }
        this[propertyName] = propertyValue;
        this._options[propertyName] = propertyValue;
    };

    /**
     * Effettua il poll dei dati.
     *
     * @params params   (Object) i parametri di poll
     * @params initTime (Number) il tempo di inizio del polling
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione asincrona
     */
    Async.prototype.poll = function (params, initTime) {
        // Wrappo il tutto in un try-catch per avere eventuali informazioni sull'errore
        try {
            return innerPoll(this, params, initTime);
        } catch (e) {
            // Loggo l'errore ottenuto, e lo rilancio
            logError(e);
            throw e;
        }
    };

    /**
     * Esegue la chiamata asincrona, eseguendo al termine il polling dei dati con un dato delay.
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione asincrona
     */
    Async.prototype.execute = function () {
        // Wrappo il tutto in un try-catch per avere eventuali informazioni sull'errore
        try {
            return innerExecute(this);
        } catch (e) {
            // Loggo l'errore ottenuto, e lo rilancio
            logError(e);
            throw e;
        }
    };

    /**
     * Metodo di utilità per controllare se lo stato sia 'CONCLUSA' oppure 'ERRORE'.
     *
     * @param stato (String) lo stato da controllare
     *
     * @returns (Boolean) true se lo stato è pari a quanto richiesto; false in caso contrario
     */
    Async.checkConclusaOErrore = function (stato) {
        return stato === 'CONCLUSA' || stato === 'ERRORE';
    };

    // Creazione della variabile globale Async per le chiamate asincrone.
    window.Async = Async;
}(window, jQuery));