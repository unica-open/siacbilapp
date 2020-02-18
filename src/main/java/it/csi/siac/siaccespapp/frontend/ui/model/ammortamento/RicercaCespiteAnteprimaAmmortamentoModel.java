/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.ammortamento;

import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RicercaCespiteAnteprimaAmmortamentoModel extends GenericBilancioModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 3497407178886367106L;
	
	
	
	
	private Integer uidDettaglioAnteprima;
	private TipoBeneCespite tipoBeneCespite;
	private CategoriaCespiti categoriaCespiti;
	
	private List<TipoBeneCespite> listaTipoBene;
	private List<TipoBeneCespite> listaTipoBeneFiltrata;
	private List<CategoriaCespiti> listaCategoriaCespite;
	
	private Integer annoAmmortamentoAnnuo;
	private String codiceConto;
	private OperazioneSegnoConto segno;
	
	/**
	 * Instantiates a new ricerca tipo bene model.
	 */
	public RicercaCespiteAnteprimaAmmortamentoModel() {
		setTitolo("Ricerca cespite");
	}
	
	
	/**
	 * @return the uidDettaglioAnteprima
	 */
	public Integer getUidDettaglioAnteprima() {
		return uidDettaglioAnteprima;
	}



	/**
	 * @param uidDettaglioAnteprima the uidDettaglioAnteprima to set
	 */
	public void setUidDettaglioAnteprima(Integer uidDettaglioAnteprima) {
		this.uidDettaglioAnteprima = uidDettaglioAnteprima;
	}


	/**
	 * @return the tipoBeneCespite
	 */
	public TipoBeneCespite getTipoBeneCespite() {
		return tipoBeneCespite;
	}



	/**
	 * @param tipoBeneCespite the tipoBeneCespite to set
	 */
	public void setTipoBeneCespite(TipoBeneCespite tipoBeneCespite) {
		this.tipoBeneCespite = tipoBeneCespite;
	}

	/**
	 * @return the categoriaCespiti
	 */
	public CategoriaCespiti getCategoriaCespiti() {
		return categoriaCespiti;
	}

	/**
	 * @param categoriaCespiti the categoriaCespiti to set
	 */
	public void setCategoriaCespiti(CategoriaCespiti categoriaCespiti) {
		this.categoriaCespiti = categoriaCespiti;
	}
	
	

	/**
	 * @return the listaTipoBene
	 */
	public List<TipoBeneCespite> getListaTipoBene() {
		return listaTipoBene;
	}

	/**
	 * @param listaTipoBene the listaTipoBene to set
	 */
	public void setListaTipoBene(List<TipoBeneCespite> listaTipoBene) {
		this.listaTipoBene = listaTipoBene;
	}

	/**
	 * @return the listaTipoBeneFiltrata
	 */
	public List<TipoBeneCespite> getListaTipoBeneFiltrata() {
		return listaTipoBeneFiltrata;
	}

	/**
	 * @param listaTipoBeneFiltrata the listaTipoBeneFiltrata to set
	 */
	public void setListaTipoBeneFiltrata(List<TipoBeneCespite> listaTipoBeneFiltrata) {
		this.listaTipoBeneFiltrata = listaTipoBeneFiltrata;
	}

	/**
	 * @return the listaCategoriaCespite
	 */
	public List<CategoriaCespiti> getListaCategoriaCespite() {
		return listaCategoriaCespite;
	}

	/**
	 * @param listaCategoriaCespite the listaCategoriaCespite to set
	 */
	public void setListaCategoriaCespite(List<CategoriaCespiti> listaCategoriaCespite) {
		this.listaCategoriaCespite = listaCategoriaCespite;
	}
	
	/**
	 * @return the annoAmmortamentoAnnuo
	 */
	public Integer getAnnoAmmortamentoAnnuo() {
		return annoAmmortamentoAnnuo;
	}

	/**
	 * @param annoAmmortamentoAnnuo the annoAmmortamentoAnnuo to set
	 */
	public void setAnnoAmmortamentoAnnuo(Integer annoAmmortamentoAnnuo) {
		this.annoAmmortamentoAnnuo = annoAmmortamentoAnnuo;
	}

	/**
	 * @return the codiceConto
	 */
	public String getCodiceConto() {
		return codiceConto;
	}


	/**
	 * @param codiceConto the codiceConto to set
	 */
	public void setCodiceConto(String codiceConto) {
		this.codiceConto = codiceConto;
	}

	/**
	 * @return the segno
	 */
	public OperazioneSegnoConto getSegno() {
		return segno;
	}


	/**
	 * @param segno the segno to set
	 */
	public void setSegno(OperazioneSegnoConto segno) {
		this.segno = segno;
	}


	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite() {
		RicercaSinteticaCespite req = creaRequest(RicercaSinteticaCespite.class);
		
		DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAnteprimaAmmortamentoAnnuoCespite = new DettaglioAnteprimaAmmortamentoAnnuoCespite();
		dettaglioAnteprimaAmmortamentoAnnuoCespite.setUid(getUidDettaglioAnteprima() != null? getUidDettaglioAnteprima().intValue() : 0);
		dettaglioAnteprimaAmmortamentoAnnuoCespite.setSegno(getSegno());
		req.setDettaglioAnteprimaAmmortamentoAnnuoCespite(dettaglioAnteprimaAmmortamentoAnnuoCespite);
		
		AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite = new AnteprimaAmmortamentoAnnuoCespite();
		anteprimaAmmortamentoAnnuoCespite.setAnno(getAnnoAmmortamentoAnnuo());
		req.setAnteprimaAmmortamentoAnnuoCespite(anteprimaAmmortamentoAnnuoCespite);
		
		req.setTipoBeneCespite(getTipoBeneCespite());
		req.setCategoriaCespiti(getCategoriaCespiti());
		
		// tiro su solo la categoria
		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
				CespiteModelDetail.ClassificazioneGiuridicaCespite,
				CespiteModelDetail.DismissioneCespite,
				CespiteModelDetail.IsCollegatoAPrimeNote,
				CespiteModelDetail.AmmortamentoAnnuoCespiteModelDetail,
				AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespiteAnnoSpecificoModelDetail,
				TipoBeneCespiteModelDetail.Annullato);
		
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
		req.setModelDetails(TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.getParametriPaginazione().setElementiPerPagina(Integer.MAX_VALUE);
		return req;
	}
	
	/**
	 * Crea request ricerca sintetica categoria cespiti.
	 *
	 * @return the ricerca sintetica categoria cespiti
	 */
	public RicercaSinteticaCategoriaCespiti creaRequestRicercaSinteticaCategoriaCespiti(){
		RicercaSinteticaCategoriaCespiti req = creaRequest(RicercaSinteticaCategoriaCespiti.class);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.getParametriPaginazione().setElementiPerPagina(Integer.MAX_VALUE);
		return req;
	}
	
	
	
}
