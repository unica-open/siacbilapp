/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.TransazioneElementare;

/**
 * Helper per la consultazione dei dati del movimento di gestione
 * @author Marchino Alessandro
 * @param <TE> la tipizzazione della transazione elementare
 *
 */
public abstract class ConsultaRegistrazioneMovFinTransazioneElementareHelper<TE extends TransazioneElementare> extends ConsultaRegistrazioneMovFinBaseHelper<TE> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	
	/**
	 * Costruttore di wrap
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	protected ConsultaRegistrazioneMovFinTransazioneElementareHelper(boolean isGestioneUEB) {
		super(isGestioneUEB);
	}
	
	/**
	 * @return the datiBaseCapitolo
	 */
	public String getDatiBaseCapitolo() {
		return calcolaDatiCapitolo(ottieniCapitolo());
	}
	
	/**
	 * @return the datiBaseStrutturaAmministrativoContabileCapitolo
	 */
	public String getDatiBaseStrutturaAmministrativoContabileCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getStrutturaAmministrativoContabile() == null) {
			return "";
		}
		return new StringBuilder()
			.append(capitolo.getStrutturaAmministrativoContabile().getCodice())
			.append(" - ")
			.append(capitolo.getStrutturaAmministrativoContabile().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiBaseTipoFinanziamentoCapitolo
	 */
	public String getDatiBaseTipoFinanziamentoCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getTipoFinanziamento() == null) {
			return "";
		}
		return new StringBuilder()
			.append(capitolo.getTipoFinanziamento().getCodice())
			.append(" - ")
			.append(capitolo.getTipoFinanziamento().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiAttoAmministrativo
	 */
	public String getDatiAttoAmministrativo() {
		AttoAmministrativo aa = ottieniAttoAmministrativo();
		if(aa == null) {
			return "";
		}
		return new StringBuilder()
			.append(aa.getAnno())
			.append(" - ")
			.append(aa.getNumero())
			.toString();
	}
	
	/**
	 * @return the datiStrutturaAmministrativoContabile
	 */
	public String getDatiStrutturaAmministrativoContabile() {
		AttoAmministrativo aa = ottieniAttoAmministrativo();
		if(aa == null || aa.getStrutturaAmmContabile() == null) {
			return "";
		}
		return new StringBuilder()
			.append(aa.getStrutturaAmmContabile().getCodice())
			.append(" - ")
			.append(aa.getStrutturaAmmContabile().getDescrizione())
			.toString();
	}
	
	/**
	 * Recupera il capitolo
	 * @return il capitolo
	 */
	protected abstract Capitolo<?, ?> ottieniCapitolo();
	
	/**
	 * Recupare il provvedimento
	 * @return il provvedimento
	 */
	protected abstract AttoAmministrativo ottieniAttoAmministrativo();
	
}
