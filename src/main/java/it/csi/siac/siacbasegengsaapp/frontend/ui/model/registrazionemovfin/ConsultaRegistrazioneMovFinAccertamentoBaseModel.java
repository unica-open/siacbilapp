/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.EnumSet;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubAccertamentoRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinAccertamentoHelper;
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
 * Classe base di model per la consultazione dell'accertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinAccertamentoBaseModel extends ConsultaRegistrazioneMovFinMovimentoGestioneBaseModel<Accertamento, SubAccertamento, ElementoSubAccertamentoRegistrazioneMovFin,
		ModificaMovimentoGestioneEntrata, ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin, ConsultaRegistrazioneMovFinAccertamentoHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8660196779235411777L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "Accertamento";
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
		request.setCaricaSub(true);
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

		return utility;
	}
	
	@Override
	protected String ottieniDenominazioneMovimentoGestione() {
		return "Accertamento";
	}
	
	@Override
	public String getStato() {
		if(consultazioneHelper.getMovimentoGestione() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Stato: ")
			.append(consultazioneHelper.getMovimentoGestione().getDescrizioneStatoOperativoMovimentoGestioneEntrata())
			.append(" dal ")
			.append(FormatUtils.formatDate(consultazioneHelper.getMovimentoGestione().getDataStatoOperativoMovimentoGestioneEntrata()))
			.toString();
	}
	
}
