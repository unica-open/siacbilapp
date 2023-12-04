/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step1.Scelta;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step1.SceltaVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step2.DefinisciVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoUEBModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step4.RiepilogoVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * Classe di model per l'inserimento di una Variazione. Contiene una mappatura del model.
 * 
 * @author Pro-Logic
 * @version 1.0.0 - 09/09/2013
 *
 */
public class InserisciVariazioneModel extends GenericBilancioModel {
	
	private static final long serialVersionUID = -242133523450910036L;
	
	private SceltaVariazioneModel scelta;
	private DefinisciVariazioneModel definisci;
	private SpecificaVariazioneImportoModel specificaImporti;
	private SpecificaVariazioneImportoUEBModel specificaUEB;
	private SpecificaVariazioneCodifiche specificaCodifiche;
	private RiepilogoVariazioneModel riepilogo;
	//nuovo maggio 2016
	private BigDecimal totaleStanziamentiEntrata;
	private BigDecimal totaleStanziamentiCassaEntrata;
	private BigDecimal totaleStanziamentiResiduiEntrata;
	private BigDecimal totaleStanziamentiSpesa;
	private BigDecimal totaleStanziamentiCassaSpesa;
	private BigDecimal totaleStanziamentiResiduiSpesa;
	
	private FaseBilancio faseBilancio;

	private int uidVariazione;
	private Integer numeroVariazione;
	private StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio;
	
	private Integer idOperazioneAsincrona;

	private Boolean isAsyncResponsePresent = Boolean.FALSE;
	
	//SIAC-4737
	//FIXME: per far in modo OGNL setti in modo corretto l'atto amministrativo nel model definisci
	@SuppressWarnings("unused")
	private AttoAmministrativo attoAmministrativo;
	@SuppressWarnings("unused")
	private AttoAmministrativo attoAmministrativoAggiuntivo;
	
	// SIAC-5016
	private Boolean isXlsx;
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	
	//SIAC-6884
	private Boolean isDecentrato;
		
	public boolean getIsDecentrato() {
		if(isDecentrato != null)
			return isDecentrato;
		return false;
	}

	public void setIsDecentrato(Boolean isDecentrato) {
		this.isDecentrato = isDecentrato;
	}
	
	/** Costruttore vuoto di default */
	public InserisciVariazioneModel() {
		scelta = new SceltaVariazioneModel();
		definisci = new DefinisciVariazioneModel();
		specificaImporti = new SpecificaVariazioneImportoModel();
		specificaUEB = new SpecificaVariazioneImportoUEBModel();
		specificaCodifiche = new SpecificaVariazioneCodifiche();
		riepilogo = new RiepilogoVariazioneModel();
		
		setTitolo("Inserimento variazioni");
	}
	
	/**
	 * @return the scelta
	 */
	public SceltaVariazioneModel getScelta() {
		return scelta;
	}

	/**
	 * @param scelta the scelta to set
	 */
	public void setScelta(SceltaVariazioneModel scelta) {
		this.scelta = scelta;
	}

	/**
	 * @return the definisci
	 */
	public DefinisciVariazioneModel getDefinisci() {
		return definisci;
	}
	
	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return definisci.getListaTipoAtto();
	}
	
	/**
	 * Gets the atto amministrativo.
	 *
	 * @return the atto amministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return definisci.getAttoAmministrativo();
	}	
	
	/**
	 * @param attoAmministrativo the attoAmministrativoToSet 
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		definisci.setAttoAmministrativo(attoAmministrativo);
	}
	
	/**Setta l'atto amministrativo nel modello del definisci (SIAC-4737)
	 * @param attoAmministrativoAggiuntivo the attoAmministrativoAggiuntivo to set 
	 */
	public void setAttoAmministrativoAggiuntivo(AttoAmministrativo attoAmministrativoAggiuntivo) {
		definisci.setAttoAmministrativoAggiuntivo(attoAmministrativoAggiuntivo);
	}
	
	/**
	 * Gets the atto amministrativo aggiuntivo.
	 *
	 * @return the atto amministrativo
	 */
	public AttoAmministrativo getAttoAmministrativoAggiuntivo() {
		return definisci.getAttoAmministrativoAggiuntivo();
	}	
	
	/**
	 * @return the stringa provvedimento
	 */
	public String getStringaProvvedimento() {
		 
		String prefissoDesc = "provvedimento " + (Scelta.IMPORTI.equals(getScelta().getScelta())? "variazione di PEG " : "");
		if(getDefinisci()!=null && getDefinisci().getAttoAmministrativo()!= null && getDefinisci().getAttoAmministrativo().getUid() != 0){
			return prefissoDesc + getDescrizioneDaProvvedimento(getDefinisci().getAttoAmministrativo());
		}
		return prefissoDesc;
	}
	
	/**
	 * @return the stringa provvedimento aggiuntivo
	 */
	public String getStringaProvvedimentoAggiuntivo() {
		
		String prefissoDesc = "provvedimento variazione di bilancio";
		if(getDefinisci()!=null && getDefinisci().getAttoAmministrativoAggiuntivo()!= null && getDefinisci().getAttoAmministrativoAggiuntivo().getUid() != 0){
			return prefissoDesc + getDescrizioneDaProvvedimento(getDefinisci().getAttoAmministrativoAggiuntivo());
		}
		return prefissoDesc;
	}

	/**
	 * Ottiene la descrizione da provvedimento
	 * @param attoAmministrativo l'atto amministrativo
	 * @return la descrizione
	 */
	protected String getDescrizioneDaProvvedimento(AttoAmministrativo attoAmministrativo) {
		StringBuilder sb = new StringBuilder();
		sb.append(": ")
		.append(attoAmministrativo.getAnno())
		.append(" / ")
		.append(attoAmministrativo.getNumero());
		return sb.toString();
	}

	/**
	 * @param definisci the definisci to set
	 */
	public void setDefinisci(DefinisciVariazioneModel definisci) {
		this.definisci = definisci;
	}
	
	/**
	 * @return the specificaImporti
	 */
	public SpecificaVariazioneImportoModel getSpecificaImporti() {
		return specificaImporti;
	}

	/**
	 * @param specificaImporti the specificaImporti to set
	 */
	public void setSpecificaImporti(SpecificaVariazioneImportoModel specificaImporti) {
		this.specificaImporti = specificaImporti;
	}

	/**
	 * @return the specificaUEB
	 */
	public SpecificaVariazioneImportoUEBModel getSpecificaUEB() {
		return specificaUEB;
	}

	/**
	 * @param specificaUEB the specificaUEB to set
	 */
	public void setSpecificaUEB(SpecificaVariazioneImportoUEBModel specificaUEB) {
		this.specificaUEB = specificaUEB;
	}

	/**
	 * @return the specificaCodifiche
	 */
	public SpecificaVariazioneCodifiche getSpecificaCodifiche() {
		return specificaCodifiche;
	}

	/**
	 * @param specificaCodifiche the specificaCodifiche to set
	 */
	public void setSpecificaCodifiche(SpecificaVariazioneCodifiche specificaCodifiche) {
		this.specificaCodifiche = specificaCodifiche;
	}

	/**
	 * @return the riepilogo
	 */
	public RiepilogoVariazioneModel getRiepilogo() {
		return riepilogo;
	}

	/**
	 * @param riepilogo the riepilogo to set
	 */
	public void setRiepilogo(RiepilogoVariazioneModel riepilogo) {
		this.riepilogo = riepilogo;
	}
	
	/**
	 * @return the uidVariazioneImportoCapitolo
	 */
	public int getUidVariazione() {
		return uidVariazione;
	}

	/**
	 * @param uidVariazione the uidVariazioneImportoCapitolo to set
	 */
	public void setUidVariazione(int uidVariazione) {
		this.uidVariazione = uidVariazione;
	}
	
	/**
	 * @return the totaleStanziamentiEntrata
	 */
	public BigDecimal getTotaleStanziamentiEntrata() {
		return totaleStanziamentiEntrata;
	}

	/**
	 * @param totaleStanziamentiEntrata the totaleStanziamentiEntrata to set
	 */
	public void setTotaleStanziamentiEntrata(BigDecimal totaleStanziamentiEntrata) {
		this.totaleStanziamentiEntrata = totaleStanziamentiEntrata;
	}

	/**
	 * @return the totaleStanziamentiCassaEntrata
	 */
	public BigDecimal getTotaleStanziamentiCassaEntrata() {
		return totaleStanziamentiCassaEntrata;
	}

	/**
	 * @param totaleStanziamentiCassaEntrata the totaleStanziamentiCassaEntrata to set
	 */
	public void setTotaleStanziamentiCassaEntrata(BigDecimal totaleStanziamentiCassaEntrata) {
		this.totaleStanziamentiCassaEntrata = totaleStanziamentiCassaEntrata;
	}

	/**
	 * @return the totaleStanziamentiResiduiEntrata
	 */
	public BigDecimal getTotaleStanziamentiResiduiEntrata() {
		return totaleStanziamentiResiduiEntrata;
	}

	/**
	 * @param totaleStanziamentiResiduiEntrata the totaleStanziamentiResiduiEntrata to set
	 */
	public void setTotaleStanziamentiResiduiEntrata(BigDecimal totaleStanziamentiResiduiEntrata) {
		this.totaleStanziamentiResiduiEntrata = totaleStanziamentiResiduiEntrata;
	}

	/**
	 * @return the totaleStanziamentiSpesa
	 */
	public BigDecimal getTotaleStanziamentiSpesa() {
		return totaleStanziamentiSpesa;
	}

	/**
	 * @param totaleStanziamentiSpesa the totaleStanziamentiSpesa to set
	 */
	public void setTotaleStanziamentiSpesa(BigDecimal totaleStanziamentiSpesa) {
		this.totaleStanziamentiSpesa = totaleStanziamentiSpesa;
	}

	/**
	 * @return the totaleStanziamentiCassaSpesa
	 */
	public BigDecimal getTotaleStanziamentiCassaSpesa() {
		return totaleStanziamentiCassaSpesa;
	}

	/**
	 * @param totaleStanziamentiCassaSpesa the totaleStanziamentiCassaSpesa to set
	 */
	public void setTotaleStanziamentiCassaSpesa(BigDecimal totaleStanziamentiCassaSpesa) {
		this.totaleStanziamentiCassaSpesa = totaleStanziamentiCassaSpesa;
	}

	/**
	 * @return the totaleStanziamentiResiduiSpesa
	 */
	public BigDecimal getTotaleStanziamentiResiduiSpesa() {
		return totaleStanziamentiResiduiSpesa;
	}

	/**
	 * @param totaleStanziamentiResiduiSpesa the totaleStanziamentiResiduiSpesa to set
	 */
	public void setTotaleStanziamentiResiduiSpesa(BigDecimal totaleStanziamentiResiduiSpesa) {
		this.totaleStanziamentiResiduiSpesa = totaleStanziamentiResiduiSpesa;
	}

	/**
	 * @return the faseBilancio
	 */
	public FaseBilancio getFaseBilancio() {
		return faseBilancio;
	}

	/**
	 * @param faseBilancio the faseBilancio to set
	 */
	public void setFaseBilancio(FaseBilancio faseBilancio) {
		this.faseBilancio = faseBilancio;
	}



	/**
	 * @return the numeroVariazione
	 */
	public Integer getNumeroVariazione() {
		return numeroVariazione;
	}

	/**
	 * @param numeroVariazione the numeroVariazione to set
	 */
	public void setNumeroVariazione(Integer numeroVariazione) {
		this.numeroVariazione = numeroVariazione;
	}

	/**
	 * @return the statoOperativoVariazioneBilancio
	 */
	public StatoOperativoVariazioneBilancio getStatoOperativoVariazioneDiBilancio() {
		return statoOperativoVariazioneBilancio;
	}

	/**
	 * @param statoOperativoVariazioneBilancio the statoOperativoVariazioneBilancio to set
	 */
	public void setStatoOperativoVariazioneDiBilancio(
			StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio) {
		this.statoOperativoVariazioneBilancio = statoOperativoVariazioneBilancio;
	}
	
	/**
	 * @return the asyncServiceResponse
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
	}

	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincronaToSet
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}

	/**
	 * @return the isAsyncResponsePresent 
	 */
	public Boolean getIsAsyncResponsePresent() {
		return isAsyncResponsePresent;
	}

	/**
	 * @param isAsyncResponsePresent the isAsyncResponsePresent to set
	 */
	public void setIsAsyncResponsePresent(Boolean isAsyncResponsePresent) {
		this.isAsyncResponsePresent = isAsyncResponsePresent;
	}
	
	/**
	 * @return the isXlsx
	 */
	public Boolean getIsXlsx() {
		return isXlsx;
	}

	/**
	 * @param isXlsx the isXlsx to set
	 */
	public void setIsXlsx(Boolean isXlsx) {
		this.isXlsx = isXlsx;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
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
		return contentLength;
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
		return fileName;
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
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * Verifica se il residuo sia modificabile
	 * @return <code>true</code> se &egrave; possibile modificare il residuo tramite la variazione, <code>false</code> altrimenti
	 */
	public boolean getIsResiduoEditabile(){	
		return getAnnoEsercizioInt().equals(getDefinisci().getAnnoVariazione());
	}

	
	/**
	 * Verifica se la cassa sia modificabile
	 * @return <code>true</code> se &egrave; possibile modificare la cassa tramite la variazione, <code>false</code> altrimenti
	 */
	public boolean getIsCassaEditabile(){	
		return getAnnoEsercizioInt().equals(getDefinisci().getAnnoVariazione());
	}

	/* **** Requests base **** */
	
	@Override
	public RicercaTipiAttoDiLegge creaRequestRicercaTipiAttoDiLegge() {
		RicercaTipiAttoDiLegge request = new RicercaTipiAttoDiLegge();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Costruisce una request per il servizio di {@link InserisceVariazioneCodifiche} a partire dal model.
	 * 
	 * @return la request creata
	 * @throws IllegalArgumentException nel caso di errore di injezione dei parametri
	 */
	public InserisceVariazioneCodifiche creaRequestInserisceVariazioneCodifiche() {
		InserisceVariazioneCodifiche request = new InserisceVariazioneCodifiche();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setVariazioneCodificaCapitolo(creaUtilityVariazioneCodificheCapitolo());
		
		// Flags per Bonita
		request.setInvioOrganoAmministrativo(definisci.getVariazioneAOrganoAmministativo());
		request.setInvioOrganoLegislativo(definisci.getVariazioneAOrganoLegislativo());
		
		return request;
	}
	
	/**
	 * Crea una request per il controllo della modificabilit&agrave; dei classificatori associati al capitolo.
	 * 
	 * @param elementoCapitoloCodifiche l'elemento rispetto al quale creare la request
	 * 
	 * @return la request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		ControllaClassificatoriModificabiliCapitolo request = new ControllaClassificatoriModificabiliCapitolo();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setModalitaAggiornamento(true);
		request.setRichiedente(getRichiedente());
		
		request.setNumeroCapitolo(elementoCapitoloCodifiche.getNumeroCapitolo());
		request.setNumeroArticolo(elementoCapitoloCodifiche.getNumeroArticolo());
		request.setTipoCapitolo(elementoCapitoloCodifiche.getTipoCapitolo());
		
		return request;
	}
	
	/**
	 * Crea una request per il controllo della modificabilit&agrave; degli attributi associati al capitolo.
	 * 
	 * @param elementoCapitoloCodifiche l'elemento rispetto al quale creare la request
	 * 
	 * @return la request creata
	 */
	public ControllaAttributiModificabiliCapitolo creaRequestControllaAttributiModificabiliCapitolo(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		ControllaAttributiModificabiliCapitolo request = new ControllaAttributiModificabiliCapitolo();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setModalitaAggiornamento(true);
		request.setRichiedente(getRichiedente());
		
		request.setNumeroCapitolo(elementoCapitoloCodifiche.getNumeroCapitolo());
		request.setNumeroArticolo(elementoCapitoloCodifiche.getNumeroArticolo());
		request.setTipoCapitolo(elementoCapitoloCodifiche.getTipoCapitolo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoVariazione} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoVariazione creaRequestRicercaTipoVariazione() {
		RicercaTipoVariazione request = new RicercaTipoVariazione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/* **** Metodi di utilità **** */
	
	/**
	 * Crea un'utilit&agrave; per la variazione degli importi.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private VariazioneImportoCapitolo creaUtilityAnagraficaVariazioneImportoCapitolo() {
		VariazioneImportoCapitolo utility = new VariazioneImportoCapitolo();
		
		AttoAmministrativo aa = definisci.getAttoAmministrativo();
		// Dati da altri model
		if(aa != null && aa.getUid() != 0) {
			utility.setAttoAmministrativo(definisci.getAttoAmministrativo());
		}
		//SIAC-4737
		AttoAmministrativo attoAmministrativoVariazioneDiBilancio = definisci.getAttoAmministrativoAggiuntivo();
		// Dati da altri model
		if(attoAmministrativoVariazioneDiBilancio != null && attoAmministrativoVariazioneDiBilancio.getUid() != 0) {
			utility.setAttoAmministrativoVariazioneBilancio(attoAmministrativoVariazioneDiBilancio);
		}
		utility.setApplicazioneVariazione(definisci.getApplicazione());
		utility.setBilancio(getBilancio());
		utility.setData(new Date());
		utility.setDescrizione(definisci.getDescrizioneVariazione());
		utility.setEnte(getEnte());
		utility.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneBilancio.BOZZA);
		utility.setTipoVariazione(definisci.getTipoVariazione());
		//SIAC-6884
		if(definisci.getDataApertura() != null){
			//si tratta di variazione decentrata
			utility.setDataAperturaProposta(definisci.getDataApertura());
			utility.setDirezioneProponente(definisci.getDirezioneProponente());
		}
		utility.setFlagConsiglio(definisci.getVariazioneAOrganoLegislativo());
		utility.setFlagGiunta(definisci.getVariazioneAOrganoAmministativo());
		return utility;
	}
	
	/**
	 * Crea un'utilit&agrave; per la variazione degli importi.
	 * 
	 * @return l'utilit&agrave; creata
	 * @throws IllegalArgumentException nel caso di errori nell'injezione dei parametri 
	 */
	private VariazioneCodificaCapitolo creaUtilityVariazioneCodificheCapitolo() {
		VariazioneCodificaCapitolo utility = new VariazioneCodificaCapitolo();
		
		AttoAmministrativo aa = definisci.getAttoAmministrativo();
		// Dati da altri model
		if(aa != null && aa.getUid() != 0) {
			utility.setAttoAmministrativo(definisci.getAttoAmministrativo());
		}
		utility.setBilancio(getBilancio());
		utility.setData(new Date());
		utility.setDescrizione(definisci.getDescrizioneVariazione());
		utility.setEnte(getEnte());
		utility.setNote(specificaCodifiche.getNote());
		utility.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneBilancio.BOZZA);
		utility.setTipoVariazione(definisci.getTipoVariazione());
		
		// Dati specifici
		List<DettaglioVariazioneCodificaCapitolo> listaDettaglioVariazioneCodifica = new ArrayList<DettaglioVariazioneCodificaCapitolo>();
		
		// Ottengo la lista da inserire nella variazione dal model della specifica
		List<ElementoCapitoloCodifiche> listaElementoCapitoloCodifiche = specificaCodifiche.getListaElementoCapitoloCodifiche();
		
		for(ElementoCapitoloCodifiche e : listaElementoCapitoloCodifiche) {
			// Swrappo il wrapper
			listaDettaglioVariazioneCodifica.addAll(ElementoCapitoloCodificheFactory.unwrap(e));
		}
		
		utility.setListaDettaglioVariazioneCodifica(listaDettaglioVariazioneCodifica);
		utility.setFlagConsiglio(definisci.getVariazioneAOrganoLegislativo());
		utility.setFlagGiunta(definisci.getVariazioneAOrganoAmministativo());
		return utility;
	}
	
	/**
	 * Impostazione dei dati tra i vari model per la popolazione del riepilogo.
	 * 
	 * @param modelSpecifica il model contenente le informazioni sulla specifica della variazione
	 */
	public void impostaDatiStep4(SpecificaVariazioneModel modelSpecifica) {
		riepilogo.setNumeroVariazione(modelSpecifica.getNumeroVariazione());
		//SIAC-8332-REGP aggiunta la possibilita' che la variazione sia in stato BOZZA o BOZZA-DECb
		riepilogo.setStatoVariazione(modelSpecifica.getStatoVariazione());
		riepilogo.setApplicazioneVariazione(definisci.getApplicazione().getDescrizione());
		riepilogo.setDescrizioneVariazione(definisci.getDescrizioneVariazione());
		riepilogo.setTipoVariazione(definisci.getTipoVariazione());
		riepilogo.setAnnoVariazione(definisci.getAnnoVariazione());
		riepilogo.setNoteVariazione(modelSpecifica.getNote());
	}

	/**
	 * Crea la request per l'inserimento dell'anagrafica della variazione di bilancio
	 * @return the InserisceAnagraficaVariazioneBilancio
	 */
	public InserisceAnagraficaVariazioneBilancio creaRequestInserisceAnagraficaVariazioneDiBilancio() {
		
		InserisceAnagraficaVariazioneBilancio request = creaRequest(InserisceAnagraficaVariazioneBilancio.class);
		VariazioneImportoCapitolo variazione = creaUtilityAnagraficaVariazioneImportoCapitolo();
		request.setInvioOrganoAmministrativo(definisci.getVariazioneAOrganoAmministativo());
		request.setInvioOrganoLegislativo(definisci.getVariazioneAOrganoLegislativo());
		request.setCaricaResidui(definisci.getCaricaResidui());
		request.setVariazioneImportoCapitolo(variazione);
		return request;
		
	}

	/**
	 * Creo la request per collegare un dato capitolo alla variazione
	 * @param dett il dettaglio della variazione
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitolo(DettaglioVariazioneImportoCapitolo dett) {
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaImporti().getElementoCapitoloVariazione();
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(getSpecificaImporti().getElementoCapitoloVariazione().getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		
		dettaglioVariazioneImportoCapitolo.setStanziamento(elementoCapitoloVariazione.getCompetenza());
		dettaglioVariazioneImportoCapitolo.setStanziamentoCassa(elementoCapitoloVariazione.getCassa());
		dettaglioVariazioneImportoCapitolo.setStanziamentoResiduo(elementoCapitoloVariazione.getResiduo());
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazione);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
		
	}

	/**
	 * Creo la request per aggiornare l'anagrafica della variazione di bilancio
	 * @return la request creata
	 */
	public AggiornaAnagraficaVariazioneBilancio creaRequestAggiornaAnagraficaVariazioneBilancio() {
		
		SpecificaVariazioneModel modelSpecifica = isGestioneUEB() ? getSpecificaUEB() : getSpecificaImporti();
		
		AggiornaAnagraficaVariazioneBilancio request = creaRequest(AggiornaAnagraficaVariazioneBilancio.class);
		
		VariazioneImportoCapitolo variazione = creaUtilityAnagraficaVariazioneImportoCapitolo();
		variazione.setNote(modelSpecifica.getNote());
		variazione.setUid(uidVariazione);
		variazione.setNumero(getNumeroVariazione());
		
		request.setEvolviProcesso(Boolean.FALSE);
		request.setVariazioneImportoCapitolo(variazione);
		return request;
	}

	/**
	 * Crea la request per l'inserimento del dettaglio del capitolo nella variazione
	 * @return the InserisciDettaglioVariazioneImportoCapitolo request
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitolo() {
		
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaImporti().getElementoCapitoloVariazione();
		return popolaRequestInserisciDettaglioVariazioneImportoCapitolo(elementoCapitoloVariazione);
	}
	
	/**
	 * Crea la request per l'inserimento del dettaglio del capitolo nella variazione
	 * @return the InserisciDettaglioVariazioneImportoCapitolo request
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitoloUEB() {
		
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaUEB().getElementoCapitoloVariazione();
		
		return popolaRequestInserisciDettaglioVariazioneImportoCapitolo(elementoCapitoloVariazione);
	}

	private InserisciDettaglioVariazioneImportoCapitolo popolaRequestInserisciDettaglioVariazioneImportoCapitolo(
			ElementoCapitoloVariazione elementoCapitoloVariazione) {
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(elementoCapitoloVariazione.getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		
		//SIAC-6705
		if(elementoCapitoloVariazione.getStatoOperativoElementoDiBilancio() != null && StatoOperativoElementoDiBilancio.PROVVISORIO.equals(elementoCapitoloVariazione.getStatoOperativoElementoDiBilancio())) {
			dettaglioVariazioneImportoCapitolo.setFlagNuovoCapitolo(Boolean.TRUE);
		}
		
		
		dettaglioVariazioneImportoCapitolo.setStanziamento(elementoCapitoloVariazione.getCompetenza());
		dettaglioVariazioneImportoCapitolo.setStanziamentoCassa(elementoCapitoloVariazione.getCassa());
		dettaglioVariazioneImportoCapitolo.setStanziamentoResiduo(elementoCapitoloVariazione.getResiduo());
			
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazione);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}

	

	/**
	 * Creo la request che aggiorna il dettaglio dell'importo del capitolo collegato alla variazione
	 * @return la request creata
	 */
	public AggiornaDettaglioVariazioneImportoCapitolo creaRequestAggiornaDettaglioVariazioneImportoCapitolo() {
		ElementoCapitoloVariazione parametri = getSpecificaImporti().getElementoCapitoloVariazione();
		return popolaRequestAggiornamentoDettaglio(parametri);
	}
	
	/**
	 * Creo la request che aggiorna il dettaglio dell'importo del capitolo collegato alla variazione
	 * @return la request creata
	 */
	public AggiornaDettaglioVariazioneImportoCapitolo creaRequestAggiornaDettaglioVariazioneImportoCapitoloUEB() {
		ElementoCapitoloVariazione parametri = getSpecificaUEB().getElementoCapitoloVariazione();
		return popolaRequestAggiornamentoDettaglio(parametri);
	}

	private AggiornaDettaglioVariazioneImportoCapitolo popolaRequestAggiornamentoDettaglio(ElementoCapitoloVariazione parametri) {
		AggiornaDettaglioVariazioneImportoCapitolo request = creaRequest(AggiornaDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo datiCapitoloDaAggiornare = new DettaglioVariazioneImportoCapitolo();
		datiCapitoloDaAggiornare.setStanziamento(parametri.getCompetenza());
		datiCapitoloDaAggiornare.setStanziamentoCassa(parametri.getCassa());
		datiCapitoloDaAggiornare.setStanziamentoResiduo(parametri.getResiduo());
		datiCapitoloDaAggiornare.setFlagNuovoCapitolo(parametri.getDaInserire());
		datiCapitoloDaAggiornare.setFlagAnnullaCapitolo(parametri.getDaAnnullare());
		
		Capitolo<?,?> cap = new Capitolo<ImportiCapitolo, ImportiCapitolo>();//datiCapitoloDaAggiornare.getCapitolo();//.setUid(parametri.getUid();
		cap.setUid(parametri.getUid());
		datiCapitoloDaAggiornare.setCapitolo(cap);
	
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazione);
		datiCapitoloDaAggiornare.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
				
		request.setDettaglioVariazioneImportoCapitolo(datiCapitoloDaAggiornare);
		return request;
	}

	/**
	 * Creo la request di eliminazione del dettaglio variazione importo capitolo 
	 * @return la request creata 
	 */
	public EliminaDettaglioVariazioneImportoCapitolo creaRequestEliminaDettaglioVariazioneImportoCapitolo() {
		ElementoCapitoloVariazione elementoVariazioneImportiCapitolo = getSpecificaImporti().getElementoCapitoloVariazione();
		return popolaRequestEliminazioneDettaglio(elementoVariazioneImportiCapitolo);
	}

	private EliminaDettaglioVariazioneImportoCapitolo popolaRequestEliminazioneDettaglio(ElementoCapitoloVariazione elementoVariazioneImportiCapitolo) {
		EliminaDettaglioVariazioneImportoCapitolo request = creaRequest(EliminaDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dett = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> cap = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		cap.setUid(elementoVariazioneImportiCapitolo.getUid());
		dett.setCapitolo(cap);
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazione);
		dett.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
		request.setDettaglioVariazioneImportoCapitolo(dett);
		return request;
	}
	
	/**
	 * Creo la request di eliminazione del dettaglio variazione importo capitolo 
	 * @return la request creata 
	 */
	public EliminaDettaglioVariazioneImportoCapitolo creaRequestEliminaDettaglioVariazioneImportoCapitoloUEB() {
		
		ElementoCapitoloVariazione elementoVariazioneImportiCapitolo = getSpecificaUEB().getElementoCapitoloVariazione();
		return popolaRequestEliminazioneDettaglio(elementoVariazioneImportiCapitolo);
	}

	/**
	 * 
	 * Creo la request di annulamento di un capitolo
	 * @return la request creata
	 */
	public AggiornaDettaglioVariazioneImportoCapitolo creaRequestAggiornaDettaglioVariazioneImportoCapitoloPerAnnullamento() {
		AggiornaDettaglioVariazioneImportoCapitolo request = creaRequest(AggiornaDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(getSpecificaImporti().getElementoCapitoloVariazione().getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		
		dettaglioVariazioneImportoCapitolo.setFlagAnnullaCapitolo(Boolean.TRUE);
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazione);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}
	
	/**
	 * @return the RicercaDettagliVariazioneImportoCapitoloNellaVariazione
	 */
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazione creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione() {
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = creaRequest(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class);
		request.setUidVariazione(uidVariazione);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	

	/**
	 * Creo la request per l'annullamento del capitolo tramite la variazione
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitoloPerAnnullamento() {
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaImporti().getElementoCapitoloVariazione();		
		return creaRequestInserimentoDettaglioPerAnnullamento(elementoCapitoloVariazione);
	}
	
	/**
	 * Crea la request per l'annullamento di una singola UEB (chiamato in sequenza dalla Action per ogni ueb del capitolo)
	 * @param elementoCapitoloVariazioneDaAnnullare l'elemento da annullare
	 * @return theInserisciDettaglioVariazioneImportoCapitolo 
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitoloUEBPerAnnullamento(ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare) {
		return creaRequestInserimentoDettaglioPerAnnullamento(elementoCapitoloVariazioneDaAnnullare);
	}
	/***
	 * metodo di utility per creare una request di annullamento del capitolo a partire da un ElementoCapitoloVariazione
	 * **/
	private InserisciDettaglioVariazioneImportoCapitolo creaRequestInserimentoDettaglioPerAnnullamento(ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare) {
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(elementoCapitoloVariazioneDaAnnullare.getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
				
		dettaglioVariazioneImportoCapitolo.setStanziamento(elementoCapitoloVariazioneDaAnnullare.getCompetenza());
		dettaglioVariazioneImportoCapitolo.setStanziamentoCassa(elementoCapitoloVariazioneDaAnnullare.getCassa());
		dettaglioVariazioneImportoCapitolo.setStanziamentoResiduo(elementoCapitoloVariazioneDaAnnullare.getResiduo());
		dettaglioVariazioneImportoCapitolo.setFlagAnnullaCapitolo(Boolean.TRUE);	
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazione);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}
	
	/**
	 * Crea la request per la ricerca del provvedimento.
	 *
	 * @param attoAmministrativoDaCercare the atto amministrativo nella variazione
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento(AttoAmministrativo attoAmministrativoDaCercare) {
		RicercaProvvedimento request =creaRequest(RicercaProvvedimento.class);
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		if(attoAmministrativoDaCercare.getUid() != 0) {
			ricercaAtti.setUid(Integer.valueOf(attoAmministrativoDaCercare.getUid()));
		} else {
			ricercaAtti.setAnnoAtto(attoAmministrativoDaCercare.getAnno());
			ricercaAtti.setNumeroAtto(attoAmministrativoDaCercare.getNumero());
			ricercaAtti.setTipoAtto(attoAmministrativoDaCercare.getTipoAtto());
		}
		
		request.setRicercaAtti(ricercaAtti);
		
		return request;
	}
	/**
	 * Crea request ricerca singolo dettaglio variazione importo capitolo nella variazione capitolo uscita previsione.
	 *
	 * @return the ricerca singolo dettaglio variazione importo capitolo nella variazione
	 */
	//SIAC-5016
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione();
		req.setCapitolo(getSpecificaImporti().getCapitoloUscitaPrevisioneNellaVariazione());
		return req;
	}
	/**
	 * Crea request ricerca singolo dettaglio variazione importo capitolo nella variazione capitolo uscita previsione.
	 *
	 * @return the ricerca singolo dettaglio variazione importo capitolo nella variazione
	 */
	//SIAC-5016
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione();
		req.setCapitolo(getSpecificaImporti().getCapitoloEntrataPrevisioneNellaVariazione());
		return req;
	}
	/**
	 * Crea request ricerca singolo dettaglio variazione importo capitolo nella variazione capitolo uscita previsione.
	 *
	 * @return the ricerca singolo dettaglio variazione importo capitolo nella variazione
	 */
	//SIAC-5016
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione();
		req.setCapitolo(getSpecificaImporti().getCapitoloEntrataGestioneNellaVariazione());
		return req;
	}
	/**
	 * Crea request ricerca singolo dettaglio variazione importo capitolo nella variazione capitolo uscita previsione.
	 *
	 * @return the ricerca singolo dettaglio variazione importo capitolo nella variazione
	 */
	//SIAC-5016
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione();
		req.setCapitolo(getSpecificaImporti().getCapitoloUscitaGestioneNellaVariazione());
		return req;
	}

	/**
	 * Crea request ricerca singolo dettaglio variazione importo capitolo nella variazione.
	 *
	 * @return the ricerca singolo dettaglio variazione importo capitolo nella variazione
	 */
	private RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = creaRequest(RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione.class);
		req.setBilancio(getBilancio());
		req.setUidVariazione(uidVariazione);
		return req;
	}
	
	/**
	 * Crea una request per il servizio {@link VariazioneBilancioExcelReport}.
	 * @return la request creata
	 */
	public VariazioneBilancioExcelReport creaRequestStampaExcelVariazioneDiBilancio() {
		VariazioneBilancioExcelReport req = creaRequest(VariazioneBilancioExcelReport.class);
		
		req.setEnte(getEnte());
		req.setXlsx(getIsXlsx());
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(getUidVariazione());
		req.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
		return req;
	}
}
