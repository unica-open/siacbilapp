/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.quadroeconomico;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.quadroeconomico.GestisciQuadroEconomicoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.QuadroEconomicoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AnnullaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AnnullaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomicoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.ParteQuadroEconomico;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la gestione del quadro economico
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class GestisciQuadroEconomicoAction extends GenericBilancioAction <GestisciQuadroEconomicoModel> {
	
	/**Per la serializzazione */
	private static final long serialVersionUID = 2235502001929012397L;
	
	//Services
	@Autowired private transient QuadroEconomicoService quadroEconomicoService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		//checkCasoDUsoApplicabile("");
		caricaListe();
		return SUCCESS;
	}
	
	
	private void caricaListe() {		
		model.setStatiOperativiQuadroEconomico(Arrays.asList(StatoOperativoQuadroEconomico.values()));
		model.setParteQuadroEconomico(Arrays.asList(ParteQuadroEconomico.values()));
	}


// FIXME ranzare se a tendere non verrà usato
//	@Override
//	protected void checkCasoDUsoApplicabile(String cdu) {
//		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
//		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
//		//L’esercizio NON deve essere in una delle seguenti FASI  in cui la Contabilità Generale non è utilizzabile:PLURIENNALE,PREVISIONE
//		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
//		boolean faseDiBilancioNonCompatibile = FaseBilancio.PLURIENNALE.equals(faseBilancio) || FaseBilancio.PREVISIONE.equals(faseBilancio);
//				//per la fase  CHIUSO  non devono essere disponibili le azioni relative alla gestione  
//				// || FaseBilancio.CHIUSO.equals(faseBilancio);
//		
//		if(faseDiBilancioNonCompatibile) {
//			//lancio un errore per impedire all'utente di effettuare l'operazione
//			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
//		}
//	}
	
	/**
	 * Validate inserisci quadro_economico padre.
	 */
	public void validateInserisciQuadroEconomicoPadre() {
		checkQuadroEconomico(model.getQuadroEconomico());
	}


	/**
	 * @param quadroEconomicoDaControllare
	 */
	private void checkQuadroEconomico(QuadroEconomico quadroEconomicoDaControllare) {
		checkNotNull(quadroEconomicoDaControllare, "quadro economico di livello 1");
		checkNotNullNorEmpty(quadroEconomicoDaControllare.getCodice(), "codice quadro economico");
		checkNotNullNorEmpty(quadroEconomicoDaControllare.getDescrizione(), "descrizione quadro economico");
	}
	
	/**
	 * Prepare inserisci quadro_economico padre.
	 */
	public void prepareInserisciQuadroEconomicoPadre() {
		model.setQuadroEconomico(null);
		model.setQuadroEconomicoFiglioInElaborazione(null);
		model.setUidQuadroEconomicoPadre(0);
	}
	
	/**
	 * Inserisci quadro_economico padre.
	 *
	 * @return the string
	 */
	public String inserisciQuadroEconomicoPadre() {
		
		model.impostaDatiPerInserimentoQuadroEconomicoPadre();
		
		InserisceQuadroEconomicoResponse response = chiamaServizioInserisceQuadroEconomico();
		
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		model.setQuadroEconomicoPadreInElaborazione(response.getQuadroEconomico());
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}


	/**
	 * @return
	 */
	private InserisceQuadroEconomicoResponse chiamaServizioInserisceQuadroEconomico() {
		InserisceQuadroEconomico req = model.creaRequestInserisceQuadroEconomico();
		return quadroEconomicoService.inserisceQuadroEconomico(req);
	}
	
	/**
	 * Validate inserisci quadro_economico padre.
	 */
	public void validateInserisciQuadroEconomicoFiglio() {
		QuadroEconomico quadroEconomicoDaControllare = model.getQuadroEconomico();
		checkQuadroEconomico(quadroEconomicoDaControllare);
		checkCondition(model.getUidQuadroEconomicoPadre() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("quadro economico livello 1 "));
	}

	/**
	 * Prepare inserisci quadro_economico padre.
	 */
	public void prepareInserisciQuadroEconomicoFiglio() {
		model.setQuadroEconomico(null);
		model.setUidQuadroEconomicoPadre(0);
	}
	
	/**
	 * Inserisci quadro_economico padre.
	 *
	 * @return the string
	 */
	public String inserisciQuadroEconomicoFiglio() {
		model.impostaDatiPerInserimentoQuadroEconomicoFiglio();
		
		InserisceQuadroEconomicoResponse response = chiamaServizioInserisceQuadroEconomico();
		
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		model.setQuadroEconomicoFiglioInElaborazione(response.getQuadroEconomico());
		return SUCCESS;
		
	}
	
	
	/**
	 * Prepare cerca quadro_economico.
	 */
	public void prepareCercaQuadroEconomico() {
		model.setQuadroEconomicoPadreInElaborazione(null);
	}
	
	/**
	 * Validate cerca quadro_economico.
	 */
	public void validateCercaQuadroEconomico() {
/*
		log.info("validateCercaQuadroEconomico", "1 " + model.getQuadroEconomico().getCodice());
		log.info("validateCercaQuadroEconomico", "2 " + model.getQuadroEconomico().getDescrizione());
		log.info("validateCercaQuadroEconomico", "3 " +  model.getQuadroEconomico().getStatoOperativoQuadroEconomico());
*/
		checkCondition(StringUtils.isNotBlank(model.getQuadroEconomico().getCodice()) || 
					  StringUtils.isNotBlank(model.getQuadroEconomico().getDescrizione()) || 
					  model.getQuadroEconomico().getStatoOperativoQuadroEconomico() != null
				     ,ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
	}
	
	/**
	 * Cerca quadro_economico.
	 *
	 * @return the string
	 */
	public String cercaQuadroEconomico() {
		final String methodName = "cercaQuadroEconomico";
		log.info(methodName, "START");
		RicercaSinteticaQuadroEconomico req = model.creaRequestRicercaSinteticaQuadroEconomico();
		
		RicercaSinteticaQuadroEconomicoResponse response = quadroEconomicoService.ricercaSinteticaQuadroEconomico(req);
		log.info(methodName,"numero risultati " + response.getListQuadroEconomico().size());
		
		
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nel servizio di ricerca del quadro_economico");
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("Il quadro economico"));
			return INPUT;
		}
		model.setListaQuadroEconomicoTrovati(response.getListQuadroEconomico());
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUADRO_ECONOMICO, req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUADRO_ECONOMICO, response.getListQuadroEconomico());
		return SUCCESS;
	}
	
	
	/**
	 * Prepare cerca quadro_economico.
	 */
	public void prepareAnnullaQuadroEconomico() {
		model.setQuadroEconomico(null);
	}
	
	/**
	 * Validate cerca quadro_economico.
	 */
	public void validateAnnullaQuadroEconomico() {
		checkNotNullNorInvalidUid(model.getQuadroEconomico(), "quadro economico");
	}
	
	/**
	 * annulla quadro economico.
	 *
	 * @return the string
	 */
	public String annullaQuadroEconomico() {
		final String methodName = "annullaQuadroEconomico";
		AnnullaQuadroEconomico req = model.creaRequestAgnnullaQuadroEconomico();
		
		AnnullaQuadroEconomicoResponse response = quadroEconomicoService.annullaQuadroEconomico(req);
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nel servizio di ricerca del quadro economico");
			addErrori(response);
			return INPUT;
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}
	
	
	/**
	 * Prepare aggiorna quadro_economico.
	 */
	public void prepareAggiornaQuadroEconomico() {
		model.setQuadroEconomico(null);
		model.setUidQuadroEconomicoPadre(0);
	}
	
	/**
	 * Validate aggiorna quadro_economico.
	 */
	public void validateAggiornaQuadroEconomico() {
		checkNotNullNorInvalidUid(model.getQuadroEconomico(), "quadro economico");
		checkNotNullNorEmpty(model.getQuadroEconomico().getCodice(), "codice quadro economico");
		checkNotNullNorEmpty(model.getQuadroEconomico().getDescrizione(),"descrizione quadro economico");
		checkNotNullNorEmpty(model.getDescrizioneStatoOperativoQuadroEconomico(), "stato quadro economico");
	}
	
	/**
	 * Aggiorna quadro economico.
	 *
	 * @return the string
	 */
	public String aggiornaQuadroEconomico() {
		final String methodName = "aggiornaQuadroEconomico";
		model.impostaDatiPerAggiornamentoClassificatore();
		AggiornaQuadroEconomico req = model.creaRequestAggiornaQuadroEconomico();
		AggiornaQuadroEconomicoResponse response = quadroEconomicoService.aggiornaQuadroEconomico(req);
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nel servizio di ricerca del quadro economico");
			addErrori(response);
			return INPUT;
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}
	
	
	

	
	
	
}
