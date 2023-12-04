/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.registroa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.registroa.RisultatiRicercaRegistroACespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCredito;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCreditoResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Classe di Action per i risultati della ricerca del registro A (prime note verso inventario contabile)del cespite.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistroACespiteAction extends GenericBilancioAction <RisultatiRicercaRegistroACespiteModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -84536314205150951L;
	
	private static final String KEY_PRIMA_NOTA_NON_INVENTARIATA = "PRIMA_NOTA_NON_INVENTARIATA";
	
	@Autowired private transient PrimaNotaCespiteService primaNotaCespiteService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Imposto la posizione di start
		Integer posizioneStart = ottieniPosizioneStartDaSessione(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE);
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Leggo i messaggi delle azioni precedenti
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Consultazione del registro A(prime note verso inventario contabile)
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		if(hasMessaggi()) {
			return INPUT;
		}
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START));
		return SUCCESS;
	}
	
	/**
	 * Validazione del metodo {@link #consulta()}
	 */
	public void validateConsulta() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "prima nota");
		if(model.getPrimaNota().getPrimaNotaInventario() == null || model.getPrimaNota().getPrimaNotaInventario().getUid() == 0) {
			addMessaggio(new Messaggio("INF01","Prima nota non ancora integrata, consultazione su prime note verso inventario contabile non possibile. Se necessario, consultare dalla funzionalit&agrave; di contabilit&agrave; generale"));
		}
	}
	
	/**
	 * Rifiuto del registro A(prime note verso inventario contabile)
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String rifiuta() {
		final String methodName = "rifiuta";
		RifiutaPrimaNotaSuRegistroACespite req = model.creaRequestRifiutaPrimaNotaSuRegistroACespite();
		RifiutaPrimaNotaSuRegistroACespiteResponse res = primaNotaCespiteService.rifiutaPrimaNotaSuRegistroACespite(req);
		
		if(res.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RifiutaPrimaNotaSuRegistroACespite.class, res));
			addErrori(res);
			return INPUT;
		}
		
		log.info(methodName, "Rifiutata prima nota " + model.getPrimaNota().getUid());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START));
		return SUCCESS;
	}
	
	/**
	 * Validazione del metodo {@link #rifiuta()}
	 */
	public void validateRifiuta() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "prima nota");
	}
	
	/**
	 * Integrazione del registro A(prime note verso inventario contabile)
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String integra() {
		final String methodName = "rifiuta";
		InserisciPrimaNotaSuRegistroACespite req = model.creaRequestInserisciPrimaNotaSuRegistroACespite();
		InserisciPrimaNotaSuRegistroACespiteResponse res = primaNotaCespiteService.inserisciPrimaNotaSuRegistroACespite(req);
		
		if(res.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(InserisciPrimaNotaSuRegistroACespite.class, res));
			addErrori(res);
			return INPUT;
		}
		
		log.info(methodName, "Integrata prima nota " + model.getPrimaNota().getUid());
		
		if(res.getPrimaNotaInventario() == null) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile integrare la prima nota."));
		}
		model.getPrimaNota().setPrimaNotaInventario(res.getPrimaNotaInventario());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START));
		return SUCCESS;
	}
	
	/**
	 * Validazione del metodo {@link #integra()}
	 */
	public void validateIntegra() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "prima nota");
	}
	
	/**
	 * Aggiornamento del registro A(prime note verso inventario contabile)
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START));
		return SUCCESS;
	}
	
	/**
	 * Validazione del metodo {@link #aggiorna()}
	 */
	public void validateAggiorna() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "prima nota");
		checkNotNullNorInvalidUid(model.getPrimaNota().getPrimaNotaInventario(), "prima nota inventario");
	}
	
	/**
	 * Validate verifica integra possibile prima nota registro A.
	 */
	public void validateVerificaIntegraPossibilePrimaNotaRegistroA() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "prima nota");
	}
	
	
	/**
	 * Verifica integra possibile prima nota registro A.
	 *
	 * @return the string
	 */
	public String verificaIntegraPossibilePrimaNotaRegistroA() {
		return checkFatturaNotaCredito(StatoAccettazionePrimaNotaDefinitiva.INTEGRATO);
	}
	
	/**
	 * Validate verifica rifiuto possibile su prima nota registro A.
	 */
	public void validateVerificaRifiutoPossibileSuPrimaNotaRegistroA() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "prima nota");
	}
	
	/**
	 * Verifica rifiuto possibile su prima nota registro A.
	 *
	 * @return the string
	 */
	public String verificaRifiutoPossibileSuPrimaNotaRegistroA() {
		return checkFatturaNotaCredito(StatoAccettazionePrimaNotaDefinitiva.RIFIUTATO);
	}

	/**
	 * @return
	 */
	private String checkFatturaNotaCredito(StatoAccettazionePrimaNotaDefinitiva statoAmmessoPrimeNoteFattura) {
		OttieniDatiPrimeNoteFatturaConNotaCredito request = model.creaRequestOttieniDatiPrimeNoteFatturaConNotaCredito();
		OttieniDatiPrimeNoteFatturaConNotaCreditoResponse response = primaNotaCespiteService.ottieniDatiPrimeNoteFatturaConNotaCredito(request);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		Evento evento = response.getEventoPrimaNota();
		boolean isEventoDocumentoDiSpesa = evento != null &&  evento.getTipoEvento() != null && TipoCollegamento.DOCUMENTO_SPESA.getCodice().equals(evento.getTipoEvento().getCodice());
		if(!TipoCausale.Integrata.equals(response.getTipoCausale()) || !isEventoDocumentoDiSpesa) {
			return SUCCESS;
		}
		DocumentoSpesa documentoSpesa = response.getDocumentoSpesaCheHaGeneratoLaPrimaNota();
		List<PrimaNota> primeNoteGenerateDallaFattura = response.getPrimeNoteGenerateDallaFattura();
		if(documentoSpesa == null || documentoSpesa.getTipoDocumento() == null) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile reperire il documento di spesa collegato alla prima nota"));
			return INPUT;
		}
		
		if( documentoSpesa.getTipoDocumento().isNotaCredito()) {
			return controllaNotaCredito(documentoSpesa, response.getFattureCollegateANotaDiCredito(), primeNoteGenerateDallaFattura, statoAmmessoPrimeNoteFattura);
		} 
		if(documentoSpesa.getTipoDocumento().isFattura()) {
			return controllaFattura(documentoSpesa, response.getNoteCreditoCollegateAFattura(), response.getPrimeNoteGenerateDalleNoteDiCredito());
		}
		
		return SUCCESS;
	}

	/**
	 * @param documentoSpesa
	 * @param fattureCollegate 
	 * @param primeNoteGenerateDallaFattura
	 */
	private String controllaNotaCredito(DocumentoSpesa documentoSpesa, List<DocumentoSpesa> fattureCollegate, List<PrimaNota> primeNoteGenerateDallaFattura, 
			StatoAccettazionePrimaNotaDefinitiva statoAmmessoPrimeNoteFattura) {
		boolean hasFattureCollegate = fattureCollegate != null && !fattureCollegate.isEmpty();
		boolean hasPrimeNoteCollegateAFattura =  primeNoteGenerateDallaFattura != null && !primeNoteGenerateDallaFattura.isEmpty();
		if(!hasFattureCollegate && !hasPrimeNoteCollegateAFattura ) {
			//la nota di credito non e' stata collegata a niente, lascio fare all'utente quello che vuole
			return SUCCESS;
		}
		
		if(hasFattureCollegate && (!hasPrimeNoteCollegateAFattura || fattureCollegate.size() != primeNoteGenerateDallaFattura.size())) {
			//non tutte le fatture collegate sono state contabilizzate in contabilita generale, non posso integrare.
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Prima nota generata da un documento di tipo nota di credito collegata a fatture che non sono ancora state registrate su contabilita generale. Impossibile integrare o rifiutare su inventario."));
			return INPUT;
		}
		
		Map<String, List<String>> mappaStato = ottieniIdentificativiPrimeNoteByStato(primeNoteGenerateDallaFattura);
		
		StatoAccettazionePrimaNotaDefinitiva statoAmmesso = StatoAccettazionePrimaNotaDefinitiva.INTEGRATO;
		
		StringBuilder erroriPrimeNote = new StringBuilder();
		
		erroriPrimeNote.append(creaStringaPrimeNote(mappaStato.get(KEY_PRIMA_NOTA_NON_INVENTARIATA), " prime note non ancora inventariate: "));
		
		for (String statoCode : mappaStato.keySet()) {
			if( statoCode != KEY_PRIMA_NOTA_NON_INVENTARIATA && statoAmmessoPrimeNoteFattura!= null && !statoAmmessoPrimeNoteFattura.getCodice().equals(statoCode)) {
				StatoAccettazionePrimaNotaDefinitiva stato = StatoAccettazionePrimaNotaDefinitiva.byCodice(statoCode);
				erroriPrimeNote.append(creaStringaPrimeNote(mappaStato.get(statoCode), " prime note in stato : " + (stato != null? stato.getDescrizione() : "null" ) + " " ));
			}
		}
		
		if(!erroriPrimeNote.toString().isEmpty()) {

			StringBuilder errore  =  new StringBuilder()
					.append("Prima nota generata da un documento di tipo nota di credito. Per poter essere elaborate, le prime note dei documenti spesa collegati devono essere tutte in stato ")
					.append(statoAmmesso.getDescrizione())
					.append(" ; ")
					.append(erroriPrimeNote);
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(errore));
			
		}
		return SUCCESS;
		
	}
	
	/**
	 * @param documentoSpesa
	 * @param noteCreditoCollegate 
	 * @param primeNoteGenerateDallaNotaCredito
	 */
	private String controllaFattura(DocumentoSpesa documentoSpesa, List<DocumentoSpesa> noteCreditoCollegate, List<PrimaNota> primeNoteGenerateDallaNotaCredito) {
		boolean hasNoteCreditoCollegate = noteCreditoCollegate != null && !noteCreditoCollegate.isEmpty();
		boolean hasPrimeNoteCollegateANotaCredito =  primeNoteGenerateDallaNotaCredito != null && !primeNoteGenerateDallaNotaCredito.isEmpty();
		if(!hasNoteCreditoCollegate || !hasPrimeNoteCollegateANotaCredito) {
			return SUCCESS;
		}
		List<String> identificativiPrimeNote = new ArrayList<String>();
		
		for (PrimaNota pn : primeNoteGenerateDallaNotaCredito) {
			identificativiPrimeNote.add(getChiavePrimaNota(pn));
		}
			
		addMessaggio(new Messaggio("", "Prima nota generata da un documento di spesa collegato ad una nota di credito. Una volta conclusa l'operazione, elaborare anche la prima nota " + StringUtils.join(identificativiPrimeNote, ", ") ));
		return SUCCESS;
	}

	
	/**
	 * Aggiungi errore operazione non consentita.
	 *
	 * @param identificativiPrimeNote the identificativi prime note
	 * @param messaggioErrore the messaggio errore
	 */
	private String creaStringaPrimeNote(List<String> identificativiPrimeNote, String messaggioErrore) {
		StringBuilder sb = new StringBuilder();
		if(identificativiPrimeNote != null && !identificativiPrimeNote.isEmpty()) {
			sb.append(messaggioErrore + StringUtils.join(identificativiPrimeNote, ", "));
		}
		return sb.toString();
	}
	/**
	 * @param primeNoteGenerateDallaFattura
	 * @return
	 */
	private Map<String, List<String>> ottieniIdentificativiPrimeNoteByStato(
			List<PrimaNota> primeNoteGenerateDallaFattura) {
		Map<String, List<String>> mappaStato = new HashMap<String, List<String>>();
		
		for (PrimaNota pnFattura : primeNoteGenerateDallaFattura) {
			PrimaNota pnotaInventario = pnFattura.getPrimaNotaInventario();
			if(pnotaInventario == null || pnotaInventario.getUid() == 0 ) {
				aggiungiAMappa(mappaStato, KEY_PRIMA_NOTA_NON_INVENTARIATA,getChiavePrimaNota(pnFattura));
				continue;
			}
			aggiungiAMappa(mappaStato, pnotaInventario.getStatoAccettazionePrimaNotaDefinitiva().getCodice(), getChiavePrimaNota(pnFattura));
		}
		return mappaStato;
	}
	
	private void aggiungiAMappa(Map<String, List<String>> mappaStato, String key, String value) {
		if(mappaStato == null) {
			return;
		}
		if(mappaStato.get(key) == null) {
			mappaStato.put(key,  new ArrayList<String>());
		}
		mappaStato.get(key).add(value);
	}

	/**
	 * @param pnFattura
	 * @return
	 */
	private String getChiavePrimaNota(PrimaNota pnFattura) {
		boolean definitiva = pnFattura.getNumeroRegistrazioneLibroGiornale() != null && pnFattura.getNumeroRegistrazioneLibroGiornale() != 0;
		String chiavePrimaNota = new StringBuilder()
				.append(" Provvisorio ")
				.append(pnFattura.getNumero())
				.append(definitiva ? " e definitivo " : "")
				.append(definitiva ? pnFattura.getNumeroRegistrazioneLibroGiornale() : "" )
				.toString();
		return chiavePrimaNota;
	}

}
