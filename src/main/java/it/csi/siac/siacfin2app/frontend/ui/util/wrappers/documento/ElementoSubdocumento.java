/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * Classe di wrap per il Subdocumento.

 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 * @version 1.1.0 - 14/09/2016
 * @param <S> la tipizzazione del subdocumento
 * @param <MG> la tipizzazione del movimento di gestione
 * @param <SMG> la tipizzazione del subdocumento di gestione
 * @param <C> la tipizzazione del capitolo
 *
 */
public abstract class ElementoSubdocumento<S extends Subdocumento<?, ?>, MG extends MovimentoGestione, SMG extends MovimentoGestione, C extends Capitolo<?, ?>> implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	/** Il subdocumento */
	protected final S subdocumento;
	/** Il movimento di gestione */
	protected final MG movimentoGestione;
	private final SMG subMovimentoGestione;
	private final boolean isGestioneUEB;
	
	/**
	 * Costruttore di wrap
	 * @param subdocumento il subdocumento
	 * @param movimentoGestione il movimento di gestione
	 * @param subMovimentoGestione il submovimento di gestione
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	protected ElementoSubdocumento(S subdocumento, MG movimentoGestione, SMG subMovimentoGestione, boolean isGestioneUEB) {
		this.subdocumento = subdocumento;
		this.movimentoGestione = movimentoGestione;
		this.subMovimentoGestione = subMovimentoGestione;
		this.isGestioneUEB = isGestioneUEB;
	}

	
	/**
	 * @return the uid
	 */
	@Override
	public int getUid() {
		return subdocumento != null ? subdocumento.getUid() : 0;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return subdocumento != null && subdocumento.getNumero() != null ? subdocumento.getNumero().toString() : "";
	}

	/**
	 * @return the movimento
	 */
	public String getMovimento() {
		if(movimentoGestione == null) {
			return "";
		}
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getBilancio() == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder()
			.append(capitolo.getBilancio().getAnno())
			.append("/")
			.append(movimentoGestione.getAnnoMovimento());
		if(movimentoGestione.getNumero() != null) {
			sb.append("/")
				.append(movimentoGestione.getNumero().toPlainString());
		}
		if(subMovimentoGestione != null && subMovimentoGestione.getNumero() != null) {
			sb.append("/")
				.append(subMovimentoGestione.getNumero().toPlainString());
		}
		return sb.toString();
	}

	/**
	 * @return the provvedimento
	 */
	public String getProvvedimento() {
		if(subdocumento == null || subdocumento.getAttoAmministrativo() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(Integer.toString(subdocumento.getAttoAmministrativo().getAnno()))
			.append("/")
			.append(Integer.toString(subdocumento.getAttoAmministrativo().getNumero()));
		if(subdocumento.getAttoAmministrativo().getTipoAtto() != null) {
			sb.append("/")
				.append(subdocumento.getAttoAmministrativo().getTipoAtto().getDescrizione());
		}
		if(subdocumento.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
			sb.append("/")
				.append(subdocumento.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
		}
		
		return sb.toString();
	}

	/**
	 * @return the ordinativo
	 */
	public String getOrdinativo() {
		if(subdocumento == null || subdocumento.getOrdinativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append(subdocumento.getOrdinativo().getAnno())
			.append("/")
			.append(subdocumento.getOrdinativo().getNumero())
			.toString();
	}
	
	/**
	 * @return the dataEmissione
	 */
	public String getDataEmissione() {
		if(subdocumento == null || subdocumento.getOrdinativo() == null) {
			return "";
		}
		return FormatUtils.formatDate(subdocumento.getOrdinativo().getDataEmissione());
	}

	/**
	 * @return the provvisorio
	 */
	public String getProvvisorio() {
		if(subdocumento == null || subdocumento.getProvvisorioCassa() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		
		if(subdocumento.getProvvisorioCassa().getDataEmissione() != null){
			sb.append(FormatUtils.formatDate(subdocumento.getProvvisorioCassa().getDataEmissione()))
				.append(" - ");
		}
		return sb.append(subdocumento.getProvvisorioCassa().getNumero())
			.toString();
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return subdocumento != null ? subdocumento.getImportoNotNull() : BigDecimal.ZERO;
	}

	/**
	 * @return the importoDaDedurre
	 */
	public BigDecimal getImportoDaDedurre() {
		return subdocumento != null ? subdocumento.getImportoDaDedurreNotNull() : BigDecimal.ZERO;
	}

	/**
	 * @return the stringaCapitolo
	 */
	public String getStringaCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(capitolo.getAnnoCapitolo())
				.append(" - ")
				.append(capitolo.getNumeroCapitolo())
				.append(" - ")
				.append(capitolo.getNumeroArticolo());
		if(isGestioneUEB) {
			sb.append(" - ")
				.append(capitolo.getNumeroUEB());
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the stringaAttivitaIva
	 */
	public String getStringaAttivitaIva() {
		if(subdocumento == null || subdocumento.getSubdocumentoIva() == null || subdocumento.getSubdocumentoIva().getAttivitaIva() == null) {
			return "";
		}
		return new StringBuilder()
			.append(subdocumento.getSubdocumentoIva().getAttivitaIva().getCodice())
			.append(" - ")
			.append(subdocumento.getSubdocumentoIva().getAttivitaIva().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the numeroRegistrazioneIva
	 */
	public String getNumeroRegistrazioneIva() {
		return subdocumento != null ? subdocumento.getNumeroRegistrazioneIVA() : "";
	}
	
	/**
	 * @return the totaleMovimentiIva
	 */
	public BigDecimal getTotaleMovimentiIva() {
		return subdocumento != null && subdocumento.getSubdocumentoIva() != null ? subdocumento.getSubdocumentoIva().getTotaleMovimentiIva() : BigDecimal.ZERO;
	}
	
	/**
	 * @return the uidSubdocumentoIva
	 */
	public int getUidSubdocumentoIva() {
		return subdocumento != null && subdocumento.getSubdocumentoIva() != null ? subdocumento.getSubdocumentoIva().getUid() : 0;
	}

	/**
	 * Ottiene il capitolo
	 * @return il capitolo
	 */
	protected abstract C ottieniCapitolo();
	
}
