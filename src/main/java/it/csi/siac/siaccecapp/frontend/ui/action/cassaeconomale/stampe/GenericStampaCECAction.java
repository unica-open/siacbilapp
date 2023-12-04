/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe;

import java.util.Arrays;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.CassaEconomaleBaseAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe.GenericStampaCECModel;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action generica per le stampa della CEC.
 * 
 * @author Paggio Simona,Nazha Ahmad
 * @version 1.0.0 - 10/02/2015
 * @version 1.0. - 02/03/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class GenericStampaCECAction <M extends GenericStampaCECModel> extends CassaEconomaleBaseAction<M> {

	/** per serializzazione */
	private static final long serialVersionUID = -2778146591196752725L;
	
	/** Serviz&icirc; della stampa per la cassa economale */
	@Autowired protected transient StampaCassaEconomaleService stampaCassaEconomaleService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErrori();
		caricaListaCasseEconomali();
		caricaListaTipoStampa();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}

	/**
	 * Carica la lista del tipoStampa nel model.
	 */
	protected void caricaListaTipoStampa() {
		List<TipoStampa> lista = Arrays.asList(TipoStampa.values());
		model.setListaTipoStampa(lista);
	}
	/**
	 * Caricamento della lista delle casse economali.
	 */
	private void caricaListaCasseEconomali() {
		String methodName="caricaListaCasseEconomali";
		List<CassaEconomale> listaCassaEconomale;
		try {
			listaCassaEconomale = ottieniListaCasseEconomali();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return;
		}
		
		if(listaCassaEconomale == null || listaCassaEconomale.isEmpty()) {
			Errore errore = ErroreCEC.CEC_ERR_0001.getErrore();
			addErrore(errore);
			return;
		}
		model.setListaCasseEconomali(listaCassaEconomale);
		log.debug(methodName, "Numero di casse economali: " + listaCassaEconomale.size());
		boolean unicaCassa = listaCassaEconomale.size() == 1;
		model.setUnicaCassa(Boolean.valueOf(unicaCassa));
		
		if(unicaCassa) {
			// Imposto la cassa nel model
			model.setCassaEconomale(listaCassaEconomale.get(0));
		}
	}
	/**
	 * Imposta il messaggio di presa in carico della stampa.
	 */
	protected void impostaMessaggioStampaPresaInCarico() {
		Errore errore = ErroreFin.STAMPA_PRESA_IN_CARICO.getErrore();
		addInformazione(new Informazione(errore.getCodice(), errore.getDescrizione()));
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		
		if(!(FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) || FaseBilancio.GESTIONE.equals(faseBilancio) || FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio))) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
}


