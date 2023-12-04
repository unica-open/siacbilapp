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

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per l'aggiornamento dello storno UEB. Contiene una mappatura per il form di aggiornamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/09/2013
 * @deprecated da rimuovere con le UEB
 */
@Deprecated
public class AggiornaStornoUEBModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5243820310143467183L;
	
	// Per l'injezione dell'uid
	private StornoUEB storno;
	
	// Id per lo storno
	private Integer uidStorno;
	private Integer uidCapitoloSorgente;
	private Integer uidCapitoloDestinazione;
	private Integer uidProvvedimento;
	
	// Importi dello storno
	private BigDecimal competenzaStorno0;
	private BigDecimal competenzaStorno1;
	private BigDecimal competenzaStorno2;
	private BigDecimal cassaStorno0;
	
	// Tipi di capitolo
	private String tipoCapitoloSorgente;
	private String tipoCapitoloDestinazione;
	
	// Numeri dei capitoli
	private Integer numeroCapitoloSorgente;
	private Integer numeroArticoloSorgente;
	private Integer numeroUEBSorgente;
	private Integer numeroCapitoloDestinazione;
	private Integer numeroArticoloDestinazione;
	private Integer numeroUEBDestinazione;
	
	// Disponibilità
	private BigDecimal disponibilitaCapitoloSorgente;
	private BigDecimal disponibilitaCapitoloDestinazione;
	
	// Numero dello storno
	private Integer numeroStorno;
	
	// Stato dello storno
	private StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio;
	
	// Numero Provvedimento
	private Integer numeroProvvedimento;
	private Integer annoProvvedimento;
	private Integer uidTipoAtto;
	private String statoProvvedimento;
	
	// Lista provvedimento
	private AttoAmministrativo attoAmministrativo;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private TipoAtto tipoAtto;
	
	/** Costruttore vuoto di default */
	public AggiornaStornoUEBModel() {
		super();
		setTitolo("Aggiorna storni UEB");
	}
	
	/* Getter e Setter */
	
	/**
	 * @return the uidStorno
	 */
	public Integer getUidStorno() {
		return uidStorno;
	}

	/**
	 * @return the storno
	 */
	public StornoUEB getStorno() {
		return storno;
	}

	/**
	 * @param storno the storno to set
	 */
	public void setStorno(StornoUEB storno) {
		this.storno = storno;
	}

	/**
	 * @param uidStorno the uidStorno to set
	 */
	public void setUidStorno(Integer uidStorno) {
		this.uidStorno = uidStorno;
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
	 * @return the competenzaStorno0
	 */
	public BigDecimal getCompetenzaStorno0() {
		return competenzaStorno0;
	}

	/**
	 * @param competenzaStorno0 the competenzaStorno0 to set
	 */
	public void setCompetenzaStorno0(BigDecimal competenzaStorno0) {
		this.competenzaStorno0 = competenzaStorno0;
	}

	/**
	 * @return the competenzaStorno1
	 */
	public BigDecimal getCompetenzaStorno1() {
		return competenzaStorno1;
	}

	/**
	 * @param competenzaStorno1 the competenzaStorno1 to set
	 */
	public void setCompetenzaStorno1(BigDecimal competenzaStorno1) {
		this.competenzaStorno1 = competenzaStorno1;
	}

	/**
	 * @return the competenzaStorno2
	 */
	public BigDecimal getCompetenzaStorno2() {
		return competenzaStorno2;
	}

	/**
	 * @param competenzaStorno2 the competenzaStorno2 to set
	 */
	public void setCompetenzaStorno2(BigDecimal competenzaStorno2) {
		this.competenzaStorno2 = competenzaStorno2;
	}

	/**
	 * @return the cassaStorno0
	 */
	public BigDecimal getCassaStorno0() {
		return cassaStorno0;
	}

	/**
	 * @param cassaStorno0 the cassaStorno0 to set
	 */
	public void setCassaStorno0(BigDecimal cassaStorno0) {
		this.cassaStorno0 = cassaStorno0;
	}

	/**
	 * @return the tipoCapitoloSorgente
	 */
	public String getTipoCapitoloSorgente() {
		return tipoCapitoloSorgente;
	}

	/**
	 * @param tipoCapitoloSorgente the tipoCapitoloSorgente to set
	 */
	public void setTipoCapitoloSorgente(String tipoCapitoloSorgente) {
		this.tipoCapitoloSorgente = tipoCapitoloSorgente;
	}

	/**
	 * @return the tipoCapitoloDestinazione
	 */
	public String getTipoCapitoloDestinazione() {
		return tipoCapitoloDestinazione;
	}

	/**
	 * @param tipoCapitoloDestinazione the tipoCapitoloDestinazione to set
	 */
	public void setTipoCapitoloDestinazione(String tipoCapitoloDestinazione) {
		this.tipoCapitoloDestinazione = tipoCapitoloDestinazione;
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
	 * @return the disponibilitaCapitoloSorgente
	 */
	public BigDecimal getDisponibilitaCapitoloSorgente() {
		return disponibilitaCapitoloSorgente;
	}

	/**
	 * @param disponibilitaCapitoloSorgente the disponibilitaCapitoloSorgente to set
	 */
	public void setDisponibilitaCapitoloSorgente(
			BigDecimal disponibilitaCapitoloSorgente) {
		this.disponibilitaCapitoloSorgente = disponibilitaCapitoloSorgente;
	}

	/**
	 * @return the disponibilitaCapitoloDestinazione
	 */
	public BigDecimal getDisponibilitaCapitoloDestinazione() {
		return disponibilitaCapitoloDestinazione;
	}

	/**
	 * @param disponibilitaCapitoloDestinazione the disponibilitaCapitoloDestinazione to set
	 */
	public void setDisponibilitaCapitoloDestinazione(
			BigDecimal disponibilitaCapitoloDestinazione) {
		this.disponibilitaCapitoloDestinazione = disponibilitaCapitoloDestinazione;
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
	 * Restituisce l'uid dello storno da aggiornare.
	 * 
	 * @return l'uid dello storno
	 */
	public int getUidDaAggiornare() {
		return storno.getUid();
	}
	
	/**
	 * Imposta l'uid dello storno da aggiornare.
	 * 
	 * @param uidDaAggiornare l'uid dello storno
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		if(storno == null) {
			storno = new StornoUEB();
		}
		storno.setUid(uidDaAggiornare);
	}
	
	/**
	 * @return the statoOperativoVariazioneBilancio
	 */
	public StatoOperativoVariazioneBilancio getStatoOperativoVariazioneDiBilancio() {
		return statoOperativoVariazioneBilancio;
	}

	/**
	 * @param statoOperativoVariazioneBilancio the statoOperativoVariazioneBilancio to set
	 */
	public void setStatoOperativoVariazioneDiBilancio(
			StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio) {
		this.statoOperativoVariazioneBilancio = statoOperativoVariazioneBilancio;
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
	 * @return the uidTipoAtto
	 */
	public Integer getUidTipoAtto() {
		return uidTipoAtto;
	}

	/**
	 * @param uidTipoAtto the uidTipoAtto to set
	 */
	public void setUidTipoAtto(Integer uidTipoAtto) {
		this.uidTipoAtto = uidTipoAtto;
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
	 * @return the stringaProvvedimento
	 */
	public String getStringaProvvedimento(){
		StringBuilder sb = new StringBuilder();
		sb.append("provvedimento");
		if(attoAmministrativo!= null){
			sb.append(": ")
			.append(attoAmministrativo.getAnno())
			.append(" / ")
			.append(attoAmministrativo.getNumero());
		}
		return sb.toString();
	}
	
	/* Override di equals, hashCode, toString */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AggiornaStornoUEBModel)) {
			return false;
		}
		AggiornaStornoUEBModel other = (AggiornaStornoUEBModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(uidStorno, other.uidStorno)
			.append(uidCapitoloSorgente, other.uidCapitoloSorgente)
			.append(uidCapitoloDestinazione, other.uidCapitoloDestinazione)
			.append(uidProvvedimento, other.uidProvvedimento)
			.append(competenzaStorno0, other.competenzaStorno0)
			.append(competenzaStorno1, other.competenzaStorno1)
			.append(competenzaStorno2, other.competenzaStorno2)
			.append(cassaStorno0, other.cassaStorno0)
			.append(numeroStorno, other.numeroStorno)
			.append(numeroProvvedimento, other.numeroProvvedimento);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(uidStorno)
			.append(uidCapitoloSorgente)
			.append(uidCapitoloDestinazione)
			.append(uidProvvedimento)
			.append(competenzaStorno0)
			.append(competenzaStorno1)
			.append(competenzaStorno2)
			.append(cassaStorno0)
			.append(numeroStorno)
			.append(numeroProvvedimento);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder
			.append("uid storno", uidStorno)
			.append("uid capitolo sorgente", uidCapitoloSorgente)
			.append("uid capitolo destinazione", uidCapitoloDestinazione)
			.append("uid provvedimento", uidProvvedimento)
			.append("competenza anno " + (getAnnoEsercizioInt()), competenzaStorno0)
			.append("competenza anno " + (getAnnoEsercizioInt() + 1), competenzaStorno1)
			.append("competenza anno " + (getAnnoEsercizioInt() + 2), competenzaStorno2)
			.append("cassa anno " + (getAnnoEsercizioInt()), cassaStorno0)
			.append("numero storno", numeroStorno)
			.append("numero provvedimento", numeroProvvedimento);
		return toStringBuilder.toString();
	}
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaStornoUEB} a partire dal model.
	 * 
	 * @param stornoEntrata definisce se lo storno UEB sia di entrata o meno
	 * 
	 * @return la request creata
	 */
	public AggiornaStornoUEB creaRequestAggiornaStornoUEB(boolean stornoEntrata) {
		AggiornaStornoUEB request = new AggiornaStornoUEB();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setStornoUEB(creaStornoUEB(stornoEntrata));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = new RicercaProvvedimento();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Creo l'utility per la ricerca per uid
		RicercaAtti utility = new RicercaAtti();
		utility.setUid(storno.getAttoAmministrativo().getUid());
		
		request.setRicercaAtti(utility);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link CalcoloDisponibilitaDiUnCapitolo} a partire dal model.
	 * 
	 * @param capitolo il capitolo la cui disponibilit&agrave; &eacute; da ottenere 
	 * 
	 * @return la request creata
	 */
	public CalcoloDisponibilitaDiUnCapitolo creaRequestCalcoloDisponibilitaDiUnCapitolo(Capitolo<?, ?> capitolo) {
		CalcoloDisponibilitaDiUnCapitolo request = new CalcoloDisponibilitaDiUnCapitolo();
		
		request.setAnnoCapitolo(capitolo.getAnnoCapitolo());
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setFase(getBilancio().getFaseEStatoAttualeBilancio());
		request.setNumroCapitolo(capitolo.getNumeroCapitolo());
		request.setRichiedente(getRichiedente());
		request.setTipoDisponibilitaRichiesta("");
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Metodo di utilit&agrave; per la creazione di uno Storno UEB di Entrata.
	 * 
	 * @param stornoEntrata definisce se lo storno UEB sia di entrata o meno
	 * 
	 * @return la utility creata
	 */
	private StornoUEB creaStornoUEB(boolean stornoEntrata) {
		StornoUEB stornoUEB = new StornoUEB();
		
		stornoUEB.setNumero(numeroStorno);
		stornoUEB.setUid(uidStorno);
		
		// Atto Amministrativo (Provvedimento)
		AttoAmministrativo attoAmministrativoDaInjettare = new AttoAmministrativo();
		attoAmministrativoDaInjettare.setUid(uidProvvedimento);
		stornoUEB.setAttoAmministrativo(attoAmministrativoDaInjettare);
		
		// Importi dello storno
		// Anno di esercizio
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo0 = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo0.setAnnoCompetenza(getAnnoEsercizioInt());
		dettaglioVariazioneImportoCapitolo0.setStanziamento(competenzaStorno0);
		dettaglioVariazioneImportoCapitolo0.setStanziamentoCassa(cassaStorno0);
		
		// Anno di esercizio + 1
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo1 = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo1.setAnnoCompetenza(getAnnoEsercizioInt() + 1);
		dettaglioVariazioneImportoCapitolo1.setStanziamento(competenzaStorno1);
		
		
		// Anno di esercizio + 2
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo2 = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo2.setAnnoCompetenza(getAnnoEsercizioInt() + 2);
		dettaglioVariazioneImportoCapitolo2.setStanziamento(competenzaStorno2);
		
		// Lista degli importi
		List<DettaglioVariazioneImportoCapitolo> listaDettaglioVariazioneCapitolo = 
				new ArrayList<DettaglioVariazioneImportoCapitolo>();
		
		listaDettaglioVariazioneCapitolo.add(dettaglioVariazioneImportoCapitolo0);
		listaDettaglioVariazioneCapitolo.add(dettaglioVariazioneImportoCapitolo1);
		listaDettaglioVariazioneCapitolo.add(dettaglioVariazioneImportoCapitolo2);
		
		stornoUEB.setListaDettaglioVariazioneImporto(listaDettaglioVariazioneCapitolo);
		
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
		
		stornoUEB.setCapitoloSorgente(sorgente);
		stornoUEB.setCapitoloDestinazione(destinazione);
		
		stornoUEB.setStatoOperativoVariazioneDiBilancio(statoOperativoVariazioneBilancio);
		stornoUEB.setDescrizione("");
		
		//Nuovo (rifacimento variazioni importi 06/2016)
		stornoUEB.setApplicazioneVariazione(ApplicazioneVariazione.GESTIONE);
		
		return stornoUEB;
	}
	
	/**
	 * Imposta i dati nel model a partire da quelli ottenuti dalla sessione.
	 * 
	 * @param disponibilitaVariareSorgente     la disponibilit&agrave; a variare del capitolo sorgente
	 * @param disponibilitaVariareDestinazione la disponibilit&agrave; a variare del capitolo di destinazione
	 */
	public void impostaDatiDaSessione(BigDecimal disponibilitaVariareSorgente, BigDecimal disponibilitaVariareDestinazione) {
		Capitolo<?, ?> sorgente = storno.getCapitoloSorgente();
		Capitolo<?, ?> destinazione = storno.getCapitoloDestinazione();
		
		// Impostazione degli uid
		uidStorno = storno.getUid();
		uidCapitoloSorgente = sorgente.getUid();
		uidCapitoloDestinazione = destinazione.getUid();
		uidProvvedimento = attoAmministrativo.getUid();
		
		// Tipi di capitolo
		tipoCapitoloSorgente = (sorgente.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE ? "E" : "S");
		tipoCapitoloDestinazione = (destinazione.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE ? "E" : "S");
		
		// Numeri
		numeroCapitoloSorgente = sorgente.getNumeroCapitolo();
		numeroArticoloSorgente = sorgente.getNumeroArticolo();
		numeroUEBSorgente = sorgente.getNumeroUEB();
		numeroCapitoloDestinazione = destinazione.getNumeroCapitolo();
		numeroArticoloDestinazione = destinazione.getNumeroArticolo();
		numeroUEBDestinazione = destinazione.getNumeroUEB();
		numeroProvvedimento = attoAmministrativo.getNumero();
		numeroStorno = storno.getNumero();
		
		// Disponibilità a variare
		disponibilitaCapitoloSorgente = disponibilitaVariareSorgente;
		disponibilitaCapitoloDestinazione = disponibilitaVariareDestinazione;
		
		// Anno provvedimento
		annoProvvedimento = attoAmministrativo.getAnno();
		
		// Stato operativo variazione di getBilancio()
		statoOperativoVariazioneBilancio = storno.getStatoOperativoVariazioneDiBilancio();
		
		Integer anno = getAnnoEsercizioInt();
		
		DettaglioVariazioneImportoCapitolo	objCassaStorno0	= ComparatorUtils.searchByAnnoDettaglio(storno.getListaDettaglioVariazioneImporto(), anno);
		cassaStorno0 =  objCassaStorno0!=null ? objCassaStorno0.getStanziamentoCassa() : null;
		competenzaStorno0 = objCassaStorno0 != null ? objCassaStorno0.getStanziamento() : null;
		
		DettaglioVariazioneImportoCapitolo	objCassaStorno1	= ComparatorUtils.searchByAnnoDettaglio(storno.getListaDettaglioVariazioneImporto(), anno + 1);
		competenzaStorno1 = objCassaStorno1 != null ? objCassaStorno1.getStanziamento() : null;
		
		DettaglioVariazioneImportoCapitolo	objCassaStorno2	= ComparatorUtils.searchByAnnoDettaglio(storno.getListaDettaglioVariazioneImporto(), anno + 2);
		competenzaStorno2 = objCassaStorno2 != null ? objCassaStorno2.getStanziamento() : null;
		
		// Dati provvedimento
		strutturaAmministrativoContabile = attoAmministrativo.getStrutturaAmmContabile();
		tipoAtto = attoAmministrativo.getTipoAtto();
	}
	
	/**
	 * Imposta i dati dell'Atto Amministrativo nei var&icirc; campi correlati.
	 * 
	 * @param attoAmministrativo l'Atto da injettare
	 */
	public void impostaAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
		this.tipoAtto = attoAmministrativo.getTipoAtto();
		this.numeroProvvedimento = attoAmministrativo.getNumero();
		this.annoProvvedimento = attoAmministrativo.getAnno();
		this.uidTipoAtto = attoAmministrativo.getTipoAtto().getUid();
	}
	
}
