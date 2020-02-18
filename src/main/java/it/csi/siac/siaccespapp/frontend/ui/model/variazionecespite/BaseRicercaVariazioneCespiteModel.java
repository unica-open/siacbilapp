/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;

/**
 * The Class BaseRicercaVariazioneCespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 */
public abstract class BaseRicercaVariazioneCespiteModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;
	
	private VariazioneCespite variazioneCespite;
	private Cespite cespite;
	private String flagSoggettoTutelaBeniCulturali;
	private String flgDonazioneRinvenimento;
	
	private List<TipoBeneCespite> listaTipoBeneCespite = new ArrayList<TipoBeneCespite>();
	private List<ClassificazioneGiuridicaCespite> listaClassificazioneGiuridicaCespite = new ArrayList<ClassificazioneGiuridicaCespite>();

	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public BaseRicercaVariazioneCespiteModel() {
		setTitolo("Inventario - Rivalutazione cespite");
	}
	
	/**
	 * @return the variazioneCespite
	 */
	public VariazioneCespite getVariazioneCespite() {
		return this.variazioneCespite;
	}

	/**
	 * @param variazioneCespite the variazioneCespite to set
	 */
	public void setVariazioneCespite(VariazioneCespite variazioneCespite) {
		this.variazioneCespite = variazioneCespite;
	}

	/**
	 * @return the cespite
	 */
	public Cespite getCespite() {
		return this.cespite;
	}

	/**
	 * @param cespite the cespite to set
	 */
	public void setCespite(Cespite cespite) {
		this.cespite = cespite;
	}

	/**
	 * @return the flagSoggettoTutelaBeniCulturali
	 */
	public String getFlagSoggettoTutelaBeniCulturali() {
		return this.flagSoggettoTutelaBeniCulturali;
	}

	/**
	 * @param flagSoggettoTutelaBeniCulturali the flagSoggettoTutelaBeniCulturali to set
	 */
	public void setFlagSoggettoTutelaBeniCulturali(String flagSoggettoTutelaBeniCulturali) {
		this.flagSoggettoTutelaBeniCulturali = flagSoggettoTutelaBeniCulturali;
	}

	/**
	 * @return the flgDonazioneRinvenimento
	 */
	public String getFlgDonazioneRinvenimento() {
		return this.flgDonazioneRinvenimento;
	}

	/**
	 * @param flgDonazioneRinvenimento the flgDonazioneRinvenimento to set
	 */
	public void setFlgDonazioneRinvenimento(String flgDonazioneRinvenimento) {
		this.flgDonazioneRinvenimento = flgDonazioneRinvenimento;
	}

	/**
	 * @return the listaTipoBeneCespite
	 */
	public List<TipoBeneCespite> getListaTipoBeneCespite() {
		return this.listaTipoBeneCespite;
	}

	/**
	 * @param listaTipoBeneCespite the listaTipoBeneCespite to set
	 */
	public void setListaTipoBeneCespite(List<TipoBeneCespite> listaTipoBeneCespite) {
		this.listaTipoBeneCespite = listaTipoBeneCespite != null ? listaTipoBeneCespite : new ArrayList<TipoBeneCespite>();
	}

	/**
	 * @return the listaClassificazioneGiuridicaCespite
	 */
	public List<ClassificazioneGiuridicaCespite> getListaClassificazioneGiuridicaCespite() {
		return this.listaClassificazioneGiuridicaCespite;
	}

	/**
	 * @param listaClassificazioneGiuridicaCespite the listaClassificazioneGiuridicaCespite to set
	 */
	public void setListaClassificazioneGiuridicaCespite(List<ClassificazioneGiuridicaCespite> listaClassificazioneGiuridicaCespite) {
		this.listaClassificazioneGiuridicaCespite = listaClassificazioneGiuridicaCespite != null ? listaClassificazioneGiuridicaCespite : new ArrayList<ClassificazioneGiuridicaCespite>();
	}

	/**
	 * @return the codiceDescrizioneTipoBene
	 */
	public String getCodiceDescrizioneTipoBene() {
		if(cespite == null || cespite.getTipoBeneCespite() == null) {
			return "";
		}
		return cespite.getTipoBeneCespite().getCodice() + " - " + cespite.getTipoBeneCespite().getDescrizione();
	}
	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	/**
	 * @return the formTitle
	 */
	public abstract String getFormTitle();
	
	/**
	 * @return the flagTipoVariazioneIncremento
	 */
	public abstract Boolean getFlagTipoVariazioneIncremento();
	
	/**
	 * @return the formSubtitle
	 */
	public abstract String getFormSubtitle();
	
	/**
	 * Gets the testo select tipo variazione.
	 *
	 * @return the testo select tipo variazione
	 */
	public abstract String getTestoSelectTipoVariazione();
	
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
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite() {
		RicercaSinteticaCespite req = creaRequest(RicercaSinteticaCespite.class);
		req.setCespite(getCespite());
		req.setTipoBeneCespite(impostaEntitaFacoltativa(getCespite().getTipoBeneCespite()));
		req.setClassificazioneGiuridicaCespite(getCespite().getClassificazioneGiuridicaCespite());
		// tiro su solo la categoria
		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
				CespiteModelDetail.ClassificazioneGiuridicaCespite,
				TipoBeneCespiteModelDetail.Annullato);
		
		// SIAC-6375
		req.getCespite().setFlagSoggettoTutelaBeniCulturali(FormatUtils.parseBoolean(getFlagSoggettoTutelaBeniCulturali(), "S", "N"));
		req.getCespite().setFlgDonazioneRinvenimento(FormatUtils.parseBoolean(getFlgDonazioneRinvenimento(), "S", "N"));
		
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}

	/**
	 * Crea una request per la ricerca di dettaglio del cespite
	 * @return la request creata
	 */
	public RicercaDettaglioCespite creaRequestRicercaDettaglioCespite() {
		RicercaDettaglioCespite req = creaRequest(RicercaDettaglioCespite.class);
		req.setCespite(getCespite());
		// TODO: aggiungere il model detail per il tipo bene?
		req.setListaCespiteModelDetails(new CespiteModelDetail[] {CespiteModelDetail.TipoBeneCespiteModelDetail});
		return req;
	}
	
	/**
	 * Crea una request per la ricerca sintetica della variazione del cespite
	 * @return la request creata
	 */
	public RicercaSinteticaVariazioneCespite creaRequestRicercaSinteticaVariazioneCespite() {
		RicercaSinteticaVariazioneCespite req = creaRequest(RicercaSinteticaVariazioneCespite.class);
		
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setVariazioneCespite(getVariazioneCespite());
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(getFlagTipoVariazioneIncremento());
		req.getVariazioneCespite().setCespite(getCespite());
		req.getVariazioneCespite().getCespite().setFlagSoggettoTutelaBeniCulturali(FormatUtils.parseBoolean(getFlagSoggettoTutelaBeniCulturali(), "S", "N"));
		req.getVariazioneCespite().getCespite().setFlgDonazioneRinvenimento(FormatUtils.parseBoolean(getFlgDonazioneRinvenimento(), "S", "N"));
		
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite,
				VariazioneCespiteModelDetail.CespiteModelDetail,
				CespiteModelDetail.TipoBeneCespiteModelDetail);
		
		return req;
	}
	/**
	 * Crea una request per l'inserimento della variazione del cespite
	 * @return la request creata
	 */
	public InserisciVariazioneCespite creaRequestInserisciVariazioneCespite() {
		InserisciVariazioneCespite req = creaRequest(InserisciVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(getFlagTipoVariazioneIncremento());
		req.getVariazioneCespite().setCespite(getCespite());
		
		return req;
	}
	
	/**
	 * Crea una request per l'aggiornamento della variazione del cespite
	 * @return la request creata
	 */
	public AggiornaVariazioneCespite creaRequestAggiornaVariazioneCespite() {
		AggiornaVariazioneCespite req = creaRequest(AggiornaVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(getFlagTipoVariazioneIncremento());
		req.getVariazioneCespite().setCespite(getCespite());
		
		return req;
	}
	
	/**
	 * Crea una request per l'eliminazione della variazione del cespite
	 * @return la request creata
	 */
	public EliminaVariazioneCespite creaRequestEliminaVariazioneCespite() {
		EliminaVariazioneCespite req = creaRequest(EliminaVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		
		return req;
	}
	
	/**
	 * Crea una request per la ricerca di dettaglio della variazione del cespite
	 * @return la request creata
	 */
	public RicercaDettaglioVariazioneCespite creaRequestRicercaDettaglioVariazioneCespite() {
		RicercaDettaglioVariazioneCespite req = creaRequest(RicercaDettaglioVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite);
		
		return req;
	}
}
