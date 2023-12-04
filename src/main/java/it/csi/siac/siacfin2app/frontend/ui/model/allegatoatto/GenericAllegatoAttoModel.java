/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.Checklist;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe generica di model per l'inserimento dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/set/2014
 *
 */
public class GenericAllegatoAttoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3448849278075864891L;
	
	private AllegatoAtto allegatoAtto;
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private StatoOperativoElencoDocumenti statoOperativoElencoDocumenti;
	
	private AttoAmministrativo attoAmministrativo;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private List<AttoAmministrativo> listaAttoAmministrativo;
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<TipoAtto> listaFiltrataTipoAtto = new ArrayList<TipoAtto>();
	private List<StatoOperativoElencoDocumenti> listaStatoOperativoElencoDocumenti = new ArrayList<StatoOperativoElencoDocumenti>();
	
	//SIAC-7005
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();

	
	// SIAC-8804
	private Checklist checklist;
	

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}


	/**
	 * @return the allegatoAtto
	 */
	public AllegatoAtto getAllegatoAtto() {
		return allegatoAtto;
	}

	/**
	 * @param allegatoAtto the allegatoAtto to set
	 */
	public void setAllegatoAtto(AllegatoAtto allegatoAtto) {
		this.allegatoAtto = allegatoAtto;
	}

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
	 * @return the statoOperativoElencoDocumenti
	 */
	public StatoOperativoElencoDocumenti getStatoOperativoElencoDocumenti() {
		return statoOperativoElencoDocumenti;
	}

	/**
	 * @param statoOperativoElencoDocumenti the statoOperativoElencoDocumenti to set
	 */
	public void setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti statoOperativoElencoDocumenti) {
		this.statoOperativoElencoDocumenti = statoOperativoElencoDocumenti;
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	/**
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}
	
	/**
	 * @return the listaFiltrataTipoAtto
	 */
	public List<TipoAtto> getListaFiltrataTipoAtto() {
		return listaFiltrataTipoAtto;
	}

	/**
	 * @param listaFiltrataTipoAtto the listaFiltrataTipoAtto to set
	 */
	public void setListaFiltrataTipoAtto(List<TipoAtto> listaFiltrataTipoAtto) {
		this.listaFiltrataTipoAtto = listaFiltrataTipoAtto;
	}

	/**
	 * @return the listaStatoOperativoElencoDocumenti
	 */
	public List<StatoOperativoElencoDocumenti> getListaStatoOperativoElencoDocumenti() {
		return listaStatoOperativoElencoDocumenti;
	}

	/**
	 * @param listaStatoOperativoElencoDocumenti the listaStatoOperativoElencoDocumenti to set
	 */
	public void setListaStatoOperativoElencoDocumenti(List<StatoOperativoElencoDocumenti> listaStatoOperativoElencoDocumenti) {
		this.listaStatoOperativoElencoDocumenti = listaStatoOperativoElencoDocumenti;
	}
	
	/**
	 * @return the listaStatoOperativoElencoDocumenti
	 */
	public List<AttoAmministrativo> getListaAttoAmministrativo() {
		return listaAttoAmministrativo;
	}

	/**
	 * Sets the lista atto amministrativo.
	 *
	 * @param listaAttoAmministrativo the new lista atto amministrativo
	 */
	public void setListaAttoAmministrativo(List<AttoAmministrativo> listaAttoAmministrativo) {
		this.listaAttoAmministrativo = listaAttoAmministrativo;
	}
	
	/**
	 * @return the listaContoTesoreria
	 */
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria;
	}

	/**
	 * @param listaContoTesoreria the listaContoTesoreria to set
	 */
	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria != null? listaContoTesoreria : null;
	}
	
	/**
	 * @return the denominazioneAllegatoAtto
	 */
	public String getDenominazioneAllegatoAtto() {
		return computeDenominazioneAllegatoAtto();
	}

	/* **** Requests **** */
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(getAttoAmministrativo().getAnno());
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero());
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		request.setRicercaAtti(ricercaAtti);
		return request;
	}
	
    //SIAC-7005
    /**
	 * Crea una request per il servizio {@link Liste}
	 * @param tipiLista i tipi di lista da cercare
	 * @return la request creata
	 */
	public Liste creaRequestListe(TipiLista... tipiLista) {
		Liste request = creaRequest(Liste.class);
		
		request.setEnte(getEnte());
		request.setTipi(tipiLista);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaElenco creaRequestRicercaElenco() {
		RicercaElenco request = creaRequest(RicercaElenco.class);
		
		getElencoDocumentiAllegato().setEnte(getEnte());
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		List<StatoOperativoElencoDocumenti> list = new ArrayList<StatoOperativoElencoDocumenti>();
		if(getStatoOperativoElencoDocumenti() != null) {
			list.add(getStatoOperativoElencoDocumenti());
		}
		request.setStatiOperativiElencoDocumenti(list);
		
		return request;
	}
	
	/* **** Utilities **** */
	/**
	 * Computa la denominazione dell'allegato atto.
	 * 
	 * @return la denominazione a partire dall'allegato atto
	 */
	protected String computeDenominazioneAllegatoAtto() {
		StringBuilder sb = new StringBuilder();
		final String delim = "/";
		sb.append("Allegato atto ")
			.append(getAttoAmministrativo().getAnno())
			.append(delim)
			.append(getAttoAmministrativo().getNumero())
			.append(delim)
			.append(getAttoAmministrativo().getTipoAtto().getCodice());
		if(getAttoAmministrativo().getStrutturaAmmContabile() != null) {
			sb.append(delim)
				.append(getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
		}
		return sb.toString();
	}
	
	/**
	 * Ottiene la descrizione completa del soggetto fornito in input.
	 * 
	 * @param soggetto il soggetto la cui descrizione &eacute; da essere calcolata
	 * 
	 * @return la descrizione completa del soggetto
	 */
	protected String computeDescrizioneCompletaSoggetto(final Soggetto soggetto) {
		if(soggetto == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		addIfNotBlank(chunks, soggetto.getCodiceSoggetto());
		addIfNotBlank(chunks, soggetto.getDenominazione());
		addIfNotBlank(chunks, soggetto.getCodiceFiscale());
		return StringUtils.join(chunks, " - ");
	}

	/**
	 * Aggiunge la stringa alla lista nel caso in cui non sia vuota.
	 * 
	 * @param list la lista da popolare
	 * @param str la strginga da utilizzare
	 */
	protected void addIfNotBlank(List<String> list, String str) {
		if(StringUtils.isNotBlank(str)) {
			list.add(str);
		}
	}
	
	/**
	 * Ottiene la descrizione completa dell'atto amministrativo fornito in input.
	 * 
	 * @param attoAmministrativo l'atto amministrativo la cui descrizione &eacute; da essere calcolata
	 * 
	 * @return la descrizione completa dell'atto
	 */
	protected String computeDescrizioneCompletaAttoAmministrativo(final AttoAmministrativo attoAmministrativo) {
		final StringBuilder sb = new StringBuilder();
		final String separatorSlash = "/";
		final String separatorDash = "-";
		if(attoAmministrativo != null) {
			sb.append(attoAmministrativo.getAnno())
				.append(separatorSlash)
				.append(attoAmministrativo.getNumero());
			if(attoAmministrativo.getTipoAtto() != null) {
				sb.append(separatorDash)
					.append(attoAmministrativo.getTipoAtto().getCodice())
					.append(separatorSlash)
					.append(attoAmministrativo.getTipoAtto().getDescrizione());
			}
			sb.append(attoAmministrativo.getOggetto());
			if(attoAmministrativo.getStrutturaAmmContabile() != null) {
				sb.append(separatorDash)
					.append(attoAmministrativo.getStrutturaAmmContabile().getCodice());
			}
			sb.append(" - Stato: ")
				.append(attoAmministrativo.getStatoOperativo());
		}
		return sb.toString();
	}
}
