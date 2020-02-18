/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubImpegnoRegistrazioneMovFin;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Helper per la consultazione dei dati della modifica dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<Impegno, SubImpegno, ModificaMovimentoGestioneSpesa,
		ElementoSubImpegnoRegistrazioneMovFin, ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;
	
	private final SubImpegno subImpegno;
	private final ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa;
	private final Soggetto soggetto;

	/**
	 * Costruttore di wrap
	 * @param impegno l'impegno
	 * @param subImpegno il subimpegno
	 * @param modificaMovimentoGestioneSpesa la modifica al movimento di gestione
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper(Impegno impegno, SubImpegno subImpegno, ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa, boolean isGestioneUEB) {
		super(impegno, isGestioneUEB);
		this.subImpegno = subImpegno;
		this.modificaMovimentoGestioneSpesa = modificaMovimentoGestioneSpesa;
		
		this.soggetto = subImpegno != null && subImpegno.getSoggetto() != null && subImpegno.getSoggetto().getUid() != 0 ? subImpegno.getSoggetto() : impegno.getSoggetto();
		wrapSubMovimentoGestione();
		wrapModificaMovimentoGestione();
	}
	
	/**
	 * @return the modificaMovimentoGestioneSpesa
	 */
	public ModificaMovimentoGestioneSpesa getModificaMovimentoGestioneSpesa() {
		return this.modificaMovimentoGestioneSpesa;
	}

	@Override
	protected void wrapSubMovimentoGestione() {
		// TODO: impostare qualcosa?
	}

	@Override
	protected void wrapModificaMovimentoGestione() {
		if(modificaMovimentoGestioneSpesa == null) {
			return;
		}
		listaModificaMovimentoGestione.add(new ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin(modificaMovimentoGestioneSpesa));
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return movimentoGestione != null ? movimentoGestione.getCapitoloUscitaGestione() : null;
	}

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return this.subImpegno;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return this.soggetto;
	}
	
}
