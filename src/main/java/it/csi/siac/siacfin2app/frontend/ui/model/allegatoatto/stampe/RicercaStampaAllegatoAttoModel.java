/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.stampe;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.GenericAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;

/**
 * Classe di model per la ricerca della stampa dell'allegato atto.
 */
public class RicercaStampaAllegatoAttoModel extends GenericAllegatoAttoModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 3564800121007182562L;
	
	private List<TipoStampaAllegatoAtto> listaTipoStampa = new ArrayList<TipoStampaAllegatoAtto>();
	private AllegatoAttoStampa allegatoAttoStampa;
	
	/** Costruttore vuoto di default */
	public RicercaStampaAllegatoAttoModel() {
		super();
		setTitolo("Ricerca stampe allegato atto");
	}
	
	/**
	 * @return the listaTipoStampa
	 */
	public List<TipoStampaAllegatoAtto> getListaTipoStampa() {
		return listaTipoStampa;
	}

	/**
	 * @param listaTipoStampa the listaTipoStampa to set
	 */
	public void setListaTipoStampa(List<TipoStampaAllegatoAtto> listaTipoStampa) {
		this.listaTipoStampa = listaTipoStampa;
	}

	/**
	 * @return the allegatoAttoStampa
	 */
	public AllegatoAttoStampa getAllegatoAttoStampa() {
		return allegatoAttoStampa;
	}

	/**
	 * @param allegatoAttoStampa the allegatoAttoStampa to set
	 */
	public void setAllegatoAttoStampa(AllegatoAttoStampa allegatoAttoStampa) {
		this.allegatoAttoStampa = allegatoAttoStampa;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaStampaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaStampaAllegatoAtto creaRequestRicercaStampaAllegatoAtto() {
		RicercaSinteticaStampaAllegatoAtto request = creaRequest(RicercaSinteticaStampaAllegatoAtto.class);
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione(ELEMENTI_PER_PAGINA_RICERCA));

		allegatoAttoStampa.setAnnoEsercizio(getAnnoEsercizioInt());
		allegatoAttoStampa.setBilancio(getBilancio());
		allegatoAttoStampa.setEnte(getEnte());
		
		if (getAttoAmministrativo()!=null) {
			AllegatoAtto aa = new AllegatoAtto();
			aa.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
			allegatoAttoStampa.setAllegatoAtto(aa);
		}
		request.setAllegatoAttoStampa(allegatoAttoStampa);
				
		return request;
	}
	
}
