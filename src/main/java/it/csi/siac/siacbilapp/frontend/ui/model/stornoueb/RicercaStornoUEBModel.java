/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.stornoueb;

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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEB;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Classe di model per la ricerca dello storno UEB. Contiene una mappatura dei campi del form di ricerca.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/09/2013
 * @deprecated da eliminare con le UEB
 */
@Deprecated
public class RicercaStornoUEBModel extends GenericBilancioModel {
	
	/** Per la sincronizzazione */
	private static final long serialVersionUID = 9136594481507133714L;
	
	private Integer numeroStorno;
	/** Dirime i problemi sul capitolo di Entrata o di Uscita */
	private TipoCapitolo tipoCapitolo;
	
	private Integer uidCapitoloSorgente;
	private Integer uidCapitoloDestinazione;
	private Integer uidProvvedimento;
	
	// Per il ripopolamento
	private Integer numeroCapitoloSorgente;
	private Integer numeroArticoloSorgente;
	private Integer numeroUEBSorgente;
	private Integer numeroCapitoloDestinazione;
	private Integer numeroArticoloDestinazione;
	private Integer numeroUEBDestinazione;
	
	private StatoOperativoElementoDiBilancio statoOperativoSorgente;
	private StatoOperativoElementoDiBilancio statoOperativoDestinazione;
	
	private AttoAmministrativo attoAmministrativo;
	
	/* Liste */
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<StatoOperativoElementoDiBilancio> listaStatoOperativoElementoDiBilancio = new ArrayList<StatoOperativoElementoDiBilancio>();
	
	/** Costruttore vuoto di default */
	public RicercaStornoUEBModel() {
		super();
		setTitolo("Ricerca storni UEB");
	}

	/* Getter e Setter */
	
	/**
	 * @return the numeroStorno
	 */
	public Integer getNumeroStorno() {
		return numeroStorno;
	}

	/**
	 * @param numeroStorno the numeroStorno to set
	 */
	public void setNumeroStorno(Integer numeroStorno) {
		this.numeroStorno = numeroStorno;
	}

	/**
	 * @return the tipoCapitolo
	 */
	public TipoCapitolo getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(TipoCapitolo tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the uidCapitoloSorgente
	 */
	public Integer getUidCapitoloSorgente() {
		return uidCapitoloSorgente;
	}

	/**
	 * @param uidCapitoloSorgente the uidCapitoloSorgente to set
	 */
	public void setUidCapitoloSorgente(Integer uidCapitoloSorgente) {
		this.uidCapitoloSorgente = uidCapitoloSorgente;
	}

	/**
	 * @return the uidCapitoloDestinazione
	 */
	public Integer getUidCapitoloDestinazione() {
		return uidCapitoloDestinazione;
	}

	/**
	 * @param uidCapitoloDestinazione the uidCapitoloDestinazione to set
	 */
	public void setUidCapitoloDestinazione(Integer uidCapitoloDestinazione) {
		this.uidCapitoloDestinazione = uidCapitoloDestinazione;
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
	 * @return the numeroCapitoloSorgente
	 */
	public Integer getNumeroCapitoloSorgente() {
		return numeroCapitoloSorgente;
	}

	/**
	 * @param numeroCapitoloSorgente the numeroCapitoloSorgente to set
	 */
	public void setNumeroCapitoloSorgente(Integer numeroCapitoloSorgente) {
		this.numeroCapitoloSorgente = numeroCapitoloSorgente;
	}

	/**
	 * @return the numeroArticoloSorgente
	 */
	public Integer getNumeroArticoloSorgente() {
		return numeroArticoloSorgente;
	}

	/**
	 * @param numeroArticoloSorgente the numeroArticoloSorgente to set
	 */
	public void setNumeroArticoloSorgente(Integer numeroArticoloSorgente) {
		this.numeroArticoloSorgente = numeroArticoloSorgente;
	}

	/**
	 * @return the numeroUEBSorgente
	 */
	public Integer getNumeroUEBSorgente() {
		return numeroUEBSorgente;
	}

	/**
	 * @param numeroUEBSorgente the numeroUEBSorgente to set
	 */
	public void setNumeroUEBSorgente(Integer numeroUEBSorgente) {
		this.numeroUEBSorgente = numeroUEBSorgente;
	}

	/**
	 * @return the numeroCapitoloDestinazione
	 */
	public Integer getNumeroCapitoloDestinazione() {
		return numeroCapitoloDestinazione;
	}

	/**
	 * @param numeroCapitoloDestinazione the numeroCapitoloDestinazione to set
	 */
	public void setNumeroCapitoloDestinazione(Integer numeroCapitoloDestinazione) {
		this.numeroCapitoloDestinazione = numeroCapitoloDestinazione;
	}

	/**
	 * @return the numeroArticoloDestinazione
	 */
	public Integer getNumeroArticoloDestinazione() {
		return numeroArticoloDestinazione;
	}

	/**
	 * @param numeroArticoloDestinazione the numeroArticoloDestinazione to set
	 */
	public void setNumeroArticoloDestinazione(Integer numeroArticoloDestinazione) {
		this.numeroArticoloDestinazione = numeroArticoloDestinazione;
	}

	/**
	 * @return the numeroUEBDestinazione
	 */
	public Integer getNumeroUEBDestinazione() {
		return numeroUEBDestinazione;
	}

	/**
	 * @param numeroUEBDestinazione the numeroUEBDestinazione to set
	 */
	public void setNumeroUEBDestinazione(Integer numeroUEBDestinazione) {
		this.numeroUEBDestinazione = numeroUEBDestinazione;
	}

	/**
	 * @return the statoOperativoSorgente
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativoSorgente() {
		return statoOperativoSorgente;
	}

	/**
	 * @param statoOperativoSorgente the statoOperativoSorgente to set
	 */
	public void setStatoOperativoSorgente(
			StatoOperativoElementoDiBilancio statoOperativoSorgente) {
		this.statoOperativoSorgente = statoOperativoSorgente;
	}

	/**
	 * @return the statoOperativoDestinazione
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativoDestinazione() {
		return statoOperativoDestinazione;
	}

	/**
	 * @param statoOperativoDestinazione the statoOperativoDestinazione to set
	 */
	public void setStatoOperativoDestinazione(
			StatoOperativoElementoDiBilancio statoOperativoDestinazione) {
		this.statoOperativoDestinazione = statoOperativoDestinazione;
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
	 * @return the listaStatoOperativoElementoDiBilancio
	 */
	public List<StatoOperativoElementoDiBilancio> getListaStatoOperativoElementoDiBilancio() {
		return listaStatoOperativoElementoDiBilancio;
	}

	/**
	 * @param listaStatoOperativoElementoDiBilancio the listaStatoOperativoElementoDiBilancio to set
	 */
	public void setListaStatoOperativoElementoDiBilancio(
			List<StatoOperativoElementoDiBilancio> listaStatoOperativoElementoDiBilancio) {
		this.listaStatoOperativoElementoDiBilancio = listaStatoOperativoElementoDiBilancio;
	}
	
	
	/**
	 * @return the stringaProvvedimento
	 */
	public String getStringaProvvedimento(){
		//per la gestione centralizzata della ricerca provvedimento
		return "provvedimento";
	}
	
	/* Override di equals, hashCode, toString */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RicercaStornoUEBModel)) {
			return false;
		}
		RicercaStornoUEBModel other = (RicercaStornoUEBModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(numeroStorno, other.numeroStorno)
			.append(tipoCapitolo, other.tipoCapitolo)
			.append(uidCapitoloSorgente, other.uidCapitoloSorgente)
			.append(uidCapitoloDestinazione, other.uidCapitoloDestinazione)
			.append(uidProvvedimento, other.uidProvvedimento);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(numeroStorno)
			.append(tipoCapitolo)
			.append(uidCapitoloSorgente)
			.append(uidCapitoloDestinazione)
			.append(uidProvvedimento);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("numero storno", numeroStorno)
			.append("tipo capitolo", tipoCapitolo)
			.append("uid capitolo sorgente", uidCapitoloSorgente)
			.append("uid capitolo destinazione", uidCapitoloDestinazione)
			.append("uid provvedimento", uidProvvedimento);
		return toStringBuilder.toString();
	}
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di {@link RicercaStornoUEB} a partire dal Model.
	 * 
	 * @return la request creata
	 */
	public RicercaStornoUEB creaRequestRicercaStornoUEB() {
		RicercaStornoUEB request = new RicercaStornoUEB();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setStornoUEB(creaStornoUEB());
		request.setTipoCapitolo(tipoCapitolo);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/* Utility */
	
	/**
	 * Metodo di utilit&agrave; per la creazione di uno storno UEB a partire dal model.
	 * 
	 * @return l'utility creata
	 */
	private StornoUEB creaStornoUEB() {
		StornoUEB utility = new StornoUEB();
		
		// Numero dello storno
		if(numeroStorno != null && numeroStorno.intValue() != 0) {
			utility.setNumero(numeroStorno);
		}
		
		// Definizione degli oggetti da eventualmente injettare
		Capitolo<?, ?> sorgente;
		Capitolo<?, ?> destinazione;
		AttoAmministrativo provvedimento = new AttoAmministrativo();
		
		if(tipoCapitolo == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE) {
			// Caso Entrata
			// Inizializzazione del capitolo sorgente
			sorgente = new CapitoloEntrataGestione();
			if(uidCapitoloSorgente != null && uidCapitoloSorgente.intValue() != 0) {
				sorgente.setUid(uidCapitoloSorgente);
				utility.setCapitoloSorgente(sorgente);
			}
			// Inizializzazione del capitolo destinazione
			destinazione = new CapitoloEntrataGestione();
			if(uidCapitoloDestinazione != null && uidCapitoloDestinazione.intValue() != 0) {
				destinazione.setUid(uidCapitoloDestinazione);
				utility.setCapitoloDestinazione(destinazione);
			}
		} else if(tipoCapitolo == TipoCapitolo.CAPITOLO_USCITA_GESTIONE) {
			// Caso Uscita
			// Inizializzazione del capitolo sorgente
			sorgente = new CapitoloUscitaGestione();
			if(uidCapitoloSorgente != null && uidCapitoloSorgente.intValue() != 0) {
				sorgente.setUid(uidCapitoloSorgente);
				utility.setCapitoloSorgente(sorgente);
			}
			// Inizializzazione del capitolo destinazione
			destinazione = new CapitoloUscitaGestione();
			if(uidCapitoloDestinazione != null && uidCapitoloDestinazione.intValue() != 0) {
				destinazione.setUid(uidCapitoloDestinazione);
				utility.setCapitoloDestinazione(destinazione);
			}
		}
		
		// Controllo del Provvedimento
		if(uidProvvedimento != null && uidProvvedimento.intValue() != 0) {
			provvedimento.setUid(uidProvvedimento);
			utility.setAttoAmministrativo(provvedimento);
		}
		
		return utility;
	}
	
	
}
