/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class ConsultaDismissioneCespiteModel extends GenericDismissioneCespiteModel {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -739068969609394407L;

	/**
	 * Instantiates a new consulta tipo bene model.
	 */
	public ConsultaDismissioneCespiteModel() {
		setTitolo("consulta dismissione");
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
		req.setDismissioneCespite(creaDismissioneDaUid());
		// tiro su solo la categoria
		req.setModelDetails(CespiteModelDetail.TipoBeneCespite,
								CespiteModelDetail.FondoAmmortamento
//				CespiteModelDetail.ResiduoAmmortamento
				);
		
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}
	
	/**
	 * Gets the descrizione categoria.
	 *
	 * @return the descrizione categoria
	 */
	public String getDescrizione() {
		if(getDismissioneCespite() == null) {
			return "";
		}
		return getDismissioneCespite().getDescrizione();
	}
	
	/**
	 * Gets the elenco.
	 *
	 * @return the elenco
	 */
	public String getElenco() {
		if(getDismissioneCespite() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(getDismissioneCespite().getAnnoElenco() != null? getDismissioneCespite().getAnnoElenco()  : "" )
				.append("/")
				.append(getDismissioneCespite().getNumeroElenco() != null? getDismissioneCespite().getNumeroElenco()  : "" );
		return sb.toString();
		
	}
	
	/**
	 * Gets the provvedimento.
	 *
	 * @return the provvedimento
	 */
	public String getProvvedimento() {
		if(getDismissioneCespite() == null || getDismissioneCespite().getAttoAmministrativo() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(Integer.toString(getDismissioneCespite().getAttoAmministrativo().getAnno()))
			.append("/")
			.append(Integer.toString(getDismissioneCespite().getAttoAmministrativo().getNumero()));
		if(getDismissioneCespite().getAttoAmministrativo().getTipoAtto() != null) {
			sb.append("/")
				.append(getDismissioneCespite().getAttoAmministrativo().getTipoAtto().getDescrizione());
		}
		if(getDismissioneCespite().getAttoAmministrativo().getStrutturaAmmContabile() != null) {
			sb.append("/")
				.append(getDismissioneCespite().getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
		}
		
		return sb.toString();
	}
	
	/**
	 * Gets the data cessazione.
	 *
	 * @return the data cessazione
	 */
	public String getDataCessazione() {
		if(getDismissioneCespite() == null) {
			return "";
		}
		return FormatUtils.formatDate(getDismissioneCespite().getDataCessazione());
	}
	
	/**
	 * Gets the causale dismissione.
	 *
	 * @return the causale dismissione
	 */
	public String getCausaleDismissione(){
		if(getDismissioneCespite() == null || getDismissioneCespite().getCausaleEP() == null) {
			return "";
		}
		return getDismissioneCespite().getCausaleEP().getCodice() + " - " + getDismissioneCespite().getCausaleEP().getDescrizione();
	}
	
	/**
	 * Gets the numero cespiti collegati.
	 *
	 * @return the numero cespiti collegati
	 */
	public String getStatoMovimento() {
		if(getDismissioneCespite() == null || getDismissioneCespite().getStatoDismissioneCespite() == null ) {
			return "";
		}
		return getDismissioneCespite().getStatoDismissioneCespite().getCodice() + " - " +  getDismissioneCespite().getStatoDismissioneCespite().getDescrizione();
		
	}
	
	/**
	 * Gets the descrizione stato cessazione.
	 *
	 * @return the descrizione stato cessazione
	 */
	public String getDescrizioneStatoCessazione() {
		if(getDismissioneCespite() == null) {
			return "";
		}
		return StringUtils.defaultString(getDismissioneCespite().getDescrizioneStatoCessazione(), "");
	}
	
}
