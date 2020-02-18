/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.stampa;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa.GenericStampaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action generica per le stampe dell'iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/07/2014
 * 
 * @param <M> la tipizzazione del model
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public abstract class GenericStampaIvaAction<M extends GenericStampaIvaModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 290636867681512487L;
	
	/** Serviz&icirc; del gruppo attivit&agrave; iva */
	@Autowired protected transient GruppoAttivitaIvaService gruppoAttivitaIvaService;
	/** Serviz&icirc; del registro iva */
	@Autowired protected transient RegistroIvaService registroIvaService;
	/** Serviz&icirc; della stampa iva */
	@Autowired protected transient StampaIvaService stampaIvaService;
	
	/**
	 * Effettua la stampa dell'iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String effettuaStampa();
	
	/**
	 * Validazione per il metodo effettuaStampa.
	 */
	public abstract void validateEffettuaStampa();
	
	/**
	 * Ottiene il tipo chiusura e il periodo a partire dal GruppoAttivitaIva selezionato.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniTipoChiusuraEPeriodoEIvaACreditoDaGruppo() {
		List<GruppoAttivitaIva> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_GRUPPO_ATTIVITA_IVA);
		GruppoAttivitaIva gai = ComparatorUtils.searchByUid(lista, model.getGruppoAttivitaIva());
		if(gai == null || gai.getTipoChiusura() == null) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("caricamento dati non corretti da sistema"));
			return SUCCESS;
		}
		
		model.setIvaACredito(gai.getIvaPrecedente());
		// Prendo gli altri dati dal tipo di chiusura
		TipoChiusura tc = gai.getTipoChiusura();
		model.setTipoChiusura(tc);
		model.setListaPeriodo(tc.getPeriodi());
		
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// La fase di bilancio non deve essere PLURIENNALE, PREVISIONE ne' CHIUSO
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Carica la lista del GruppoAttivitaIva a partire dal servizio.
	 */
	protected void caricaListaGruppoAttivitaIva() {
		List<GruppoAttivitaIva> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_GRUPPO_ATTIVITA_IVA);
		if(lista == null) {
			// Non ho ancora la lista in sessione: la carico dal servizio
			RicercaGruppoAttivitaIva request = model.creaRequestRicercaGruppoAttivitaIva();
			logServiceRequest(request);
			RicercaGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaGruppoAttivitaIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			lista = response.getListaGruppoAttivitaIva();
			sessionHandler.setParametro(BilSessionParameter.LISTA_GRUPPO_ATTIVITA_IVA, lista);
		}
		model.setListaGruppoAttivitaIva(lista);
	}
	
	/**
	 * Carica la lista del tipoChiusura nel model.
	 */
	protected void caricaListaTipoChiusura() {
		List<TipoChiusura> lista = Arrays.asList(TipoChiusura.values());
		model.setListaTipoChiusura(lista);
	}
	
	/**
	 * Carica la lista del periodo.
	 */
	protected void caricaListaPeriodo() {
		TipoChiusura tc = model.getTipoChiusura();
		// Se non ho una chiusura, non posso avere i periodi
		if(tc != null) {
			model.setListaPeriodo(tc.getPeriodi());
		}
	}
	
	/**
	 * Carica la lista del tipoStampa nel model.
	 */
	protected void caricaListaTipoStampa() {
		List<TipoStampa> lista = Arrays.asList(TipoStampa.values());
		model.setListaTipoStampa(lista);
	}
	
	/**
	 * Imposta il messaggio di presa in carico della stampa.
	 */
	protected void impostaMessaggioStampaPresaInCarico() {
		Errore errore = ErroreFin.STAMPA_PRESA_IN_CARICO.getErrore();
		addInformazione(new Informazione(errore.getCodice(), errore.getDescrizione()));
	}
	
}
