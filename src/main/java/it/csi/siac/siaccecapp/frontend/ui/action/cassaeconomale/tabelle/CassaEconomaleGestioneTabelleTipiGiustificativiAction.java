/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.tabelle;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle.CassaEconomaleGestioneTabelleTipiGiustificativiModel;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle.OperazioneTipiGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoCassaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la gestione della tabella dei tipi giustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CassaEconomaleGestioneTabelleTipiGiustificativiAction extends GenericBilancioAction<CassaEconomaleGestioneTabelleTipiGiustificativiModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7106759330726144422L;

	@Autowired private transient CassaEconomaleService cassaEconomaleService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Carico la lista delle tipologie
		model.setListaTipologiaGiustificativo(Arrays.asList(TipologiaGiustificativo.values()));
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		setCassaEconomaleInModelFromSession();
		boolean cassaInStatoValido = StatoOperativoCassaEconomale.VALIDA.equals(model.getCassaEconomale().getStatoOperativoCassaEconomale());
		model.setCassaInStatoValido(Boolean.valueOf(cassaInStatoValido));
		// Pulsante attivo solo se la cassa e' in stato 'Valida'
		Boolean inserimentoAbilitato = Boolean.valueOf(cassaInStatoValido);
		model.setInserimentoAbilitato(inserimentoAbilitato);
		
		return SUCCESS;
	}
	
	/**
	 * Imposta la cassa economale nel model.
	 */
	private void setCassaEconomaleInModelFromSession() {
		CassaEconomale cassaEconomale = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE);
		model.setCassaEconomale(cassaEconomale);
	}
	
	/**
	 * Ricerca i tipi di operazione di cassa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		setCassaEconomaleInModelFromSession();
		RicercaSinteticaTipoGiustificativo request = model.creaRequestRicercaSinteticaTipoGiustificativo();
		logServiceRequest(request);
		RicercaSinteticaTipoGiustificativoResponse response = cassaEconomaleService.ricercaSinteticaTipoGiustificativo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica del tipo di giustiicativo");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		// Dati trovati. Imposto in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_GIUSTIFICATIVO, response.getTipiGiustificativo());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_TIPO_GIUSTIFICATIVO, request);
		return SUCCESS;
	}
	
	/**
	 * Inizio dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserimento() {
		model.setOperazioneTipiGiustificativo(OperazioneTipiGiustificativo.INSERIMENTO);
		
		// Impostazione dei default
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setPercentualeAnticipoTrasferta(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		tipoGiustificativo.setPercentualeAnticipoMissione(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		model.setTipoGiustificativo(tipoGiustificativo);
		
		return SUCCESS;
	}
	
	/**
	 * Inserimento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		setCassaEconomaleInModelFromSession();
		InserisceTipoGiustificativo request = model.creaRequestInserisceTipoGiustificativo();
		logServiceRequest(request);
		InserisceTipoGiustificativoResponse response = cassaEconomaleService.inserisceTipoGiustificativo(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		TipoGiustificativo tipoGiustificativo = response.getTipoGiustificativo();
		log.debug(methodName, "Inserito TipoGiustificativo con uid " + tipoGiustificativo.getUid());
		
		// Inserimento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		impostaTipoGiustificativoRicercaSeNonPresente();
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Validazione per il metodo {@link #inserimento()}.
	 */
	public void validateInserimento() {
		TipoGiustificativo tipoGiustificativo = model.getTipoGiustificativo();
		checkNotNull(tipoGiustificativo, "Tipo operazione da inserire", true);
		
		checkNotNull(tipoGiustificativo.getTipologiaGiustificativo(), "Tipo giustificativo");
		checkNotNullNorEmpty(tipoGiustificativo.getCodice(), "Codice");
		checkNotNullNorEmpty(tipoGiustificativo.getDescrizione(), "Descrizione");
		
		// Controllo sulle percentuali
		validatePercentuale(tipoGiustificativo.getPercentualeAnticipoTrasfertaNotNull(), "Percentuale anticipo trasferta");
		validatePercentuale(tipoGiustificativo.getPercentualeAnticipoMissioneNotNull(), "Percentuale anticipo missione");
	}
	
	/**
	 * Validazione per la percentuale.
	 * 
	 * @param percentuale la percentuale da validate
	 * @param nomeCampo   il nome del campo
	 * 
	 * @throws NullPointerException se la percentuale &eacute; <code>null</code>
	 */
	private void validatePercentuale(BigDecimal percentuale, String nomeCampo) {
		checkCondition(percentuale.signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, ": non deve essere negativo"));
		checkCondition(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.compareTo(percentuale) >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, ": non deve essere maggiore di 100"));
	}
	
	/**
	 * Inizio dell'aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornamento() {
		// Cerco il tipo per uid
		List<TipoGiustificativo> listaTipi = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_GIUSTIFICATIVO);
		TipoGiustificativo tipoGiustiicativo = model.getTipoGiustificativo();
		TipoGiustificativo tipoTrovato = ComparatorUtils.searchByUid(listaTipi, tipoGiustiicativo);
		model.setTipoGiustificativo(tipoTrovato);
		
		// Imposto il tipo di operazione
		model.setOperazioneTipiGiustificativo(OperazioneTipiGiustificativo.AGGIORNAMENTO);
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		setCassaEconomaleInModelFromSession();
		AggiornaTipoGiustificativo request = model.creaRequestAggiornaTipoGiustificativo();
		logServiceRequest(request);
		AggiornaTipoGiustificativoResponse response = cassaEconomaleService.aggiornaTipoGiustificativo(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		TipoGiustificativo tipoGiustificativo = response.getTipoGiustificativo();
		log.debug(methodName, "Aggiornato TipoGiustificativo con uid " + tipoGiustificativo.getUid());
		
		// Aggiornamento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		impostaTipoGiustificativoRicercaSeNonPresente();
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		TipoGiustificativo tipoGiustificativo = model.getTipoGiustificativo();
		checkNotNull(tipoGiustificativo, "Tipo operazione da aggiornare", true);
		
		checkNotNullNorEmpty(tipoGiustificativo.getDescrizione(), "Descrizione");
		// Controllo sulle percentuali
		validatePercentuale(tipoGiustificativo.getPercentualeAnticipoTrasfertaNotNull(), "Percentuale anticipo trasferta");
		validatePercentuale(tipoGiustificativo.getPercentualeAnticipoMissioneNotNull(), "Percentuale anticipo missione");
		
		// Lotto P
		// Controllo dell'importo per il tipo ANTICIPO MISSIONE
		if(TipologiaGiustificativo.ANTICIPO_MISSIONE.equals(tipoGiustificativo.getTipologiaGiustificativo())) {
			//JIRA-2916 In gestione dei Tipi Giustificativi, per i giustificativi di tipo 'M' l'Importo non è obbligatorio. 
			
			checkCondition(tipoGiustificativo.getImporto() == null || tipoGiustificativo.getImporto().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "non puo' essere negativo"));
		}
	}
	
	/**
	 * Annullamento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullamento() {
		final String methodName = "annullamento";
		AnnullaTipoGiustificativo request = model.creaRequestAnnullaTipoGiustificativo();
		logServiceRequest(request);
		AnnullaTipoGiustificativoResponse response = cassaEconomaleService.annullaTipoGiustificativo(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		TipoGiustificativo tipoGiustificativo = response.getTipoGiustificativo();
		log.debug(methodName, "Annullato TipoGiustificativo con uid " + tipoGiustificativo.getUid());
		
		// Annullamento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Imposta il tipo di giustificativo per la ricerca qualora non sia gi&agrave; presente nel model.
	 */
	private void impostaTipoGiustificativoRicercaSeNonPresente() {
		if(model.getTipoGiustificativoRicerca() == null) {
			model.setTipoGiustificativoRicerca(new TipoGiustificativo());
		}
	}
	
}
