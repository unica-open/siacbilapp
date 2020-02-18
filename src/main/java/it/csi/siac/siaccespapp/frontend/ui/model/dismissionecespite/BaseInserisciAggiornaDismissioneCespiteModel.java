/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;

/**
 * The Class InserisciDismissioneCespiteModel.
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
public class BaseInserisciAggiornaDismissioneCespiteModel extends GenericDismissioneCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -1543584867152439685L;
	
	private List<TipoBeneCespite> listaTipoBeneCespite;
	
	private List<Integer> uidsCespitiDaCollegare;
	
	private int uidCespiteDaScollegare;
	
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
	 * @return the uidsCespitiDaCollegare
	 */
	public List<Integer> getUidsCespitiDaCollegare() {
		return uidsCespitiDaCollegare;
	}

	/**
	 * @param uidsCespitiDaCollegare the uidsCespitiDaCollegare to set
	 */
	public void setUidsCespitiDaCollegare(List<Integer> uidsCespitiDaCollegare) {
		this.uidsCespitiDaCollegare = uidsCespitiDaCollegare;
	}
	
	/**
	 * @return the uidCespiteDaScollegare
	 */
	public int getUidCespiteDaScollegare() {
		return uidCespiteDaScollegare;
	}

	/**
	 * @param uidCespiteDaScollegare the uidCespiteDaScollegare to set
	 */
	public void setUidCespiteDaScollegare(int uidCespiteDaScollegare) {
		this.uidCespiteDaScollegare = uidCespiteDaScollegare;
	}

	
	/**
	 * Gets the chiave dismissione cespite.
	 *
	 * @return the chiave dismissione cespite
	 */
	public String getChiaveDismissioneCespite() {
		StringBuilder sb = new StringBuilder();
		DismissioneCespite ds = getDismissioneCespite();
		if(ds == null || ds.getUid() == 0) {
			return sb.toString();
		}
		sb.append(StringUtils.defaultString(ds.getAnnoElenco().toString()))
		.append(" / ")
		.append(StringUtils.defaultString(ds.getNumeroElenco().toString()));
		return sb.toString();
	}
	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite() {
		RicercaSinteticaCespite req = creaRequest(RicercaSinteticaCespite.class);
		Cespite cespite = new Cespite();
		req.setCespite(cespite);
		req.setDismissioneCespite(getDismissioneCespite());
		// tiro su solo la categoria
		req.setModelDetails(CespiteModelDetail.TipoBeneCespite,
								CespiteModelDetail.FondoAmmortamento
//				CespiteModelDetail.ResiduoAmmortamento
				);
		
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
	 * Crea request collega cespite dismissione cespite.
	 *
	 * @return the collega cespite dismissione cespite
	 */
	public CollegaCespiteDismissioneCespite creaRequestCollegaCespiteDismissioneCespite() {
		CollegaCespiteDismissioneCespite req = creaRequest(CollegaCespiteDismissioneCespite.class);
		req.setDismissioneCespite(getDismissioneCespite());
		req.setUidsCespiti(getUidsCespitiDaCollegare());
		return req;
		
	}

	/**
	 * Crea request scollega cespite dismissione cespite.
	 *
	 * @return the scollega cespite dismissione cespite
	 */
	public ScollegaCespiteDismissioneCespite creaRequestScollegaCespiteDismissioneCespite() {
		ScollegaCespiteDismissioneCespite req = creaRequest(ScollegaCespiteDismissioneCespite.class);
		req.setDismissioneCespite(getDismissioneCespite());
		Cespite c = new Cespite();
		c.setUid(getUidCespiteDaScollegare());
		req.setCespite(c);
		return req;
	}

	/**
	 * Crea request effettua scritture dismissione cespite.
	 *
	 * @return the effettua scritture dismissione cespite
	 */
	public InserisciPrimeNoteDismissioneCespite creaRequestEffettuaScrittureDismissioneCespite() {
		InserisciPrimeNoteDismissioneCespite req = creaRequest(InserisciPrimeNoteDismissioneCespite.class);
		req.setDismissioneCespite(getDismissioneCespite());
		return req;
	}

	/**
	 * Gets the azione annullamento.
	 *
	 * @return the azione annullamento
	 */
	public String getAzioneAnnullaForm() {
		return "";
	}
}
