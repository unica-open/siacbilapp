/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagcomp;

import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.ComponenteCapitoloAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.anagcomp.AggiornaComponenteCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.AmbitoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.FonteFinanziariaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MomentoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoGestioneComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;


 
/**
 * Classe di Action per l'aggiornamento del TipoComponenteImportiCapitolo
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 26/set/2019
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaComponenteCapitoloAction extends ComponenteCapitoloAction<AggiornaComponenteCapitoloModel> {

	 
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9216375838218043857L;

	@Autowired
	private transient TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;
	
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Controllo sull'applicabilita' del caso d'uso riferentesi allo stato del bilancio
		checkCasoDUsoApplicabile(model.getTitolo());
		
		// Caricamento del dettaglio
		RicercaDettaglioTipoComponenteImportiCapitolo req = model.creaRequestRicercaDettaglioTipoComponenteImportiCapitolo();
		logServiceRequest(req);
		RicercaDettaglioTipoComponenteImportiCapitoloResponse res = tipoComponenteImportiCapitoloService.ricercaDettaglioTipoComponenteImportiCapitolo(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			throwExceptionFromErrori(res.getErrori());
		}
		
		// Controllo sullo stato del Componente Capitolo
		checkStatoComponenteCapitoloCompatibileConOperazione(res.getTipoComponenteImportiCapitolo());
		
		// Imposto i dati nel model
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = res.getTipoComponenteImportiCapitolo();
		model.setComponenteCapitolo(tipoComponenteImportiCapitolo);
		
		// Carico gli elenchi
		caricaListaClassificatori(tipoComponenteImportiCapitolo);
		
		
		// Leggo i dati dell'azione precedente
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		 
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #aggiornamentoCP()}
	 */
	public void validateAggiornamentoCP() {

		TipoComponenteImportiCapitolo componenteCapitolo = model.getComponenteCapitolo();
		
		
		checkNotNull(componenteCapitolo, "Componente Capitolo", true);
		
		caricaListaClassificatori(componenteCapitolo);
		
		checkNotNullNorEmpty(componenteCapitolo.getDescrizione(), "Descrizione");
		
		if(componenteCapitolo.getMacrotipoComponenteImportiCapitolo().getDescrizione().equals(MacrotipoComponenteImportiCapitolo.FPV.getDescrizione())){
			checkNotNull(componenteCapitolo.getSottotipoComponenteImportiCapitolo(), "Sottotipo");
//			SIAC-7179
//			checkNotNull(componenteCapitolo.getFonteFinanziariaComponenteImportiCapitolo(), "Fonte Finanziamento");
			checkNotNull(componenteCapitolo.getMomentoComponenteImportiCapitolo(), "Momento");
//			SIAC-7179
//			checkCondition(componenteCapitolo.getAnno() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"), true);
			
		}
		if(componenteCapitolo.getMacrotipoComponenteImportiCapitolo().getDescrizione().equals(MacrotipoComponenteImportiCapitolo.FRESCO.getDescrizione())){
			checkCondition(componenteCapitolo.getAmbitoComponenteImportiCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ambito"), true);
		}
//		SIAC-7179
//		if(componenteCapitolo.getMacrotipoComponenteImportiCapitolo().getDescrizione().equals(MacrotipoComponenteImportiCapitolo.AVANZO.getDescrizione())){
//			checkCondition(componenteCapitolo.getFonteFinanziariaComponenteImportiCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Fonte Finanziamento"), true);
//		}
		
		checkNotNull(componenteCapitolo.getTipoGestioneComponenteImportiCapitolo(), "Tipo Gestione");
		checkNotNull(componenteCapitolo.getPropostaDefaultComponenteImportiCapitolo(), "Default Previsione");
		checkNotNull(componenteCapitolo.getDataInizioValidita(), "Data Inizio Validità");
		//checkNotNull(componenteCapitolo.getDataFineValidita(), "Data Fine Validità");
	 
	}
	/**
	 * Carico la lista dei classificatori
	 */
	protected void caricaListaClassificatori(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		model.setListaMacroTipo(Arrays.asList(MacrotipoComponenteImportiCapitolo.values()));
		model.getComponenteCapitolo().setMacrotipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo());
		model.setListaAmbito(filterByMacrotipo(AmbitoComponenteImportiCapitolo.values(),tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo()));
		model.setListaSottoTipo(filterByMacrotipo(SottotipoComponenteImportiCapitolo.values(), tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo()));
		model.setListaFonteFinanziamento(filterByMacrotipo(FonteFinanziariaComponenteImportiCapitolo.values(), tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo()));
		model.setListaMomento(filterByMacrotipo(MomentoComponenteImportiCapitolo.values(), tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo()));
		

		 model.setListaPrevisione(Arrays.asList(PropostaDefaultComponenteImportiCapitolo.values()));
		 model.setListaGestione(Arrays.asList(TipoGestioneComponenteImportiCapitolo.values()));
 

	}
	

	/**
	 * Controlla che lo stato dell'AllegatoAtto sia compatibile con l'operazione richiesta.
	 * 
	 * @param allegatoAtto l'allegato il cui stato &eacute; da controllare
	 */
	private void checkStatoComponenteCapitoloCompatibileConOperazione(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		final String methodName = "checkStatoComponenteCapitoloCompatibileConOperazione";
		List<AzioneConsentita> listaAzioneConsentita = sessionHandler.getAzioniConsentite();
		if(!checkValido(tipoComponenteImportiCapitolo, listaAzioneConsentita)  ) {
			log.info(methodName, "Stato dell'allegato con uid " + tipoComponenteImportiCapitolo.getUid() + " non valido: " + tipoComponenteImportiCapitolo.getStato());
			throwExceptionFromErrori(Arrays.asList(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore()));
		}
	}
	
	/**
	 * Controlla se l'azione sia valida  
	 * 
	 * @param listaAzioneConsentita le azioni consentite
	 * @param tipoComponenteImportiCapitolo         
	 * 
	 * @return <code>true</code> se l'allegato e' valido per l'azione decentrata; <code>false</code> in caso contrario.
	 *         <br>
	 *         Restituisce <code>true</code> se l'azione non &eacute; decentrata
	 */
	private boolean checkValido(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo, List<AzioneConsentita> listaAzioneConsentita) {
		// Per l’azione OP-COM-aggAttoAllegatoDec si deve verificare anche che lo stato dell’atto selezionato sia 'DA COMPLETARE'.
		return !AzioniConsentiteFactory.isConsentito(AzioniConsentite.COMPONENTE_CAPITOLO_AGGIORNA, listaAzioneConsentita)
			|| StatoEntita.IN_LAVORAZIONE.equals(tipoComponenteImportiCapitolo.getStato());
	}
	
	
	public String aggiornamentoCP() {
		final String methodName = "aggiornamentoCP";
		
		// Invoco il servizio di aggiornamento
		AggiornaTipoComponenteImportiCapitolo req = model.creaRequestAggiornaComponenteCapitolo();
		logServiceRequest(req);
		AggiornaTipoComponenteImportiCapitoloResponse res = tipoComponenteImportiCapitoloService.aggiornaTipoComponenteImportiCapitolo(req);
		
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		
		log.debug(methodName, "Aggiornato tipoComponenteImportiCapitolo con uid " + model.getComponenteCapitolo().getUid());
		
		// Reimposto i dati nel model
		model.setComponenteCapitolo(res.getTipoComponenteImportiCapitolo());
		// Imposto il messaggio di successo 
		
		String macroTipoComponentePerAggiornamento = req.getTipoComponenteImportiCapitolo().getMacrotipoComponenteImportiCapitolo().getDescrizione();
		SottotipoComponenteImportiCapitolo sottoTipoComponentePerAggiornamento = req.getTipoComponenteImportiCapitolo().getSottotipoComponenteImportiCapitolo();
		String descrizioneComponentePerAggiornamento=req.getTipoComponenteImportiCapitolo().getDescrizione();
		String successoAggiornamento="";
		successoAggiornamento += (sottoTipoComponentePerAggiornamento != null) ? 
				" Macrotipo: "+macroTipoComponentePerAggiornamento+" - Sottotipo: "+sottoTipoComponentePerAggiornamento+" - Descrizione: "+descrizioneComponentePerAggiornamento.toUpperCase() :
					" Macrotipo: "+macroTipoComponentePerAggiornamento+" - Descrizione: "+descrizioneComponentePerAggiornamento.toUpperCase();	
		
				
	
				
		addInformazione(MessaggioBil.AGGIORNAMENTO_COMPONENTE_CAPITOLO_ANDATO_A_BUON_FINE.getMessaggio(successoAggiornamento));
	    impostaInformazioneSuccessoAzioneInSessionePerRedirezione(successoAggiornamento);
	    sessionHandler.cleanAllSafely();
	    
	    // Imposto il parametro RIENTRO per avvertire la action di ricaricare la lista
	 	sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	
	
	/**
	 * Annullamento dei campi dell'azione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		return SUCCESS;
	}
	
}

 