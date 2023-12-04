/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.previmpacc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.previmpacc.RisultatiRicercaPrevisioneImpegnatoAccertatoModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaPrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaPrevisioneImpegnatoAccertatoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Messaggio;


/**
 * The Class RisultatiRicercaPrevisioneImpegnatoAccertatoBaseAction.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 * @param <MODEL> the generic type
 */
public abstract class RisultatiRicercaPrevisioneImpegnatoAccertatoBaseAction<MODEL extends RisultatiRicercaPrevisioneImpegnatoAccertatoModel> extends GenericBilancioAction<MODEL> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2301925677014138352L;
	
	@Autowired private CapitoloService capitoloService;

	protected void impostaStartPosition() {
		final String methodName = "execute";
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName,"startPosition = "+startPosition);
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
	}
	
	public String aggiorna() {
		AggiornaPrevisioneImpegnatoAccertato reqAggiorna = model.creaRequestAggiornaPrevisioneImpegnatoAccertato();
		AggiornaPrevisioneImpegnatoAccertatoResponse respone = capitoloService.aggiornaPrevisioneImpegnatoAccertatoSuCapitolo(reqAggiorna);
		if(respone.hasErrori()) {
			addErrori(respone);
			return INPUT;
		}
		addInformazione(new Messaggio("CRU_CON_001", "Operazione Effettuata correttamente"));
		return SUCCESS;
	}
	
	public String consulta() {
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		return SUCCESS;
	}

	public String caricaTabellaImporti() {
		caricaImporti();
		return SUCCESS;
	}
	
	
	protected void caricaImporti() {
		RicercaStanziamentiCapitoloGestione request = model.creaRequestRicercaStanziamentiCapitolo();
		RicercaStanziamentiCapitoloGestioneResponse response = capitoloService.ricercaStanziamentiCapitoloGestione(request);
		if(response.hasErrori()) {
			addErrori(response);
			return;
		}
		impostaImportiCapitolo(response.getListaImportiCapitolo());
		model.setPrevisioneImpegnatoAccertato(response.getPrevisioneImpegnatoAccertato());
	}
	
	private void impostaImportiCapitolo(List<ImportiCapitolo> listaImportiCapitolo) {
		int annoEsercizio = model.getAnnoEsercizioInt().intValue();
		for (ImportiCapitolo ic : listaImportiCapitolo) {
			
			if(ic.getAnnoCompetenza().intValue() == annoEsercizio) {
				model.setImportiCapitolo0(ic);
			}else if(ic.getAnnoCompetenza().intValue() == (annoEsercizio +1)) {
				model.setImportiCapitolo1(ic);
			}else if(ic.getAnnoCompetenza().intValue() == (annoEsercizio +2)) {
				model.setImportiCapitolo2(ic);
			}
		}
	}


	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
