/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di modello per l'ottenimento del dettaglio dell'ElencoDocumentiAllegato.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 21/ott/2014
 */
public class DettaglioElencoDocumentiAllegatoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7538291013112788069L;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaElementoElencoDocumentiAllegato = new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>();
	//SIAC-5589: aggiungo il filtro per soggetto
	private Soggetto soggetto;
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaElementoElencoDocumentiAllegatoFiltrato = new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>();
	
	/**
	 * @return the elencoDocumentiAllegato
	 */
	public ElencoDocumentiAllegato getElencoDocumentiAllegato() {
		return elencoDocumentiAllegato;
	}
	
	/**
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato to set
	 */
	public void setElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
	}
	
	/**
	 * @return the listaElementoElencoDocumentiAllegato
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaElementoElencoDocumentiAllegato() {
		return listaElementoElencoDocumentiAllegato;
	}
	
	/**
	 * @param listaElementoElencoDocumentiAllegato the listaElementoElencoDocumentiAllegato to set
	 */
	public void setListaElementoElencoDocumentiAllegato(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaElementoElencoDocumentiAllegato) {
		this.listaElementoElencoDocumentiAllegato = listaElementoElencoDocumentiAllegato;
	}
	
	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the listaElementoElencoDocumentiAllegatoFiltrato
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaElementoElencoDocumentiAllegatoFiltrato() {
		return listaElementoElencoDocumentiAllegatoFiltrato;
	}

	/**
	 * @param listaElementoElencoDocumentiAllegatoFiltrato the listaElementoElencoDocumentiAllegatoFiltrato to set
	 */
	public void setListaElementoElencoDocumentiAllegatoFiltrato(
			List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaElementoElencoDocumentiAllegatoFiltrato) {
		this.listaElementoElencoDocumentiAllegatoFiltrato = listaElementoElencoDocumentiAllegatoFiltrato;
	}
	
	/* **** REQUESTS **** */
	
	/**
	 * Crea una request per il servizio {@link RicercaDettaglioElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioElenco creaRequestRicercaDettaglioElenco() {
		RicercaDettaglioElenco request = creaRequest(RicercaDettaglioElenco.class);
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		return request;
	}
	/**
	 * Crea una request per il servizio {@link RicercaSinteticaQuoteElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaQuoteElenco creaRequestRicercaSinteticaQuoteElenco() {
		RicercaSinteticaQuoteElenco request = creaRequest(RicercaSinteticaQuoteElenco.class);
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		request.setSoggetto(getSoggetto());
		request.setParametriPaginazione(new ParametriPaginazione(0,10));
		return request;
	}
}
