/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.conti;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti.AggiornaPianoDeiContiAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.conti.AggiornaPianoDeiContiFINModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaContoResponse;
import it.csi.siac.siacgenser.model.CodiceBilancio;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaPianoDeiContiFINAction extends AggiornaPianoDeiContiAction<AggiornaPianoDeiContiFINModel> {


	/** Per la serializzazione */
	private static final long serialVersionUID = 3420174692292561587L;
	

	@Override
	public void prepareExecute(){
		sessionHandler.setParametro(getBilSessionParameterListaCodiciBilancio(), null);
		sessionHandler.setParametro(BilSessionParameter.UID_CLASSE, null);
	}
	
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	@Override
	public String execute(){
		final String methodName = "execute";
		try {
			checkCasoDUsoApplicabile(model.getTitolo());
			caricaDettaglioConto();
			caricaCodifiche();
			/*per modale conto*/
			caricaListaClassi();
			caricaListaClassiAmmortamento();
			caricaListaTitoli();
			caricaListaClasseSoggetto();
			impostaAltriDati();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			sessionHandler.setParametro(BilSessionParameter.ERRORI_AZIONE_PRECEDENTE, model.getErrori());
			return INPUT;
		}
		
		return SUCCESS;
	}


	/**
	 * Validazione del metodo {@link #execute()}.
	 */
	public void validateExecute() {
		checkCondition(model.getUidConto() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("uid conto"), true);
	}
	
	/**
	 * Ottiene la lista dei codici bilancio.
	 * 
	 * @return uan stringa corrispondente al risultato dell'invocazione.
	 */
	public String ottieniListaCodiceBilancio(){
		Integer uid = sessionHandler.getParametro(BilSessionParameter.UID_CLASSE);
		caricaListaCodiceBilancio(uid);
		return SUCCESS;
	}
	
	@Override
	public String aggiornamento(){
		final String methodName = "aggiornamento";
		// per modale conto
		try {
			caricaListaClassi();
			caricaListaTitoli();
			caricaListaClasseSoggetto();
			
			caricaCodifiche();
		} catch(WebServiceInvocationFailureException wsife) {
			log.warn(methodName, wsife.getMessage());
			return INPUT;
		}
		List<CodiceBilancio> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_CODICE_BILANCIO_GEN);
		model.setListaCodiceBilancio(lista);
		validazioneAggiornamento();
		if(hasErrori()){
			return INPUT;
		}
		AggiornaConto reqAC = model.creaRequestAggiornaConto();
		logServiceRequest(reqAC);
		AggiornaContoResponse resAC = contoService.aggiornaConto(reqAC);
		logServiceResponse(resAC);
		
		if(resAC.hasErrori()) {
			addErrori(resAC);
			return INPUT;
		}
		model.setConto(resAC.getConto());
		if(model.getConto().getTipoConto().isTipoCespiti() && model.getConto().getContoCollegato() == null){
			addMessaggio(ErroreGEN.NESSUN_CONTO_COLLEGATO.getErrore());
		}
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}
	
	/**
	 * Carica la lista di tutte le classi piano
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaClassiCompleta(){
		final String methodName = "ottieniListaClassiCompleta";
		try {
			caricaListaClassi();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
		}
		return SUCCESS;
	}
	
	
	/**
	 * Carica la lista delle sole classi piano che hanno conti di tipo ammortamento
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaClassiAmmortamento(){
		final String methodName = "ottieniListaClassiAmmortamento";
		try {
			caricaListaClassiAmmortamento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
		}
		model.setListaClassi(model.getListaClassiAmmortamento());
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti.BaseInserisciAggiornaPianoDeiContiAction#getBilSessionParameterListaClassiPiano()
	 */
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassiPiano() {
		
		return BilSessionParameter.LISTA_CLASSE_PIANO_GEN;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListaCodiciBilancio() {
	    return BilSessionParameter.LISTA_CODICE_BILANCIO_GEN;
	}
	

}
