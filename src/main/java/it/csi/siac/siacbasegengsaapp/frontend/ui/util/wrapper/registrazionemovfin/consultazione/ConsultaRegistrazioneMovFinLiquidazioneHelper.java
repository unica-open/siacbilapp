/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * Helper per la consultazione dei dati dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinLiquidazioneHelper extends ConsultaRegistrazioneMovFinTransazioneElementareHelper<Liquidazione> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;

	private final Liquidazione liquidazione;
	
	/**
	 * Costruttore di wrap
	 * @param liquidazione la liquidazione
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinLiquidazioneHelper(Liquidazione liquidazione, boolean isGestioneUEB) {
		super(isGestioneUEB);
		this.liquidazione = liquidazione;
	}
	
	/**
	 * @return the liquidazione
	 */
	public Liquidazione getLiquidazione() {
		return this.liquidazione;
	}

	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return liquidazione != null ? liquidazione.getCapitoloUscitaGestione() : null;
	}

	@Override
	protected AttoAmministrativo ottieniAttoAmministrativo() {
		return liquidazione != null ? liquidazione.getAttoAmministrativoLiquidazione() : null;
	}
	
	/**
	 * @return the datiImpegno
	 */
	public String getDatiImpegno() {
		if(liquidazione == null || liquidazione.getImpegno() == null || liquidazione.getImpegno().getNumeroBigDecimal() == null) {
			return "";
		}
		return new StringBuilder()
			.append(liquidazione.getImpegno().getAnnoMovimento())
			.append(" / ")
			.append(liquidazione.getImpegno().getNumeroBigDecimal().toPlainString())
			.toString();
	}
	
	/**
	 * @return the numeroSubImpegno
	 */
	public String getNumeroSubImpegno() {
		if(liquidazione == null || liquidazione.getSubImpegno() == null) {
			return "";
		}
		return FormatUtils.formatPlain(liquidazione.getSubImpegno().getNumeroBigDecimal());
	}
	
	@Override
	public String getDatiCreazioneModifica() {
		if(liquidazione == null) {
			return "";
		}
		return calcolaDatiCreazioneModifica(liquidazione.getDataEmissioneLiquidazione(), liquidazione.getLoginOperazione(),
				liquidazione.getDataModifica(), liquidazione.getLoginModifica());
	}
	
}
