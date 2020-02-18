/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * Wrapper per il movimento iva.
 * 
 * @author Valentina
 * @version 1.0.0 - 06/05/2015
 * 
 * @param <D>   il tipo del documento
 * @param <SD>  il tipo del subdocumento
 * @param <SDI> il tipo del subdocumento iva
 *
 */
public class ElementoMovimentoIva<D extends Documento<SD, SDI>, SD extends Subdocumento<D, SDI>, SDI extends SubdocumentoIva<D, SD, ?>> implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5373837455635805301L;
	
	private final SDI subdocumentoIva;
	private final AliquotaSubdocumentoIva aliquota;
	
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param subdocumento il subdocumento da wrappare
	 * @param aliquota     l'aliquota da wrappare
	 */
	public ElementoMovimentoIva(SDI subdocumento, AliquotaSubdocumentoIva aliquota) {
		this.subdocumentoIva = subdocumento;
		this.aliquota = aliquota;
	}
	

	/**
	 * @return the subdocumentoIva
	 */
	public SDI getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @return the aliquota
	 */
	public AliquotaSubdocumentoIva getAliquota() {
		return aliquota;
	}

	@Override
	public int getUid() {
		return 0;
	}

	/**
	 * @return the numeroQuota
	 */
	public String getNumeroQuota() {
		if(subdocumentoIva == null || subdocumentoIva.getSubdocumento() == null || subdocumentoIva.getSubdocumento().getNumero() == null) {
			return "";
		}
		return subdocumentoIva.getSubdocumento().getNumero().toString();
	}
	
	/**
	 * @return the registrazioneIva
	 */
	public String getRegistrazioneIva() {
		if(subdocumentoIva == null || subdocumentoIva.getSubdocumento() == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		addIfNotNullNorEmpty(chunks, subdocumentoIva.getSubdocumento().getNumeroRegistrazioneIVA());
		addIfNotNullNorEmpty(chunks, Boolean.TRUE.equals(subdocumentoIva.getFlagRegistrazioneIva()) ? "S&Igrave;" : "NO");
		
		return StringUtils.join(chunks, " / ");
	}
	
	/**
	 * @return the attivitaIva
	 */
	public String getAttivitaIva() {
		if(subdocumentoIva == null || subdocumentoIva.getAttivitaIva() == null) {
			return "";
		}
		
		List<String> chunks = new ArrayList<String>();
		addIfNotNullNorEmpty(chunks, subdocumentoIva.getAttivitaIva().getCodice());
		addIfNotNullNorEmpty(chunks, subdocumentoIva.getAttivitaIva().getDescrizione());
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the aliquotaIva
	 */
	public String getAliquotaIva() {
		if(aliquota == null || aliquota.getAliquotaIva() == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a rel=\"popover\" href=\"#\" data-original-title=\"Percentuale aliquota\" data-trigger=\"hover\" data-content=\"")
			.append(FormatUtils.formatCurrency(aliquota.getAliquotaIva().getPercentualeAliquota()))
			.append("\"/>")
			.append(aliquota.getAliquotaIva().getCodice())
			.append(" - ")
			.append(aliquota.getAliquotaIva().getDescrizione())
			.append("</a>")
			.toString();
	}
	
	/**
	 * @return the imponibile
	 */
	public BigDecimal getImponibile() {
		return aliquota != null ? aliquota.getImponibile() : null;
	}
	
	/**
	 * @return the imposta
	 */
	public BigDecimal getImposta() {
		return aliquota != null ? aliquota.getImposta() : null;
	}
	
	/**
	 * @return the impostaIndetraibile
	 */
	public BigDecimal getImpostaIndetraibile() {
		return aliquota != null ? aliquota.getImpostaIndetraibile() : null;
	}
	
	/**
	 * @return the impostaDetraibile
	 */
	public BigDecimal getImpostaDetraibile() {
		return aliquota != null ? aliquota.getImpostaDetraibile() : null;
	}
	
	/**
	 * Aggiunge la stringa se non null e non vuota.
	 * 
	 * @param chunks l'aggregatore delle stringhe
	 * @param chunk la stringa da apporre
	 */
	private void addIfNotNullNorEmpty(Collection<String> chunks, Object chunk) {
		if(chunk == null) {
			return;
		}
		String str = chunk.toString();
		if(StringUtils.isNotBlank(str)) {
			chunks.add(str);
		}
	}

}
