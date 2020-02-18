/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe astratta di model per il documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/03/2014
 *
 */
public class GenericDocumentoSpesaModel extends GenericDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2784842126402261243L;

	private DocumentoSpesa documento;
	
	private List<TipoImpresa> listaTipoImpresa = new ArrayList<TipoImpresa>();
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<CommissioniDocumento> listaCommissioniDocumento = new ArrayList<CommissioniDocumento>();
	private List<NaturaOnere> listaNaturaOnere = new ArrayList<NaturaOnere>();
	// Codice tributo
	private List<TipoOnere> listaTipoOnere = new ArrayList<TipoOnere>();
	// Attività
	private List<AttivitaOnere> listaAttivitaOnere = new ArrayList<AttivitaOnere>();
	private List<Causale770> listaCausale770 = new ArrayList<Causale770>();
	
	// PCC
	private List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC = new ArrayList<CodiceUfficioDestinatarioPCC>();
	private List<CodicePCC> listaCodicePCC = new ArrayList<CodicePCC>();
	// SIAC-5311 SIOPE+
	private List<SiopeDocumentoTipo> listaSiopeDocumentoTipo = new ArrayList<SiopeDocumentoTipo>();
	private List<SiopeDocumentoTipoAnalogico> listaSiopeDocumentoTipoAnalogico = new ArrayList<SiopeDocumentoTipoAnalogico>();
	
	//SIAC-5346
	private Integer uidCodiceUfficioDestinatarioPccToFilter;
	private List<CodicePCC> listaCodicePCCFiltered = new ArrayList<CodicePCC>();
	private boolean disabilitaPCCSeUnivoco;
	

	/**
	 * @return the documento
	 */
	public DocumentoSpesa getDocumento() {
		return documento;
	}
	
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoSpesa documento) {
		this.documento = documento;
	}
	
	/**
	 * @return the listaTipoImpresa
	 */
	public List<TipoImpresa> getListaTipoImpresa() {
		return listaTipoImpresa;
	}
	
	/**
	 * @param listaTipoImpresa the listaTipoImpresa to set
	 */
	public void setListaTipoImpresa(List<TipoImpresa> listaTipoImpresa) {
		this.listaTipoImpresa = (listaTipoImpresa == null ? new ArrayList<TipoImpresa>() : listaTipoImpresa);
	}
	
	/**
	 * @return the listaSedeSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}
	
	/**
	 * @param listaSedeSecondariaSoggetto the listaSedeSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = (listaSedeSecondariaSoggetto == null ? new ArrayList<SedeSecondariaSoggetto>() : listaSedeSecondariaSoggetto);
	}
	
	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}
	
	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = (listaModalitaPagamentoSoggetto == null ? new ArrayList<ModalitaPagamentoSoggetto>() : listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * @return the listaCommissioniDocumento
	 */
	public List<CommissioniDocumento> getListaCommissioniDocumento() {
		return listaCommissioniDocumento;
	}
	
	/**
	 * @param listaCommissioniDocumento the listaCommissioniDocumento to set
	 */
	public void setListaCommissioniDocumento(List<CommissioniDocumento> listaCommissioniDocumento) {
		this.listaCommissioniDocumento = (listaCommissioniDocumento == null ? new ArrayList<CommissioniDocumento>() : listaCommissioniDocumento);
	}
	
	/**
	 * @return the listaNaturaOnere
	 */
	public List<NaturaOnere> getListaNaturaOnere() {
		return listaNaturaOnere;
	}
	
	/**
	 * @param listaNaturaOnere the listaNaturaOnere to set
	 */
	public void setListaNaturaOnere(List<NaturaOnere> listaNaturaOnere) {
		this.listaNaturaOnere = (listaNaturaOnere == null ? new ArrayList<NaturaOnere>() : listaNaturaOnere);
	}
	
	/**
	 * @return the listaTipoOnere
	 */
	public List<TipoOnere> getListaTipoOnere() {
		return listaTipoOnere;
	}
	
	/**
	 * @param listaTipoOnere the listaTipoOnere to set
	 */
	public void setListaTipoOnere(List<TipoOnere> listaTipoOnere) {
		this.listaTipoOnere = (listaTipoOnere == null ? new ArrayList<TipoOnere>() : listaTipoOnere);
	}
	
	/**
	 * @return the listaAttivitaOnere
	 */
	public List<AttivitaOnere> getListaAttivitaOnere() {
		return listaAttivitaOnere;
	}
	
	/**
	 * @param listaAttivitaOnere the listaAttivitaOnere to set
	 */
	public void setListaAttivitaOnere(List<AttivitaOnere> listaAttivitaOnere) {
		this.listaAttivitaOnere = (listaAttivitaOnere == null ? new ArrayList<AttivitaOnere>() : listaAttivitaOnere);
	}
	
	/**
	 * @return the listaCausale770
	 */
	public List<Causale770> getListaCausale770() {
		return listaCausale770;
	}
	
	/**
	 * @param listaCausale770 the listaCausale770 to set
	 */
	public void setListaCausale770(List<Causale770> listaCausale770) {
		this.listaCausale770 = (listaCausale770 == null ? new ArrayList<Causale770>() : listaCausale770);
	}

	/**
	 * @return the listaCodiceUfficioDestinatarioPCC
	 */
	public List<CodiceUfficioDestinatarioPCC> getListaCodiceUfficioDestinatarioPCC() {
		return listaCodiceUfficioDestinatarioPCC;
	}

	/**
	 * @param listaCodiceUfficioDestinatarioPCC the listaCodiceUfficioDestinatarioPCC to set
	 */
	public void setListaCodiceUfficioDestinatarioPCC(List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC) {
		this.listaCodiceUfficioDestinatarioPCC = listaCodiceUfficioDestinatarioPCC != null ? listaCodiceUfficioDestinatarioPCC : new ArrayList<CodiceUfficioDestinatarioPCC>();
	}

	/**
	 * @return the listaCodicePCC
	 */
	public List<CodicePCC> getListaCodicePCC() {
		return listaCodicePCC;
	}

	/**
	 * @param listaCodicePCC the listaCodicePCC to set
	 */
	public void setListaCodicePCC(List<CodicePCC> listaCodicePCC) {
		this.listaCodicePCC = listaCodicePCC != null ? listaCodicePCC : new ArrayList<CodicePCC>();
	}
	
	/**
	 * @return the listaSiopeDocumentoTipo
	 */
	public List<SiopeDocumentoTipo> getListaSiopeDocumentoTipo() {
		return listaSiopeDocumentoTipo;
	}

	/**
	 * @param listaSiopeDocumentoTipo the listaSiopeDocumentoTipo to set
	 */
	public void setListaSiopeDocumentoTipo(List<SiopeDocumentoTipo> listaSiopeDocumentoTipo) {
		this.listaSiopeDocumentoTipo = listaSiopeDocumentoTipo != null ? listaSiopeDocumentoTipo : new ArrayList<SiopeDocumentoTipo>();
	}

	/**
	 * @return the listaSiopeDocumentoTipoAnalogico
	 */
	public List<SiopeDocumentoTipoAnalogico> getListaSiopeDocumentoTipoAnalogico() {
		return listaSiopeDocumentoTipoAnalogico;
	}

	/**
	 * @param listaSiopeDocumentoTipoAnalogico the listaSiopeDocumentoTipoAnalogico to set
	 */
	public void setListaSiopeDocumentoTipoAnalogico(List<SiopeDocumentoTipoAnalogico> listaSiopeDocumentoTipoAnalogico) {
		this.listaSiopeDocumentoTipoAnalogico = listaSiopeDocumentoTipoAnalogico != null ? listaSiopeDocumentoTipoAnalogico : new ArrayList<SiopeDocumentoTipoAnalogico>();
	}
	
	/**
	 * Gets the uid codice ufficio destinatario pcc tofilter.
	 *
	 * @return the uidCodiceUfficioDestinatarioPccTofilter
	 */
	public Integer getUidCodiceUfficioDestinatarioPccToFilter() {
		return uidCodiceUfficioDestinatarioPccToFilter;
	}

	/**
	 * Sets the uid codice ufficio destinatario pcc tofilter.
	 *
	 * @param uidCodiceUfficioDestinatarioPccToFilter the new uid codice ufficio destinatario pcc tofilter
	 */
	public void setUidCodiceUfficioDestinatarioPccToFilter(Integer uidCodiceUfficioDestinatarioPccToFilter) {
		this.uidCodiceUfficioDestinatarioPccToFilter = uidCodiceUfficioDestinatarioPccToFilter;
	}

	/**
	 * Gets the lista codice PCC filtered.
	 *
	 * @return the listaCodicePCCFiltered
	 */
	public List<CodicePCC> getListaCodicePCCFiltered() {
		return listaCodicePCCFiltered;
	}

	/**
	 * Sets the lista codice PCC filtered.
	 *
	 * @param listaCodicePCCFiltered the listaCodicePCCFiltered to set
	 */
	public void setListaCodicePCCFiltered(List<CodicePCC> listaCodicePCCFiltered) {
		this.listaCodicePCCFiltered = listaCodicePCCFiltered != null ? listaCodicePCCFiltered : new ArrayList<CodicePCC>();
	}
	
	/**
	 * @return the disabilitaPCCSeUnivoco
	 */
	public boolean isDisabilitaPCCSeUnivoco() {
		return disabilitaPCCSeUnivoco;
	}

	/**
	 * @param disabilitaPCCSeUnivoco the disabilitaPCCSeUnivoco to set
	 */
	public void setDisabilitaPCCSeUnivoco(boolean disabilitaPCCSeUnivoco) {
		this.disabilitaPCCSeUnivoco = disabilitaPCCSeUnivoco;
	}

	/**
	 * @return the stringaDescrizioneSAC
	 */
	public String getStringaDescrizioneSAC() {
		return getDocumento() == null || getDocumento().getStrutturaAmministrativoContabile() == null
			? ""
			: getDocumento().getStrutturaAmministrativoContabile().getCodice() + "-" + getDocumento().getStrutturaAmministrativoContabile().getDescrizione();
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodiceUfficioDestinatarioPCC}.
	 * 
	 * @param struttureAmministrativoContabili la lista delle strutture per cui filtrare
	 * @return la request creata
	 */
	public RicercaCodiceUfficioDestinatarioPCC creaRequestRicercaCodiceUfficioDestinatarioPCC(List<StrutturaAmministrativoContabile> struttureAmministrativoContabili) {
		RicercaCodiceUfficioDestinatarioPCC request = creaRequest(RicercaCodiceUfficioDestinatarioPCC.class);
		
		request.setStruttureAmministrativoContabili(struttureAmministrativoContabili);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodicePCC}.
	 *
	 * @param struttureAmministrativoContabili la lista delle strutture per cui filtrare
	 * @return la request creata
	 */
	public RicercaCodicePCC creaRequestRicercaCodicePCC(List<StrutturaAmministrativoContabile> struttureAmministrativoContabili) {
		RicercaCodicePCC request = creaRequest(RicercaCodicePCC.class);
		
		request.setStruttureAmministrativoContabili(struttureAmministrativoContabili);
		 
		CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC = new CodiceUfficioDestinatarioPCC();
		//sara' sempre null tranne quando viene richiamato dal javascript
		codiceUfficioDestinatarioPCC.setUid(uidCodiceUfficioDestinatarioPccToFilter != null ? uidCodiceUfficioDestinatarioPccToFilter.intValue() : 0);
		request.setCodiceUfficioDestinatarioPCC(codiceUfficioDestinatarioPCC);
		return request;
	}
	
	/**
	 * CR-2831-2885: calcola la data di scadenza rispetto al valore di default per il termine di pagamento
	 */
	public void calcolaDataScadenzaDefault() {

		//valore di default per il termine di pagamento
		Integer termineScadenza = BilConstants.TERMINE_PAGAMENTO.getId();
		Date dataDiPartenza = getDocumento().getDataRicezionePortale() != null ? getDocumento().getDataRicezionePortale() :
			getDocumento().getDataRepertorio() != null ?  getDocumento().getDataRepertorio() : getDocumento().getDataEmissione();
		Date dataScadenza =  DateUtils.addDays(dataDiPartenza, termineScadenza.intValue());
		getDocumento().setDataScadenza(dataScadenza);
	}
	
}
