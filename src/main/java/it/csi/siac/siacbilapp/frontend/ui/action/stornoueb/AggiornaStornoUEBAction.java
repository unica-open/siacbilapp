/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.stornoueb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.stornoueb.AggiornaStornoUEBModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEBResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Action per l'aggiornamento dello storno UEB.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaStornoUEBAction extends GenericBilancioAction<AggiornaStornoUEBModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6924847763915882795L;
	
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListeCodifiche();
		
		log.debugEnd(methodName, "");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Controllo se il caso d'uso sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		
		// Ottenimento dei dati dalla sessione
		log.debug(methodName, "Ottengo la lista degli storni dalla sessione");
		ListaPaginata<StornoUEB> listaStorni = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_STORNO);
		log.debug(methodName, "Ricavo lo storno che mi interessa");
		StornoUEB storno = ComparatorUtils.searchByUid(listaStorni, model.getStorno());
		// Imposto lo storno trovato nel model
		model.setStorno(storno);
		
		log.debug(methodName, "Effettuo la ricerca di dettaglio per il provvedimento");
		RicercaProvvedimento requestRicercaProvvedimento = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse responseRicercaProvvedimento = provvedimentoService.ricercaProvvedimento(requestRicercaProvvedimento);
		
		boolean ricercaSenzaSuccesso = responseRicercaProvvedimento.hasErrori() || responseRicercaProvvedimento.getListaAttiAmministrativi().isEmpty();
		AttoAmministrativo attoAmministrativo = ricercaSenzaSuccesso ? model.getStorno().getAttoAmministrativo() : responseRicercaProvvedimento.getListaAttiAmministrativi().get(0);
		TipoAtto tipoAtto = ricercaSenzaSuccesso ? storno.getAttoAmministrativo().getTipoAtto() : attoAmministrativo.getTipoAtto();
		model.setAttoAmministrativo(attoAmministrativo);
		model.setTipoAtto(tipoAtto);
		
		BigDecimal disponibilitaVariareSorgente = ottieniDisponibilitaVariareFromCapitolo(storno.getCapitoloSorgente());
		BigDecimal disponibilitaVariareDestinazione = ottieniDisponibilitaVariareFromCapitolo(storno.getCapitoloDestinazione());
		
		// Impostazione dei dati provenienti dalla sessione
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaSessione(disponibilitaVariareSorgente, disponibilitaVariareDestinazione);
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene la disponibilita a variare del capitolo.
	 * 
	 * @param capitolo il capitolo
	 * @return la disponibilita a variare, se presente; zero altrimenti
	 */
	private <C extends Capitolo<?, ?>> BigDecimal ottieniDisponibilitaVariareFromCapitolo(C capitolo) {
		return capitolo != null && capitolo.getImportiCapitolo() != null && capitolo.getImportiCapitolo().getDisponibilitaVariareAnno1() != null
				? capitolo.getImportiCapitolo().getDisponibilitaVariareAnno1()
				: BigDecimal.ZERO;
	}
	
	/**
	 * Aggiorna lo storno UEB.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		
		boolean tipoStornoEntrata = "E".equalsIgnoreCase(model.getTipoCapitoloSorgente());
		
		log.debug(methodName, "Creazione della request");
		AggiornaStornoUEB request = model.creaRequestAggiornaStornoUEB(tipoStornoEntrata);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di aggiornamento");
		AggiornaStornoUEBResponse response;
		// Casi differenti a seconda che si abbia uno storno di entrata o di uscita
		if(tipoStornoEntrata) {
			response = variazioneDiBilancioService.aggiornaStornoUEBEntrata(request);
		} else {
			response = variazioneDiBilancioService.aggiornaStornoUEBUscita(request);
		}
		log.debug(methodName, "Richiamato il WebService di aggiornamento");
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Fallimento della chiamata al servizio
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "Storno UEB " + model.getUidStorno() + " aggiornato correttamente tra le UEB " + 
				model.getUidCapitoloSorgente() + " e " + model.getUidCapitoloDestinazione());
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		// Imposto il rientro in sessione
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		// Ricarico l'atto amministrativo dalla lista presente in sessione
		model.setStorno(response.getStornoUEB());

		RicercaProvvedimento requestRicercaProvvedimento = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse responseRicercaProvvedimento = provvedimentoService.ricercaProvvedimento(requestRicercaProvvedimento);
		boolean ricercaSenzaSuccesso = responseRicercaProvvedimento.hasErrori() || responseRicercaProvvedimento.getListaAttiAmministrativi().isEmpty();
		AttoAmministrativo attoDaReInjettare = ricercaSenzaSuccesso ? model.getStorno().getAttoAmministrativo() : responseRicercaProvvedimento.getListaAttiAmministrativi().get(0);
		model.impostaAttoAmministrativo(attoDaReInjettare);
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		final String methodName = "validate";
		log.debugStart(methodName, "");
		
		// Validazione dei campi del capitolo sorgente
		checkNotNull(model.getUidProvvedimento(), "Provvedimento");
		
		// Valida gli importi: almeno uno deve essere compilato
		checkAtLeastOneNotNullNorInvalid("Importi da stornare", false,
			model.getCompetenzaStorno0(),
			model.getCompetenzaStorno1(),
			model.getCompetenzaStorno2(),
			model.getCassaStorno0());
		
		// Controlla che il provvedimento non sia annullato
		checkCondition(!"ANNULLATO".equalsIgnoreCase(model.getStatoProvvedimento()), ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore(""));
		
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
			Errore errore = ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("aggiornamento storni UEB");
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
	
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo, che controlla
	 * anche che il valore inserito non sia invalido.
	 * <br>
	 * Esemp&icirc; di campi numerici non validi:
	 * <ul>
	 * 	<li> {@link Integer} - pari a zero </li>
	 *  <li> {@link Double} - minore di zero </li>
	 *  <li> {@link BigDecimal} - minore di zero </li>
	 * </ul>
	 * 
	 * @param campo			il campo da controllare
	 * @return se il campo &eacute; non null n&eacute; invalido
	 */
	public boolean checkNotNullNorInvalid(Number campo) {
		return campo != null
				&& (!(campo instanceof Integer) && campo.intValue() == 0)
				&& (!(campo instanceof Double) && campo.doubleValue() < 0)
				&& (!(campo instanceof BigDecimal) && ((BigDecimal)campo).compareTo(BigDecimal.ZERO) < 0);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo, che controlla
	 * anche che il valore inserito non sia invalido.
	 * <br>
	 * Esemp&icirc; di campi numerici non validi:
	 * <ul>
	 * 	<li> {@link Integer} - pari a zero </li>
	 *  <li> {@link Double} - maggiore di zero </li>
	 *  <li> {@link BigDecimal} - maggiore di zero </li>
	 * </ul>
	 * 
	 * @param campo			il campo da controllare
	 * @return se il campo &eacute; non null n&eacute; positivo
	 */
	public boolean checkNotNullNorPositive(Number campo) {
		return campo != null
				&& (!(campo instanceof Integer) || campo.intValue() != 0)
				&& (!(campo instanceof Double) || campo.doubleValue() > 0)
				&& (!(campo instanceof BigDecimal) || ((BigDecimal)campo).compareTo(BigDecimal.ZERO) > 0);
	}
	
	/**
	 * Metodo di utilit&agrave; per controllare che almeno uno dei campi forniti in input sia non-<code>null</code> e valido.
	 * 
	 * @param errore      l'errore da apporre nel caso in cui tutti i campi siano <code>null</code>
	 * @param positivi    <code>true</code> se i valori devono essere positivi per essere validi; <code>false</code> altrimenti
	 * @param campi       i campi da controllare
	 */
	public void checkAtLeastOneNotNullNorInvalid(String errore, boolean positivi, Number... campi) {
		boolean almenoUnCampoValido = false;
		
		for(Number number : campi) {
			boolean valid = false;
			if(positivi) {
				valid = checkNotNullNorInvalid(number);
			} else {
				valid = checkNotNullNorPositive(number);
			}
			// Controllo se il dato e' valido
			if(valid) {
				return;
			}
		}
		// Se nemmeno un campo è valido, segnalare l'errore
		if(!almenoUnCampoValido) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(errore));
		}
	}
}
