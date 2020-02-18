/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagcomp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.ComponenteCapitoloAction;
import it.csi.siac.siacbilapp.frontend.ui.model.anagcomp.InserisciComponenteCapitoloModel;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.AmbitoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.FonteFinanziariaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MomentoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoGestioneComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per l'inserimento del TipoComponenteImportiCapitolo.
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 26/set/2019
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciComponenteCapitoloAction extends ComponenteCapitoloAction<InserisciComponenteCapitoloModel> {


	/** Per la serializzazione */
	private static final long serialVersionUID = 5448553594192555948L;

	TipoComponenteImportiCapitolo cc = new TipoComponenteImportiCapitolo();
	
	@Autowired
	private transient TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;
	
	
	
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaListaMacrotipo();
		caricaListaPrevisione();
		caricaListaGestione();
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
		
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	
	
	
	/**
	 * Inserisce il TipoComponenteImportiCapitolo.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserimentoCP() {
		final String methodName = "inserimentoCP";
		
		log.debug(methodName, "Creazione della request");
		InserisceTipoComponenteImportiCapitolo req = model.creaRequestInserisceComponenteCapitolo();
	 
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceTipoComponenteImportiCapitoloResponse response;
		
		
		
		response = tipoComponenteImportiCapitoloService.inserisceTipoComponenteImportiCapitolo(req);
		
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "TipoComponenteImportiCapitolo inserito correttamente  " + 
				model.getComponenteCapitolo().getUid()  );
		
		model.getComponenteCapitolo().setCodice(response.getTipoComponenteImportiCapitolo().getCodice());
		String macroTipoComponentePerInserimento = req.getTipoComponenteImportiCapitolo().getMacrotipoComponenteImportiCapitolo().getDescrizione();
		SottotipoComponenteImportiCapitolo sottoTipoComponentePerInserimento = req.getTipoComponenteImportiCapitolo().getSottotipoComponenteImportiCapitolo();
		String descrizioneComponentePerInserimento=req.getTipoComponenteImportiCapitolo().getDescrizione();
		String successoInserimento="";
		successoInserimento += (sottoTipoComponentePerInserimento != null) ? 
				"Macrotipo: "+macroTipoComponentePerInserimento+" - Sottotipo: "+sottoTipoComponentePerInserimento+" - Descrizione: "+descrizioneComponentePerInserimento.toUpperCase() :
					"Macrotipo: "+macroTipoComponentePerInserimento+" - Descrizione: "+descrizioneComponentePerInserimento.toUpperCase();	
		
		addInformazione(MessaggioBil.INSERIMENTO_COMPONENTE_CAPITOLO_ANDATO_A_BUON_FINE.getMessaggio(successoInserimento));



		//impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		model.setUidComponenteCapitolo(response.getTipoComponenteImportiCapitolo().getUid());
		
		
		sessionHandler.cleanAllSafely();
		return SUCCESS;
	}
	
	 
	/**
	 * Validazione per il metodo {@link #inserimentoCP()}.
	 */
	public void validateInserimentoCP() {
		effettuaValidazione();
	}

	/**
	 * Effettua la validazione sul provvedimento
	 */
	private void effettuaValidazione() {
	 
		checkCondition(model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Macrotipo"), true);
		checkNotNullNorEmpty(model.getComponenteCapitolo().getDescrizione(), "Descrizione");
		
		if(model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo().getDescrizione().equals(MacrotipoComponenteImportiCapitolo.FPV.getDescrizione())){
			checkNotNull(model.getComponenteCapitolo().getSottotipoComponenteImportiCapitolo(), "Sottotipo");
//			SIAC-7179
//			checkNotNull(model.getComponenteCapitolo().getFonteFinanziariaComponenteImportiCapitolo(), "Fonte Finanziamento");
			checkNotNull(model.getComponenteCapitolo().getMomentoComponenteImportiCapitolo(), "Momento");
//			SIAC-7179
//			checkCondition(model.getComponenteCapitolo().getAnno() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"), true);
			
		}
		if(model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo().getDescrizione().equals(MacrotipoComponenteImportiCapitolo.FRESCO.getDescrizione())){
			checkCondition(model.getComponenteCapitolo().getAmbitoComponenteImportiCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ambito"), true);
		}
//		SIAC-7179
//		if(model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo().getDescrizione().equals(MacrotipoComponenteImportiCapitolo.AVANZO.getDescrizione())){
//			checkCondition(model.getComponenteCapitolo().getFonteFinanziariaComponenteImportiCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Fonte Finanziamento"), true);
//		}
		
		//checkNotNull(model.getComponenteCapitolo().getPropostaDefaultComponenteImportiCapitolo(), "Default Previsione");
		checkNotNull(model.getComponenteCapitolo().getTipoGestioneComponenteImportiCapitolo(), "Tipo Gestione");
		checkNotNull(model.getComponenteCapitolo().getPropostaDefaultComponenteImportiCapitolo(), "Default Previsione");
		checkNotNull(model.getComponenteCapitolo().getDataInizioValidita(), "Data Inizio Validità");

		
	}
	
	
	protected void caricaListaMacrotipo() throws WebServiceInvocationFailureException {
		List<MacrotipoComponenteImportiCapitolo> macrotipoList = Arrays.asList(MacrotipoComponenteImportiCapitolo.values());
		model.setListaMacroTipo(macrotipoList);
	}
	 
	
	public String caricaClassificatori() {
		model.setListaAmbito(filterByMacrotipo(AmbitoComponenteImportiCapitolo.values(), model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo()));
		model.setListaSottoTipo(filterByMacrotipo(SottotipoComponenteImportiCapitolo.values(), model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo()));
		model.setListaFonteFinanziamento(filterByMacrotipo(FonteFinanziariaComponenteImportiCapitolo.values(), model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo()));
		model.setListaMomento(filterByMacrotipo(MomentoComponenteImportiCapitolo.values(), model.getComponenteCapitolo().getMacrotipoComponenteImportiCapitolo()));
		
		return SUCCESS;
	}
	
	/* -- ATTRIBUTI OPERATIVI DA INSERIRE PER OGNI NUOVA COMPONENTE -- */
	
	//valori possibili della proposta di default: no, solo previsione, solo gestione, si
	private void caricaListaPrevisione() {
		PropostaDefaultComponenteImportiCapitolo[] tipoProposta = PropostaDefaultComponenteImportiCapitolo.values();
	       List<PropostaDefaultComponenteImportiCapitolo> tipoPropostaList = new ArrayList<PropostaDefaultComponenteImportiCapitolo>();
	       for (PropostaDefaultComponenteImportiCapitolo tipoPropostaElem : tipoProposta) {
	    	   tipoPropostaList.add(tipoPropostaElem);
	       }
	       
	       model.setListaPrevisione(tipoPropostaList);
	}
	
	//valori possibili: manuale, solo automatica
	private void caricaListaGestione() {
		TipoGestioneComponenteImportiCapitolo[] tipoGestione = TipoGestioneComponenteImportiCapitolo.values();
	       List<TipoGestioneComponenteImportiCapitolo> tipoGestioneList = new ArrayList<TipoGestioneComponenteImportiCapitolo>();
	       for (TipoGestioneComponenteImportiCapitolo tipoGestioneElem : tipoGestione) {
	    	   tipoGestioneList.add(tipoGestioneElem);
	       }
	       
	       model.setListaGestione(tipoGestioneList);
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

 
