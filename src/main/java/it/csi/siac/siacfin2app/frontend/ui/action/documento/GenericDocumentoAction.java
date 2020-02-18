/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.GenericDocumentoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Classe di Action per la gestione della ricerca del Documento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 * 
 * @param <M> la tipizzazione del Model
 */
public abstract class GenericDocumentoAction <M extends GenericDocumentoModel> extends GenericBilancioAction<M> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	/** Serviz&icirc; del documetno */
	@Autowired protected transient DocumentoService documentoService;
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	
	/**
	 * Controlla se la lista dei Tipo Documento sia presente in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 * 
	 * @param tipoFamigliaDocumento  il tipo di famiglia del documento
	 * @param flagSubordinato        se il documento sia subordinato
	 * @param flagRegolarizzazione   se il documento sia di tipo regolarizzazione
	 */
	protected void checkAndObtainListaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(tipoFamigliaDocumento, flagSubordinato, flagRegolarizzazione);
		RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
		if(!response.hasErrori()) {
				model.setListaTipoDocumento(response.getElencoTipiDocumento());
				//creo un clone x avere sempre i dati iniziali corretti della lista, per evitare problemi di sovrascrittura dati in sessione
				model.setListaTipoDocumentoClone(ReflectionUtil.deepClone(model.getListaTipoDocumento()));
		}
		
	}
	
	/**
	 * Controlla se la lista dei Codice Bollo sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i codici.
	 */
	protected void checkAndObtainListaCodiceBollo() {
		List<CodiceBollo> listaCodiceBollo = model.getListaCodiceBollo();
		if(!listaCodiceBollo.isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaCodiceBollo request = model.creaRequestRicercaCodiceBollo();
		RicercaCodiceBolloResponse response = documentoService.ricercaCodiceBollo(request);
		if(!response.hasErrori()) {
			//ho ottenuto dal servizio una lista. La setto nel model
			listaCodiceBollo = response.getElencoCodiciBollo();
			model.setListaCodiceBollo(listaCodiceBollo);
		}
	}
	
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 */
	protected void checkAndObtainListaClassiSoggetto() {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			//la lista non e' presente in sessione, la devo ricaricare da servizio
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				listaClassiSoggetto = response.getListaClasseSoggetto();
				//ordino la lista per codice
				ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
				//setto la lista ordinata in sessione per successivi utilizzi
				sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
			}
		}
		//setto la lista in sessione, sia che io l'abbia ottenuta da servizio o da sessione
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}

	
	/**
	 * Carica la lista dei tipi di atto.
	 */
	protected void checkAndObtainListaTipiAtto() {
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaTipoAtto == null) {
			//la lista non e' presente in sessione, la devo ricaricare da servizio
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
			}
			//ordino la lista per codice
			ComparatorUtils.sortByCodice(listaTipoAtto);
			//setto la lista ordinata in sessione per successivi utilizzi
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		//setto la lista nel model, sia che io l'abbia ottenuta da servizio o da sessione
		model.setListaTipoAtto(listaTipoAtto);
	}
	
	/**
	 * Carica la lista stati operativi del documento (per ora necessaria solo alla ricerca)
	 */
	protected void caricaListaStati() {
		final String methodName = "caricaListaStati";
		log.debug(methodName, "Caricamento della lista degli stati operativi del Documento");
		//gli stati possibili sono tutti gli stati del documento
		List<StatoOperativoDocumento> listaStato = Arrays.asList(StatoOperativoDocumento.values());
		model.setListaStatoOperativoDocumento(listaStato);
	}
	
	/**
	 * Validazione di esistenza e unicit&agrave; per il Soggetto.
	 * 
	 * @param controlloValiditaSoggetto Se si debba effettuare il controllo sulla validit&agrave; del soggetto.
	 */
	protected void validaSoggetto(boolean controlloValiditaSoggetto) {
		final String methodName = "validaSoggetto";
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		
		//il servizio ha avuto successo, controllo cio' che ho ottenuto
		Soggetto soggetto = response.getSoggetto();
		if(soggetto == null) {
			//il soggetto non e' stato trovato
			log.info(methodName, "Soggetto == null");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			return;
		}
		
		if(controlloValiditaSoggetto) {
			//devo controllare la validita' del soggetto
			checkCondition(StatoOperativoAnagrafica.VALIDO.equals(soggetto.getStatoOperativo())
							|| StatoOperativoAnagrafica.SOSPESO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
		}
		//setto nel model
		model.setSoggetto(soggetto);
	}
	
	
	/**
	 * Validazione di unicit&agrave; per il Provvedimento.
	 */
	protected void verificaUnicitaProvvedimento() {
		// Controllo se tra i parametri di ricerca io abbia o meno impostato la SAC
		boolean ricercaConSac = model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0;
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			// Si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		List<AttoAmministrativo> lista = response.getListaAttiAmministrativi();
		if(lista.isEmpty()){
			// Non ho trovato nessun provvedimento
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
			return;
		}
		if(lista.size() == 1){
			//ho trovato un solo provvedimento per i criteri impostati: lo prendo ed esco.
			model.setAttoAmministrativo(lista.get(0));
			return;
		}
		// La SAC e' chiave del provvedimento ma e' un campo facoltativo.
		
		if(ricercaConSac){
			// Ho trovato piu' di un atto pur filtrando la SAC: e' sicuramente un errore
			addErrore(ErroreFin.PIU_RISULTATI_TROVATI.getErrore());
			return;
		}
		//ho effettuato ricerca senza sac, vedo se ne esiste solo 1 senza sac per prenderlo
		List<AttoAmministrativo> listaSenzaSac = new ArrayList<AttoAmministrativo>();
		for(AttoAmministrativo atto: lista){
			if(atto.getStrutturaAmmContabile() == null || atto.getStrutturaAmmContabile().getUid() == 0){
				// aggiungo alla lista solo gli atti senza SAC
				listaSenzaSac.add(atto);
			}
		}
		if(!listaSenzaSac.isEmpty() && listaSenzaSac.size() == 1){
			//esiste un solo atto amministrativo senza sac tra quelli trovati: lo prendo
			model.setAttoAmministrativo(listaSenzaSac.get(0));
		}else{
			//non ho modo di stabilire quale atto selezionare: restituisco un errores
			addErrore(ErroreFin.PIU_RISULTATI_TROVATI.getErrore());
		}
	}
	
	/**
	 * Controlla se il provvedimento sia esistente, e pertanto da cercare.
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; esistente; <code>false</code> in caso contrario
	 */
	protected boolean checkProvvedimentoEsistente() {
		return (model.getUidProvvedimento() != null && model.getUidProvvedimento() != 0) ||
			(
				model.getAttoAmministrativo() != null &&
					(
						model.getAttoAmministrativo().getAnno() != 0 &&
						(
							model.getAttoAmministrativo().getNumero() != 0 ||
							(model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0)
						)
					)
			);
	}
	
	/**
	 * Validazione dei campi relativi al provvedimento
	 * 
	 * @param attoAmministrativo il provvedimento da validare
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */	
	protected boolean checkFormProvvedimentoValido(AttoAmministrativo attoAmministrativo) {
		// Check sul provvedimento
		boolean checkProvvedimento = false;
		if(attoAmministrativo == null) {
			// Se l'atto è null, non sono stati inizializzati i vari campi obbligatori
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno"));
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero"));
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Atto"));
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Struttura Amministrativa"));
		} else {
			checkCondition(attoAmministrativo.getAnno() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno", "Minore di zero"));
			checkCondition(attoAmministrativo.getNumero() > 0 || (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero o Tipo Atto"));
			checkProvvedimento = true;
			// verificare se basta questo check / probabilmente anche struttura amministrativa può essere criterio di ricerca	
		}
		return checkProvvedimento;
	}
	
	/**
	 * Metodo per la pulizia del model.
	 * <br>
	 * L'esempio di applicazione del metodo &eacute; per la ripetizione del caso d'uso: si puliscono i dati presenti,
	 * e si permette all'utente di re-iniziare con la creazione.
	 */
	protected void cleanModel() {
		//pulisco i dati nel modello in modo da non averne di sporchi
		model.setSoggetto(null);
		model.reinizializzaNetto();
		model.setListaTipoDocumento(new ArrayList<TipoDocumento>());
		model.setListaClasseSoggetto(new ArrayList<CodificaFin>());
		model.setListaCodiceBollo(new ArrayList<CodiceBollo>());
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		//il bilancio NON puo' essere PLURIENNALE, PREVISIONE, CHIUSO
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
				FaseBilancio.GESTIONE.equals(faseBilancio) ||
				FaseBilancio.ASSESTAMENTO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}

	/**
	 * Per evitare che gli utenti inseriscano documenti con i dati di protocollo valorizzati in maniera parziale si richiede di aggiungere un controllo,
	 * di tipo warning che, con questo algoritmo:
	 * <br/>
	 * Se &eacute; valorizzato almeno uno dei campi della sezione repertorio/protocollo (anno, numero, registro o data) ma non sono stati valorizzati
	 * tutti e quattro si segnali, con apposito messaggio di warning il fatto che non sono stati valorizzati tutti i campi relativi alla sezione repertorio/protocollo.
	 * <br/>
	 * Il controllo deve essere non bloccante.
	 * @param documento il documento di spesa da controllare
	 */
	protected void checkProtocollo(Documento<?, ?> documento) {
		final String methodName = "checkProtocollo";
		if(documento == null) {
			// Non ho il documento: esco
			log.debug(methodName, "Documento non inizializzato");
			return;
		}
		//controllo i dati di repertorio
		if(StringUtils.isBlank(documento.getRegistroRepertorio())
				&& documento.getAnnoRepertorio() == null
				&& StringUtils.isBlank(documento.getNumeroRepertorio())
				&& documento.getDataRepertorio() == null) {
			// I dati di repertorio non sono stati valorizzati. Va tutto bene ed esco
			log.debug(methodName, "Dati repertorio non inizializzati");
			return;
		}
		boolean dataNumeroRepertorioNonValorizzati = documento.getDataRepertorio() != null ^ StringUtils.isNotEmpty(documento.getNumeroRepertorio());
		if(dataNumeroRepertorioNonValorizzati) {
			// Data e numero non sono valorizzati. Ma questo e' un errore: non faccio i controlli sul warning
			log.debug(methodName, "Data e numero repertorio non valorizzati, errore gia' presente");
			return;
		}
		
		warnCondition(StringUtils.isNotBlank(documento.getRegistroRepertorio())
				&& documento.getAnnoRepertorio() != null
				&& StringUtils.isNotBlank(documento.getNumeroRepertorio())
				&& documento.getDataRepertorio() != null,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("Dati repertorio/protocollo", ": Registro, Anno, Numero e Data devono essere tutti valorizzati o tutti non valorizzati"));
	}
	/**
	 * SIAC-6565
	 * Carica la lista stati SDI del documento
	 */
	protected void caricaListaStatiSDI() {
		final String methodName = "caricaListaStatiSDI";
		log.debug(methodName, "Caricamento della lista degli stati SDI del Documento");
		List<StatoSDIDocumento> listaStato = Arrays.asList(StatoSDIDocumento.values());
		model.setListaStatoSDIDocumento(listaStato);
	}
	
	
	/**
	 * SIAC-6677
	 * Controlla se il campo di testo &eacute; composto da soli numeri
	 * @param strNum la stringa da controllare
	 * @return se la stringa &eacute; composta da soli numeri
	 */
	protected boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}
}
