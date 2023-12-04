/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.tipobenecespite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.model.ContoTipoBeneSelector;
import it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite.GenericTipoBeneModel;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class GenericTipoBeneAction.
 *
 * @param <M> the generic type
 */
public class GenericTipoBeneAction<M extends GenericTipoBeneModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -2591461680086672733L;

	/** Servizio per la classificazione cespite */
	@Autowired protected transient ClassificazioneCespiteService classificazioneCespiteService;
	@Autowired private transient ContoService contoService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient CausaleService causaleService;
	
	/**
	 * Caricamento delle liste
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void caricaListe() throws WebServiceInvocationFailureException {
		caricaCodifiche();
		caricaCategoria();
		caricaListaCausale();
		caricaListaTitoli();
	}
	
	/**
	 * Caricamento della categoria
	 */
	private void caricaCategoria() {
		RicercaSinteticaCategoriaCespiti req = model.creaRequestRicercaSinteticaCategoriaCespiti(Boolean.TRUE);
		RicercaSinteticaCategoriaCespitiResponse res = classificazioneCespiteService.ricercaSinteticaCategoriaCespiti(req);
		if(res.hasErrori()){
			addErrori(res);
			model.setListaCategoriaCespiti(new ArrayList<CategoriaCespiti>());
			return;
		}
		model.setListaCategoriaCespiti(res.getListaCategoriaCespiti());
	}

	/**
	 * Caricamento delle codifiche
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void caricaCodifiche() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
	
		if(listaEvento == null || listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(Evento.class, "ClassePiano" + "_" + Ambito.AMBITO_FIN.getSuffix());
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaCodifiche.class, response));
			}
			listaEvento = filtraEventoPerTipoBene(response.getCodifiche(Evento.class));
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN, listaClassePiano);
			sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, listaEvento);
		}
		
		model.setListaEvento(listaEvento);
		model.setListaClassi(listaClassePiano);
	}
	
	/**
	 * Check conti tipo bene.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 */
	protected void checkContiTipoBene(TipoBeneCespite tipoBeneCespite) {
		checkConto(model.getTipoBeneCespite().getContoPatrimoniale(), model.getContoPatrimonialeTipoBeneSelector());
		checkUnivocitaContoPatrimoniale(model.getTipoBeneCespite().getContoPatrimoniale());
		checkConto(model.getTipoBeneCespite().getContoAmmortamento(), model.getContoAmmortamentoTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoFondoAmmortamento(), model.getContoFondoAmmortamentoTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoIncremento(), model.getContoIncrementoTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoDecremento(), model.getContoDecrementoTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoMinusValenza(), model.getContoMinusValenzaTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoPlusvalenza(), model.getContoPlusValenzaTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoAlienazione(), model.getContoAlienazioneTipoBeneSelector());
		checkConto(model.getTipoBeneCespite().getContoDonazione(), model.getContoDonazioneTipoBeneSelector());
	}


	private void checkUnivocitaContoPatrimoniale(Conto contoPatrimoniale) {
		if(contoPatrimoniale == null || StringUtils.isBlank(contoPatrimoniale.getCodice())) {
			return;
		}
		RicercaSinteticaTipoBeneCespite req = model.creaRequestRicercaSinteticaTipoBeneCespiteByCodiceContoPatrimoniale(contoPatrimoniale);
		RicercaSinteticaTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			return;
		}
		if(res.getListaTipoBeneCespite() != null && !res.getListaTipoBeneCespite().isEmpty()) {
			String codiceTipoBene = res.getListaTipoBeneCespite().get(0).getCodice();
			addErrore(ErroreCore.ESISTONO_ENTITA_COLLEGATE.getErrore("il conto patrimoniale con codice " + contoPatrimoniale.getCodice() + " risulta essere gi&agrave; associato al tipo bene " + codiceTipoBene ));
		}
	}


	/**
	 * @param contoPatrimoniale
	 * @param contoSelector
	 */
	private void checkConto(Conto contoPatrimoniale, ContoTipoBeneSelector contoSelector) {
		Conto contoCaricato = ottieniConto(contoPatrimoniale, contoSelector);
		if(contoCaricato == null) {
			return;
		}
		String codiceClassePiano = contoSelector.getCodiceClassePiano();
		if(StringUtils.isBlank(codiceClassePiano)) {
			return;
		}
		String codiceClassePianoConto = contoCaricato.getPianoDeiConti().getClassePiano().getCodice();
		String msg = new StringBuilder().append("il conto con codice ")
				.append(contoPatrimoniale.getCodice())
				.append(" presenta la classe piano ")
				.append(codiceClassePianoConto)
				.append(" incompatibile con la classe piano richiesta per ")
				.append(contoSelector.name())
				.append(" ( ")
				.append(codiceClassePiano)
				.append(" )")
				.toString();
		checkCondition(codiceClassePiano.equals(codiceClassePianoConto), ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(msg));
	}

	/**
	 * Caricamento del conto
	 * @param contoPatrimoniale il conto patrimoniale
	 * @param contoSelector il selettore del conto tipo bene
	 * @return il conto
	 */
	protected Conto ottieniConto(Conto contoPatrimoniale, ContoTipoBeneSelector contoSelector) {
		final String methodName = "ottieniConto";
		if(contoPatrimoniale == null || StringUtils.isBlank(contoPatrimoniale.getCodice())) {
			//il conto non e' obbligatorio
			log.debug(methodName, "conto  " +  contoSelector.name() + " non selezionato: esco.");
			return null;
		}
		Conto conto = model.creaContoRicercaSelector(contoPatrimoniale.getUid(), contoPatrimoniale.getCodice(), contoSelector);
		RicercaSinteticaConto req = model.creaRequestRicercaSinteticaConto(conto);
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(req);
		if(response.hasErrori()) {
			addErrori(response);
			return null;
		}
		
		if(response.getConti() == null || response.getConti().size() != 1) {
			addErrore(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("Impossibile ottenere un record univoco per il codice " + contoPatrimoniale.getCodice()));
			return null;
		}
		
		return response.getConti().get(0);
	}

	/**
	 * @param listaEventoTotale
	 */
	private List<Evento> filtraEventoPerTipoBene(List<Evento> listaEventoTotale) {
		List<Evento> listaEventoFiltrato = new ArrayList<Evento>();
		if (listaEventoTotale == null) {
			return listaEventoFiltrato;
		}
			// verifica tipo causale tipo evento libera
		for (Evento e : listaEventoTotale) {
			if (e != null && e.getTipoEvento() != null
					&& !e.getTipoEvento().getListaTipoCausaleEP().isEmpty()) {
				for (TipoCausale tc : e.getTipoEvento().getListaTipoCausaleEP()) {
					if (TipoCausale.Libera.equals(tc) && BilConstants.CODICE_EVENTO_INVENTARIO_CONTABILITA.getConstant().equals(e.getTipoEvento().getCodice())) {
						listaEventoFiltrato.add(e);
						break;
					}
				}
			}
		}
		return listaEventoFiltrato;
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
				String errorMsg = createErrorInServiceInvocationString(RicercaSinteticaModulareCausale.class, res);
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
	 * Ottieni ricerca dettaglio tipo bene cespite response.
	 *
	 * @return the ricerca dettaglio tipo bene cespite response
	 * @throws FrontEndBusinessException the front end business exception
	 */
	protected RicercaDettaglioTipoBeneCespiteResponse ottieniRicercaDettaglioTipoBeneCespiteResponse() throws FrontEndBusinessException {
		if(model.getUidTipoBeneCespite() == 0) {
			throw new FrontEndBusinessException("Impossibile reperire il tipo bene, uid non fornito.");
		}
		RicercaDettaglioTipoBeneCespite req = model.creaRequestRicercaDettaglioTipoBeneCespite();
		RicercaDettaglioTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaDettaglioTipoBeneCespite(req);
		
		return res;
	}

	
	
	
}
