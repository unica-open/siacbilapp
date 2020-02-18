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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di wrap per il Subdocumento di Entrata con Iva.

 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 23/06/2014
 *
 */
public class ElementoSubdocumentoIvaEntrata extends SubdocumentoEntrata implements ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8922245154938908797L;
	
	private Boolean gestioneUEB;
	private Integer annoEsercizio;
	private CapitoloEntrataGestione capitoloEntrata;
	private List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
	
	/** Costruttore vuoto di default */
	public ElementoSubdocumentoIvaEntrata() {
		super();
	}
	
	/**
	 * Costruttore a partire dalla sottoclasse.
	 * 
	 * @param subdocumento  il subdocumento da wrappare
	 * @param annoEsercizio l'anno di esercizio del subdocumento
	 * @param gestioneUEB   se l'ente gestisce le UEB o meno
	 */
	public ElementoSubdocumentoIvaEntrata(SubdocumentoEntrata subdocumento, Integer annoEsercizio, Boolean gestioneUEB) {
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
	 * @return the capitoloEntrata
	 */
	public CapitoloEntrataGestione getCapitoloEntrata() {
		return capitoloEntrata;
	}

	/**
	 * @param capitoloEntrata the capitoloEntrata to set
	 */
	public void setCapitoloEntrata(CapitoloEntrataGestione capitoloEntrata) {
		this.capitoloEntrata = capitoloEntrata;
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
	 * @return the capitolo
	 */
	public String getCapitolo() {
		StringBuilder result = new StringBuilder();
		CapitoloEntrataGestione capitoloEntrataGestione = getCapitoloEntrata();
		if(capitoloEntrataGestione != null) {
			result.append(capitoloEntrataGestione.getAnnoCapitolo())
				.append("-")
				.append(capitoloEntrataGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloEntrataGestione.getNumeroArticolo());
			if(Boolean.TRUE.equals(getGestioneUEB())) {
				result.append("/").append(capitoloEntrataGestione.getNumeroUEB());
			}
		}
		return result.toString();
	}
	
	/**
	 * @return the movimento
	 */
	public String getMovimento() {
		StringBuilder result = new StringBuilder();
		Accertamento accertamento = getAccertamento();
		if(accertamento != null) {
			result.append(getAnnoEsercizio())
				.append("/")
				.append(accertamento.getAnnoMovimento())
				.append("/")
				.append(accertamento.getNumero().toPlainString());
			SubAccertamento subAccertamento = getSubAccertamento();
			if(subAccertamento != null) {
				result.append("-")
					.append(subAccertamento.getNumero().toPlainString());
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
			result = getSubdocumentoIva().getTotaleMovimentiIva();
		}
		return result;
	}

}
