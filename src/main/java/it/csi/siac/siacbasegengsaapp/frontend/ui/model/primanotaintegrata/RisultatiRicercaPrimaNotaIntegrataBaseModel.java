/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * Classe base di model per i risultati di ricerca della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
public abstract class RisultatiRicercaPrimaNotaIntegrataBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione **/
	private static final long serialVersionUID = -4710276463213893584L;
	
	private PrimaNota primaNota;
	private Integer savedDisplayStart;
	
	//collegamento prime note
	private Integer indicePrimaNota;
	private List<TipoRelazionePrimaNota> listaMotivazioni = new ArrayList<TipoRelazionePrimaNota>();
	private List <TipoCausale> listaTipoPrimaNota = Arrays.asList(TipoCausale.values());
	private TipoRelazionePrimaNota motivazione;
	private String noteCollegamento;
	private List<PrimaNota> listaPrimeNoteCollegate = new ArrayList<PrimaNota>();
	private PrimaNota primaNotaDaCollegare;
	private Integer annoPrimaNota;
	private List<TipoEvento> listaTipiEvento = new ArrayList<TipoEvento>();
	
	//SIAC-5799
	private String riepilogoRicerca;
	
	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return "risultatiRicercaPrimaNotaIntegrataFIN";
	}
	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}
	
	/**
	 * @return the indicePrimaNota
	 */
	public Integer getIndicePrimaNota() {
		return indicePrimaNota;
	}

	/**
	 * @param indicePrimaNota the indicePrimaNota to set
	 */
	public void setIndicePrimaNota(Integer indicePrimaNota) {
		this.indicePrimaNota = indicePrimaNota;
	}

	/**
	 * @return the listaMotivazioni
	 */
	public List<TipoRelazionePrimaNota> getListaMotivazioni() {
		return listaMotivazioni;
	}

	/**
	 * @param listaMotivazioni the listaMotivazioni to set
	 */
	public void setListaMotivazioni(List<TipoRelazionePrimaNota> listaMotivazioni) {
		this.listaMotivazioni = listaMotivazioni;
	}

	/**
	 * @return the listaTipoPrimaNota
	 */
	public List<TipoCausale> getListaTipoPrimaNota() {
		return listaTipoPrimaNota;
	}

	/**
	 * @param listaTipoPrimaNota the listaTipoPrimaNota to set
	 */
	public void setListaTipoPrimaNota(List<TipoCausale> listaTipoPrimaNota) {
		this.listaTipoPrimaNota = listaTipoPrimaNota;
	}

	/**
	 * @return the motivazione
	 */
	public TipoRelazionePrimaNota getMotivazione() {
		return motivazione;
	}

	/**
	 * @param motivazione the motivazione to set
	 */
	public void setMotivazione(TipoRelazionePrimaNota motivazione) {
		this.motivazione = motivazione;
	}

	/**
	 * @return the noteCollegamento
	 */
	public String getNoteCollegamento() {
		return noteCollegamento;
	}
	/**
	 * @param noteCollegamento the noteCollegamento to set
	 */
	public void setNoteCollegamento(String noteCollegamento) {
		this.noteCollegamento = noteCollegamento;
	}
	/**
	 * @return the listaPrimeNoteCollegate
	 */
	public List<PrimaNota> getListaPrimeNoteCollegate() {
		return listaPrimeNoteCollegate;
	}
	/**
	 * @param listaPrimeNoteCollegate the listaPrimeNoteCollegate to set
	 */
	public void setListaPrimeNoteCollegate(List<PrimaNota> listaPrimeNoteCollegate) {
		this.listaPrimeNoteCollegate = listaPrimeNoteCollegate;
	}
	/**
	 * @return the primaNotaDaCollegare
	 */
	public PrimaNota getPrimaNotaDaCollegare() {
		return primaNotaDaCollegare;
	}

	/**
	 * @param primaNotaDaCollegare the primaNotaDaCollegare to set
	 */
	public void setPrimaNotaDaCollegare(PrimaNota primaNotaDaCollegare) {
		this.primaNotaDaCollegare = primaNotaDaCollegare;
	}

	/**
	 * @return the annoPrimaNota
	 */
	public Integer getAnnoPrimaNota() {
		return annoPrimaNota;
	}

	/**
	 * @param annoPrimaNota the annoPrimaNota to set
	 */
	public void setAnnoPrimaNota(Integer annoPrimaNota) {
		this.annoPrimaNota = annoPrimaNota;
	}

	/**
	 * @return the listaTipiEvento
	 */
	public List<TipoEvento> getListaTipiEvento() {
		return listaTipiEvento;
	}

	/**
	 * @param listaTipiEvento the listaTipiEvento to set
	 */
	public void setListaTipiEvento(List<TipoEvento> listaTipiEvento) {
		this.listaTipiEvento = listaTipiEvento;
	}
	
	/**
	 * @return the riepilogoRicerca
	 */
	public String getRiepilogoRicerca() {
		return riepilogoRicerca;
	}
	/**
	 * @param riepilogoRicerca the riepilogoRicerca to set
	 */
	public void setRiepilogoRicerca(String riepilogoRicerca) {
		this.riepilogoRicerca = riepilogoRicerca;
	}
	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNota(){
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		PrimaNota pn = new PrimaNota();
		pn.setUid(primaNota.getUid());
		request.setPrimaNota(pn);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ValidaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public ValidaPrimaNota creaRequestValidaPrimaNota() {
		ValidaPrimaNota request = creaRequest(ValidaPrimaNota.class);
		getPrimaNota().setAmbito(getAmbito());
		getPrimaNota().setBilancio(getBilancio());
		request.setPrimaNota(getPrimaNota());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaPrimeNote}.
	 * 
	 * @return la request creata
	 */
	public RicercaPrimeNote creaRequestRicercaPrimeNote() {
		RicercaPrimeNote request = creaRequest(RicercaPrimeNote.class);
		primaNotaDaCollegare.setAmbito(getAmbito());
		request.setAnnoPrimaNota(annoPrimaNota);
		request.setPrimaNota(primaNotaDaCollegare);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link CollegaPrimeNote}.
	 * @param pNotaDaCollegare la prima nota da collegare
	 * 
	 * @return la request creata
	 */
	public CollegaPrimeNote creaRequestCollegaPrimeNote(PrimaNota pNotaDaCollegare) {
		CollegaPrimeNote request = creaRequest(CollegaPrimeNote.class);
		request.setPrimaNotaPadre(primaNota);
		request.setPrimaNotaFiglia(pNotaDaCollegare);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaCollegamentoPrimeNote}.
	 * @param pNotaDaScollegare la prima nota da scollegare
	 * 
	 * @return la request creata
	 */
	public EliminaCollegamentoPrimeNote creaRequestEliminaCollegamentoPrimeNote(PrimaNota pNotaDaScollegare) {
		EliminaCollegamentoPrimeNote request = creaRequest(EliminaCollegamentoPrimeNote.class);
		request.setPrimaNotaPadre(primaNota);
		request.setPrimaNotaFiglia(pNotaDaScollegare);
		return request;
	}
	
	@Override
	public RicercaDettaglioBilancio creaRequestRicercaDettaglioBilancio(){
		Integer annoBilancioPrecedente = getBilancio().getAnno() - 1;
		RicercaDettaglioBilancio request = creaRequest(RicercaDettaglioBilancio.class);
		request.setChiaveBilancio(null);
		request.setAnnoBilancio(annoBilancioPrecedente);
		request.setRichiedente(getRichiedente());
		return request;
	}

}
