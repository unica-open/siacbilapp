/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.gruppoattivita;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita.InserisciGruppoAttivitaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

/**
 * Classe di action per l'inserimento del Gruppo Attivita Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 28/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciGruppoAttivitaIvaAction extends GenericGruppoAttivitaIvaAction<InserisciGruppoAttivitaIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoChiusura();
		caricaListaTipoAttivita();
		caricaListaAttivitaIva();
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
	public String inserimento() {
		final String methodName = "inserimento";
		
		InserisceGruppoAttivitaIvaEProrata request = model.creaRequestInserisceGruppoAttivitaIvaEProrata();
		logServiceRequest(request);
		InserisceGruppoAttivitaIvaEProrataResponse response = gruppoAttivitaIvaService.inserisceGruppoAttivitaIvaEProrata(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Fornisco l'errore a video ed esco
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		GruppoAttivitaIva gai = response.getGruppoAttivitaIva();
		log.debug(methodName, "Inserimento Gruppo Attivita Iva andato a buon fine: inserito gruppo con uid " + gai.getUid());
		
		model.impostaDati(gai);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca delle associazioni con le Attivita Iva siano validi.
	 */
	public void validateInserimento() {
		checkNotNull(model.getGruppoAttivitaIva(), "Gruppo attivita Iva", true);
		
		GruppoAttivitaIva gai = model.getGruppoAttivitaIva();
		checkNotNullNorEmpty(gai.getCodice(), "Codice");
		checkNotNullNorEmpty(gai.getDescrizione(), "Descrizione");
		
		checkNotNull(model.getTipoAttivita(), "Tipo attivita'");
		checkCondition(!gai.getListaAttivitaIva().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Attivita'"));
		
		// Deve esserci almeno un'attivita compilata
		for(AttivitaIva attivitaIva : gai.getListaAttivitaIva()) {
			checkNotNullNorInvalidUid(attivitaIva, "Attivita'");
		}
		
		checkNotNull(model.getTipoChiusura(), "Tipo chiusura");
		
		// La pro-rata e' obbligatoria
		checkNotNull(model.getProRataEChiusuraGruppoIva(), "% Pro-rata", true);
		ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva = model.getProRataEChiusuraGruppoIva();
		
		checkNotNull(proRataEChiusuraGruppoIva.getPercentualeProRata(), "% Pro-rata");
		
		// L'iva a credito deve essere zero o negativa
		checkCondition(proRataEChiusuraGruppoIva.getIvaPrecedente() == null || proRataEChiusuraGruppoIva.getIvaPrecedente().signum() <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Iva a credito precedente", ": non deve essere positiva"));
	}
	
	/**
	 * Redirige verso l'aggiornamento del GruppoAttivitaIva.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		log.debug(methodName, "Redirezione verso l'aggiornamento del gruppo con uid " + model.getUidGruppoAttivitaIva());
		return SUCCESS;
	}
	
}
