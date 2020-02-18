/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe base di model per la ricerca delle causali a partire dall'evento.
 * 
 * @version 1.0.0 - 14/10/2015
 *
 */
public class RicercaCausaleEPByEventoModel extends GenericBilancioModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8626967104334288774L;
	
	private Evento evento;
	private Ambito ambito;
	private String ambitoSuffix;
	private TipoCausale tipoCausale;
	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();

	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	/**
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return listaCausaleEP;
	}

	/**
	 * @param listaCausaleEP the listaCausaleEP to set
	 */
	public void setListaCausaleEP(List<CausaleEP> listaCausaleEP) {
		this.listaCausaleEP = listaCausaleEP != null ? listaCausaleEP : new ArrayList<CausaleEP>();
	}
	
	/**
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return ambito;
	}

	/**
	 * @param ambito the ambito to set
	 */
	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}

	/**
	 * @return the ambitoSuffix
	 */
	public String getAmbitoSuffix() {
		return ambitoSuffix;
	}

	
	/////////////////////////////
	/**
	 * @return l'ambito
	 */
	public Ambito getAmbitoContoCausale(){
		return getAmbito();
	}
	
	/**
	 * Ottiene il suffisso dell'ambito corrispondente: pu&oacute; essere "FIN" o "GSA"
	 * 
	 * @return la stringa con il suffisso dell'ambito
	 */
	public final String getAmbitoContoCausaleSuffix() {
		return getAmbitoContoCausale().getSuffix();
	}

	/////////////////////////////////////////////////////
	
	/**
	 * @param ambitoSuffix the ambitoSuffix to set
	 */
	public void setAmbitoSuffix(String ambitoSuffix) {
		this.ambitoSuffix = ambitoSuffix;
	}
	
	/**
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale() {
		return tipoCausale;
	}

	/**
	 * @param tipoCausale the tipoCausale to set
	 */
	public void setTipoCausale(TipoCausale tipoCausale) {
		this.tipoCausale = tipoCausale;
	}

	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCausale creaRequestRicercaSinteticaCausale() {
		RicercaSinteticaCausale request = creaRequest(RicercaSinteticaCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		if (getEvento() != null) { 
			List<Evento> listaEventoRicerca = new ArrayList<Evento>();
			listaEventoRicerca.add(getEvento());
			causEpPerRequest.setEventi(listaEventoRicerca);
		}
		
		causEpPerRequest.setTipoCausale(TipoCausale.Libera);

		//causEpPerRequest.setAmbito(getAmbitoContoCausale());
		causEpPerRequest.setAmbito(getAmbito());
		
		request.setCausaleEP(causEpPerRequest);
		request.setBilancio(getBilancio());
		//TODO mettere eccezione se supera 100
		request.setParametriPaginazione(creaParametriPaginazione(100));
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaMinimaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaMinimaCausale creaRequestRicercaMinimaCausale() {
		RicercaMinimaCausale request = creaRequest(RicercaMinimaCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		
		causEpPerRequest.setTipoCausale(getTipoCausale());
		causEpPerRequest.setAmbito(getAmbito());
		causEpPerRequest.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		
		if(getEvento() != null && getEvento().getUid() != 0) {
			causEpPerRequest.setEventi(Arrays.asList(getEvento()));
		}
		request.setCausaleEP(causEpPerRequest);
		request.setBilancio(getBilancio());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareCausale creaRequestRicercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale request = creaRequest(RicercaSinteticaModulareCausale.class);
		
		request.setBilancio(getBilancio());
		// Verificare limitazione
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		// TODO: controllare quali model detail servano
		request.setCausaleEPModelDetails();
		
		CausaleEP causEpPerRequest = new CausaleEP();
		causEpPerRequest.setTipoCausale(tipoCausale != null ? tipoCausale : TipoCausale.Libera);
		causEpPerRequest.setAmbito(getAmbito());
		request.setCausaleEP(causEpPerRequest);
		
		if (getEvento() != null) { 
			causEpPerRequest.setEventi(Arrays.asList(getEvento()));
		}
		
		return request;
	}

}
