/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloEntrataConsultabile;

/**
 * Classe di model per la ricerca del capitolo EntrataConsultabile
 * @author Elisa Chiari
 * @version 1.0.0 - 01/03/2016
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCapitoloEntrataConsultabileAjaxModel extends  RicercaCapitoloConsultabileAjaxModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -4019313491770951676L;
	
	private TitoloEntrata titoloEntrata;
	private TipologiaTitolo tipologiaTitolo;
	private CategoriaTipologiaTitolo categoriaTipologiaTitolo;
	
	/** Costruttore */
	public RicercaCapitoloEntrataConsultabileAjaxModel() {
		super();
		setTitolo("Ricerca Capitolo entrata Consultabile - AJAX");
	}

	/**
	 * @return the request ottieniElencoEntitaDiPartenzaCapitoloEntrataConsultabile
	 */
	public RicercaSinteticaEntitaConsultabile creaRequestRicercaSinteticaEntitaConsultabile() {
		
		ParametriRicercaCapitoloEntrataConsultabile prcec = new ParametriRicercaCapitoloEntrataConsultabile();
		
		prcec.setTipoCapitolo("Entrata");
		prcec.setFaseBilancio(getFaseBilancio());
		prcec.setAnnoCapitolo(getAnnoCapitolo());
		prcec.setNumeroCapitolo(getNumeroCapitolo());
		prcec.setNumeroArticolo(getNumeroArticolo());
		prcec.setNumeroUEB(getNumeroUEB());
		prcec.setDescrizione(getDescrizione());
		prcec.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		
		prcec.setTitoloEntrata(getTitoloEntrata());
		prcec.setTipologiaTitolo(getTipologiaTitolo());
		prcec.setCategoriaTipologiaTitolo(getCategoriaTipologiaTitolo());
		
		
		return creaRequestRicercaSinteticaEntitaConsultabile(prcec);
	}


	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}

	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}

	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}

	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}

	/**
	 * @return the categoriaTipologiaTitolo
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitolo() {
		return categoriaTipologiaTitolo;
	}

	/**
	 * @param categoriaTipologiaTitolo the categoriaTipologiaTitolo to set
	 */
	public void setCategoriaTipologiaTitolo(CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		this.categoriaTipologiaTitolo = categoriaTipologiaTitolo;
	}

}
