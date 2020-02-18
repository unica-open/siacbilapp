/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.dismissionecespite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.GenericDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespiteResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class GenericDismissioneCespiteAction.
 *
 * @param <M> the generic type
 */
public class GenericDismissioneCespiteAction<M extends GenericDismissioneCespiteModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -1200126028117057775L;

	/** Il service del cespite */
	@Autowired protected transient CespiteService cespiteService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient CausaleService causaleService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	

	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.GESTIONE.equals(faseBilancio) ||
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Caricamento delle list
	 * @throws WebServiceInvocationFailureException in caso di eccezione nel caricamento
	 */
	protected void caricaListe() throws WebServiceInvocationFailureException {
		caricaListaEvento();
		caricaListaTipoAtto();
		caricaListaCausale();
	}

	/**
	 * Caricamento della lista dell'evento
	 * @throws WebServiceInvocationFailureException in caso di eccezione nel caricamento dei dati
	 */
	private void caricaListaEvento() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
	
		if(listaEvento == null ) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(Evento.class);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaEvento = filtraEventi(response.getCodifiche(Evento.class));
			sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, listaEvento);
		}
		
		model.setListaEvento(listaEvento);
	}
	
	/**
	 * Carica la lista dei tipi di atto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaInSessione == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			logServiceRequest(request);
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			
			listaInSessione = response.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaInSessione);
		}
		
		model.setListaTipoAtto(listaInSessione);
	}
	
	/**
	 * Caricamento della lista delle causali.
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaCausale() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCausale";
		List<CausaleEP> listaCausaliEp = sessionHandler.getParametro(BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_INV);
		// Se non ho i dati, effettuo la ricerca

		if (listaCausaliEp == null) {
			// recuperare da servizio la lista causali
			RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
			RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);

			if (res.hasErrori()) {
				String errorMsg = createErrorInServiceInvocationString(req, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			listaCausaliEp = res.getCausali();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_INV, listaCausaliEp);
		}

		model.setListaCausaleEP(listaCausaliEp);

	}
	
	/**
	 * Filtra evento.
	 *
	 * @param listaEventoTotale the lista evento totale
	 * @return the list
	 */
	private List<Evento> filtraEventi(List<Evento> listaEventoTotale) {
		List<Evento> listaEventoFiltrato = new ArrayList<Evento>();
		if (listaEventoTotale == null) {
			return listaEventoFiltrato;
		}
			// verifica tipo causale tipo evento libera
		for (Evento e : listaEventoTotale) {
			if (e != null && e.getTipoEvento() != null
					&& !e.getTipoEvento().getListaTipoCausaleEP().isEmpty()) {
				for (TipoCausale tc : e.getTipoEvento().getListaTipoCausaleEP()) {
					if (TipoCausale.Libera.equals(tc)) {
						listaEventoFiltrato.add(e);
						break;
					}
				}
			}
		}
		return listaEventoFiltrato;
	}
	
	/**
	 * Effettua un controllo sull'atto amministrativo.
	 */
	protected void checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		
		AttoAmministrativo attoSelezionato = model.getAttoAmministrativo();
		checkNotNull(attoSelezionato, "Provvedimento", true);
		if(idEntitaPresente(attoSelezionato)) {
			model.getDismissioneCespite().setAttoAmministrativo(attoSelezionato);
			return;
		}
		checkCondition(attoSelezionato.getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno provvedimento"));
		checkCondition(attoSelezionato.getNumero() != 0 || (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0),ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero o tipo provvedimento"));
		
		if(hasErrori()) {
			log.error(methodName, "errore checkAttoAmministrativo");
			return;
		}
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return;
		}
		List<AttoAmministrativo> list = response.getListaAttiAmministrativi();
		checkCondition(!list.isEmpty(), ErroreCore.NESSUN_DATO_REPERITO.getErrore(), true);
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getStrutturaAmministrativoContabile(), true);
		// Ho un unico atto
		AttoAmministrativo attoAmministrativo = list.get(0);
		model.getDismissioneCespite().setAttoAmministrativo(attoAmministrativo);
		// Imposto i dati nel model
		model.setAttoAmministrativo(attoAmministrativo);
		model.setTipoAtto(attoAmministrativo.getTipoAtto());
		model.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		
	}
	

	/**
	 * Ottieni ricerca dettaglio tipo bene cespite response.
	 *
	 * @return the ricerca dettaglio tipo bene cespite response
	 * @throws FrontEndBusinessException the front end business exception
	 */
	protected RicercaDettaglioDismissioneCespiteResponse ottieniRicercaDettaglioDismissioneCespiteResponse() throws FrontEndBusinessException {
		if(model.getUidDismissioneCespite() == 0) {
			throw new FrontEndBusinessException("Impossibile reperire il cespite, uid non fornito.");
		}
		RicercaDettaglioDismissioneCespite req = model.creaRequestRicercaDettaglioDismissioneCespite();
		RicercaDettaglioDismissioneCespiteResponse res = cespiteService.ricercaDettaglioDismissioneCespite(req);
		
		return res;
	}
	
	

	
	
	
}
