/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.RicercaPianoDeiContiModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigliResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 * @param <M> la tipizzazione del model
 */
public abstract class RicercaPianoDeiContiAction<M extends RicercaPianoDeiContiModel> extends GenericBilancioAction<M> {

	private static final long serialVersionUID = -6579757634075287520L;

	/** Service per il conto */
	@Autowired protected transient ContoService contoService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	/**
	 * @return il parametro di sessione corrispondente alla lista classi piano
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaClassiPiano();
	
	
	/**
	 * Validazione della ricerca del conto.
	 */
	protected void validaRicerca(){
		if(model.getConto().getPianoDeiConti()== null || model.getConto().getPianoDeiConti().getClassePiano()== null 
				||  model.getConto().getPianoDeiConti().getClassePiano().getUid() == 0){
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classe piano dei conti"));
		}
		if(StringUtils.isBlank(model.getConto().getCodice()) && StringUtils.isBlank(model.getConto().getCodiceInterno())){
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("valorizzare codice conto o codifica interna"));
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		return response;
	}
	

	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassi() {
		final String methodName = "caricaListaClassi";
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassiPiano());
		if(listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche("ClassePiano" + "_" + model.getAmbito().getSuffix());
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(RicercaCodifiche.class, response);
				log.warn(methodName, errorMsg);
				addErrori(response);
				
			}
			
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(getBilSessionParameterListaClassiPiano(), listaClassePiano);
		}
		model.setListaClassi(listaClassePiano);
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaTitoli() {
		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		List<TitoloEntrata> listaTE = response.getClassificatoriTitoloEntrata();
		
		if(!listaTE.isEmpty()){
			model.setListaTitoloEntrata(listaTE);
		}else{
			model.setListaTitoloEntrata(new ArrayList<TitoloEntrata> ());
		}
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		
		if(!listaTS.isEmpty()){
			model.setListaTitoloSpesa(listaTS);
		}else{
			model.setListaTitoloSpesa(new ArrayList<TitoloSpesa> ());
		}
	}
	
	/**
	 * Impostazione dei dati nel model.
	 * 
	 * @param resRSCF la response della ricerca
	 */
	protected void impostaDatiNelModel(RicercaSinteticaContoFigliResponse resRSCF) {
		model.setConto(resRSCF.getContoPadre());
		Boolean figliPresenti = resRSCF.getContiFiglio() != null && !resRSCF.getContiFiglio().isEmpty();
		model.setFigliPresenti(figliPresenti);
		model.setIsGestioneConsentita(isGestioneConsentita());
		model.setIsTabellaVisibile(Boolean.TRUE);
		model.setGerarchiaConti(costruisciListaGerarchiaConti(resRSCF.getContoPadre()));
	}
	
	

	/**
	 * Controlla se la gestione sia consentita.
	 * 
	 * @return <code>true</code> se la gestione &eacute; consentita; <code>false</code> altrimenti
	 */
	protected boolean isGestioneConsentita() {
		return false;
	}

	
	/**
	 * Costruisce una lista contenente la gerarchia dei conti a partire dal nodo del piano dei conti fino al conto passato come parametro
	 * 
	 * @param contoPadre il conto padre da cui costruire la lista della gerarchia
	 * @return la lista di conti ottenuta
	 */
	protected List<String> costruisciListaGerarchiaConti(Conto contoPadre) {
		List<String> lista = new ArrayList<String>();
		Conto conto = contoPadre;
		log.debug("conto iniziale: ", conto.getCodice());
		while(conto != null){
			log.debug("conto: ", conto.getCodice());
			lista.add(conto.getDescrizione());
			conto = conto.getContoPadre();
		}
		return lista;
	}
	
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
		
		if(FaseBilancio.PREVISIONE.equals(faseBilancio) || FaseBilancio.PLURIENNALE.equals(faseBilancio)) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	

}
