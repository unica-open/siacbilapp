/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagtipodoc;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.TipoDocumentoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.anagtipodoc.RicercaTipoDocumentoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di Action per la ricerca del RicercaTipoDocumento
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 09/09/2020
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaTipoDocumentoAction  extends TipoDocumentoAction<RicercaTipoDocumentoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	
	TipoComponenteImportiCapitolo cc = new TipoComponenteImportiCapitolo();
	
	
	@Autowired
	private transient TipoDocumentoFELService tipoDocumentoServiceFEL;
	
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListe();

		
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	
	/**
	 * Ricerca con operazioni il TipoDocumentoFEL 
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazionericercaTipoDocumento
	 */
	public String ricercaTipoDocumento() {
		final String methodName = "ricercaComponenteCapitolo";

		// Popolo le codifiche
		popolaCodificheDaSessione();
		
		log.debug(methodName, "Creazione della request");
		RicercaSinteticaTipoDocumentoFEL req = model.creaRequestTipoDocumentoFEL();
		logServiceRequest(req);
		RicercaSinteticaTipoDocumentoFELResponse res = tipoDocumentoServiceFEL.ricercaSinteticaTipoDocumentoFEL(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio RicercaComponenteCapitolo");
			addErrori(res);
			return INPUT;
		}
		
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato di ricerca per i dati forniti");
			impostaMessaggioNessunDatoTrovato();
			return INPUT;
		}
		//rimuovere
		if(res.getListaTipoDocFEL().isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreBil.COMPONENTE_INESISTENTE.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + res.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_TIPO_DOCUMENTO_FEL, req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_DOCUMENTO_FEL, res.getListaTipoDocFEL());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);

		return SUCCESS;
	}
	
	/**
	 * 
	 */
	protected void impostaMessaggioNessunDatoTrovato() {
		addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste dei MacroTipo  e SottoTipo
	 */

	public String caricaListaTipoDocumentiContabilia() {
		
		model.setListaDocContabiliaEntrata(null);
		model.setListaDocContabiliaSpesa(null);
		
		
		return SUCCESS;
	}
	

	/**
	 * Caricamento delle liste
	 */
	private void caricaListe(){
		// Caricamento delle varie liste per la gestione della UI
		// I tipi di documento utilizzato sono solo quelli di spesa, con eventuale filtro subordinato/regolarizzazione da chiamante
		checkAndObtainListaTipoDocumentoEntrata(TipoFamigliaDocumento.ENTRATA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
		checkAndObtainListaTipoDocumentoSpesa(TipoFamigliaDocumento.SPESA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
	}
	
	/**
	 * Popola le codifiche a partire dalla lista caricata in sessione.
	 */
	private void popolaCodificheDaSessione() {
		List<TipoDocFEL> listaTipoDocFELInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO_FEL);
		 model.getTipoDocFel().setUid(5);
		TipoDocFEL tipoDocFEL = ComparatorUtils.searchByUid(listaTipoDocFELInSessione, model.getTipoDocFel());
		// Imposto il tipoAtto nel model
		model.setTipoDocFel(tipoDocFEL);
		
	 
	}
	
	
	

	
}
