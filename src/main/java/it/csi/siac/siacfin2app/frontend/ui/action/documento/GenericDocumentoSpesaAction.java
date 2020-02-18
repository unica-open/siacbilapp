/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.GenericDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action generica per il documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/03/2014
 * @param <M> la tipizzazione del Model
 *
 */
public class GenericDocumentoSpesaAction<M extends GenericDocumentoSpesaModel> extends GenericDocumentoAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1896852550719827192L;
	
	/** Serviz&icirc; del documento di spesa */
	@Autowired protected transient DocumentoSpesaService documentoSpesaService;
	/** IL servizio delle codifiche */
	@Autowired protected transient CodificheService codificheService;
	
	/**
	 * Controlla se la lista dei Tipo Impresa sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaTipoImpresa() {
		if(!model.getListaTipoImpresa().isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaTipoImpresa req = model.creaRequestRicercaTipoImpresa();
		RicercaTipoImpresaResponse res = documentoService.ricercaTipoImpresa(req);
		if(!res.hasErrori()) {
			model.setListaTipoImpresa(res.getElencoTipiImpresa());
		}
	}
	
	/**
	 * Controlla se la lista delle Natura Onere sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaNaturaOnere() {
		if(!model.getListaNaturaOnere().isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaNaturaOnere req = model.creaRequestRicercaNaturaOnere();
		RicercaNaturaOnereResponse res = documentoService.ricercaNaturaOnere(req);
		if(!res.hasErrori()) {
			model.setListaNaturaOnere(res.getElencoNatureOnere());
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
		listaCommissioniDocumento = new ArrayList<CommissioniDocumento>(Arrays.asList(CommissioniDocumento.values()));
		model.setListaCommissioniDocumento(listaCommissioniDocumento);
	}
	
	/**
	 * Controlla se la lista delle Natura Onere sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaTipoAvviso() {
		if(!model.getListaTipoAvviso().isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaTipoAvviso req = model.creaRequestRicercaTipoAvviso();
		RicercaTipoAvvisoResponse res = documentoService.ricercaTipoAvviso(req);
		if(!res.hasErrori()) {
			model.setListaTipoAvviso(res.getElencoTipiAvviso());
		}
	}
	
	/**
	 * Carica la lista dei tipi di atto.
	 */
	protected void checkAndObtainListaNoteTesoriere() {
		if(!model.getListaNoteTesoriere().isEmpty()) {
			return;
		}
		
		RicercaNoteTesoriere req = model.creaRequestRicercaNoteTesoriere();
		RicercaNoteTesoriereResponse res = documentoService.ricercaNoteTesoriere(req);
		if(!res.hasErrori()) {
			model.setListaNoteTesoriere(res.getElencoNoteTesoriere());
		}
	}
	
	/**
	 * Controlla se la lista dei Codice Ufficio Destinatario PCC sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaCodiceUfficioDestinatarioPCC() {
		if(!model.getListaCodiceUfficioDestinatarioPCC().isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaCodiceUfficioDestinatarioPCC req = model.creaRequestRicercaCodiceUfficioDestinatarioPCC(ottieniListaStruttureAmministrativoContabiliDaSessione());
		RicercaCodiceUfficioDestinatarioPCCResponse res = documentoService.ricercaCodiceUfficioDestinatarioPCC(req);
		if(!res.hasErrori()) {
			model.setListaCodiceUfficioDestinatarioPCC(res.getCodiciUfficiDestinatariPcc());
		}
	}
	
	/**
	 * Controlla se la lista dei Codice PCC sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaCodicePCC() {
		if(!model.getListaCodicePCC().isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		RicercaCodicePCC req = model.creaRequestRicercaCodicePCC(ottieniListaStruttureAmministrativoContabiliDaSessione());
		RicercaCodicePCCResponse res = documentoService.ricercaCodicePCC(req);
		if(!res.hasErrori()) {
			model.setListaCodicePCC(res.getCodiciPCC());
		}
	}
	
	/**
	 * Controlla se la lista dei Codice PCC sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 *
	 * @return the string
	 */
	public String obtainListaCodicePCCByCodiceUfficioDestinatario() {
		final String methodName = "obtainListaCodicePCCByCodiceUfficioDestinatario";
		//DA ANALISI: Se sono presenti in archivio relazioni con il CodiceUfficioDestinatario filtrare i valori del CodicePCC in base a queste relazioni e quindi al codice ufficio impostato, altrimenti visualizzarli come è attualmente.
		model.setListaCodicePCCFiltered(ReflectionUtil.deepClone(model.getListaCodicePCC()));
		//controllo che l'utente sia abilitato al filtro del codice PCC tramite 
//		if(!AzioniConsentiteFactory.isConsentito(AzioniConsentite.DOCUMENTO_SPESA_LIMITA_DATI_FEL, sessionHandler.getAzioniConsentite())) {
//			log.warn(methodName, "Utente non abilitato al filtro del codice PCC tramite codice ufficio.");
//			return SUCCESS;
//		}
		RicercaCodicePCC req = model.creaRequestRicercaCodicePCC(ottieniListaStruttureAmministrativoContabiliDaSessione());
		RicercaCodicePCCResponse res = documentoService.ricercaCodicePCC(req);
		if(res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Non ho ottenuto codici PCC filtrati");
			return SUCCESS;
		}
		if(res.getCodiciPCC().isEmpty()) {
			log.debug(methodName, "non ho trovato dei codici PCC legati all'ufficio destinatario PCC con uid: " + model.getUidCodiceUfficioDestinatarioPccToFilter());
			return SUCCESS;
		}
		model.setListaCodicePCCFiltered(res.getCodiciPCC());
		return SUCCESS;
	}
	
	@Override
	protected void cleanModel() {
		super.cleanModel();
		
		model.setDocumento(null);
		model.setListaTipoImpresa(new ArrayList<TipoImpresa>());
		model.setListaSedeSecondariaSoggetto(new ArrayList<SedeSecondariaSoggetto>());
		model.setListaModalitaPagamentoSoggetto(new ArrayList<ModalitaPagamentoSoggetto>());
		model.setListaCommissioniDocumento(new ArrayList<CommissioniDocumento>());
		model.setListaNaturaOnere(new ArrayList<NaturaOnere>());
		model.setListaTipoOnere(new ArrayList<TipoOnere>());
		model.setListaAttivitaOnere(new ArrayList<AttivitaOnere>());
		model.setListaCausale770(new ArrayList<Causale770>());
		//SIAC-5346
		model.setUidCodiceUfficioDestinatarioPccToFilter(null);
	}
	
	/**
	 * setta i parametri di default come e' stato richiesta dalla CR-2831
	 * Si richiede nello step 2 dell'inserisci documento di pre-impostare 
	 * il campo Termine di pagamento con 60 giorni e di conseguenza calcolare la data scadenza 
	 */
	public void popolaParametriDiDefaultStep2() {
		model.getDocumento().setTerminePagamento(BilConstants.TERMINE_PAGAMENTO.getId());
		model.calcolaDataScadenzaDefault();
		//SIAC-5346
		model.setDisabilitaPCCSeUnivoco(AzioniConsentiteFactory.isConsentito(AzioniConsentite.DOCUMENTO_SPESA_LIMITA_DATI_FEL, sessionHandler.getAzioniConsentite()));
	}
	
}
