/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.categoriacespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti.GenericCategoriaCespitiModel;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * The Class GenericTipoBeneAction.
 *
 * @param <M> the generic type
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GenericCategoriaCespitiAction<M extends GenericCategoriaCespitiModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -1888771309849318851L;
	
	/** Serviz&icirc; delle codifiche */
	@Autowired private transient CodificheService codificheService;
	/** Serviz&icirc; della categoria */
	@Autowired protected transient ClassificazioneCespiteService classificazioneCespiteService;

	/**
	 * Carica lista tipo calcolo.
	 *
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	protected void caricaListaTipoCalcolo() throws WebServiceInvocationFailureException {

		//provo a reperire il dato dalla sessione
		List<CategoriaCalcoloTipoCespite> listaTipoCalcolo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_CALCOLO);
		
		//controllo se in sessione c'e' il dato
		if(listaTipoCalcolo == null) {
			//carico la lista da servizio
			listaTipoCalcolo = ottieniTipoCalcoloDaServizio();
			//metto la lista in sessione 
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_CALCOLO,listaTipoCalcolo);
		}
		
		//la lista va caricata nel model
		model.setListaTipoCalcolo(listaTipoCalcolo);
	}

	/**
	 * @throws WebServiceInvocationFailureException
	 */
	private List<CategoriaCalcoloTipoCespite> ottieniTipoCalcoloDaServizio() throws WebServiceInvocationFailureException {
		final String methodName = "ottieniTipoCalcoloDaServizio";

		//carico le codifiche da servizio
		RicercaCodifiche request = model.creaRequestRicercaCodifiche(CategoriaCalcoloTipoCespite.class);
		RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
		
		// Controllo la presenza di eventuali errori
		if(response.hasErrori()) {
			//1 - aggiungo gli errori nel model
			addErrori(response);			
			//2 - loggo e lancio l'eccezione
			String errorMsg = createErrorInServiceInvocationString(RicercaCodifiche.class, response);
			log.warn(methodName, errorMsg);
			
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		//controllo la coerenza delle codifiche ottenute
		List<CategoriaCalcoloTipoCespite> codifiche = response.getCodifiche(CategoriaCalcoloTipoCespite.class);
		if(codifiche == null || codifiche.isEmpty()) {
			throw new WebServiceInvocationFailureException("impossibile ottenere la lista di tipo calcolo");
		}
		return codifiche;
	}
	
	/**
	 * Ottieni ricerca dettaglio categoria cespiti response.
	 *
	 * @return the ricerca dettaglio categoria cespiti response
	 * @throws FrontEndBusinessException se l'uid non &eacute; fornito
	 */
	protected RicercaDettaglioCategoriaCespitiResponse ottieniRicercaDettaglioCategoriaCespitiResponse() throws FrontEndBusinessException {
		if(model.getUidCategoriaCespiti() == 0) {
			throw new FrontEndBusinessException("Impossibile reperire la categoria, uid non fornito.");
		}
		RicercaDettaglioCategoriaCespiti req = model.creaRequestRicercaDettaglioCategoriaCespiti();
		RicercaDettaglioCategoriaCespitiResponse res = classificazioneCespiteService.ricercaDettaglioCategoriaCespiti(req);
		return res;
	}
	
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
	 * valida i campi della categoria cespiti
	 */
	protected void validaCampiCategoriaCespiti() {
		CategoriaCespiti categoria = model.getCategoriaCespiti();
		checkNotNull(categoria, "categoria cespiti");
		//tutti i campi sono obbligatori: controllo che siano stati indicati
		checkNotNullNorEmpty(categoria.getCodice(), "codice");
		checkNotNullNorEmpty(categoria.getDescrizione(), "descrizione");
		checkNotNull(categoria.getAliquotaAnnua(), "aliquota annua");
		checkNotNullNorInvalidUid(categoria.getCategoriaCalcoloTipoCespite(), "tipo calcolo");
	}
	
}
