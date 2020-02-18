/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe;

import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.CassaEconomaleBaseAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe.RicercaStampeCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la ricerca delle stampe cassa economale.
 * 
 * @author Valentina Triolo
 * @version 1.0.0 - 31/03/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaStampeCassaEconomaleAction extends CassaEconomaleBaseAction<RicercaStampeCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2090534180054254871L;
	
	@Autowired private transient StampaCassaEconomaleService stampaCassaEconomaleService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricamentoListe();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Ricerca la stampa iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		RicercaStampeCassaEconomale request = model.creaRequestRicercaStampeCassaEconomale();
		logServiceRequest(request);
		RicercaStampeCassaEconomaleResponse response = stampaCassaEconomaleService.ricercaStampeCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio RicercaStampeCassaEconomale");
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato a fronte della ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio RicercaStampeCassaEconomale avvenuta con successo");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_STAMPE_CEC, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_STAMPE_CEC, response.getListaStampe());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		// SIAC-4799
		sessionHandler.setParametro(BilSessionParameter.TIPO_DOCUMENTO_STAMPA_CEC, model.getStampeCassaFile().getTipoDocumento());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricerca()}.
	 */
	public void validateRicerca() {
		StampeCassaFile sc = model.getStampeCassaFile();
		checkNotNull(sc, "stampa cassa economale", true);
		checkNotNull(sc.getTipoDocumento(), "Tipo stampa", true);
		checkNotNull(model.getCassaEconomale(), "Cassa economale", true);
	}
	
	/**
	 * Carica le liste.
	 */
	private void caricamentoListe() {
		caricaListaCasseEconomali();
		caricaListaTipoStampa();
		caricaListaTipoDocumento();
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
		
		if(listaCassaEconomale.size() == 1) {
			// Imposto la cassa nel model
			model.setCassaEconomale(listaCassaEconomale.get(0));
		}
	}

	/**
	 * Caricamento della lista del tipoStampa.
	 */
	private void caricaListaTipoStampa() {
		model.setListaTipoStampa(Arrays.asList(TipoStampa.values()));
	}
	
	/**
	 * Caricamento della lista del tipoDocumento.
	 */
	private void caricaListaTipoDocumento() {
		model.setListaTipoDocumento(Arrays.asList(TipoDocumento.values()));
	}
	
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		
		if(!( FaseBilancio.GESTIONE.equals(faseBilancio) || FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) || FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO
				.equals(faseBilancio))) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
