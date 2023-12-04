/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.RiepilogoCompletaDefinisciPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;

/**
 * The Class RiepilogoCompletaDefinisciPreDocumentoEntrataAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RiepilogoCompletaDefinisciPreDocumentoEntrataAction extends BaseCompletaDefinisciPreDocumentoEntrataAction<RiepilogoCompletaDefinisciPreDocumentoEntrataModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8035760969996481037L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaContoTesoreria();
		
		caricaListaContoCorrente();
		caricaListaTipoCausale();
		caricaListaCausaleEntrata();
		caricaListaTipoAtto();
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		popolaModelDaRequest();
		return SUCCESS;
	}

	private void popolaModelDaRequest() {
		RicercaSinteticaPreDocumentoEntrata req = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, RicercaSinteticaPreDocumentoEntrata.class);
		
		if(req != null) {
			model.setDataCompetenzaDa(req.getDataCompetenzaDa() != null ? req.getDataCompetenzaDa() : null);
			model.setDataCompetenzaA(req.getDataCompetenzaA() != null ? req.getDataCompetenzaA() : null);
			
			if(!req.getContoCorrenteMancante() && req.getPreDocumentoEntrata() != null && req.getPreDocumentoEntrata().getContoCorrente() != null) {
				model.setContoCorrente(req.getPreDocumentoEntrata().getContoCorrente());				
			}

			if(req.getTipoCausale() != null) {
				model.setTipoCausale(req.getTipoCausale());
			}
			
			if(!req.getCausaleEntrataMancante() && req.getPreDocumentoEntrata() != null && req.getPreDocumentoEntrata().getCausaleEntrata() != null) {
				model.setCausaleEntrata(req.getPreDocumentoEntrata().getCausaleEntrata());
			}
			
			if(!req.getProvvedimentoMancante() && req.getPreDocumentoEntrata() != null 
					&& (req.getPreDocumentoEntrata().getAttoAmministrativo() != null 
					&& req.getPreDocumentoEntrata().getAttoAmministrativo().getUid() != 0 
					&& req.getPreDocumentoEntrata().getAttoAmministrativo().getAnno() != 0
					&& req.getPreDocumentoEntrata().getAttoAmministrativo().getNumero() != 0)) {
				model.setAttoAmministrativo(req.getPreDocumentoEntrata().getAttoAmministrativo());
			}
			
			if(!Boolean.TRUE.equals(model.isInviaTutti())) {
				req.setUidPredocumentiDaFiltrare(model.getListaUid());
				sessionHandler.setParametro(BilSessionParameter.LISTA_UID_PREDOC_SELEZIONATI, model.getListaUid());
			}
			
			//SIAC-6780
			if(sessionHandler.containsKey(BilSessionParameter.LISTA_UID_PREDOC_SELEZIONATI)) {
				List<Integer> listUid = sessionHandler.getParametro(BilSessionParameter.LISTA_UID_PREDOC_SELEZIONATI);
				model.setListaUid(listUid);
			}
			
			sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_PREDOCUMENTI_SELEZIONATI_COMPLETA_DEFINISCI, req);
		}
		
	}
	
	
	/**
	 * Carica predocumenti selezionati.
	 *
	 * @return the string
	 */
	public String caricaPredocumentiSelezionati() {
		RicercaSinteticaPreDocumentoEntrata req = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_PREDOCUMENTI_SELEZIONATI_COMPLETA_DEFINISCI, RicercaSinteticaPreDocumentoEntrata.class);
		RicercaSinteticaPreDocumentoEntrataResponse res = preDocumentoEntrataService.ricercaSinteticaPreDocumentoEntrata(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		sessionHandler.setParametro(BilSessionParameter.LISTA_PREDOC_SELEZIONATI, res.getPreDocumenti());
		model.setImportoTotale(res.getImportoTotale());
		return SUCCESS;
	}
	
	
	/**
	 * Cerca totali for riepilogo.
	 *
	 * @return the string
	 */
	public String cercaTotaliForRiepilogo() {
		final String methodName = "cercaTotaliForRiepilogo";
		
		List<Integer> uids =  sessionHandler.getParametro(BilSessionParameter.LISTA_UID_PREDOC_SELEZIONATI);
		model.setListaUid(uids);
		// Invocazione del servizio
		RicercaSinteticaPreDocumentoEntrata reqRicerca = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_PREDOCUMENTI_SELEZIONATI_COMPLETA_DEFINISCI, RicercaSinteticaPreDocumentoEntrata.class);
		RicercaTotaliPreDocumentoEntrataPerStato req = model.creaRequestRicercaTotaliPreDocumentoEntrataPerRiepilogoCompletaDefinisci(reqRicerca);
		RicercaTotaliPreDocumentoEntrataPerStatoResponse res = preDocumentoEntrataService.ricercaTotaliPreDocumentoEntrataPerStatoRiepilogo(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "esecuzione del servizio RicercaTotaliPreDocumentoEntrataPerStato terminata con errori.");
			return INPUT;
		}
		
		impostaTotaliElenco(res);
		impostaTotaliElencoNoCassa(res);
		
		return SUCCESS;
	}

	/**
	 * Controllo di validazione per il completamento e la definizione del preDocumento tramite ajax.
	 */
	public String controlloCompletamentoForm() {
		String methodName = "controlloCompletamentoForm";
		
		validateCompletaDefinisci();
		
		log.debug(methodName, ": COMPLETO");
		return SUCCESS;
	}
	
	@Override
	protected void checkDisponibilitaAccertamentoSubaccertamento() {
		// Vuoto
	}
}
