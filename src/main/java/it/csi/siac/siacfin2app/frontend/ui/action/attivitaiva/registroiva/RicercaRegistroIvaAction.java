/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.RicercaRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di action per la ricerca del Registro Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 03/06/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaRegistroIvaAction extends GenericRegistroIvaAction<RicercaRegistroIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5741394309208125518L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoRegistro();
		caricaListaGruppoAttivitaIva();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Controllo se il CDU sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Ricerca il Registro Iva
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		
		RicercaSinteticaRegistroIva request = model.creaRequestRicercaSinteticaRegistroIva();
		logServiceRequest(request);
		RicercaSinteticaRegistroIvaResponse response = registroIvaService.ricercaSinteticaRegistroIva(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Fornisco l'errore a video ed esco
			log.info(methodName, "Errore nell'invocazione del servizio di RicercaSinteticaRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		ListaPaginata<RegistroIva> lista = response.getListaRegistroIva();
		int totaleElementi = lista.getTotaleElementi();
		
		if(totaleElementi == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale registri: " + totaleElementi);
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_REGISTRO_IVA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_REGISTRO_IVA, lista);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca siano validi.
	 */
	public void validateRicerca() {
		RegistroIva ri = model.getRegistroIva();
		
		// Controllo se i dati siano stati forniti
		boolean formValido = checkPresenzaIdEntita(model.getGruppoAttivitaIva()) ||
				checkCampoValorizzato(model.getTipoRegistroIva(), "Tipo Registro Iva") ||
				StringUtils.isNotBlank(ri.getCodice()) ||
				StringUtils.isNotBlank(ri.getDescrizione());
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	
}
