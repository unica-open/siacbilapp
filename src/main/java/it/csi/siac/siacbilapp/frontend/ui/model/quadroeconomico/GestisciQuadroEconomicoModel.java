/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilapp.frontend.ui.model.quadroeconomico;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AnnullaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.model.ParteQuadroEconomico;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 18/02/2017
 *
 */
public class GestisciQuadroEconomicoModel extends GenericBilancioModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -2737909917024097721L;



	/** Costruttore vuoto di default */
	public GestisciQuadroEconomicoModel() {
		setTitolo("Quadro Economico");
	}
	
	private QuadroEconomico quadroEconomicoPadreInElaborazione;
	private QuadroEconomico quadroEconomicoFiglioInElaborazione;
	
	private List<QuadroEconomico> listaQuadroEconomicoTrovati  = new ArrayList<QuadroEconomico>();
	
	private QuadroEconomico quadroEconomico;
	
	private List<StatoOperativoQuadroEconomico> statiOperativiQuadroEconomico =  new ArrayList<StatoOperativoQuadroEconomico>();

	private List<ParteQuadroEconomico> parteQuadroEconomico =  new ArrayList<ParteQuadroEconomico>();
		
	private StatoOperativoQuadroEconomico statoOperativoQuadroEconomicoDefault = StatoOperativoQuadroEconomico.VALIDO;
	
	private ParteQuadroEconomico parteQuadroEconomicoDefault = ParteQuadroEconomico.A;

	
	private int uidQuadroEconomicoPadre;
	private String descrizioneStatoOperativoQuadroEconomico;
	
	

	/**
	 * Gets the quadroEconomico.
	 *
	 * @return the quadroEconomico
	 */
	public QuadroEconomico getQuadroEconomicoPadreInElaborazione() {
		return quadroEconomicoPadreInElaborazione;
	}

	/**
	 * Sets the quadroEconomico.
	 *
	 * @param quadroEconomicoPadreInElaborazione the new quadro economico padre in elaborazione
	 */
	public void setQuadroEconomicoPadreInElaborazione(QuadroEconomico quadroEconomicoPadreInElaborazione) {
		this.quadroEconomicoPadreInElaborazione = quadroEconomicoPadreInElaborazione;
	}

	/**
	 * Gets the quadro economico figlio in elaborazione.
	 *
	 * @return the quadro economicoFiglioInElaborazione
	 */
	public QuadroEconomico getQuadroEconomicoFiglioInElaborazione() {
		return quadroEconomicoFiglioInElaborazione;
	}

	/**
	 * Sets the cquadro economico figlio in elaborazione.
	 *
	 * @param quadroEconomicoFiglioInElaborazione the quadroEconomicoFiglioInElaborazione to set
	 */
	public void setQuadroEconomicoFiglioInElaborazione(QuadroEconomico quadroEconomicoFiglioInElaborazione) {
		this.quadroEconomicoFiglioInElaborazione = quadroEconomicoFiglioInElaborazione;
	}

	/**
	 * Gets the lista quadroEconomico.
	 *
	 * @return the listaquadroEconomico
	 */
	public List<QuadroEconomico> getListaQuadroEconomicoTrovati() {
		return listaQuadroEconomicoTrovati;
	}

	/**
	 * Sets the lista quadroEconomico.
	 *
	 * @param listaQuadroEconomicoTrovati the new lista quadro economico trovati
	 */
	public void setListaQuadroEconomicoTrovati(List<QuadroEconomico> listaQuadroEconomicoTrovati) {
		this.listaQuadroEconomicoTrovati = listaQuadroEconomicoTrovati != null? listaQuadroEconomicoTrovati : new ArrayList<QuadroEconomico>();
	}

	/**
	 * Gets the stato operativo quadroEconomico default.
	 *
	 * @return the statoOperativoQuadroEconomicoDefault
	 */
	public StatoOperativoQuadroEconomico getStatoOperativoQuadroEconomicoDefault() {
		return statoOperativoQuadroEconomicoDefault;
	}

	/**
	 * Sets the stato operativo QuadroEconomico default.
	 *
	 * @param statoOperativoQuadroEconomicoDefault the statoOperativoClassificatoreDefault to set
	 */
	public void setStatoOperativoQuadroEconomicoDefault(StatoOperativoQuadroEconomico statoOperativoQuadroEconomicoDefault) {
		this.statoOperativoQuadroEconomicoDefault = statoOperativoQuadroEconomicoDefault;
	}
	
	// CREAZIONE DELLE REQUESTS
	
	/**
	 * @return the parteQuadroEconomicoDefault
	 */
	public ParteQuadroEconomico getParteQuadroEconomicoDefault() {
		return parteQuadroEconomicoDefault;
	}

	/**
	 * @param parteQuadroEconomicoDefault the parteQuadroEconomicoDefault to set
	 */
	public void setParteQuadroEconomicoDefault(ParteQuadroEconomico parteQuadroEconomicoDefault) {
		this.parteQuadroEconomicoDefault = parteQuadroEconomicoDefault;
	}

	/**
	 * @return the QuadroEconomico
	 */
	public QuadroEconomico getQuadroEconomico() {
		return quadroEconomico;
	}

	/**
	 * Sets the quadro economico.
	 *
	 * @param quadroEconomico the new quadro economico
	 */
	public void setQuadroEconomico(QuadroEconomico quadroEconomico) {
		this.quadroEconomico = quadroEconomico;
	}
	
	/**
	 * Gets the stati operativi QuadroEconomico.
	 *
	 * @return the statiOperativiQuadroEconomico
	 */
	public List<StatoOperativoQuadroEconomico> getStatiOperativiQuadroEconomico() {
		return statiOperativiQuadroEconomico;
	}

	/**
	 * Sets the stati operativi QuadroEconomico.
	 * @param statiOperativiQuadroEconomico the statiOperativiQuadroEconomico to set
	 */
	public void setStatiOperativiQuadroEconomico(List<StatoOperativoQuadroEconomico> statiOperativiQuadroEconomico) {
		this.statiOperativiQuadroEconomico = statiOperativiQuadroEconomico != null? statiOperativiQuadroEconomico : new ArrayList<StatoOperativoQuadroEconomico>();
	}

	/**
	 * Gets the stati operativi QuadroEconomico.
	 *
	 * @return the statiOperativiQuadroEconomico
	 */
	public List<ParteQuadroEconomico> getParteQuadroEconomico() {
		return parteQuadroEconomico;
	}

	/**
	 * Sets the stati operativi QuadroEconomico.
	 * @param parteQuadroEconomico the parteQuadroEconomico to set
	 */
	public void setParteQuadroEconomico(List<ParteQuadroEconomico> parteQuadroEconomico) {
		this.parteQuadroEconomico = parteQuadroEconomico != null? parteQuadroEconomico : new ArrayList<ParteQuadroEconomico>();
	}

	/**
	 * @return the uidClassificatorePadre
	 */
	public int getUidQuadroEconomicoPadre() {
		return uidQuadroEconomicoPadre;
	}

	/**
	 * @param uidQuadroEconomicoPadre the uidQuadroEconomicoPadre to set
	 */
	public void setUidQuadroEconomicoPadre(int uidQuadroEconomicoPadre) {
		this.uidQuadroEconomicoPadre = uidQuadroEconomicoPadre;
	}

	/**
	 * Gets the descrizione stato operativo QuadroEconomico.
	 *
	 * @return the descrizione stato operativo QuadroEconomico
	 */
	public String getDescrizioneStatoOperativoQuadroEconomico() {
		return descrizioneStatoOperativoQuadroEconomico;
	}

	/**
	 * Sets the descrizione stato operativo quadro economico.
	 *
	 * @param descrizioneStatoOperativoQuadroEconomico the new descrizione stato operativo QuadroEconomico
	 */
	public void setDescrizioneStatoOperativoQuadroEconomico(String descrizioneStatoOperativoQuadroEconomico) {
		this.descrizioneStatoOperativoQuadroEconomico = descrizioneStatoOperativoQuadroEconomico;
	}

	/**
	 * Crea request inserisce quadro economico
	 *
	 * @return la request creata
	 */
	public InserisceQuadroEconomico creaRequestInserisceQuadroEconomico() {
		InserisceQuadroEconomico request = creaRequest(InserisceQuadroEconomico.class);
		
		QuadroEconomico qe = getQuadroEconomico();
		qe.setCodice(qe.getCodice().trim());
		
		request.setQuadroEconomico(getQuadroEconomico());
		request.setBilancio(getBilancio());
		return request;
	}

	/**
	 * Imposta dati per inserimento quadro economico padre.
	 */
	public void impostaDatiPerInserimentoQuadroEconomicoPadre() {
		getQuadroEconomico().setLivello(Integer.valueOf(0));
		getQuadroEconomico().setStatoOperativoQuadroEconomico(StatoOperativoQuadroEconomico.VALIDO);
		//getQuadroEconomico().set
	}
	
	/**
	 * Imposta dati per inserimentoquadro economico figlio.
	 */
	public void impostaDatiPerInserimentoQuadroEconomicoFiglio() {
		getQuadroEconomico().setLivello(Integer.valueOf(1));
		QuadroEconomico cPadre = new QuadroEconomico();
		cPadre.setUid(getUidQuadroEconomicoPadre());
		getQuadroEconomico().setQuadroEconomicoPadre(cPadre);
		getQuadroEconomico().setStatoOperativoQuadroEconomico(StatoOperativoQuadroEconomico.VALIDO);
	}
	
	/**
	 * Imposta dati per aggiornamento classificatore.
	 */
	public void impostaDatiPerAggiornamentoClassificatore() {
		StatoOperativoQuadroEconomico socGSA = StatoOperativoQuadroEconomico.byDescrizione(getDescrizioneStatoOperativoQuadroEconomico());
		getQuadroEconomico().setStatoOperativoQuadroEconomico(socGSA);
		QuadroEconomico cPadre = new QuadroEconomico();
		cPadre.setUid(getUidQuadroEconomicoPadre());
		getQuadroEconomico().setQuadroEconomicoPadre(cPadre);
	}
	/**
	 * Crea request ricerca sinteticaQuadroEconomico
	 *
	 * @return the ricerca sintetica QuadroEconomico
	 */
	public RicercaSinteticaQuadroEconomico creaRequestRicercaSinteticaQuadroEconomico() {
		RicercaSinteticaQuadroEconomico req = creaRequest(RicercaSinteticaQuadroEconomico.class);
		req.setQuadroEconomico(getQuadroEconomico());
		req.setBilancio(getBilancio());
		req.setParametriPaginazione(new ParametriPaginazione(0, Integer.MAX_VALUE));
		return req;
	}
	
	/**
	 * Crea request aggiorna QuadroEconomico
	 *
	 * @return the aggiorna QuadroEconomico
	 */
	public AggiornaQuadroEconomico creaRequestAggiornaQuadroEconomico() {
		AggiornaQuadroEconomico req = creaRequest(AggiornaQuadroEconomico.class);
		QuadroEconomico qe = getQuadroEconomico();
		qe.setCodice(qe.getCodice().trim());
		req.setQuadroEconomico(getQuadroEconomico());
		req.setBilancio(getBilancio());
		return req;
	}
	
	/**
	 * Crea request agnnulla quadro economico
	 *
	 * @return the annullaquadro economico
	 */
	public AnnullaQuadroEconomico creaRequestAgnnullaQuadroEconomico() {
		AnnullaQuadroEconomico req = creaRequest(AnnullaQuadroEconomico.class);
		req.setQuadroEconomico(getQuadroEconomico());
		req.setBilancio(getBilancio());
		return req;
	}

}
