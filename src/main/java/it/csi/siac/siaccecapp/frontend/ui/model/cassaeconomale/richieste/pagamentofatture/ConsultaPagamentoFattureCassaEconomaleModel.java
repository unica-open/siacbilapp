/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.Function;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.Reductor;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


/**
 * Classe di model per la consultazione del pagamento fatture.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 * @author Marchino Alessandro
 * @version 1.1.0 - 15/09/2015
 */
public class ConsultaPagamentoFattureCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 476264063690603015L;
	
	private DocumentoSpesa documentoSpesa;
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	private BigDecimal importoPagato = BigDecimal.ZERO;
	private BigDecimal importoSplitReverse = BigDecimal.ZERO;
	/** Costruttore vuoto di default */
	public ConsultaPagamentoFattureCassaEconomaleModel() {
		setTitolo("Consultazione pagamento fatture");
	}

	/**
	 * @return the documentoSpesa
	 */
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}

	/**
	 * @param documentoSpesa the documentoSpesa to set
	 */
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}

	/**
	 * @return the listaSubdocumentoSpesa
	 */
	public List<SubdocumentoSpesa> getListaSubdocumentoSpesa() {
		return listaSubdocumentoSpesa;
	}

	/**
	 * @param listaSubdocumentoSpesa the listaSubdocumentoSpesa to set
	 */
	public void setListaSubdocumentoSpesa(List<SubdocumentoSpesa> listaSubdocumentoSpesa) {
		this.listaSubdocumentoSpesa = listaSubdocumentoSpesa != null ? listaSubdocumentoSpesa : new ArrayList<SubdocumentoSpesa>();
	}
	
	/**
	 * @return the importoPagato
	 */
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}

	/**
	 * @param importoPagato the importoPagato to set
	 */
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}

	/**
	 * @return the importoSplitReverse
	 */
	public BigDecimal getImportoSplitReverse() {
		return importoSplitReverse;
	}

	/**
	 * @param importoSplitReverse the importoSplitReverse to set
	 */
	public void setImportoSplitReverse(BigDecimal importoSplitReverse) {
		this.importoSplitReverse = importoSplitReverse;
	}

	/**
	 * Effettua il join delle quote prendendo il numero.
	 *
	 * @param quote le quote per cui effettuare il join.
	 * @return i numeri delle quote, separati da virgola
	 */
	private String joinNumeroQuote(List<SubdocumentoSpesa> quote) {
		// Completamente inefficiente. Ma per una volta tanto volevo provare a programmare in maniera funzionale
		Collection<String> listaNumeroQuote = CollectionUtil.map(quote, new Function<SubdocumentoSpesa, String>() {
			@Override
			public String map(SubdocumentoSpesa source) {
				return source.getNumero().toString();
			}
		});
		return CollectionUtil.reduce(listaNumeroQuote, new Reductor<String, String>() {
			@Override
			public String reduce(String accumulator, String currentValue, int index, Collection<String> collection) {
				return accumulator + ", " + currentValue;
			}
		});
	}
	
	/**
	 * @return the descrizioneQuoteAssociate
	 */
	public String getDescrizioneQuoteAssociate() {
		return joinNumeroQuote(getListaSubdocumentoSpesa());
	}

	/**
	 * @return the rilevanteIVA
	 */
	public boolean isRilevanteIVA() {
		for(SubdocumentoSpesa ss : getListaSubdocumentoSpesa()) {
			if(Boolean.TRUE.equals(ss.getFlagRilevanteIVA())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		if(getDocumentoSpesa() == null || getDocumentoSpesa().getSoggetto() == null) {
			return "";
		}
		Soggetto s = getDocumentoSpesa().getSoggetto();
		List<String> chunks = new ArrayList<String>();
		if(StringUtils.isNotBlank(s.getCodiceFiscale())) {
			chunks.add("CF: " + s.getCodiceFiscale());
		}
		if(StringUtils.isNotBlank(s.getPartitaIva())) {
			chunks.add("P.IVA: " + s.getPartitaIva());
		}
		return StringUtils.join(chunks, " - ");
	}

}
