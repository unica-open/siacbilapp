/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.GestioneRateiERiscontiPrimaNotaIntegrataModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesaResponse;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRiscontoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRiscontoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.Risconto;

/**
 * Classe di action per la gestione di Ratei e Risconti della prima nota integrata.
 *
 * @author Valentina
 * @version 1.0.0 - 11/07/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class GestioneRateiERiscontiPrimaNotaIntegrataAction extends  GenericBilancioAction<GestioneRateiERiscontiPrimaNotaIntegrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -62645579772205252L;
	
	@Autowired private transient PrimaNotaService primaNotaService;
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
	}
	
		
	/**
	 * Inserimento dei rateo
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciRateo(){
		validazioneRateo();
		if(hasErrori()){
			return SUCCESS;
		}
		InserisciRateo reqIR = model.creaRequestInserisciRateo();
		InserisciRateoResponse  resIR = primaNotaService.inserisciRateo(reqIR);
		if(resIR.hasErrori()){
			addErrori(resIR);
			return SUCCESS;
		}
		model.setRateo(resIR.getRateo());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento del rateo
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaRateo(){
		validazioneRateo();
		if(hasErrori()){
			return SUCCESS;
		}
		AggiornaRateo reqAR = model.creaRequestAggiornaRateo();
		AggiornaRateoResponse  resAR = primaNotaService.aggiornaRateo(reqAR);
		if(resAR.hasErrori()){
			addErrori(resAR);
			return SUCCESS;
		}
		model.setRateo(resAR.getRateo());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Caricamento dei risconti già inseriti
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String impostaRiscontiInseriti(){
		RicercaDettaglioPrimaNota reqRDPN = model.creaRequestRicercaDettaglioPrimaNota();
		RicercaDettaglioPrimaNotaResponse resRDPN = primaNotaService.ricercaDettaglioPrimaNota(reqRDPN);
		if(resRDPN.hasErrori()){
			addErrori(resRDPN);
		}
		model.setRiscontiGiaInseriti(resRDPN.getPrimaNota().getRisconti());
		model.setUidPrimaNota(null);
		return SUCCESS;
	}
	
	/**
	 * Inseriemento del risconto
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciRisconto(){
		validazioneRisconto();
		if(hasErrori()){
			return SUCCESS;
		}
		InserisciRisconto reqIR = model.creaRequestInserisciRisconto();
		InserisciRiscontoResponse  resIR = primaNotaService.inserisciRisconto(reqIR);
		if(resIR.hasErrori()){
			addErrori(resIR);
			return SUCCESS;
		}
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		model.getRiscontiGiaInseriti().add(resIR.getRisconto());
		impostaInformazioneSuccesso();
		model.setRisconto(null);
		model.setIdxRisconto(null);
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento del risconto
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaRisconto(){
		int indiceRisconto = model.getIdxRisconto().intValue();
		Risconto riscontoDaAggiornare = model.getRiscontiGiaInseriti().get(indiceRisconto);
		validazioneRisconto();
		if(hasErrori()){
			return SUCCESS;
		}
		AggiornaRisconto reqAR = model.creaRequestAggiornaRisconto(riscontoDaAggiornare);
		AggiornaRiscontoResponse  resAR = primaNotaService.aggiornaRisconto(reqAR);
		if(resAR.hasErrori()){
			addErrori(resAR);
			return SUCCESS;
		}
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccesso();
		model.setRisconto(null);
		model.setIdxRisconto(null);
		return SUCCESS;
	}
	
	/**
	 * validazione del rateo da inserire/aggiornare
	 * 
	 */
	private void validazioneRateo(){
		checkNotNull(model.getRateo(), "rateo");
		checkNotNull(model.getRateo().getAnno(), "anno rateo");
		checkNotNull(model.getRateo().getImporto(), "importo rateo");
		checkCondition(model.getRateo().getAnno().compareTo(model.getBilancio().getAnno())< 0 , ErroreCore.FORMATO_NON_VALIDO.getErrore("anno",": deve essere precedente all'anno dell'attuale bilancio."));
	}
	
	/**
	 * validazione del risconto da inserire/aggiornare
	 * 
	 */
	private void validazioneRisconto(){
		checkNotNull(model.getRisconto(), "risconto");
		checkNotNull(model.getRisconto().getAnno(), "anno risconto");
		checkNotNull(model.getRisconto().getImporto(), "importo risconto");
		checkCondition(model.getRisconto().getAnno().compareTo(model.getBilancio().getAnno())> 0 , ErroreCore.FORMATO_NON_VALIDO.getErrore("anno",": deve essere successivo all'anno dell'attuale bilancio."));
	}
	
    /**
     * Controllo nota credito.
     *
     * @return the string
     */
    public String controlloNotaCredito() {
		RicercaModulareDocumentoSpesa req = model.creaRequestRicercaDettaglioModulareDocumentoSpesa();
		RicercaModulareDocumentoSpesaResponse res = documentoSpesaService.ricercaModulareDocumentoSpesa(req);
		if(res.hasErrori()) {
			addErrori(res);
			return SUCCESS;
		}
		Boolean esisteNCDCollegataADocumento = Boolean.TRUE.equals(res.getDocumento().getEsisteNCDCollegataADocumento());
		model.setEsisteNCDCollegataADocumento(esisteNCDCollegataADocumento.booleanValue());
		return SUCCESS;
   }
    
    /**
     * Validatecontrollo nota credito.
     */
    public void validatecontrolloNotaCredito() {
    	checkCondition(model.getUidDocumento() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
    }
	
	
}
