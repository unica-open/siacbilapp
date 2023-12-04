/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.provvedimento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.provvedimento.AggiornaProvvedimentoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Action per L'aggiornamento di un Provvedimento.
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaProvvedimentoAction extends GenericBilancioAction<AggiornaProvvedimentoModel> {
	/** Per la serializzazione */
	private static final long serialVersionUID = -5783603456743125980L;
	/** La descrizione del movimento interno */
	private static final String DESCRIZIONE_MOVIMENTO_INTERNO = "Movimento interno";
	
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		model.setTipiAtti(caricaTipiAtti());
		model.setStatiOperativi(caricaStatiOperativi());
	}
		
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		RicercaProvvedimentoResponse response = ricerca();
		if (response.hasErrori() || response.getListaAttiAmministrativi() == null || response.getListaAttiAmministrativi().isEmpty()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "ricerca provvedimento ok");
		
		// Prendo il primo atto amministrativo
		AttoAmministrativo attoAmministrativo = response.getListaAttiAmministrativi().get(0);
		model.setAttoAmministrativo(attoAmministrativo);
		model.setMovimentoInterno(DESCRIZIONE_MOVIMENTO_INTERNO.equalsIgnoreCase(attoAmministrativo.getTipoAtto().getDescrizione()));
		
		verificaStatiOperativi();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link AggiornaProvvedimentoAction#execute()}.
	 */
	public void validateExecute() {
		checkCondition(model.getUidDaAggiornare() != null && model.getUidDaAggiornare().intValue() != 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Uid da Aggiornare"));
	}
	
	/**
	 * Aggiornamento il Provvedimento.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiornamentoCDU() {
		final String methodName = "aggiornamentoCDU";
		
		AggiornaProvvedimentoResponse response = aggiorna();
		
		/*test risultato chiamata al servizio*/
		if (response.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "aggiornamento completato con successo");
		impostaInformazioneSuccesso();
		
		sessionHandler.cleanAllSafely();
		
		// SIAC-5639: reimposto i dati dal servizio
		model.setAttoAmministrativo(response.getAttoAmministrativoAggiornato());
		
		// Imposto i dati per il rientro dall'aggiornamento
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link AggiornaProvvedimentoAction#aggiornamentoCDU()}.
	 */
	public void validateAggiornamentoCDU() {
		try {
			checkCondition(model.getAttoAmministrativo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Atto Amministrativo"), true);
		} catch (ParamValidationException e) {
			// Errore di validazione del dato: non ho l'atto. Esco subito
			return;
		}
		
		checkCondition(model.getAttoAmministrativo().getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		
		TipoAtto tipoAtto = ComparatorUtils.searchByUidEventuallyNull(model.getTipiAtti(), model.getAttoAmministrativo().getTipoAtto());
		model.getAttoAmministrativo().setTipoAtto(tipoAtto);
		checkNotNullNorInvalidUid(tipoAtto, "Tipo Atto");
		// Se non ho un movimento interno, controllo che abbia messo il numero
		checkCondition(tipoAtto == null || Boolean.TRUE.equals(tipoAtto.getProgressivoAutomatico()) || model.getAttoAmministrativo().getNumero() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Atto Amministrativo"));
		checkCondition(Math.abs(model.getAttoAmministrativo().getNumero()) < 1000000, ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Atto Amministrativo", ": non deve avere piu' di 6 cifre"));
		// SIAC-4285
		if(model.getAttoAmministrativo().getParereRegolaritaContabile() == null) {
			model.getAttoAmministrativo().setParereRegolaritaContabile(Boolean.FALSE);
		}
	}
	
	/* CARICA LISTE DI CONTROLLO*/
	/**
	 * Carica la lista dei tipi di Atto di legge.
	 * 
	 * @return la lista dei tipi di atto
	 */
	private List<TipoAtto> caricaTipiAtti() {
		List<TipoAtto> tipiAtti = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(tipiAtti == null) {
			tipiAtti = new ArrayList<TipoAtto>();
			// Non ho ancora i dati in sessione: li carico
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			logServiceRequest(request);
			
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			if (!response.hasErrori()) {
				tipiAtti = response.getElencoTipi();
				ComparatorUtils.sortByCodice(tipiAtti);
				sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, tipiAtti);
			}
		}
		return tipiAtti;
	}
	
	/**
	 * Crea una lista con gli Stati Operatici relativi agli Atti.
	 * 
	 * @return la lista degli stati
	 */
	private List<StatoOperativoAtti> caricaStatiOperativi() {
		return new ArrayList<StatoOperativoAtti>(Arrays.asList(StatoOperativoAtti.values()));
	}
	
	/**
	 * Controllo la lista degli stati operativi per il provvedimento.
	 */
	private void verificaStatiOperativi() {
		StatoOperativoAtti stato = StatoOperativoAtti.fromString(model.getAttoAmministrativo().getStatoOperativo());
		if (stato == null) {
			return;
		}
		if(StatoOperativoAtti.ANNULLATO.equals(stato)) {
			model.getStatiOperativi().remove(StatoOperativoAtti.DEFINITIVO);
		} else if(StatoOperativoAtti.DEFINITIVO.equals(stato)) {
			model.getStatiOperativi().remove(StatoOperativoAtti.PROVVISORIO);
		}
	}
	
	private RicercaProvvedimentoResponse ricerca() {
		// Prepara Request
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		// richiama servizio 
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		return response;
	}
	
	/**
	 * Aggiorna il provvedimento.
	 * 
	 * @return la response del servizio
	 */
	private AggiornaProvvedimentoResponse aggiorna() {
		//task-69
		//boolean prova = model.getAttoAmministrativo().getBloccoRagioneria();
		TipoAtto tipoAtto = ComparatorUtils.searchByUid(model.getTipiAtti(), model.getAttoAmministrativo().getTipoAtto());
		model.getAttoAmministrativo().setTipoAtto(tipoAtto);
		
		//CHECK SU INSERIEMNTO MANUALE
		if( model.getAttoAmministrativo()!= null && 
				 (model.getAttoAmministrativo().getProvenienza()!= null && model.getAttoAmministrativo().getProvenienza().length()==0)){
			model.getAttoAmministrativo().setProvenienza(null);
		}
		
		
		// Prepara Request
		AggiornaProvvedimento request = model.creaRequestAggiornaProvvedimento();
		logServiceRequest(request);
		
		if (isAzioneRichiestaAggiornaProvvedimentoSistemaEsterno()) {
			request.setCodiceInc(defaultGetCodiceInc());
		}
		
		// richiama servizio
		AggiornaProvvedimentoResponse response = provvedimentoService.aggiornaProvvedimento(request);
		logServiceResponse(response);
		return response;
	}
	
	public boolean isAzioneRichiestaAggiornaProvvedimentoSistemaEsterno() {
		return isAzioneRichiesta(AzioneConsentitaEnum.AGGIORNA_PROVVEDIMENTO_SISTEMA_ESTERNO);
	}
	

	private String defaultGetCodiceInc() {
		return String.format("%s-%s", 
				sessionHandler.getRichiedente().getOperatore().getCodiceFiscale(), 
				StringUtils.isBlank(model.getCodiceInc()) ? "BackofficeModificaModalitaPagamentoAttoAllegato" : model.getCodiceInc());
	}
}
