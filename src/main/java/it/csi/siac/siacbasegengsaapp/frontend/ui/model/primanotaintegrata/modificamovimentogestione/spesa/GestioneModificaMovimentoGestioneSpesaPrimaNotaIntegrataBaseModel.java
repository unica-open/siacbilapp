/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.spesa;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.MovimentoGestioneHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sulla modifica del movimento di gestione di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 18/11/2015
 */
public abstract class GestioneModificaMovimentoGestioneSpesaPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Impegno, ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4094484863433488410L;
	
	private Impegno impegno;
	private SubImpegno subImpegno;
	private ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa;
	
	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * @return the modificaMovimentoGestioneSpesa
	 */
	public ModificaMovimentoGestioneSpesa getModificaMovimentoGestioneSpesa() {
		return modificaMovimentoGestioneSpesa;
	}

	/**
	 * @param modificaMovimentoGestioneSpesa the modificaMovimentoGestioneSpesa to set
	 */
	public void setModificaMovimentoGestioneSpesa(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		this.modificaMovimentoGestioneSpesa = modificaMovimentoGestioneSpesa;
	}

	@Override
	public String getConsultazioneSubpath() {
		return "ModificaMovimentoGestioneSpesa";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @param mmgs il subimpegno per cui creare la request
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(ModificaMovimentoGestioneSpesa mmgs) {
		SubImpegno subImpegnoModifica = mmgs.getSubImpegno();
		
		if(subImpegnoModifica != null){
			Impegno impegnoPadre = new Impegno();
			impegnoPadre.setAnnoMovimento(subImpegnoModifica.getAnnoImpegnoPadre());
			impegnoPadre.setNumero(subImpegnoModifica.getNumeroImpegnoPadre());
			return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), impegnoPadre, subImpegnoModifica);
		}
		
		return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), mmgs.getImpegno());
	
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
		req.setCapitolo(getImpegno().getCapitoloUscitaGestione());
		req.setSoggetto(getImpegno().getSoggetto());
		return req;
	}

}


