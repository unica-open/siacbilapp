/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.ammortamento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.model.ContoTipoBeneSelector;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siacgenser.model.ClassePiano;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class InserisciAmmortamentoMassivoModel extends GenericBilancioModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -4320392761520607326L;
	
	private Cespite cespite;
	
	private Integer ultimoAnnoAmmortamento;
	private List<Integer> listaIdCespiti;
	
	private List<TipoBeneCespite> listaTipoBeneCespite = new ArrayList<TipoBeneCespite>();
	
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	
	private boolean abilitaPulsantiAmmortamento = true;

	/**
	 * Instantiates a new ricerca tipo bene model.
	 */
	public InserisciAmmortamentoMassivoModel() {
		setTitolo("Ammortamento massivo cespiti");
	}
	
	/**
	 * @return the cespite
	 */
	public Cespite getCespite() {
		return cespite;
	}



	/**
	 * @param cespite the cespite to set
	 */
	public void setCespite(Cespite cespite) {
		this.cespite = cespite;
	}

	/**
	 * @return the ultimoAnnoammortamento
	 */
	public Integer getUltimoAnnoAmmortamento() {
		return ultimoAnnoAmmortamento;
	}

	/**
	 * @param ultimoAnnoammortamento the ultimoAnnoammortamento to set
	 */
	public void setUltimoAnnoAmmortamento(Integer ultimoAnnoammortamento) {
		this.ultimoAnnoAmmortamento = ultimoAnnoammortamento;
	}
	/**
	 * @return the listaIdCespiti
	 */
	public List<Integer> getListaIdCespiti() {
		return listaIdCespiti;
	}

	/**
	 * @param listaIdCespiti the listaIdCespiti to set
	 */
	public void setListaIdCespiti(List<Integer> listaIdCespiti) {
		this.listaIdCespiti = listaIdCespiti;
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
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}

	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}

	/**
	 * @return the listaTipoBeneCespite
	 */
	public List<TipoBeneCespite> getListaTipoBeneCespite() {
		return listaTipoBeneCespite;
	}

	/**
	 * @param listaTipoBeneCespite the listaTipoBeneCespite to set
	 */
	public void setListaTipoBeneCespite(List<TipoBeneCespite> listaTipoBeneCespite) {
		this.listaTipoBeneCespite = listaTipoBeneCespite;
	}
	
	/**
	 * @return the abilitaPulsantiAmmortamento
	 */
	public boolean isAbilitaPulsantiAmmortamento() {
		return abilitaPulsantiAmmortamento;
	}

	/**
	 * @param abilitaPulsantiAmmortamento the abilitaPulsantiAmmortamento to set
	 */
	public void setAbilitaPulsantiAmmortamento(boolean abilitaPulsantiAmmortamento) {
		this.abilitaPulsantiAmmortamento = abilitaPulsantiAmmortamento;
	}

	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return Ambito.AMBITO_INV;
	}
	
	/**
	 * Gets the conto patrimoniale tipo bene selector.
	 *
	 * @return the conto patrimoniale tipo bene selector
	 */
	public ContoTipoBeneSelector getContoPatrimonialeTipoBeneSelector() {
		return ContoTipoBeneSelector.Patrimoniale;
	}

	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite() {
		RicercaSinteticaCespite req = creaRequest(RicercaSinteticaCespite.class);
		req.setCespite(getCespite());
		req.setTipoBeneCespite(getCespite().getTipoBeneCespite());
		// tiro su solo la categoria
		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
				TipoBeneCespiteModelDetail.Annullato);
		req.setConPianoAmmortamentoCompleto(Boolean.FALSE);
		req.setMassimoAnnoAmmortato(getUltimoAnnoAmmortamento());
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}
	
	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaTipoBeneCespite creaRequestRicercaSinteticaTipoBene() {
		RicercaSinteticaTipoBeneCespite req = creaRequest(RicercaSinteticaTipoBeneCespite.class);
		//tiro su solo la categoria
		req.setModelDetails(TipoBeneCespiteModelDetail.ContoPatrimoniale, TipoBeneCespiteModelDetail.Annullato);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.getParametriPaginazione().setElementiPerPagina(Integer.MAX_VALUE);
		return req;
	}

	/**
	 * Crea request inserisci ammortamento massivo cespite selezionati.
	 *
	 * @return the inserisci ammortamento massivo cespite
	 */
	public InserisciAmmortamentoMassivoCespite creaRequestInserisciAmmortamentoMassivoCespiteSelezionati() {
		InserisciAmmortamentoMassivoCespite req = getDatiBaseRequestInserisciAmmortamentoMassivo();
		req.setUidsCespiti(getListaIdCespiti());
		return req;
	}
	
	/**
	 * Gets the dati base request inserisci ammortamento massivo.
	 *
	 * @return the dati base request inserisci ammortamento massivo
	 */
	private InserisciAmmortamentoMassivoCespite getDatiBaseRequestInserisciAmmortamentoMassivo() {
		InserisciAmmortamentoMassivoCespite req = creaRequest(InserisciAmmortamentoMassivoCespite.class);
		req.setUltimoAnnoAmmortamento(getUltimoAnnoAmmortamento());
		return req;
	}

	/**
	 * Crea request inserisci ammortamento massivo cespite tutti.
	 *
	 * @param requestRicerca the request ricerca
	 * @return the inserisci ammortamento massivo cespite
	 */
	public InserisciAmmortamentoMassivoCespite creaRequestInserisciAmmortamentoMassivoCespiteTutti(RicercaSinteticaCespite requestRicerca) {
		InserisciAmmortamentoMassivoCespite req = getDatiBaseRequestInserisciAmmortamentoMassivo();
		req.setRequestRicerca(requestRicerca);
		return req;
	}
	
}
