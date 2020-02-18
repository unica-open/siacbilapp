/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.registrocomunicazionipcc;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.registrocomunicazionipcc.ElementoRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumento;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * Classe di modello per la consultazione dei registri delle comunicazioni PCC legate al subdocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/06/2015
 *
 */
public class ConsultaRegistroComunicazioniPCCSubdocumentoSpesaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6240950494725685075L;
	
	private SubdocumentoSpesa subdocumentoSpesa;
	private DocumentoSpesa documentoSpesa;
	private RegistroComunicazioniPCC registroComunicazioniPCC;
	private StatoDebito statoDebito;
	
	// Per mantenere il riferimento alla CONTABILIZZAZIONE
	private TipoOperazionePCC tipoOperazionePCC;
	private Integer riga;
	
	private List<RegistroComunicazioniPCC> listaRegistroComunicazioniPCC = new ArrayList<RegistroComunicazioniPCC>();
	private List<TipoOperazionePCC> listaTipoOperazionePCC = new ArrayList<TipoOperazionePCC>();
	private List<StatoDebito> listaStatoDebito = new ArrayList<StatoDebito>();
	private List<CausalePCC> listaCausalePCC = new ArrayList<CausalePCC>();
	
	/** Costruttore vuoto di default */
	public ConsultaRegistroComunicazioniPCCSubdocumentoSpesaModel() {
		setTitolo("Elenco comunicazioni a PCC");
	}

	/**
	 * @return the subdocumentoSpesa
	 */
	public SubdocumentoSpesa getSubdocumentoSpesa() {
		return subdocumentoSpesa;
	}

	/**
	 * @param subdocumentoSpesa the subdocumentoSpesa to set
	 */
	public void setSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		this.subdocumentoSpesa = subdocumentoSpesa;
	}

	/**
	 * @return the documentoSpesa
	 */
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}

	/**
	 * @param documentoSpesa the documentoSpesa to set
	 */
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}

	/**
	 * @return the registroComunicazioniPCC
	 */
	public RegistroComunicazioniPCC getRegistroComunicazioniPCC() {
		return registroComunicazioniPCC;
	}

	/**
	 * @param registroComunicazioniPCC the registroComunicazioniPCC to set
	 */
	public void setRegistroComunicazioniPCC(RegistroComunicazioniPCC registroComunicazioniPCC) {
		this.registroComunicazioniPCC = registroComunicazioniPCC;
	}

	/**
	 * @return the statoDebito
	 */
	public StatoDebito getStatoDebito() {
		return statoDebito;
	}

	/**
	 * @param statoDebito the statoDebito to set
	 */
	public void setStatoDebito(StatoDebito statoDebito) {
		this.statoDebito = statoDebito;
	}
	
	/**
	 * @return the tipoOperazionePCC
	 */
	public TipoOperazionePCC getTipoOperazionePCC() {
		return tipoOperazionePCC;
	}

	/**
	 * @param tipoOperazionePCC the tipoOperazionePCC to set
	 */
	public void setTipoOperazionePCC(TipoOperazionePCC tipoOperazionePCC) {
		this.tipoOperazionePCC = tipoOperazionePCC;
	}

	/**
	 * @return the riga
	 */
	public Integer getRiga() {
		return riga;
	}

	/**
	 * @param riga the riga to set
	 */
	public void setRiga(Integer riga) {
		this.riga = riga;
	}

	/**
	 * @return the listaRegistroComunicazioniPCC
	 */
	public List<RegistroComunicazioniPCC> getListaRegistroComunicazioniPCC() {
		return listaRegistroComunicazioniPCC;
	}

	/**
	 * @param listaRegistroComunicazioniPCC the listaRegistroComunicazioniPCC to set
	 */
	public void setListaRegistroComunicazioniPCC(List<RegistroComunicazioniPCC> listaRegistroComunicazioniPCC) {
		this.listaRegistroComunicazioniPCC = listaRegistroComunicazioniPCC != null ? listaRegistroComunicazioniPCC : new ArrayList<RegistroComunicazioniPCC>();
	}

	/**
	 * @return the listaTipoOperazionePCC
	 */
	public List<TipoOperazionePCC> getListaTipoOperazionePCC() {
		return listaTipoOperazionePCC;
	}

	/**
	 * @param listaTipoOperazionePCC the listaTipoOperazionePCC to set
	 */
	public void setListaTipoOperazionePCC(List<TipoOperazionePCC> listaTipoOperazionePCC) {
		this.listaTipoOperazionePCC = listaTipoOperazionePCC != null ? listaTipoOperazionePCC : new ArrayList<TipoOperazionePCC>();
	}

	/**
	 * @return the listaStatoDebito
	 */
	public List<StatoDebito> getListaStatoDebito() {
		return listaStatoDebito;
	}

	/**
	 * @param listaStatoDebito the listaStatoDebito to set
	 */
	public void setListaStatoDebito(List<StatoDebito> listaStatoDebito) {
		this.listaStatoDebito = listaStatoDebito != null ? listaStatoDebito : new ArrayList<StatoDebito>();
	}

	/**
	 * @return the listaCausalePCC
	 */
	public List<CausalePCC> getListaCausalePCC() {
		return listaCausalePCC;
	}

	/**
	 * @param listaCausalePCC the listaCausalePCC to set
	 */
	public void setListaCausalePCC(List<CausalePCC> listaCausalePCC) {
		this.listaCausalePCC = listaCausalePCC != null ? listaCausalePCC : new ArrayList<CausalePCC>();
	}
	
	/**
	 * @return the listaContabilizzazioni
	 */
	public List<ElementoRegistroComunicazioniPCC> getListaContabilizzazioni() {
		List<ElementoRegistroComunicazioniPCC> result = new ArrayList<ElementoRegistroComunicazioniPCC>();
		// Prendo le comunicazioni di tipo CONTABILIZZAZIONE
		for(RegistroComunicazioniPCC rcpcc : getListaRegistroComunicazioniPCC()) {
			if(rcpcc != null && rcpcc.getTipoOperazionePCC() != null && BilConstants.TIPO_OPERAZIONE_PCC_CONTABILIZZAZIONE.getConstant().equals(rcpcc.getTipoOperazionePCC().getCodice())) {
				ElementoRegistroComunicazioniPCC wrapper = new ElementoRegistroComunicazioniPCC(rcpcc);
				result.add(wrapper);
			}
		}
		return result;
	}
	
	/**
	 * @return the listaComunicazioneDataScadenza
	 */
	public List<ElementoRegistroComunicazioniPCC> getListaComunicazioneDataScadenza() {
		List<ElementoRegistroComunicazioniPCC> result = new ArrayList<ElementoRegistroComunicazioniPCC>();
		// Prendo le comunicazioni di tipo COMUNICAZIONE DATA SCADENZA
		for(RegistroComunicazioniPCC rcpcc : getListaRegistroComunicazioniPCC()) {
			if(rcpcc != null && rcpcc.getTipoOperazionePCC() != null
					&& (BilConstants.TIPO_OPERAZIONE_PCC_COMUNICAZIONE_DATA_SCADENZA.getConstant().equals(rcpcc.getTipoOperazionePCC().getCodice())
							|| BilConstants.TIPO_OPERAZIONE_PCC_CANCELLAZIONE_COMUNICAZIONI_SCADENZA.getConstant().equals(rcpcc.getTipoOperazionePCC().getCodice()))) {
				ElementoRegistroComunicazioniPCC wrapper = new ElementoRegistroComunicazioniPCC(rcpcc);
				result.add(wrapper);
			}
		}
		return result;
	}
	
	/**
	 * @return the listaComunicazionePagamento
	 */
	public List<ElementoRegistroComunicazioniPCC> getListaComunicazionePagamento() {
		List<ElementoRegistroComunicazioniPCC> result = new ArrayList<ElementoRegistroComunicazioniPCC>();
		// Prendo le comunicazioni di tipo COMUNICAZIONE PAGAMENTO
		for(RegistroComunicazioniPCC rcpcc : getListaRegistroComunicazioniPCC()) {
			if(rcpcc != null && rcpcc.getTipoOperazionePCC() != null && BilConstants.TIPO_OPERAZIONE_PCC_COMUNICAZIONE_PAGAMENTO.getConstant().equals(rcpcc.getTipoOperazionePCC().getCodice())) {
				ElementoRegistroComunicazioniPCC wrapper = new ElementoRegistroComunicazioniPCC(rcpcc);
				result.add(wrapper);
			}
		}
		return result;
	}
	
	/**
	 * @return the documentoAnnullato
	 */
	public boolean isDocumentoAnnullato() {
		return getDocumentoSpesa() != null && StatoOperativoDocumento.ANNULLATO.equals(getDocumentoSpesa().getStatoOperativoDocumento());
	}
	
	/**
	 * @return presentiComunicazioniPagamento
	 */
	public boolean isPresentiComunicazioniPagamento() {
		// Dovrebbe essere piu' veloce di getListaComunicazionePagamento.isEmpty(), ma forse meno chiaro
		for(RegistroComunicazioniPCC rcpcc : getListaRegistroComunicazioniPCC()) {
			if(rcpcc != null && rcpcc.getTipoOperazionePCC() != null && BilConstants.TIPO_OPERAZIONE_PCC_COMUNICAZIONE_PAGAMENTO.getConstant().equals(rcpcc.getTipoOperazionePCC().getCodice())) {
				return true;
			}
		}
		return false;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaRegistriComunicazioniPCCSubdocumento}.
	 * 
	 * @return la request creata
	 */
	public RicercaRegistriComunicazioniPCCSubdocumento creaRequestRicercaRegistriComunicazioniPCCSubdocumento() {
		RicercaRegistriComunicazioniPCCSubdocumento request = creaRequest(RicercaRegistriComunicazioniPCCSubdocumento.class);
		
		request.setSubdocumentoSpesa(getSubdocumentoSpesa());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioQuotaSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioQuotaSpesa creaRequestRicercaDettaglioQuotaSpesa() {
		RicercaDettaglioQuotaSpesa request = creaRequest(RicercaDettaglioQuotaSpesa.class);
		
		request.setSubdocumentoSpesa(getSubdocumentoSpesa());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisciRegistroComunicazioniPCC}.
	 * 
	 * @return la request creata
	 */
	public InserisciRegistroComunicazioniPCC creaRequestInserisciRegistroComunicazioniPCC() {
		InserisciRegistroComunicazioniPCC request = creaRequest(InserisciRegistroComunicazioniPCC.class);
		
		// Popolo i campi del registro
		getRegistroComunicazioniPCC().setEnte(getEnte());
		getRegistroComunicazioniPCC().setDocumentoSpesa(getDocumentoSpesa());
		getRegistroComunicazioniPCC().setSubdocumentoSpesa(getSubdocumentoSpesa());
		getRegistroComunicazioniPCC().setTipoOperazionePCC(getTipoOperazionePCC());
		
		request.setRegistroComunicazioniPCC(getRegistroComunicazioniPCC());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaRegistroComunicazioniPCC}.
	 * 
	 * @return la request creata
	 */
	public AggiornaRegistroComunicazioniPCC creaRequestAggiornaRegistroComunicazioniPCC() {
		AggiornaRegistroComunicazioniPCC request = creaRequest(AggiornaRegistroComunicazioniPCC.class);
		
		// Popolo i campi del registro
		getRegistroComunicazioniPCC().setEnte(getEnte());
		getRegistroComunicazioniPCC().setDocumentoSpesa(getDocumentoSpesa());
		getRegistroComunicazioniPCC().setSubdocumentoSpesa(getSubdocumentoSpesa());
		getRegistroComunicazioniPCC().setTipoOperazionePCC(getTipoOperazionePCC());
		
		request.setRegistroComunicazioniPCC(getRegistroComunicazioniPCC());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaRegistroComunicazioniPCC}.
	 * 
	 * @return la request creata
	 */
	public EliminaRegistroComunicazioniPCC creaRequestEliminaRegistroComunicazioniPCC() {
		EliminaRegistroComunicazioniPCC request = creaRequest(EliminaRegistroComunicazioniPCC.class);
		
		request.setRegistroComunicazioniPCC(getRegistroComunicazioniPCC());
		
		return request;
	}

}
