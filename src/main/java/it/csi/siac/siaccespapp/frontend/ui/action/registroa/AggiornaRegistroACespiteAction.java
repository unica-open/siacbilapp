/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.registroa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecser.model.EventoRegistroACespiteSelector;
import it.csi.siac.siaccespapp.frontend.ui.model.registroa.AggiornaRegistroACespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import xyz.timedrain.arianna.plugin.BreadCrumb;

/**
 * Action per la consultazione del registro A (prime note verso inventario contabile)
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaRegistroACespiteAction extends BaseConsultaAggiornaRegistroACespiteAction<AggiornaRegistroACespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2308065496807217805L;
	
	@Autowired private ClassificazioneCespiteService classificazioneCespiteService;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		boolean movimentiCaricatiCorrettamente = caricaMovimentiDettaglio();
		if(!movimentiCaricatiCorrettamente) {
			return INPUT;
		}
		caricaListaTipoBene();
		return SUCCESS;
	}

	/**
	 * Caricamento della lista del Tipo Bene
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoBene() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoBene";
		List<TipoBeneCespite> listaTipoBeneCespite = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_BENE_CESPITE);
		// Se non ho i dati, effettuo la ricerca

		if (listaTipoBeneCespite == null) {
			// recuperare da servizio la lista Tipo Bene Cespite
			RicercaSinteticaTipoBeneCespite         req = model.creaRequestRicercaSinteticaTipoBeneCespite();
			RicercaSinteticaTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);

			if (res.hasErrori()) {
				String errorMsg = createErrorInServiceInvocationString(RicercaSinteticaTipoBeneCespite.class, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			listaTipoBeneCespite = res.getListaTipoBeneCespite();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_BENE_CESPITE, listaTipoBeneCespite);
		}

		model.setListaTipoBeneCespite(listaTipoBeneCespite);

	}
	
	@Override
	public String ottieniTabellaCespiti() { 
		EventoRegistroACespiteSelector eventoRegistroASelector = EventoRegistroACespiteSelector.byCodiceEventoAndTipoCausale(model.getCodiceEventoCespite(), model.getTipoCausale());
		if(eventoRegistroASelector != null) {
			model.setAbilitaInserimentoCespite(eventoRegistroASelector.getIsInserimentoCespiteContestualePrimaNota());
			model.setAbilitaRicercaCespite(eventoRegistroASelector.getIsCollegabileACespiteEsistente());
			return SUCCESS;
		}
		model.setAbilitaInserimentoCespite(Boolean.FALSE);
		model.setAbilitaRicercaCespite(Boolean.FALSE);
		
		return SUCCESS;
	}

	
	/**
	 * Validate redirect to inserisci cespite.
	 */
	public void validateRedirectToInserisciCespite() {
		checkNotNullNorInvalidUid(model.getMovimentoDettaglio(), "movimento ep");
	}
	
	/**
	 * Redirect to inserisci cespite.
	 *
	 * @return the string
	 */
	public String redirectToInserisciCespite() {
		sessionHandler.setParametro(BilSessionParameter.UID_MOVIMENTO_DETTAGLIO_DA_COLLEGARE_A_CESPITE, model.getMovimentoDettaglio().getUid());
		sessionHandler.setParametro(BilSessionParameter.UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE, model.getUidPrimaNota());
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_CESPITE_SU_PRIMA_NOTA, model.getImportosuRegistroA());
		return SUCCESS;
	}
	
	/**
	 * Validate redirect to inserisci cespite.
	 */
	public void validateRedirectToAggiornaCespite() {
		checkNotNullNorInvalidUid(model.getMovimentoDettaglio(), "movimento ep");
	}
	
	/**
	 * Redirect to inserisci cespite.
	 *
	 * @return the string
	 */
	public String redirectToAggiornaCespite() {
		sessionHandler.setParametro(BilSessionParameter.UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE, model.getUidPrimaNota());
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_CESPITE_SU_PRIMA_NOTA, model.getImportosuRegistroA());
		return SUCCESS;
	}
	
	/**
	 * Validate collega cespite esistente.
	 */
	public void validateCollegaCespiteEsistente() {
		checkNotNullNorInvalidUid(model.getCespite(), "cespite");
		checkNotNull(model.getUidMovimentoDettaglio(), "movimento ep");
	}
	
	/**
	 * Collega cespite esistente.
	 *
	 * @return the string
	 */
	public String collegaCespiteEsistente() {
		final String methodName ="collegaCespiteEsistente";
		CollegaCespiteRegistroACespite req = model.creaRequestCollegaCespiteRegistroACespite();
		CollegaCespiteRegistroACespiteResponse response = primaNotaCespiteService.collegaCespitePRegistroACespite(req);
		if(response.hasErrori()) {
			log.debug(methodName, "errori durante l'invocazione del servizio.");
			addErrori(response);
			return INPUT;
		}
		addMessaggi(response.getMessaggi());
		List<Cespite> listaScartati = response.getListaScartati();
		impostaMessaggioCespitiScartati(listaScartati);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Validate collega cespite esistente.
	 */
	public void validateAggiornaImportoCespite() {
		checkNotNullNorInvalidUid(model.getCespite(), "cespite");
		checkNotNullNorInvalidUid(model.getMovimentoDettaglio(), "movimento ep");
		checkNotNull(model.getImportosuRegistroA(), "importo su prima nota");
	}
	
	/**
	 * Collega cespite esistente.
	 *
	 * @return the string
	 */
	public String aggiornaImportoCespite() {
		final String methodName ="aggiornaImportoCespite";
		AggiornaImportoCespiteRegistroACespite req = model.creaRequestAggiornaImportoCespiteRegistroACespite();
		AggiornaImportoCespiteRegistroACespiteResponse response = primaNotaCespiteService.aggiornaImportoCespiteRegistroACespite(req);
		if(response.hasErrori()) {
			log.debug(methodName, "errori durante l'invocazione del servizio.");
			addErrori(response);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	
	
	/**
	 * @param listaScartati
	 */
	private void impostaMessaggioCespitiScartati(List<Cespite> listaScartati) {
		if( listaScartati == null || listaScartati.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder()
				.append("Non &egrave; stato possibile collegare tutti i cespiti selezionati. ")
				.append("Numero espiti scartati: ")
				.append(listaScartati.size())
				.append(".Identificativo dei cespiti:");
		List<String> chunks = new ArrayList<String>();
		for (Cespite cespite : listaScartati) {
			chunks.add(Integer.valueOf(cespite.getUid()).toString());
		}
		sb.append(StringUtils.join(chunks, ", "));
		addMessaggio(MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio(sb.toString()));
	}
	
	/**
	 * Scollega cespite da prima nota.
	 *
	 * @return the string
	 */
	public String scollegaCespiteDaPrimaNota() {
		final String methodName ="collegaCespiteEsistente";
		ScollegaCespiteRegistroACespite req = model.creaRequestSollegaCespiteRegistroACespite();
		ScollegaCespiteRegistroACespiteResponse response = primaNotaCespiteService.scollegaCespitePRegistroACespite(req);
		if(response.hasErrori()) {
			log.debug(methodName, "errori durante l'invocazione del servizio.");
			addErrori(response);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	/**
	 * Salvataggio
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String salva() {
		final String methodName ="salva";
		AggiornaPrimaNotaSuRegistroACespite req = model.creaRequestAggiornaPrimaNotaSuRegistroACespite();
		// XXX: la chiamata al servizio e' stata resa asincrona in seguito a timeout verificatori in ambiente di test. Potrebbe pero' trattarsi di 
		// un problema di sovraccarico del sistema, mantengo la chiamata precedente per un eventuale ritorno al sincrono	
//		AggiornaPrimaNotaSuRegistroACespiteResponse response = aggiornaSincrono(req);	
		AsyncServiceResponse response = aggiornaAsincrono(req);
		if(response.hasErrori()) {
			log.debug(methodName, "errori durante l'invocazione del servizio.");
			addErrori(response);
			return INPUT;
		}
		model.setDisabilitaAzioni(true);

		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("integrazione della prima nota", ""));
		return SUCCESS;
	}

	//SIAC-6570
// XXX: la chiamata al servizio e' stata resa asincrona in seguito a timeout verificatori in ambiente di test. Potrebbe pero' trattarsi di 
// un problema di sovraccarico del sistema, mantengo la chiamata precedente per un eventuale ritorno al sincrono	
//	/**
//	 * Aggiorna sincrono.
//	 *
//	 * @param req the req
//	 * @return the aggiorna prima nota cespite da contabilita generale response
//	 */
//	private AggiornaPrimaNotaSuRegistroACespiteResponse aggiornaSincrono(AggiornaPrimaNotaSuRegistroACespite req) {
//		return primaNotaCespiteService.aggiornaPrimaNotaSuRegistroACespite(req);
//	}
	
	/**
	 * Aggiorna asincrono.
	 *
	 * @param req the req
	 * @return the async service response
	 */
	private AsyncServiceResponse aggiornaAsincrono(AggiornaPrimaNotaSuRegistroACespite req) {
		return primaNotaCespiteService.aggiornaPrimaNotaSuRegistroACespiteAsync(wrapRequestToAsync(req));
	}
	
}