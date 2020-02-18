/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorAliquotaSubdocumentoIva;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe generica di model per il Documento Iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/06/2014
 *
 */
public class GenericDocumentoIvaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3135321381486498912L;
	
	private Integer uidSubdocumentoIva;
	private Boolean tipoSubdocumentoIvaQuota = Boolean.FALSE;
	
	private Soggetto soggetto;
	
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIva;
	private BigDecimal percentualeAliquotaIva = BigDecimal.ZERO;
	private BigDecimal percentualeIndetraibilitaAliquotaIva = BigDecimal.ZERO;
	
	// Tenuti fuori in quanto opzionali
	private Boolean flagRilevanteIRAP = Boolean.FALSE;
	private Boolean flagIntracomunitarioPreesistente = Boolean.FALSE;
	private Valuta valuta;
	private BigDecimal importoInValuta = BigDecimal.ZERO;
	private AttivitaIva attivitaIva;
	
	// Non salvato
	private TipoRegistroIva tipoRegistroIva;
	
	private List<Valuta> listaValuta = new ArrayList<Valuta>();
	private List<TipoRegistrazioneIva> listaTipoRegistrazioneIva = new ArrayList<TipoRegistrazioneIva>();
	private List<TipoRegistroIva> listaTipoRegistroIva = new ArrayList<TipoRegistroIva>();
	private List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
	private List<RegistroIva> listaRegistroIva = new ArrayList<RegistroIva>();
	
	// Movimenti Iva
//	private List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIva =
//		new SortedSetList<AliquotaSubdocumentoIva>(ComparatorAliquotaSubdocumentoIva.INSTANCE);
	
	private List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIva = new ArrayList<AliquotaSubdocumentoIva>() ;
	private List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIvaNota = new ArrayList<AliquotaSubdocumentoIva>();
	
	private List<AliquotaIva> listaAliquotaIva = new ArrayList<AliquotaIva>();
	private Integer riga;
	private String suffisso;
	
	// Intracomunitario
	private Boolean flagIntracomunitarioUtilizzabile = Boolean.FALSE;
	private TipoRegistroIva tipoRegistroIvaIntracomunitarioDocumento;
	private AttivitaIva attivitaIvaIntracomunitarioDocumento;
	private RegistroIva registroIvaIntracomunitarioDocumento;
	private Integer uidIntracomunitarioDocumento;
	
	private List<AttivitaIva> listaAttivitaIvaIntracomunitario = new ArrayList<AttivitaIva>();
	private List<RegistroIva> listaRegistroIvaIntracomunitario = new ArrayList<RegistroIva>();
	
	//CR 3019
	private boolean movimentoResiduo;
	private boolean registrazioneSenzaProtocollo;
	
	/** Costruttore vuoto di default */
	public GenericDocumentoIvaModel() {
		super();
	}
	
	/**
	 * @return the uidSubdocumentoIva
	 */
	public Integer getUidSubdocumentoIva() {
		return uidSubdocumentoIva;
	}

	/**
	 * @param uidSubdocumentoIva the uidSubdocumentoIva to set
	 */
	public void setUidSubdocumentoIva(Integer uidSubdocumentoIva) {
		this.uidSubdocumentoIva = uidSubdocumentoIva;
	}
	
	/**
	 * @return the tipoSubdocumentoIvaQuota
	 */
	public Boolean getTipoSubdocumentoIvaQuota() {
		return tipoSubdocumentoIvaQuota;
	}

	/**
	 * @param tipoSubdocumentoIvaQuota the tipoSubdocumentoIvaQuota to set
	 */
	public void setTipoSubdocumentoIvaQuota(Boolean tipoSubdocumentoIvaQuota) {
		this.tipoSubdocumentoIvaQuota = tipoSubdocumentoIvaQuota != null ? tipoSubdocumentoIvaQuota : Boolean.FALSE;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the aliquotaSubdocumentoIva
	 */
	public AliquotaSubdocumentoIva getAliquotaSubdocumentoIva() {
		return aliquotaSubdocumentoIva;
	}

	/**
	 * @param aliquotaSubdocumentoIva the aliquotaSubdocumentoIva to set
	 */
	public void setAliquotaSubdocumentoIva(AliquotaSubdocumentoIva aliquotaSubdocumentoIva) {
		this.aliquotaSubdocumentoIva = aliquotaSubdocumentoIva;
	}

	/**
	 * @return the percentualeAliquotaIva
	 */
	public BigDecimal getPercentualeAliquotaIva() {
		return percentualeAliquotaIva;
	}

	/**
	 * @param percentualeAliquotaIva the percentualeAliquotaIva to set
	 */
	public void setPercentualeAliquotaIva(BigDecimal percentualeAliquotaIva) {
		this.percentualeAliquotaIva = percentualeAliquotaIva != null ? percentualeAliquotaIva : BigDecimal.ZERO;
	}

	/**
	 * @return the percentualeIndetraibilitaAliquotaIva
	 */
	public BigDecimal getPercentualeIndetraibilitaAliquotaIva() {
		return percentualeIndetraibilitaAliquotaIva;
	}

	/**
	 * @param percentualeIndetraibilitaAliquotaIva the percentualeIndetraibilitaAliquotaIva to set
	 */
	public void setPercentualeIndetraibilitaAliquotaIva(BigDecimal percentualeIndetraibilitaAliquotaIva) {
		this.percentualeIndetraibilitaAliquotaIva = percentualeIndetraibilitaAliquotaIva != null ? percentualeIndetraibilitaAliquotaIva : BigDecimal.ZERO;
	}

	/**
	 * @return the flagRilevanteIRAP
	 */
	public Boolean getFlagRilevanteIRAP() {
		return flagRilevanteIRAP;
	}

	/**
	 * @param flagRilevanteIRAP the flagRilevanteIRAP to set
	 */
	public void setFlagRilevanteIRAP(Boolean flagRilevanteIRAP) {
		this.flagRilevanteIRAP = flagRilevanteIRAP != null ? flagRilevanteIRAP : Boolean.FALSE;
	}

	/**
	 * @return the flagIntracomunitarioPreesistente
	 */
	public Boolean getFlagIntracomunitarioPreesistente() {
		return flagIntracomunitarioPreesistente;
	}

	/**
	 * @param flagIntracomunitarioPreesistente the flagIntracomunitarioPreesistente to set
	 */
	public void setFlagIntracomunitarioPreesistente(Boolean flagIntracomunitarioPreesistente) {
		this.flagIntracomunitarioPreesistente = flagIntracomunitarioPreesistente != null ? flagIntracomunitarioPreesistente : Boolean.FALSE;
	}

	/**
	 * @return the valuta
	 */
	public Valuta getValuta() {
		return valuta;
	}

	/**
	 * @param valuta the valuta to set
	 */
	public void setValuta(Valuta valuta) {
		this.valuta = valuta;
	}

	/**
	 * @return the importoInValuta
	 */
	public BigDecimal getImportoInValuta() {
		return importoInValuta;
	}

	/**
	 * @param importoInValuta the importoInValuta to set
	 */
	public void setImportoInValuta(BigDecimal importoInValuta) {
		this.importoInValuta = importoInValuta != null ? importoInValuta : BigDecimal.ZERO;
	}

	/**
	 * @return the attivitaIva
	 */
	public AttivitaIva getAttivitaIva() {
		return attivitaIva;
	}

	/**
	 * @param attivitaIva the attivitaIva to set
	 */
	public void setAttivitaIva(AttivitaIva attivitaIva) {
		this.attivitaIva = attivitaIva;
	}

	/**
	 * @return the tipoRegistroIva
	 */
	public TipoRegistroIva getTipoRegistroIva() {
		return tipoRegistroIva;
	}

	/**
	 * @param tipoRegistroIva the tipoRegistroIva to set
	 */
	public void setTipoRegistroIva(TipoRegistroIva tipoRegistroIva) {
		this.tipoRegistroIva = tipoRegistroIva;
	}

	/**
	 * @return the listaValuta
	 */
	public List<Valuta> getListaValuta() {
		return listaValuta;
	}

	/**
	 * @param listaValuta the listaValuta to set
	 */
	public void setListaValuta(List<Valuta> listaValuta) {
		this.listaValuta = listaValuta != null ? listaValuta : new ArrayList<Valuta>();
	}

	/**
	 * @return the listaTipoRegistrazioneIva
	 */
	public List<TipoRegistrazioneIva> getListaTipoRegistrazioneIva() {
		return listaTipoRegistrazioneIva;
	}

	/**
	 * @param listaTipoRegistrazioneIva the listaTipoRegistrazioneIva to set
	 */
	public void setListaTipoRegistrazioneIva(List<TipoRegistrazioneIva> listaTipoRegistrazioneIva) {
		this.listaTipoRegistrazioneIva = listaTipoRegistrazioneIva != null ? listaTipoRegistrazioneIva : new ArrayList<TipoRegistrazioneIva>();
	}

	/**
	 * @return the listaTipoRegistroIva
	 */
	public List<TipoRegistroIva> getListaTipoRegistroIva() {
		return listaTipoRegistroIva;
	}

	/**
	 * @param listaTipoRegistroIva the listaTipoRegistroIva to set
	 */
	public void setListaTipoRegistroIva(List<TipoRegistroIva> listaTipoRegistroIva) {
		this.listaTipoRegistroIva = listaTipoRegistroIva != null ? listaTipoRegistroIva : new ArrayList<TipoRegistroIva>();
	}

	/**
	 * @return the listaAttivitaIva
	 */
	public List<AttivitaIva> getListaAttivitaIva() {
		return listaAttivitaIva;
	}

	/**
	 * @param listaAttivitaIva the listaAttivitaIva to set
	 */
	public void setListaAttivitaIva(List<AttivitaIva> listaAttivitaIva) {
		this.listaAttivitaIva = listaAttivitaIva != null ? listaAttivitaIva : new ArrayList<AttivitaIva>();
	}

	/**
	 * @return the listaRegistroIva
	 */
	public List<RegistroIva> getListaRegistroIva() {
		return listaRegistroIva;
	}

	/**
	 * @param listaRegistroIva the listaRegistroIva to set
	 */
	public void setListaRegistroIva(List<RegistroIva> listaRegistroIva) {
		this.listaRegistroIva = listaRegistroIva != null ? listaRegistroIva : new ArrayList<RegistroIva>();
	}

	/**
	 * @return the listaAliquotaSubdocumentoIva
	 */
	public List<AliquotaSubdocumentoIva> getListaAliquotaSubdocumentoIva() {
		return listaAliquotaSubdocumentoIva != null ? listaAliquotaSubdocumentoIva : new ArrayList<AliquotaSubdocumentoIva>();
	}
	
	/**
	 * @param listaAliquotaSubdocumentoIva the listaAliquotaSubdocumentoIva to set
	 */
	public void setListaAliquotaSubdocumentoIva(List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIva) {
		this.listaAliquotaSubdocumentoIva = listaAliquotaSubdocumentoIva != null ? listaAliquotaSubdocumentoIva : new ArrayList<AliquotaSubdocumentoIva>();
		Collections.sort(this.listaAliquotaSubdocumentoIva, ComparatorAliquotaSubdocumentoIva.INSTANCE);
	}
	
	/**
	 * Aggiunge l'aliquota del subdocumentoIva
	 * @param aliquota l'aliquota da aggiungere
	 */
	public void addAliquotaSubdocumentoIva(AliquotaSubdocumentoIva aliquota) {
		getListaAliquotaSubdocumentoIva().add(aliquota);
		Collections.sort(listaAliquotaSubdocumentoIva, ComparatorAliquotaSubdocumentoIva.INSTANCE);
	}

//	/**
//	 * @param listaAliquotaSubdocumentoIva the listaAliquotaSubdocumentoIva to set
//	 */
//	public void setListaAliquotaSubdocumentoIva(List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIva) {
//		this.listaAliquotaSubdocumentoIva = new SortedSetList<AliquotaSubdocumentoIva>(
//			listaAliquotaSubdocumentoIva != null ? listaAliquotaSubdocumentoIva : new ArrayList<AliquotaSubdocumentoIva>(),
//			ComparatorAliquotaSubdocumentoIva.INSTANCE);
//	}
	
	/**
	 * @return the listaAliquotaSubdocumentoIvaNota
	 */
	public List<AliquotaSubdocumentoIva> getListaAliquotaSubdocumentoIvaNota() {
		return listaAliquotaSubdocumentoIvaNota != null ? listaAliquotaSubdocumentoIvaNota : new ArrayList<AliquotaSubdocumentoIva>();
	}
	
	/**
	 * @param listaAliquotaSubdocumentoIvaNota the listaAliquotaSubdocumentoIvaNota to set
	 */
	public void setListaAliquotaSubdocumentoIvaNota(List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIvaNota) {
		this.listaAliquotaSubdocumentoIvaNota = listaAliquotaSubdocumentoIvaNota != null ? listaAliquotaSubdocumentoIvaNota : new ArrayList<AliquotaSubdocumentoIva>();
		Collections.sort(this.listaAliquotaSubdocumentoIvaNota, ComparatorAliquotaSubdocumentoIva.INSTANCE);
	}
	
	/**
	 * Aggiunge l'aliquota della nota del subdocumentoIva
	 * @param aliquota l'aliquota da aggiungere
	 */
	public void addAliquotaSubdocumentoIvaNota(AliquotaSubdocumentoIva aliquota) {
		getListaAliquotaSubdocumentoIvaNota().add(aliquota);
		Collections.sort(listaAliquotaSubdocumentoIvaNota, ComparatorAliquotaSubdocumentoIva.INSTANCE);
	}

//	/**
//	 * @param listaAliquotaSubdocumentoIvaNota the listaAliquotaSubdocumentoIvaNota to set
//	 */
//	public void setListaAliquotaSubdocumentoIvaNota(List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIvaNota) {
//		this.listaAliquotaSubdocumentoIvaNota = new SortedSetList<AliquotaSubdocumentoIva>(
//			listaAliquotaSubdocumentoIvaNota != null ? listaAliquotaSubdocumentoIvaNota : new ArrayList<AliquotaSubdocumentoIva>(),
//			ComparatorAliquotaSubdocumentoIva.INSTANCE);
//	}
	
	/**
	 * Ottiene il totale degli imponibili dei MovimentiIva.
	 * 
	 * @return il totale degli imponibili
	 */
	public BigDecimal getTotaleImponibileMovimentiIvaNota() {
		BigDecimal result = BigDecimal.ZERO;
		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIvaNota) {
			result = result.add(asi.getImponibile());
		}
		return result;
	}
	
	/**
	 * Ottiene il totale delle imposte dei MovimentiIva.
	 * 
	 * @return il totale delle imposte
	 */
	public BigDecimal getTotaleImpostaMovimentiIvaNota() {
		BigDecimal result = BigDecimal.ZERO;
		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIvaNota) {
			result = result.add(asi.getImposta());
		}
		return result;
	}
	
	/**
	 * Ottiene il totale dei totali dei MovimentiIva.
	 * 
	 * @return il totale dei totali
	 */
	public BigDecimal getTotaleTotaleMovimentiIvaNota() {
		BigDecimal result = BigDecimal.ZERO;
		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIvaNota) {
			result = result.add(asi.getTotale());
		}
		return result;
	}
	
	/**
	 * @return the listaAliquotaIva
	 */
	public List<AliquotaIva> getListaAliquotaIva() {
		return listaAliquotaIva;
	}

	/**
	 * @param listaAliquotaIva the listaAliquotaIva to set
	 */
	public void setListaAliquotaIva(List<AliquotaIva> listaAliquotaIva) {
		this.listaAliquotaIva = listaAliquotaIva != null ? listaAliquotaIva : new ArrayList<AliquotaIva>();
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

	@Override
	public String getSuffisso() {
		return suffisso;
	}

	/**
	 * @param suffisso the suffisso to set
	 */
	public void setSuffisso(String suffisso) {
		this.suffisso = suffisso;
	}

	/**
	 * @return the flagIntracomunitarioUtilizzabile
	 */
	public Boolean getFlagIntracomunitarioUtilizzabile() {
		return flagIntracomunitarioUtilizzabile;
	}

	/**
	 * @param flagIntracomunitarioUtilizzabile the flagIntracomunitarioUtilizzabile to set
	 */
	public void setFlagIntracomunitarioUtilizzabile(Boolean flagIntracomunitarioUtilizzabile) {
		this.flagIntracomunitarioUtilizzabile = flagIntracomunitarioUtilizzabile != null ? flagIntracomunitarioUtilizzabile : Boolean.FALSE;
	}

	/**
	 * @return the tipoRegistroIvaIntracomunitarioDocumento
	 */
	public TipoRegistroIva getTipoRegistroIvaIntracomunitarioDocumento() {
		return tipoRegistroIvaIntracomunitarioDocumento;
	}

	/**
	 * @param tipoRegistroIvaIntracomunitarioDocumento the tipoRegistroIvaIntracomunitarioDocumento to set
	 */
	public void setTipoRegistroIvaIntracomunitarioDocumento(TipoRegistroIva tipoRegistroIvaIntracomunitarioDocumento) {
		this.tipoRegistroIvaIntracomunitarioDocumento = tipoRegistroIvaIntracomunitarioDocumento;
	}

	/**
	 * @return the attivitaIvaIntracomunitarioDocumento
	 */
	public AttivitaIva getAttivitaIvaIntracomunitarioDocumento() {
		return attivitaIvaIntracomunitarioDocumento;
	}

	/**
	 * @param attivitaIvaIntracomunitarioDocumento the attivitaIvaIntracomunitarioDocumento to set
	 */
	public void setAttivitaIvaIntracomunitarioDocumento(AttivitaIva attivitaIvaIntracomunitarioDocumento) {
		this.attivitaIvaIntracomunitarioDocumento = attivitaIvaIntracomunitarioDocumento;
	}

	/**
	 * @return the registroIvaIntracomunitarioDocumento
	 */
	public RegistroIva getRegistroIvaIntracomunitarioDocumento() {
		return registroIvaIntracomunitarioDocumento;
	}

	/**
	 * @param registroIvaIntracomunitarioDocumento the registroIvaIntracomunitarioDocumento to set
	 */
	public void setRegistroIvaIntracomunitarioDocumento(RegistroIva registroIvaIntracomunitarioDocumento) {
		this.registroIvaIntracomunitarioDocumento = registroIvaIntracomunitarioDocumento;
	}

	/**
	 * @return the uidIntracomunitarioDocumento
	 */
	public Integer getUidIntracomunitarioDocumento() {
		return uidIntracomunitarioDocumento;
	}

	/**
	 * @param uidIntracomunitarioDocumento the uidIntracomunitarioDocumento to set
	 */
	public void setUidIntracomunitarioDocumento(Integer uidIntracomunitarioDocumento) {
		this.uidIntracomunitarioDocumento = uidIntracomunitarioDocumento;
	}

	/**
	 * @return the listaAttivitaIvaIntracomunitario
	 */
	public List<AttivitaIva> getListaAttivitaIvaIntracomunitario() {
		return listaAttivitaIvaIntracomunitario;
	}

	/**
	 * @param listaAttivitaIvaIntracomunitario the listaAttivitaIvaIntracomunitario to set
	 */
	public void setListaAttivitaIvaIntracomunitario(List<AttivitaIva> listaAttivitaIvaIntracomunitario) {
		this.listaAttivitaIvaIntracomunitario = listaAttivitaIvaIntracomunitario != null ? listaAttivitaIvaIntracomunitario : new ArrayList<AttivitaIva>();
	}

	/**
	 * @return the listaRegistroIvaIntracomunitario
	 */
	public List<RegistroIva> getListaRegistroIvaIntracomunitario() {
		return listaRegistroIvaIntracomunitario;
	}

	/**
	 * @param listaRegistroIvaIntracomunitario the listaRegistroIvaIntracomunitario to set
	 */
	public void setListaRegistroIvaIntracomunitario(List<RegistroIva> listaRegistroIvaIntracomunitario) {
		this.listaRegistroIvaIntracomunitario = listaRegistroIvaIntracomunitario != null ? listaRegistroIvaIntracomunitario : new ArrayList<RegistroIva>();
	}
	
	/**
	 * @return the movimentoResiduo
	 */
	public boolean isMovimentoResiduo() {
		return movimentoResiduo;
	}

	/**
	 * @param movimentoResiduo the movimentoResiduo to set
	 */
	public void setMovimentoResiduo(boolean movimentoResiduo) {
		this.movimentoResiduo = movimentoResiduo;
	}
	
	/**
	 * @return the registrazioneSenzaProtocollo
	 */
	public boolean isRegistrazioneSenzaProtocollo() {
		return registrazioneSenzaProtocollo;
	}

	/**
	 * @param registrazioneSenzaProtocollo the registrazioneSenzaProtocollo to set
	 */
	public void setRegistrazioneSenzaProtocollo(boolean registrazioneSenzaProtocollo) {
		this.registrazioneSenzaProtocollo = registrazioneSenzaProtocollo;
	}

	/**
	 * Ottiene il totale degli imponibili dei MovimentiIva.
	 * 
	 * @return il totale degli imponibili
	 */
	public BigDecimal getTotaleImponibileMovimentiIva() {
		BigDecimal result = BigDecimal.ZERO;
		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIva) {
			result = result.add(asi.getImponibile());
		}
		return result;
	}
	
	/**
	 * Ottiene il totale delle imposte dei MovimentiIva.
	 * 
	 * @return il totale delle imposte
	 */
	public BigDecimal getTotaleImpostaMovimentiIva() {
		BigDecimal result = BigDecimal.ZERO;
		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIva) {
			result = result.add(asi.getImposta());
		}
		return result;
	}
	
	/**
	 * Ottiene il totale dei totali dei MovimentiIva.
	 * 
	 * @return il totale dei totali
	 */
	public BigDecimal getTotaleTotaleMovimentiIva() {
		BigDecimal result = BigDecimal.ZERO;
		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIva) {
			result = result.add(asi.getTotale());
		}
		return result;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaValuta}.
	 * 
	 * @return la request creata
	 */
	public RicercaValuta creaRequestRicercaValuta() {
		RicercaValuta request = creaRequest(RicercaValuta.class);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoRegistrazioneIva}.
	 * 
	 * @param entrata se il tipo &eacute di entrata
	 * @param spesa   se il tipo &eacute di spesa
	 * 
	 * @return la request creata
	 */
	public RicercaTipoRegistrazioneIva creaRequestRicercaTipoRegistrazioneIva(Boolean entrata, Boolean spesa) {
		RicercaTipoRegistrazioneIva request = creaRequest(RicercaTipoRegistrazioneIva.class);
		
		TipoRegistrazioneIva tri = new TipoRegistrazioneIva();
		tri.setFlagTipoRegistrazioneIvaEntrata(entrata);
		tri.setFlagTipoRegistrazioneIvaSpesa(spesa);
		tri.setEnte(getEnte());
		
		request.setTipoRegistrazioneIva(tri);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAttivitaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaAttivitaIva creaRequestRicercaAttivitaIva() {
		RicercaAttivitaIva request = creaRequest(RicercaAttivitaIva.class);
		
		AttivitaIva ai = new AttivitaIva();
		request.setAttivitaIva(ai);
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaRegistroIva}.
	 * 
	 * @param tipo il tipo di registro
	 * @param attivita     l'attivita Iva
	 * 
	 * @return la request creata
	 */
	public RicercaRegistroIva creaRequestRicercaRegistroIva(TipoRegistroIva tipo, AttivitaIva attivita) {
		RicercaRegistroIva request = creaRequest(RicercaRegistroIva.class);
		
		RegistroIva ri = new RegistroIva();
		ri.setEnte(getEnte());
		ri.setTipoRegistroIva(tipo);
		
		if(attivita != null && attivita.getUid() != 0) {
			impostaAttivitaIva(ri, attivita);
		}
		request.setRegistroIva(ri);
		
		return request;
	}
	
	/**
	 * Impostazione dell'attivit&agrave; iva nel registro.
	 * 
	 * @param registro il registro da popolare
	 * @param attivita l'attivit&agrave; tramite cui popolare
	 */
	private void impostaAttivitaIva(RegistroIva registro, AttivitaIva attivita) {
		GruppoAttivitaIva gai;
		if(attivita.getGruppoAttivitaIva() != null && attivita.getGruppoAttivitaIva().getUid() != 0) {
			gai = ReflectionUtil.deepClone(attivita.getGruppoAttivitaIva());
		} else {
			gai = new GruppoAttivitaIva();
		}
		AttivitaIva att = ReflectionUtil.deepClone(attivita);
		att.setGruppoAttivitaIva(null);
		gai.setListaAttivitaIva(Arrays.asList(att));
		registro.setGruppoAttivitaIva(gai);
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAliquotaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaAliquotaIva creaRequestRicercaAliquotaIva() {
		RicercaAliquotaIva request = creaRequest(RicercaAliquotaIva.class);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		request.setEnte(getEnte());
		request.setParametroSoggettoK(creaParametroRicercaSoggettoK());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaRelazioneAttivitaIvaCapitolo}.
	 * 
	 * @param capitolo il capitolo rispetto cui cercare le relazioni
	 * 
	 * @return la request creata
	 */
	public RicercaRelazioneAttivitaIvaCapitolo creaRequestRicercaRelazioneAttivitaIvaCapitolo(Capitolo<?, ?> capitolo) {
		RicercaRelazioneAttivitaIvaCapitolo request = creaRequest(RicercaRelazioneAttivitaIvaCapitolo.class);
		
		request.setBilancio(getBilancio());
		request.setCapitolo(capitolo);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @param tipoFamigliaDocumento il tipo di famiglia del documento
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) {
		RicercaTipoDocumento request = creaRequest(RicercaTipoDocumento.class);
		
		request.setEnte(getEnte());
		request.setTipoFamDoc(tipoFamigliaDocumento);
		
		return request;
	}
	
	/**
	 * Crea un'utility per la ricerca del soggetto.
	 * 
	 * @return l'utility creata
	 * 
	 * @throws NullPointerException nel caso in cui il documento o il soggetto siano <code>null</code>
	 */
	private ParametroRicercaSoggettoK creaParametroRicercaSoggettoK() {
		ParametroRicercaSoggettoK utility = new ParametroRicercaSoggettoK();
		utility.setCodice(getSoggetto().getCodiceSoggetto());
		return utility;
	}
	
	/**
	 * Popola l'attivita se presente.
	 * <br>
	 * Serve per popolare il GruppoAttivitaIva.
	 */
	public void popolaAttivitaIvaSePresente() {
		AttivitaIva ai = ComparatorUtils.searchByUid(getListaAttivitaIva(), getAttivitaIva());
		setAttivitaIva(ai);
	}

}
