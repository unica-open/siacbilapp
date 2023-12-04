/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagtipodoc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.TipoDocumentoAction;
import it.csi.siac.siacbilapp.frontend.ui.model.anagtipodoc.InserisciTipoDocumentoModel;
import it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
/**
 * Classe di Action per l'inserimento del Tipo Documento
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 09/09/2020
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciTipoDocumentoAction extends TipoDocumentoAction<InserisciTipoDocumentoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private transient TipoDocumentoFELService tipoDocumentoServiceFEL;
	
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaListe();

	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Pulisco il model
		setModel(null);
	
		super.prepare();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		log.debug(methodName, "Creazione della request");
		
		caricaListe();
		
		checkCasoDUsoApplicabile(model.getTitolo());
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
	 * Inserisce il TipoComponenteImportiCapitolo.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserimentoTD() {
		final String methodName = "inserimentoCP";
		
		log.debug(methodName, "Creazione della request");
		InserisceTipoDocumentoFEL req = model.creaRequesInserisceTipoDocumentoFEL();
	 
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceTipoDocumentoFELResponse response;
 		
		// Caricamento del dettaglio
		RicercaDettaglioTipoDocumentoFEL reqRic = model.creaRequestRicercaDettaglioTipoDocumentoFEL();
		logServiceRequest(reqRic);
		RicercaDettaglioTipoDocumentoFELResponse resRic = tipoDocumentoServiceFEL.ricercaDettaglioTipoDocumentoFEL(reqRic);
		logServiceResponse(resRic);
		
		// Controllo gli errori
		if(resRic.hasErrori()) {
			 addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento",String.format("Codice: %s", 
					 reqRic.getTipoDocFEL().getCodice()				
						)));
					 
			return INPUT;
		}
		
		if (resRic.getTipoDocFEL() !=null && resRic.getTipoDocFEL().getDescrizione() != null && !resRic.getTipoDocFEL().getDescrizione().equals("") ) { 
			
			 
			 addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento",String.format("Codice: %s", 
					 resRic.getTipoDocFEL().getCodice()				
						)));
					 
			return INPUT;
			
		}
	 
		//Chiede la conferma nel caso in cui si stia associando un tipo documento contabilia già utilizzato in altre associazioni
		if(StringUtils.isNotEmpty(model.getMessaggioRichiestaConfermaProsecuzione()) ){
			
			log.debug(methodName, "Sono presenti dei messaggi. E' necessaria la conferma dell'utente. ");
			model.setRichiediConfermaUtente(true);
			
			return INPUT;
		}
		
		model.setRichiediConfermaUtente(false);
		
		response = tipoDocumentoServiceFEL.inserisceTipoDocumentoFEL(req);
		
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			caricaListe();
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "TipoDocumentoFEL inserito correttamente  " + 
				model.getTipoDocFel().getUid()  );

		
		String codicePerInserimento=req.getTipoDocumentoFEL().getCodice();
		String descrizionePerInserimento=req.getTipoDocumentoFEL().getDescrizione();
		
	 
		
		
		String successoInserimento="";
		successoInserimento += (req.getTipoDocumentoFEL() != null) ? 
				"Codice: "+codicePerInserimento+" - Descrizione: "+descrizionePerInserimento: "";
 
		addInformazione(MessaggioBil.INSERIMENTO_COMPONENTE_CAPITOLO_ANDATO_A_BUON_FINE.getMessaggio(successoInserimento));



		//impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		model.setCodice(response.getTipoDocumentoFEL().getCodice());
		
		
		sessionHandler.cleanAllSafely();
		return SUCCESS;
	}
	
	 
	/**
	 * Validazione per il metodo {@link #inserimentoCP()}.
	 */
	public void validateInserimentoTD() {
		effettuaValidazione();
	}

	/**
	 * Effettua la validazione sul Tipo Documento
	 */
	private void effettuaValidazione() {
	 
		checkNotNullNorEmpty(model.getTipoDocFel().getCodice(), "Codice");
		checkNotNullNorEmpty(model.getTipoDocFel().getDescrizione(), "Descrizione");
		
		//Chiedo la conferma nel caso in cui si stia associando un tipo documento contabilia già utilizzato in altre associazioni
		checkVerificaCodiciFelContabilia();
		
		//RICICLO  
//		if (model.getTipoDocFel()!= null && 
//			 (model.getTipoDocFel().getTipoDocContabiliaSpesa() != null && model.getTipoDocFel().getTipoDocContabiliaSpesa().getUid()==0   ) &&
//			 (model.getTipoDocFel().getTipoDocContabiliaEntrata() != null && model.getTipoDocFel().getTipoDocContabiliaEntrata().getUid()==0) ) {
//			     addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento Contabilia Entrata o Tipo documento Contabilia Spesa"));
//				// Lancia l'errore se richiesto
//				throw new ParamValidationException("Error found: " + ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento Contabilia Entrata o Tipo documento Contabilia Spesa").getTesto());
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

 
	

	/**
	 * Annullamento dei campi dell'azione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String annulla() {
		return SUCCESS;
	}
	
	 
	
	
}

 
