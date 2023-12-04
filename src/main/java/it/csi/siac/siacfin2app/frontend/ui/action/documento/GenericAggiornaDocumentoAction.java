/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.AggiornaDocumentoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;
import it.csi.siac.siacfin2ser.model.TipoAvviso;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;

/**
 * Classe di action per l'aggiornamento del Documento (metodi comuni per entrata e spesa).
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/03/2014
 * @param <M> la tipizzazione del Model
 */
public class GenericAggiornaDocumentoAction<M extends AggiornaDocumentoModel> extends GenericDocumentoAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;

	@Autowired private transient ContoTesoreriaService contoTesoreriaService;

	/**
	 * Controlla se la lista delle Natura Onere sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaTipoAvviso() {
		List<TipoAvviso> listaTipoAvviso = model.getListaTipoAvviso();
		if(!listaTipoAvviso.isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaTipoAvviso request = model.creaRequestRicercaTipoAvviso();
		RicercaTipoAvvisoResponse response = documentoService.ricercaTipoAvviso(request);
		if(!response.hasErrori()) {
			listaTipoAvviso = response.getElencoTipiAvviso();
			model.setListaTipoAvviso(listaTipoAvviso);
		}
	}
	
	/**
	 * Carica la lista delle note al tesoriere
	 */
	protected void checkAndObtainListaNoteTesoriere() {
		List<NoteTesoriere> listaNoteTesoriere = model.getListaNoteTesoriere();
		if(!listaNoteTesoriere.isEmpty()) {
			return;
		}
		
		RicercaNoteTesoriere request = model.creaRequestRicercaNoteTesoriere();
		RicercaNoteTesoriereResponse response = documentoService.ricercaNoteTesoriere(request);
		if(!response.hasErrori()) {
			listaNoteTesoriere = response.getElencoNoteTesoriere();
			model.setListaNoteTesoriere(listaNoteTesoriere);
		}
	}
	
	/**
	 * Carica la lista del conto tesoreria
	 */
	protected void checkAndObtainListaContoTesoreria() {
		List<ContoTesoreria> listaContoTesoreria = model.getListaContoTesoreria();
		if(!listaContoTesoreria.isEmpty()) {
			return;
		}
		
		LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
		LeggiContiTesoreriaResponse response = contoTesoreriaService.leggiContiTesoreria(request);
		if(!response.hasErrori()) {
			listaContoTesoreria = response.getContiTesoreria();
			model.setListaContoTesoreria(listaContoTesoreria);
		}
	}
	
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
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
	 * Effettua una validazioen dei campi necessar&icirc; per l'eliminazione della quota.
	 */
	protected void validaEliminaQuota() {
		checkNotNull(model.getRigaDaEliminare(), "Subdocumento da eliminare");
	}
	
	
	/**
	 * Validazione per il Provvedimento.
	 * 
	 * @param azioneDecentrata il nome dell'azione decentrata
	 */
	protected void validaProvvedimentoPerInserimentoNuovaQuota(String azioneDecentrata) {
		final String methodName = "validaProvvedimentoPerInserimentoNuovaQuota";
		
		List<TipoAtto> list = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		TipoAtto ta = ComparatorUtils.searchByUid(list, model.getTipoAtto());
		model.setTipoAtto(ta);
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaProvvedimento.class, response));
			addErrori(response);
			return;
		}
		
		// Il provvedimento deve esistere
		if(response.getListaAttiAmministrativi().isEmpty()) {
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
			return;
		}
		//SIAC 3931
		Map<Boolean, Long> mapSac = countProvvedimentiByPresenzaSac(response.getListaAttiAmministrativi());

		try {
			// Deve esistere un unico provvedimento
			if(!Boolean.TRUE.equals(model.getProseguireConElaborazioneAttoAmministrativo())){
				StringBuilder identificativo = new StringBuilder()
						.append("identificativo: ")
						.append(model.getAttoAmministrativo().getAnno());
				if(model.getAttoAmministrativo().getNumero() != 0) {
					identificativo.append("/")
						.append(model.getAttoAmministrativo().getNumero());
				}
				if(model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0) {
					identificativo.append("/")
						.append(model.getTipoAtto().getCodice());
				}
				
				// Controllo le SAC: deve esserci al piu' un provvedimento senza SAC
				warnCondition(mapSac.get(Boolean.FALSE).longValue() != 1L || mapSac.get(Boolean.TRUE).longValue() == 0L,
						ErroreBil.PROVVEDIMENTI_UGUALI_CON_SAC_DIVERSE.getErrore(identificativo.toString()),
						true);
				checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getStrutturaAmministrativoContabile(), true);
			} else {
				checkCondition(model.getStrutturaAmministrativoContabile() == null || model.getStrutturaAmministrativoContabile().getUid() == 0 || mapSac.get(Boolean.FALSE).longValue() <= 1L,
						ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Provvedimento"), true);
				filtraListaAtti(response.getListaAttiAmministrativi());
			}
		} catch (ParamValidationException pve) {
			log.debug(methodName, pve.getMessage());
			return;
		}

		model.setProseguireConElaborazioneAttoAmministrativo(Boolean.FALSE);
		
		AttoAmministrativo attoAmministrativo = response.getListaAttiAmministrativi().get(0);
		
		//effettuo controlli solo quando non e' gia' disabilitato
		if(!Boolean.TRUE.equals(model.getProvvedimentoQuotaDisabilitato())){
			// Controllo stato
			if(!BilConstants.STATO_OPERATIVO_ATTO_AMMINISTRATIVO_DEFINITIVO.getConstant().equals(attoAmministrativo.getStatoOperativo())){
				addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione quota documento di spesa", "definitivo"));
				return;
			}
			
			if (attoAmministrativo.getAllegatoAtto() != null && attoAmministrativo.getAllegatoAtto().getUid() !=0){
				addErrore(ErroreFin.ATTO_GIA_ABBINATO.getErrore());
				return;
			}
			// TODO:
			/*
			 *  2-
				inoltre il provvedimento non deve essere associato ad un 'Allegato Atto'
				con stato in convalida uguale a CONVALIDATO, ANNULLATO o RIFIUTATO, altrimenti si visualizza il messaggio <FIN_ERR_0075, 
			 	Stato Provvedimento non consentito, 'Gestione quota documento di spesa', 'NON CONVALIDATO-NON RIFIUTATO-NON ANNULLATO'>.
			 */
		}
		
		model.setAttoAmministrativo(attoAmministrativo);
		log.debug(methodName, attoAmministrativo.getUid());
		
		controlloDecentrato(attoAmministrativo.getStrutturaAmmContabile(), azioneDecentrata);
	}
	
	/**
	 * Filtro la lista solo se contiene atti con anno numero tipo uguali ma con sac diverse
	 * @param listaAttiAmministrativi
	 */
	private void filtraListaAtti(List<AttoAmministrativo> listaAttiAmministrativi) {
		if (listaAttiAmministrativi.size() > 1 && (model.getStrutturaAmministrativoContabile() == null || model.getStrutturaAmministrativoContabile().getUid() == 0)) {
			for (Iterator<AttoAmministrativo> it = listaAttiAmministrativi.iterator(); it.hasNext();) {
				AttoAmministrativo attoAmministrativo = it.next();
				if (attoAmministrativo.getStrutturaAmmContabile() != null && attoAmministrativo.getStrutturaAmmContabile().getUid() != 0) {
					it.remove();
				}
			}
		}
	}

	/**
	 * verifico se ho una lista di atti amministrativi uguali ma con sac diverse 
	 * @param listaAttiAmministrativi la lista degli atti da controllare
	 * @return <code>true</code> se tutti gli atti sono uguali, ma con SAC diverse; <code>false</code> altrimenti
	 */
	private boolean isAttiUgualiConSacDiverse(List<AttoAmministrativo> listaAttiAmministrativi) {
		StrutturaAmministrativoContabile sac = model.getStrutturaAmministrativoContabile();
		if(sac != null && sac.getUid() != 0) {
			// Ho una SAC, evito il controllo
			return false;
		}
		boolean hasOneWithoutSAC = false;
		boolean hasOneWithSAC = false;
		for (AttoAmministrativo aa : listaAttiAmministrativi) {
			// Anno e numero so essere uguali. Valuto solo la SAC
			hasOneWithoutSAC = hasOneWithoutSAC || aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0;
			hasOneWithSAC = hasOneWithSAC || (aa.getStrutturaAmmContabile() != null && aa.getStrutturaAmmContabile().getUid() != 0);
		}
		// Faccio scegliere se e solo se ce ne sono almeno due di cui uno con sac e uno senza sac
		return hasOneWithoutSAC && hasOneWithSAC;
	}
	
	/**
	 * Conta i provvedimenti con e senza SAC
	 * @param listaAttiAmministrativi la lista degli atti
	 * @return una mappa che associa alla presenza dei SAC il numero degli atti corrispondenti
	 */
	private Map<Boolean, Long> countProvvedimentiByPresenzaSac(Iterable<AttoAmministrativo> listaAttiAmministrativi) {
		long with = 0L;
		long without = 0L;
		for (AttoAmministrativo aa : listaAttiAmministrativi) {
			if(aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0) {
				without++;
			} else {
				with++;
			}
		}
		
		Map<Boolean, Long> res = new HashMap<Boolean, Long>();
		res.put(Boolean.TRUE, Long.valueOf(with));
		res.put(Boolean.FALSE, Long.valueOf(without));
		return res;
	}

	/**
	 * Valida gli importi.
	 * <br>
	 * Nel caso in cui la validazione non vada a buon fine, ripristina come valore di netto ed arrotondamento i vecchi valori
	 * @param <D> la tipizzazione del documento
	 * 
	 * @param documento il documento da cui ottenere i dati
	 * @param totaleImponibileOneriRC il totale imponibile, oneri e reverse/change
	 */
	protected <D extends Documento<?, ?>> void validazioneImporti(D documento, BigDecimal totaleImponibileOneriRC) {
		try {
			final String methodName = "validazioneImporti";
			log.debug(methodName, "importo: " + documento.getImporto());
			log.debug(methodName, "arrotondamento: " + documento.getArrotondamento());
			log.debug(methodName, "netto: " + model.getNetto());
			log.debug(methodName, "totale da pagare quote: " + model.getTotaleDaPagareQuote());
			
//			// L'arrotondamento deve essere non-positivo NON PIU', CR 2889
//			checkCondition(documento.getArrotondamento().signum() <= 0,
//					ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": deve essere minore o uguale a zero"), true);
			// Importo + arrotondamento > 0
			checkCondition(documento.getImporto().add(documento.getArrotondamento()).signum() >= 0,
					ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere maggiore o uguale a zero"), true);
			checkCondition(documento.getImporto().add(documento.getArrotondamento()).add(totaleImponibileOneriRC).subtract(model.getTotaleQuote()).signum() >= 0,
//					ErroreCore.FORMATO_NON_VALIDO.getErrore("importo o arrotondamento",
//							": importo sommato ad arrotondamento deve essere maggiore o uguale al totale delle quote")
					ErroreFin.TOTALE_QUOTE_E_IMPORTI_DOCUMENTO_NON_COERENTI.getErrore("controllare gli importi del documento e gli importi delle quote.")
							);
			BigDecimal nuovoNetto = documento.getImporto().add(documento.getArrotondamento()).add(totaleImponibileOneriRC).subtract(model.getTotaleImportoDaDedurreSuFattura());
			checkCondition(nuovoNetto.signum() >= 0,
					ErroreCore.FORMATO_NON_VALIDO.getErrore("netto",
							": l'importo netto deve essere maggiore o uguale a zero"));
		} catch(ParamValidationException e) {
			log.debug("validazioneImporti", "Arrotondamento errato: " + e.getMessage() + " - Impostazione vecchio netto");
			documento.setArrotondamento(model.getOldArrotondamento());
			model.impostaNettoFromOld();
		} finally{
			// Ricalcolo gli importi
			model.calcoloImporti();
		}
	}
	
	/**
	 * Valida gli importi.
	 * <br>
	 * Nel caso in cui la validazione non vada a buon fine, ripristina come valore di netto ed arrotondamento i vecchi valori
	 * @param <D> la tipizzazione del documento
	 * 
	 * @param documento il documento da cui ottenere i dati
	 * @param totaleImponibileOneriRC il totale imponibile, oneri e reverse/change
	 */
	protected <D extends Documento<?, ?>> void validazioneImportiTestata(D documento, BigDecimal totaleImponibileOneriRC) {
		try {
			final String methodName = "validazioneImporti";
			log.debug(methodName, "importo: " + documento.getImporto());
			log.debug(methodName, "arrotondamento: " + documento.getArrotondamento());
			log.debug(methodName, "netto: " + model.getNetto());
			log.debug(methodName, "totale da pagare quote: " + model.getTotaleDaPagareQuote());
			
//			// L'arrotondamento deve essere non-positivo NON PIU', CR 2889
//			checkCondition(documento.getArrotondamento().signum() <= 0,
//					ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": deve essere minore o uguale a zero"), true);
			// Importo + arrotondamento > 0
			// L'importo deve essere non negativo
			checkCondition(documento.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"), true);
			checkCondition(documento.getImporto().add(documento.getArrotondamento()).signum() > 0,
					ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere maggiore di zero"), true);
			checkCondition(documento.getImporto().add(documento.getArrotondamento()).add(totaleImponibileOneriRC).subtract(model.getTotaleQuote()).signum() >= 0,
//					ErroreCore.FORMATO_NON_VALIDO.getErrore("importo o arrotondamento",
//							": importo sommato ad arrotondamento deve essere maggiore o uguale al totale delle quote")
					ErroreFin.TOTALE_QUOTE_E_IMPORTI_DOCUMENTO_NON_COERENTI.getErrore("controllare gli importi del documento e gli importi delle quote.")
							);
			BigDecimal nuovoNetto = documento.getImporto().add(documento.getArrotondamento()).add(totaleImponibileOneriRC).subtract(model.getTotaleNoteCredito());
			checkCondition(nuovoNetto.signum() > 0,
					ErroreCore.FORMATO_NON_VALIDO.getErrore("netto",
							": l'importo netto deve essere maggiore di zero"));
		} catch(ParamValidationException e) {
			log.debug("validazioneImporti", "Arrotondamento errato: " + e.getMessage() + " - Impostazione vecchio netto");
			documento.setArrotondamento(model.getOldArrotondamento());
			model.impostaNettoFromOld();
		} finally{
			// Ricalcolo gli importi
			model.calcoloImporti();
		}
	}
	
	@Override
	protected void cleanModel() {
		super.cleanModel();
		
		model.setListaCommissioniDocumento(new ArrayList<CommissioniDocumento>());
		model.setListaCausale770(new ArrayList<Causale770>());
		model.setListaTipoDocumento(new ArrayList<TipoDocumento>());
	}
	
	/**
	 * Pulisce le quote del documento.
	 */
	protected void cleanQuote() {
		model.setAttoAmministrativo(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setTipoAtto(null);
		model.setCapitoloRilevanteIvaVisibile(Boolean.FALSE);
		model.setTipoAvviso(null);
		model.setNoteTesoriere(null);
		model.setProseguireConElaborazioneAttoAmministrativo(Boolean.FALSE);
		
	}
	
	/**
	 * Pulisce le quote del documento per l'aggiornamento.
	 */
	protected void cleanQuotePerAggiornamento() {
		cleanQuote();
	}
	
	/**
	 * effettua un controllo sul provvedimento ed imposta il flag
	 */
	protected void controllaProvvedimento() {
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		if(response.hasErrori() || response.getListaAttiAmministrativi()== null) {
			return;
		}

		model.setProseguireConElaborazioneAttoAmministrativo(isAttiUgualiConSacDiverse(response.getListaAttiAmministrativi()) ? Boolean.TRUE : Boolean.FALSE);

	}
	
}
