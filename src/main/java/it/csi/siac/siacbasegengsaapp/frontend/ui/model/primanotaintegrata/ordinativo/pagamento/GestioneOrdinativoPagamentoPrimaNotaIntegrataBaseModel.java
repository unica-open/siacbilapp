/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe di modello per la prima nota integrata sull'ordinativo di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<OrdinativoPagamento, ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper>{

	/** per serializzazione **/
	private static final long serialVersionUID = -5269780085950750799L;
	
	private OrdinativoPagamento ordinativoPagamento;
	
	/**
	 * @return the ordinativoPagamento
	 */
	public OrdinativoPagamento getOrdinativoPagamento() {
		return ordinativoPagamento;
	}

	/**
	 * @param ordinativoPagamento the ordinativoPagamento to set
	 */
	public void setOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
		this.ordinativoPagamento = ordinativoPagamento;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "OrdinativoPagamento";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaOrdinativoPagamentoPerChiave}.
	 * @param ordinativo l'ordinativo per cui creare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaOrdinativoPagamentoPerChiave creaRequestRicercaOrdinativoPagamentoPerChiave(Ordinativo ordinativo) {
		RicercaOrdinativoPagamentoPerChiave req = creaRequest(RicercaOrdinativoPagamentoPerChiave.class);
		
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		pRicercaOrdinativoPagamentoK.setBilancio(getBilancio());
		OrdinativoPagamento ordinativoPag = new OrdinativoPagamento();
		ordinativoPag.setNumero(ordinativo.getNumero());
		ordinativoPag.setAnno(ordinativo.getAnno());
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPag);
		
		req.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		req.setEnte(getEnte());
		return req;
	
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la {@link ClasseDiConciliazione} da ricercare
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getOrdinativoPagamento().getCapitoloUscitaGestione());
		req.setSoggetto(getOrdinativoPagamento().getSoggetto());
		return req;
	}

}
