/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.BaseInserisciAggiornaPianoDeiContiModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
//import it.csi.siac.siacgenser.model.CategoriaCespiti;
  import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamento;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamentoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.CodiceBilancio;
import it.csi.siac.siacgenser.model.TipoConto;
import it.csi.siac.siacgenser.model.TipoLegame;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaPianoDeiContiAction<M extends BaseInserisciAggiornaPianoDeiContiModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5716922411860569395L;
	
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	/** Serviz&icirc; del conto */
	@Autowired protected transient ContoService contoService;
	/** Serviz&icirc; delle codifiche */
	@Autowired protected transient CodificheService codificheService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	
	/**
	 * @return il parametro di sessione corrispondente alla lista classi piano
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaClassiPiano();

	/**
	 * @return il parametro di sessione corrispondente alla lista codici bilancio
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaCodiciBilancio();

	/**
	 * Caricamento della lista delle classi.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClassi() throws WebServiceInvocationFailureException {
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassiPiano());
		if(listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche("ClassePiano" + "_" + model.getAmbito().getSuffix());
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(getBilSessionParameterListaClassiPiano(), listaClassePiano);
		}
		model.setListaClassi(listaClassePiano);
	}
	
	/**
	 * Caricamento della lista delle classi che hanno conti di tipo ammortamento, da visualizzare nel caso sia selezionato il tipo CESPITI.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClassiAmmortamento() throws WebServiceInvocationFailureException {
		RicercaClassiPianoAmmortamento request = model.creaRequestRicercaClassiPianoAmmortamento();
		logServiceRequest(request);
		RicercaClassiPianoAmmortamentoResponse response = contoService.ricercaClassiPianoAmmortamento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		model.setListaClassiAmmortamento(response.getClassiPiano());
	}
	

	/**
	 * Controlla se la ricerca guidata del conto FIN &eacute; disabilitata.
	 * 
	 * @param cl la classe del piano
	 * @return <code>true</code> se il conto FIN guidata &acute; disabilitato; <code>false</code> altrimenti
	 */
	protected boolean isContoFinGuidataDisabled (ClassePiano cl){
		ClassePiano clp = ComparatorUtils.searchByUidEventuallyNull(model.getListaClassi(), cl);
		
		return clp != null
			&& !BilConstants.CLASSE_CONTO_COSTI_DI_ESERCIZIO.getConstant().equals(clp.getCodice())
			&& !BilConstants.CLASSE_CONTO_ATTIVO_PATRIMONIALE.getConstant().equals(clp.getCodice())
			&& !BilConstants.CLASSE_CONTO_PASSIVO_PATRIMONIALE.getConstant().equals(clp.getCodice());
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		return response;
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaTitoli() throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		List<TitoloEntrata> listaTE = response.getClassificatoriTitoloEntrata();
		
		model.setListaTitoloEntrata(listaTE);
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		
		model.setListaTitoloSpesa(listaTS);
	}
	
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
	
	
	/**
	 * caricamento delle codifiche tipoConto, tipoLegame e categoria cespiti, solo se non presenti in sessione
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaCodifiche() throws WebServiceInvocationFailureException{
		List<TipoConto> listaTipoContoDaSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_CONTO);
		List<CategoriaCespiti> listaCategoriaCespitiDaSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CATEGORIA_CESPITI);
		List<TipoLegame> listaTipoLegameDaSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_LEGAME);
		
		if(listaTipoContoDaSessione == null || listaCategoriaCespitiDaSessione == null || listaTipoLegameDaSessione == null
				|| listaTipoContoDaSessione.isEmpty() || listaCategoriaCespitiDaSessione.isEmpty() || listaTipoLegameDaSessione.isEmpty()){
			
			RicercaCodifiche request = model.creaRequestRicercaCodifiche();
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaTipoContoDaSessione = response.getCodifiche(TipoConto.class);
			listaCategoriaCespitiDaSessione = response.getCodifiche(CategoriaCespiti.class);
			listaTipoLegameDaSessione = response.getCodifiche(TipoLegame.class);
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_CONTO, listaTipoContoDaSessione);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CATEGORIA_CESPITI, listaCategoriaCespitiDaSessione);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_LEGAME, listaTipoLegameDaSessione);
		}
		
		model.setListaTipoConto(listaTipoContoDaSessione);
		model.setListaCategoriaCespiti(listaCategoriaCespitiDaSessione);
		model.setListaTipoLegame(listaTipoLegameDaSessione);
		impostaUidTipoCespitiEGenerico();
		
	}


	/**
	 * Caricamento della lista del codice bilancio.
	 * 
	 * @param uidClasse l'uid della classe
	 */
	protected void caricaListaCodiceBilancio(Integer uidClasse){
		final String methodName = "caricaListaCodiceBilancio";
		List<CodiceBilancio> lista = sessionHandler.getParametro(getBilSessionParameterListaCodiciBilancio());
		if(lista == null || lista.isEmpty()){
			LeggiTreeCodiceBilancio reqLTCB = model.creaRequestLeggiTreeCodiceBilancio(uidClasse);
			LeggiTreeCodiceBilancioResponse resLTCB = contoService.leggiTreeCodiceBilancio(reqLTCB);
			if(resLTCB.hasErrori() && resLTCB.verificatoErrore(ErroreCore.ENTITA_NON_TROVATA)){
				log.debug(methodName, "Nessun codice trovato");
				addMessaggio(ErroreCore.ENTITA_NON_TROVATA.getErrore("codifica di bilancio", "relativa alla classe piano " + uidClasse));
			} else if(resLTCB.hasErrori()) {
				log.info(methodName, createErrorInServiceInvocationString(reqLTCB, resLTCB));
				addErrori(resLTCB);
				return;
			}
			lista = resLTCB.getTreeCodiciBilancio();
			sessionHandler.setParametro(getBilSessionParameterListaCodiciBilancio(), lista);
		}
		model.setListaCodiceBilancio(lista);
	}

	/**
	 * Ottengo l'uid del tipoConto Cespiti, per abilitare il campo CATEGORIA CESPITI
	 */
	protected void impostaUidTipoCespitiEGenerico() {
		if(model.getListaTipoConto() == null || model.getListaTipoConto().isEmpty()){
			return;
		}
		for(TipoConto tipoConto : model.getListaTipoConto()){
			if(BilConstants.CODICE_CONTO_CESPITI.getConstant().equals(tipoConto.getCodice())){
				model.setUidTipoCespiti(tipoConto.getUid());
			}
			if(BilConstants.CODICE_CONTO_GENERICO.getConstant().equals(tipoConto.getCodice())){
				model.setUidTipoGenerico(tipoConto.getUid());
			}
			
		}
		
	}

	/**
	 * Ricerca sintetica del conto collegato passato in input e vaidazione del risultato trovato
	 */
	protected void ricercaEValidazioneContoCollegato() {
		if(model.getConto().getContoCollegato() == null 
				|| model.getConto().getContoCollegato().getCodice() == null
				||  StringUtils.isBlank(model.getConto().getContoCollegato().getCodice())){
			return;
		}
		RicercaSinteticaConto reqRSC = model.creaRequestRicercaContoCollegato();
		logServiceRequest(reqRSC);
		RicercaSinteticaContoResponse resRSC = contoService.ricercaSinteticaConto(reqRSC);
		logServiceResponse(resRSC);
		if(resRSC.hasErrori()){
			addErrori(resRSC);
			return;
		}
		if(resRSC.getConti().isEmpty()){
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto con codice", model.getConto().getContoCollegato().getCodice()));
			return;
		}
		model.getConto().setContoCollegato(resRSC.getConti().get(0));
		
	}

	/**
	 * Ricerca sintetica del soggetto passato in input e vaidazione del risultato trovato
	 */
	protected void ricercaEValidazioneSoggetto() {
		if(model.getConto().getSoggetto() == null || model.getConto().getSoggetto().getCodiceSoggetto() == null
				|| StringUtils.isBlank(model.getConto().getSoggetto().getCodiceSoggetto())){
			return;
		}
		RicercaSoggettoPerChiave reqRS = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(reqRS);
		RicercaSoggettoPerChiaveResponse resRS = soggettoService.ricercaSoggettoPerChiave(reqRS);
		logServiceResponse(resRS);
		
		if(resRS.hasErrori()) {
			addErrori(resRS);
			return;
		}
		if(resRS.getSoggetto() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getConto().getSoggetto().getCodiceSoggetto()));
			return;
		}
		
		model.getConto().setSoggetto(resRS.getSoggetto());
	
	}
	
	/**
	 * Ricerca sintetica del piano dei conti finanziario in input e vaidazione del risultato trovato
	 */
	protected void ricercaEValidazionePDC() {
		if(model.getConto().getElementoPianoDeiConti() == null || model.getConto().getElementoPianoDeiConti().getCodice() == null ||  StringUtils.isBlank(model.getConto().getElementoPianoDeiConti().getCodice())){
			return;
		}
		LeggiElementoPianoDeiContiByCodiceAndAnno reqPDC = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno();
		logServiceRequest(reqPDC);
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse resPDC = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(reqPDC);
		logServiceResponse(resPDC);
		
		if(resPDC.hasErrori()) {
			addErrori(resPDC);
			return;
		}
		if(resPDC.getElementoPianoDeiConti() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Piano dei conti", model.getConto().getElementoPianoDeiConti().getCodice()));
			return;
		}
		checkCondition(resPDC.getElementoPianoDeiConti().getLivello() == 5, ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + resPDC.getElementoPianoDeiConti().getCodice(), "e' di livello " + resPDC.getElementoPianoDeiConti().getLivello()), true);
		model.getConto().setElementoPianoDeiConti(resPDC.getElementoPianoDeiConti());
	
	}
	

	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		FaseBilancio faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		
		if(faseBilancio == null){
			RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
			RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
			faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
			sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
		}
		
		if(FaseBilancio.PREVISIONE.equals(faseBilancio) || FaseBilancio.PLURIENNALE.equals(faseBilancio)) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}


}
