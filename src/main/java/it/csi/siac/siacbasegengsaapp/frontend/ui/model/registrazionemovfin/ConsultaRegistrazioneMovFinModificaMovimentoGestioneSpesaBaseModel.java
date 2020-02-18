/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.math.BigDecimal;
import java.util.EnumSet;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubImpegnoRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper;
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
 * Classe base di model per la consultazione della Modifica del Movimento di Gestione di Spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/11/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaBaseModel extends ConsultaRegistrazioneMovFinMovimentoGestioneBaseModel<Impegno, SubImpegno, ElementoSubImpegnoRegistrazioneMovFin,
		ModificaMovimentoGestioneSpesa, ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin, ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9094467602549217043L;
	
	private Integer uid;

	/**
	 * @return the uid
	 */
	public Integer getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@Override
	public String getConsultazioneSubpath() {
		return "ModificaMovimentoGestioneSpesa";
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
		// carico i sub sempre essendo una consultazione
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
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO));
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
		if (getNumeroSub() != null && !getNumeroSub().equals(BigDecimal.ZERO)) {
			utility.setNumeroSubDaCercare(getNumeroSub());
		}
		return utility;
	}


	@Override
	protected String ottieniDenominazioneMovimentoGestione() {
		return "Modifica Movimento Gestione Spesa";
	}
	
	@Override
	public String getStato() {
		if(consultazioneHelper.getMovimentoGestione() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Stato: ")
			.append(consultazioneHelper.getModificaMovimentoGestioneSpesa().getStatoOperativoModificaMovimentoGestione())
			.append(" dal ")
			.append(FormatUtils.formatDate(consultazioneHelper.getModificaMovimentoGestioneSpesa().getDataModificaMovimentoGestione()))
			.toString();
	}
	
}
