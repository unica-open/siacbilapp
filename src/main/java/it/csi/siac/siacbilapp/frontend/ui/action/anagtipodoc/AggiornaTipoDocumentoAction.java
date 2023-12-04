/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagtipodoc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.TipoDocumentoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.anagtipodoc.AggiornaTipoDocumentoModel;
import it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di Action per l'aggiornamento del TipoComponenteImportiCapitolo
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 09/09/2020
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaTipoDocumentoAction extends TipoDocumentoAction<AggiornaTipoDocumentoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;

	@Autowired
	private transient TipoDocumentoFELService tipoDocumentoServiceFEL;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
 


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Controllo sull'applicabilita' del caso d'uso riferentesi allo stato del bilancio
		//checkCasoDUsoApplicabile(model.getTitolo());
		
		// Caricamento del dettaglio
		RicercaDettaglioTipoDocumentoFEL req = model.creaRequestRicercaDettaglioTipoDocumentoFEL();
		logServiceRequest(req);
		RicercaDettaglioTipoDocumentoFELResponse res = tipoDocumentoServiceFEL.ricercaDettaglioTipoDocumentoFEL(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioTipoDocumentoFEL.class, res));
			throwExceptionFromErrori(res.getErrori());
		}
		
		// Controllo sullo stato del Componente Capitolo
		//checkStatoComponenteCapitoloCompatibileConOperazione(res.getTipoDocFEL());
		
		// Imposto i dati nel model
		TipoDocFEL tipoDocFel = res.getTipoDocFEL();
		model.setTipoDocFel(tipoDocFel);
		
		// Carico gli elenchi
		//caricaListaClassificatori(tipoComponenteImportiCapitolo);
		caricaListe();

		
		// Leggo i dati dell'azione precedente
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		 
		return SUCCESS;
	}
	

	
	/**
	 * Validazione per il metodo {@link #aggiornamentoCP()}
	 */
	public void validateAggiornamentoTD() {

		TipoDocFEL tipoDocFEL = model.getTipoDocFel();
		
		
		checkNotNull(tipoDocFEL, "Tipo Documento", true);
		
		caricaListe();
		
		checkNotNullNorEmpty(model.getTipoDocFel().getCodice(), "Codice");
		checkNotNullNorEmpty(model.getTipoDocFel().getDescrizione(), "Descrizione");
		
		
		//chiedo la conferma nel caso in cui si stia associando un tipo documento contabilia giù utilizzato in altre associazioni
		checkVerificaCodiciFelContabilia();
		
		
		//RICICLO 
//		if (model.getTipoDocFel()!= null && 
//				 (model.getTipoDocFel().getTipoDocContabiliaSpesa() != null && model.getTipoDocFel().getTipoDocContabiliaSpesa().getUid()==0   ) &&
//				 (model.getTipoDocFel().getTipoDocContabiliaEntrata() != null && model.getTipoDocFel().getTipoDocContabiliaEntrata().getUid()==0) ) {
//				     addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento Contabilia Entrata o Tipo documento Contabilia Spesa"));
//					// Lancia l'errore se richiesto
//					throw new ParamValidationException("Error found: " + ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento Contabilia Entrata o Tipo documento Contabilia Spesa").getTesto());
//		}	
		 
	}
	
	protected void checkVerificaCodiciFelContabilia() {
		// Caricamento del dettaglio
		RicercaTipoDocumentoFEL reqRic = model.creaRequestRicercaTipoDocumentoFEL();
				logServiceRequest(reqRic);
				RicercaTipoDocumentoFELResponse  resRic = tipoDocumentoServiceFEL.ricercaTipoDocumentoFEL(reqRic);
				logServiceResponse(resRic);
		
				List<TipoDocFEL> tipoDocumentiFELAll = resRic.getTipoDocumentiFEL();
				int numeroREcord=0;
				String msgCodiceFel="";
				
				for (TipoDocFEL tipoDocFEL : tipoDocumentiFELAll)
				{
					if (model.getTipoDocFel().getTipoDocContabiliaEntrata() != null && tipoDocFEL.getTipoDocContabiliaEntrata() != null 
							&& tipoDocFEL.getTipoDocContabiliaEntrata().getUid() ==  model.getTipoDocFel().getTipoDocContabiliaEntrata().getUid()) {
						numeroREcord++;
						msgCodiceFel=msgCodiceFel + " " + tipoDocFEL.getCodice() + " ,";
					}  
				}
				
		if(numeroREcord == 0 || Boolean.TRUE.equals(model.getForzaTipoDocumento())) {
			// resetto tutte le informazioni relative ad un'eventuale precedente richiesta di conferma 
			model.setMessaggioRichiestaConfermaProsecuzione("");
			return;
		}

		model.setMessaggioRichiestaConfermaProsecuzione("Tipo documento Contabilia Entrata selezionato già utilizzato con Codice FEL:" + StringUtils.chop(msgCodiceFel) 
				+ "</br> Si desidera continuare?");
	}
	
	public String aggiornamentoTD() {
		final String methodName = "aggiornamentoTD";
		
		//Chiedo la conferma nel caso in cui si stia associando un tipo documento contabilia già utilizzato in altre associazioni
		if(StringUtils.isNotEmpty(model.getMessaggioRichiestaConfermaProsecuzione()) ){
			log.debug(methodName, "Sono presenti dei messaggi. E' necessaria la conferma dell'utente. ");
			model.setRichiediConfermaUtente(true);
			
			return INPUT;
		}
		model.setRichiediConfermaUtente(false);
		// Invoco il servizio di aggiornamento
		AggiornaTipoDocumentoFEL req = model.creaRequestAggiornaTipoDocumentoFEL();
		logServiceRequest(req);
		AggiornaTipoDocumentoFELResponse res = tipoDocumentoServiceFEL.aggiornaTipoDocumentoFEL(req);
		
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaTipoDocumentoFEL.class, res));
			addErrori(res);
			return INPUT;
		}
		
		log.debug(methodName, "Aggiornato tipoDocumentoServiceFEL con codice " + model.getTipoDocFel().getCodice());
		
		// Reimposto i dati nel model
		model.setTipoDocFel(res.getTipoDocumentoFEL());
		// Imposto il messaggio di successo 
		
		 
		String successoAggiornamento="";
		successoAggiornamento += (req.getTipoDocumentoFEL() != null) ? 
				" Codice: "+req.getTipoDocumentoFEL().getCodice()+" - Descrizione: "+req.getTipoDocumentoFEL().getDescrizione() :""; 
		
				
		addInformazione(MessaggioBil.AGGIORNAMENTO_TIPO_DOCUMENTO_ANDATO_A_BUON_FINE.getMessaggio(successoAggiornamento));
	    impostaInformazioneSuccessoAzioneInSessionePerRedirezione(successoAggiornamento);
	    sessionHandler.cleanAllSafely();
	    
	    // Imposto il parametro RIENTRO per avvertire la action di ricaricare la lista
	 	sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle liste
	 */
	private void caricaListe(){
		// Caricamento delle varie liste per la gestione della UI
		// I tipi di documento utilizzato sono solo quelli di spesa, con eventuale filtro subordinato/regolarizzazione da chiamante
		checkAndObtainListaTipoDocumentoEntrata(TipoFamigliaDocumento.ENTRATA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
		checkAndObtainListaTipoDocumentoSpesa(TipoFamigliaDocumento.SPESA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
	}

	
	
	/**
	 * Annullamento dei campi dell'azione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		return SUCCESS;
	}
	
}
