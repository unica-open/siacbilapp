/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.stornoueb;

import java.math.BigDecimal;
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
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per l'Inserimento dello Storno. Definisce il mapping per il form di inserimento.
 * 
 * @author Marchino Alessandro
 * @deprecated da rimuovere con le UEB
 */
public class InserisciStornoUEBModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5198478826157925789L;

	/** Dirime i problemi sul capitolo di Entrata o di Uscita */
	private String tipoCapitolo;
	
	// Dati capitolo
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private StatoOperativoElementoDiBilancio stato;
	
	private Integer numeroUEBSorgente;
	private Integer numeroUEBDestinazione;
	private Integer numeroProvvedimento;
	
	private BigDecimal disponibilitaSorgente;
	private BigDecimal disponibilitaDestinazione;
	
	// Dati provvedimento
	private AttoAmministrativo attoAmministrativo;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	// Importi
	private BigDecimal stanziamentoCompetenzaSorgente0 = BigDecimal.ZERO;
	private BigDecimal stanziamentoCassaSorgente0 = BigDecimal.ZERO;
	private BigDecimal stanziamentoCompetenzaSorgente1 = BigDecimal.ZERO;
	private BigDecimal stanziamentoCompetenzaSorgente2 = BigDecimal.ZERO;
	
	// Lista del Tipo Atto, per evitare il NullPointerException
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	// UID
	private Integer uidCapitoloSorgente;
	private Integer uidCapitoloDestinazione;
	private Integer uidProvvedimento;
	
	// Numero dello storno per il post-inserimento
	private Integer numeroStorno;
	
	// Dati extra
	private TipoAtto tipoAtto;
	private Integer annoProvvedimento;
	private String statoProvvedimento;
	
	/** Costruttore vuoto di default */
	public InserisciStornoUEBModel() {
		super();
		setTitolo("Inserimento storni UEB");
	}
	
	/* ==== Getter e Setter ====*/
	
	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}

	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}

	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	/**
	 * @return the stato
	 */
	public StatoOperativoElementoDiBilancio getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(StatoOperativoElementoDiBilancio stato) {
		this.stato = stato;
	}

	/**
	 * @return the disponibilitaSorgente
	 */
	public BigDecimal getDisponibilitaSorgente() {
		return disponibilitaSorgente;
	}

	/**
	 * @param disponibilitaSorgente the disponibilitaSorgente to set
	 */
	public void setDisponibilitaSorgente(BigDecimal disponibilitaSorgente) {
		this.disponibilitaSorgente = disponibilitaSorgente;
	}

	/**
	 * @return the disponibilitaDestinazione
	 */
	public BigDecimal getDisponibilitaDestinazione() {
		return disponibilitaDestinazione;
	}

	/**
	 * @param disponibilitaDestinazione the disponibilitaDestinazione to set
	 */
	public void setDisponibilitaDestinazione(BigDecimal disponibilitaDestinazione) {
		this.disponibilitaDestinazione = disponibilitaDestinazione;
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
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(
			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the stanziamentoCompetenzaSorgente0
	 */
	public BigDecimal getStanziamentoCompetenzaSorgente0() {
		return stanziamentoCompetenzaSorgente0;
	}

	/**
	 * @param stanziamentoCompetenzaSorgente0 the stanziamentoCompetenzaSorgente0 to set
	 */
	public void setStanziamentoCompetenzaSorgente0(
			BigDecimal stanziamentoCompetenzaSorgente0) {
		this.stanziamentoCompetenzaSorgente0 = stanziamentoCompetenzaSorgente0;
	}

	/**
	 * @return the stanziamentoCassaSorgente0
	 */
	public BigDecimal getStanziamentoCassaSorgente0() {
		return stanziamentoCassaSorgente0;
	}

	/**
	 * @param stanziamentoCassaSorgente0 the stanziamentoCassaSorgente0 to set
	 */
	public void setStanziamentoCassaSorgente0(BigDecimal stanziamentoCassaSorgente0) {
		this.stanziamentoCassaSorgente0 = stanziamentoCassaSorgente0;
	}

	/**
	 * @return the stanziamentoCompetenzaSorgente1
	 */
	public BigDecimal getStanziamentoCompetenzaSorgente1() {
		return stanziamentoCompetenzaSorgente1;
	}

	/**
	 * @param stanziamentoCompetenzaSorgente1 the stanziamentoCompetenzaSorgente1 to set
	 */
	public void setStanziamentoCompetenzaSorgente1(
			BigDecimal stanziamentoCompetenzaSorgente1) {
		this.stanziamentoCompetenzaSorgente1 = stanziamentoCompetenzaSorgente1;
	}

	/**
	 * @return the stanziamentoCompetenzaSorgente2
	 */
	public BigDecimal getStanziamentoCompetenzaSorgente2() {
		return stanziamentoCompetenzaSorgente2;
	}

	/**
	 * @param stanziamentoCompetenzaSorgente2 the stanziamentoCompetenzaSorgente2 to set
	 */
	public void setStanziamentoCompetenzaSorgente2(
			BigDecimal stanziamentoCompetenzaSorgente2) {
		this.stanziamentoCompetenzaSorgente2 = stanziamentoCompetenzaSorgente2;
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
	 * @return the numeroProvvedimento
	 */
	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	/**
	 * @param numeroProvvedimento the numeroProvvedimento to set
	 */
	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}
	
	/**
	 * @return the annoProvvedimento
	 */
	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}

	/**
	 * @param annoProvvedimento the annoProvvedimento to set
	 */
	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}
	
	/**
	 * @return the statoProvvedimento
	 */
	public String getStatoProvvedimento() {
		return statoProvvedimento;
	}

	/**
	 * @param statoProvvedimento the statoProvvedimento to set
	 */
	public void setStatoProvvedimento(String statoProvvedimento) {
		this.statoProvvedimento = statoProvvedimento;
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
		if (!(obj instanceof InserisciStornoUEBModel)) {
			return false;
		}
		InserisciStornoUEBModel other = (InserisciStornoUEBModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(tipoCapitolo, other.tipoCapitolo)
			.append(numeroCapitolo, other.numeroCapitolo)
			.append(numeroArticolo, other.numeroArticolo)
			.append(stato, other.stato)
			.append(stanziamentoCompetenzaSorgente0, other.stanziamentoCompetenzaSorgente0)
			.append(stanziamentoCassaSorgente0, other.stanziamentoCassaSorgente0)
			.append(stanziamentoCompetenzaSorgente1, other.stanziamentoCompetenzaSorgente1)
			.append(stanziamentoCompetenzaSorgente2, other.stanziamentoCompetenzaSorgente2)
			.append(uidCapitoloSorgente, other.uidCapitoloSorgente)
			.append(uidCapitoloDestinazione, other.uidCapitoloDestinazione)
			.append(uidProvvedimento, other.uidProvvedimento)
			.append(numeroStorno, other.numeroStorno);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(tipoCapitolo)
			.append(numeroCapitolo)
			.append(numeroArticolo)
			.append(stato)
			.append(stanziamentoCompetenzaSorgente0)
			.append(stanziamentoCassaSorgente0)
			.append(stanziamentoCompetenzaSorgente1)
			.append(stanziamentoCompetenzaSorgente2)
			.append(uidCapitoloSorgente)
			.append(uidCapitoloDestinazione)
			.append(uidProvvedimento)
			.append(numeroStorno);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("tipo capitolo", tipoCapitolo)
			.append("numero capitolo", numeroCapitolo)
			.append("numero articolo", numeroArticolo)
			.append("stato", stato)
			.append("competenza anno " + getAnnoEsercizioInt(), stanziamentoCompetenzaSorgente0)
			.append("cassa anno " + getAnnoEsercizioInt(), stanziamentoCassaSorgente0)
			.append("competenza anno " + (getAnnoEsercizioInt() + 1), stanziamentoCompetenzaSorgente1)
			.append("competenza anno " + (getAnnoEsercizioInt() + 2), stanziamentoCompetenzaSorgente2)
			.append("uid sorgente", uidCapitoloSorgente)
			.append("uid destinazione", uidCapitoloDestinazione)
			.append("uid provvedimento", uidProvvedimento)
			.append("numero storno", numeroStorno);
		return toStringBuilder.toString();
	}
	
	/* Request */
	
	/**
	 * Crea una request per il servizio di Inserisce Storno UEB.
	 * 
	 * @param stornoEntrata definisce se lo storno UEB sia di entrata o meno
	 * 
	 * @return la request creata
	 */
	public InserisceStornoUEB creaRequestInserisciStorno(boolean stornoEntrata) {
		InserisceStornoUEB request = new InserisceStornoUEB();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Modifica nei due casi di capitolo di entrata e di uscita
		request.setStornoUEB(creaStornoUEB(stornoEntrata));
		return request;
	}
	
	/* Utility */
	
	/**
	 * Metodo di utilit&agrave; per la creazione di uno Storno UEB di Entrata.
	 * 
	 * @param stornoEntrata definisce se lo storno UEB sia di entrata o meno
	 * 
	 * @return la utility creata
	 */
	private StornoUEB creaStornoUEB(boolean stornoEntrata) {
		StornoUEB storno = new StornoUEB();
		
		// Atto Amministrativo (Provvedimento)
		AttoAmministrativo attoAmministrativoDaInjettare = new AttoAmministrativo();
		attoAmministrativoDaInjettare.setUid(uidProvvedimento);
		storno.setAttoAmministrativo(attoAmministrativoDaInjettare);
		
		// Importi dello storno
		// Anno di esercizio
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo0 = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo0.setAnnoCompetenza(getAnnoEsercizioInt());
		dettaglioVariazioneImportoCapitolo0.setStanziamento(stanziamentoCompetenzaSorgente0);
		dettaglioVariazioneImportoCapitolo0.setStanziamentoCassa(stanziamentoCassaSorgente0);
		
		// Anno di esercizio + 1
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo1 = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo1.setAnnoCompetenza(getAnnoEsercizioInt() + 1);
		dettaglioVariazioneImportoCapitolo1.setStanziamento(stanziamentoCompetenzaSorgente1);
		
		// Anno di esercizio + 2
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo2 = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo2.setAnnoCompetenza(getAnnoEsercizioInt() + 2);
		dettaglioVariazioneImportoCapitolo2.setStanziamento(stanziamentoCompetenzaSorgente2);
		
		// Lista degli importi
		List<DettaglioVariazioneImportoCapitolo> listaDettaglioVariazioneCapitolo = 
				new ArrayList<DettaglioVariazioneImportoCapitolo>();
		
		listaDettaglioVariazioneCapitolo.add(dettaglioVariazioneImportoCapitolo0);
		listaDettaglioVariazioneCapitolo.add(dettaglioVariazioneImportoCapitolo1);
		listaDettaglioVariazioneCapitolo.add(dettaglioVariazioneImportoCapitolo2);
		
		storno.setListaDettaglioVariazioneImporto(listaDettaglioVariazioneCapitolo);
		
		// Capitolo sorgente
		Capitolo<?, ?> sorgente;
		Capitolo<?, ?> destinazione;
		
		// Controllo del tipo di capitolo per l'injezione
		if(stornoEntrata) {
			// Creazione dei capitoli di entrata
			sorgente = new CapitoloEntrataGestione();
			destinazione = new CapitoloEntrataGestione();
		} else {
			// Creazione dei capitoli di uscita
			sorgente = new CapitoloUscitaGestione();
			destinazione = new CapitoloUscitaGestione();
		}
		
		sorgente.setUid(uidCapitoloSorgente);
		destinazione.setUid(uidCapitoloDestinazione);
		
		storno.setCapitoloSorgente(sorgente);
		storno.setCapitoloDestinazione(destinazione);
		storno.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneBilancio.DEFINITIVA);
		storno.setDescrizione("");
		//Nuovo (rifacimento variazioni importi 06/2016)
		storno.setApplicazioneVariazione(ApplicazioneVariazione.GESTIONE);
		
		return storno;
	}
	
}
