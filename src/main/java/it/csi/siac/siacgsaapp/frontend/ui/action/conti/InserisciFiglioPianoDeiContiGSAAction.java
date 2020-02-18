/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conti;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti.InserisciFiglioPianoDeiContiAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.conti.InserisciFiglioPianoDeiContiFINModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceContoResponse;
import it.csi.siac.siacgenser.model.CodiceBilancio;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciFiglioPianoDeiContiGSAAction extends InserisciFiglioPianoDeiContiAction<InserisciFiglioPianoDeiContiFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5716922411860569395L;
	
	@Override
	public void prepare() throws Exception {
		sessionHandler.setParametro(BilSessionParameter.CONTO_PADRE, null);
		sessionHandler.setParametro(BilSessionParameter.CONTI_FIGLI_SENZA_FIGLI, null);
		super.prepare();
	}
	
	@Override
	public void prepareExecute(){
		sessionHandler.setParametro(getBilSessionParameterListaCodiciBilancio(), null);
		sessionHandler.setParametro(BilSessionParameter.UID_CLASSE, null);
	}
	
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	@Override
	public String execute(){
		final String methodName = "execute";
		checkCasoDUsoApplicabile(model.getTitolo());
		model.setConto(new Conto());
		// per modale conto
		try {
			caricaListaClassi();
			caricaListaClassiAmmortamento();
			caricaListaClasseSoggetto();
			/*fine x modale*/
			caricaPadreEListe();
			impostaTipoContoGenerico();
			caricaDatiAggiuntiviSeNecessario();
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
	public String inserimento(){
		final String methodName = "inserimento";
		/*per modale conto*/
		try {
			caricaListaClassi();
			caricaListaClasseSoggetto();
			caricaPadreEListe();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
		}
		List<CodiceBilancio> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_CODICE_BILANCIO_GEN);
		model.setListaCodiceBilancio(lista);
		validazioneInserimento();
		
		if(hasErrori()){
			return INPUT;
		}
		InserisceConto reqIC = model.creaRequestInserisceConto();
		logServiceRequest(reqIC);
		InserisceContoResponse resIC = contoService.inserisceConto(reqIC);
		logServiceResponse(resIC);
		
		if(resIC.hasErrori()) {
			addErrori(resIC);
			return INPUT;
		}
		model.setConto(resIC.getConto());
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
		return BilSessionParameter.LISTA_CLASSE_PIANO_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListaCodiciBilancio() {
	    return BilSessionParameter.LISTA_CODICE_BILANCIO_GSA;
	}
	

}
