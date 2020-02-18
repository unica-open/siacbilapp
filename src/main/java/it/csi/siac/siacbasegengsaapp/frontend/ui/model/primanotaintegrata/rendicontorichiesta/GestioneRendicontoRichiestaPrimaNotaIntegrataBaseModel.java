/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.rendicontorichiesta;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRendicontoRichiestaHelper;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sul rendiconto richiesta .
 * 
 * @author Simona Paggio 
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneRendicontoRichiestaPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<RendicontoRichiesta, ConsultaRegistrazioneMovFinRendicontoRichiestaHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4871490745313821173L;
	
	private RendicontoRichiesta rendicontoRichiesta;
	
	/**
	 * @return the rendicontoRichiesta
	 */
	public RendicontoRichiesta getRendicontoRichiesta() {
		return rendicontoRichiesta;
	}

	/**
	 * @param rendicontoRichiesta the rendicontoRichiesta to set
	 */
	public void setRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		this.rendicontoRichiesta = rendicontoRichiesta;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "RendicontoRichiesta";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRendicontoRichiesta}.
	 * 
	 * @param rendiconto il rendiconto richiesta per cui creare la request
	 * @return la request creata
	 */
	public RicercaDettaglioRendicontoRichiesta creaRequestRicercaDettaglioRendicontoRichiesta(RendicontoRichiesta rendiconto) {
		RicercaDettaglioRendicontoRichiesta req = creaRequest(RicercaDettaglioRendicontoRichiesta.class);
		
		req.setRendicontoRichiesta(rendiconto);
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
		req.setCapitolo(getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione());
		req.setSoggetto(getRendicontoRichiesta().getImpegno().getSoggetto());
		return req;
	}

}


