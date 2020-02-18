/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinBaseHelper;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.EntitaExt;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Consultazione della registrazione per il delle entit&agrave; collegate alla Registrazione MovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <E> la tipizzazione dell'entit&agrave; da consultare
 * @param <H> la tipizzazione dell'helper
 *
 */
public abstract class ConsultaRegistrazioneMovFinBaseModel<E extends Entita, H extends ConsultaRegistrazioneMovFinBaseHelper<E>> extends GenericBilancioModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 869632513277753627L;
	
	/** L'helper per la consultazione */
	protected H consultazioneHelper;
	
	/**
	 * @return the consultazioneHelper
	 */
	public H getConsultazioneHelper() {
		return this.consultazioneHelper;
	}

	/**
	 * @param consultazioneHelper the consultazioneHelper to set
	 */
	public void setConsultazioneHelper(H consultazioneHelper) {
		this.consultazioneHelper = consultazioneHelper;
	}

	/**
	 * @return the intestazione
	 */
	public abstract String getIntestazione();
	
	/**
	 * @return the stato
	 */
	public abstract String getStato();
	
	/**
	 * @return the datiCreazioneModifica
	 */
	public abstract String getDatiCreazioneModifica();
	
	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * @return the consultazioneSubpath
	 */
	public abstract String getConsultazioneSubpath();
	

	/**
	 * Calcolo dei dati di creazione e di modifica.
	 * 
	 * @param entitaExt l'entita estesa da cui ottenere i dati
	 * @return una stringa corrispondente ai dati di creazione e modifica
	 */
	protected String calcolaDatiCreazioneModifica(EntitaExt entitaExt) {
		return calcolaDatiCreazioneModifica(entitaExt.getDataCreazione(), entitaExt.getLoginCreazione(), entitaExt.getDataModifica(), entitaExt.getLoginModifica());
	}
	
	/**
	 * Calcolo dei dati di creazione e di modifica.
	 * 
	 * @param dataCreazione  la data di creazione dell'entita
	 * @param loginCreazione il login di creazione dell'entita
	 * @param dataModifica   la data di ultima modifica dell'entita
	 * @param loginModifica  il login di ultima modifica dell'entita
	 * 
	 * @return una stringa corrispondente ai dati di creazione e modifica
	 */
	protected String calcolaDatiCreazioneModifica(Date dataCreazione, String loginCreazione, Date dataModifica, String loginModifica) {
		return new StringBuilder()
			.append("Inserimento: ")
			.append(FormatUtils.formatDate(dataCreazione))
			.append(" (")
			.append(loginCreazione != null ? loginCreazione : "")
			.append(") - Ultima modifica: ")
			.append(FormatUtils.formatDate(dataModifica))
			.append(" (")
			.append(loginModifica != null ? loginModifica : "")
			.append(")")
			.toString();
	}

	/**
	 * Calcolo dei dati del soggetto.
	 * 
	 * @param soggetto il soggetto da cui ottenere i dati
	 * 
	 * @return i dati del soggetto
	 */
	protected String calcolaDatiSoggetto(Soggetto soggetto) {
		List<String> chunks = new ArrayList<String>();
		
		if(StringUtils.isNotBlank(soggetto.getCodiceSoggetto())) {
			chunks.add(soggetto.getCodiceSoggetto());
		}
		if(StringUtils.isNotBlank(soggetto.getDenominazione())) {
			chunks.add(soggetto.getDenominazione());
		}
		if(StringUtils.isNotBlank(soggetto.getCodiceFiscale())) {
			chunks.add("CF: " + soggetto.getCodiceFiscale());
		}
		if(StringUtils.isNotBlank(soggetto.getPartitaIva())) {
			chunks.add("P.IVA: " + soggetto.getPartitaIva());
		}
		
		return StringUtils.join(chunks, " - ");
	}
}
