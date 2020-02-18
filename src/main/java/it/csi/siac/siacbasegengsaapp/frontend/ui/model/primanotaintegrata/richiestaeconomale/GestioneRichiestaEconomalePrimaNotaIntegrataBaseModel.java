/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRichiestaEconomaleHelper;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sualla richiesta Economale.
 * 
 * @author Simona Paggio 
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<RichiestaEconomale, ConsultaRegistrazioneMovFinRichiestaEconomaleHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4871490745313821173L;
	
	private RichiestaEconomale richiestaEconomale;

	/**
	 * @return the richiestaEconomale
	 */
	public RichiestaEconomale getRichiestaEconomale() {
		return richiestaEconomale;
	}

	/**
	 * @param richiestaEconomale the richiestaEconomale to set
	 */
	public void setRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		this.richiestaEconomale = richiestaEconomale;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "RichiestaEconomale";
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRichiestaEconomale}.
	 * 
	 * @param richiesta la richiesta economale per cui creare la request
	 * @return la request creata
	 */
	public RicercaDettaglioRichiestaEconomale creaRequestRicercaDettaglioRichiestaEconomale(RichiestaEconomale richiesta) {
		RicercaDettaglioRichiestaEconomale req = creaRequest(RicercaDettaglioRichiestaEconomale.class);
		
		req.setRichiestaEconomale(richiesta);
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la classeDiConciliazione per cui ricercare i conti di conciliazione
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione());
		req.setSoggetto(getRichiestaEconomale().getImpegno().getSoggetto());
		return req;
	}

}
