/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.attodilegge;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.attodilegge.ElementoRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ric.RicercaAttiDiLeggeCapitolo;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;

/**
 * Classe di model per l'Associazione tra Atto di Legge e Capitolo. Contiene una mappatura dei dati del form.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/09/2013
 *
 */
public class AssociaAttoDiLeggeAlCapitoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -659850513295972173L;
	
	private AttoDiLegge attoDiLegge;
	private Capitolo<?, ?> capitolo;
	
	private String anno;
	private String numero;
	private Date dataInizioFinanziamento;
	private Date dataFineFinanziamento;
	private String gerarchia;
	private String descrizione;
	
	// 	Per la associazione all'Atto di Legge
	private Integer uidAttoDiLegge;

	// 	Per la associazione al Capitolo
	private Integer uidCapitolo;
	
	// 	Per la associazione al Capitolo
	private Integer uidBilancio;

	// 	Per la associazione all'Atto di Legge
	private Integer uidAttoDiLeggeCapitolo;
	
	// Oggetti definiti dal dataTable
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	private List<ElementoRelazioneAttoDiLeggeCapitolo> aaData = new ArrayList<ElementoRelazioneAttoDiLeggeCapitolo>();
	
	
	/** Costruttore vuoto di default */
	public AssociaAttoDiLeggeAlCapitoloModel() {
		super();
	}
	
	/* Getter e Setter */
	
	/**
	 * @return the attoDiLegge
	 */
	public AttoDiLegge getAttoDiLegge() {
		return attoDiLegge;
	}

	/**
	 * @param attoDiLegge the attoDiLegge to set
	 */
	public void setAttoDiLegge(AttoDiLegge attoDiLegge) {
		this.attoDiLegge = attoDiLegge;
	}

	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the anno
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(String anno) {
		this.anno = anno;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	/**
	 * @return the dataInizioFinanziamento
	 */
	public Date getDataInizioFinanziamento() {
		if(dataInizioFinanziamento == null) {
			return null;
		}
		return new Date(dataInizioFinanziamento.getTime());
	}

	/**
	 * @param dataInizioFinanziamento the dataInizioFinanziamento to set
	 */
	public void setDataInizioFinanziamento(Date dataInizioFinanziamento) {
		if(dataInizioFinanziamento != null) {
			this.dataInizioFinanziamento = new Date(dataInizioFinanziamento.getTime());
		} else {
			this.dataInizioFinanziamento = null;
		}
	}

	/**
	 * @return the dataFineFinanziamento
	 */
	public Date getDataFineFinanziamento() {
		if(dataFineFinanziamento == null) {
			return null;
		}
		return new Date(dataFineFinanziamento.getTime());
	}

	/**
	 * @param dataFineFinanziamento the dataFineFinanziamento to set
	 */
	public void setDataFineFinanziamento(Date dataFineFinanziamento) {
		if(dataFineFinanziamento != null) {
			this.dataFineFinanziamento = new Date(dataFineFinanziamento.getTime());
		} else {
			this.dataFineFinanziamento = null;
		}
	}

	/**
	 * @return the gerarchia
	 */
	public String getGerarchia() {
		return gerarchia;
	}

	/**
	 * @param gerarchia the gerarchia to set
	 */
	public void setGerarchia(String gerarchia) {
		this.gerarchia = gerarchia;
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
	 * @return the uidAttoDiLegge
	 */
	public Integer getUidAttoDiLegge() {
		return uidAttoDiLegge;
	}

	/**
	 * @param uidAttoDiLegge the uidAttoDiLegge to set
	 */
	public void setUidAttoDiLegge(Integer uidAttoDiLegge) {
		this.uidAttoDiLegge = uidAttoDiLegge;
	}

	/**
	 * @return the uidCapitolo
	 */
	public Integer getUidCapitolo() {
		return uidCapitolo;
	}

	/**
	 * @param uidCapitolo the uidCapitolo to set
	 */
	public void setUidCapitolo(Integer uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}

	/**
	 * @return the uidBilancio
	 */
	public Integer getUidBilancio() {
		return uidBilancio;
	}

	/**
	 * @param uidBilancio the uidBilancio to set
	 */
	public void setUidBilancio(Integer uidBilancio) {
		this.uidBilancio = uidBilancio;
	}

	/**
	 * @return the uidAttoDiLeggeCapitolo
	 */
	public Integer getUidAttoDiLeggeCapitolo() {
		return uidAttoDiLeggeCapitolo;
	}

	/**
	 * @param uidAttoDiLeggeCapitolo the uidAttoDiLeggeCapitolo to set
	 */
	public void setUidAttoDiLeggeCapitolo(Integer uidAttoDiLeggeCapitolo) {
		this.uidAttoDiLeggeCapitolo = uidAttoDiLeggeCapitolo;
	}

	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * @return the aaData
	 */
	public List<ElementoRelazioneAttoDiLeggeCapitolo> getAaData() {
		return aaData;
	}

	/**
	 * @param aaData the aaData to set
	 */
	public void setAaData(List<ElementoRelazioneAttoDiLeggeCapitolo> aaData) {
		this.aaData = aaData;
	}
	
	/* Override di equals, hashCode, toString */
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AssociaAttoDiLeggeAlCapitoloModel)) {
			return false;
		}
		AssociaAttoDiLeggeAlCapitoloModel other = (AssociaAttoDiLeggeAlCapitoloModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(attoDiLegge, other.attoDiLegge)
			.append(capitolo, other.capitolo)
			.append(anno, other.anno)
			.append(numero, other.numero)
			.append(dataInizioFinanziamento, other.dataInizioFinanziamento)
			.append(dataFineFinanziamento, other.dataFineFinanziamento)
			.append(gerarchia, other.gerarchia)
			.append(descrizione, other.descrizione);
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(attoDiLegge)
			.append(capitolo)
			.append(anno)
			.append(numero)
			.append(dataInizioFinanziamento)
			.append(dataFineFinanziamento)
			.append(gerarchia)
			.append(descrizione);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("atto di legge", attoDiLegge)
			.append("capitolo", capitolo)
			.append("anno", anno)
			.append("numero", numero)
			.append("gerarchia", gerarchia)
			.append("descrizione", descrizione)
			.append("data inizio finanziamento", dataInizioFinanziamento)
			.append("data fine finanziamento", dataFineFinanziamento);
		return toStringBuilder.toString();
	}
	
	/* Request */
	/**
	 * Crea una request per inserimento di una relazione Atto di Legge - Capitolo.
	 * 
	 * @return la request creata
	 */
	public InserisceRelazioneAttoDiLeggeCapitolo creaRequestInserisciRelazioneAttoDiLeggeCapitolo() {
		InserisceRelazioneAttoDiLeggeCapitolo request = new InserisceRelazioneAttoDiLeggeCapitolo();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento dell'atto di legge
		request.setAttoDiLegge(creaAttoDiLeggeDaAssociare());
		
		// Popolamento del Capitolo
		request.setCapitolo(creaCapitoloDaAssociare());
		
		// Popolamento del AttoDiLeggeCapitolo
		request.setAttoDiLeggeCapitolo(creaAttoDiLeggeCapitolo());
		
		
		return request;
	}


	/**
	 * Crea una request per la cancellazione di una relazione tra Atto di Legge e Capitolo.
	 * 
	 * @return la request creata
	 */
	public CancellaRelazioneAttoDiLeggeCapitolo creaRequestCancellaRelazioneAttoDiLeggeCapitolo() {
		CancellaRelazioneAttoDiLeggeCapitolo request = new CancellaRelazioneAttoDiLeggeCapitolo();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento del AttoDiLeggeCapitolo
		request.setAttoDiLeggeCapitolo(creaAttoDiLeggeCapitoloDaEliminare());
		
		
		return request;
	}
	/**
	 * Crea una request per la cancellazione di un Atto di Legge.
	 * 
	 * @return la request creata
	 */
	public RicercaRelazioneAttoDiLeggeCapitolo creaRequestRicercaRelazioneAttoDiLeggeCapitolo() {
		RicercaRelazioneAttoDiLeggeCapitolo request = new RicercaRelazioneAttoDiLeggeCapitolo();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
	
		RicercaAttiDiLeggeCapitolo ricercaAttiDiLeggeCapitolo = new RicercaAttiDiLeggeCapitolo();
		
		ricercaAttiDiLeggeCapitolo.setCapitolo(creaCapitoloDaCercare());
		ricercaAttiDiLeggeCapitolo.setStato(StatoEntita.VALIDO);
		ricercaAttiDiLeggeCapitolo.setBilancio(getBilancio());
		ricercaAttiDiLeggeCapitolo.setAttoDiLegge(new AttoDiLegge());
		request.setRicercaAttiDiLeggeCapitolo(ricercaAttiDiLeggeCapitolo);
		
		return request;
	}
	/**
	 * Crea request per ricerca puntuale relazione Capitolo Atto di Legge.
	 * <br>
	 * Deve essere valorizzato uidAttoDiLegge
	 * 
	 * @return la request creata
	 */
	public RicercaRelazioneAttoDiLeggeCapitolo creaRequestRicercaPuntualeRelazioneAttoDiLeggeCapitolo() {
		RicercaRelazioneAttoDiLeggeCapitolo request = new RicercaRelazioneAttoDiLeggeCapitolo();
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
	
		RicercaAttiDiLeggeCapitolo ricercaAttiDiLeggeCapitolo = new RicercaAttiDiLeggeCapitolo();
		
		ricercaAttiDiLeggeCapitolo.setUid(uidAttoDiLeggeCapitolo);
		
		request.setRicercaAttiDiLeggeCapitolo(ricercaAttiDiLeggeCapitolo);

		return request;

	}
	/**
	 * Crea una request per aggiornamento di una relazione Atto di Legge - Capitolo.
	 * 
	 * @return la request creata
	 */
	public AggiornaRelazioneAttoDiLeggeCapitolo creaRequestAggiornaRelazioneAttoDiLeggeCapitolo() {
		AggiornaRelazioneAttoDiLeggeCapitolo request = new AggiornaRelazioneAttoDiLeggeCapitolo();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento dell'atto di legge
		request.setAttoDiLegge(creaAttoDiLeggeDaAggiornare());
		
		// Popolamento del Capitolo
		request.setCapitolo(creaCapitoloDaAssociare());
		
		// Popolamento del AttoDiLeggeCapitolo
		request.setAttoDiLeggeCapitolo(creaAttoDiLeggeCapitoloDaAggiornare());
		
		return request;
	}
	

	/* Metodi di utilita' */
	/**
	 * Metodo di utilit&agrave; per la creazione di un Atto di Legge per la associazione al capitolo.
	 * 
	 * @return l'Atto da associare
	 */
	private AttoDiLegge creaAttoDiLeggeDaAssociare() {
		AttoDiLegge attoDiLeggeDaCreare = new AttoDiLegge();
		if(uidAttoDiLegge != null) {
			attoDiLeggeDaCreare.setUid(uidAttoDiLegge);
		} else {
			attoDiLeggeDaCreare.setUid(0);
		}
		
		return attoDiLeggeDaCreare;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un Capitolo da associare a Atto di Legge.
	 * 
	 * @return Il capitolo da associare
	 */
	private Capitolo<?, ?> creaCapitoloDaAssociare() {
		@SuppressWarnings("rawtypes")
		Capitolo<?, ?> capitoloDaCreare = new Capitolo();
		if(uidCapitolo != null) {
			capitoloDaCreare.setUid(uidCapitolo);
		} else {
			capitoloDaCreare.setUid(0);
		}
		
		return capitoloDaCreare;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un Capitolo da associare a Atto di Legge.
	 * 
	 * @return Il capitolo da associare
	 */
	private AttoDiLeggeCapitolo creaAttoDiLeggeCapitolo() {
		AttoDiLeggeCapitolo attoDiLeggeCapitolo = new AttoDiLeggeCapitolo();
		
		attoDiLeggeCapitolo.setDescrizione(descrizione);
		attoDiLeggeCapitolo.setGerarchia(gerarchia);
		attoDiLeggeCapitolo.setDataInizioFinanz(dataInizioFinanziamento);
		attoDiLeggeCapitolo.setDataFineFinanz(dataFineFinanziamento);
		
		attoDiLeggeCapitolo.setAttoDiLegge(creaAttoDiLeggeDaAssociare());
		
		return attoDiLeggeCapitolo;
	}

	/**
	 * Metodo di utilit&agrave; per la creazione di un Capitolo da associare a Atto di Legge.
	 * 
	 * @return AttoDILeggeCapitolo da eliminare
	 */
	private AttoDiLeggeCapitolo creaAttoDiLeggeCapitoloDaAggiornare() {
		AttoDiLeggeCapitolo attoDiLeggeCapitolo = new AttoDiLeggeCapitolo();
		
		attoDiLeggeCapitolo.setUid(uidAttoDiLeggeCapitolo);
		attoDiLeggeCapitolo.setDescrizione(descrizione);
		attoDiLeggeCapitolo.setGerarchia(gerarchia);
		attoDiLeggeCapitolo.setDataInizioFinanz(dataInizioFinanziamento);
		attoDiLeggeCapitolo.setDataFineFinanz(dataFineFinanziamento);
		
		return attoDiLeggeCapitolo;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un Atto di Legge da aggiornare.
	 * 
	 * @return l'Atto da associare
	 */
	private AttoDiLegge creaAttoDiLeggeDaAggiornare() {
		AttoDiLegge attoDiLeggeDaAggiornare = new AttoDiLegge();
		if(uidAttoDiLegge != null) {
			attoDiLeggeDaAggiornare.setUid(uidAttoDiLegge);
		} else {
			attoDiLeggeDaAggiornare.setUid(0);
		}
		
		return attoDiLeggeDaAggiornare;
	}
	/**
	 * Metodo di utilit&agrave; per la creazione di un Capitolo da associare a Atto di Legge.
	 * 
	 * @return AttoDILeggeCapitolo da eliminare
	 */
	private AttoDiLeggeCapitolo creaAttoDiLeggeCapitoloDaEliminare() {
		AttoDiLeggeCapitolo attoDiLeggeCapitolo = new AttoDiLeggeCapitolo();
		
		attoDiLeggeCapitolo.setUid(uidAttoDiLeggeCapitolo);
		
		return attoDiLeggeCapitolo;
	}
	
	/* Metodi di utilita' */
	/**
	 * Metodo di utilit&agrave; per la creazione di un Capitolo da associare a Atto di Legge.
	 * 
	 * @return Il capitolo da cercare
	 */
	private Capitolo<?, ?> creaCapitoloDaCercare() {
		@SuppressWarnings("rawtypes")
		Capitolo<?, ?> capitoloDaCercare = new Capitolo();
		if(uidCapitolo != null) {
			capitoloDaCercare.setUid(uidCapitolo);
		} else {
			capitoloDaCercare.setUid(0);
		}
		
		return capitoloDaCercare;
	}
	
	
}
