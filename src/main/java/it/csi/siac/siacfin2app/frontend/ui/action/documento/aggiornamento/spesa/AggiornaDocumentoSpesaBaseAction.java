/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2app.frontend.ui.action.documento.GenericAggiornaDocumentoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.AggiornaDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSospensione;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.siopeplus.SiopeScadenzaMotivo;

/**
 * Classe di action base per l'aggiornamento del Documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaBaseAction extends GenericAggiornaDocumentoAction<AggiornaDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5667209763529168310L;

	/** Nome della famiglia */
	public static final String FAMILY_NAME = "AggiornaDocumentoSpesa";

	/** Serviz&icirc; del dovumento di spesa */
	@Autowired protected transient DocumentoSpesaService documentoSpesaService;
	/** Serviz&icirc; del documento iva di spesa */
	@Autowired protected transient DocumentoIvaSpesaService documentoIvaSpesaService;
	/** Serviz&icirc; del provvisorio */
	@Autowired protected transient ProvvisorioService provvisorioService;
	/** Serviz&icirc; delle codifiche */
	@Autowired protected transient CodificheService codificheService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
		model.setMessaggiSenzaRichiestaConferma(new ArrayList<Messaggio>());
	}
	
	/**
	 * Inizializza la action.
	 * 
	 * @throws FrontEndBusinessException nel caso in cui l'inizializzazione non vada a buon fine
	 */
	protected void initAction() throws FrontEndBusinessException {
		try {
			super.prepare();
		} catch (Exception e) {
			log.error("initAction", "Errore nell'inizializzazione della action: " + e.getMessage());
			//impossibile inizializzare la action, lancio un'eccezione
			throw new FrontEndBusinessException("Errore nell'inizializzazione della action", e);
		}
	}
	
	/**
	 * Controlla se la lista dei Tipo Impresa sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaTipoImpresa() {
		List<TipoImpresa> listaTipoImpresa = model.getListaTipoImpresa();
		//controllo se sia o meno necessario caricare la lista
		if(!listaTipoImpresa.isEmpty()) {
			// Ho già la lista nel model, non devo ricchiamare il servizio
			return;
		}
		
		RicercaTipoImpresa request = model.creaRequestRicercaTipoImpresa();
		RicercaTipoImpresaResponse response = documentoService.ricercaTipoImpresa(request);
		if(!response.hasErrori()) {
			listaTipoImpresa = response.getElencoTipiImpresa();
			model.setListaTipoImpresa(listaTipoImpresa);
		}
	}
	
	/**
	 * Controlla se la lista delle Commissioni Documento sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, carica le commissioni dall'Enum.
	 */
	protected void checkAndObtainListaCommissioniDocumento() {
		List<CommissioniDocumento> listaCommissioniDocumento = model.getListaCommissioniDocumento();
		if(!listaCommissioniDocumento.isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		listaCommissioniDocumento = Arrays.asList(CommissioniDocumento.values());
		model.setListaCommissioniDocumento(listaCommissioniDocumento);
	}
	
	/**
	 * Controlla se la lista delle Natura Onere sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaNaturaOnere() {
		List<NaturaOnere> listaNaturaOnere = sessionHandler.getParametro(BilSessionParameter.LISTA_NATURA_ONERE);
		if(listaNaturaOnere == null) {
			RicercaNaturaOnere request = model.creaRequestRicercaNaturaOnere();
			RicercaNaturaOnereResponse response = documentoService.ricercaNaturaOnere(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				return;
			}
			//metto in sessione la lista trovata
			listaNaturaOnere = response.getElencoNatureOnere();
			sessionHandler.setParametro(BilSessionParameter.LISTA_NATURA_ONERE, listaNaturaOnere);
		}
		//metto nel model la lista trovata
		model.setListaNaturaOnere(listaNaturaOnere);
	}
	
	/**
	 * Verifica la coerenza degli importi con la gestione split reverse.
	 * <br/>
	 * Il sistema controlla il totale degli importi di uno stesso tipo Iva split-reverse-esente sulle quote: la somma degli importi split-reverse-esente su tutte le quote non dovr&agrave; superare
	 * il totale espresso nei relativi oneri a livello di documento nei dati delle ritenute (il confronto va fatto in base allo stesso TipoIvaSplitReverse degli oneri del documento e quello delle
	 * singole quote: split istituzionale, split commerciale, reverse, importo esente), se non &egrave; cos&igrave; informa l'utente con il messaggio
	 * <code>&lt;FIN_ERR_0287, Totali Iva split-reverse-esente non congruenti&gr;</code>.
	 * 
	 * @param isFromRitenute  se il controllo proviene dalle ritenute
	 * @param isAggiornamento se la validazione sia in aggiornamento
	 */
	protected void checkCoerenzaImportiSplitReverse(boolean isFromRitenute, boolean isAggiornamento) {
		Map<TipoIvaSplitReverse, BigDecimal> map = new EnumMap<TipoIvaSplitReverse, BigDecimal>(TipoIvaSplitReverse.class);
		
		// Per ogni tipo iva split reverse, il valore in mappa sara' importoOnere - importoSubdocumento. Si dovra' controllare che tale valore sia >= 0
		
		// Ciclo sui dettagli onere
		for (DettaglioOnere dettaglioOnere : model.getListaDettaglioOnere()) {
			if (hasTipoIvaSplitReverse (dettaglioOnere)
					&& dettaglioOnere.getImportoCaricoSoggetto() != null) {
				//il dettaglio s' di ripo spilt reverse ed ha un importo a carico del soggetto > 0, popolo la mappa
				addToMap(map, dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse(), dettaglioOnere.getImportoCaricoSoggetto());
			}
		}
		
		if (isFromRitenute) {
			// Se sono in aggiornamento dovrei poter controllare i dati del subdocumento
			if(isAggiornamento) {
				// Cerco il subdocumento attuale nella lista del model, e modifico l'importo
				DettaglioOnere dOnere = ComparatorUtils.searchByUidEventuallyNull(model.getListaDettaglioOnere(), model.getDettaglioOnere());
				if(dOnere != null 
						&& hasTipoIvaSplitReverse(dOnere) 
						&& dOnere.getImportoCaricoSoggetto() != null) {
					addToMap(map, dOnere.getTipoOnere().getTipoIvaSplitReverse(), dOnere.getImportoCaricoSoggetto().negate());
				}
			}
		
			if (model.getDettaglioOnere().getTipoOnere().getTipoIvaSplitReverse() != null) {
        			// Aggiungo la ritenuta che sto inserendo
        			addToMap(map, model.getDettaglioOnere().getTipoOnere().getTipoIvaSplitReverse(), model.getDettaglioOnere().getImportoCaricoSoggetto());
			}
		}
		// Ciclo sui subdocumenti - quote
		for(SubdocumentoSpesa subdocumentoSpesa : model.getListaSubdocumentoSpesa()) {
			if(subdocumentoSpesa.getTipoIvaSplitReverse() != null && subdocumentoSpesa.getImportoSplitReverse() != null) {
				addToMap(map, subdocumentoSpesa.getTipoIvaSplitReverse(), subdocumentoSpesa.getImportoSplitReverse().negate());
			}
		}
		
		if (!isFromRitenute) {
			// Se sono in aggiornamento dovrei poter controllare i dati del subdocumento
			if(isAggiornamento) {
				// Cerco il subdocumento attuale nella lista del model, e modifico l'importo
				SubdocumentoSpesa ss = ComparatorUtils.searchByUidEventuallyNull(model.getListaSubdocumentoSpesa(), model.getSubdocumento());
				if(ss != null && ss.getTipoIvaSplitReverse() != null && ss.getImportoSplitReverse() != null) {
					addToMap(map, ss.getTipoIvaSplitReverse(), ss.getImportoSplitReverse());
				}
			}
		
			// Aggiungo il subdocumento che sto inserendo
			addToMap(map, model.getSubdocumento().getTipoIvaSplitReverse(), model.getSubdocumento().getImportoSplitReverse().negate());
		}
		// Ciclo sulla mappa. Se la condizione di cui sopra non e' verificata invio un errore
		for (Map.Entry<TipoIvaSplitReverse, BigDecimal> entry : map.entrySet()) {
			checkCondition(entry.getValue() == null || entry.getValue().signum() >= 0, ErroreFin.TOTALI_SPLIT_REVERSE_ESENTE_INCONGRUENTI.getErrore());
		}
	}

	/**
	 * Aggiunge un dato valore alla mappa.
	 * 
	 * @param map                 la mappa da popolare
	 * @param tipoIvaSplitReverse la chiave per la mappa
	 * @param importo             l'importo da impostare nella mappa
	 */
	private void addToMap(Map<TipoIvaSplitReverse, BigDecimal> map, TipoIvaSplitReverse tipoIvaSplitReverse, BigDecimal importo) {
		BigDecimal partial = map.get(tipoIvaSplitReverse);
		if(partial == null) {
			//inizializzo il campo
			partial = BigDecimal.ZERO;
		}
		partial = partial.add(importo);
		map.put(tipoIvaSplitReverse, partial);
	}
	
	/**
	 * Verifica  getTipoIvaSplitReverse x il dettalgioOnere
	 * 
	 * @param dettaglioOnere il dettaglio da verificare
	 * @return true se valorizzato tipoIvaSplitReverse
	 */
	private boolean hasTipoIvaSplitReverse (DettaglioOnere dettaglioOnere){
		return dettaglioOnere != null && dettaglioOnere.getTipoOnere() != null && dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse() != null;
	}
	
	@Override
	public void cleanModel() {
		super.cleanModel();
		
		//pulisco i campi del model
		model.setSubdocumento(null);
		model.setSedeSecondariaSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setMovimentoGestione(null);
		model.setDettaglioOnere(null);
	}
	
	/**
	 * Verifico che le attivazioni contabili siano possibili
	 * 
	 */
	protected void checkAttivazioneRegContabili() {
		final String methodName = "checkAttivazioneRegContabili";
		if(model.getDocumento() == null || model.getDocumento().getTipoDocumento() == null) {
			log.debug(methodName, "Dati non presenti. Non attivo alcunche'");
			//non ho i dati,non posso attivare
			model.setAttivaRegistrazioniContabiliVisible(false);
			return;
		}
		
		StatoOperativoDocumento sto = model.getDocumento().getStatoOperativoDocumento();
		TipoDocumento td = model.getDocumento().getTipoDocumento();
		//controllo se siano verificate le condizioni per l'attivazione
		boolean condizioneVisibilitaRegistrazioniContabili = !StatoOperativoDocumento.ANNULLATO.equals(sto)
				&& !StatoOperativoDocumento.EMESSO.equals(sto)
				&& !StatoOperativoDocumento.INCOMPLETO.equals(sto)
				&& !Boolean.TRUE.equals(model.getDocumento().getContabilizzaGenPcc())
				&& (Boolean.TRUE.equals(td.getFlagAttivaGEN()) || Boolean.TRUE.equals(td.getFlagComunicaPCC()));
		model.setAttivaRegistrazioniContabiliVisible(condizioneVisibilitaRegistrazioniContabili);
	}
	
	/**
	 * Controlla se la lista delle Causali Sospensione sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListeCausaleSospensioneAndSIOPE() {
		if(!model.getListaCausaleSospensione().isEmpty()
				&& !model.getListaSiopeDocumentoTipo().isEmpty()
				&& !model.getListaSiopeDocumentoTipoAnalogico().isEmpty()
				&& !model.getListaSiopeScadenzaMotivo().isEmpty()
				&& !model.getListaSiopeAssenzaMotivazione().isEmpty()) {
			// Ho gia' la lista nel model
			return;
		}
		
		@SuppressWarnings("unchecked")
		RicercaCodifiche req = model.creaRequestRicercaCodifiche(CausaleSospensione.class, SiopeDocumentoTipo.class,
				SiopeDocumentoTipoAnalogico.class, SiopeScadenzaMotivo.class, SiopeAssenzaMotivazione.class);
		RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
		
		if(!res.hasErrori()) {
			//imposto nel model le liste ottenute
			model.setListaCausaleSospensione(res.getCodifiche(CausaleSospensione.class));
			model.setListaSiopeDocumentoTipo(res.getCodifiche(SiopeDocumentoTipo.class));
			model.setListaSiopeDocumentoTipoAnalogico(res.getCodifiche(SiopeDocumentoTipoAnalogico.class));
			model.setListaSiopeScadenzaMotivo(res.getCodifiche(SiopeScadenzaMotivo.class));
			model.setListaSiopeAssenzaMotivazione(res.getCodifiche(SiopeAssenzaMotivazione.class));
		}
	}
	
	/**
	 * Non permettere agli operatori che hanno la seguente Azione: <code>OP-SPE-NoDatiSospensioneDec</code>
	 * di visualizzare e quindi modificare i dati di sospensione presenti a livello di documento, nascondere i campi,
	 * quando almeno una delle quote documento ha una liquidazione collegata in stato definitivo.
	 * @return se i dati sono editabili
	 */
	protected boolean isDatiSospensioneEditabili() {
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		return
			// Condizione precedente: devo avere la gestione acquisti
			AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_GESTIONE_ACQUISTI, azioniConsentite)
			&& (
				// Controllo sull'azione decentrata
				!AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_NO_DATI_SOSPENSIONE_DECENTRATO, azioniConsentite)
				// Controllo sulle liquidazioni
				|| nessunaQuotaConLiquidazioneDefinitiva()
			);
	}
	
	/**
	 * Controlla se nessuna delle quote del documento presenti una liquidazione in stato DEFINITIVO
	 * @return se non vi siano quote con liquidazioni definitive
	 */
	private boolean nessunaQuotaConLiquidazioneDefinitiva() {
		for(SubdocumentoSpesa ss : model.getListaSubdocumentoSpesa()) {
			if(ss.getLiquidazione() != null && StatoOperativoLiquidazione.VALIDO.equals(ss.getLiquidazione().getStatoOperativoLiquidazione())) {
				//questo documento ha una liquidazione: return false
				return false;
			}
		}
		
		return true;
	}
}
