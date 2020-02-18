/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.handler.session;

import it.csi.siac.siaccommonapp.handler.session.SessionParameter;

/**
 * Enumeration contenente i parametri della sessione.
 * 
 * @author Domenico Lisi, Alessandro Marchino, Luciano Gallo, Massimo Venesia, Daniele Argiolas
 * @version 1.0.0 24/07/2013
 *
 */
@SuppressWarnings("javadoc")
public enum BilSessionParameter implements SessionParameter {
	
	/* Parametri generati da BIL */
	
	// Richiesta di non pulizia del model
	NON_PULIRE_MODEL,
	
	FASE_BILANCIO,
	FASE_BILANCIO_PRECEDENTE,
	/* Liste */
	LISTA_MISSIONE,
	LISTA_TITOLO_SPESA,
	LISTA_TITOLO_SPESA_ORIGINALE,
	LISTA_TITOLO_ENTRATA,
	LISTA_CATEGORIA_CAPITOLO_CAPITOLO_USCITA_PREVISIONE,
	LISTA_CATEGORIA_CAPITOLO_CAPITOLO_USCITA_GESTIONE,
	LISTA_CATEGORIA_CAPITOLO_CAPITOLO_ENTRATA_PREVISIONE,
	LISTA_CATEGORIA_CAPITOLO_CAPITOLO_ENTRATA_GESTIONE,
	LISTA_TIPO_FINANZIAMENTO,
	LISTA_TIPO_FONDO,
	LISTA_SIOPE_TIPO_DEBITO,
	LISTA_SIOPE_ASSENZA_MOTIVAZIONE,
	// Fase 1.10.0: aggiunta classificatori generici. 1-10 + 31-35 SPESA ; 36-50 ENTRATA
	LISTA_CLASSIFICATORE_GENERICO_1(false),
	LISTA_CLASSIFICATORE_GENERICO_2(false),
	LISTA_CLASSIFICATORE_GENERICO_3(false),
	LISTA_CLASSIFICATORE_GENERICO_4(false),
	LISTA_CLASSIFICATORE_GENERICO_5(false),
	LISTA_CLASSIFICATORE_GENERICO_6(false),
	LISTA_CLASSIFICATORE_GENERICO_7(false),
	LISTA_CLASSIFICATORE_GENERICO_8(false),
	LISTA_CLASSIFICATORE_GENERICO_9(false),
	LISTA_CLASSIFICATORE_GENERICO_10(false),
	LISTA_CLASSIFICATORE_GENERICO_31(false),
	LISTA_CLASSIFICATORE_GENERICO_32(false),
	LISTA_CLASSIFICATORE_GENERICO_33(false),
	LISTA_CLASSIFICATORE_GENERICO_34(false),
	LISTA_CLASSIFICATORE_GENERICO_35(false),
	
	LISTA_CLASSIFICATORE_GENERICO_36(false),
	LISTA_CLASSIFICATORE_GENERICO_37(false),
	LISTA_CLASSIFICATORE_GENERICO_38(false),
	LISTA_CLASSIFICATORE_GENERICO_39(false),
	LISTA_CLASSIFICATORE_GENERICO_40(false),
	LISTA_CLASSIFICATORE_GENERICO_41(false),
	LISTA_CLASSIFICATORE_GENERICO_42(false),
	LISTA_CLASSIFICATORE_GENERICO_43(false),
	LISTA_CLASSIFICATORE_GENERICO_44(false),
	LISTA_CLASSIFICATORE_GENERICO_45(false),
	LISTA_CLASSIFICATORE_GENERICO_46(false),
	LISTA_CLASSIFICATORE_GENERICO_47(false),
	LISTA_CLASSIFICATORE_GENERICO_48(false),
	LISTA_CLASSIFICATORE_GENERICO_49(false),
	LISTA_CLASSIFICATORE_GENERICO_50(false),
	
	LISTA_CLASSIFICATORE_GENERICO_51(false),
	LISTA_CLASSIFICATORE_GENERICO_52(false),
	LISTA_CLASSIFICATORE_GENERICO_53(false),
	
	LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE,
	
	LISTA_RICORRENTE_SPESA,
	LISTA_RICORRENTE_ENTRATA,
	LISTA_PERIMETRO_SANITARIO_SPESA,
	LISTA_PERIMETRO_SANITARIO_ENTRATA,
	LISTA_TRANSAZIONE_UNIONE_EUROPEA_SPESA,
	LISTA_TRANSAZIONE_UNIONE_EUROPEA_ENTRATA,
	LISTA_POLITICHE_REGIONALI_UNITARIE,
	
	/* Liste da chiamata AJAX */
	LISTA_PROGRAMMA,
	LISTA_CLASSIFICAZIONE_COFOG,
	LISTA_MACROAGGREGATO,
	LISTA_ELEMENTO_PIANO_DEI_CONTI,
	LISTA_TIPOLOGIA_TITOLO,
	LISTA_CATEGORIA_TIPOLOGIA_TITOLO,
	LISTA_UEB,
	LISTA_SIOPE_ENTRATA,
	LISTA_SIOPE_SPESA,
	
	// Provvedimento
	LISTA_FILTRATA_TIPO_ATTO_AMMINISTRATIVO,
	LISTA_TIPO_ATTO_AMMINISTRATIVO,
	// Atto di legge
	LISTA_TIPO_ATTO_DI_LEGGE,
	
	// Tipo Ambito
	LISTA_TIPO_AMBITO,

	// Soggetto
	LISTA_CLASSI_SOGGETTO,
	LISTA_SEDE_SECONDARIA_SOGGETTO,
	LISTA_MODALITA_PAGAMENTO_SOGGETTO,
	
	// Tipo Documento
	LISTA_TIPO_DOCUMENTO,
	
	/* liste per le ritenute */
	LISTA_NATURA_ONERE,
	LISTA_TIPO_ONERE,
	LISTA_ATTIVITA_ONERE,
	LISTA_CAUSALE_770,
	LISTA_SOMME_NON_SOGGETTE,
	
	/* Liste per il PreDocumento */
	LISTA_TIPO_CAUSALE_SPESA,
	LISTA_TIPO_CAUSALE_ENTRATA,
	LISTA_CONTO_TESORERIA,
	LISTA_NAZIONE,
	LISTA_COMUNE,
	LISTA_CONTO_CORRENTE,
	LISTA_CAUSALE_SPESA,
	LISTA_CAUSALE_ENTRATA,
	// SIAC-4492
	LISTA_CAUSALE_ENTRATA_NON_ANNULLATE,
	
	// Gruppo Attivita Iva
	LISTA_GRUPPO_ATTIVITA_IVA,
	// Registro Iva
	LISTA_TIPO_REGISTRO_IVA,
	LISTA_REGISTRO_IVA,
	
	// Liste per il documento IVA
	LISTA_VALUTA,
	LISTA_CODICE_BOLLO,
	LISTA_TIPO_REGISTRAZIONE_IVA,
	/* FILTRI */
		LISTA_TIPO_REGISTRAZIONE_IVA_FILTRO_USCITA,
		LISTA_TIPO_REGISTRAZIONE_IVA_FILTRO_ENTRATA,
	/* FINE FILTRI */
	LISTA_ATTIVITA_IVA,
	LISTA_ATTIVITA_IVA_COMPLETE,
	LISTA_ALIQUOTA_IVA,
	
	// Allegato Atto
	LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO,
	LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO,
	
	// Ordinativi
	LISTA_DISTINTA,
	
	/* Model per l'inserimento */
	MODEL_INSERIMENTO_USCITA_PREVISIONE,
	MODEL_INSERIMENTO_ENTRATA_PREVISIONE,
	
	/* Nuovi dati Ricerca */
	RIEPILOGO_RICERCA_DOCUMENTO,
	IMPORTO_TOTALE_RICERCA,
	RIEPILOGO_RICERCA_PREDOCUMENTO,
	RIEPILOGO_RICERCA_CAUSALE,
	RIEPILOGO_RICERCA_DOCUMENTO_IVA,
	RIEPILOGO_RICERCA_TESTATA_DOCUMENTO,
	IMPORTO_TOTALE_RICERCA_PREDOCUMENTO,
	RIEPILOGO_RICERCA_ALLEGATO_ATTO,
	
	/* Risultati della ricerca sintetica */
	/* Non eliminabili, si' da non generare problemi in caso di rientro nei risultati di ricerca */
	// Capitolo
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE(false),
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE(false),
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE(false),
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE_IMPORTI(false),
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE_IMPORTI(false),
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE_IMPORTI(false),
	RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE_IMPORTI(false),




	RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE(false),
	// Storno
	RISULTATI_RICERCA_STORNO(false),
	// Provvedimento
	RISULTATI_RICERCA_PROVVEDIMENTO(false),
	// Variazioni
	RISULTATI_RICERCA_VARIAZIONI(false),
	RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE(false),
	INSERIMENTO_DA_VARIAZIONE(false),
	//CR-3956
	RISULTATI_RICERCA_STORICO_VARIAZIONI_CODIFICHE_CAPITOLO(false),
	REQUEST_RICERCA_STORICO_VARIAZIONI_CODIFICHE_CAPITOLO(false),
	// Vincolo
	RISULTATI_RICERCA_VINCOLO(false),
	// Progetto
	RISULTATI_RICERCA_PROGETTO(false),
	//SIAC-6255
	LISTA_MODALITA_AFFIDAMENTO,
	LISTA_QUADRO_ECONOMICO_CRONOPROGRAMMA,
	// Cronoprogrammi nel progetto
	RISULTATI_RICERCA_CRONOPROGRAMMI_NELPROGETTO(false),
	// Documenti
	RISULTATI_RICERCA_DOCUMENTI_SPESA(false),
	RISULTATI_RICERCA_DOCUMENTI_ENTRATA(false),
	// Quote
	RISULTATI_RICERCA_QUOTE_SPESA(false),
	RISULTATI_RICERCA_QUOTE_DA_EMETTERE_SPESA(false),
	RISULTATI_RICERCA_QUOTE_ENTRATA(false),
	RISULTATI_RICERCA_QUOTE_DA_EMETTERE_ENTRATA(false),
	RISULTATI_RICERCA_QUOTE_DA_ASSOCIARE(),
	// Causali
	RISULTATI_RICERCA_CAUSALI_SPESA(false),
	RISULTATI_RICERCA_CAUSALI_ENTRATA(false),
	// PreDocumenti
	RISULTATI_RICERCA_PREDOCUMENTI_SPESA(false),
	RISULTATI_RICERCA_PREDOCUMENTI_ENTRATA(false),
	// GruppoAttivitaIva
	RISULTATI_RICERCA_GRUPPO_ATTIVITA_IVA(false),
	// RegistroIva
	RISULTATI_RICERCA_REGISTRO_IVA(false),
	// Documenti IVA
	RISULTATI_RICERCA_DOCUMENTI_IVA_SPESA(false),
	RISULTATI_RICERCA_DOCUMENTI_IVA_ENTRATA(false),
	// Testata
	RISULTATI_RICERCA_TESTATA_DOCUMENTI_SPESA(false),
	RISULTATI_RICERCA_TESTATA_DOCUMENTI_ENTRATA(false),
	// AllegatoAtto
	RISULTATI_RICERCA_ALLEGATO_ATTO(false),
	//SIAC-5584
	RISULTATI_RICERCA_ALLEGATO_ATTO_MULT(false),
	// ElencoDocumentiAllegato
	RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO(false),
	RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE(false),
	// ProvvisorioDiCassa
	RISULTATI_RICERCA_PROVVISORIO_DI_CASSA(false),
	// TipoOnere
	RISULTATI_RICERCA_TIPO_ONERE(false),
	// StampaIva
	RISULTATI_RICERCA_STAMPA_IVA(false),
	// Stampa AllegatoAtto
	RISULTATI_RICERCA_STAMPA_ALLEGATO_ATTO,
		
	/* Model */
	MODEL_CONSULTA_CAPITOLO(false),
	
	/* Risultati della ricerca di dettaglio */
	LISTA_UEB_COLLEGATE(false),
	REQUEST_RICERCA_DETTAGLIO_CAPITOLO_MASSIVO(false),
	
	/* Risultati delle ricerche massive come dato aggiuntivo */
	RISULTATI_RICERCA_MASSIVA_COME_DATO_AGGIUNTIVO(false),
	
	/* Wrappers */
	CLASSIFICATORI_AGGIORNAMENTO,
	EDITABILITA_CLASSIFICATORI,
	EDITABILITA_ATTRIBUTI,
	/* Non eliminabile, si' da non generare problemi in caso di rientro nei risultati di ricerca */
	
	// Capitolo
	REQUEST_RICERCA_SINTETICA_CAPITOLO (false),
	REQUEST_RICERCA_SINTETICA_MASSIVA_CAPITOLO (false),
	
	// Storno
	REQUEST_RICERCA_STORNO(false),
	// Provvedimento
	REQUEST_RICERCA_PROVVEDIMENTO(false),
	// Variazioni
	REQUEST_RICERCA_VARIAZIONI(false),
	REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE(false),
	RISULTATI_CONSULTA_DETTAGLIO_VARIAZIONI,
	// Vincolo
	REQUEST_RICERCA_VINCOLO(false),
	// Progetto
	REQUEST_RICERCA_PROGETTO(false),
	// CronoProgrammi nel Progetto
	REQUEST_RICERCA_CRONOPROGRAMMI_NELPROGETTO(false),
	// Documento
	REQUEST_RICERCA_DOCUMENTI_SPESA(false),
	REQUEST_RICERCA_DOCUMENTI_ENTRATA(false),
	// Quote
	REQUEST_RICERCA_QUOTE_SPESA(false),
	REQUEST_RICERCA_QUOTE_DA_EMETTERE_SPESA(false),
	REQUEST_RICERCA_QUOTE_ENTRATA(false),
	REQUEST_RICERCA_QUOTE_DA_EMETTERE_ENTRATA(false),
	REQUEST_RICERCA_QUOTE_DA_ASSOCIARE,
	// PreDocumento
	REQUEST_RICERCA_PREDOCUMENTI_SPESA(false),
	REQUEST_RICERCA_PREDOCUMENTI_ENTRATA(false),
	// Causali
	REQUEST_RICERCA_CAUSALI_SPESA(false),
	REQUEST_RICERCA_CAUSALI_ENTRATA(false),
	// GruppoAttivitaIva
	REQUEST_RICERCA_GRUPPO_ATTIVITA_IVA(false),
	// RegistroIva
	REQUEST_RICERCA_REGISTRO_IVA(false),
	// Documento Iva
	REQUEST_RICERCA_DOCUMENTI_IVA_SPESA(false),
	REQUEST_RICERCA_DOCUMENTI_IVA_ENTRATA(false),
	// Testata Documento
	REQUEST_RICERCA_TESTATA_DOCUMENTI_SPESA(false),
	REQUEST_RICERCA_TESTATA_DOCUMENTI_ENTRATA(false),
	// AllegatoAtto
	REQUEST_RICERCA_ALLEGATO_ATTO(false),
	// SIAC-5584
	REQUEST_RICERCA_ALLEGATO_ATTO_MULT(false),
	FLAG_ATTO_AUTOMATICO(false),
	//ElencoQuoteDocumentiAllegato
	ELENCO_DOCUMENTI_ALLEGATO_LIGHT,
	RESPONSE_RICERCA_DETTAGLIO_QUOTE_ELENCO,
	REQUEST_RICERCA_DETTAGLIO_QUOTE_ELENCO,
	RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO,
	// ElencoDocumentiAllegato
	REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO(false),
	REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE(false),
	// ProvvisorioDiCassa
	REQUEST_RICERCA_PROVVISORIO_DI_CASSA(false),
	// TipoOnere
	REQUEST_RICERCA_TIPO_ONERE(false),
	// Stampa Iva
	REQUEST_RICERCA_STAMPA_IVA(false),
	//Stampa Allegato Atto
	REQUEST_RICERCA_STAMPA_ALLEGATO_ATTO,
	/* 
	 * Parametri per la gestione del rientro alla visualizzazione dei risultati di ricerca dopo un AGGIORNA
	 * un ANNULLA o un ELIMINA. 
	 */
	RIENTRO,
	// Impostata a false per evitare che sia cancellata da eventuali azioni successive ai risultati di ricerca
	RIENTRO_POSIZIONE_START(false),
	RIENTRO_POSIZIONE_START_CONSULTAZIONE(false),
	
	/* Models */
	/* variazioni */
	MODEL_INSERISCI_VARIAZIONE_IMPORTI("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.variazione.InserisciVariazioneImportiAction", false),
	MODEL_INSERISCI_VARIAZIONE_IMPORTI_UEB("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.variazione.InserisciVariazioneImportiUEBAction", false),
	MODEL_INSERISCI_VARIAZIONE_CODIFICHE("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.variazione.InserisciVariazioneCodificheAction", false),
	MODEL_AGGIORNA_VARIAZIONE_IMPORTI("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.variazione.AggiornaVariazioneImportiAction", false),
	MODEL_AGGIORNA_VARIAZIONE_IMPORTI_UEB("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.variazione.AggiornaVariazioneImportiUEBAction", false),
	MODEL_AGGIORNA_VARIAZIONE_CODIFICHE("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.variazione.AggiornaVariazioneCodificheAction", false),
	/* Progetto */
	MODEL_INSERISCI_CRONOPROGRAMMA("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.progetto.InserisciCronoprogrammaAction", false),
	MODEL_AGGIORNA_CRONOPROGRAMMA("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.progetto.AggiornaCronoprogrammaAction", false),
	UID_PROGETTO,
	/* Documento */
	MODEL_INSERISCI_DOCUMENTO_SPESA("PutModelInSessionInterceptor_it.csi.siac.siacfin2app.frontend.ui.action.documento.InserisciDocumentoSpesaAction", false),
	MODEL_INSERISCI_DOCUMENTO_ENTRATA("PutModelInSessionInterceptor_it.csi.siac.siacfin2app.frontend.ui.action.documento.InserisciDocumentoEntrataAction", false),
	
	MODEL_INSERISCI_DOCUMENTO_IVA_SPESA("PutModelInSessionInterceptor_it.csi.siac.siacfin2app.frontend.ui.action.documentoiva.InserisciDocumentoIvaSpesaAction", false),
	MODEL_INSERISCI_DOCUMENTO_IVA_ENTRATA("PutModelInSessionInterceptor_it.csi.siac.siacfin2app.frontend.ui.action.documentoiva.InserisciDocumentoIvaEntrataAction", false),
	
	/* Progetto */
	MODEL_INSERIMENTO_PROGETTO("PutModelInSessionInterceptor_it.csi.siac.siacbilapp.frontend.ui.action.progetto.InserisciProgettoAction", true),
	
	/* provvedimento */
	ATTO_AMMINISTRATIVO,
	
	/* Varie */
	TIPO_VARIAZIONE(false),
	
	INSERIMENTO_NUOVA_UEB(false),
	INSERISCI_NUOVO_DA_AGGIORNAMENTO(false),
	INSERIMENTO_DA_RICERCA(false),
	
	ERRORI_AZIONE_PRECEDENTE(false),
	MESSAGGI_AZIONE_PRECEDENTE(false),
	INFORMAZIONI_AZIONE_PRECEDENTE(false),
	AGGIORNAMENTO_DA_RICERCA(false),
	
	PREDOCUMENTO_STATO_OPERATIVO(false),
	PREDOCUMENTO_CAUSALE_MANCANTE(false),
	PREDOCUMENTO_CONTO_MANCANTE(false),
	
	TAB_VISUALIZZAZIONE_ALLEGATO_ATTO,
	
	// Elementi da pulire
	DA_PULIRE(false),
	
	// Dati obbligatori qualora le liste non siano paginate
	NUMERO_PAGINA_SERVIZI_FIN,
	NUMERO_RISULTATI_SERVIZI_FIN,
	
	
	// Soggetto e MovimentoGestione
	SOGGETTO,
	IMPEGNO,
	SUBIMPEGNO,
	ACCERTAMENTO,
	SUBACCERTAMENTO,
	ORDINATIVO_INCASSO,
	ORDINATIVO_PAGAMENTO,
	RICHIESTA_ECONOMALE,
	RENDICONTO_RICHIESTA,
	LIQUIDAZIONE,
	MODIFICA_MOVIMENTO_GESTIONE_SPESA,
	MODIFICA_MOVIMENTO_GESTIONE_ENTRATA,
	
	//integrazione servizio ricercaImpegnoPerChiaveOttimizzato
	REQUEST_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI,
	RISULTATI_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI,
	//integrazione servizio ricercaAccertamentoPerChiaveOttimizzato
	REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI,
	RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI,
	
	// Cassa Economale
	CASSA_ECONOMALE,
	CASSA_ECONOMALE_TEMP,
	
	LISTA_MODALITA_PAGAMENTO_CASSA_ECONOMALE(false),
	LISTA_MODALITA_PAGAMENTO_DIPENDENTE(false),
	LISTA_TIPO_GIUSTIFICATIVO_ANTICIPO(false),
	LISTA_TIPO_GIUSTIFICATIVO_ANTICIPO_MISSIONE(false),
	LISTA_TIPO_GIUSTIFICATIVO_RIMBORSO(false),
	LISTA_TIPO_GIUSTIFICATIVO_PAGAMENTO(false),
	LISTA_MEZZI_DI_TRASPORTO(false),
	LISTA_TIPO_RICHIESTA_ECONOMALE(false),
	LISTA_MISSIONE_CARICAMENTO_ESTERNO(false),
	
	RISULTATI_RICERCA_TIPO_OPERAZIONE_DI_CASSA(false),
	RISULTATI_RICERCA_TIPO_GIUSTIFICATIVO(false),
	RISULTATI_RICERCA_OPERAZIONE_CASSA(false),
	RISULTATI_RICERCA_RICHIESTA_ECONOMALE(false),
	RISULTATI_RICERCA_MODULARE_RICHIESTA_ECONOMALE(false),
	RISULTATI_RICERCA_STAMPE_CEC,
	
	REQUEST_RICERCA_TIPO_OPERAZIONE_DI_CASSA(false),
	REQUEST_RICERCA_TIPO_GIUSTIFICATIVO(false),
	REQUEST_RICERCA_OPERAZIONE_CASSA(false),
	REQUEST_RICERCA_RICHIESTA_ECONOMALE(false),
	REQUEST_RICERCA_MODULARE_RICHIESTA_ECONOMALE(false),
	REQUEST_RICERCA_STAMPE_CEC,
	
	
	//Contabililta' generale
	RISULTATI_RICERCA_FIGLI_CONTO,
	
	REQUEST_RICERCA_CONTO,
	
	CONTO_PADRE,
	RISULTATI_RICERCA_CONTO_COMP,
	REQUEST_RICERCA_CONTO_COMP,
	
	//CAUSALE EP
	RISULTATI_RICERCA_CAUSALE_GEN,
	REQUEST_RICERCA_CAUSALE_GEN,
	RISULTATI_RICERCA_CAUSALE_GSA,
	REQUEST_RICERCA_CAUSALE_GSA,
	
	
	LISTA_TIPO_CONTO,
	LISTA_CATEGORIA_CESPITI,
	LISTA_TIPO_LEGAME,
	LISTA_CODICE_BILANCIO_GEN,
	LISTA_CODICE_BILANCIO_GSA,
	LISTA_TIPO_EVENTO,
	LISTA_EVENTO,
	LISTA_EVENTO_FULL,
	
	LISTA_CLASSE_PIANO_GSA,
	LISTA_CLASSE_PIANO_GEN,
	// Classificatori EP
	LISTA_VALORE_BENE,
	LISTA_MODALITA_AQUISIZIONE_BENE,
	LISTA_TIPO_DOCUMENTO_COLLEGATO,
	LISTA_TIPO_ONERE_FISCALE,
	LISTA_RILEVANTE_IVA,
	
	CONTI_FIGLI_SENZA_FIGLI,
	CONTO,
	ULTIMO_UID_TIPO_EVENTO_RICERCATO,
	ULTIMO_TIPO_EVENTO_RICERCATO,
	CONTO_DA_PULIRE,
	UID_CLASSE,
	
	//GEN PRIMA NOTA
	LISTA_CAUSALE_EP,
	LISTA_CAUSALE_EP_LIBERA,
	LISTA_CAUSALE_EP_LIBERA_GEN,
	LISTA_CAUSALE_EP_INTEGRATA_GEN,
	REGISTRAZIONEMOVFIN,
	LISTA_TIPO_RELAZIONE,
	
	LISTA_CAUSALE_EP_INTEGRATA_GSA,
	LISTA_CAUSALE_EP_INTEGRATA_MANUALE_GSA,
	LISTA_CAUSALE_EP_LIBERA_GSA,
	
	ULTIMO_EVENTO_RICERCATO,
	RISULTATI_RICERCA_PRIMANOTALIBERA_GEN,
	RISULTATI_RICERCA_PRIMANOTAINTEGRATA_GEN,
	//SIAC-5799
	RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_GEN,
	RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN,
	RISULTATI_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN,
	REQUEST_RICERCA_PRIMANOTALIBERA_GEN,
	REQUEST_RICERCA_PRIMANOTAINTEGRATA_GEN,
	REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN,
	REQUEST_RICERCA_PRIMANOTA_GEN,
	RISULTATI_RICERCA_PRIMANOTA_GEN,
	REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN,
	RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN,
	
	RISULTATI_RICERCA_PRIMANOTAINTEGRATA_GSA,
	//SIAC-5799
	RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_GSA,
	RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA,
	RISULTATI_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA,
	RISULTATI_RICERCA_PRIMANOTALIBERA_GSA,
	RISULTATI_RICERCA_PRIMANOTAINTEGRATAMANUALE_GSA,
	REQUEST_RICERCA_PRIMANOTAINTEGRATA_GSA,
	REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA,
	REQUEST_RICERCA_PRIMANOTALIBERA_GSA,
	REQUEST_RICERCA_PRIMANOTAINTEGRATAMANUALE_GSA,
	REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA,
	RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA,
	
	PRIMA_NOTA_INTEGRATA_DOCUMENTO_RICALCOLA_QUOTE,
	
	//registrazioneMovFin
	REQUEST_RICERCA_REGISTRAZIONI_MOV_FIN_GEN,
	RISULTATI_RICERCA_REGISTRAZIONI_MOV_FIN_GEN,
	RIEPILOGO_RICERCA_REGISTRAZIONE_GEN,
	
	REQUEST_RICERCA_REGISTRAZIONI_MOV_FIN_GSA,
	RISULTATI_RICERCA_REGISTRAZIONI_MOV_FIN_GSA,
	RIEPILOGO_RICERCA_REGISTRAZIONE_GSA,
	
	// fatturaElettronica
	LISTA_CODICE_UFFICIO_DESTINATARIO_PCC,
	LISTA_CODICE_PCC,
	RISULTATI_RICERCA_FATTURA_FEL,
	REQUEST_RICERCA_FATTURA_FEL,
	FATTURA_FEL,
	
	// inserimentoContabilizzazionePCC
	LISTA_STATO_DEBITO_PCC,
	
	// Conciliazioni
	REQUEST_RICERCA_CONCILIAZIONI_PER_TITOLO,
	REQUEST_RICERCA_CONCILIAZIONI_PER_CAPITOLO,
	REQUEST_RICERCA_CONCILIAZIONI_PER_BENEFICIARIO,
	RISULTATI_RICERCA_CONCILIAZIONI_PER_TITOLO,
	RISULTATI_RICERCA_CONCILIAZIONI_PER_CAPITOLO,
	RISULTATI_RICERCA_CONCILIAZIONI_PER_BENEFICIARIO,
	
	// Ricerca clasificatori
	REQUEST_RICERCA_SIOPE_SPESA,
	REQUEST_RICERCA_SIOPE_ENTRATA,
	RISULTATI_RICERCA_SIOPE_SPESA,
	RISULTATI_RICERCA_SIOPE_ENTRATA,
	
	SIOPE_SPESA,
	SIOPE_ENTRATA,
	LISTA_IMPEGNI(false),
	//Ricerca impegni
	RISULTATI_RICERCA_IMPEGNI_SUBIMPEGNI(false),
	REQUEST_RICERCA_IMPEGNI_SUBIMPEGNI(false),

	//Ricerca accertamenti
	RISULTATI_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI(false),
	REQUEST_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI(false),
	
	//associazione quote a provvisori
	RISULTATI_RICERCA_QUOTE_SPESA_PER_PROVVISORIO,
	REQUEST_RICERCA_QUOTE_SPESA_PER_PROVVISORIO,
	RISULTATI_RICERCA_QUOTE_ENTRATA_PER_PROVVISORIO,
	REQUEST_RICERCA_QUOTE_ENTRATA_PER_PROVVISORIO,
	LISTA_UID_PROVVISORI,
	LISTA_PROVVISORI_CASSA_SELEZIONATI,
	TOTALE_PROVVISORI_SELEZIONATI,
	TIPO_PROVVISORIO,

	//CAPITOLI gestione
	RISULTATI_RICERCA_CAPITOLI_USCITA_GESTIONE,
	RISULTATI_RICERCA_CAPITOLI_ENTRATA_GESTIONE,
	REQUEST_RICERCA_CAPITOLI_USCITA_GESTIONE,
	REQUEST_RICERCA_CAPITOLI_ENTRATA_GESTIONE,
	
	//CAPITOLI previsione
	RISULTATI_RICERCA_CAPITOLI_USCITA_PREVISIONE,
	RISULTATI_RICERCA_CAPITOLI_ENTRATA_PREVISIONE,
	REQUEST_RICERCA_CAPITOLI_USCITA_PREVISIONE,
	REQUEST_RICERCA_CAPITOLI_ENTRATA_PREVISIONE,
	
	// SIAC-3940
	RISULTATI_RICERCA_SINTETICA_VARIAZIONI_SINGOLO_CAPITOLO,
	REQUEST_RICERCA_SINTETICA_VARIAZIONI_SINGOLO_CAPITOLO,

	// SIAC-3965
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_IVA,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_IVA,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_NOTA_CREDITO,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_NOTA_CREDITO,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_IVA,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_IVA,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_NOTA_CREDITO,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_NOTA_CREDITO, 
	
	// SIAC-3957
	LISTA_REGISTRO_IVA_STAMPA_MULTIPLA,
	
	// SIAC-4088
	REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA,
	RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA,
	
	// SIAC-4222
	REQUEST_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GEN,
	RISULTATI_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GEN,
	RIEPILOGO_RICERCA_REGISTRAZIONE_MASSIVA_GEN,
	
	REQUEST_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GSA,
	RISULTATI_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GSA,
	RIEPILOGO_RICERCA_REGISTRAZIONE_MASSIVA_GSA,
	
	// SIAC-4280
	ABILITATA_MODIFICA_ASSOCIAZIONE_IMPUTAZIONI_CONTABILI_PREDOCUMENTO_ENTRATA,
	ABILITATA_MODIFICA_ASSOCIAZIONE_IMPUTAZIONI_CONTABILI_PREDOCUMENTO_SPESA,
	CAUSALE_SELEZIONATA_PREDOCUMENTO_ENTRATA,
	CAUSALE_SELEZIONATA_PREDOCUMENTO_SPESA,
	
	// SIAC-4422
	REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO,
	RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO,
	
	// SIAC-4799
	TIPO_DOCUMENTO_STAMPA_CEC,
	REQUEST_RICERCA_SINTETICA_RENDICONTO_CASSA_DA_STAMPARE,
	RISULTATI_RICERCA_SINTETICA_RENDICONTO_CASSA_DA_STAMPARE,
	
	// SIAC-5076
	LISTA_GENERE_VINCOLO,
	
	// SIAC-5154
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_COLLEGATO,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_COLLEGATO,
	RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_COLLEGATO,
	REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_COLLEGATO,
	
	// SIAC-5336
	LISTA_CLASSIFICATORE_GSA,
	RISULTATI_RICERCA_CLASSIFICATORE_GSA(false),
	REQUEST_RICERCA_CLASSIFICATORE_GSA(false),
	//SIAC-5589
	RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO_FILTRATE,
	REQUEST_RICERCA_DETTAGLIO_QUOTE_ELENCO_FILTRATE,
	
	//CESPITI
	LISTA_TIPO_CALCOLO, 
	//CATEGORIA
	RISULTATI_RICERCA_CATEGORIA_CESPITI,
	REQUEST_RICERCA_CATEGORIA_CESPITI,
	
	//CATEGORIA
	RISULTATI_RICERCA_TIPO_BENE_CESPITE,
	REQUEST_RICERCA_TIPO_BENE_CESPITE,
	
	//TIPO BENE
	LISTA_TIPO_BENE_CESPITE,
	
	//CESPITE
	RISULTATI_RICERCA_CESPITE,
	REQUEST_RICERCA_CESPITE,
	LISTA_CLASSIFICAZIONE_GIURIDICA_CESPITE,
	

	//DISMISSIONE
	RISULTATI_RICERCA_CESPITE_DA_DISMISSIONE,
	REQUEST_RICERCA_CESPITE_DA_DISMISSIONE,

	// VARIAZIONE CESPITE
	RISULTATI_RICERCA_VARIAZIONE_CESPITE,
	REQUEST_RICERCA_VARIAZIONE_CESPITE,
	RISULTATI_RICERCA_RIVALUTAZIONE_CESPITE,
	REQUEST_RICERCA_RIVALUTAZIONE_CESPITE,
	RISULTATI_RICERCA_SVALUTAZIONE_CESPITE,
	REQUEST_RICERCA_SVALUTAZIONE_CESPITE,
	//CESPITI PRIMA NOTA
	REQUEST_RICERCA_PRIMANOTALIBERA_INV,
	RISULTATI_RICERCA_PRIMANOTALIBERA_INV,
	LISTA_CAUSALE_EP_LIBERA_INV,

	
	//DISMISSIONI
	RISULTATI_RICERCA_DISMISSIONE_CESPITE,
	REQUEST_RICERCA_DISMISSIONE_CESPITE,

	//SIAC-6206
	LISTA_CLASSIFICATORE_STIPENDI, 
	//Ammortamento
	REQUEST_RICERCA_DETTAGLIO_AMMORTAMENTO, 
	RISULTATI_RICERCA_DETTAGLIO_AMMORTAMENTO, 
	REQUEST_RICERCA_ANTEPRIMA_AMMORTAMENTO, 
	RISULTATI_RICERCA_ANTEPRIMA_AMMORTAMENTO,
	
	// REGISTRO A (prime note verso inventario contabile)
	RISULTATI_RICERCA_REGISTRO_A_CESPITE,
	REQUEST_RICERCA_REGISTRO_A_CESPITE,
	@Deprecated
	RISULTATI_RICERCA_MOVIMENTI_EP_REGISTRO_A_CESPITE,
	@Deprecated
	REQUEST_RICERCA_MOVIMENTI_EP_REGISTRO_A_CESPITE, 
	UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE,
	//SIAC-6553
	IMPORTO_CESPITE_SU_PRIMA_NOTA,
	REQUEST_RICERCA_SCRITTURE_REGISTROA_CESPITE, 
	RISULTATI_RICERCA_SCRITTURE_REGISTROA_CESPITE, 
	
	RISULTATI_RICERCA_MOVIMENTI_DETTAGLIO_REGISTRO_A_CESPITE, REQUEST_RICERCA_MOVIMENTI_DETTAGLIO_REGISTRO_A_CESPITE, 
	UID_MOVIMENTO_DETTAGLIO_DA_COLLEGARE_A_CESPITE,
	
	// SIAC-5225
	LISTA_QUADRO_ECONOMICO,
	RISULTATI_RICERCA_QUADRO_ECONOMICO(false),
	REQUEST_RICERCA_QUADRO_ECONOMICO(false),

	
	// SIAC-6881 COMPONENTE CAPITOLO
	LISTA_COMPONENTE_CAPITOLO,
	REQUEST_RICERCA_COMPONENTE_CAPITOLO(false),
	RISULTATI_RICERCA_COMPONENTE_CAPITOLO(false),
	//salvataggio delle componenti del capitolo in sessione
	LISTA_IMPORTI_COMPONENTE_CAPITOLO,
	LISTA_IMPORTI_COMPONENTE_CAPITOLO_DA_RICERCA,
	CAPITOLO_PER_RICERCA_DETTAGLIO_VARIAZIONE,
	;
	private String paramName;
	private boolean isEliminabile;
	private boolean isEliminabileRedirectToCruscotto;

	/**
	 * Costruttore vuoto di default
	 */
	private BilSessionParameter(){
		this.paramName = this.name();
		this.isEliminabile = true;
		this.isEliminabileRedirectToCruscotto = true;
	}
	
	/**
	 * Costruttore definente l'eliminabilit&agrave;.
	 * 
	 * @param isEliminabile l'eliminabilit&agrave; del parametro
	 */
	private BilSessionParameter(boolean isEliminabile) {
		this.paramName = this.name();
		this.isEliminabile = isEliminabile;
		this.isEliminabileRedirectToCruscotto = true;
	}
	
	/**
	 * Costruttore utilizzante il nome del parametro e la condizione di eliminabilit&agrave;.
	 * 
	 * @param paramName		il nome del parametro. Nel caso sia impostato a <code>null</code>, viene considerato come
	 * 						nome del parametro quanto ottenuto dal metodo {@link Enum#name()}
	 * @param isEliminabile l'eliminabilit&agrave; del parametro
	 */
	private BilSessionParameter(String paramName, boolean isEliminabile){
		this(paramName, isEliminabile, true);
	}
	
	
	private BilSessionParameter(String paramName, boolean isEliminabile, boolean isEliminabileRedirectToCruscotto){
		this.paramName = paramName;
		this.isEliminabile = isEliminabile;
		this.isEliminabileRedirectToCruscotto = isEliminabileRedirectToCruscotto;
	}

	/**
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}

	@Override
	public boolean isEliminabile() {
		return isEliminabile;
	}

	/**
	 * @return the isEliminabileRedirectToCruscotto
	 */
	public Boolean isEliminabileRedirectToCruscotto() {
		return Boolean.valueOf(isEliminabileRedirectToCruscotto);
	}

	/**
	 * Ottengo un SessionParam a partire dal paramName.
	 * 
	 * @param paramName il nome del parametro
	 * 
	 * @return il SessionParam corrispondente, se censito; <code>null</code> altrimenti
	 */
	public static BilSessionParameter byParamName(String paramName) {
		for(BilSessionParameter sp : BilSessionParameter.values()){
			if(sp.getParamName().equals(paramName)){
				return sp;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return getParamName();
	}
	
	

}