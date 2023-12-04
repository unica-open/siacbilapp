/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata;

import java.util.EnumSet;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sulla modifica del movimento di gestione di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 18/11/2015
 */
public abstract class GestioneModificaMovimentoGestioneEntrataPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Accertamento, ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4094484863433488410L;
	
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	private ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata;
	
	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/**
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}

	/**
	 * @param subAccertamento the subAccertamento to set
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}

	/**
	 * @return the modificaMovimentoGestioneEntrata
	 */
	public ModificaMovimentoGestioneEntrata getModificaMovimentoGestioneEntrata() {
		return modificaMovimentoGestioneEntrata;
	}

	/**
	 * @param modificaMovimentoGestioneEntrata the modificaMovimentoGestioneEntrata to set
	 */
	public void setModificaMovimentoGestioneEntrata(
			ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		this.modificaMovimentoGestioneEntrata = modificaMovimentoGestioneEntrata;
	}

	@Override
	public String getConsultazioneSubpath() {
		return "ModificaMovimentoGestioneEntrata";
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * 
	 * @param mmge il subimpegno per cui creare la request
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(ModificaMovimentoGestioneEntrata mmge) {
		RicercaAccertamentoPerChiaveOttimizzato req = creaRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		req.setSubPaginati(true);
		req.setCaricaSub(mmge.getSubAccertamento() != null && mmge.getSubAccertamento().getUid() != 0);
		req.setEnte(getEnte());
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		RicercaAccertamentoK ricercaAccertamentoK = new RicercaAccertamentoK();
		ricercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
		if(mmge.getAccertamento() != null){
			ricercaAccertamentoK.setAnnoAccertamento(mmge.getAccertamento().getAnnoMovimento());
			ricercaAccertamentoK.setNumeroAccertamento(mmge.getAccertamento().getNumeroBigDecimal());
		}else if(mmge.getSubAccertamento() != null){
			ricercaAccertamentoK.setAnnoAccertamento(mmge.getSubAccertamento().getAnnoAccertamentoPadre());
			ricercaAccertamentoK.setNumeroAccertamento(mmge.getSubAccertamento().getNumeroAccertamentoPadre());
			ricercaAccertamentoK.setNumeroSubDaCercare(mmge.getSubAccertamento().getNumeroBigDecimal());
			
		}
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 
		req.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		req.setpRicercaAccertamentoK(ricercaAccertamentoK);
		
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
		req.setCapitolo(getAccertamento().getCapitoloEntrataGestione());
		req.setSoggetto(getAccertamento().getSoggetto());
		return req;
	}

}


