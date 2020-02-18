/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubMovimentoGestioneRegistrazioneMovFin;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;

/**
 * Helper per la consultazione dei dati del movimento di gestione
 * @author Marchino Alessandro
 * @param <MG> la tipizzazione del movimento di gestione
 * @param <SMG> la tipizzazione del submovimento di gestione
 * @param <MMG> la tipizzazione della modifica del movimento di gestione
 * @param <WSMG> la tipizzazione del wrapper del submovimento di gestione
 * @param <WMMG> la tipizzazione del wrapper della modifica del movimento di gestione
 *
 */
public abstract class ConsultaRegistrazioneMovFinMovimentoGestioneHelper<MG extends MovimentoGestione, SMG extends MG, MMG extends ModificaMovimentoGestione,
		WSMG extends ElementoSubMovimentoGestioneRegistrazioneMovFin<SMG>, WMMG extends ElementoModificaMovimentoGestioneRegistrazioneMovFin<MMG>>
	extends ConsultaRegistrazioneMovFinTransazioneElementareHelper<MG> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	
	/** Il movimento di gestione */
	protected final MG movimentoGestione;
	/** La lista dei submovimenti di gestione */
	protected final List<WSMG> listaSubMovimentoGestione;
	/** La lista delle modifiche */
	protected final List<WMMG> listaModificaMovimentoGestione;
	
	/**
	 * Costruttore di wrap
	 * @param movimentoGestione l'impegno
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	protected ConsultaRegistrazioneMovFinMovimentoGestioneHelper(MG movimentoGestione, boolean isGestioneUEB) {
		super(isGestioneUEB);
		this.movimentoGestione = movimentoGestione;
		
		this.listaSubMovimentoGestione = new ArrayList<WSMG>();
		this.listaModificaMovimentoGestione = new ArrayList<WMMG>();
	}
	
	/**
	 * @return the listaSubMovimentoGestione
	 */
	public List<WSMG> getListaSubMovimentoGestione() {
		return this.listaSubMovimentoGestione;
	}

	/**
	 * @return the listaModificaMovimentoGestione
	 */
	public List<WMMG> getListaModificaMovimentoGestione() {
		return this.listaModificaMovimentoGestione;
	}

	/**
	 * @return the movimentoGestione
	 */
	public MG getMovimentoGestione() {
		return this.movimentoGestione;
	}
	
	@Override
	protected AttoAmministrativo ottieniAttoAmministrativo() {
		return movimentoGestione != null ? movimentoGestione.getAttoAmministrativo() : null;
	}
	
	/**
	 * @return the movimentoOrigine
	 */
	public String getMovimentoOrigine() {
		if(movimentoGestione == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		if(movimentoGestione.getAnnoOriginePlur() != 0) {
			chunks.add(Integer.toString(movimentoGestione.getAnnoOriginePlur()));
		}
		if(movimentoGestione.getNumeroOriginePlur() != null) {
			chunks.add(movimentoGestione.getNumeroOriginePlur().toPlainString());
		}
		
		return StringUtils.join(chunks, " / ");
	}
	
	/**
	 * @return the daRiaccertamento
	 */
	public String isDaRiaccertamento() {
		return movimentoGestione != null && movimentoGestione.isFlagDaRiaccertamento()
			? "S&Iacute;"
			: "NO";
	}
	
	/**
	 * Checks if is soggetto durc.
	 *
	 * @return the string
	 */
	public String isSoggettoDurc() {
		return movimentoGestione != null && movimentoGestione.isFlagSoggettoDurc()
			? "S&Iacute;"
			: "NO";
	}
	
	/**
	 * @return the annoRiaccertato
	 */
	public Integer getAnnoRiaccertato() {
		return movimentoGestione != null && movimentoGestione.getAnnoRiaccertato() != 0
			? Integer.valueOf(movimentoGestione.getAnnoRiaccertato())
			: null;
	}
	
	/**
	 * @return the numeroRiaccertato
	 */
	public String getNumeroRiaccertato() {
		return movimentoGestione != null ? FormatUtils.formatPlain(movimentoGestione.getNumeroRiaccertato()) : "";
	}
	
	@Override
	public String getDatiCreazioneModifica() {
		if(movimentoGestione == null) {
			return "";
		}
		return calcolaDatiCreazioneModifica(
				movimentoGestione.getDataEmissione(),
				movimentoGestione.getUtenteCreazione(),
				movimentoGestione.getDataModifica(),
				movimentoGestione.getUtenteModifica());
	}
	
	/**
	 * Wrap del submovimento di gestione
	 */
	protected abstract void wrapSubMovimentoGestione();
	/**
	 * Wrap della modifica del movimento gestione
	 */
	protected abstract void wrapModificaMovimentoGestione();
	
}
