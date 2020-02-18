/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegato;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
/**
 * Classe base di model per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
public abstract class AggiornaPrimaNotaIntegrataDocumentoBaseModel extends BaseAggiornaPrimaNotaIntegrataBaseModel {

	/** Per la serializzazione	 */
	private static final long serialVersionUID = 3503574932819500780L;

	private List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	private List<MovimentoEP> listaMovimentoEP = new ArrayList<MovimentoEP>();
	
	//TODO: non so se questo mai servira'
	private String descrRichiesta;
	private String documentiCollegati;
	private String noteCredito;
	
	// Liste
	private List<ElementoQuotaPrimaNotaIntegrata> listaElementoQuota = new ArrayList<ElementoQuotaPrimaNotaIntegrata>();
	private Map<Integer,List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScritture = new HashMap<Integer, List<ElementoScritturaPrimaNotaIntegrata>>();
	
	
	/* modale compilazione guidata conto*/
	//TODO: vedere,forse solo GSA?
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private int uidMovimentoEPPerScritture;
	
	//SIAC-6227
	private RegistrazioneMovFin registrazioneInElaborazione;
	

	/**
	 * @return th mappaMovimentoEPScritture
	 */
	public Map<Integer, List<ElementoScritturaPrimaNotaIntegrata>> getMappaMovimentoEPScritture() {
		return mappaMovimentoEPScritture;
	}

	/**
	 * @param mappaMovimentoEPScritture the mappaMovimentoEPScritture to set
	 */
	public void setMappaMovimentoEPScritture(Map<Integer, List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScritture) {
		this.mappaMovimentoEPScritture = mappaMovimentoEPScritture;
	}

	/**
	 * @return the descrRichiesta
	 */
	public String getDescrRichiesta() {
		return descrRichiesta;
	}
	/**
	 * @param descrRichiesta the descrRichiesta to set
	 */
	public void setDescrRichiesta(String descrRichiesta) {
		this.descrRichiesta = descrRichiesta;
	}
	/**
	 * @return documentiCollegati
	 */
	public String getDocumentiCollegati() {
		return documentiCollegati;
	}
	/**
	 * @param documentiCollegati the documentoCollegati to set
	 */
	public void setDocumentiCollegati(String documentiCollegati) {
		this.documentiCollegati = documentiCollegati;
	}
	/**
	 * @return noteCredito
	 */
	public String getNoteCredito() {
		return noteCredito;
	}
	/**
	 * @param noteCredito the notecredito to set
	 */
	public void setNoteCredito(String noteCredito) {
		this.noteCredito = noteCredito;
	}
	/**
	 * @return the listaElementoQuota to set
	 */
	public List<ElementoQuotaPrimaNotaIntegrata> getListaElementoQuota() {
		return listaElementoQuota;
	}
	/**
	 * @param listaElementoQuota the listaElementoquota to set
	 */
	public void setListaElementoQuota(List<ElementoQuotaPrimaNotaIntegrata> listaElementoQuota) {
		this.listaElementoQuota = listaElementoQuota;
	}
	
	/**
	 * @return the listaElementoScrittura
	 */
	public List<ElementoScritturaPrimaNotaIntegrata> getListaElementoScrittura() {
		return listaElementoScrittura;
	}

	/**
	 * @param listaElementoScrittura the listaElementoScrittura to set
	 */
	public void setListaElementoScrittura(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura) {
		this.listaElementoScrittura = listaElementoScrittura;
	}
	
	/**
	 * @return the listaMovimentoEP
	 */
	public List<MovimentoEP> getListaMovimentoEP() {
		return listaMovimentoEP;
	}

	/**
	 * @return listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata
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
	 * @return the uidMovimentoEPPerScritture
	 */
	public int getUidMovimentoEPPerScritture() {
		return uidMovimentoEPPerScritture;
	}
	/**
	 * @param uidMovimentoEPPerScritture the uidMovimentoEPPerscritture to set
	 */
	public void setUidMovimentoEPPerScritture(int uidMovimentoEPPerScritture) {
		this.uidMovimentoEPPerScritture = uidMovimentoEPPerScritture;
	}
	
	
	
	/**
	 * @return the registrazioneInElaborazione
	 */
	public RegistrazioneMovFin getRegistrazioneInElaborazione() {
		return registrazioneInElaborazione;
	}

	/**
	 * @param registrazioneInElaborazione the registrazioneInElaborazione to set
	 */
	public void setRegistrazioneInElaborazione(RegistrazioneMovFin registrazioneInElaborazione) {
		this.registrazioneInElaborazione = registrazioneInElaborazione;
	}

	@Override
	public String getAzioneAggiornamento() {
		return "aggiornaPrimaNotaIntegrataDocumento" + getAmbito().getSuffix() + "_aggiorna";
	}

	
	
	/**
	 * Prima Nota Libera ha solo un movimentoEP
	 * 
	 * @return the causaleEP
	 */
	private CausaleEP getCausaleEP() {
		return getPrimaNota() != null 
			&& getPrimaNota().getListaMovimentiEP() != null 
			&& !getPrimaNota().getListaMovimentiEP().isEmpty()
			&& getPrimaNota().getListaMovimentiEP().get(0) != null
				? getPrimaNota().getListaMovimentiEP().get(0).getCausaleEP()
				: null;
		
	}
	/**
	 * @return the descrizioneCausale
	 */
	public String getDescrizioneCausale() {
		CausaleEP causaleEP = getCausaleEP();
		StringBuilder sb = new StringBuilder();
		if(causaleEP != null) {
			sb.append(causaleEP.getCodice())
				.append(" - ")
				.append(causaleEP.getDescrizione());
		}
		return sb.toString();
	}
	
	
	/**
	 * Ottiene la lista dei movimenti EP con l'ambito correttamente popolato.
	 * 
	 * @return la lista dei movimenti
	 */
	@Override
	public List<MovimentoEP> ottieniListaMovimentiEPConAmbito() {
		List<MovimentoEP> listaMovimentiEPPrimaNota = getPrimaNota().getListaMovimentiEP();
		Map<Integer, List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScrittureElaborate = getMappaMovimentoEPScritture();
		List<MovimentoEP> listaMovimentoEPAggiornata = new ArrayList<MovimentoEP>();
		//passo dall'utility all'oggetto List<MovimentiEP>
		for(MovimentoEP movEP : listaMovimentiEPPrimaNota){
			if(movEP == null || movEP.getUid()==0){
				continue;
			}
			List<MovimentoDettaglio> listaMovimentiDettaglioFinal = new ArrayList<MovimentoDettaglio>();
			int numeroRiga = 0;
			for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : mappaMovimentoEPScrittureElaborate.get(movEP.getUid())){
				if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null && elementoScrittura.getMovimentoDettaglio().getImporto()!= null){
					MovimentoDettaglio movimentoDettaglio = elementoScrittura.getMovimentoDettaglio();
					movimentoDettaglio.setNumeroRiga(numeroRiga);
					listaMovimentiDettaglioFinal.add(movimentoDettaglio);
					numeroRiga++;
				}
			} 
			
			movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
			listaMovimentoEPAggiornata.add(movEP);
		}
		return listaMovimentoEPAggiornata;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRegistrazioneMovFin}.
	 * @param regIniziale la registrazione iniziale
	 * @param idDocumento l'id del documento
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaRegistrazioneMovFin creaRequestRicercaSinteticaRegistrazioneMovFin(RegistrazioneMovFin regIniziale, int idDocumento) {
		RicercaSinteticaRegistrazioneMovFin request = creaRequest(RicercaSinteticaRegistrazioneMovFin.class);
		
		// Trasformo a tutti gli effetti la ricerca sintetica in estesa. Sono una persona molto cattiva :-)
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		
		request.setIdDocumento(idDocumento);
		
		// Creo una nuova registrazione in cui includo solo il getBilancio()
		RegistrazioneMovFin rmf = new RegistrazioneMovFin();
		rmf.setBilancio(getBilancio());
		rmf.setAmbito(getAmbito());
		request.setRegistrazioneMovFin(rmf);
		//devo indicare da che evento parto per distinguere le registrazioni della cassa economale da quelle del doc standard
		request.setEventoRegistrazioneIniziale(regIniziale.getEvento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link CalcolaImportoMovimentoCollegato}.
	 * @param registrazioneMovFin la registrazione
	 * @return la request creata
	 */
	public CalcolaImportoMovimentoCollegato creaRequestCalcolaImportoMovimentoCollegato(RegistrazioneMovFin registrazioneMovFin) {
		CalcolaImportoMovimentoCollegato req = creaRequest(CalcolaImportoMovimentoCollegato.class);
		req.setRegistrazioneMovFin(registrazioneMovFin);
		return req;
	}

}
