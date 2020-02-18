/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class GenericCespiteModel extends GenericBilancioModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 4461892722673125155L;
	
	
	private Cespite cespite;
	private int uidCespite;
	
	private List<TipoBeneCespite> listaTipoBeneCespite = new ArrayList<TipoBeneCespite>();
	private List<ClassificazioneGiuridicaCespite> listaClassificazioneGiuridicaCespite = Arrays.asList(ClassificazioneGiuridicaCespite.values());

	/**
	 * @return the tipoBeneCespite
	 */
	public Cespite getCespite() {
		return cespite;
	}

	/**
	 * @param tipoBeneCespite the tipoBeneCespite to set
	 */
	public void setCespite(Cespite tipoBeneCespite) {
		this.cespite = tipoBeneCespite;
	}
	
	/**
	 * @return the uidCespite
	 */
	public int getUidCespite() {
		return uidCespite;
	}

	/**
	 * @param uidCespite the uidCespite to set
	 */
	public void setUidCespite(int uidCespite) {
		this.uidCespite = uidCespite;
	}
	
	/**
	 * @return the tipoBeneCespite
	 */
	public Cespite getTipoBeneCespite() {
		return cespite;
	}

	/**
	 * @param tipoBeneCespite the tipoBeneCespite to set
	 */
	public void setTipoBeneCespite(Cespite tipoBeneCespite) {
		this.cespite = tipoBeneCespite;
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
	 * @return the listaClassificazioneGiuridicaCespite
	 */
	public List<ClassificazioneGiuridicaCespite> getListaClassificazioneGiuridicaCespite() {
		return listaClassificazioneGiuridicaCespite;
	}

	//UTILITIES
	
	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public Ambito getAmbito(){
		return getAmbitoFIN();
	}
	
	/**
	 * Gets the ambito FIN.
	 *
	 * @return the ambito FIN
	 */
	public Ambito getAmbitoFIN(){
		return Ambito.AMBITO_FIN;
	}
	
	
	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaTipoBeneCespite creaRequestRicercaSinteticaTipoBene() {
		RicercaSinteticaTipoBeneCespite req = creaRequest(RicercaSinteticaTipoBeneCespite.class);
		//tiro su solo la categoria
		//req.setModelDetails(TipoBeneCespiteModelDetail.ContoPatrimoniale, TipoBeneCespiteModelDetail.Annullato);
		req.setModelDetails(TipoBeneCespiteModelDetail.ContoPatrimoniale, TipoBeneCespiteModelDetail.Annullato, TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail, CategoriaCespitiModelDetail.Annullato);
		req.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		return req;
	}
	
	/**
	 * Crea request ricerca dettaglio tipo bene cespite.
	 *
	 * @return the ricerca dettaglio tipo bene cespite
	 */
	public RicercaDettaglioCespite creaRequestRicercaDettaglioCespite() {
		RicercaDettaglioCespite req = creaRequest(RicercaDettaglioCespite.class);
		Cespite tc = new Cespite();
		tc.setUid(getUidCespite());
		req.setCespite(tc);
		return req;
	}
	

}
