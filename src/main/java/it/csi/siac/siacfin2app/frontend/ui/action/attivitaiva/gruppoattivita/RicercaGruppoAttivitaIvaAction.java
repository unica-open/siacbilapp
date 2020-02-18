/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.gruppoattivita;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita.RicercaGruppoAttivitaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

/**
 * Classe di action per la ricerca del Gruppo Attivita Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaGruppoAttivitaIvaAction extends GenericGruppoAttivitaIvaAction<RicercaGruppoAttivitaIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoAttivita();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Controllo se il CDU sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Inserisce il Gruppo Attivita Iva
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		
		RicercaSinteticaGruppoAttivitaIva request = model.creaRequestRicercaSinteticaGruppoAttivitaIva();
		logServiceRequest(request);
		RicercaSinteticaGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaSinteticaGruppoAttivitaIva(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Fornisco l'errore a video ed esco
			log.info(methodName, "Errore nell'invocazione del servizio di RicercaGruppoAttivitaIva");
			addErrori(response);
			return INPUT;
		}
		
		ListaPaginata<GruppoAttivitaIva> lista = response.getListaGruppoAttivitaIva();
		int totaleElementi = lista.getTotaleElementi();
		
		if(totaleElementi == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale gruppi: " + totaleElementi);
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_GRUPPO_ATTIVITA_IVA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_GRUPPO_ATTIVITA_IVA, lista);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca siano validi.
	 */
	public void validateRicerca() {
		GruppoAttivitaIva gai = model.getGruppoAttivitaIva();
		
		if(StringUtils.isBlank(gai.getCodice()) && StringUtils.isBlank(gai.getDescrizione()) && model.getTipoAttivita() == null) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	
}
