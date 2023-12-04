/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.InserisciRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di action per l'inserimento del Registro Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 28/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciRegistroIvaAction extends GenericRegistroIvaAction<InserisciRegistroIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
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
	 * Inserisce il Gruppo Attivita Iva
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		
		InserisceRegistroIva request = model.creaRequestInserisceRegistroIva();
		logServiceRequest(request);
		controllaFlagLiquidazioneIva(request);
		InserisceRegistroIvaResponse response = registroIvaService.inserisceRegistroIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Fornisco l'errore a video ed esco
			log.info(methodName, "Errore nell'invocazione del servizio di InserisceRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		RegistroIva ri = response.getRegistroIva();
		log.debug(methodName, "Inserimento Registro Iva andato a buon fine: uid registro inserito: " + ri.getUid());
		
		model.impostaDati(ri);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	private void controllaFlagLiquidazioneIva(InserisceRegistroIva request) {
		// SIAC-6276
		if (request.getRegistroIva().getFlagLiquidazioneIva() == null) {
			request.getRegistroIva().setFlagLiquidazioneIva(true);
		}
	}

	/**
	 * Controlla se i dati forniti per la ricerca delle associazioni con le Attivita Iva siano validi.
	 */
	public void validateInserimento() {
		RegistroIva ri = model.getRegistroIva();
		
		checkNotNullNorEmpty(ri.getCodice(), "Codice");
		checkNotNullNorEmpty(ri.getDescrizione(), "Descrizione");
		
		// Il gruppo deve essere selezionato
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "Gruppo Attivita Iva");
		// Il tipo registro deve essere indicato
		checkNotNull(model.getTipoRegistroIva(), "Tipo Registro Iva");
	}
	
	/**
	 * Redirige verso l'aggiornamento del Registro Iva.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		log.debug(methodName, "Redirezione verso l'aggiornamento del registro con uid " + model.getUidRegistroIva());
		return SUCCESS;
	}
	
}
