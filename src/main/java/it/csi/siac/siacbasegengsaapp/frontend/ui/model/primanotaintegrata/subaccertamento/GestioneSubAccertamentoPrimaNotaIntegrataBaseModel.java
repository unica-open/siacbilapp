/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento;

import java.util.EnumSet;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinSubAccertamentoHelper;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sul subaccertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public abstract class GestioneSubAccertamentoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Accertamento, ConsultaRegistrazioneMovFinSubAccertamentoHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 7442732307688078658L;
	
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	
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
	
	@Override
	public String getConsultazioneSubpath() {
		return "SubAccertamento";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * @param subacc il subaccertamento da ricercare
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(SubAccertamento subacc) {
		RicercaAccertamentoPerChiaveOttimizzato req = creaRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		
		RicercaAccertamentoK ricercaAccertamentoK = new RicercaAccertamentoK();
		ricercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaAccertamentoK.setAnnoAccertamento(subacc.getAnnoAccertamentoPadre());
		ricercaAccertamentoK.setNumeroAccertamento(subacc.getNumeroAccertamentoPadre());
		ricercaAccertamentoK.setNumeroSubDaCercare(subacc.getNumeroBigDecimal());
		
		req.setpRicercaAccertamentoK(ricercaAccertamentoK);
		req.setEnte(getEnte());
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 
		
		req.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
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
