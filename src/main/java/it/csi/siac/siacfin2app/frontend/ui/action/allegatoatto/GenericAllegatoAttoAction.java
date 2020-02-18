/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.GenericAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di Action generica per l'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/set/2014
 * @param <M> la tipizzazione del Model
 */
public class GenericAllegatoAttoAction<M extends GenericAllegatoAttoModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7810178505219844173L;
	
	/** Il nome del CDU_INSERIMENTO di inserimento */
	protected static final String CDU_INSERIMENTO = "OP-COM-insAttoAllegato";
	/** Il nome del CDU_INSERIMENTO di associazione documenot */
	protected static final String CDU_ASSOCIAZIONE_DOCUMENTO = "OP-COM-insAttoAllegato_Documento";
	/** Il nome del CDU_INSERIMENTO di associazione movimento */
	protected static final String CDU_ASSOCIAZIONE_MOVIMENTO = "OP-COM-insAttoAllegato_Movimento";
	/** Il nome del CDU_INSERIMENTO di convalida allegato */
	protected static final String CDU_CONVALIDA_ALLEGATO = "OP-COM-convDet";
	/** Il nome del CDU_INSERIMENTO di convalida provvisorio */
	protected static final String CDU_CONVALIDA_PROVVISORIO = "OP-COM-convDetQuietanza";
	/** Il nome del CDU_INSERIMENTO di riportare allo stato completato un allegato Atto*/
	protected static final String CDU_RICOMPLETA_ALLEGATO = "OP-COM-RiComplDet";
	
	
	/** Serviz&icirc; dell'allegato atto */
	@Autowired protected transient AllegatoAttoService allegatoAttoService;
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	
	/**
	 * Controlla la presenza dell'atto amministrativo e la sua unicit&agrave;.
	 */
	protected void controlloEsistenzaEUnicitaAttoAmministrativo() {
		final String methodName = "controlloEsistenzaEUnicitaAttoAmministrativo";
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		
		
		
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMessage);
			addErrori(response);
			throw new ParamValidationException(errorMessage);
		}
		checkCondition(!response.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);

		// Controllo di avere al piu' un provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getAttoAmministrativo().getStrutturaAmmContabile(), true);
		
		// Imposto l'atto nel model
		AttoAmministrativo aa = response.getListaAttiAmministrativi().get(0);
		
		model.setAttoAmministrativo(aa);
		model.setStrutturaAmministrativoContabile(aa.getStrutturaAmmContabile());
	}

	/**
	 * Controlla la presenza dell'atto amministrativo .
	 */
	//--ANTO SIAC-5660 
	protected void controlloEsistenzaAttoAmministrativo() {
		final String methodName = "controlloEsistenzaAttoAmministrativo";
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMessage);
			addErrori(response);
			throw new ParamValidationException(errorMessage);
		}
		checkCondition(!response.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);
		List<AttoAmministrativo> lista = response.getListaAttiAmministrativi();
		
		model.setAttoAmministrativo(lista.get(0));		
		model.setStrutturaAmministrativoContabile(lista.get(0).getStrutturaAmmContabile());
		
		model.setListaAttoAmministrativo(lista);		
	}

	/**
	 * Controlla la presenza dell'elenco e la sua unicit&agrave;.
	 */
	protected void controlloEsistenzaEUnicitaElencoDocumentiAllegato() {
		final String methodName = "controlloEsistenzaEUnicitaElencoDocumentiAllegato";
		RicercaElenco request = model.creaRequestRicercaElenco();
		logServiceRequest(request);
		RicercaElencoResponse response = allegatoAttoService.ricercaElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMessage);
			addErrori(response);
			throw new ParamValidationException(errorMessage);
		}
		checkCondition(response.getTotaleElementi() > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Elenco",model.getElencoDocumentiAllegato().getAnno() + "/" + model.getElencoDocumentiAllegato().getNumero()), true);
		checkCondition(response.getTotaleElementi() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Elenco"), true);
		// Imposto il dato nel model
		model.setElencoDocumentiAllegato(response.getElenchiDocumentiAllegato().get(0));
	}

	/**
	 * Carica la lista dei tipi di atto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<TipoAtto> listaFiltrataInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_FILTRATA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaInSessione == null || listaFiltrataInSessione == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			logServiceRequest(request);
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoAtto");
			}
			
			listaInSessione = response.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaInSessione);
			listaFiltrataInSessione = new ArrayList<TipoAtto>();
			for(TipoAtto tipo : listaInSessione){
				if(!Boolean.TRUE.equals(tipo.getProgressivoAutomatico())){
					listaFiltrataInSessione.add(tipo);
				}
			}
			sessionHandler.setParametro(BilSessionParameter.LISTA_FILTRATA_TIPO_ATTO_AMMINISTRATIVO, listaFiltrataInSessione);
		}
		model.setListaFiltrataTipoAtto(listaFiltrataInSessione);
		model.setListaTipoAtto(listaInSessione);
	}
	
	/**
	 * Carico la lista degli stati operativo dell'elenco documenti.
	 */
	protected void caricaListaStatoOperativoElencoDocumenti() {
		model.setListaStatoOperativoElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.values()));
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}

