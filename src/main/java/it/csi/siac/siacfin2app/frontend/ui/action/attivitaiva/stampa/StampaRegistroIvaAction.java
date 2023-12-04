/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.stampa;

import java.util.Arrays;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa.StampaRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per la stampa del registro iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.2 - 05/08/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StampaRegistroIvaAction extends GenericStampaIvaAction<StampaRegistroIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 290636867681512487L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricamentoListe();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		
		caricaValoriDefault();
		return super.execute();
	}
	
	/**
	 * Mostra la pagina di successo per avvenuta stampa
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String mostraPaginaSuccesso() {
		impostaMessaggioStampaPresaInCarico();
		return SUCCESS;
	}
	
	@Override
	public String effettuaStampa() {
		final String methodName = "effettuaStampa";

		StampaRegistroIva request = model.creaRequestStampaRegistroIva();
		logServiceRequest(request);
		StampaRegistroIvaResponse response = stampaIvaService.stampaRegistroIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio StampaRegistroIva");
			addErrori(response);
			addErrore(ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DEL_REGISTRO_IVA.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio StampaRegistroIva avvenuta con successo");
		impostaMessaggioStampaPresaInCarico();
		return SUCCESS;
	}
	
	/**
	 * Effettua la stampa multipla
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaStampaMultipla() {
		final String methodName = "effettuaStampaMultipla";
		
		if(model.isFlagOnlyCheckRegistroIva()) {
			List<RegistroIva> registriIva = sessionHandler.getParametro(BilSessionParameter.LISTA_REGISTRO_IVA);
			log.debug(methodName, "Test di invio");
			StampaRegistriIva request = model.creaRequestStampaRegistriIva(registriIva, Boolean.TRUE);
			StampaRegistriIvaResponse response = stampaIvaService.stampaRegistriIva(request);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.debug(methodName, "Errore nell'invocazione del servizio StampaRegistroIva");
				addErrori(response);
				addErrore(ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DEL_REGISTRO_IVA.getErrore());
				return INPUT;
			}
			
			if(response.getRegistriIvaSenzaErrori().isEmpty()){
				String msg = "nessun registro soddisfa le condizioni di attivazione della stampa.";
				log.debug(methodName, msg);
				addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
				addErrore(ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DEL_REGISTRO_IVA.getErrore());
				return INPUT;
			}
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_REGISTRO_IVA_STAMPA_MULTIPLA, response.getRegistriIvaSenzaErrori());
			
			addMessaggi(response.getMessaggi());
			if(hasMessaggi()){
				return INPUT;
			}
		}
		
		List<RegistroIva> registriIva = sessionHandler.getParametro(BilSessionParameter.LISTA_REGISTRO_IVA_STAMPA_MULTIPLA);
		StampaRegistriIva request = model.creaRequestStampaRegistriIva(registriIva, Boolean.FALSE);
		StampaRegistriIvaResponse response = stampaIvaService.stampaRegistriIva(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio StampaRegistroIva");
			addErrori(response);
			addErrore(ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DEL_REGISTRO_IVA.getErrore());
			return INPUT;
		}

				
		log.debug(methodName, "Invocazione del servizio StampaRegistriIva avvenuta");

		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaStampaMultipla()}.
	 */
	public void validateEffettuaStampaMultipla() {
		if (!model.isFlagOnlyCheckRegistroIva()) {
			return;
		}
		
		// Tento di ricaricare la lista dei registri iva e dei periodi
		caricaListaRegistroIva();
		caricaListaPeriodo();
		
		// Validazione logica
		checkNotNull(model.getTipoRegistroIva(), "Tipo registro iva");
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "Gruppo attivita' iva");
		checkNotNull(model.getTipoChiusura(), "Tipo di chiusura");
		checkNotNull(model.getPeriodo(), "Periodo");
		checkNotNull(model.getTipoStampa(), "Tipo stampa");
		
		// Carico il messaggio di elaborazione non possibile
		checkCondition(!hasErrori(), ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DEL_REGISTRO_IVA.getErrore());
	}

	
	@Override
	public void validateEffettuaStampa() {
		// Tento di ricaricare la lista dei registri iva e dei periodi
		caricaListaRegistroIva();
		caricaListaPeriodo();
		
		// Validazione logica
		checkNotNull(model.getTipoRegistroIva(), "Tipo registro iva");
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "Gruppo attivita' iva");
		checkNotNull(model.getTipoChiusura(), "Tipo di chiusura");
		checkNotNull(model.getPeriodo(), "Periodo");
		checkNotNullNorInvalidUid(model.getRegistroIva(), "Registro iva");
		checkNotNull(model.getTipoStampa(), "Tipo stampa");
		
		// Carico il messaggio di elaborazione non possibile
		checkCondition(!hasErrori(), ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DEL_REGISTRO_IVA.getErrore());
	}
	
	/**
	 * Carica le varie liste da servizio.
	 */
	private void caricamentoListe() {
		caricaListaTipoRegistroIva();
		caricaListaGruppoAttivitaIva();
		caricaListaTipoChiusura();
		caricaListaTipoStampa();
	}
	
	/**
	 * Carica la lista dei tipi di registro iva nel model.
	 */
	private void caricaListaTipoRegistroIva() {
		// Ricarico l'intera lista dall'enum
		List<TipoRegistroIva> lista = Arrays.asList(TipoRegistroIva.values());
		model.setListaTipoRegistroIva(lista);
	}
	
	/**
	 * Carica la lista del RegistroIva a partire dal servizio.
	 */
	private void caricaListaRegistroIva() {
		GruppoAttivitaIva gai = model.getGruppoAttivitaIva();
		TipoRegistroIva tri = model.getTipoRegistroIva();
		if(gai != null && gai.getUid() != 0 && tri != null) {
			RicercaRegistroIva request = model.creaRequestRicercaRegistroIva(gai, tri);
			logServiceRequest(request);
			RicercaRegistroIvaResponse response = registroIvaService.ricercaRegistroIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			model.setListaRegistroIva(response.getListaRegistroIva());
		}
	}
	
	/**
	 * Caricamento dei valori di default.
	 */
	private void caricaValoriDefault() {
		model.setTipoStampa(TipoStampa.BOZZA);
	}
	
}
