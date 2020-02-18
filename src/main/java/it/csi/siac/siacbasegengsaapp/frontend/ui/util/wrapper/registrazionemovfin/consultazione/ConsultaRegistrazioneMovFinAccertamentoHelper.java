/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubAccertamentoRegistrazioneMovFin;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

/**
 * Helper per la consultazione dei dati dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinAccertamentoHelper extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<Accertamento, SubAccertamento, ModificaMovimentoGestioneEntrata,
		ElementoSubAccertamentoRegistrazioneMovFin, ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;

	/**
	 * Costruttore di wrap
	 * @param accertamento l'accertamento
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinAccertamentoHelper(Accertamento accertamento, boolean isGestioneUEB) {
		super(accertamento, isGestioneUEB);
		
		wrapSubMovimentoGestione();
		wrapModificaMovimentoGestione();
	}
	
	@Override
	protected void wrapSubMovimentoGestione() {
		if(movimentoGestione == null || movimentoGestione.getElencoSubAccertamenti() == null) {
			return;
		}
		for(SubAccertamento sa : movimentoGestione.getElencoSubAccertamenti()) {
			ElementoSubAccertamentoRegistrazioneMovFin wrapper = new ElementoSubAccertamentoRegistrazioneMovFin(sa);
			listaSubMovimentoGestione.add(wrapper);
		}
	}

	@Override
	protected void wrapModificaMovimentoGestione() {
		if(movimentoGestione == null || movimentoGestione.getListaModificheMovimentoGestioneEntrata() == null) {
			return;
		}
		for(ModificaMovimentoGestioneEntrata mmge : movimentoGestione.getListaModificheMovimentoGestioneEntrata()) {
			ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin wrapper = new ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin(mmge);
			listaModificaMovimentoGestione.add(wrapper);
		}
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return movimentoGestione != null ? movimentoGestione.getCapitoloEntrataGestione() : null;
	}
	
}
