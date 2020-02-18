/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.classifgsa;

import java.util.Arrays;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.ClassificatoreGSAService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;
import it.csi.siac.siacgsaapp.frontend.ui.model.classifgsa.GestisciClassificatoreGSAModel;

/**
 * Classe di action per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class GestisciClassificatoreGSAAction extends GenericBilancioAction <GestisciClassificatoreGSAModel> {
	
	/**Per la serializzazione */
	private static final long serialVersionUID = 2235502001929012397L;
	
	//Services
	@Autowired private transient ClassificatoreGSAService classificatoreGSAService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		caricaListe();
		return SUCCESS;
	}
	
	
	private void caricaListe() {
		
		model.setStatiOperativiClassificatoreGSA(Arrays.asList(StatoOperativoClassificatoreGSA.values()));
	}


	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		//L’esercizio NON deve essere in una delle seguenti FASI  in cui la Contabilità Generale non è utilizzabile:PLURIENNALE,PREVISIONE
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = FaseBilancio.PLURIENNALE.equals(faseBilancio) || FaseBilancio.PREVISIONE.equals(faseBilancio);
				//per la fase  CHIUSO  non devono essere disponibili le azioni relative alla gestione  
				// || FaseBilancio.CHIUSO.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			//lancio un errore per impedire all'utente di effettuare l'operazione
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Validate inserisci classificatore padre.
	 */
	public void validateInserisciClassificatoreGSAPadre() {
		checkClassificatoreGSA(model.getClassificatoreGSA());
	}


	/**
	 * @param classificatoreGSADaControllare
	 */
	private void checkClassificatoreGSA(ClassificatoreGSA classificatoreGSADaControllare) {
		checkNotNull(classificatoreGSADaControllare, "classificatore di livello 1");
		checkNotNullNorEmpty(classificatoreGSADaControllare.getCodice(), "codice classificatore");
		checkNotNullNorEmpty(classificatoreGSADaControllare.getDescrizione(), "descrizione classificatore");
	}
	
	/**
	 * Prepare inserisci classificatore padre.
	 */
	public void prepareInserisciClassificatoreGSAPadre() {
		model.setClassificatoreGSA(null);
		model.setClassificatoreGSAFiglioInElaborazione(null);
		model.setUidClassificatorePadre(0);
	}
	
	/**
	 * Inserisci classificatore padre.
	 *
	 * @return the string
	 */
	public String inserisciClassificatoreGSAPadre() {
		
		model.impostaDatiPerInserimentoClassificatoreGSAPadre();
		
		InserisceClassificatoreGSAResponse response = chiamaServizioInserisceClassificatoreGSA();
		
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		model.setClassificatoreGSAPadreInElaborazione(response.getClassificatoreGSA());
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}


	/**
	 * @return
	 */
	private InserisceClassificatoreGSAResponse chiamaServizioInserisceClassificatoreGSA() {
		InserisceClassificatoreGSA req = model.creaRequestInserisceClassificatoreGSA();
		return classificatoreGSAService.inserisceClassificatoreGSA(req);
	}
	
	/**
	 * Validate inserisci classificatore padre.
	 */
	public void validateInserisciClassificatoreGSAFiglio() {
		ClassificatoreGSA classificatoreGSADaControllare = model.getClassificatoreGSA();
		checkClassificatoreGSA(classificatoreGSADaControllare);
		checkCondition(model.getUidClassificatorePadre() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("classificatore livello 1 "));
	}

	/**
	 * Prepare inserisci classificatore padre.
	 */
	public void prepareInserisciClassificatoreGSAFiglio() {
		model.setClassificatoreGSA(null);
		model.setUidClassificatorePadre(0);
	}
	
	/**
	 * Inserisci classificatore padre.
	 *
	 * @return the string
	 */
	public String inserisciClassificatoreGSAFiglio() {
		model.impostaDatiPerInserimentoClassificatoreGSAFiglio();
		
		InserisceClassificatoreGSAResponse response = chiamaServizioInserisceClassificatoreGSA();
		
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		model.setClassificatoreGSAFiglioInElaborazione(response.getClassificatoreGSA());
		return SUCCESS;
		
	}
	
	
	/**
	 * Prepare cerca classificatore GSA.
	 */
	public void prepareCercaClassificatoreGSA() {
		model.setClassificatoreGSAPadreInElaborazione(null);
	}
	
	/**
	 * Validate cerca classificxatore GSA.
	 */
	public void validateCercaClassificatoreGSA() {
//		checkNotNull(model.getClassificatoreGSA(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("classificatore").getTesto());
//		checkCondition(StringUtils.isNotBlank(model.getClassificatoreGSAPadreInElaborazione().getCodice()) ^ StringUtils.isNotBlank(model.getClassificatoreGSAPadreInElaborazione().getDescrizione()), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
	}
	
	/**
	 * Cerca classificatore GSA.
	 *
	 * @return the string
	 */
	public String cercaClassificatoreGSA() {
		final String methodName = "cercaClassificatoreGSA";
		RicercaSinteticaClassificatoreGSA req = model.creaRequestRicercaSinteticaClassificatoreGSA();
		
		RicercaSinteticaClassificatoreGSAResponse response = classificatoreGSAService.ricercaSinteticaClassificatoreGSA(req);
		
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nel servizio di ricerca del classificatore GSA");
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("classificatore gsa"));
			return INPUT;
		}
		model.setListaClassificatoriGSATrovati(response.getClassificatoriGSA());
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CLASSIFICATORE_GSA, req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CLASSIFICATORE_GSA, response.getClassificatoriGSA());
		return SUCCESS;
	}
	
	
	/**
	 * Prepare cerca classificatore GSA.
	 */
	public void prepareAnnullaClassificatoreGSA() {
		model.setClassificatoreGSA(null);
	}
	
	/**
	 * Validate cerca classificxatore GSA.
	 */
	public void validateAnnullaClassificatoreGSA() {
		checkNotNullNorInvalidUid(model.getClassificatoreGSA(), "classificatore gsa");
	}
	
	/**
	 * annulla classificatore GSA.
	 *
	 * @return the string
	 */
	public String annullaClassificatoreGSA() {
		final String methodName = "annullaClassificatoreGSA";
		AnnullaClassificatoreGSA req = model.creaRequestAgnnullaClassificatoreGSA();
		
		AnnullaClassificatoreGSAResponse response = classificatoreGSAService.annullaClassificatoreGSA(req);
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nel servizio di ricerca del classificatore GSA");
			addErrori(response);
			return INPUT;
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}
	
	
	/**
	 * Prepare aggiorna classificatore GSA.
	 */
	public void prepareAggiornaClassificatoreGSA() {
		model.setClassificatoreGSA(null);
		model.setUidClassificatorePadre(0);
	}
	
	/**
	 * Validate aggiorna classificatore GSA.
	 */
	public void validateAggiornaClassificatoreGSA() {
		checkNotNullNorInvalidUid(model.getClassificatoreGSA(), "classificatore GSA");
		checkNotNullNorEmpty(model.getClassificatoreGSA().getCodice(), "codice classificatore");
		checkNotNullNorEmpty(model.getClassificatoreGSA().getDescrizione(),"descrizione classificatore");
		checkNotNullNorEmpty(model.getDescrizioneStatoOperativoClassificatore(), "stato classificatore");
	}
	
	/**
	 * Aggiorna classificatore GSA.
	 *
	 * @return the string
	 */
	public String aggiornaClassificatoreGSA() {
		final String methodName = "aggiornaClassificatoreGSA";
		model.impostaDatiPerAggiornamentoClassificatore();
		AggiornaClassificatoreGSA req = model.creaRequestAggiornaClassificatoreGSA();
		AggiornaClassificatoreGSAResponse response = classificatoreGSAService.aggiornaClassificatoreGSA(req);
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nel servizio di ricerca del classificatore GSA");
			addErrori(response);
			return INPUT;
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}
	
	
	

	
	
	
}
