/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.EnumSet;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubImpegnoRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinImpegnoHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * Classe base di model per la consultazione dell'Impegno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinImpegnoBaseModel extends ConsultaRegistrazioneMovFinMovimentoGestioneBaseModel<Impegno, SubImpegno, ElementoSubImpegnoRegistrazioneMovFin,
		ModificaMovimentoGestioneSpesa, ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin, ConsultaRegistrazioneMovFinImpegnoHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1294948158973919518L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "Impegno";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setpRicercaImpegnoK(creaPRicercaImpegnoK());
		//carico i sub sempre essendo una consultazione
		request.setCaricaSub(true);
		request.setSubPaginati(true);
		request.setEscludiSubAnnullati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO, TipologiaClassificatore.MACROAGGREGATO));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
	
	/**
	 * Crea un'utilit&agrave; per la ricerca impegno.
	 * 
	 * @return l'utility creata
	 */
	private RicercaImpegnoK creaPRicercaImpegnoK() {
		RicercaImpegnoK utility = new RicercaImpegnoK();

		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoImpegno(getAnno());
		utility.setNumeroImpegno(getNumero());
		return utility;
	}

	@Override
	protected String ottieniDenominazioneMovimentoGestione() {
		return "Impegno";
	}
	
	@Override
	public String getStato() {
		if(consultazioneHelper.getMovimentoGestione() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Stato: ")
			.append(consultazioneHelper.getMovimentoGestione().getDescrizioneStatoOperativoMovimentoGestioneSpesa())
			.append(" dal ")
			.append(FormatUtils.formatDate(consultazioneHelper.getMovimentoGestione().getDataStatoOperativoMovimentoGestioneSpesa()))
			.toString();
	}
	
}
