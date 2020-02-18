/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;


/**
 * Classe di model per la consultazione dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 */
public class ConsultaAnticipoSpesePerMissioneCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1382056510121783654L;

	/** Costruttore vuoto di default */
	public ConsultaAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Consultazione anticipo spese per missione");
	}
	/**
	 * @return the totaleImportiGiustificativi
	 */
	public BigDecimal getTotaleImportiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		if (getRichiestaEconomale() != null && !getRichiestaEconomale().getGiustificativi().isEmpty()) {
			for (Giustificativo g : getRichiestaEconomale().getGiustificativi()) {
				if (g.getImportoGiustificativo() != null) {
					totale = totale.add(g.getImportoGiustificativo());
				}
			}
		}
		return totale;
	}
	
	/**
	 * @return the totaleImportiSpettantiGiustificativi
	 */
	public BigDecimal getTotaleImportiSpettantiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		if (getRichiestaEconomale() != null && !getRichiestaEconomale().getGiustificativi().isEmpty()) {
			for (Giustificativo g : getRichiestaEconomale().getGiustificativi()) {
				if (g.getImportoGiustificativo() != null) {
					totale = totale.add(g.getImportoSpettanteAnticipoMissioneNotNull());
				}
			}
		}
		return totale;
	}
	
	@Override
	protected String computaStringaSospeso(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale == null || richiestaEconomale.getSospeso() == null) {
			return "";
		}
		return " - Sospeso N. " + richiestaEconomale.getSospeso().getNumeroSospeso();
	}
	
	@Override
	public String getDatiMatricola() {
		if(getRichiestaEconomale() == null) {
			return "";
		}
		if(getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getMatricola());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getMatricola());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getNome());
		if (!isGestioneHR()) {
			//hr mette tutti i dati in un solo campo
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCognome());
		}
		return StringUtils.join(chunks, " - ");
	}
}
