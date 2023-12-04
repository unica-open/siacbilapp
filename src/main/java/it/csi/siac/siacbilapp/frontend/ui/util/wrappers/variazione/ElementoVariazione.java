/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siaccorser.model.OperazioneAsincrona;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di wrap per la variazione
 * @author Daniele Argiolas
 *
 */
public class ElementoVariazione implements Serializable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7486794176784318820L;

	private int uid;
	private Integer numero;
	private String applicazione;
	private String descrizione;
	private TipoVariazione tipoVariazione;
	private Integer anno;
	
	// Provvedimento PEG
	private String provvedimento;
	// Provvedimento di bilancio
	private String provvedimentoBilancio;
	private StatoOperativoVariazioneBilancio statoVariazione;
	//SIAC-6177
	private ElementoStatoOperativoVariazione elementoStatoOperativoVariazione;
	
	//SIAC-6884
	private Date dataAperturaProposta;
	private Date dataChiusuraProposta;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileDirezioneProponente;
	//SIAC-8264
	private String tipologiaUltimaOperazioneEffettuata="";
	private String statoUltimaOperazioneEffettuata = "";;
	private String dataUltimaOperazioneEffettuata = "";
	
	/**
	 * @return the stringaDataAperturaProposta
	 */
	public String getStringaDataAperturaProposta() {
		return dataAperturaProposta != null ? FormatUtils.formatDate(dataAperturaProposta) : "";
	}

	/**
	 * @return the stringaDataChiusuraProposta
	 */
	public String getStringaDataChiusuraProposta() {
		return dataChiusuraProposta != null ? FormatUtils.formatDate(dataChiusuraProposta) : "";
	}	
	
	public Date getDataAperturaProposta() {
		return dataAperturaProposta;
	}

	public void setDataAperturaProposta(Date dataAperturaProposta) {
		this.dataAperturaProposta = dataAperturaProposta;
	}

	public Date getDataChiusuraProposta() {
		return dataChiusuraProposta;
	}

	public void setDataChiusuraProposta(Date dataChiusuraProposta) {
		this.dataChiusuraProposta = dataChiusuraProposta;
	}

	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileDirezioneProponente() {
		return strutturaAmministrativoContabileDirezioneProponente;
	}

	public void setStrutturaAmministrativoContabileDirezioneProponente(
			StrutturaAmministrativoContabile strutturaAmministrativoContabileDirezioneProponente) {
		this.strutturaAmministrativoContabileDirezioneProponente = strutturaAmministrativoContabileDirezioneProponente;
	}
	

	/** Costruttore vuoto di default */
	public ElementoVariazione(){
		super();
	}

	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the numero
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * @return the applicazione
	 */
	public String getApplicazione() {
		return applicazione;
	}

	/**
	 * @param applicazione the applicazione to set
	 */
	public void setApplicazione(String applicazione) {
		this.applicazione = applicazione;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the tipoVariazione
	 */
	public TipoVariazione getTipoVariazione() {
		return tipoVariazione;
	}

	/**
	 * @param tipoVariazione the tipoVariazione to set
	 */
	public void setTipoVariazione(TipoVariazione tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}

	/**
	 * @return the provvedimento
	 */
	public String getProvvedimento() {
		return provvedimento;
	}

	/**
	 * @param provvedimento the provvedimento to set
	 */
	public void setProvvedimento(String provvedimento) {
		this.provvedimento = provvedimento;
	}

	/**
	 * @return the provvedimentoBilancio
	 */
	public String getProvvedimentoBilancio() {
		return provvedimentoBilancio;
	}

	/**
	 * @param provvedimentoBilancio the provvedimentoBilancio to set
	 */
	public void setProvvedimentoBilancio(String provvedimentoBilancio) {
		this.provvedimentoBilancio = provvedimentoBilancio;
	}

	/**
	 * @return the statoVariazione
	 */
	public StatoOperativoVariazioneBilancio getStatoVariazione() {
		return statoVariazione;
	}

	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(
			StatoOperativoVariazioneBilancio statoVariazione) {
		this.statoVariazione = statoVariazione;
	}

	/**
	 * @return the elementoStatoOperativoVariazione
	 */
	public ElementoStatoOperativoVariazione getElementoStatoOperativoVariazione() {
		return elementoStatoOperativoVariazione;
	}

	/**
	 * @param elementoStatoOperativoVariazione the elementoStatoOperativoVariazione to set
	 */
	public void setElementoStatoOperativoVariazione(ElementoStatoOperativoVariazione elementoStatoOperativoVariazione) {
		this.elementoStatoOperativoVariazione = elementoStatoOperativoVariazione;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return this.anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getTipologiaUltimaOperazioneEffettuata() {
		return tipologiaUltimaOperazioneEffettuata;
	}

	public void setTipologiaUltimaOperazioneEffettuata(String tipologiaUltimaOperazioneEffettuata) {
		this.tipologiaUltimaOperazioneEffettuata = tipologiaUltimaOperazioneEffettuata;
	}

	public String getStatoUltimaOperazioneEffettuata() {
		return statoUltimaOperazioneEffettuata;
	}

	public void setStatoUltimaOperazioneEffettuata(String statoUltimaOperazioneEffettuata) {
		this.statoUltimaOperazioneEffettuata = statoUltimaOperazioneEffettuata;
	}

	public String getDataUltimaOperazioneEffettuata() {
		return dataUltimaOperazioneEffettuata;
	}

	public void setDataUltimaOperazioneEffettuata(String dataUltimaOperazioneEffettuata) {
		this.dataUltimaOperazioneEffettuata = dataUltimaOperazioneEffettuata;
	}

}

