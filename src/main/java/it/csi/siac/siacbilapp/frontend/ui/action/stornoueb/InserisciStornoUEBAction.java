/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.stornoueb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.stornoueb.InserisciStornoUEBModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione dell'inserimento dello Storno UEB.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciStornoUEBAction extends GenericBilancioAction<InserisciStornoUEBModel> {
			
	/** Per la serializzazione */
	private static final long serialVersionUID = -4293260896660104654L;
	
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListeCodifiche();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Controllo che il caso d'uso sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Inserisce lo storno UEB.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciCDU() {
		final String methodName = "inserisciCDU";
		
		boolean stornoEntrata = "Entrata".equalsIgnoreCase(model.getTipoCapitolo());
		
		log.debug(methodName, "Creazione della request");
		InserisceStornoUEB request = model.creaRequestInserisciStorno(stornoEntrata);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceStornoUEBResponse response;
		// Switch sui due casi possibili
		if(stornoEntrata){
			response = variazioneDiBilancioService.inserisceStornoUEBEntrata(request);
		} else {
			response = variazioneDiBilancioService.inserisceStornoUEBUscita(request);
		}
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "Storno UEB inserito correttamente tra le UEB " + 
				model.getUidCapitoloSorgente() + " e " + model.getUidCapitoloDestinazione());
		model.setNumeroStorno(response.getStornoUEB().getNumero());
		addInformazione(MessaggioBil.INSERIMENTO_ANDATO_A_BUON_FINE.getMessaggio(response.getStornoUEB().getNumero()));
		
		// Effettuo una ricerca del tipo di provvedimento per impostare i valori nella maschera disattivata
		log.debug(methodName, "Effettuo la ricerca di dettaglio per il provvedimento");
		AttoAmministrativo attoTemporaneo = new AttoAmministrativo();
		attoTemporaneo.setUid(model.getUidProvvedimento());
		List<AttoAmministrativo> listaInSessione = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVEDIMENTO);
		AttoAmministrativo attoAmministrativo = ComparatorUtils.searchByUid(listaInSessione, attoTemporaneo);
		model.setAttoAmministrativo(attoAmministrativo);
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		final String methodName = "validate";
		log.debugStart(methodName, "");
		
		// Validazione dei campi del capitolo sorgente
		checkNotNull(model.getUidCapitoloSorgente(), "UEB sorgente");
		checkNotNull(model.getUidCapitoloDestinazione(), "UEB destinazione");
		checkNotNull(model.getUidProvvedimento(), "Provvedimento");
		
		// Controlla che il provvedimento non sia annullato
		checkCondition(!"ANNULLATO".equalsIgnoreCase(model.getStatoProvvedimento()), ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore(""));
		
		boolean importiPresenti = false;
		boolean importiPositivi = false;
		
		// Controllo gli stanziamenti
		if(model.getStanziamentoCompetenzaSorgente0() != null) {
			importiPresenti = true;
			if(model.getStanziamentoCompetenzaSorgente0().compareTo(BigDecimal.ZERO) > 0) {
				importiPositivi = true;
			}
		}
		if(model.getStanziamentoCompetenzaSorgente1() != null) {
			importiPresenti = true;
			if(model.getStanziamentoCompetenzaSorgente1().compareTo(BigDecimal.ZERO) > 0) {
				importiPositivi = true;
			}
		}
		if(model.getStanziamentoCompetenzaSorgente2() != null) {
			importiPresenti = true;
			if(model.getStanziamentoCompetenzaSorgente2().compareTo(BigDecimal.ZERO) > 0) {
				importiPositivi = true;
			}
		}
		if(model.getStanziamentoCassaSorgente0() != null) {
			importiPresenti = true;
			if(model.getStanziamentoCassaSorgente0().compareTo(BigDecimal.ZERO) > 0) {
				importiPositivi = true;
			}
		}
		
		if(!importiPresenti) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importi da stornare"));
		} else if(importiPositivi) {
			addErrore(new Errore("BIL_ERR_0019", "L'importo indicato deve essere minore o uguale a zero"));
		}
		
		log.debugEnd(methodName, "");
		super.validate();
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		final String methodName = "checkCasoDUsoApplicabile";
		
		if(!model.isGestioneUEB()) {
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("l'ente non gestisce l'informazione UEB").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
		
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione della response");
			// Imposto il primo errore che trovo nella response
			throw new GenericFrontEndMessagesException(response.getErrori().get(0).getTesto(), GenericFrontEndMessagesException.Level.ERROR);
		}
		
		model.setBilancio(response.getBilancio());
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		log.debug(methodName, "Stato del bilancio: " + faseBilancio);
		
		// Controllo che la fase sia ESERCIZIO_PROVVISORIO, GESTIONE o ASSESTAMENTO
		boolean faseValida = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
				FaseBilancio.GESTIONE.equals(faseBilancio) ||
				FaseBilancio.ASSESTAMENTO.equals(faseBilancio);
		log.debug(methodName, "La fase di bilancio è valida? " + faseValida);
		if(!faseValida) {
			Errore errore = ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("inserimento storni UEB");
			throw new GenericFrontEndMessagesException(errore.getTesto(), GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste delle codifiche.
	 */
	private void caricaListeCodifiche() {
		final String methodName = "caricaListeCodifiche";
		log.debug(methodName, "Caricamento della lista dei tipi di atto");
		
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}
	
}
