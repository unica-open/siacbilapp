/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.provvedimento;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.provvedimento.RisultatiRicercaProvvedimentoModel;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Action relativa ai risultati della ricerca per il provvedimento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaProvvedimentoAction extends GenericBilancioAction<RisultatiRicercaProvvedimentoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5762798503478748226L;
	
	@Autowired private transient ProvvedimentoService provvedimentoService;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Imposto la pagina di partenza
		Integer posizioneIniziale = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, Integer.class);
		if(posizioneIniziale != null) {
			model.setPosizioneIniziale(posizioneIniziale);
			sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		}
		
		// Controllo se si sia nel cas di rientro dall'aggiornamento
		log.debug(methodName, "Controllo se devo richiamare il servizio");
		Boolean rientroDaAggiorna = sessionHandler.getParametro(BilSessionParameter.RIENTRO);
		if(rientroDaAggiorna == null) {
			log.debug(methodName, "Carico la lista dalla sessione");
			// Impostazione dei dati dalla sessione
			List<AttoAmministrativo> listaInSessione = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVEDIMENTO);
			
			//siac 6959 mock per elementi
			/*for(int i=0; i<listaInSessione.size(); i++){
				
					listaInSessione.get(i).setBloccoRagioneria(false);
					listaInSessione.get(i).setInseritoManualmente(true);
				
				
			}*/
			
			/////
			model.setListaProvvedimento(listaInSessione);
			return SUCCESS;
		}
		
		// Svuoto il parametro dalla sessione
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		// Richiamo il servizio
		RicercaProvvedimento requestRicerca = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROVVEDIMENTO, RicercaProvvedimento.class);
		RicercaProvvedimentoResponse responseRicerca = provvedimentoService.ricercaProvvedimento(requestRicerca);
		logServiceResponse(responseRicerca);
		
		if(responseRicerca.hasErrori()) {
			log.debug(methodName, "Fallimento nella response");
			addErrori(responseRicerca);
			return SUCCESS;
		}
		
		if(responseRicerca.getListaAttiAmministrativi().isEmpty()) {
			log.debug(methodName, "La lista è vuota");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
		
		// Ottengo la lista dalla response
		model.setListaProvvedimento(responseRicerca.getListaAttiAmministrativi());
		
		// Imposto i dati in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVEDIMENTO, responseRicerca.getListaAttiAmministrativi());
		
		return SUCCESS;
	}
	
	/**
	 * Redirezione alla action di aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		log.debug("aggiorna", "Provvedimento da aggiornare: " + model.getUidDaAggiornare() + ". Pagina in cui rientrare: " + model.getiDisplayStart());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, model.getiDisplayStart());
		return SUCCESS;
	}
	
	/**
	 * Redirezione alla action di consultazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		log.debug("consulta", "Provvedimento da consultare: " + model.getUidDaConsultare() + ". Pagina in cui rientrare: " + model.getiDisplayStart());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, model.getiDisplayStart());
		return SUCCESS;
	}

	
	public boolean isAggiornaAbilitato(AttoAmministrativo attoAmministrativo) {
		return  isAzioneRichiesta(AzioneConsentitaEnum.AGGIORNA_PROVVEDIMENTO_SISTEMA_ESTERNO) ||
				! Boolean.TRUE.equals(attoAmministrativo.getBloccoRagioneria())  && 
				(attoAmministrativo.getProvenienza() == null || "MANUALE".equalsIgnoreCase(attoAmministrativo.getProvenienza()) );
	}

}
