/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.model.ContoTipoBeneSelector;
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
import it.csi.siac.siacgenser.model.ClassePiano;

/**
 * The Class BaseInserisciVariazioneCespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 */
public abstract class BaseInserisciVariazioneCespiteModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;
	
	private VariazioneCespite variazioneCespite;
	private Cespite cespite;
	private String flagSoggettoTutelaBeniCulturali;
	private String flgDonazioneRinvenimento;
	
	private List<TipoBeneCespite> listaTipoBeneCespite = new ArrayList<TipoBeneCespite>();
	private List<ClassificazioneGiuridicaCespite> listaClassificazioneGiuridicaCespite = new ArrayList<ClassificazioneGiuridicaCespite>();
	
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	
	private int uidCespite;
	private String backUrl = "backToStep2";
	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public BaseInserisciVariazioneCespiteModel() {
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
	 * @return the backUrl
	 */
	public String getBackUrl() {
		return backUrl;
	}

	/**
	 * @param backUrl the backUrl to set
	 */
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
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
	 * @return the wizardStep3
	 */
	public abstract String getWizardStep3();
	
	/**
	 * @return the flagTipoVariazioneIncremento
	 */
	public abstract Boolean getFlagTipoVariazioneIncremento();
	
	/**
	 * @return the tableTitle
	 */
	public abstract String getTableTitle();
	
	/**
	 * @return the buttonNewTitle
	 */
	public abstract String getButtonNewTitle();
	
	/**
	 * @return the intestazioneInserimentoNuovaVariazione
	 */
	public abstract String getIntestazioneInserimentoNuovaVariazione();
	/**
	 * @return the intestazioneAggiornamentoVariazione
	 */
	public abstract String getIntestazioneAggiornamentoVariazione();
	/**
	 * @return the testoSelectTipoVariazione
	 */
	public abstract String getTestoSelectTipoVariazione();
	
	/**
	 * Gets the conto patrimoniale tipo bene enum.
	 *
	 * @return the conto patrimoniale tipo bene enum
	 */
	public ContoTipoBeneSelector getContoPatrimonialeTipoBeneSelector(){
		return ContoTipoBeneSelector.Patrimoniale;
	}
	
	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return getAmbitoFIN();
	}
	
	/**
	 * Gets the ambito FIN.
	 *
	 * @return the ambito FIN
	 */
	public Ambito getAmbitoFIN() {
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
		req.setTipoBeneCespite(getCespite().getTipoBeneCespite());
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
		req.setVariazioneCespite(new VariazioneCespite());
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(getFlagTipoVariazioneIncremento());
		// Copio solo l'uid del cespite per alleggerire la ricerca
		req.getVariazioneCespite().setCespite(new Cespite());
		req.getVariazioneCespite().getCespite().setUid(getCespite().getUid());
		
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite);
		
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

	/**
	 * Imposta cespite da uid.
	 */
	public void impostaCespiteDaUid() {
		setCespite(new Cespite());
		getCespite().setUid(getUidCespite());
	}
}
