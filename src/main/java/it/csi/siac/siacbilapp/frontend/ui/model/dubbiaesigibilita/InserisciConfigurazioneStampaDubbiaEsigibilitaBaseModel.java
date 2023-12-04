/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconet;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoMediaPrescelta;
import it.csi.siac.siacbilser.model.fcde.TipologiaEstrazioniFogliDiCalcolo;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe base di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * @author Marchino Alessandro
 */
public abstract class InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2437882777856288808L;
	
	
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa;
	
	private AttributiBilancio attributiBilancio;
	private boolean attributiBilancioPresenti;
	private Integer indiceAccantonamento;

	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TipologiaTitolo> listaTipologiaTitolo = new ArrayList<TipologiaTitolo>();
	private List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = new ArrayList<CategoriaTipologiaTitolo>();
	private List<ElementoPianoDeiConti> listaElementoPianoDeiConti = new ArrayList<ElementoPianoDeiConti>();
	private List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = new ArrayList<StrutturaAmministrativoContabile>();
	
	private List<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio>();
	
	// SIAC-4469
	private boolean datiAnnoPrecedentePresenti;
	private Integer idOperazioneAsincrona;
	
	// Workaorund 
	private String salvataggio;
	
	// SIAC-8421
	private String sceltaUtente;
	
	// Stampa
	private String contentType;
	private Long contentLength;
	private String fileName;
	private InputStream inputStream;
	private Set<Cookie> cookies;
	
	/**
	 * @return the attributiBilancio
	 */
	public AttributiBilancio getAttributiBilancio() {
		return attributiBilancio;
	}

	/**
	 * @param attributiBilancio the attributiBilancio to set
	 */
	public void setAttributiBilancio(AttributiBilancio attributiBilancio) {
		this.attributiBilancio = attributiBilancio;
	}

	/**
	 * @return the attributiBilancioPresenti
	 */
	public boolean isAttributiBilancioPresenti() {
		return attributiBilancioPresenti;
	}

	/**
	 * @param attributiBilancioPresenti the attributiBilancioPresenti to set
	 */
	public void setAttributiBilancioPresenti(boolean attributiBilancioPresenti) {
		this.attributiBilancioPresenti = attributiBilancioPresenti;
	}

	/**
	 * @return the indiceAccantonamento
	 */
	public Integer getIndiceAccantonamento() {
		return this.indiceAccantonamento;
	}

	/**
	 * @param indiceAccantonamento the indiceAccantonamento to set
	 */
	public void setIndiceAccantonamento(Integer indiceAccantonamento) {
		this.indiceAccantonamento = indiceAccantonamento;
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
	 * @return the listaTipologiaTitolo
	 */
	public List<TipologiaTitolo> getListaTipologiaTitolo() {
		return listaTipologiaTitolo;
	}

	/**
	 * @param listaTipologiaTitolo the listaTipologiaTitolo to set
	 */
	public void setListaTipologiaTitolo(List<TipologiaTitolo> listaTipologiaTitolo) {
		this.listaTipologiaTitolo = listaTipologiaTitolo != null ? listaTipologiaTitolo : new ArrayList<TipologiaTitolo>();
	}

	/**
	 * @return the listaCategoriaTipologiaTitolo
	 */
	public List<CategoriaTipologiaTitolo> getListaCategoriaTipologiaTitolo() {
		return listaCategoriaTipologiaTitolo;
	}

	/**
	 * @param listaCategoriaTipologiaTitolo the listaCategoriaTipologiaTitolo to set
	 */
	public void setListaCategoriaTipologiaTitolo(List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo) {
		this.listaCategoriaTipologiaTitolo = listaCategoriaTipologiaTitolo != null ? listaCategoriaTipologiaTitolo : new ArrayList<CategoriaTipologiaTitolo>();
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
	 * @return the listaStrutturaAmministrativoContabile
	 */
	public List<StrutturaAmministrativoContabile> getListaStrutturaAmministrativoContabile() {
		return listaStrutturaAmministrativoContabile;
	}

	/**
	 * @param listaStrutturaAmministrativoContabile the listaStrutturaAmministrativoContabile to set
	 */
	public void setListaStrutturaAmministrativoContabile(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		this.listaStrutturaAmministrativoContabile = listaStrutturaAmministrativoContabile != null ? listaStrutturaAmministrativoContabile : new ArrayList<StrutturaAmministrativoContabile>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> getListaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion() {
		return this.listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion the listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion(List<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion = listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion != null ? listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio>();
	}

	/**
	 * @return the datiAnnoPrecedentePresenti
	 */
	public boolean isDatiAnnoPrecedentePresenti() {
		return datiAnnoPrecedentePresenti;
	}

	/**
	 * @param datiAnnoPrecedentePresenti the datiAnnoPrecedentePresenti to set
	 */
	public void setDatiAnnoPrecedentePresenti(boolean datiAnnoPrecedentePresenti) {
		this.datiAnnoPrecedentePresenti = datiAnnoPrecedentePresenti;
	}

	/**
	 * @return the idOperazioneAsincrona
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
	}

	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincrona to set
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}
	
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	}
	
	public boolean isStatoAccantonamentoFondiDubbiaEsigibilitaDefinitivo() {
		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null && 
			 StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA.equals(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getStatoAccantonamentoFondiDubbiaEsigibilita());
	}

	public void setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		this.accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	}
	
	/**
	 * @return the accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa() {
		return this.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa;
	}

	/**
	 * @param accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa the accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa to set
	 */
	public void setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa) {
		this.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa;
	}

	/**
	 * @return the salvataggio
	 */
	public String getSalvataggio() {
		return salvataggio;
	}

	/**
	 * @param salvataggio the salvataggio to set
	 */
	public void setSalvataggio(String salvataggio) {
		this.salvataggio = salvataggio;
	}

	/**
	 * @return the sceltaUtente
	 */
	public String getSceltaUtente() {
		return sceltaUtente;
	}

	/**
	 * @param sceltaUtente the sceltaUtente to set
	 */
	public void setSceltaUtente(String sceltaUtente) {
		this.sceltaUtente = sceltaUtente;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the contentLength
	 */
	public Long getContentLength() {
		return this.contentLength;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return this.inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the cookies
	 */
	public Set<Cookie> getCookies() {
		return cookies;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(Set<Cookie> cookies) {
		this.cookies = cookies;
	}

	/**
	 * @return the elencoTitoloEntrataAccantonamenti
	 */
	public abstract Collection<TitoloEntrata> getElencoTitoloEntrataAccantonamenti();
	/**
	 * @return the elencoTipologiaTitoloAccantonamenti
	 */
	public abstract Collection<TipologiaTitolo> getElencoTipologiaTitoloAccantonamenti();
	
	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link RicercaAttributiBilancio}.
	 * @return la request creata
	 */
	public RicercaAttributiBilancio creaRequestRicercaAttributiBilancio() {
		RicercaAttributiBilancio request = creaRequest(RicercaAttributiBilancio.class);
		request.setBilancio(getBilancio());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaAttributiBilancio}.
	 * @return la request creata
	 */
	public AggiornaAttributiBilancio creaRequestAggiornaAttributiBilancio() {
		AggiornaAttributiBilancio request = creaRequest(AggiornaAttributiBilancio.class);
	
		request.setBilancio(getBilancio());
		request.setAttributiBilancio(getAttributiBilancio());
	
		return request;
	}

	/**
	 * Rimozione dei capitoli dalla lista dei temporanei
	 * @param temporanei i dati temporanei
	 * @param selezionati i dati selezionati
	 * @param <AFDE> la tipizzazione dell'accantonamento
	 */
	protected <AFDE extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> void rimuoviCapitoliDaTemp(Iterable<AFDE> temporanei, Iterable<AFDE> selezionati) {
		for (Iterator<AFDE> itTemp = temporanei.iterator(); itTemp.hasNext();) {
			AFDE aTemp = itTemp.next();
			for (Iterator<AFDE> itSel = selezionati.iterator(); itSel.hasNext();) {
				AFDE aSel = itSel.next();
				
				if (aTemp.getUid() == aSel.getUid()) {
					itTemp.remove();
					itSel.remove();
					
					break;
				}
			}
		}
	}
	
	/**
	 * Gestisce gli attributi di bilancio
	 * @param ab gliattributi
	 */
	public void handleAttributiBilancio(AttributiBilancio ab) {
		setAttributiBilancio(ab == null ? new AttributiBilancio() : ab);
		checkIfAttributiBilancioPresent();
		setDefaultValuesAttributiBilancio();
	}
	
	/**
	 * Controlla se gli attributi del bilancio siano presenti
	 */
	private void checkIfAttributiBilancioPresent() {
		boolean attributiBilancioNonPresenti = getAttributiBilancio().getAccantonamentoGraduale() == null
				&& getAttributiBilancio().getPercentualeAccantonamentoAnno() == null
				&& getAttributiBilancio().getPercentualeAccantonamentoAnno1() == null
				&& getAttributiBilancio().getPercentualeAccantonamentoAnno2() == null
				&& getAttributiBilancio().getRiscossioneVirtuosa() == null 
				&& getAttributiBilancio().getMediaApplicata() == null
				&& getAttributiBilancio().getUltimoAnnoApprovato() == null;
		
		setAttributiBilancioPresenti(!attributiBilancioNonPresenti);
	}
	
	/**
	 * Imposta i valori di default per gli attributi di bilancio
	 */
	protected void setDefaultValuesAttributiBilancio() {
		if (getAttributiBilancio().getAccantonamentoGraduale() == null) {
			getAttributiBilancio().setAccantonamentoGraduale(Boolean.FALSE);
		}
	
		if (getAttributiBilancio().getRiscossioneVirtuosa() == null) {
			getAttributiBilancio().setRiscossioneVirtuosa(Boolean.FALSE);
		}
	
		if (getAttributiBilancio().getMediaApplicata() == null) {
			getAttributiBilancio().setMediaApplicata(TipoMediaPrescelta.SEMPLICE);
		}
	}
	
	public SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
			TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita, StatoAccantonamentoFondiDubbiaEsigibilita statoAccantonamentoFondiDubbiaEsigibilita) {

		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setBilancio(getBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setTipoAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita);;
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setStatoAccantonamentoFondiDubbiaEsigibilita(statoAccantonamentoFondiDubbiaEsigibilita);
		
		return req;
	}

	public CalcolaImportiPerAllegatoArconet creaRequestCalcolaImportiAllegatoArconet() {
		CalcolaImportiPerAllegatoArconet req = creaRequest(CalcolaImportiPerAllegatoArconet.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setBilancio(getBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		
		return req;
	}
	
	public SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioNuovo(TipoAccantonamentoFondiDubbiaEsigibilita tipo) {

		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		// Copia parametri
		impostaAttributiBilancio(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		
		return req;
	}
	
	protected abstract void impostaAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio2);
	
	public RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributi) {

		RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(attributi);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails(
			AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato
		);
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport}.
	 * @return la request creata
	 * 
	 * SIAC-7858 21/06/2021 CM 
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport creaRequestStampaExcelAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport request = creaRequest(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport.class);

		request.setEnte(getEnte());
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());

		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport}.
	 * Richiesta parametrizzata per scalare sulle varie stampe secondarie legate al tipo di accantonamento.
	 * @return la request creata
	 * 
	 * SIAC-7858 21/06/2021 CM 
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport creaRequestStampaExcelAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(TipologiaEstrazioniFogliDiCalcolo tipologia) {
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport request = creaRequest(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport.class);
		
		request.setEnte(getEnte());
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		request.setTipologia(tipologia);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio}.
	 * @param stato lo statoda applicare
	 * @return la request creata
	 * 
	 * SIAC-7858 04/06/2021 CM 
	 */
	public ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(StatoAccantonamentoFondiDubbiaEsigibilita stato) {
		ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio request = creaRequest(ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		request.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setUid(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid());
		request.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setStatoAccantonamentoFondiDubbiaEsigibilita(stato);
		request.setSceltaUtente(getSceltaUtente());

		return request;
	}

	public abstract RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
	
	public abstract RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();

	public abstract ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestInizializzaioneAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
	
	/**
	 * Metodo di utilit&agrave; per la formattazione delle date sulle JSP
	 * @param date la data da formattare
	 * @return la data
	 */
	public String formatDate(Date date) {
		return FormatUtils.formatDate(date);
	}
	
	protected <A extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> A getAccantonamentoByIndice(List<A> listaAccantonamentoFondiDubbiaEsigibilita) {
		return indiceAccantonamento == null || indiceAccantonamento < 0
			? null
			: indiceAccantonamento >= listaAccantonamentoFondiDubbiaEsigibilita.size()
				? null
				: listaAccantonamentoFondiDubbiaEsigibilita.get(indiceAccantonamento);
	}

	/**
	 * Workaround per evitare di aumentare il numero massimo dei parametri gestibili da una chiamata
	 */
	@SuppressWarnings("unchecked")
	protected <A extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> List<A> popolaListaDatiSalvataggio(A acc, List<A> list) {
		
		String[] righe = getSalvataggio().split(":");

		if(righe != null && righe.length > 0 && StringUtils.isNotBlank(righe[0])) {
		
			for (String riga : righe) {
				String[] elementiRiga = riga.split(";");
				
				if(elementiRiga != null && elementiRiga.length > 0 && StringUtils.isNotBlank(elementiRiga[0])) {
					
					A accantonamento = (A) CoreUtil.instantiateNewClass(acc.getClass());
					
					popolaCampi(accantonamento, elementiRiga, accantonamento.getTipoAccantonamentoFondiDubbiaEsigiblita());
					
					list.add(accantonamento);
				}
			}
		}
		
		return list;
	}

	private <A extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> void popolaCampi(A accantonamento, String[] elementiRiga,
			TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		
		switch (tipo) {
			case PREVISIONE:
				accantonamento.setUid(StringUtils.isBlank(elementiRiga[0]) ? 0 : Integer.parseInt(elementiRiga[0]));
				accantonamento.setNumeratore(StringUtils.isBlank(elementiRiga[1]) ? null : new BigDecimal(elementiRiga[1]));
				accantonamento.setNumeratore1(StringUtils.isBlank(elementiRiga[2]) ? null : new BigDecimal(elementiRiga[2]));
				accantonamento.setNumeratore2(StringUtils.isBlank(elementiRiga[3]) ? null : new BigDecimal(elementiRiga[3]));
				accantonamento.setNumeratore3(StringUtils.isBlank(elementiRiga[4]) ? null : new BigDecimal(elementiRiga[4]));
				accantonamento.setNumeratore4(StringUtils.isBlank(elementiRiga[5]) ? null : new BigDecimal(elementiRiga[5]));
				accantonamento.setDenominatore(StringUtils.isBlank(elementiRiga[6]) ? null : new BigDecimal(elementiRiga[6]));
				accantonamento.setDenominatore1(StringUtils.isBlank(elementiRiga[7]) ? null : new BigDecimal(elementiRiga[7]));
				accantonamento.setDenominatore2(StringUtils.isBlank(elementiRiga[8]) ? null : new BigDecimal(elementiRiga[8]));
				accantonamento.setDenominatore3(StringUtils.isBlank(elementiRiga[9]) ? null : new BigDecimal(elementiRiga[9]));
				accantonamento.setDenominatore4(StringUtils.isBlank(elementiRiga[10]) ? null : new BigDecimal(elementiRiga[10]));
				accantonamento.setMediaUtente(StringUtils.isBlank(elementiRiga[11]) ? null : new BigDecimal(elementiRiga[11]));
				//SIAC-8393 e SIAC-8394
				accantonamento.setAccantonamento(StringUtils.isBlank(elementiRiga[12]) ? null : new BigDecimal(elementiRiga[12]));
				accantonamento.setAccantonamento1(StringUtils.isBlank(elementiRiga[13]) ? null : new BigDecimal(elementiRiga[13]));
				accantonamento.setAccantonamento2(StringUtils.isBlank(elementiRiga[14]) ? null : new BigDecimal(elementiRiga[14]));
				accantonamento.setTipoMediaPrescelta(TipoMediaAccantonamentoFondiDubbiaEsigibilita.byCodice(elementiRiga[15]));
				return;
			case GESTIONE:
				accantonamento.setUid(StringUtils.isBlank(elementiRiga[0]) ? 0 : Integer.parseInt(elementiRiga[0]));
				accantonamento.setNumeratore(StringUtils.isBlank(elementiRiga[1]) ? null : new BigDecimal(elementiRiga[1]));
				accantonamento.setDenominatore(StringUtils.isBlank(elementiRiga[2]) ? null : new BigDecimal(elementiRiga[2]));
				accantonamento.setMediaUtente(StringUtils.isBlank(elementiRiga[3]) ? null : new BigDecimal(elementiRiga[3]));
				//SIAC-8513 per la gestione si aggiunge la media semplice totali
				accantonamento.setMediaSempliceTotali(StringUtils.isBlank(elementiRiga[4]) ? null : new BigDecimal(elementiRiga[4]));
				//SIAC-8393 e SIAC-8394
				accantonamento.setAccantonamento(StringUtils.isBlank(elementiRiga[5]) ? null : new BigDecimal(elementiRiga[5]));
				accantonamento.setAccantonamento1(StringUtils.isBlank(elementiRiga[6]) ? null : new BigDecimal(elementiRiga[6]));
				accantonamento.setAccantonamento2(StringUtils.isBlank(elementiRiga[7]) ? null : new BigDecimal(elementiRiga[7]));
				accantonamento.setTipoMediaPrescelta(TipoMediaAccantonamentoFondiDubbiaEsigibilita.byCodice(elementiRiga[8]));
				return;
			case RENDICONTO:
				accantonamento.setUid(StringUtils.isBlank(elementiRiga[0]) ? 0 : Integer.parseInt(elementiRiga[0]));
				accantonamento.setNumeratore(StringUtils.isBlank(elementiRiga[1]) ? null : new BigDecimal(elementiRiga[1]));
				accantonamento.setNumeratore1(StringUtils.isBlank(elementiRiga[2]) ? null : new BigDecimal(elementiRiga[2]));
				accantonamento.setNumeratore2(StringUtils.isBlank(elementiRiga[3]) ? null : new BigDecimal(elementiRiga[3]));
				accantonamento.setNumeratore3(StringUtils.isBlank(elementiRiga[4]) ? null : new BigDecimal(elementiRiga[4]));
				accantonamento.setNumeratore4(StringUtils.isBlank(elementiRiga[5]) ? null : new BigDecimal(elementiRiga[5]));
				accantonamento.setDenominatore(StringUtils.isBlank(elementiRiga[6]) ? null : new BigDecimal(elementiRiga[6]));
				accantonamento.setDenominatore1(StringUtils.isBlank(elementiRiga[7]) ? null : new BigDecimal(elementiRiga[7]));
				accantonamento.setDenominatore2(StringUtils.isBlank(elementiRiga[8]) ? null : new BigDecimal(elementiRiga[8]));
				accantonamento.setDenominatore3(StringUtils.isBlank(elementiRiga[9]) ? null : new BigDecimal(elementiRiga[9]));
				accantonamento.setDenominatore4(StringUtils.isBlank(elementiRiga[10]) ? null : new BigDecimal(elementiRiga[10]));
				accantonamento.setMediaUtente(StringUtils.isBlank(elementiRiga[11]) ? null : new BigDecimal(elementiRiga[11]));
				accantonamento.setAccantonamento(StringUtils.isBlank(elementiRiga[12]) ? null : new BigDecimal(elementiRiga[12]));
				((AccantonamentoFondiDubbiaEsigibilitaRendiconto) accantonamento).setResiduoFinaleCapitolo(StringUtils.isBlank(elementiRiga[13]) ? null : new BigDecimal(elementiRiga[13]));
				accantonamento.setTipoMediaPrescelta(TipoMediaAccantonamentoFondiDubbiaEsigibilita.byCodice(elementiRiga[14]));
				return;
			default:
				return;
		}
	}
}
