/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

/**
 * Classe di model per la consultazione dell'Ordinativo di Pagamento
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel extends ConsultaRegistrazioneMovFinOrdinativoBaseModel<OrdinativoPagamento, SubOrdinativoPagamento, ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8597207740352000010L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "OrdinativoPagamento";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaOrdinativoPagamentoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaOrdinativoPagamentoPerChiave creaRequestRicercaOrdinativoPagamentoPerChiave() {
		RicercaOrdinativoPagamentoPerChiave req = new RicercaOrdinativoPagamentoPerChiave();
		req.setRichiedente(getRichiedente());
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		pRicercaOrdinativoPagamentoK.setBilancio(getBilancio());
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		ordinativoPagamento.setNumero(getNumero());
		ordinativoPagamento.setAnno(getAnno());
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);
		req.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		req.setEnte(getEnte());
		return req;
	}

}
