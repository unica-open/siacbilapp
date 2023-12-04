/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.elaborazioniflussopagopa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.pagopa.frontend.webservice.PagoPAService;
import it.csi.siac.pagopa.frontend.webservice.msg.AggiornaAccertamentoModaleResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaAccertamentoResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDocResponse;
import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.elaborazioniflussopagopa.ConsultaElaborazioneFlussoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;

/**
 * Classe di Action per la gestione della consultazione del Elaborazione FLusso.
 * 
 * @author Vincenzo Gambino
 * @version 1.0.0 - 14/07/2020
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaElaborazioneFlussoAction extends GenericElaborazioniFlussoAction<ConsultaElaborazioneFlussoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9157748833471208658L;
	
	@Autowired private transient PagoPAService pagoPAService;
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public void prepare() throws Exception {
		if(model != null) {
			cleanErroriMessaggiInformazioni();
		}
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		final String methodName = "ConsultaElaborazioneFlussoAction";
		String flusso ="";
		
		//SETTING ID ELABORAZIONE
		//model.setUidElaborazioneFlusso(model.getUidDaConsultare());
		
		//INTESTAZIONE
		
		ListaPaginata<Elaborazione> elaborazioni = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_ELABORAZIONI_FLUSSO_PAGO_PA);
		if(elaborazioni!= null && !elaborazioni.isEmpty()){
			for(Elaborazione e : elaborazioni){
				if(model.getUidDaConsultare() != null && e.getUid()==model.getUidDaConsultare().intValue()){
					model.setNumeroProvvisorio(e.getNumeroProvvisorio());
					if(e.getDataEmissioneProvvisorio()!= null){
						String dataEmissioneProvStr = formatterDay.format(e.getDataEmissioneProvvisorio());
						model.setDataEmissioneProvvisorio(dataEmissioneProvStr);
					}
					model.setImportoProvvisorio(e.getImportoProvvisorio());
					if(e.getDataElaborazione()!= null){
						String dataElaborazioneStr = formatter.format(e.getDataElaborazione());
						model.setDataElaborazione(dataElaborazioneStr);
					}
					if(e.getStatoElaborazione()!= null){
						model.setStatoElaborazione(e.getStatoElaborazione().getCodice());
					}
					flusso = e.getFlusso();
					break;
				}
			}
		}
		
		//RIGHE DI RICONCILIAZIONE
		RicercaRiconciliazioniDoc req = model.creaRequestRicercaRiconciliazioniDoc();
		RiconciliazioneDoc riconciliazione = new RiconciliazioneDoc();
		Elaborazione elaborazione = new Elaborazione();
		elaborazione.setUid(model.getUidDaConsultare());
		elaborazione.setFlusso(flusso);
		riconciliazione.setElaborazione(elaborazione);
		req.setRiconciliazioneDoc(riconciliazione);
		RicercaRiconciliazioniDocResponse res = pagoPAService.ricercaRiconciliazioniDoc(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio RicercaRiconciliazioni");
			addErrori(res);
			sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_RICONCILIAZIONE_PAGO_PA, req);
			sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_RICONCILIAZIONE_PAGO_PA, new ListaPaginataImpl<RiconciliazioneDoc>());
			sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, null);
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_RICONCILIAZIONE_PAGO_PA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_RICONCILIAZIONE_PAGO_PA, res.getRiconciliazioniDoc());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, null);
		
		//SIAC-8005
		startPositionFromSessionParamter(methodName, BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE);
		
		return SUCCESS;
	
	}
	
	
	
	public String ottieniListaErrori (){
		
		final String methodName = "ottieniListaErroriRiconciliazione";
		log.debug(methodName, "Uid di elaborazione flusso da consultare: " + model.getUidDaConsultare());
		
		RicercaRiconciliazioniDoc request = model.creaRequestRicercaRiconciliazioniErrore();
		Riconciliazione riconciliazione = new Riconciliazione();
		riconciliazione.setRicId(model.getRiconciliazioneId());
		
		RiconciliazioneDoc riconciliazioneDoc = new RiconciliazioneDoc();
		riconciliazioneDoc.setRiconciliazione(riconciliazione);
		request.setRiconciliazioneDoc(riconciliazioneDoc);
		request.setRiconciliazione(riconciliazione);
		
		//SIAC-7556 CM 22/07/2020 Inizio
		Elaborazione elaborazione = new Elaborazione();
		request.getRiconciliazioneDoc().setElaborazione(elaborazione);
		request.getRiconciliazioneDoc().getElaborazione().setFlussoId(model.getUidDaConsultare());
		request.getRiconciliazioneDoc().getElaborazione().setUid(model.getUidDaConsultare());
		//SIAC-7556 CM 22/07/2020 Fine
		
		logServiceRequest(request);
		
		RicercaRiconciliazioniDocResponse response = pagoPAService.ricercaRiconciliazioniDoc(request) ; 
		logServiceResponse(response);
		

		
		if(response.isFallimento()) {
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_RICONCILIAZIONE_ERRORE_PAGO_PA, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_RICONCILIAZIONE_ERRORE_PAGO_PA, response.getRiconciliazioniDoc());
		
		if(response.getRiconciliazioniDoc()!= null && !response.getRiconciliazioniDoc().isEmpty() && 
				response.getRiconciliazioniDoc().get(0).getRiconciliazioneDocTipoCode()!= null &&
				("FTV".equals(response.getRiconciliazioniDoc().get(0).getRiconciliazioneDocTipoCode())
					|| "FAT".equals(response.getRiconciliazioniDoc().get(0).getRiconciliazioneDocTipoCode())	
						|| "FIT".equals(response.getRiconciliazioniDoc().get(0).getRiconciliazioneDocTipoCode())
						)){
			 model.setTipoFattura(true);
		}else{
			 model.setTipoFattura(false);
		}
		return SUCCESS;
	}
	
	//SIAC-8046 CM Task 2.2-2.3 31/03-16/04/2021 Inizio
	public String verificaEaggiornaAccertamentoModale () {

		final String methodName = "verificaEaggiornaAccertamentoModale";
		//ListaPaginata<RiconciliazioneDoc> listaRiconciliazioneDoc = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_RICONCILIAZIONE_PAGO_PA);
		
		RicercaRiconciliazioniDoc request = model.creaRequestAggiornaAccertamentoRiconciliazione();
		Riconciliazione riconciliazione = new Riconciliazione();
		riconciliazione.setRicId(model.getRiconciliazioneId());
		riconciliazione.setAnno(model.getAnnoAcc());
		riconciliazione.setNumeroAccertamento(model.getNumAcc());
		
		RiconciliazioneDoc riconciliazioneDoc = new RiconciliazioneDoc();
		riconciliazioneDoc.setRiconciliazione(riconciliazione);
		request.setRiconciliazioneDoc(riconciliazioneDoc);
		request.setRiconciliazione(riconciliazione);
		
		logServiceRequest(request);
		
		/*if(model.getAnnoAcc().isEmpty() || model.getNumAcc().isEmpty()) {
			//genera errore e non chiamare il servizio
			model.setResRicercaAccertamento(false);
			return INPUT;
		}*/
		
		//primo controllo sull'esistenza dell'accertamento
		//un accertamento è valido se esiste ed è in stato definitivo
		RicercaAccertamentoResponse response = pagoPAService.ricercaAccertamento(request); 
		
		if(response.isFallimento()) {
			addErrori(response);
			return INPUT;
		}
		
		if(!response.isAccertamentoExist()) {
			//torna un errore
			//System.out.println("L'accertamento non esiste.");
			model.setResRicercaAccertamento(false);
			return INPUT;
		}else {
			
			model.setResRicercaAccertamento(true);
			
			//chiama il servizio che esegue l'update nella tabella di riconciliazione
			AggiornaAccertamentoModaleResponse responseAggiornamento = pagoPAService.aggiornaAccertamento(request);
			
			//System.out.println("codice: " + responseAggiornamento.getCodice());
			//System.out.println("descrizione: " + responseAggiornamento.getDescrizione());
			//System.out.println("L'accertamento esiste, quindi si può salvare nella tabella Riconciliazioni");
		    
			// 2 errore gestito (nessun record prima query/collegamento DE/DS)
		    // 1 errore generico
		    // 0 ok	
			if(responseAggiornamento.getCodice().equals(0)) {
				model.setResAggiornaAccertamento(true);
				model.setDescrizioneResAggiornaAccertamento(responseAggiornamento.getDescrizione());
				return SUCCESS;
			}else if(responseAggiornamento.getCodice().equals(1) || responseAggiornamento.getCodice().equals(2)) {
				model.setResAggiornaAccertamento(false);
				model.setDescrizioneResAggiornaAccertamento(responseAggiornamento.getDescrizione());
				return INPUT;
			}else {
				model.setResAggiornaAccertamento(false);
				model.setDescrizioneResAggiornaAccertamento("errore generico");
				return INPUT;
			}
			
			//model.setResRicercaAccertamento(true);
			//return SUCCESS;
		}		
	}
	//SIAC-8046 CM Task 2.2-2.3 31/03-16/04/2021 Fine
	
	
	
	@SuppressWarnings("unused")
	private String formatDataEmissioneProvvisorio(String s){
		String res = "";
		if(s!= null &&	s.length()>19){
			StringBuilder sb = new StringBuilder();
			String[] dayTime = s.split(" ");
			if(dayTime.length==2){
				//DAY
				String day = dayTime[0];
				String[] dayArr = day.split("-");
				if(dayArr.length==3){
					sb.append(dayArr[2]).append("/").append(dayArr[1]).append("/").append(dayArr[0]);
					sb.append(" ");
				}
				
				
				//TIME
				String time = dayTime[1];
				String[] timeArr = time.split("\\.");
				if(timeArr.length==2){
					sb.append(timeArr[0]);
				}
				res = sb.toString();
			}
		}
		
		
		return res;
	}
	

}
