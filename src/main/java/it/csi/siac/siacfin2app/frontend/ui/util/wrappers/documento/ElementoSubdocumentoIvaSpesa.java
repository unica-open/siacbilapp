/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di wrap per il Subdocumento di Spesa con Iva.

 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/06/2014
 *
 */
public class ElementoSubdocumentoIvaSpesa extends SubdocumentoSpesa implements ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4670005268007769442L;
	
	private Boolean gestioneUEB;
	private Integer annoEsercizio;
	private CapitoloUscitaGestione capitoloUscita;
	private List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
	
	/** Costruttore vuoto di default */
	public ElementoSubdocumentoIvaSpesa() {
		super();
	}
	
	/**
	 * Costruttore a partire dalla sottoclasse.
	 * 
	 * @param subdocumento  il subdocumento da wrappare
	 * @param annoEsercizio l'anno di esercizio del subdocumento
	 * @param gestioneUEB   se l'ente gestisce le UEB o meno
	 */
	public ElementoSubdocumentoIvaSpesa(SubdocumentoSpesa subdocumento, Integer annoEsercizio, Boolean gestioneUEB) {
		this();
		ReflectionUtil.downcastByReflection(subdocumento, this);
		this.gestioneUEB = gestioneUEB;
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the gestioneUEB
	 */
	public Boolean getGestioneUEB() {
		return gestioneUEB;
	}

	/**
	 * @param gestioneUEB the gestioneUEB to set
	 */
	public void setGestioneUEB(Boolean gestioneUEB) {
		this.gestioneUEB = gestioneUEB;
	}

	/**
	 * @return the annoEsercizio
	 */
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	/**
	 * @param annoEsercizio the annoEsercizio to set
	 */
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the capitoloUscita
	 */
	public CapitoloUscitaGestione getCapitoloUscita() {
		return capitoloUscita;
	}

	/**
	 * @param capitoloUscita the capitoloUscita to set
	 */
	public void setCapitoloUscita(CapitoloUscitaGestione capitoloUscita) {
		this.capitoloUscita = capitoloUscita;
	}

	/**
	 * @return the listaAttivitaIva
	 */
	public List<AttivitaIva> getListaAttivitaIva() {
		return listaAttivitaIva;
	}

	/**
	 * @param listaAttivitaIva the listaAttivitaIva to set
	 */
	public void setListaAttivitaIva(List<AttivitaIva> listaAttivitaIva) {
		this.listaAttivitaIva = listaAttivitaIva != null ? listaAttivitaIva : new ArrayList<AttivitaIva>();
	}
	
	/**
	 * Ottiene una struinga di descrizione del capitolo
	 *
	 * @return the capitolo
	 */
	public String getCapitolo() {
		StringBuilder result = new StringBuilder();
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscita();
		if(capitoloUscitaGestione != null) {
			result.append(capitoloUscitaGestione.getAnnoCapitolo())
				.append("-")
				.append(capitoloUscitaGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloUscitaGestione.getNumeroArticolo());
			if(Boolean.TRUE.equals(getGestioneUEB())) {
				result.append( "/" )
					.append(capitoloUscitaGestione.getNumeroUEB());
			}
		}
		return result.toString();
	}
	
	/**
	 * Gets la stringa di descrizione del movimento.
	 *
	 * @return the movimento
	 */
	public String getMovimento() {
		StringBuilder result = new StringBuilder();
		Impegno impegno = getImpegno();
		if(impegno != null) {
			result.append(getAnnoEsercizio())
				.append("/")
				.append(impegno.getAnnoMovimento())
				.append("/").append(impegno.getNumero().toPlainString());
			SubImpegno subImpegno = getSubImpegno();
			if(subImpegno != null) {
				result.append("-").append(subImpegno.getNumero().toPlainString());
			}
		}
		return result.toString();
	}
	
	/**
	 * @return the attivitaIva
	 */
	public String getAttivitaIva() {
		String result = "";
		if(getSubdocumentoIva() != null && getSubdocumentoIva().getAttivitaIva() != null) {
			AttivitaIva ai = getSubdocumentoIva().getAttivitaIva();
			result = ai.getCodice() + ai.getDescrizione();
		}
		return result;
	}
	
	/**
	 * @return the registrazioneIva
	 */
	public String getRegistrazioneIva() {
		String result = "";
		if(getSubdocumentoIva() != null && getSubdocumentoIva().getProgressivoIVA() != null) {
			result = getSubdocumentoIva().getProgressivoIVA().toString();
		}
		return result;
	}
	
	/**
	 * @return the importoTotaleMovimentiIva
	 */
	public BigDecimal getImportoTotaleMovimentiIva() {
		BigDecimal result = BigDecimal.ZERO;
		if(getSubdocumentoIva() != null) {
			result = getSubdocumentoIva().calcolaTotaleMovimentiIva();
		}
		return result;
	}

}
