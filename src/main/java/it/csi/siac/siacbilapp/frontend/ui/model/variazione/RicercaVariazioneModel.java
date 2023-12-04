/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodifiche;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per la ricerca delle variazioni di getBilancio(). Contiene una mappatura dei campi del form di ricerca.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 - 29/10/2013
 * 
 */
public class RicercaVariazioneModel extends GenericBilancioModel {
	
	/** Per la sincronizzazione */
	private static final long serialVersionUID = 9136594481507133714L;
	
	// Le tre applicazioni della variazione
	private static final String APPLICAZIONE_PREVISIONE = "previsione";
	private static final String APPLICAZIONE_ASSESTAMENTO = "assestamento";
	private static final String APPLICAZIONE_GESTIONE = "gestione";
	
	/* Tipo variazione: radiobox importi / codifiche */
	private String tipologiaSceltaVariazione;
	private Integer numeroVariazione;
	
	private String applicazioneVariazione;
	private String descrizioneVariazione;
	private TipoVariazione tipoVariazione;
	private String tipoVariazioneHidden;
	private StatoOperativoVariazioneBilancio statoVariazione;
	
	private AttoAmministrativo attoAmministrativo;
	private Integer uidProvvedimento;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	/* Liste */
	private List<String> listaApplicazioneVariazione = new ArrayList<String>();
	private List<TipoVariazione> listaTipoVariazione = new ArrayList<TipoVariazione>();
	//SIAC-6177
	private List<ElementoStatoOperativoVariazione> listaStatoVariazione = new ArrayList<ElementoStatoOperativoVariazione>();
	
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	private Date dataAperturaProposta;
	private Date dataChiusuraProposta;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileDirezioneProponente;
	private List<StrutturaAmministrativoContabile> listaDirezioni = new ArrayList<StrutturaAmministrativoContabile>();
	
	/**
	 * @return the listaDirezioni
	 */
	public List<StrutturaAmministrativoContabile> getListaDirezioni() {
		return listaDirezioni;
	}

	/**
	 * @param listaDirezioni the listaDirezioni to set
	 */
	public void setListaDirezioni(List<StrutturaAmministrativoContabile> listaDirezioni) {
		this.listaDirezioni = listaDirezioni;
	}

	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileDirezioneProponente() {
		return strutturaAmministrativoContabileDirezioneProponente;
	}

	public void setStrutturaAmministrativoContabileDirezioneProponente(
			StrutturaAmministrativoContabile strutturaAmministrativoContabileDirezioneProponente) {
		this.strutturaAmministrativoContabileDirezioneProponente = strutturaAmministrativoContabileDirezioneProponente;
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

	/** Costruttore vuoto di default */
	public RicercaVariazioneModel() {
		super();
		setTitolo("Ricerca variazioni");
	}

	/**
	 * @return the tipologiaSceltaVariazione
	 */
	public String getTipologiaSceltaVariazione() {
		return tipologiaSceltaVariazione;
	}



	/**
	 * @param tipologiaSceltaVariazione the tipologiaSceltaVariazione to set
	 */
	public void setTipologiaSceltaVariazione(String tipologiaSceltaVariazione) {
		this.tipologiaSceltaVariazione = tipologiaSceltaVariazione;
	}



	/**
	 * @return the uidVariazione
	 */
	public Integer getNumeroVariazione() {
		return numeroVariazione;
	}



	/**
	 * @param numeroVariazione the numeroVariazione to set
	 */
	public void setNumeroVariazione(Integer numeroVariazione) {
		this.numeroVariazione = numeroVariazione;
	}



	/**
	 * @return the applicazioneVariazione
	 */
	public String getApplicazioneVariazione() {
		return applicazioneVariazione;
	}



	/**
	 * @param applicazioneVariazione the applicazioneVariazione to set
	 */
	public void setApplicazioneVariazione(String applicazioneVariazione) {
		this.applicazioneVariazione = applicazioneVariazione;
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
	 * @return the statoVariazione
	 */
	public StatoOperativoVariazioneBilancio getStatoVariazione() {
		return statoVariazione;
	}



	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(StatoOperativoVariazioneBilancio statoVariazione) {
		this.statoVariazione = statoVariazione;
	}



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



	/**
	 * @return the listaApplicazioneVariazione
	 */
	public List<String> getListaApplicazioneVariazione() {
		return listaApplicazioneVariazione;
	}



	/**
	 * @param listaApplicazioneVariazione the listaApplicazioneVariazione to set
	 */
	public void setListaApplicazioneVariazione(List<String> listaApplicazioneVariazione) {
		this.listaApplicazioneVariazione = listaApplicazioneVariazione;
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
	 * @return the listaStatoVariazione
	 */
	public List<ElementoStatoOperativoVariazione> getListaStatoVariazione() {
		return listaStatoVariazione;
	}



	/**
	 * @param listaStatoVariazione the listaStatoVariazione to set
	 */
	public void setListaStatoVariazione(List<ElementoStatoOperativoVariazione> listaStatoVariazione) {
		this.listaStatoVariazione = listaStatoVariazione;
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
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the tipoVariazioneHidden
	 */
	public String getTipoVariazioneHidden() {
		return tipoVariazioneHidden;
	}

	/**
	 * @param tipoVariazioneHidden the tipoVariazioneHidden to set
	 */
	public void setTipoVariazioneHidden(String tipoVariazioneHidden) {
		this.tipoVariazioneHidden = tipoVariazioneHidden;
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
	
	/**
	 * @return the stringaProvvedimento
	 */
	public String getStringaProvvedimento(){
		return "provvedimento";
	}

	/* Override di equals, hashCode, toString */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RicercaVariazioneModel)) {
			return false;
		}
		RicercaVariazioneModel other = (RicercaVariazioneModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(tipologiaSceltaVariazione, other.tipologiaSceltaVariazione)
			.append(numeroVariazione, other.numeroVariazione)
			.append(applicazioneVariazione, other.applicazioneVariazione)
			.append(descrizioneVariazione, other.descrizioneVariazione)
			.append(tipoVariazione, other.tipoVariazione)
			.append(statoVariazione, other.statoVariazione)
			.append(uidProvvedimento, other.uidProvvedimento);
		return equalsBuilder.isEquals();
	}
	

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(tipologiaSceltaVariazione)
			.append(numeroVariazione)
			.append(applicazioneVariazione)
			.append(descrizioneVariazione)
			.append(tipoVariazione)
			.append(statoVariazione)
			.append(uidProvvedimento);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("tipo variazione", tipologiaSceltaVariazione)
			.append("numero variazione", numeroVariazione)
			.append("uid applicazione variazione", applicazioneVariazione)
			.append("descrizione variazione", descrizioneVariazione)
			.append("uid tipo variazione", tipoVariazione)
			.append("uid stato variazione", statoVariazione)
			.append("uid provvedimento", uidProvvedimento);
		return toStringBuilder.toString();
	}
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di {@link RicercaVariazioneBilancio} a partire dal Model.
	 * @param limitaRicerca 
	 * 
	 * @return la request creata
	 */
	public RicercaVariazioneBilancio creaRequestRicercaVariazioneBilancio(boolean limitaRicerca) {
		RicercaVariazioneBilancio request = new RicercaVariazioneBilancio();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setVariazioneImportoCapitolo(creaVariazioneImporto());
		
		List<TipoCapitolo> tipiCapitolo = new ArrayList<TipoCapitolo>();
		if(APPLICAZIONE_GESTIONE.equalsIgnoreCase(applicazioneVariazione) || APPLICAZIONE_ASSESTAMENTO.equalsIgnoreCase(applicazioneVariazione)){
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
			request.setTipiCapitolo(tipiCapitolo);
		} else if(APPLICAZIONE_PREVISIONE.equalsIgnoreCase(applicazioneVariazione)){
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
			request.setTipiCapitolo(tipiCapitolo);
		}
		
		if(uidProvvedimento!=null && uidProvvedimento.intValue() != 0){
			AttoAmministrativo attoAmministrativoDaInjettare = new AttoAmministrativo();
			attoAmministrativoDaInjettare.setUid(uidProvvedimento);
			request.setAttoAmministrativo(attoAmministrativoDaInjettare);
		}
		//SIAC-8771-REGP
		request.setLimitaRisultatiDefinitiveODecentrate(limitaRicerca);
			
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	/**
	 * Crea una request per il servizio di {@link RicercaVariazioneCodifiche} a partire dal Model.
	 * 
	 * @return la request creata
	 */
	public RicercaVariazioneCodifiche creaRequestRicercaVariazioneCodifiche() {
		RicercaVariazioneCodifiche request = new RicercaVariazioneCodifiche();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setVariazioneCodificaCapitolo(creaVariazioneCodifica());
		
		List<TipoCapitolo> tipiCapitolo = new ArrayList<TipoCapitolo>();
		if(APPLICAZIONE_GESTIONE.equalsIgnoreCase(applicazioneVariazione) || APPLICAZIONE_ASSESTAMENTO.equalsIgnoreCase(applicazioneVariazione)){
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
			request.setTipiCapitolo(tipiCapitolo);
		} else if(APPLICAZIONE_PREVISIONE.equalsIgnoreCase(applicazioneVariazione)){
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
			tipiCapitolo.add(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
			request.setTipiCapitolo(tipiCapitolo);
		}
			
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/* Utility */
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una VariazioneDiBilancio a partire dal model.
	 * 
	 * @return l'utility creata
	 */
	private VariazioneImportoCapitolo creaVariazioneImporto() {
		VariazioneImportoCapitolo utility = new VariazioneImportoCapitolo();
		
		utility.setBilancio(getBilancio());
		utility.setEnte(getEnte());
		
		// Numero della variazione
		if(numeroVariazione != null && numeroVariazione.intValue() != 0) {
			utility.setNumero(numeroVariazione);
		}
		if(applicazioneVariazione!=null){
			// TODO: applicazione?
//			utility.setApplicazione(applicazioneVariazione);
		}
		if(descrizioneVariazione!=null && !descrizioneVariazione.isEmpty()){
			utility.setDescrizione(descrizioneVariazione);
		}
		if(tipoVariazione!=null){
			utility.setTipoVariazione(tipoVariazione);
		}
		if(statoVariazione!=null){
			utility.setStatoOperativoVariazioneDiBilancio(statoVariazione);
		}
		if(dataAperturaProposta!=null){
			utility.setDataAperturaProposta(dataAperturaProposta);
		}
		if(dataChiusuraProposta!=null){
			utility.setDataChiusuraProposta(dataChiusuraProposta);
		}
		if(strutturaAmministrativoContabileDirezioneProponente!=null && strutturaAmministrativoContabileDirezioneProponente.getUid() != 0){
			utility.setDirezioneProponente(strutturaAmministrativoContabileDirezioneProponente);
		}
		
		return utility;
	}
	
	private VariazioneCodificaCapitolo creaVariazioneCodifica() {
		VariazioneCodificaCapitolo utility = new VariazioneCodificaCapitolo();
		
		utility.setBilancio(getBilancio());
		utility.setEnte(getEnte());
		
		// Numero della variazione
		if(numeroVariazione != null && numeroVariazione.intValue() != 0) {
			utility.setNumero(numeroVariazione);
		}
		if(applicazioneVariazione!=null){
			// TODO: applicazione?
//			utility.setApplicazione(applicazioneVariazione);
		}
		if(descrizioneVariazione!=null && !descrizioneVariazione.isEmpty()){
			utility.setDescrizione(descrizioneVariazione);
		}
		if(tipoVariazione!=null){
			utility.setTipoVariazione(tipoVariazione);
		}
		if(statoVariazione!=null){
			utility.setStatoOperativoVariazioneDiBilancio(statoVariazione);
		}
		if(dataAperturaProposta!=null){
			utility.setDataAperturaProposta(dataAperturaProposta);
		}
		if(dataChiusuraProposta!=null){
			utility.setDataChiusuraProposta(dataChiusuraProposta);
		}
		if(strutturaAmministrativoContabileDirezioneProponente!=null  && strutturaAmministrativoContabileDirezioneProponente.getUid() != 0){
			utility.setDirezioneProponente(strutturaAmministrativoContabileDirezioneProponente);
		}
		
		if(uidProvvedimento!=null && uidProvvedimento.intValue() != 0){
			AttoAmministrativo attoAmministrativoDaInjettare = new AttoAmministrativo();
			attoAmministrativoDaInjettare.setUid(uidProvvedimento);
			utility.setAttoAmministrativo(attoAmministrativoDaInjettare);
		}
		return utility;
	}
	
	
}
