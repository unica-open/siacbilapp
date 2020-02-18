/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.GenericCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class GenericTipoBeneAction.
 *
 * @param <M> the generic type
 */
public class GenericCespiteAction<M extends GenericCespiteModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -2591461680086672733L;
	
	/** IL service per il cespite */
	@Autowired protected transient CespiteService cespiteService;
	@Autowired private transient ClassificazioneCespiteService classificazioneCespiteService;
	
	/**
	 * Carica liste.
	 * @param excludeAnnullati escludere gli annullati
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	protected void caricaListe(boolean excludeAnnullati) throws WebServiceInvocationFailureException {
		caricaTipoBene(excludeAnnullati);
	}
	
	/**
	 * Caricamento del tipo bene
	 * @param excludeAnnullati escludere gli annullati
	 */
	private void caricaTipoBene(boolean excludeAnnullati) {
		final String methodName = "caricaTipoBene";
		RicercaSinteticaTipoBeneCespite req = model.creaRequestRicercaSinteticaTipoBene();
		RicercaSinteticaTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			model.setListaTipoBeneCespite(new ArrayList<TipoBeneCespite>());
			return;
		}
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun tipo bene cespite");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			model.setListaTipoBeneCespite(new ArrayList<TipoBeneCespite>());
			return;
		}
		List<TipoBeneCespite> listaTipoBeneCespite = excludeAnnullati ? filtraTipiBeneValidi(res.getListaTipoBeneCespite()) : res.getListaTipoBeneCespite();
		model.setListaTipoBeneCespite(listaTipoBeneCespite);
		
		
	}
	
	/**
	 * Filtra tipi bene validi.
	 *
	 * @param listaTipoBeneDaFiltrare the lista tipo bene cespite
	 * @return the list
	 */
	private List<TipoBeneCespite> filtraTipiBeneValidi(ListaPaginata<TipoBeneCespite> listaTipoBeneDaFiltrare) {
		List<TipoBeneCespite> filtrati = new ArrayList<TipoBeneCespite>();
		for (TipoBeneCespite tipoBeneCespite : listaTipoBeneDaFiltrare) {
			if(tipoBeneCespite.getCategoriaCespiti() !=null && !Boolean.TRUE.equals(tipoBeneCespite.getAnnullato()) && !Boolean.TRUE.equals(tipoBeneCespite.getCategoriaCespiti().getAnnullato())) {
				filtrati.add(tipoBeneCespite);
			}
		}
		return filtrati;
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
	 * Controlla tutti i campi del cespite
	 * @param cespite il cespite da controllare
	 */
	protected void checkAllCampiCespite(Cespite cespite) {
		checkNotNull(cespite, "cespite");
		checkNotNullNorEmpty(cespite.getCodice(), "codice tipo bene");
		checkNotNullNorEmpty(cespite.getDescrizione(), "descrizione tipo bene");
		checkCondition(model.idEntitaPresente(cespite.getTipoBeneCespite()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo bene"));
		checkNotNull(cespite.getClassificazioneGiuridicaCespite(), "classificazione giuridica");
		checkNotNull(cespite.getDataAccessoInventario(), "data ingresso in inventario");
		checkNotNull(cespite.getValoreIniziale(), "valore iniziale");
	}

	/**
	 * Ottieni ricerca dettaglio tipo bene cespite response.
	 *
	 * @return the ricerca dettaglio tipo bene cespite response
	 * @throws FrontEndBusinessException the front end business exception
	 */
	protected RicercaDettaglioCespiteResponse ottieniRicercaDettaglioCespiteResponse() throws FrontEndBusinessException {
		if(model.getUidCespite() == 0) {
			throw new FrontEndBusinessException("Impossibile reperire il cespite, uid non fornito.");
		}
		RicercaDettaglioCespite req = model.creaRequestRicercaDettaglioCespite();
		RicercaDettaglioCespiteResponse res = cespiteService.ricercaDettaglioCespite(req);
		
		return res;
	}

	
	
	
}
