/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoConsultazioneScritturaPrimaNotaLiberaFactory;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.ConsultaCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegata;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegataResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrata;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrataFactory;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class ConsultaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCespiteAction extends GenericCespiteAction<ConsultaCespiteModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 9021669725330286849L;
	
	@Autowired private PrimaNotaService primaNotaService;
	
	private static final Map<TipoCollegamento, String> MAPPING_DATI_FINANZIARI;
	
	static {
		Map<TipoCollegamento, String> tmp = new HashMap<TipoCollegamento, String>();
		tmp.put(TipoCollegamento.LIQUIDAZIONE, "liquidazione");
		tmp.put(TipoCollegamento.DOCUMENTO_SPESA, "documentoSpesa");
		tmp.put(TipoCollegamento.SUBDOCUMENTO_SPESA, "documentoSpesa");
		tmp.put(TipoCollegamento.DOCUMENTO_ENTRATA, "documentoEntrata");
		tmp.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, "documentoEntrata");
		
		MAPPING_DATI_FINANZIARI = Collections.unmodifiableMap(tmp);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setCespite(null);
		RicercaDettaglioCespiteResponse response = ottieniRicercaDettaglioCespiteResponse();
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		model.setCespite(response.getCespite());
		return SUCCESS;		
	}
	
	//////// -----CARICAMENTO COLLAPSE ENTITA LEGATE AL CESPITE
	
	/**
	 * Ottieni piano ammortamento.
	 *
	 * @return the string
	 */
	public String ottieniPianoAmmortamento() {
		RicercaSinteticaDettaglioAmmortamentoAnnuoCespite req = model.creaRequestRicercaSinteticaDettaglioAmmortamentoAnnuoCespite();
		RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse response = cespiteService.ricercaSinteticaDettaglioAmmortamentoAnnuoCespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		model.setTotaleImportoAmmortato(response.getTotaleImportoAmmortato());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DETTAGLIO_AMMORTAMENTO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_AMMORTAMENTO, response.getListaDettaglioAmmortamentoAnnuoCespite());
		return SUCCESS;
	}
	
	/**
	 * Ottieni dati contabili.
	 *
	 * @return the string
	 */
	public String ottieniPrimeNoteContabilitaGenerale(){
		RicercaSinteticaScrittureRegistroAByCespite req = model.creaRequestRicercaSinteticaScrittureRegistroAByCespite();
		RicercaSinteticaScrittureRegistroAByCespiteResponse response = cespiteService.ricercaSinteticaScrittureRegistroAByCespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SCRITTURE_REGISTROA_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SCRITTURE_REGISTROA_CESPITE, response.getListaPrimaNota());
		return SUCCESS;
	}
	
	
	/**
	 * Ottieni dismissioni.
	 *
	 * @return the string
	 */
	public String ottieniDismissioni(){
		RicercaSinteticaDismissioneCespite req = model.creaRicercaSinteticaDismissioneCespite();
		RicercaSinteticaDismissioneCespiteResponse response = cespiteService.ricercaSinteticaDismissioneCespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		//N.B: non ci saranno mai piu' di una dismissione per cespite
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DISMISSIONE_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DISMISSIONE_CESPITE, response.getListaDismissioneCespite());
		return SUCCESS;
	}
	
	/**
	 * Ottieni rivalutazioni.
	 *
	 * @return the string
	 */
	public String ottieniRivalutazioni(){
		RicercaSinteticaVariazioneCespite request = model.creaRequestRicercaSinteticaVariazioneCespite(Boolean.TRUE);
		RicercaSinteticaVariazioneCespiteResponse response = cespiteService.ricercaSinteticaVariazioneCespite(request);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		// Ricerca effettuata con successo: imposto i dati in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_RIVALUTAZIONE_CESPITE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_RIVALUTAZIONE_CESPITE, response.getListaVariazioneCespite());
		
		return SUCCESS;
	}
	
	/**
	 * Ottieni svalutazioni.
	 *
	 * @return the string
	 */
	public String ottieniSvalutazioni(){
		RicercaSinteticaVariazioneCespite request = model.creaRequestRicercaSinteticaVariazioneCespite(Boolean.FALSE);
		RicercaSinteticaVariazioneCespiteResponse response = cespiteService.ricercaSinteticaVariazioneCespite(request);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		// Ricerca effettuata con successo: imposto i dati in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SVALUTAZIONE_CESPITE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SVALUTAZIONE_CESPITE, response.getListaVariazioneCespite());
		return SUCCESS;
	}
	
	
////////-----CARICAMENTO PRIME NOTE VARIE
	
	/**
	 * Ottieni donazioni rinvenimenti.
	 *
	 * @return the string
	 */
	public String ottieniDonazioniRinvenimenti(){
		RicercaScrittureInventarioByEntitaCollegata req = model.creaRequestRicercaScrittureInventarioByEntitaCollegataCespite();
		return ottieniPrimeNoteCollegate(req, Cespite.class);
	}
	
	/**
	 * Ottieni prime note ammortamento.
	 *
	 * @return the string
	 */
	public String ottieniPrimeNoteAmmortamento(){
		RicercaScrittureInventarioByEntitaCollegata req = model.creaRequestRicercaScrittureInventarioByEntitaCollegataDettaglioAmmortamentoAnnuo();
		return ottieniPrimeNoteCollegate(req, DettaglioAmmortamentoAnnuoCespite.class);
	}
	
	/**
	 * Ottieni prime note dati contabili.
	 *
	 * @return the string
	 */
	public String ottieniPrimeNoteDatiContabili(){
		RicercaScrittureInventarioByEntitaCollegata req = model.creaRequestRicercaScrittureInventarioByEntitaCollegataPrimaNota();
		return ottieniPrimeNoteCollegate(req, PrimaNota.class);
	}
	
	/**
	 * Ottieni prime note variazione.
	 *
	 * @return the string
	 */
	public String ottieniPrimeNoteDismissione() {
		//le dismissioni, al contrario delle altre entita, hanno potenzialmente molte prime note
		RicercaScrittureInventarioByEntitaCollegata req = model.creaRequestRicercaScrittureInventarioByEntitaCollegataDismissione();
		return ottieniPrimeNoteCollegate(req, DismissioneCespite.class);
	}
	
	/**
	 * Ottieni prime note variazione.
	 *
	 * @return the string
	 */
	public String ottieniPrimeNoteVariazione() {
		RicercaScrittureInventarioByEntitaCollegata req = model.creaRequestRicercaScrittureInventarioByEntitaCollegataVariazione();
		return ottieniPrimeNoteCollegate(req, VariazioneCespite.class);
	}
	
	/**
	 * @param req
	 * @param classeEntitaGenerantePrimaNota 
	 * @return
	 */
	private String ottieniPrimeNoteCollegate(RicercaScrittureInventarioByEntitaCollegata req, Class<?> classeEntitaGenerantePrimaNota) {
		RicercaScrittureInventarioByEntitaCollegataResponse res = cespiteService.ricercaScrittureInventarioByEntitaCollegata(req);
		if(res.hasErrori()) {
			addErrori(res);
			impostaDefaultListaPrimeNote(classeEntitaGenerantePrimaNota);
			return INPUT;
		}
		List<PrimaNota> primeNote = res.getListaPrimaNota();
		if(primeNote == null) {
			impostaDefaultListaPrimeNote(classeEntitaGenerantePrimaNota);
			return INPUT;
		}
		
		model.setListaPrimeNote(ElementoConsultazioneScritturaPrimaNotaLiberaFactory.creaElementoConsultazioneScritturaPrimaNotaLibera(primeNote, DismissioneCespite.class));
		return SUCCESS;
	}
	
	/**
	 * Imposta una lista vupota di prime note collegate alla class clazz passata in input.
	 *
	 * @param clazz the clazz
	 */
	private void impostaDefaultListaPrimeNote(Class<?> clazz) {
		PrimaNota pn= null;
		model.setListaPrimeNote(Arrays.asList(ElementoConsultazioneScritturaPrimaNotaLiberaFactory.creaElementoConsultazioneScritturaPrimaNotaLibera(pn, clazz)));
	}
	
	/**
	 * Ottiene i dati finanziari
	 * @return una stringa corrispondente ai risultati dell'invocazione
	 */
	public String ottieniDatiFinanziari() {
		final String methodName = "ottieniDatiFinanziari";
		String tipoMovimento = MAPPING_DATI_FINANZIARI.get(model.getTipoCollegamento());
		model.setTipoMovimento(tipoMovimento);
		
		OttieniEntitaCollegatePrimaNota req = model.creaRequestOttieniEntitaCollegatePrimaNota();
		OttieniEntitaCollegatePrimaNotaResponse res = primaNotaService.ottieniEntitaCollegatePrimaNota(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(OttieniEntitaCollegatePrimaNota.class, res));
			addErrori(res);
			return INPUT;
		}
		impostaDatiFinanziariFromRequestEResponse(req, res);	
		
		return SUCCESS;
	}
	
	/**
	 *  A partire dalla request ({@link OttieniEntitaCollegatePrimaNota}) e dalla response ({@link OttieniEntitaCollegatePrimaNotaResponse}), imposta nel model e in sessione i dati necessari alla consultazione ed, eventualmente, alla gestione paginata della tabella
	 *  
	 *   @params req la request da impostare in sessione per la tabella paginata
	 *   @params req la response da cui ottenere gli elementi da consultare
	 * */
	private void impostaDatiFinanziariFromRequestEResponse(OttieniEntitaCollegatePrimaNota req,	OttieniEntitaCollegatePrimaNotaResponse res) {
		final String methodName = "impostaDatiFinanziariFromRequestEResponse";
		List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> wrappers = ElementoMovimentoConsultazionePrimaNotaIntegrataFactory.getInstances(res.getEntitaCollegate());
		popolaDatiaccessorii(wrappers);
		model.setListaDatiFinanziari(wrappers);
		if(!(TipoCollegamento.SUBDOCUMENTO_SPESA.equals(model.getTipoCollegamento()) || TipoCollegamento.SUBDOCUMENTO_ENTRATA.equals(model.getTipoCollegamento()))){
			return;
		}
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + res.getTotaleElementi());

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN, req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN, res.getEntitaCollegate());
	}
	
	/**
	 *  Popola il model  con eventuali dati accessori che devono essere estrapolati dall'elemento
	 *  @param wrappers la lista di ElementoMovimentoConsultazionePrimaNotaIntegrata da cui estrapolare i dati accessori
	 * */
	private void popolaDatiaccessorii(List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> wrappers) {
		for(ElementoMovimentoConsultazionePrimaNotaIntegrata<?> el : wrappers){
			if(el.getDatiAccessorii() != null){
				model.setDatiAccessoriiMovimentoFinanziario(el.getDatiAccessorii());
				break;
			}
		}
		
	}

	/**
	 * Validazione per il metodo {@link #ottieniDatiFinanziari()}
	 */
	public void validateOttieniDatiFinanziari() {
		checkNotNullNorInvalidUid(model.getPrimaNotaContabilitaFinanziaria(), "Prima nota");
		checkNotNull(model.getTipoCollegamento(), "Tipo collegamento");
	}


}
