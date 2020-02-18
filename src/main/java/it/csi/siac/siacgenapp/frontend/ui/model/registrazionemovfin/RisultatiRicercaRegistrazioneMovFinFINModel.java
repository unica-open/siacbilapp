/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca.ElementoRegistrazioneMovFin;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Model per la visualizzazione dei risultati di ricerca per la RegistrazioneMovFin (ambito GEN).
 * 
 * @author Valentina
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/05/2015
 * @version 1.1.0 - 05/10/2015
 * 
 */
public class RisultatiRicercaRegistrazioneMovFinFINModel extends RisultatiRicercaRegistrazioneMovFinBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7566737194035542251L;

	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<Macroaggregato> listaMacroaggregato = new ArrayList<Macroaggregato>();
	private List<TipologiaTitolo> listaTitoloTipologia = new ArrayList<TipologiaTitolo>();
	private List<CategoriaTipologiaTitolo> listaTipologiaCategoria = new ArrayList<CategoriaTipologiaTitolo>();
	private List<ElementoPianoDeiConti> listaElementoPianoDeiConti = new ArrayList<ElementoPianoDeiConti>();
		
	private int uidTitoloSpesaConto = 0;
	private int uidTitoloEntrataConto = 0;
	private int uidMacroaggregatoConto = 0;
	private int uidTipologiaTitoloConto = 0;
	private int uidPianoDeiContiRegMovFin = 0;
	private int uidCategoriaConto = 0;
	
	private int uidPianoDeiContiRegMovFinAggiornato = 0;
	private int uidRegistrazioneDaAggiornare = 0;
	
	private ElementoRegistrazioneMovFin registrazioneAggiornata;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMovFinFINModel() {
		super();
		setTitolo("Risultati di ricerca registro");
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
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
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
	 * @return the listaMacroaggregato
	 */
	public List<Macroaggregato> getListaMacroaggregato() {
		return listaMacroaggregato;
	}

	/**
	 * @param listaMacroaggregato the listaMacroaggregato to set
	 */
	public void setListaMacroaggregato(List<Macroaggregato> listaMacroaggregato) {
		this.listaMacroaggregato = listaMacroaggregato != null ? listaMacroaggregato : new ArrayList<Macroaggregato>();
	}

	/**
	 * @return the listaTitoloTipologia
	 */
	public List<TipologiaTitolo> getListaTitoloTipologia() {
		return listaTitoloTipologia;
	}

	/**
	 * @param listaTitoloTipologia the listaTitoloTipologia to set
	 */
	public void setListaTitoloTipologia(List<TipologiaTitolo> listaTitoloTipologia) {
		this.listaTitoloTipologia = listaTitoloTipologia != null ? listaTitoloTipologia : new ArrayList<TipologiaTitolo>();
	}

	/**
	 * @return the listaTipologiaCategoria
	 */
	public List<CategoriaTipologiaTitolo> getListaTipologiaCategoria() {
		return listaTipologiaCategoria;
	}

	/**
	 * @param listaTipologiaCategoria the listaTipologiaCategoria to set
	 */
	public void setListaTipologiaCategoria(List<CategoriaTipologiaTitolo> listaTipologiaCategoria) {
		this.listaTipologiaCategoria = listaTipologiaCategoria != null ? listaTipologiaCategoria : new ArrayList<CategoriaTipologiaTitolo>();
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
	 * @return the uidTitoloSpesaConto
	 */
	public int getUidTitoloSpesaConto() {
		return uidTitoloSpesaConto;
	}

	/**
	 * @param uidTitoloSpesaConto the uidTitoloSpesaConto to set
	 */
	public void setUidTitoloSpesaConto(int uidTitoloSpesaConto) {
		this.uidTitoloSpesaConto = uidTitoloSpesaConto;
	}

	/**
	 * @return the uidTitoloEntrataConto
	 */
	public int getUidTitoloEntrataConto() {
		return uidTitoloEntrataConto;
	}

	/**
	 * @param uidTitoloEntrataConto the uidTitoloEntrataConto to set
	 */
	public void setUidTitoloEntrataConto(int uidTitoloEntrataConto) {
		this.uidTitoloEntrataConto = uidTitoloEntrataConto;
	}

	/**
	 * @return the uidMacroaggregatoConto
	 */
	public int getUidMacroaggregatoConto() {
		return uidMacroaggregatoConto;
	}

	/**
	 * @param uidMacroaggregatoConto the uidMacroaggregatoConto to set
	 */
	public void setUidMacroaggregatoConto(int uidMacroaggregatoConto) {
		this.uidMacroaggregatoConto = uidMacroaggregatoConto;
	}

	/**
	 * @return the uidTipologiaTitoloConto
	 */
	public int getUidTipologiaTitoloConto() {
		return uidTipologiaTitoloConto;
	}

	/**
	 * @param uidTipologiaTitoloConto the uidTipologiaTitoloConto to set
	 */
	public void setUidTipologiaTitoloConto(int uidTipologiaTitoloConto) {
		this.uidTipologiaTitoloConto = uidTipologiaTitoloConto;
	}

	/**
	 * @return the uidPianoDeiContiRegMovFin
	 */
	public int getUidPianoDeiContiRegMovFin() {
		return uidPianoDeiContiRegMovFin;
	}

	/**
	 * @param uidPianoDeiContiRegMovFin the uidPianoDeiContiRegMovFin to set
	 */
	public void setUidPianoDeiContiRegMovFin(int uidPianoDeiContiRegMovFin) {
		this.uidPianoDeiContiRegMovFin = uidPianoDeiContiRegMovFin;
	}

	/**
	 * @return the uidCategoriaConto
	 */
	public int getUidCategoriaConto() {
		return uidCategoriaConto;
	}

	/**
	 * @param uidCategoriaConto the uidCategoriaConto to set
	 */
	public void setUidCategoriaConto(int uidCategoriaConto) {
		this.uidCategoriaConto = uidCategoriaConto;
	}

	/**
	 * @return the uidPianoDeiContiRegMovFinAggiornato
	 */
	public int getUidPianoDeiContiRegMovFinAggiornato() {
		return uidPianoDeiContiRegMovFinAggiornato;
	}

	/**
	 * @param uidPianoDeiContiRegMovFinAggiornato the uidPianoDeiContiRegMovFinAggiornato to set
	 */
	public void setUidPianoDeiContiRegMovFinAggiornato(int uidPianoDeiContiRegMovFinAggiornato) {
		this.uidPianoDeiContiRegMovFinAggiornato = uidPianoDeiContiRegMovFinAggiornato;
	}

	/**
	 * @return the uidRegistrazioneDaAggiornare
	 */
	public int getUidRegistrazioneDaAggiornare() {
		return uidRegistrazioneDaAggiornare;
	}

	/**
	 * @param uidRegistrazioneDaAggiornare the uidRegistrazioneDaAggiornare to set
	 */
	public void setUidRegistrazioneDaAggiornare(int uidRegistrazioneDaAggiornare) {
		this.uidRegistrazioneDaAggiornare = uidRegistrazioneDaAggiornare;
	}

	/**
	 * @return the registrazioneAggiornata
	 */
	public ElementoRegistrazioneMovFin getRegistrazioneAggiornata() {
		return registrazioneAggiornata;
	}

	/**
	 * @param registrazioneAggiornata the registrazioneAggiornata to set
	 */
	public void setRegistrazioneAggiornata(ElementoRegistrazioneMovFin registrazioneAggiornata) {
		this.registrazioneAggiornata = registrazioneAggiornata;
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoriByRelazione}.
	 * @return la request creata
	 */
	public LeggiClassificatoriByRelazione creaRequestLeggiClassificatoriByRelazioneDaPdC() {
		return creaRequestLeggiClassificatoriByRelazioneDaPdC(getUidPianoDeiContiRegMovFin());
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoriByRelazione}.
	 * @param uidElementoPianoDeiConti l'uid del piano dei conti
	 * @return la request creata
	 */
	public LeggiClassificatoriByRelazione creaRequestLeggiClassificatoriByRelazioneDaPdC(int uidElementoPianoDeiConti) {
		LeggiClassificatoriByRelazione request = creaRequest(LeggiClassificatoriByRelazione.class);
		request.setIdClassif(Integer.valueOf(uidElementoPianoDeiConti));
		request.setFromAToB(false);
		request.setEnte(getEnte());
		request.setAnno(getAnnoEsercizioInt());

		return request;
		
	}

	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoriBilByIdFiglio}.
	 * @param uidFiglio l'uid del figlio
	 * @return la request creata
	 */
	public LeggiClassificatoriBilByIdFiglio creaRequestLeggiClassificatoriBilByIdFiglio(int uidFiglio) {
		LeggiClassificatoriBilByIdFiglio request = creaRequest(LeggiClassificatoriBilByIdFiglio.class);
		request.setIdFiglio(uidFiglio);
		request.setIdEnteProprietario(getEnte().getUid());
		request.setAnno(getBilancio().getAnno());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoriByRelazione}.
	 * @return la request creata
	 */
	public AggiornaElementoPianoDeiContiRegistrazioneMovFin creaRequestAggiornaElementoPianoDeiContiRegistrazioneMovFin() {
		AggiornaElementoPianoDeiContiRegistrazioneMovFin request = creaRequest(AggiornaElementoPianoDeiContiRegistrazioneMovFin.class);
		RegistrazioneMovFin registrazione = new RegistrazioneMovFin();
		registrazione.setUid(getUidRegistrazioneDaAggiornare());
		ElementoPianoDeiConti elementoPianoDeiContiAggiornato = new ElementoPianoDeiConti();
		elementoPianoDeiContiAggiornato.setUid(getUidPianoDeiContiRegMovFinAggiornato());
		registrazione.setElementoPianoDeiContiAggiornato(elementoPianoDeiContiAggiornato);
		request.setRegistrazioneMovFin(registrazione);
		return request;
	}

}
