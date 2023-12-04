/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

/**
 * Classe di wrap per il Documento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 *
 */
public class ElementoDocumento implements Serializable, ModelWrapper, Comparable<ElementoDocumento> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	
	private Integer uid;
	private String documento;
	private String data;
	private String statoOperativoDocumentoCode;
	private String statoOperativoDocumentoDesc;
	private String tipoDocumentoNotaCredito;
	private boolean flagComunicaPCC;
	private boolean flagAttivaGen;
	private boolean contabilizzaGenPCC;
	private String soggetto;
	private BigDecimal importo = BigDecimal.ZERO;
	private BigDecimal importoDaDedurreSuFattura = BigDecimal.ZERO;
	private String statoOperativoDocumentoPadreCode;
	private TipoRelazione tipoRelazione;
	private String azioni;
	
	//SIAC-5617
	private String tipoDocumentoCode;
	
	//SIAC-7557
	private Integer idTipoDocumento;
	
	
	// SIAC-6565
	private String statoSDI;
	private String esitoStatoSDI;
	
	// TODO: vedere se sia necessaria la lista
	private Boolean tipoALG = Boolean.FALSE;
	
	/** Costruttore vuoto di default */
	public ElementoDocumento() {
		super();
	}

	/**
	 * @return the uid
	 */
	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the statoOperativoDocumentoCode
	 */
	public String getStatoOperativoDocumentoCode() {
		return statoOperativoDocumentoCode;
	}

	/**
	 * @param statoOperativoDocumentoCode the statoOperativoDocumentoCode to set
	 */
	public void setStatoOperativoDocumentoCode(String statoOperativoDocumentoCode) {
		this.statoOperativoDocumentoCode = statoOperativoDocumentoCode;
	}

	/**
	 * @return the statoOperativoDocumentoDesc
	 */
	public String getStatoOperativoDocumentoDesc() {
		return statoOperativoDocumentoDesc;
	}

	/**
	 * @param statoOperativoDocumentoDesc the statoOperativoDocumentoDesc to set
	 */
	public void setStatoOperativoDocumentoDesc(String statoOperativoDocumentoDesc) {
		this.statoOperativoDocumentoDesc = statoOperativoDocumentoDesc;
	}

	/**
	 * @return the tipoDocumentoNotaCredito
	 */
	public String getTipoDocumentoNotaCredito() {
		return tipoDocumentoNotaCredito;
	}

	/**
	 * @param tipoDocumentoNotaCredito the tipoDocumentoNotaCredito to set
	 */
	public void setTipoDocumentoNotaCredito(String tipoDocumentoNotaCredito) {
		this.tipoDocumentoNotaCredito = tipoDocumentoNotaCredito;
	}

	/**
	 * @return the soggetto
	 */
	public String getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	
	/**
	 * @return the importoDaDedurreSuFattura
	 */
	public BigDecimal getImportoDaDedurreSuFattura() {
		return importoDaDedurreSuFattura;
	}

	/**
	 * @param importoDaDedurreSuFattura the importoDaDedurreSuFattura to set
	 */
	public void setImportoDaDedurreSuFattura(BigDecimal importoDaDedurreSuFattura) {
		this.importoDaDedurreSuFattura = importoDaDedurreSuFattura;
	}

	/**
	 * @return the statoOperativoDocumentoPadreCode
	 */
	public String getStatoOperativoDocumentoPadreCode() {
		return statoOperativoDocumentoPadreCode;
	}

	/**
	 * @param statoOperativoDocumentoPadreCode the statoOperativoDocumentoPadreCode to set
	 */
	public void setStatoOperativoDocumentoPadreCode(
			String statoOperativoDocumentoPadreCode) {
		this.statoOperativoDocumentoPadreCode = statoOperativoDocumentoPadreCode;
	}

	/**
	 * @return the tipoRelazione
	 */
	public TipoRelazione getTipoRelazione() {
		return tipoRelazione;
	}

	/**
	 * @param tipoRelazione the tipoRelazione to set
	 */
	public void setTipoRelazione(TipoRelazione tipoRelazione) {
		this.tipoRelazione = tipoRelazione;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	/**
	 * @return the tipoALG
	 */
	public Boolean getTipoALG() {
		return tipoALG;
	}

	/**
	 * @param tipoALG the tipoALG to set
	 */
	public void setTipoALG(Boolean tipoALG) {
		this.tipoALG = tipoALG;
	}

	/**
	 * @return the flagComunicaPCC
	 */
	public boolean isFlagComunicaPCC() {
		return flagComunicaPCC;
	}

	/**
	 * @param flagComunicaPCC the flagComunicaPCC to set
	 */
	public void setFlagComunicaPCC(boolean flagComunicaPCC) {
		this.flagComunicaPCC = flagComunicaPCC;
	}

	/**
	 * @return the flagAttivaGen
	 */
	public boolean isFlagAttivaGen() {
		return flagAttivaGen;
	}

	/**
	 * @param flagAttivaGen the flagAttivaGen to set
	 */
	public void setFlagAttivaGen(boolean flagAttivaGen) {
		this.flagAttivaGen = flagAttivaGen;
	}

	/**
	 * @return the contabilizzaGenPCC
	 */
	public boolean isContabilizzaGenPCC() {
		return contabilizzaGenPCC;
	}

	/**
	 * @param contabilizzaGenPCC the contabilizzaGenPCC to set
	 */
	public void setContabilizzaGenPCC(boolean contabilizzaGenPCC) {
		this.contabilizzaGenPCC = contabilizzaGenPCC;
	}
	

	/**
	 * @return the tipoDocumentoCode
	 */
	public String getTipoDocumentoCode() {
		return tipoDocumentoCode;
	}

	/**
	 * @param tipoDocumentoCode the tipoDocumentoCode to set
	 */
	public void setTipoDocumentoCode(String tipoDocumentoCode) {
		this.tipoDocumentoCode = tipoDocumentoCode;
	}

	
	//SIAC-7557
	
	/**
	 * @return the idTipoDocumento
	 */
	public Integer getIdTipoDocumento()
	{
		return idTipoDocumento;
	}

	/**
	 * @param idTipoDocumento the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(int idTipoDocumento)
	{
		this.idTipoDocumento = idTipoDocumento;
	}
	//SIAC-7557
	/**
	 * @return the statoSDI
	 */
	public String getStatoSDI() {
		return this.statoSDI;
	}

	/**
	 * @param statoSDI the statoSDI to set
	 */
	public void setStatoSDI(String statoSDI) {
		this.statoSDI = statoSDI;
	}

	/**
	 * @return the esitoStatoSDI
	 */
	public String getEsitoStatoSDI() {
		return this.esitoStatoSDI;
	}

	/**
	 * @param esitoStatoSDI the esitoStatoSDI to set
	 */
	public void setEsitoStatoSDI(String esitoStatoSDI) {
		this.esitoStatoSDI = esitoStatoSDI;
	}

	/**
	 * Controlla se il documento &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del documento &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoValido() {
		return StatoOperativoDocumento.VALIDO.getCodice().equalsIgnoreCase(statoOperativoDocumentoCode);
	}
	
	/**
	 * Controlla se il documento &eacute; in stato incompleto.
	 * 
	 * @return <code>true</code> se lo stato operativo del documento &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoIncompleto() {
		return StatoOperativoDocumento.INCOMPLETO.getCodice().equalsIgnoreCase(statoOperativoDocumentoCode);
	}
	
	/**
	 * Controlla se il documento &eacute; in stato emesso.
	 * 
	 * @return <code>true</code> se lo stato operativo del documento &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoEmesso() {
		return StatoOperativoDocumento.EMESSO.getCodice().equalsIgnoreCase(statoOperativoDocumentoCode);
	}
	
	/**
	 * Controlla se il documento &eacute; in stato annullato.
	 * 
	 * @return <code>true</code> se lo stato operativo del documento &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoAnnullato() {
		return StatoOperativoDocumento.ANNULLATO.getCodice().equalsIgnoreCase(statoOperativoDocumentoCode);
	}
	
	/**
	 * Controlla se si tratta di una nota di credito.
	 * 
	 * @return <code>true</code> se il gruppo del tipo &eacute; nota credito; <code>false</code> altrimenti
	 */
	public boolean checkNotaCredito() {
		return BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(tipoDocumentoNotaCredito);
	}
	
	/**
	 * Controlla se il documento .
	 * 
	 * @return <code>true</code> se il gruppo del tipo &eacute; nota credito; <code>false</code> altrimenti
	 */
	public boolean checkAllegatoAtto() {
		return Boolean.TRUE.equals(tipoALG);
	}
	
	
	//SIAC-6988 inizio FL
	/**
	 *  Controlla se si tratta di una nota di accredito .
	 *  CODICE_NOTE_ACCREDITO("NCV"),
	 * */
	
	public boolean checkNotaAccredito() {
		if (tipoDocumentoCode != null && !tipoDocumentoCode.equals("")) {
			return BilConstants.CODICE_NOTE_ACCREDITO.getConstant().equalsIgnoreCase(tipoDocumentoCode);	
		}
		return false;
	}
	
	
	/**
	 * Controlla se il documento &eacute; in uno stato SDI per la gestione delle note di credito. in caso di FTV
	 * 
	 * @return <code>true</code> se il gruppo del tipo &eacute; nota credito; <code>false</code> altrimenti
	 */
	public boolean isGestioneStatoSDINota() {
	 
			return checkNotaAccredito() && checkStatoSDIFEL();
		 
	}
	
	//SIAC-6988 fine FL	
	
	/**
	 * Controlla se il documento &eacute; in uno stato conforme per la gestione delle note di credito.
	 * 
	 * @return <code>true</code> se il gruppo del tipo &eacute; nota credito; <code>false</code> altrimenti
	 */
	public boolean isGestioneStatoNota() {
		return 
			// controllo lo stato del documento padre
			StatoOperativoDocumento.INCOMPLETO.getCodice().equalsIgnoreCase(statoOperativoDocumentoPadreCode) ||
			StatoOperativoDocumento.VALIDO.getCodice().equalsIgnoreCase(statoOperativoDocumentoPadreCode) ||
			StatoOperativoDocumento.PARZIALMENTE_EMESSO.getCodice().equalsIgnoreCase(statoOperativoDocumentoPadreCode) ||
			StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.getCodice().equalsIgnoreCase(statoOperativoDocumentoPadreCode);
	}
	
	/**
	 * Controlla se il documento &eacute;
	 * 
	 * @return <code>true</code> se il gruppo del tipo &eacute; nota credito; <code>false</code> altrimenti
	 */
	public boolean checkIntrastat() {
		return TipoRelazione.CONTROREGISTRAZIONE_INTRASTAT.equals(tipoRelazione);
	}
	
	/**
	 * @return the contabilizzato
	 */
	public String getContabilizzato(){
		return isContabilizzaGenPCC() ? "S&igrave;" : "No";
	}
	
	@Override
	public int compareTo(ElementoDocumento o) {
		return new CompareToBuilder().append(this.uid, o.uid).toComparison();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof ElementoDocumento)) {
			return false;
		}
		ElementoDocumento other = (ElementoDocumento)obj;
		return new EqualsBuilder().append(this.uid, other.uid).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(uid).toHashCode();
	}
	//SIAC-6565-CR1215
	/**
	 * La decode stato SDI
	 * @return la decodifica
	 */
	public String getDecodeStatoSDI() {
		//TODO IF PER DECODIFICA
		if (statoSDI!=null && !statoSDI.isEmpty()) {
			String statoConEsito = StatoSDIDocumento.getDescrizioneFromCodice(statoSDI) + "|" + esitoStatoSDI; 
			return statoConEsito;
		
		}
		return " | ";
	}

	/**
	 * Controlla se lo stato SDI &eacute; valido per consentire EmettiFattura e Aggiorna.
	 * @return se lo stato sia valido
	 */
	public boolean checkStatoSDIValido() {
		
		return  statoSDI == null ||
				statoSDI.equalsIgnoreCase("") ||
				StatoSDIDocumento.SCARTATA.getCodice().equalsIgnoreCase(statoSDI) ||
				StatoSDIDocumento.MANCATA_CONS.getCodice().equalsIgnoreCase(statoSDI) ||
				StatoSDIDocumento.RIFIUTATA.getCodice().equalsIgnoreCase(statoSDI) ||
				StatoSDIDocumento.SCARTATO_FEL.getCodice().equalsIgnoreCase(statoSDI);
	}
	
	/** SIAC-6988 FL
	 * Controlla se lo stato SDI &eacute; valido per consentire EmettiFattura e Aggiorna.
	 * @return se lo stato sia valido
	 */
	public boolean checkStatoSDIFEL() {
		
		return   StatoSDIDocumento.DECORR_TERMINI.getCodice().equalsIgnoreCase(statoSDI)  ||
				StatoSDIDocumento.ACCET_CONSEG.getCodice().equalsIgnoreCase(statoSDI)  ||
				StatoSDIDocumento.INVIATA_FEL.getCodice().equalsIgnoreCase(statoSDI);
		
	}
}
