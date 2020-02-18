/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubMovimentoGestioneRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinMovimentoGestioneHelper;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;

/**
 * Classe di model per la consultazione del movimento di gestione (impegno/accertamento).
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 07/10/2015
 * @param <MG>   la tipizzazione del movimento di gestione
 * @param <SMG>  la tipizzazione del submovimento di gestione
 * @param <WSMG> la tipizzazione del wrapper la il submovimento di gestione
 * @param <MMG>  la tipizzazione delle modifiche del movimento di gestione
 * @param <WMMG> la tipizzazione del wrapper delle modifiche del movimento di gestione
 * @param <H>    la tipizzazione dell'helper
 *
 */
public abstract class ConsultaRegistrazioneMovFinMovimentoGestioneBaseModel<MG extends MovimentoGestione,
		SMG extends MG, WSMG extends ElementoSubMovimentoGestioneRegistrazioneMovFin<SMG>,
		MMG extends ModificaMovimentoGestione, WMMG extends ElementoModificaMovimentoGestioneRegistrazioneMovFin<MMG>,
		H extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<MG, SMG, MMG, WSMG, WMMG>> extends ConsultaRegistrazioneMovFinTransazioneElementareBaseModel<MG, H> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3199728226051714175L;
	
	private BigDecimal numero;
	private Integer anno;
	private BigDecimal numeroSub;
	
	/**
	 * @return the numero
	 */
	public BigDecimal getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(BigDecimal numero) {
		this.numero = numero;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @return the numeroSub
	 */
	public BigDecimal getNumeroSub() {
		return numeroSub;
	}

	/**
	 * @param numeroSub the numeroSub to set
	 */
	public void setNumeroSub(BigDecimal numeroSub) {
		this.numeroSub = numeroSub;
	}

	@Override
	public String getIntestazione() {
		StringBuilder sb = new StringBuilder()
			.append(ottieniDenominazioneMovimentoGestione())
			.append(" ")
			.append(getAnno())
			.append("/")
			.append(getNumero().toPlainString());
		if(getNumeroSub() != null) {
			sb.append("-")
				.append(getNumeroSub().toPlainString());
		}
		return sb.toString();
	}

	/**
	 * Ottiene la denominazione del movimento di gestione (Impegno/Accertamento).
	 * 
	 * @return la denominazione
	 */
	protected abstract String ottieniDenominazioneMovimentoGestione();
	
}
