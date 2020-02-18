/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.tipoonere.InserisciTipoOnereModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di action per l'inserimento del TipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciTipoOnereAction extends GenericTipoOnereAction<InserisciTipoOnereModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7305563104937245934L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		try {
			caricamentoListe();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento delle liste: " + wsife.getMessage());
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("caricamento delle liste delle codifiche non possibile"));
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}

	/**
	 * Carica le liste per la visualizzazione delle pagine.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nel caricamento delle liste
	 */
	private void caricamentoListe() throws WebServiceInvocationFailureException {
		// Carica la Natura Onere
		caricaNaturaOnere();
		caricaCausale770();
		caricaCodiceSommaNonSoggetta();
		caricaAttivitaOnere();
		caricaListaTipoIvaSplitReverse();
	}

	/**
	 * Inserimetno del tipoOnere.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		InserisceTipoOnere request = model.creaRequestInserisceTipoOnere();
		logServiceRequest(request);
		InserisceTipoOnereResponse response = tipoOnereService.inserisceTipoOnere(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		final TipoOnere tipoOnere = response.getTipoOnere();
		log.debug(methodName, "TipoOnere inserito correttamente con uid " + tipoOnere.getUid() + ". Redirezione verso l'aggiornamento");
		model.setTipoOnere(tipoOnere);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #inserimento()}.
	 */
	public void validateInserimento() {
		TipoOnere to = model.getTipoOnere();
		// Se fallisce, esco subito
		checkCondition(to != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Onere"), true);
		
		checkNotNullNorInvalidUid(to.getNaturaOnere(), "Natura Onere");
		checkNotNullNorEmpty(to.getCodice(), "Codice");
		checkNotNullNorEmpty(to.getDescrizione(), "Descrizione");
		
		checkCondition(to.getAliquotaCaricoSoggetto() == null || to.getAliquotaCaricoSoggetto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico soggetto", ": deve essere positiva"));
		checkCondition(to.getAliquotaCaricoEnte() == null || to.getAliquotaCaricoEnte().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico ente", ": deve essere positiva"));
		
		// La somma delle aliquote NON deve superare il 100
		checkCondition(to.getAliquotaCaricoSoggetto() == null
				|| to.getAliquotaCaricoEnte() == null
				|| to.getAliquotaCaricoSoggettoNotNull().add(to.getAliquotaCaricoEnteNotNull()).compareTo(BilUtilities.BIG_DECIMAL_ONE_HUNDRED) <= 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota", ":la somma delle aliquote non deve superare 100"));
		
		// Lotto L
		controlloSplitReverse();
	}

}
