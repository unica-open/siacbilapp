/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.provvedimento;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.RicercaEntitaConsultabileBaseAjaxModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaProvvedimentoConsultabile;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per la ricerca del provvedimento consultabile (cruscottino) come entita di partenza
 * @author Elisa Chiari
 * @version 1.0.0 - 02/03/2016
 *
 */
public class RicercaProvvedimentoConsultabileAjaxModel extends RicercaEntitaConsultabileBaseAjaxModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8338956513114711092L;
	
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private TipoAtto tipoAttoProvvedimento;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
		
	/** Costruttore */
	public RicercaProvvedimentoConsultabileAjaxModel() {
		super();
		setTitolo("Ricerca Provvedimento Consultabile - AJAX");
	}
	
	/**
	 * @return the request ottieniElencoEntitaDiPartenzaCapitoloEntrataConsultabile
	 */
	public RicercaSinteticaEntitaConsultabile creaRequestRicercaSinteticaEntitaConsultabile() {
		RicercaSinteticaEntitaConsultabile request = new RicercaSinteticaEntitaConsultabile();
		ParametriRicercaProvvedimentoConsultabile parametriRicercaProvvedimentoConsultabile = new ParametriRicercaProvvedimentoConsultabile();
		
		//TODO popolare i dati della request!!!
		parametriRicercaProvvedimentoConsultabile.setAnnoAtto(annoProvvedimento);
		parametriRicercaProvvedimentoConsultabile.setNumeroAtto(numeroProvvedimento);
		parametriRicercaProvvedimentoConsultabile.setTipoAtto(tipoAttoProvvedimento);
		parametriRicercaProvvedimentoConsultabile.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		
		request.setParametriRicercaEntitaConsultabile(parametriRicercaProvvedimentoConsultabile);
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the annoProvvedimento
	 */
	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}

	/**
	 * @param annoProvvedimento the annoProvvedimento to set
	 */
	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	/**
	 * @return the numeroProvvedimento
	 */
	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	/**
	 * @param numeroProvvedimento the numeroProvvedimento to set
	 */
	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}

	/**
	 * @return the tipoAttoProvvedimento
	 */
	public TipoAtto getTipoAttoProvvedimento() {
		return tipoAttoProvvedimento;
	}

	/**
	 * @param tipoAttoProvvedimento the tipoAttoProvvedimento to set
	 */
	public void setTipoAttoProvvedimento(TipoAtto tipoAttoProvvedimento) {
		this.tipoAttoProvvedimento = tipoAttoProvvedimento;
	}
}
