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
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Helper per la consultazione dei dati dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<Accertamento, SubAccertamento, ModificaMovimentoGestioneEntrata,
		ElementoSubAccertamentoRegistrazioneMovFin, ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;
	
	private final SubAccertamento subAccertamento;
	private final ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata;
	private final Soggetto soggetto;

	/**
	 * Costruttore di wrap
	 * @param accertamento l'accertamento
	 * @param subAccertamento il subaccertamento
	 * @param modificaMovimentoGestioneEntrata la modifica al movimento di gestione
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper(Accertamento accertamento, SubAccertamento subAccertamento, ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata, boolean isGestioneUEB) {
		super(accertamento, isGestioneUEB);
		this.subAccertamento = subAccertamento;
		this.modificaMovimentoGestioneEntrata = modificaMovimentoGestioneEntrata;
		
		this.soggetto = subAccertamento != null && subAccertamento.getSoggetto() != null && subAccertamento.getSoggetto().getUid() != 0 ? subAccertamento.getSoggetto() : accertamento.getSoggetto();
		wrapSubMovimentoGestione();
		wrapModificaMovimentoGestione();
	}
	
	/**
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return this.subAccertamento;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return this.soggetto;
	}

	/**
	 * @return the modificaMovimentoGestioneEntrata
	 */
	public ModificaMovimentoGestioneEntrata getModificaMovimentoGestioneEntrata() {
		return this.modificaMovimentoGestioneEntrata;
	}

	@Override
	protected void wrapSubMovimentoGestione() {
		// TODO: impostare qualcosa?
	}

	@Override
	protected void wrapModificaMovimentoGestione() {
		if(modificaMovimentoGestioneEntrata == null) {
			return;
		}
		listaModificaMovimentoGestione.add(new ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin(modificaMovimentoGestioneEntrata));
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return movimentoGestione != null ? movimentoGestione.getCapitoloEntrataGestione() : null;
	}
	
}
