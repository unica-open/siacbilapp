/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.math.BigDecimal;
import java.util.EnumSet;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubAccertamentoRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
 * Classe base di model per la consultazione della Modifica del Movimento di Gestione di Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/11/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataBaseModel extends ConsultaRegistrazioneMovFinMovimentoGestioneBaseModel<Accertamento, SubAccertamento, ElementoSubAccertamentoRegistrazioneMovFin,
		ModificaMovimentoGestioneEntrata, ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin, ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2378518502838398860L;
	
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
		return "ModificaMovimentoGestioneEntrata";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		request.setpRicercaAccertamentoK(creaPRicercaAccertamentoK());
		request.setCaricaSub(getNumeroSub() != null && !getNumeroSub().equals(BigDecimal.ZERO));
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
    
    /**
	 * Crea un'utilit&agrave; per la ricerca impegno.
	 * 
	 * @return l'utility creata
	 */
	private RicercaAccertamentoK creaPRicercaAccertamentoK() {
		RicercaAccertamentoK utility = new RicercaAccertamentoK();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoAccertamento(getAnno());
		utility.setNumeroAccertamento(getNumero());
		
		//riga in piu rispetto alla vecchia versione 
		if (getNumeroSub() != null && !getNumeroSub().equals(BigDecimal.ZERO)) {
			utility.setNumeroSubDaCercare(getNumeroSub());
		}
		return utility;
	}
	
	@Override
	protected String ottieniDenominazioneMovimentoGestione() {
		return "Modifica Movimento Gestione Entrata";
	}
	
	@Override
	public String getStato() {
		if(consultazioneHelper.getMovimentoGestione() == null) {
			return "";
		}
		return new StringBuilder()
		.append("Stato: ")
		.append(consultazioneHelper.getModificaMovimentoGestioneEntrata().getStatoOperativoModificaMovimentoGestione())
		.append(" dal ")
		.append(FormatUtils.formatDate(consultazioneHelper.getModificaMovimentoGestioneEntrata().getDataModificaMovimentoGestione()))
		.toString();
	}
	
}
