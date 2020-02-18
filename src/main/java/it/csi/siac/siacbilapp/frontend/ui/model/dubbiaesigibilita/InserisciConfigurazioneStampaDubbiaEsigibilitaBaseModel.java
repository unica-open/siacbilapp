/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TipoMediaPrescelta;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe base di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * @author Marchino Alessandro
 */
public abstract class InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2437882777856288808L;
	
	private AttributiBilancio attributiBilancio;
	private boolean attributiBilancioPresenti;

	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TipologiaTitolo> listaTipologiaTitolo = new ArrayList<TipologiaTitolo>();
	private List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = new ArrayList<CategoriaTipologiaTitolo>();
	private List<ElementoPianoDeiConti> listaElementoPianoDeiConti = new ArrayList<ElementoPianoDeiConti>();
	private List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = new ArrayList<StrutturaAmministrativoContabile>();
	
	// SIAC-4469
	private boolean datiAnnoPrecedentePresenti;
	private Integer idOperazioneAsincrona;
	
	/**
	 * @return the attributiBilancio
	 */
	public AttributiBilancio getAttributiBilancio() {
		return attributiBilancio;
	}

	/**
	 * @param attributiBilancio the attributiBilancio to set
	 */
	public void setAttributiBilancio(AttributiBilancio attributiBilancio) {
		this.attributiBilancio = attributiBilancio;
	}

	/**
	 * @return the attributiBilancioPresenti
	 */
	public boolean isAttributiBilancioPresenti() {
		return attributiBilancioPresenti;
	}

	/**
	 * @param attributiBilancioPresenti the attributiBilancioPresenti to set
	 */
	public void setAttributiBilancioPresenti(boolean attributiBilancioPresenti) {
		this.attributiBilancioPresenti = attributiBilancioPresenti;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTipologiaTitolo
	 */
	public List<TipologiaTitolo> getListaTipologiaTitolo() {
		return listaTipologiaTitolo;
	}

	/**
	 * @param listaTipologiaTitolo the listaTipologiaTitolo to set
	 */
	public void setListaTipologiaTitolo(List<TipologiaTitolo> listaTipologiaTitolo) {
		this.listaTipologiaTitolo = listaTipologiaTitolo != null ? listaTipologiaTitolo : new ArrayList<TipologiaTitolo>();
	}

	/**
	 * @return the listaCategoriaTipologiaTitolo
	 */
	public List<CategoriaTipologiaTitolo> getListaCategoriaTipologiaTitolo() {
		return listaCategoriaTipologiaTitolo;
	}

	/**
	 * @param listaCategoriaTipologiaTitolo the listaCategoriaTipologiaTitolo to set
	 */
	public void setListaCategoriaTipologiaTitolo(List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo) {
		this.listaCategoriaTipologiaTitolo = listaCategoriaTipologiaTitolo != null ? listaCategoriaTipologiaTitolo : new ArrayList<CategoriaTipologiaTitolo>();
	}

	/**
	 * @return the listaElementoPianoDeiConti
	 */
	public List<ElementoPianoDeiConti> getListaElementoPianoDeiConti() {
		return listaElementoPianoDeiConti;
	}

	/**
	 * @param listaElementoPianoDeiConti the listaElementoPianoDeiConti to set
	 */
	public void setListaElementoPianoDeiConti(List<ElementoPianoDeiConti> listaElementoPianoDeiConti) {
		this.listaElementoPianoDeiConti = listaElementoPianoDeiConti != null ? listaElementoPianoDeiConti : new ArrayList<ElementoPianoDeiConti>();
	}

	/**
	 * @return the listaStrutturaAmministrativoContabile
	 */
	public List<StrutturaAmministrativoContabile> getListaStrutturaAmministrativoContabile() {
		return listaStrutturaAmministrativoContabile;
	}

	/**
	 * @param listaStrutturaAmministrativoContabile the listaStrutturaAmministrativoContabile to set
	 */
	public void setListaStrutturaAmministrativoContabile(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		this.listaStrutturaAmministrativoContabile = listaStrutturaAmministrativoContabile != null ? listaStrutturaAmministrativoContabile : new ArrayList<StrutturaAmministrativoContabile>();
	}

	/**
	 * @return the datiAnnoPrecedentePresenti
	 */
	public boolean isDatiAnnoPrecedentePresenti() {
		return datiAnnoPrecedentePresenti;
	}

	/**
	 * @param datiAnnoPrecedentePresenti the datiAnnoPrecedentePresenti to set
	 */
	public void setDatiAnnoPrecedentePresenti(boolean datiAnnoPrecedentePresenti) {
		this.datiAnnoPrecedentePresenti = datiAnnoPrecedentePresenti;
	}

	/**
	 * @return the idOperazioneAsincrona
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
	}

	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincrona to set
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}

	/**
	 * @return the actionOperazioneAttributi
	 */
	public abstract String getActionOperazioneAttributi();

	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link RicercaAttributiBilancio}.
	 * @return la request creata
	 */
	public RicercaAttributiBilancio creaRequestRicercaAttributiBilancio() {
		RicercaAttributiBilancio request = creaRequest(RicercaAttributiBilancio.class);
		request.setBilancio(getBilancio());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaAttributiBilancio}.
	 * @return la request creata
	 */
	public AggiornaAttributiBilancio creaRequestAggiornaAttributiBilancio() {
		AggiornaAttributiBilancio request = creaRequest(AggiornaAttributiBilancio.class);
	
		request.setBilancio(getBilancio());
		request.setAttributiBilancio(getAttributiBilancio());
	
		return request;
	}

	/**
	 * Rimozione dei capitoli dalla lista dei temporanei
	 * @param temporanei i dati temporanei
	 * @param selezionati i dati selezionati
	 * @param <AFDE> la tipizzazione dell'accantonamento
	 */
	protected <AFDE extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> void rimuoviCapitoliDaTemp(Iterable<AFDE> temporanei, Iterable<AFDE> selezionati) {
		for (Iterator<AFDE> itTemp = temporanei.iterator(); itTemp.hasNext();) {
			AFDE aTemp = itTemp.next();
			for (Iterator<AFDE> itSel = selezionati.iterator(); itSel.hasNext();) {
				AFDE aSel = itSel.next();
				
				if (aTemp.getUid() == aSel.getUid()) {
					itTemp.remove();
					itSel.remove();
					
					break;
				}
			}
		}
	}
	
	/**
	 * Gestisce gli attributi di bilancio
	 * @param ab gliattributi
	 */
	public void handleAttributiBilancio(AttributiBilancio ab) {
		setAttributiBilancio(ab == null ? new AttributiBilancio() : ab);
		checkIfAttributiBilancioPresent();
		setDefaultValuesAttributiBilancio();
	}
	
	/**
	 * Controlla se gli attributi del bilancio siano presenti
	 */
	private void checkIfAttributiBilancioPresent() {
		boolean attributiBilancioNonPresenti = getAttributiBilancio().getAccantonamentoGraduale() == null
				&& getAttributiBilancio().getPercentualeAccantonamentoAnno() == null
				&& getAttributiBilancio().getPercentualeAccantonamentoAnno1() == null
				&& getAttributiBilancio().getPercentualeAccantonamentoAnno2() == null
				&& getAttributiBilancio().getRiscossioneVirtuosa() == null 
				&& getAttributiBilancio().getMediaApplicata() == null
				&& getAttributiBilancio().getUltimoAnnoApprovato() == null;
		
		setAttributiBilancioPresenti(!attributiBilancioNonPresenti);
	}
	
	/**
	 * Imposta i valori di default per gli attributi di bilancio
	 */
	protected void setDefaultValuesAttributiBilancio() {
		if (getAttributiBilancio().getAccantonamentoGraduale() == null) {
			getAttributiBilancio().setAccantonamentoGraduale(Boolean.FALSE);
		}
	
		if (getAttributiBilancio().getRiscossioneVirtuosa() == null) {
			getAttributiBilancio().setRiscossioneVirtuosa(Boolean.FALSE);
		}
	
		if (getAttributiBilancio().getMediaApplicata() == null) {
			getAttributiBilancio().setMediaApplicata(TipoMediaPrescelta.SEMPLICE);
		}
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAzionePerChiave}.
	 * @param chiaveAzione la chiave dell'azione
	 * @return la request creata
	 */
	public RicercaAzionePerChiave creaRequestRicercaAzionePerChiave(String chiaveAzione) {
		it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiave req = creaRequest(RicercaAzionePerChiave.class);
		Azione azione = new Azione();
		azione.setNome(chiaveAzione);
		req.setAzione(azione);
		return req;
	}

}
