/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per la definizione della variazione. Contiene una mappatura del model.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 15/10/2013
 *
 */
public class DefinisciVariazioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 565379629866677570L;
	
	private ApplicazioneVariazione applicazione;
	private String descrizioneVariazione;
	private TipoVariazione tipoVariazione;
	private Integer annoVariazione;
	
	// Per Bonita
	private Boolean variazioneAOrganoAmministativo;
	private Boolean variazioneAOrganoLegislativo;
	
	// Liste
	private List<ApplicazioneVariazione> listaTipoApplicazione = new ArrayList<ApplicazioneVariazione>();
	private List<TipoVariazione> listaTipoVariazione = new ArrayList<TipoVariazione>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	// Azione a cui redirigere
	private String azioneCuiRedirigere;
	
	// Per il form di inserimento del provvedimento+
	private AttoAmministrativo attoAmministrativo;
	private Integer uidProvvedimento;
//	private TipoAtto tipoAtto;
//	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
//	private Integer numeroProvvedimento;
//	private Integer annoProvvedimento;
//	private String statoProvvedimento;
	
	private Integer uidProvvedimentoRadio;
	private Boolean annoVariazioneAbilitato;
	
	//SIAC-3736
	private Boolean caricaResidui = Boolean.FALSE;
	private boolean showCaricaResidui = true;
	private boolean anagraficaInserita;
	
	//SIAC-4737
	// Per il provvedimento di variazione di bilancio
	private AttoAmministrativo attoAmministrativoAggiuntivo;
//	private StrutturaAmministrativoContabile strutturaAmministrativoContabileAggiuntivo;
//	private TipoAtto tipoAttoAggiuntivo;
//	private AttoAmministrativo attoAmministrativoAggiuntivoOld;
	private String stringaProvvedimentoAggiuntivo;
	//SIAC-6177
	private String etichettaGiunta;
	private String etichettaConsiglio;
	
	//SIAC-6884
	private Date dataApertura;
	private List<StrutturaAmministrativoContabile> listaDirezioni = new ArrayList<StrutturaAmministrativoContabile>();
	private StrutturaAmministrativoContabile direzioneProponente; 
			
	public StrutturaAmministrativoContabile getDirezioneProponente() {
		return direzioneProponente;
	}

	public void setDirezioneProponente(StrutturaAmministrativoContabile direzioneProponente) {
		this.direzioneProponente = direzioneProponente;
	}

	public List<StrutturaAmministrativoContabile> getListaDirezioni() {
		return listaDirezioni;
	}

	public void setListaDirezioni(List<StrutturaAmministrativoContabile> listaDirezioni) {
		this.listaDirezioni = listaDirezioni;
	}

	public Date getDataApertura() {
		return dataApertura;
	}
	
	public void setDataApertura(Date dataApertura) {
		this.dataApertura = dataApertura;
	}
		
	public String getDataAperturaFormatted(){
		String result = "";
		if(dataApertura != null){
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy"); 
			result = sd.format(dataApertura);
		}
		return result;
	}
	//end SIAC-6884
	
	/** Costruttore vuoto di default */
	public DefinisciVariazioneModel() {
		super();
	}

	/**
	 * @return the tipoApplicazione
	 */
	public ApplicazioneVariazione getApplicazione() {
		return applicazione;
	}

	/**
	 * @param tipoApplicazione the tipoApplicazione to set
	 */
	public void setApplicazione(ApplicazioneVariazione tipoApplicazione) {
		this.applicazione = tipoApplicazione;
	}

	/**
	 * @return the descrizioneVariazione
	 */
	public String getDescrizioneVariazione() {
		return descrizioneVariazione;
	}

	/**
	 * @param descrizioneVariazione the descrizioneVariazione to set
	 */
	public void setDescrizioneVariazione(String descrizioneVariazione) {
		this.descrizioneVariazione = descrizioneVariazione;
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
	 * @return the annoVariazione
	 */
	public Integer getAnnoVariazione() {
		return annoVariazione;
	}

	/**
	 * @param annoVariazione the annoVariazione to set
	 */
	public void setAnnoVariazione(Integer annoVariazione) {
		this.annoVariazione = annoVariazione;
	}

	/**
	 * @return the variazioneAOrganoAmministativo
	 */
	public Boolean getVariazioneAOrganoAmministativo() {
		return variazioneAOrganoAmministativo;
	}

	/**
	 * @param variazioneAOrganoAmministativo the variazioneAOrganoAmministativo to set
	 */
	public void setVariazioneAOrganoAmministativo(Boolean variazioneAOrganoAmministativo) {
		this.variazioneAOrganoAmministativo = variazioneAOrganoAmministativo;
	}

	/**
	 * @return the variazioneAOrganoLegislativo
	 */
	public Boolean getVariazioneAOrganoLegislativo() {
		return variazioneAOrganoLegislativo;
	}

	/**
	 * @param variazioneAOrganoLegislativo the variazioneAOrganoLegislativo to set
	 */
	public void setVariazioneAOrganoLegislativo(Boolean variazioneAOrganoLegislativo) {
		this.variazioneAOrganoLegislativo = variazioneAOrganoLegislativo;
	}

	/**
	 * @return the listaTipoApplicazione
	 */
	public List<ApplicazioneVariazione> getListaTipoApplicazione() {
		return listaTipoApplicazione;
	}

	/**
	 * @param listaTipoApplicazione the listaTipoApplicazione to set
	 */
	public void setListaTipoApplicazione(List<ApplicazioneVariazione> listaTipoApplicazione) {
		this.listaTipoApplicazione = listaTipoApplicazione;
	}

	/**
	 * @return the listaTipoVariazione
	 */
	public List<TipoVariazione> getListaTipoVariazione() {
		return listaTipoVariazione;
	}

	/**
	 * @param listaTipoVariazione the listaTipoVariazione to set
	 */
	public void setListaTipoVariazione(List<TipoVariazione> listaTipoVariazione) {
		this.listaTipoVariazione = listaTipoVariazione;
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}

	/**
	 * @return the azioneCuiRedirigere
	 */
	public String getAzioneCuiRedirigere() {
		return azioneCuiRedirigere;
	}

	/**
	 * @param azioneCuiRedirigere the azioneCuiRedirigere to set
	 */
	public void setAzioneCuiRedirigere(String azioneCuiRedirigere) {
		this.azioneCuiRedirigere = azioneCuiRedirigere;
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

//	/**
//	 * @return the tipoAtto
//	 */
//	public TipoAtto getTipoAtto() {
//		return tipoAtto;
//	}
//
//	/**
//	 * @param tipoAtto the tipoAtto to set
//	 */
//	public void setTipoAtto(TipoAtto tipoAtto) {
//		this.tipoAtto = tipoAtto;
//	}
//
//	/**
//	 * @return the strutturaAmministrativoContabile
//	 */
//	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
//		return strutturaAmministrativoContabile;
//	}
//
//	/**
//	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
//	 */
//	public void setStrutturaAmministrativoContabile(
//			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
//		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
//	}

	/**
	 * @return the uidProvvedimento
	 */
	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}

	/**
	 * @param uidProvvedimento the uidProvvedimento to set
	 */
	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}

//	/**
//	 * @return the numeroProvvedimento
//	 */
//	public Integer getNumeroProvvedimento() {
//		return numeroProvvedimento;
//	}
//
//	/**
//	 * @param numeroProvvedimento the numeroProvvedimento to set
//	 */
//	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
//		this.numeroProvvedimento = numeroProvvedimento;
//	}
//
//	/**
//	 * @return the annoProvvedimento
//	 */
//	public Integer getAnnoProvvedimento() {
//		return annoProvvedimento;
//	}
//
//	/**
//	 * @param annoProvvedimento the annoProvvedimento to set
//	 */
//	public void setAnnoProvvedimento(Integer annoProvvedimento) {
//		this.annoProvvedimento = annoProvvedimento;
//	}
//
//	/**
//	 * @return the statoProvvedimento
//	 */
//	public String getStatoProvvedimento() {
//		return statoProvvedimento;
//	}

//	/**
//	 * @param statoProvvedimento the statoProvvedimento to set
//	 */
//	public void setStatoProvvedimento(String statoProvvedimento) {
//		this.statoProvvedimento = statoProvvedimento;
//	}

	/**
	 * @return the uidProvvedimentoRadio
	 */
	public Integer getUidProvvedimentoRadio() {
		return uidProvvedimentoRadio;
	}

	/**
	 * @param uidProvvedimentoRadio the uidProvvedimentoRadio to set
	 */
	public void setUidProvvedimentoRadio(Integer uidProvvedimentoRadio) {
		this.uidProvvedimentoRadio = uidProvvedimentoRadio;
	}

	/**
	 * @return the annoVariazioneAbilitato
	 */
	public Boolean getAnnoVariazioneAbilitato() {
		return annoVariazioneAbilitato;
	}

	/**
	 * @param annoVariazioneAbilitato the annoVariazioneAbilitato to set
	 */
	public void setAnnoVariazioneAbilitato(Boolean annoVariazioneAbilitato) {
		this.annoVariazioneAbilitato = annoVariazioneAbilitato;
	}
	
	/**
	 * @return the caricaResidui
	 */
	public Boolean getCaricaResidui() {
		return caricaResidui;
	}

	/**
	 * @param caricaResidui the Caricaresidui to set
	 */
	public void setCaricaResidui(Boolean caricaResidui) {
		this.caricaResidui = caricaResidui;
	}

	/**
	 * @return the showCarica Residui
	 */
	public boolean getShowCaricaResidui() {
		return showCaricaResidui;
	}

	/**
	 * @param showCaricaResidui the showCaricaResidui to set
	 */
	public void setShowCaricaResidui(boolean showCaricaResidui) {
		this.showCaricaResidui = showCaricaResidui;
	}

	/**
	 * @return the anagraficaInserita
	 */
	public boolean isAnagraficaInserita() {
		return anagraficaInserita;
	}

	/**
	 * @param anagraficaInserita the anagraficaInserita to set
	 */
	public void setAnagraficaInserita(boolean anagraficaInserita) {
		this.anagraficaInserita = anagraficaInserita;
	}

	
	/**
	 * Gets the atto amministrativo aggiuntivo.
	 *
	 * @return the atto amministrativo aggiuntivo
	 */
	public AttoAmministrativo getAttoAmministrativoAggiuntivo() {
		return attoAmministrativoAggiuntivo;
	}

//	/**
//	 * Gets the struttura amministrativo contabile aggiuntivo.
//	 *
//	 * @return the struttura amministrativo contabile aggiuntivo
//	 */
//	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileAggiuntivo() {
//		return strutturaAmministrativoContabileAggiuntivo;
//	}
//
//	/**
//	 * Gets the tipo atto aggiuntivo.
//	 *
//	 * @return the tipo atto aggiuntivo
//	 */
//	public TipoAtto getTipoAttoAggiuntivo() {
//		return tipoAttoAggiuntivo;
//	}
//
//	/**
//	 * Gets the atto amministrativo aggiuntivo old.
//	 *
//	 * @return the atto amministrativo aggiuntivo old
//	 */
//	public AttoAmministrativo getAttoAmministrativoAggiuntivoOld() {
//		return attoAmministrativoAggiuntivoOld;
//	}
//
	/**
	 * Gets the stringa provvedimento aggiuntivo.
	 *
	 * @return the stringa provvedimento aggiuntivo
	 */
	public String getStringaProvvedimentoAggiuntivo() {
		return stringaProvvedimentoAggiuntivo;
	}

	/**
	 * Sets the atto amministrativo aggiuntivo.
	 *
	 * @param attoAmministrativoAggiuntivo the new atto amministrativo aggiuntivo
	 */
	public void setAttoAmministrativoAggiuntivo(AttoAmministrativo attoAmministrativoAggiuntivo) {
		this.attoAmministrativoAggiuntivo = attoAmministrativoAggiuntivo;
	}
	
	

	/**
	 * @return the etichettaGiunta
	 */
	public String getEtichettaGiunta() {
		return etichettaGiunta;
	}

	/**
	 * @param etichettaGiunta the etichettaGiunta to set
	 */
	public void setEtichettaGiunta(String etichettaGiunta) {
		this.etichettaGiunta = etichettaGiunta;
	}

	/**
	 * @return the etichettaConsiglio
	 */
	public String getEtichettaConsiglio() {
		return etichettaConsiglio;
	}

	/**
	 * @param etichettaConsiglio the etichettaConsiglio to set
	 */
	public void setEtichettaConsiglio(String etichettaConsiglio) {
		this.etichettaConsiglio = etichettaConsiglio;
	}
//
//	/**
//	 * Sets the struttura amministrativo contabile aggiuntivo.
//	 *
//	 * @param strutturaAmministrativoContabileAggiuntivo the new struttura amministrativo contabile aggiuntivo
//	 */
//	public void setStrutturaAmministrativoContabileAggiuntivo(StrutturaAmministrativoContabile strutturaAmministrativoContabileAggiuntivo) {
//		this.strutturaAmministrativoContabileAggiuntivo = strutturaAmministrativoContabileAggiuntivo;
//	}
//
//	/**
//	 * Sets the tipo atto aggiuntivo.
//	 *
//	 * @param tipoAttoAggiuntivo the new tipo atto aggiuntivo
//	 */
//	public void setTipoAttoAggiuntivo(TipoAtto tipoAttoAggiuntivo) {
//		this.tipoAttoAggiuntivo = tipoAttoAggiuntivo;
//	}
//
//	/**
//	 * Sets the atto amministrativo aggiuntivo old.
//	 *
//	 * @param attoAmministrativoAggiuntivoOld the new atto amministrativo aggiuntivo old
//	 */
//	public void setAttoAmministrativoAggiuntivoOld(AttoAmministrativo attoAmministrativoAggiuntivoOld) {
//		this.attoAmministrativoAggiuntivoOld = attoAmministrativoAggiuntivoOld;
//	}
//
	/**
	 * Sets the stringa provvedimento aggiuntivo.
	 *
	 * @param stringaProvvedimentoAggiuntivo the new stringa provvedimento aggiuntivo
	 */
	public void setStringaProvvedimentoAggiuntivo(String stringaProvvedimentoAggiuntivo) {
		this.stringaProvvedimentoAggiuntivo = stringaProvvedimentoAggiuntivo;
	}

	/**
	 * @return the informazioniProvvedimento
	 */
	public String getInformazioniProvvedimento() {
		if(getAttoAmministrativo() == null || getAttoAmministrativo().getAnno() == 0 || getAttoAmministrativo().getNumero() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		
		sb.append(": ")
			.append(getAttoAmministrativo().getAnno())
			.append(" / ")
			.append(getAttoAmministrativo().getNumero());
		
		return sb.toString();
	}

}
