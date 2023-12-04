/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* ***************************
 ****** Gestione errori ******
 *****************************/
//*
window.siacAppVersion = function() {
    doLog('${product-version}');
};


window.onerror = function(msg, url, line, colNo, err) {
	var stringaErrore = "Error: " + msg + "\nURL: " + url + "\nLine #: " + line;
	if (colNo) {
	  stringaErrore += "\nColumn #: " + colNo;
	}
	bootboxAlert("<pre>" + stringaErrore + "</pre>");
	//alert(stringaErrore);
	doLog(stringaErrore);
	if (err && err.stack) {
	  doLog(err.stack);
	}
	return true;
	};


/**
 * Effettua un log su console del messaggio
 * @param msg il messaggio da loggare
 * @param level il livello del log (Optional, default: log)
 */
function doLog(msg, level) {
    var innerLevel = level || 'log';
    if(window.console && typeof window.console[innerLevel] === 'function') {
        window.console[innerLevel](msg);
    }
}

//*/
//Array di semafori
window.semaphore = {};

// Pre-submit
/**
 * Pre-submit di un form. Nel caso si voglia annullare l'invio, ritornare false o impostare 'defaultPrevented' sull'evento (se presente)
 * @param e (Event) l'evento scatenante
 * @returns (boolean) se effettuare il submit
 */
function preSubmit(e) {
    // Implementazione di default: non faccio nulla, permetto i submit
    return true;
}

/*
 ************************************
 **** Funzioni JavaScript comuni ****
 ************************************
 */

/**
 * Apre l'alert di errore tramite bootbox.
 *
 * @param msg       (String) il messaggio da visualizzare
 * @param title     (String) il titolo dell'alert (Optional - default: 'Attenzione')
 * @param className (String) la classe del modale (Optional - default: 'dialogAlert')
 * @param btns      (Array)  i pulsanti del modale (Optional)
 */
function bootboxAlert(msg, title, className, btns) {
    //var innerMsg = msg || "";
    var innerTitle = title || "Attenzione";
    var innerClassName = className || "dialogAlert";
    var innerButtons = btns || [{
        "label": "Ok",
        "class": "btn-primary",
        "callback": $.noop
    }];
    /*
    // Bootbox v 4.x.x
    bootbox.dialog({
        className: innerClassName
        , title: innerTitle
        , message: innerMsg
        , buttons: {
            success: {
                label: "Ok"
                , className: "btn"
                , callback: $.noop
            }
        }
    });
    //*/
    // Bootbox v 3.x.x
    bootbox.dialog(msg, innerButtons, {
        animate: true,
        classes: innerClassName,
        header: innerTitle,
        backdrop: 'static'
    });
}

/**
 *Gestisce la richiesta di conferma prosecuzione su  una certa azione.
 *
 *@param messaggioRichiestaConferma il messaggio da mostrare all'utente
 *@param callbackConferma (function) la function da eseguire nel caso l'utente 
 *		prema il pulsante "conferma" (se non presente, viene solo chiuso il modale)
 *@param callbackIndietro (function) la function da eseguire nel caso l'utente 
 *		prema il pulsante "No, indietro" (se non presente, viene solo chiuso il modale)
 *@param etichettaConferma (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
 *
 *@param etichettaIndietro(stringa) l'etichetta da mostrare sul pulsante che chiama la function fncIndietro
 *
 * */
function impostaRichiestaConfermaUtente(messaggioRichiestaConferma, callbackConferma, callbackIndietro, etichettaConferma, etichettaIndietro) {
    var fncConferma = callbackConferma && typeof callbackConferma ==="function" ? callbackConferma : $.noop;
    var fncIndietro = callbackIndietro && typeof callbackIndietro ==="function" ? callbackIndietro : $.noop;
	var messaggioDaMostrare = messaggioRichiestaConferma || "";
	var siProsegui = etichettaConferma ? etichettaConferma : 'si, prosegui';
	var noIndietro = etichettaIndietro ? etichettaIndietro : 'No, indietro';
	
	bootboxAlert(messaggioDaMostrare, 'Attenzione', 'dialogWarn', [
        {
            "label" : noIndietro
            , "class" : "btn"
            , "callback": fncIndietro
        }
        , {
            "label" : siProsegui
            , "class" : "btn"
            , "callback" : fncConferma
       }
    ]);
}

/**
 * Svuota tutte le dataTables presenti e visibili nella pagina.
 *
 * @param arrayDaIgnorare (Array) l'array di tabelle da ignorare
 */
function cleanDataTables(arrayDaIgnorare) {
    jQuery.each(jQuery.fn.dataTable.fnTables(), function(key, value) {
        // Controllo se l'elemento non sia nell'array di elementi da ignorare
        if (jQuery.inArray(value, arrayDaIgnorare) === -1) {
            jQuery(value).dataTable().fnClearTable();
            jQuery(value).dataTable().fnDestroy();
        }
    });
}

/**
 * Elabora la stringa passata in input ritornando la stessa con il primo carattere maiuscolo.
 *  
 * @param lowerCaseString la stringa di cui si vuole avere il primo carattere in maiuscolo
 */
function capitalizeFirstChar(lowerCaseString){
	var handable = lowerCaseString && typeof lowerCaseString === 'string' && lowerCaseString.length >0;
	if(!handable){
		//non so che è stato passato, lo ritorno
		return lowerCaseString;
	}
	
	return lowerCaseString.replace(/^\w/, function (chr) {
		  return chr.toUpperCase();
	});
}

/**
 * Elabora la stringa passata in input ritornando la stessa con il primo carattere minuscolo.
 *  
 * @param lowerCaseString la stringa di cui si vuole avere il primo carattere in minuscolo
 */
function lowerFirstChar(upperCaseString){
	var handable = upperCaseString && typeof upperCaseString === 'string' && upperCaseString.length >0;
	if(!handable){
		//non so che è stato passato, lo ritorno
		return upperCaseString;
	}
	
	return upperCaseString.replace(/^\w/, function (chr) {
		  return chr.toLowerCase();
	});
}

function parsePercent(num) {
    return parseLocalNum(num.replace(/%$/g, ""));
}

function formatPercent(num, prec = 2) {
    return formatMoney(num, prec) + '%';
}

function formatDecimal(num, prec = 2) {
    return formatMoney(num, prec);
}

/**
 * Parsing di un numero escludendo il locale.
 * <br>
 * Il presente metodo &egrave; utilizzato ogni qual volta si debba parsificare un numero decimale, in quanto differenti locali possono ripercuotersi in
 * differenti metodi di scrittura del numero. In particolare, la versione corrente:
 * <ul>
 *     <li>elimina la virgola come separatore decimale e la sostituisce con il carattere di punto decimale;</li>
 *     <li>elimina il punto come separatore delle terne di cifre</li>
 * </ul>
 * come da definizione dello standard ECMA-262.
 *
 * @param num (String) stringa rappresentante il numero decimale da parsificare
 *
 * @returns (String) stringa contenente il numero parsificato, tolto il locale
 *
 * @see Standard ECMA-262, {@plainlink http://www.ecma-international.org/publications/standards/Ecma-262.htm}, p.20
 */
function parseLocalNum(num) {
    if(num === undefined || num === null){
        return '';
    }
    if(num === '0') return num;
    // SIAC-8505 per le virgole da /g passiamo a /i per sostituire solo la prima occorrenza
    num = num.replace(/\./g, "").replace(/\,/i, ".");
    // se e' presente almeno un altra virgola questa ed i successivi valori saranno eliminati
    return num.indexOf(',') === -1 ? num : num.slice(0, num.indexOf(','));
}

/**
 * Formattazione della data secondo il locale italiano.
 * <br>
 * In particolare, il locale scelto permette la formattazione della data nel formato dd/MM/yyyy
 *
 * @param data (Date) la data da formattare
 *
 * @returns (String) una stringa corrispondente alla data formattata in maniera corretta, se la data e' valida; una stringa vuota se la data non e' fornita o e' invalida
 */
function formatDate(data) {
    if(!data) {
        return "";
    }
    var date = new Date(data);
    return !isNaN(date.getTime()) && (('0' + date.getDate()).slice(-2) + '/' + ('0' + (date.getMonth() + 1)).slice(-2) + '/' + date.getFullYear()) || "";
}

/**
 * Date parsing in DD/MM/YYYY format.
 * <br>
 * @param d (String) date to parse
 *
 * @returns (Date) the parsed date
 */
function parseDate(d) {
    if(!d || d === '') {
        return null;
    }
    
    var split = d.split('/');

    return new Date(split[2], split[1] - 1, split[0]);
}


/**
 * Popolamento della select.
 *
 * @param select          (jQuery)  la select da popolare
 * @param options         (Array)   l'array delle opzioni
 * @param addEmptyOption  (boolean) inserisce l'elemento vuoto (opzionale, default = false)
 * @param emptyOptionText (string)  inserisce il testo per l'elemento vuoto (opzionale, default = "")
 */
function popolaSelectDaCodifica(select, options, /*opzionale*/ addEmptyOption, /*opzionale*/ emptyOptionText) {
    var opts = "";
    if (!!addEmptyOption) {
        opts += "<option value=''>" + (emptyOptionText || "") + "</option>";
    }
    options.forEach(function(el) {
        opts += "<option value='" + el.uid + "'>" + el.codice + " - " + el.descrizione + "</option>";
    });
    select.html(opts);
}

/**
 * Se c'è una sola opzione nella select, la pre-seleziono
 * 
 * @param $select (JQuery object) la select da pre-selezionare
 * 
 * @return boolean true se la select&egrave stata selezionata
 */
function preSelezionaSeUnicaOpzione($select) {
    var selectPreSelezionata = false;
    var options = $select.find("option");
    if(options.size() === 2){
        options.not(":first").prop("selected", true);
        selectPreSelezionata = true;
    }
    return selectPreSelezionata;
} 

/**
 * Funzione di utilit&agrave; per l'impostazione dei dati all'interno degli alert
 *
 * @param arrayDaInjettare  (Array)   l'array dei dati da injettare nell'alert
 * @param alertDaPopolare   (jQuery)  l'alert da popolare con i valori dell'alert
 * @param redirezione       (Boolean) se effettuare una redirezione sull'hash dell'alert (Optional, default = true)
 * @param chiudereGliAlerts (Boolean) se sia necessario chiudere gli alerts (Optional, default = true)
 *
 * @returns (Boolean) <code>true</code> se i dati sono stati impostati; <code>false</code> altrimenti
 *
 */
function impostaDatiNegliAlert(arrayDaInjettare, alertDaPopolare, /* Optional */ redirezione, /* Optional */chiudereGliAlerts) {
    var result = false;
    var innerRedirezione = redirezione === undefined ? true : redirezione;
    var innerChiusura = chiudereGliAlerts === undefined ? true : chiudereGliAlerts;
    if (arrayDaInjettare && arrayDaInjettare.length > 0 && alertDaPopolare.length > 0) {
        // Chiudo i vari alert aperti
        if (!!innerChiusura) {
            jQuery(".alert").not(".alert-persistent").slideUp();
            alertDaPopolare.find("ul").find("li").remove();
        }

        // Aggiungo gli errori alla lista
        jQuery.each(arrayDaInjettare, function(key, value) {
            // Se ho un oggetto con codice e descrizione, lo spezzo; in caso contrario utilizzo direttamente il valore in entrata
            if (this.codice !== undefined) {
                alertDaPopolare.find("ul").append(jQuery("<li/>").html(this.codice + " - " + this.descrizione));
            } else {
                alertDaPopolare.find("ul").append(jQuery("<li/>").html(value));
            }
        });
        // Mostro l'alert di errore al termine dell'eventuale animazione
        alertDaPopolare.promise().done(function() {
            this.slideDown();
        });
        // Ritorno true per indicare di aver injettato i parametri
        result = true;
        if (innerRedirezione) {
            // Sposto l'attenzione della pagina sull'alert
            alertDaPopolare.scrollToSelf();
        }
    }
    return result;
}

/**
 * Effettua un refresh della tabella dopo aver atteso un dato tempo.
 *
 * @param time  (Number) il tempo da attendere in ms
 * @param alert (jQuery) l'alert da popolare
 * @param table (String) il selettore della tabella
 */
function refreshTable(time, alert, table) {
    var elapsedTime = -1000;
    var ul = alert.find("ul");
    var li = jQuery("<li/>").html("La tabella sar&agrave; aggiornata fra <span></span>");
    var span = li.find("span");

    ul.append(li);

    span.html(stringaFromTime(truncateToSeconds(time)));

    refresh();

    function stringaFromTime(time) {
        return time + ((time === 1) ? " secondo" : " secondi");
    }

    function truncateToSeconds(number) {
        return (number / 1000).toFixedDown(0);
    }

    function refresh() {
        elapsedTime += 1000;
        if (elapsedTime >= time) {
            li.remove();
            jQuery(table).dataTable().fnDraw();
            return;
        }
        span.html(stringaFromTime(truncateToSeconds(time - elapsedTime)));
        setTimeout(refresh, 1000);
    }
}

/**
 * Effettua un submit del form alla pressione del pulsante <code>ENTER</code> su un qualunque campo di tipo <code>input</code>.
 * 
 * @param formSelector (String) il selettore CSS del form
 */
function submitFormOnEnterPress(formSelector) {
    $(formSelector).on("keypress", ":input:not(textarea)", function(e) {
        // Ottengo il codice del pulsante (http://api.jquery.com/event.which/)
        var code = e.which || e.keyCode || e.charCode;
        // 13 === VK_ENTER
        if(code === 13) {
            $(e.delegateTarget).submit();
        }
    });
}

/**
 * Controlla il valore del booleano fornito: se e' pari a 'true' allora restituisce il valore 'trueVal', altrimenti restituisce 'falseVal'.
 *
 * @param bool     (Boolean) il boolean da controllare
 * @param trueVal  (String)  il valore da restituire nel caso in cui il boolean sia 'true'
 * @param falseVal (String)  il valore da restituire nel caso in cui il boolean sia 'false'
 */
function gestioneBoolean(bool, trueVal, falseVal) {
    return !!bool ? trueVal : falseVal;
}

/**
 * Clones properties from source object to destination object.
 *
 * @param src   (Object) the source object
 * @param dest  (Object) the destination object
 * @param props (Multi)  an array of properties to be copied, or a string with a single property to be copied
 */
function cloneProperties(src, dest, props) {
    var i;
    if(typeof props === 'string') {
        // Single property: clones it
        return cloneSingle(src, dest, props, simpleCopy);
    }
    if(props instanceof Array) {
        for(i in props) {
            if(Object.prototype.hasOwnProperty.call(props, i)) {
                cloneSingle(src, dest, props[i], simpleCopy);
            }
        }
        return;
    }
    if(props instanceof Object) {
        for(i in props) {
            if(Object.prototype.hasOwnProperty.call(props, i)) {
                cloneSingle(src, dest, i, returnProperty);
            }
        }
        return;
    }

    // Restituisce la proprieta' da inserire
    function returnProperty(src, val) {
        return src[val] === undefined ? props[val] : src[val];
    }

    // Simply copies the property
    function simpleCopy(src, val) {
        return src[val];
    }

    // The function that actually does the cloning
    function cloneSingle(src, dest, prop, defaultFnc) {
        if(typeof prop !== 'string' || !prop.length) {
            return;
        }
        var arr = prop.split('.');
        var val = arr[0];

        // Basic call
        if(arr.length === 1){
            dest[val] = defaultFnc(src, val);
            return;
        }
        // Do not copy undefined or null values
        if(src[val] === undefined || src[val] === null) {
            return;
        }

        // Determine the eventual support
        var baseObj = Array.isArray(src[val]) ? [] : {};
        // Create the support if necessary
        var newVal = dest[val] !== undefined ? dest[val] : baseObj;
        // Inject the value
        dest[val] = newVal;
        // Recursive call
        return cloneSingle(src[val], dest[val], arr.slice(1).join('.'), defaultFnc);
    }
}

/**
 * Funzionamento di default dei preDrawCallback di dataTable.
 * @param opts le opzioni del dataTable
 */
function defaultPreDraw(opts) {
    $('#' + opts.nTable.id + '_processing').parent('div').show();
}

/**
 * Funzionamento di default dei drawCallback di dataTable.
 * @param opts le opzioni del dataTable
 */
function defaultDrawCallback(opts) {
    $('#' + opts.nTable.id + '_processing').parent('div').hide();
}

/**
 * Funzionamento di default dei drawCallback di dataTable per i risultati di ricerca sintetica
 * @param numField (string) il campo in cui applicare il numero dei record
 * @returns (function(any) => void) la funzione richiamata dal dataTable
 */
function defaultDrawCallbackRisultatiRicerca(numField) {
    return function(opts) {
        var records = opts.fnRecordsTotal();
        var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
        $(numField).html(testo);
        // Nascondo il div del processing
        $('#' + opts.nTable.id + '_processing').parent('div').hide();
    };
}

/**
 * Crea una funzione che imposta la classe sull'elemento
 * @returns (function(Node) => void) la funzione che imposta la classe sul nodo
 */
function doAddClassToElement() {
    var classNames = Array.prototype.slice.call(arguments);
    if(!classNames.length){
        return $.noop;
    }
    classNames = classNames.join(' ');
    return function(elem) {
        $(elem).addClass(classNames);
    };
}

/**
 * Crea un default per l'invocazione di dataTable
 * @param sel          (string) il selettore delle proprieta' nell'oggetto
 * @param defaultValue (any) il valore di default
 * @param operation    (function) la funzione da invocare sul risultato
 * @returns (function) una funzione per l'applicazione di dataTable
 */
function defaultPerDataTable(sel, defaultValue, operation) {
    var split = sel.split('.');
    var length = split.length;
    var defVal = defaultValue !== undefined ? defaultValue : '';
    var oper = operation !== undefined && typeof operation === 'function' ? operation : passThrough;

    return function(source) {
        var res = source;
        var i;
        for(i = 0 ; i < length; i++) {
            if(res[split[i]] === undefined) {
                return oper(defVal);
            }
            res = res[split[i]];
        }
        return oper(res);
    };
}

function deepValue(element, path, defaultValue) {
    var split = path.split('.');
    var length = split.length;
    var defVal = defaultValue !== undefined ? defaultValue : '';
    var res = element;
    var i;
    for(i = 0 ; i < length; i++) {
        if(res[split[i]] === undefined) {
            return defVal;
        }
        res = res[split[i]];
    }
    return res;
}
/**
 * Funzione che restituisce l'argomento invariato
 * @param e (any) l'argomento
 * @returns (any) l'argomento
 */
function passThrough(e) {
    return e;
}

/*
 * ***************************
 * **** Aggiunte a jQuery ****
 * ***************************
 */

// Statiche
/**
 * Crea una funzione per l'invio via POST della chiamata AJAX per recuperare il JSON.
 * <br>
 * Utilizzo:
 * <pre>
 *   jQuery.postJSON(...)
 * </pre>
 *
 * @param url     (String)   l'url cui effettuare la chiamata
 * @param data    (String)   i dati da inviare
 * @param success (Function) la funzione da eseguire in caso di successo
 * @param async   (Boolean)  se la chiamata sia asincrona (Optional, default = true)
 *
 * @returns (Deferred) l'oggetto jQuery associato alla chiamata AJAX
 */
jQuery.postJSON = function(url, data, success, /* Optional */async) {
    var result, innerAsyncTemp = async, innerSuccess = success, innerData = data, innerAsync;
    // Se non ho preimpostato il il parametro data, shifto le variabili di uno
    // indietro
    if (jQuery.isFunction(data)) {
        innerAsyncTemp = async || success;
        innerSuccess = data;
        innerData = undefined;
    }

    innerAsync = (innerAsyncTemp === undefined ? true : innerAsyncTemp);

    try {
        result = jQuery.ajax({
            type : "POST",
            url : url,
            data : innerData,
            success : innerSuccess,
            dataType : "json",
            async : innerAsync
        }).fail(function(jqXHR) {
            var stringaErrore = "Errore nella chiamata al servizio: " + jqXHR.status + " - " + jqXHR.statusText;
            doLog(stringaErrore, 'error');
            if (jqXHR.status !== 0) {
                bootboxAlert(stringaErrore);
            }
        });
    } catch (errore) {
        doLog(errore, 'error');
        result = jQuery.Deferred().resolve();
    }
    return result;
};

// Prototype

/**
 * Serializes to an object.
 *
 * @returns (Object) the serialized object
 */
jQuery.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    if (a.length === 0) {
        // Workaround for jQuery bug #8883
        // see http://bugs.jquery.com/ticket/8883
        a = this.filter(":input").add(this.find(":input")).serializeArray();
    }
    jQuery.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    // Puts also the unchecked checkboxes into the object
    this.find("input[type='checkbox']").each(function() {
        var self = $(this);
        var name = self.attr("name");
        var checked = self.prop("checked");
        if (!o[name]) {
            o[name] = checked;
        }
    });

    return o;
};

/**
 * Substitutes an handler for an event.
 *
 * @see <a href="http://api.jquery.com/on/">jQuery.fn.on</a>
 *
 * @param events   (String)   one or more space-separated event types and optional namespaces, such as "click" or "keydown.myPlugin"
 * @param selector (String)   a selector string to filter the descendants of the selected elements that trigger the event.
 *                            If the selector is <code>null</code> or omitted, the event is always triggered when it reaches the selected element
 * @param data     (Object)   data to be passed to the handler in event.data when an event is triggered
 * @param handler  (Function) a function to execute when the event is triggered. The value <code>false</code> is also allowed as a shorthand for a
 *                            function that simply does <code>return false</code>.
 */
jQuery.fn.substituteHandler = function(events, selector, data, handler) {
    return this.off(events).on(events, selector, data, handler);
};

/**
 * Prevents the default action for an event
 *
 * @param eventName (String)   the name of the event
 * @param selector  (String)   the selector for delegating the event (Optional)
 * @param data      (Object)   rhe data to be passed to the handler (Optional)
 * @param callback  (Function) the eventual callback (Optional)
 */
jQuery.fn.eventPreventDefault = function(eventName, selector, data, callback) {
    var innerSelector = selector;
    var innerData = data;
    var innerCallback = callback;
    // From jQuery
    if (innerData == null && innerCallback == null) {
        innerCallback = selector;
        innerData = innerSelector = undefined;
    } else if (innerCallback == null) {
        if (typeof innerSelector === "string") {
            innerCallback = data;
            innerData = undefined;
        } else {
            // ( types, data, fn )
            innerCallback = data;
            innerData = selector;
            innerSelector = undefined;
        }
    }
    return this.substituteHandler(eventName, innerSelector, innerData, function(e) {
        e.preventDefault();
        if(innerCallback) {
            return innerCallback.apply(this, arguments);
        }
    });
};


/**
 * Forzatura maiuscole
 */
jQuery.fn.forzaMaiuscole = function() {
    return this.on("keypress", onkeypress);
   
    function onkeypress(e) {
    	var code;
    	var currValue = this.value || '';
    	var newValue;
    	var maxlen = e.target.maxLength;
    	var selStart = this.selectionStart !== undefined ? this.selectionStart : currValue.length; 
    	var selEnd = this.selectionEnd !== undefined ? this.selectionEnd : currValue.length;
    	var newChar;
    	
    	if (e.returnValue === false) {
    		return false;
    	}
    	
        code = e.charCode || e.keyCode;
        
        if (code < 32 || e.charCode === 0 || e.ctrlKey || e.altKey) {
            return;
        }
        
        newChar = String.fromCharCode(code).toUpperCase();
        newValue = currValue.substring(0, selStart) + newChar + currValue.substring(selEnd);
        
        if(maxlen === undefined || maxlen === -1 || maxlen >= newValue.length) {
        	this.value = newValue;
        }
        
		if(this.setSelectionRange) {
			this.setSelectionRange(selStart + newChar.length, selStart + newChar.length);
		}
		
		return false;
	}
};


jQuery.fn.forzaVirgolaDecimale = function() {
    return this
		.on("input", function(e) {
			$(this).val($(this).val().replace('.', ','));
		})
    	.on("focus", function(e) {
    		$(this).val($(this).val().replace(/\./g, ''));
    	})
    	;
}; 


/**
 * Gestione dei valori minimi e massimi
 */
jQuery.fn.gestioneMinMax = function() {
    return this.on("blur", onblur);

    /**
     * Gestione del blur.
     */
    function onblur() {
        var self = $(this);
        var value = self.val();
        var min = self.data("min");
        var max = self.data("max");
        if(value !== "" && (value < min || value > max)) {
            self.val("");
            bootboxAlert("Attenzione: il valore del campo (" + value + ") non deve essere inferiore a " + min + " n&eacute; superiore a " + max);
        }
    }
};

/**
 * Gestione dei decimali.
 *
 * @param positiveMultiplier il moltiplicatore da utilizzare per i numeri positivi
 * @param negativeMultiplier il moltiplicatore da utilizzare per i numeri negativi
 *
 * @returns (jQuery) l'oggetto jQuery originale
 */
jQuery.fn.gestioneDeiDecimali = function(positiveMultiplier, negativeMultiplier) {
    return this.on("blur", onblur);
    // this.on("keyup", keyup);

    /**
     * Gestione del blur.
     */
    function onblur() {
        var self = $(this);
        var numeroDecimali = self.data('decimalPlaces') || 2;
        var importo = self.val();
        if(positiveMultiplier !== undefined && importo > 0){
            self.val(importo * positiveMultiplier);
        }
        if(negativeMultiplier !== undefined && importo < 0){
            self.val(importo * negativeMultiplier);
        }
        var numberString = self.val().replace(/\./g, "").replace(/,/g, ".");
        var number = parseFloat(numberString);

        if (!isNaN(number)) {
            self.val(number.formatMoney(numeroDecimali));
        }
    }


    /**
     * Controlla che il codice fornito non sia invalido, ovvero corrispondente a un carattere speciale.
     *
     * @param code (Number) il key/char-code dell'evento di tastiera
     * @param e    (Event)  l'evento scatenante
     *
     * @returns true se il codice non &eacute; valido; false in caso contrario
     */
    function isInvalidCode(code, e) {
        var isCodeNotBackspaceOrDelete = code !== 8 && code !== 46;
        var isCodeSpecialOrArrow = code < 32 || code === 37 || code === 39;
        var isCodeModified = e.ctrlKey || e.altKey;
        return isCodeNotBackspaceOrDelete && (isCodeSpecialOrArrow || isCodeModified);
    }

    /**
     * Controlla che il carattere non sia invalido.
     *
     * @param nextChar (String) il carattere da validare
     * @param delim    (String) il delimitatore
     *
     * @returns true se il carattere &eacute; valido; false in caso contrario
     */
    function isNotInvalidChar(nextChar, delim) {
        return nextChar !== ' ' && nextChar !== '-' && nextChar !== delim;
    }

    /**
     * Gestione del keyup.
     *
     * @param e (Event) l'evento
     */
    function keyup(e) {
        var delim = '.';
        var decimalSeparator = ",";
        var str = this.value;
        var res = [];
        var j = 0;
        var k = 0;
        var decimalPart = "";

        var code = e.charCode || e.keyCode;
        // Inizializzo le altre variabili
        var selectionEnd, originalLength, i, nextChar, result;
        if (isInvalidCode(code, e)) {
            return;
        }

        selectionEnd = this.selectionEnd;
        originalLength = str.length;

        str = str.replace(/\./g, "").replace(/[a-zA-Z]/g, "");

        // Determino la posizione di partenza
        i = str.lastIndexOf(decimalSeparator) - 1;
        if (i >= 0) {
            decimalPart = decimalSeparator + str.split(decimalSeparator)[1].substring(0, 2);
        } else {
            i = str.length - 1;
        }

        while (i >= 0) {
            res[j] = str.charAt(i);
            nextChar = str.charAt(i - 1);
            k++;
            if (i > 0 && k === 3 && isNotInvalidChar(nextChar, delim)) {
                j++;
                res[j] = delim;
                k = 0;
            }
            j++;
            i--;
        }

        // Imposto il risultato
        result = res.reverse().join("") + decimalPart;
        this.value = result;

        // Riposiziono il cursore
        if (result.length > originalLength) {
            selectionEnd++;
        } else if (result.length < originalLength) {
            selectionEnd--;
        }
        this.setSelectionRange(selectionEnd, selectionEnd);
    }

    /**
     * Test per il blocco della scrittura dei decimali.
     *
     * @param e (Event) l'evento scatenante
     *
     * @returns <code>false</code> se devo rifiutare l'evento di scrittura; <code>undefined</code> in caso contrario
     */
    function test(e) {
        var self = jQuery(this);
        var split;
        var text = extractTextFromEvent(e);
        if(!text) {
            return;
        }
        // Giustappongo il testo al termine del contenuto del campo
        text = self.val() + text;
        // Se ho la virgola, vuol dire che ho scritto con notazione italiana. Cancellare e riformattare con notazione internazionale
        if(text.indexOf(",") !== -1) {
            text = text.replace(/\./g, "").replace(/,/g, ".");
        }
        // Splitto la stringa al punto decimale
        split = text.split(".");
        // Se ho termini dopo il punto e questi termini hanno lunghezza maggiore di 2, allora rifiuto l'evento di scrittura
        if(split[1] && split[1].length > 2) {
            e.preventDefault();
            e.returnValue = false;
            return false;
        }
    }

    /**
     * Estrae il testo dall'evento.
     *
     * @param event (Event) l'evento da analizzare
     *
     * @returns (String) il testo
     */
    function extractTextFromEvent(event) {
        var text;
        var code;
        // Controllo il testo
        if (event.type === "textinput" || event.type === "textInput") {
            text = (event.originalEvent) ? event.originalEvent.data : event.data;
        } else {
            code = event.charCode || event.keyCode;
            if (code < 32 || event.charCode === 0 || event.ctrlKey || event.altKey) {
                return;
            }
            text = String.fromCharCode(code);
        }
        return text;
    }
};

/**
 * Salva il valore originale del campo all'interno del data originalValue.
 *
 * @returns (jQuery) l'oggetto jQuery originale
 */
jQuery.fn.keepOriginalValues = function() {
    return this.each(function() {
        var self = jQuery(this);
        self.data("originalValue", self.val());
    });
};

/**
 * Restituisce un filtro sugli elementi jQuery per gli elementi che cambiarono il loro valore da quello originale.
 *
 * @returns (jQuery) l'oggetto jQuery filtrato
 */
jQuery.fn.findChangedValues = function() {
    return this.filter(function() {
        var self = jQuery(this);
        return self.data("originalValue") !== self.val();
    });
};



/**
 * Scrolls the page to the given element.
 *
 * @param time (Number) the time (in ms) in which the transition is to be completed (Optional - default: 800)
 *
 * @returns (jQuery) the original jQuery element
 */
jQuery.fn.scrollToSelf = function(/* Optional */time) {
    var completionTime = (time > 0 && time) || 800;
    var to = this.offset().top;
    jQuery('html, body').animate({
        scrollTop : to
    }, completionTime);
    return this;
};

/**
 * Resets the form
 */
jQuery.fn.reset = function() {
    return jQuery(this).each(function() {
        this.reset();
    });
};



jQuery.fn.readonly = function() {
	this.each(function() {
		var $this = jQuery(this);
		$this.focus(function() { this.blur() });
		var tagName = $this.prop('tagName').toLowerCase();
		
		if (tagName === 'select') {
			$this.css("pointer-events", "none").wrap('<span style="cursor: not-allowed"></span>');
			return;
		}
		
		if (tagName === 'input') {
			if (this.type.toLowerCase() === 'radio') {
				$this
					.css("cursor", "not-allowed")
					.click(function(e){ e.stopPropagation(); return false; });
				return;
			}
		}
	});
};


/**
 * Inverts the selection.
 */
$.fn.invert = function() {
    return this.end().not(this);
};

// Gestione dell'evento speciale

/**
 * Gestione del triplo click.
 *
 * @see http://benalman.com/news/2010/03/jquery-special-events/
 */
(function($) {
    // A collection of elements to which the tripleclick event is bound.
    var elems = $([]),
    // Initialize the clicks counter and last-clicked timestamp.
    clicks = 0, last = 0,
    // Caches the document
    $document = $(document);

    // Click speed threshold, defaults to 500.
    $.tripleclickThreshold = 500;

    // Special event definition.
    $.event.special.tripleclick = {
        setup : function() {
            // Add this element to the internal collection.
            elems = elems.add(this);
            // If this is the first element to which the event has been bound, bind a handler to document to catch all 'click' events.
            if (elems.length === 1) {
                $document.bind('click', clickHandler);
            }
        },
        teardown : function() {
            // Remove this element from the internal collection.
            elems = elems.not(this);

            // If this is the last element removed, remove the document 'click' event handler that "powers" this special event.
            if (elems.length === 0) {
                $document.unbind('click', clickHandler);
            }
        }
    };

    // This function is executed every time an element is clicked.
    function clickHandler(event) {
        var elem = $(event.target);

        // If more than 'threshold' time has passed since the last click, reset the clicks counter.
        if (event.timeStamp - last > $.tripleclickThreshold) {
            clicks = 0;
        }

        // Update the last-clicked timestamp.
        last = event.timeStamp;

        // Increment the clicks counter. If the counter has reached 3, trigger the "tripleclick" event and reset the clicks counter to 0.
        // Trigger bound handlers using triggerHandler so the event doesn't propagate.
        if (++clicks === 3) {
            elem.trigger('tripleclick');
            clicks = 0;
        }
    }
}(jQuery));

// Cache per jQuery
(function(g, $) {
    var cache = {};
    g.$cache = innerCache;
    function innerCache(sel, context, useOriginal) {
        var key;
        if(useOriginal) {
            key = computeKey(sel, context);
            initCache(key, useOriginal, [sel, context]);
        } else if(context) {
            if(typeof context === 'boolean') {
                key = computeKey(sel);
                initCache(key, context, [sel]);
            } else {
                key = computeKey(sel, context);
                initCache(key, false, [sel, context]);
            }
        } else {
            initCache(key, false, [sel]);
        }
        return cache[key];
    }
    function computeKey(sel, context) {
        if(context) {
            return sel + ' § ' + context;
        }
        return sel + ' §';
    }
    function initCache(key, useOriginal, args) {
        if(!cache[key] || useOriginal) {
            cache[key] = $.apply(undefined, args);
        }
    }
}(this, jQuery));

/* *****************************************
 * **** Funzioni JavaScript per Struts2 ****
 * *****************************************/

/**
 * Qualifies the source object for the injection in Struts2.
 *
 * @param source        (Object) the object whose fields are to be qualified
 * @param qualification (String) the qualification to use (Optional - default: "", meaning no qualification is done)
 *
 * @returns (Object) a qualified copy of the source object
 */
function qualify(source, /* Optional */qualification) {
    var key,
        newKey,
        index,
        temp,
        dest = {},
        completeQualification = (qualification ? (qualification + ".") : "");
    // Cycle on object properties
    for (key in source) {
        if (source.hasOwnProperty(key) && source[key] !== undefined) {
            if (Object.prototype.toString.call(source[key]) === "[object Array]") {
                // If the property is an array
                for (index in source[key]) {
                    if (source[key].hasOwnProperty(index)) {
                        newKey = completeQualification + key + "[" + index + "]";
                        dest[newKey] = source[key][index];
                    }
                }
            } else if (Object.prototype.toString.call(source[key]) === "[object Object]") {
                // If the property is an object
                if (source[key].hasOwnProperty("_name")) {
                    // It's a Java Enum. Strip the wrapping object
                    newKey = completeQualification + key;
                    dest[newKey] = source[key]._name;
                } else {
                    temp = qualify(source[key], completeQualification + key);
                    for (index in temp) {
                        if (temp.hasOwnProperty(index)) {
                            dest[index] = temp[index];
                        }
                    }
                }
            } else {
                // The property is neither an array nor an object
                newKey = completeQualification + key;
                dest[newKey] = source[key];
            }
        }
    }
    return dest;
}

/**
 * Unqualifies the source object for the injection in Struts2.
 *
 * @param source (Object) the object whose fields are to be unqualified
 * @param indice (Number) the optional index to which to unqualify (Optional, default = 0)
 *
 * @returns (Object) an unqualified copy of the source object
 */
function unqualify(source, /* Optional */index) {
    // Default value for missing or invalid index
    if (isNaN(index) || index < 0) {
        index = 0;
    }
    var key, newKey, dest = {};
    for (key in source) {
        if (source.hasOwnProperty(key)) {
            newKey = key.split(".").slice(index).join(".");
            dest[newKey] = source[key];
        }
    }
    return dest;
}

/**
 * Projects an object to a string, coherent with what Struts2 awaits
 *
 * @param source (Object) the object to project
 * @param prefix (String) the prefix
 *
 * @returns (String) the string projecting the object
 */
function projectToString(source, prefix) {
	var tempArr = [];
	var innerPrefix = prefix;
    var key;
    var index;
    var temp;

	if(innerPrefix === undefined) {
		innerPrefix = '';
	}
    // Cycle on object properties
    for (key in source) {
        if (source.hasOwnProperty(key) && source[key] !== undefined) {
            if (Object.prototype.toString.call(source[key]) === "[object Array]") {
                // If the property is an array
                for (index in source[key]) {
                    if (source[key].hasOwnProperty(index)) {
                        if(Object.prototype.toString.call(source[key][index]) === "[object Array]" || Object.prototype.toString.call(source[key][index]) === "[object Object]") {
                            temp = projectToString(source[key][index], innerPrefix + key + '[' + index + '].');
                            tempArr.push(temp);
                        } else {
                            tempArr.push(innerPrefix + key + '[' + index + '].' + source[key][index]);
                        }
                    }
                }
            } else if (Object.prototype.toString.call(source[key]) === "[object Object]") {
                // If the property is an object
                if (source[key].hasOwnProperty("_name")) {
                    // It's a Java Enum. Strip the wrapping object
                    temp = source[key]._name;
                    tempArr.push(innerPrefix + key + '=' + temp);
                } else {
                    temp = projectToString(source[key], innerPrefix + key + '.');
                    tempArr.push(temp);
                }
            } else {
                // The property is neither an array nor an object
                tempArr.push(innerPrefix + key + '=' + source[key]);
            }
        }
    }
    return tempArr.join('&');
}


/* ******************************************************************
 * **** Modifica dei prototype degli oggetti JavaScript standard ****
 * ******************************************************************/
/**
 * Formatta la valuta.
 *
 * @param prec (Number) the precision (Optional, default: 2)
 *
 * @returns (String) l'importo formattato
 */
Number.prototype.formatMoney = function(c, d, t) {
    // Numero
    var n = this;
    // Numero di cifre decimali
    var cifre = isNaN(Math.abs(c)) ? 2 : c;
    // Separatore decimale
    var dec = ((d === undefined) ? "," : d);
    // Separatore delle triplette
    var trip = ((t === undefined) ? "." : t);
    // Segno
    var s = n < 0 ? "-" : "";
    // Parte intera del numero
    var i = parseInt(Math.abs(+n || 0).toFixed(cifre), 10) + '';
    var j = i.length > 3 ? (i.length % 3) : 0;
    var k = Math.abs(n) - i;

    return s + (j ? i.substr(0, j) + trip : "")
            + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + trip)
            + (cifre ? dec + Math.abs(k).toFixed(cifre).slice(2) : '');
};

BigNumber.config({
    FORMAT: {
        decimalSeparator: ',',
        groupSeparator: '.',
        groupSize : 3, secondaryGroupSize : 0,
        fractionGroupSeparator : '\xA0',
        fractionGroupSize : 0
    }
});

/**
 * Formattazione degli importi in decimale per la valuta
 * @params val (string | number) il valore da formattare
 * @params prec (string | number) cifre decimali, default 2
 * @returns (string) il valore formattato
 */
function formatMoney(val, prec) {
    var res = '';
    if(prec === undefined) {
       prec = 2;
    }
    if(val === undefined || val === null || isNaN(val) || !isFinite(val) || val === '') {
        return '';
    }
    try {
        res = new BigNumber(val).toFormat(prec, BigNumber.ROUND_HALF_UP);
    } catch(err) {
        // Per sicurezza
        doLog('Error: ' + err);
        if (err && err.stack) {
            doLog(err.stack);
        }
    }
    return res;
}

/**
 * Utility function to improve the native Number.prototype.toFixed(precision).
 *
 * @param value (Number) the number to approximate within precision
 * @param prec  (Number) the precision to use (Optional, default: 2)
 */
function toFixed(value, /* Optional */prec) {
    var sign = value >= 0 ? 1 : -1;
    var precision = prec || 2;
    return (Math.round((value * Math.pow(10, precision)) + (sign * 0.001)) / Math.pow(10, precision)).toFixed(precision);
}

/**
 * Truncates a number to fixed number of digits.
 *
 * @param digits (Number) the number of digits to truncate down to
 *
 * @returns (Number) the truncated number
 */
Number.prototype.toFixedDown = function(digits) {
    var re = new RegExp("(\\d+\\.\\d{" + digits + "})(\\d)"),
        m = this.toString().match(re);
    return m ? parseFloat(m[1]) : this.valueOf();
};

/**
 * Capitalizza la prima lettera di una stringa.
 */
String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1).toLowerCase();
};

if(jQuery.fn.zTree && jQuery.fn.zTree._z && jQuery.fn.zTree._z.view) {
    /**
     * Imposta il tabindex a -1 per gli elementi DOM generati dallo zTree
     */
    jQuery.fn.zTree._z.view.makeDOMNodeMainBefore = function(html, setting, node) {
        html.push("<li id='", node.tId, "' class='level", node.level,"' tabindex='-1' hidefocus='true' treenode>");
    };
}

function caricaSelectCodifiche(select, listaCodifiche, fnc, valueField){
    var oldUid = select.data('oldValue');
	var innerValueField = valueField || 'uid';
    var html;
    listaCodifiche = listaCodifiche || [];
	

    if (select.attr("disabled") === "disabled") {
        select.removeAttr("disabled");
    }

    html = listaCodifiche.reduce(function(acc, val) {
        acc += '<option value="' + val[innerValueField] + '"';
        if(oldUid == val[innerValueField]) {
            acc += ' selected';
        }
        if(fnc) {
            acc += fnc(val);
        }
        acc += '>' + val.codice + '-' + val.descrizione + '</option>';
        return acc;
    }, '<option value=""></option>');
    
    select.html(html);
    return select;
}
 
/**
 * Autoselezione delle combo
 * @param sel                (string|jQuery) le select da utilizzare
 * @param doNotTriggerChange (boolean)       se evitare di effettuare il trigger del change
 * @returns (jQuery) le select
 */
function autoselectCombo(sel, doNotTriggerChange) {
    function filter(idx, el) {
        return el.innerHTML;
    }
    function mapper(idx, el) {
        return el.value;
    }
    function iterator(idx, el) {
        var myself = $(el);
        var opts = myself.find('option')
            .filter(filter)
            .map(mapper)
            .toArray();
        if(opts.length === 1) {
            myself.val(opts[0]);
            if(!doNotTriggerChange) {
                myself.change();
            }
        }
    }
    return jQuery(sel).each(iterator);
}

/**
 * Escapes an HTML string
 * @param unsafe the string to escape
 * @returns the escaped string
 */
function escapeHtml(unsafe) {
    if(unsafe === null || unsafe === undefined) {
        return '';
    }
    if(typeof unsafe !== 'string'){
    	//non so che e' stato passato, lo ritorno
    	return unsafe;
    }
    return unsafe
         .replace(/&/g, '&amp;')
         .replace(/</g, '&lt;')
         .replace(/>/g, '&gt;')
         .replace(/"/g, '&quot;')
         .replace(/'/g, '&#039;');
 }



/* *********************************************
 * **** Caricamento di funzionalita' varie. ****
 * *********************************************/
jQuery(function() {
    var $document = jQuery(document);
    var $body = jQuery(document.body);
    // Previene il reindirizzamento di ogni link avente href="#"
    jQuery('a[href="#"]').click(function(e) {
        e.preventDefault();
    });

    /* **** Bootstrap **** */
    // Tooltip
    jQuery(".tooltip-test").tooltip();
    jQuery('[data-toggle="tooltip"]').tooltip({
        placement : 'top'
    });

    // Popover

    jQuery('[data-toggle="popover"]').popover();
    jQuery('a[data-toggle="popover"]').click(function(e) {
	        e.preventDefault();
	    }
	);

    // Tabs
    jQuery('.nav.nav-tabs').tab()
    // Impedisce ai tab-pane che non sono il primo di essere visualizzati
        .find("li").not(".active").each(function() {
            var idTab = jQuery(this).find("a").attr("href");
            jQuery(idTab).removeClass("active");
        });

    // Collapse
    jQuery('.collapse').collapse({
        // Evito che il collapse venga aperto immediatamente
        toggle : false
    }).on('hidden', function() {
        jQuery(this).find(':input')
                .prop("tabindex", "-1")
                .end()
            .parent()
                .find("a.accordion-toggle")
                    .first()
                        .addClass("collapsed");
    }).on('shown', function() {
        jQuery(this).find(':input')
                .removeProp("tabindex")
                .end()
            .parent()
                .find("a.accordion-toggle")
                    .first()
                        .removeClass("collapsed");
    });

    // Per la gestione dei collapse funzionanti come accordion
    // Cfr. http://stackoverflow.com/questions/13665800/
    $document.on('click', '.accordion-toggle', function(event) {
        // Impedisce la propagazione dell'evento
        var self = jQuery(this);
        var parent = self.data('parent');
        var actives = parent && jQuery(parent).find('.collapse.in');
        var target;
        var href;
        if(self.data('ignoreCollapse') !== undefined) {
        	return;
        }
        event.stopPropagation();
        event.preventDefault();
        if (actives && actives.length) {
            actives.collapse('hide');
        }
        target = self.attr('data-target');
        if (!target) {
            href = self.attr('href');
            // Strip for ie7
            target = href && href.replace(/.*(?=#[^\s]+$)/, '');
        }
        jQuery(target).collapse('toggle');
    });

    // Gestione dei pulsanti di deselezione radio
    $document.on("click", "[data-uncheck]", function(e) {
        var button = e.target;
        var target = $(button).data("uncheck");
        $("input[type='radio'][name='" + target + "']").prop("checked", false).first().change();
    });

    /**
     * Shows the overflow as visible.
     *
     * @param event         {Event}   the event
     * @param elt           {Element} the element over which to act
     * @param selector      {String}  the selector for the element
     * @param childSelector {String}  the selector for the children of the element
     */
    function showOverflow(event, elt, selector, childSelector) {
        event.stopPropagation();
        jQuery(elt).find(childSelector)
                .css("overflow", "visible")
                .end()
            .find(selector)
                .css("overflow", "hidden");
    }

    /**
     * Shows the overflow as hidden.
     *
     * @param event         {Event}   the event
     * @param elt           {Element} the element over which to act
     * @param childSelector {String}  the selector for the children of the element
     */
    function hideOverflow(event, elt, childSelector) {
        event.stopPropagation();
        jQuery(elt).find(childSelector).css("overflow", "hidden");
    }

    // DatePicker
    var dataOdierna;
    var dataAttuale = new Date();
    var annoDatepicker = parseInt(jQuery("#HIDDEN_anno_datepicker").val(), 10);
    if (!isNaN(annoDatepicker)) {
        dataOdierna = new Date(annoDatepicker, dataAttuale.getMonth(), dataAttuale.getDate(), 0, 0, 0, 0);
    }

    jQuery("input.datepicker").each(function() {
        var self = jQuery(this).datepicker({
            weekStart : 1,
            language : "it",
            startDate : "01/01/1901",
            autoclose : true
        }).attr("tabindex", -1);
        var originalDate = self.val();

        if (dataOdierna) {
            self.datepicker("update", dataOdierna).val(originalDate);
        }
    });

    // Overlay
    jQuery("[data-overlay]").overlay();
    $document.on("click", "*[data-element-overlay]", function() {
        var element = $(this).data("elementOverlay");
        jQuery(element).overlay("show");
    });

    // AllowedChars
    jQuery(".soloNumeri").allowedChars({
        numeric : true
    });
    
    jQuery(".numeroNaturale").allowedChars({
        regExp : /[0-9]/
    });

    
    // Chosen (integrare le opzioni)
    jQuery(".chosen-select").chosen({
        allow_single_deselect : true,
        disable_search_threshold : 5,
        no_results_text : 'Nessun risultato'
    });

    // Multi-select
    jQuery(".multiselect").multiSelect();

    
    jQuery(".readonly").readonly();

    /*
     * Gestione dell'hide sull'alert. Per ottenere cio', basta sostituire il
     * 'data-dismiss' con un 'data-hide'. Cfr.
     * http://stackoverflow.com/questions/13550477/
     */
    jQuery("[data-hide]").on("click", function() {
        var self = jQuery(this);
        self.closest("." + self.attr("data-hide")).slideUp();
    });

    /* Formattazione corretta dei campi numerici */
    jQuery(".decimale").gestioneDeiDecimali();
    //jQuery(".decimale-negativo").gestioneDeiDecimaliNegativi();
    jQuery(".decimale-negativo").gestioneDeiDecimali(-1);

    jQuery('input.forzaMaiuscole').forzaMaiuscole();
    
    jQuery('input.forzaVirgolaDecimale').forzaVirgolaDecimale();
    
    // Gestione dei campi con massimo e minimo
    jQuery("[data-min][data-max]").gestioneMinMax();

    jQuery("form").on("click", ".reset", function(e) {
        // Reset del form al click sul pulsante di classe reset
        e.preventDefault();
        jQuery(e.delegateTarget).reset();
    }).on("click", ".submit", function(e) {
        // Submit del form al click sul pulsante di classe submit
        e.preventDefault();
        jQuery(e.delegateTarget).submit();
    }).on("reset", function(e) {
        var campi;
        var $this = jQuery(this);
        // Cancello tutti i dati con il reset. Non ripristino i campi iniziali
        e.preventDefault();
        // Pericoloso?
        $this.find("input[type='checkbox'], input[type='radio']")
            .not("*[data-maintain]")
                .removeAttr("checked");

        campi = $this.find(":input")
            .not("*[data-maintain], input[type='hidden'], input[type='button'], input[type='submit'], input[type='reset'], input[type='radio'], button");
        campi.val("");
        e.campi = campi;
    })
    // Evita la possibilita' di effettuare i doppi submit dai form.
    .submit(function(e) {
        var doSubmit = true;
        try {
            doSubmit = preSubmit(e);
        } catch(e) {
            // Log dell'errore: proseguo comunque
            doLog(e);
        }
        if(doSubmit && !e.isDefaultPrevented()) {
            $body.overlay('show').trigger('showOverlay');
            return true;
        }
        e && e.preventDefault();
        return false;
    });

    $document.on("click", "a[data-original-testo-errore]", function(e) {
        var originalErrore = $(this).data("originalTestoErrore");
        e.preventDefault();
        bootboxAlert(originalErrore);
    });
    
    
    $("#submit-on-enter-key").closest('form').find(':input:not(textarea)').on("keypress	", function(e) {
        var code = e.which || e.keyCode || e.charCode;
        if(code === 13) { // 13 === VK_ENTER
            e.preventDefault();
            $("#submit-on-enter-key").click();
        }
    });
    
    $("form.ignore-enter-key").on("keypress", ":input:not(textarea)", function(e) {
        var code = e.which || e.keyCode || e.charCode;
        if(code === 13) { // 13 === VK_ENTER
            e.preventDefault();
            return false;
        }
    });
    
    $body.overlay({loader: false});
    
	$('.disableOverlay').click(function(){
		$body.data('disableOverlay', true);
	});
	
	$body
		.data('disableOverlay', false)
		.on('showOverlay', function() {
			if ($body.data('disableOverlay')) {
				$body.overlay('hide');
			}
			
			$body.data('disableOverlay', false);
		});

	

});
