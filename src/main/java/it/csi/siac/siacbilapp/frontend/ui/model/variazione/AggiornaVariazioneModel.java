/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoUEBModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step4.RiepilogoVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.helper.ImportiCapitoloComponenteVariazioneModificabile;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.util.Costanti;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacfinser.CostantiFin;

/**
 * Classe di model per l'aggiornamento della variazione. Contiene una mappatura dei tre possibili form considerati.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 19/11/2013
 *
 */
public class AggiornaVariazioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7948123269684123083L;
	
	private BigDecimal totaleStanziamentiEntrata;
	private BigDecimal totaleStanziamentiCassaEntrata;
	private BigDecimal totaleStanziamentiResiduiEntrata;
	private BigDecimal totaleStanziamentiSpesa;
	private BigDecimal totaleStanziamentiCassaSpesa;
	private BigDecimal totaleStanziamentiResiduiSpesa;

	//SIAC-6883 variazioni multianno
	private BigDecimal totaleStanziamentiSpesa1;
	private BigDecimal totaleStanziamentiSpesa2;
	private BigDecimal totaleStanziamentiEntrata1;
	private BigDecimal totaleStanziamentiEntrata2;
	
	private SpecificaVariazioneImportoModel specificaImporti;
	private SpecificaVariazioneImportoUEBModel specificaUEB; 
	private SpecificaVariazioneCodifiche specificaCodifiche;
	
	private Integer numeroVariazione;
	private StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio;
	private TipoVariazione tipoVariazione;
	private ApplicazioneVariazione applicazione;
	private Integer annoCompetenza;
	private String descrizione;
	private String note;
	//per capitolo
	private List<CategoriaCapitolo> listaCategoriaCapitolo = new ArrayList<CategoriaCapitolo>();
	
	// Per il provvedimento PEG
	private AttoAmministrativo attoAmministrativo;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private TipoAtto tipoAtto;
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private AttoAmministrativo attoAmministrativoOld;
	private String stringaProvvedimento;
	
	// Per il provvedimento di variazione di bilancio
	private AttoAmministrativo attoAmministrativoAggiuntivo;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileAggiuntivo;
	private TipoAtto tipoAttoAggiuntivo;
	private AttoAmministrativo attoAmministrativoAggiuntivoOld;
	private String stringaProvvedimentoAggiuntivo;

	// Dati per Bonita
	@Deprecated 
	private Boolean invioAdOrganoLegislativo;
	@Deprecated
	private Boolean invioAdOrganoAmministrativo;
	private Date dataVariazione;
	private Integer uidVariazione;
	private Integer uidVariazioneImportoCapitolo;
	private String idAttivita;
	
	// Per la visualizzazione dei pulsanti
	private Boolean salvaAbilitato = Boolean.TRUE;
	private Boolean annullaAbilitato = Boolean.TRUE;
	private Boolean concludiAbilitato = Boolean.TRUE;
	private Boolean campiNonModificabili = Boolean.FALSE;
	
	//dati per la visualizzazione di conferma quadratura di cassa
	private Boolean richiediConfermaQuadratura = Boolean.FALSE;
	
	//SIAC-7629 inizio FL
	private Boolean richiediConfermaQuadraturaCP = Boolean.FALSE;
	//SIAC-7629 fine FL
	
	private String saltaCheckStanziamentoCassa = "";
	
	//SIAC-8332-REGP sanato errore introdotto da SIAC-7629
	private String saltaCheckStanziamento = "";
	
	// Per cachare le responses
	private Map<String, String> cache = new HashMap<String, String>();

	private Integer idOperazioneAsincrona;

	private Boolean isAsyncResponsePresent = Boolean.FALSE;

	//SIAC-4737
	private boolean richiediConfermaMancanzaProvvedimentoVariazioneBilancio;
	private boolean saltaCheckProvvedimentoVariazioneBilancio;
	
	//SIAC-5016
	private CapitoloEntrataPrevisione capitoloEntrataPrevisioneNellaVariazione;
	private CapitoloUscitaPrevisione capitoloUscitaPrevisioneNellaVariazione;
	private CapitoloEntrataGestione capitoloEntrataGestioneNellaVariazione;
	private CapitoloUscitaGestione capitoloUscitaGestioneNellaVariazione;
	private ElementoCapitoloVariazione elementoCapitoloVariazioneTrovatoNellaVariazione;
	
	private Boolean isXlsx;
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	
	//SIAC-6177
	private ElementoStatoOperativoVariazione elementoStatoOperativoVariazione;
	
	// SIAC-6527
	private Integer idAzioneReportVariazioni; 
	
	//SIAC-6881
	private List<TipoComponenteImportiCapitolo> listaTipoComponente = new ArrayList<TipoComponenteImportiCapitolo>();
	private boolean fromInserimento;
	private RiepilogoVariazioneModel riepilogo;
	
	//SIAC-6884 per AnnullaCapitolo da InserisciVariazione
	private boolean decentrato = false;
	private boolean regionePiemonte = false;
	
	

	private List<TipoComponenteImportiCapitolo> listaTipoComponenteDefault = new ArrayList<TipoComponenteImportiCapitolo>();

	//SIAC-6884
	private Date dataApertura;
	private StrutturaAmministrativoContabile direzioneProponente;
	//SIAC-8332-REGP eliminato campo variazioneDecentrataAperta
//	private boolean variazioneDecentrataAperta;
	private Date dataChiusuraProposta;	
	private Boolean flagGiunta;
	private Boolean flagConsiglio;

	public Date getDataApertura() {
		return dataApertura;
	}
		
	public StrutturaAmministrativoContabile getDirezioneProponente() {
		return direzioneProponente;
	}

	public void setDirezioneProponente(StrutturaAmministrativoContabile direzioneProponente) {
		this.direzioneProponente = direzioneProponente;
	}

	public void setDataApertura(Date dataApertura) {
		this.dataApertura = dataApertura;
	}
			
	public String getDataAperturaFormatted(){
		String result = "";
		if(dataApertura != null){
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy"); 
			result = sd.format(dataApertura);
		}
		return result;
	}
	
	public String getDataChiusuraFormatted(){
		String result = "";
		if(dataChiusuraProposta != null){
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy"); 
			result = sd.format(dataChiusuraProposta);
		}
		return result;
	}
	
	/** Costruttore vuoto di default */
	public AggiornaVariazioneModel() {
		specificaImporti = new SpecificaVariazioneImportoModel();
		specificaUEB = new SpecificaVariazioneImportoUEBModel();
		specificaCodifiche = new SpecificaVariazioneCodifiche();
		riepilogo = new RiepilogoVariazioneModel();

		setTitolo("Aggiorna variazioni");
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
	public void setSpecificaCodifiche(
			SpecificaVariazioneCodifiche specificaCodifiche) {
		this.specificaCodifiche = specificaCodifiche;
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
	 * @return the statoOperativoAggiuntivo
	 */
	public StatoOperativoVariazioneBilancio getStatoOperativoVariazioneDiBilancio() {
		return statoOperativoVariazioneBilancio;
	}

	/**
	 * @param statoOperativoAggiuntivo the statoOperativoAggiuntivo to set
	 */
	public void setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneBilancio statoOperativoAggiuntivo) {
		this.statoOperativoVariazioneBilancio = statoOperativoAggiuntivo;
	}

	/**
	 * @return the tipoVariazione
	 */
	public TipoVariazione getTipoVariazione() {
		return tipoVariazione;
	}

	/**
	 * @param tipoVariazione the tipoVariazione to set
	 */
	public void setTipoVariazione(TipoVariazione tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}

	/**
	 * @return the applicazione
	 */
	public ApplicazioneVariazione getApplicazione() {
		return applicazione;
	}

	/**
	 * @param applicazione the applicazione to set
	 */
	public void setApplicazione(ApplicazioneVariazione applicazione) {
		this.applicazione = applicazione;
	}

	/**
	 * @return the annoCompetenza
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @param annoCompetenza the annoCompetenza to set
	 */
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
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
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(
			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
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
	 * @return the attoAmministrativoOld
	 */
	public AttoAmministrativo getAttoAmministrativoOld() {
		return attoAmministrativoOld;
	}

	/**
	 * @param attoAmministrativoOld the attoAmministrativoOld to set
	 */
	public void setAttoAmministrativoOld(AttoAmministrativo attoAmministrativoOld) {
		this.attoAmministrativoOld = attoAmministrativoOld;
	}
	/**
	 * @return the stringaProvvedimento
	 */
	public String getStringaProvvedimento() {
		return stringaProvvedimento;
	}

	/**
	 * @param stringaProvvedimento the stringaProvvedimento to set
	 */
	public void setStringaProvvedimento(String stringaProvvedimento) {
		this.stringaProvvedimento = stringaProvvedimento;
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
	 * @return the listaCategoriaCapitolo
	 */
	public List<CategoriaCapitolo> getListaCategoriaCapitolo() {
		return listaCategoriaCapitolo;
	}

	/**
	 * @param listaCategoriaCapitolo the listaCategoriaCapitolo to set
	 */
	public void setListaCategoriaCapitolo(List<CategoriaCapitolo> listaCategoriaCapitolo) {
		this.listaCategoriaCapitolo = listaCategoriaCapitolo;
	}
	/**
	 * @return the dataVariazione
	 */
	public Date getDataVariazione() {
		if(dataVariazione == null) {
			return null;
		}
		return new Date(dataVariazione.getTime());
	}

	/**
	 * @param dataVariazione the dataVariazione to set
	 */
	public void setDataVariazione(Date dataVariazione) {
		if(dataVariazione != null) {
			this.dataVariazione = new Date(dataVariazione.getTime());
		} else {
			this.dataVariazione = null;
		}
	}

	/**
	 * @return the uidVariazione
	 */
	public Integer getUidVariazione() {
		return uidVariazione;
	}

	/**
	 * @param uidVariazione the uidVariazione to set
	 */
	public void setUidVariazione(Integer uidVariazione) {
		this.uidVariazione = uidVariazione;
	}
	
	/**
	 * @return the uidVariazioneImportoCapitolo
	 */
	public Integer getUidVariazioneImportoCapitolo() {
		return uidVariazioneImportoCapitolo;
	}

	/**
	 * @param uidVariazioneImportoCapitolo the uidVariazioneImportoCapitolo to set
	 */
	public void setUidVariazioneImportoCapitolo(Integer uidVariazioneImportoCapitolo) {
		this.uidVariazioneImportoCapitolo = uidVariazioneImportoCapitolo;
	}

	/**
	 * @return the idAttivita
	 */
	public String getIdAttivita() {
		return idAttivita;
	}

	/**
	 * @param idAttivita the idAttivita to set
	 */
	public void setIdAttivita(String idAttivita) {
		this.idAttivita = idAttivita;
	}
	
	/**
	 * @return the salvaAbilitato
	 */
	public Boolean getSalvaAbilitato() {
		return salvaAbilitato;
	}

	/**
	 * @param salvaAbilitato the salvaAbilitato to set
	 */
	public void setSalvaAbilitato(Boolean salvaAbilitato) {
		this.salvaAbilitato = salvaAbilitato;
	}

	/**
	 * @return the annullaAbilitato
	 */
	public Boolean getAnnullaAbilitato() {
		return annullaAbilitato;
	}

	/**
	 * @param annullaAbilitato the annullaAbilitato to set
	 */
	public void setAnnullaAbilitato(Boolean annullaAbilitato) {
		this.annullaAbilitato = annullaAbilitato;
	}

	/**
	 * @return the concludiAbilitato
	 */
	public Boolean getConcludiAbilitato() {
		return concludiAbilitato;
	}

	/**
	 * @param concludiAbilitato the concludiAbilitato to set
	 */
	public void setConcludiAbilitato(Boolean concludiAbilitato) {
		this.concludiAbilitato = concludiAbilitato;
	}
	
	/**
	 * @return the campiNonModificabili
	 */
	public Boolean getCampiNonModificabili() {
		return campiNonModificabili;
	}

	/**
	 * @param campiNonModificabili the campiNonModificabili to set
	 */
	public void setCampiNonModificabili(Boolean campiNonModificabili) {
		this.campiNonModificabili = campiNonModificabili;
	}
	/**
	 * @return the cache
	 */
	public Map<String, String> getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Map<String, String> cache) {
		this.cache = cache;
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
	 * @return the totaleStanziamentiSpesa1
	 */
	public BigDecimal getTotaleStanziamentiSpesa1() {
		return totaleStanziamentiSpesa1;
	}

	/**
	 * @param totaleStanziamentiSpesa1 the totaleStanziamentiSpesa1 to set
	 */
	public void setTotaleStanziamentiSpesa1(BigDecimal totaleStanziamentiSpesa1) {
		this.totaleStanziamentiSpesa1 = totaleStanziamentiSpesa1;
	}

	/**
	 * @return the totaleStanziamentiSpesa2
	 */
	public BigDecimal getTotaleStanziamentiSpesa2() {
		return totaleStanziamentiSpesa2;
	}

	/**
	 * @param totaleStanziamentiSpesa2 the totaleStanziamentiSpesa2 to set
	 */
	public void setTotaleStanziamentiSpesa2(BigDecimal totaleStanziamentiSpesa2) {
		this.totaleStanziamentiSpesa2 = totaleStanziamentiSpesa2;
	}

	/**
	 * @return the totaleStanziamentiEntrata1
	 */
	public BigDecimal getTotaleStanziamentiEntrata1() {
		return totaleStanziamentiEntrata1;
	}

	/**
	 * @param totaleStanziamentiEntrata1 the totaleStanziamentiEntrata1 to set
	 */
	public void setTotaleStanziamentiEntrata1(BigDecimal totaleStanziamentiEntrata1) {
		this.totaleStanziamentiEntrata1 = totaleStanziamentiEntrata1;
	}

	/**
	 * @return the totaleStanziamentiEntrata2
	 */
	public BigDecimal getTotaleStanziamentiEntrata2() {
		return totaleStanziamentiEntrata2;
	}

	/**
	 * @param totaleStanziamentiEntrata2 the totaleStanziamentiEntrata2 to set
	 */
	public void setTotaleStanziamentiEntrata2(BigDecimal totaleStanziamentiEntrata2) {
		this.totaleStanziamentiEntrata2 = totaleStanziamentiEntrata2;
	}

	/**
	 * @return the richiediConfermaQuadratura
	 */
	public Boolean getRichiediConfermaQuadratura() {
		return richiediConfermaQuadratura;
	}

	/**
	 * @param richiediConfermaQuadratura the richiediConfermaQuadratura to set
	 */
	public void setRichiediConfermaQuadratura(Boolean richiediConfermaQuadratura) {
		this.richiediConfermaQuadratura = richiediConfermaQuadratura;
	}

	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincrona to set
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}

	/**
	 * @return the idOperazioneAsincrona
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
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
		this.isAsyncResponsePresent = isAsyncResponsePresent != null ? isAsyncResponsePresent : Boolean.FALSE;
	}

	/**
	 * @return the saltaCheckStanziamentoCassa
	 */
	public String getSaltaCheckStanziamentoCassa() {
		return saltaCheckStanziamentoCassa;
	}

	/**
	 * @param saltaCheckStanziamentoCassa the saltaCheckStanziamentoCassa to set
	 */
	public void setSaltaCheckStanziamentoCassa(String saltaCheckStanziamentoCassa) {
		this.saltaCheckStanziamentoCassa = saltaCheckStanziamentoCassa;
	}
	
	public String getSaltaCheckStanziamento() {
		return saltaCheckStanziamento;
	}

	public void setSaltaCheckStanziamento(String saltaCheckStanziamento) {
		this.saltaCheckStanziamento = saltaCheckStanziamento;
	}

	/**
	 * Gets the atto amministrativo aggiuntivo.
	 *
	 * @return the atto amministrativo aggiuntivo
	 */
	public AttoAmministrativo getAttoAmministrativoAggiuntivo() {
		return attoAmministrativoAggiuntivo;
	}

	/**
	 * Gets the struttura amministrativo contabile aggiuntivo.
	 *
	 * @return the struttura amministrativo contabile aggiuntivo
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileAggiuntivo() {
		return strutturaAmministrativoContabileAggiuntivo;
	}

	/**
	 * Gets the tipo atto aggiuntivo.
	 *
	 * @return the tipo atto aggiuntivo
	 */
	public TipoAtto getTipoAttoAggiuntivo() {
		return tipoAttoAggiuntivo;
	}

	/**
	 * Gets the atto amministrativo O aggiuntivo old.
	 *
	 * @return the atto amministrativo O aggiuntivo old
	 */
	public AttoAmministrativo getAttoAmministrativoOAggiuntivoOld() {
		return attoAmministrativoAggiuntivoOld;
	}

	/**
	 * Gets the stringa provvedimento opzionale.
	 *
	 * @return the stringa provvedimento opzionale
	 */
	public String getStringaProvvedimentoAggiuntivo() {
		return stringaProvvedimentoAggiuntivo;
	}

	/**
	 * Sets the atto amministrativo opzionale.
	 *
	 * @param attoAmministrativoAggiuntivo the new atto amministrativo opzionale
	 */
	public void setAttoAmministrativoAggiuntivo(AttoAmministrativo attoAmministrativoAggiuntivo) {
		this.attoAmministrativoAggiuntivo = attoAmministrativoAggiuntivo;
	}

	/**
	 * Sets the struttura amministrativo contabile aggiuntivo.
	 *
	 * @param strutturaAmministrativoContabileAggiuntivo the new struttura amministrativo contabile aggiuntivo
	 */
	public void setStrutturaAmministrativoContabileAggiuntivo(StrutturaAmministrativoContabile strutturaAmministrativoContabileAggiuntivo) {
		this.strutturaAmministrativoContabileAggiuntivo = strutturaAmministrativoContabileAggiuntivo;
	}

	/**
	 * Sets the tipo atto aggiuntivo.
	 *
	 * @param tipoAttoAggiuntivo the new tipo atto aggiuntivo
	 */
	public void setTipoAttoAggiuntivo(TipoAtto tipoAttoAggiuntivo) {
		this.tipoAttoAggiuntivo = tipoAttoAggiuntivo;
	}

	/**
	 * Sets the atto amministrativo O aggiuntivo old.
	 *
	 * @param attoAmministrativoAggiuntivoOld the new atto amministrativo O aggiuntivo old
	 */
	public void setAttoAmministrativoOAggiuntivoOld(AttoAmministrativo attoAmministrativoAggiuntivoOld) {
		this.attoAmministrativoAggiuntivoOld = attoAmministrativoAggiuntivoOld;
	}

	/**
	 * Sets the stringa provvedimento aggiuntivo.
	 *
	 * @param stringaProvvedimentoAggiuntivo the new stringa provvedimento aggiuntivo
	 */
	public void setStringaProvvedimentoAggiuntivo(String stringaProvvedimentoAggiuntivo) {
		this.stringaProvvedimentoAggiuntivo = stringaProvvedimentoAggiuntivo;
	}
	
	/**
	 * Gets the richiedi conferma mancanza provvedimento variazione bilancio.
	 *
	 * @return the richiedi conferma mancanza provvedimento variazione bilancio
	 */
	public boolean getRichiediConfermaMancanzaProvvedimentoVariazioneBilancio() {
		return richiediConfermaMancanzaProvvedimentoVariazioneBilancio;
	}
	
	/**
	 * Sets the richiedi conferma mancanza provvedimento variazione bilancio.
	 *
	 * @param richiediConfermaMancanzaProvvedimentoVariazioneBilancio the new richiedi conferma mancanza provvedimento variazione bilancio
	 */
	public void setRichiediConfermaMancanzaProvvedimentoVariazioneBilancio(boolean richiediConfermaMancanzaProvvedimentoVariazioneBilancio) {
		this.richiediConfermaMancanzaProvvedimentoVariazioneBilancio = richiediConfermaMancanzaProvvedimentoVariazioneBilancio;
	}

	/**
	 * Checks if is salta check provvedimento variazione bilancio.
	 *
	 * @return true, if is salta check provvedimento variazione bilancio
	 */
	public boolean isSaltaCheckProvvedimentoVariazioneBilancio() {
		return saltaCheckProvvedimentoVariazioneBilancio;
	}

	/**
	 * Sets the salta check provvedimento variazione bilancio.
	 *
	 * @param saltaCheckProvvedimentoVariazioneBilancio the new salta check provvedimento variazione bilancio
	 */
	public void setSaltaCheckProvvedimentoVariazioneBilancio(boolean saltaCheckProvvedimentoVariazioneBilancio) {
		this.saltaCheckProvvedimentoVariazioneBilancio = saltaCheckProvvedimentoVariazioneBilancio;
	}

	/**
	 * @return the attoAmministrativoAggiuntivoOld
	 */
	public AttoAmministrativo getAttoAmministrativoAggiuntivoOld() {
		return attoAmministrativoAggiuntivoOld;
	}

	/**
	 * @param attoAmministrativoAggiuntivoOld the attoAmministrativoAggiuntivoOld to set
	 */
	public void setAttoAmministrativoAggiuntivoOld(AttoAmministrativo attoAmministrativoAggiuntivoOld) {
		this.attoAmministrativoAggiuntivoOld = attoAmministrativoAggiuntivoOld;
	}

	/**
	 * @return the capitoloEntrataPrevisioneNellaVariazione
	 */
	public CapitoloEntrataPrevisione getCapitoloEntrataPrevisioneNellaVariazione() {
		return capitoloEntrataPrevisioneNellaVariazione;
	}

	/**
	 * @param capitoloEntrataPrevisioneNellaVariazione the capitoloEntrataPrevisioneNellaVariazione to set
	 */
	public void setCapitoloEntrataPrevisioneNellaVariazione(
			CapitoloEntrataPrevisione capitoloEntrataPrevisioneNellaVariazione) {
		this.capitoloEntrataPrevisioneNellaVariazione = capitoloEntrataPrevisioneNellaVariazione;
	}

	/**
	 * @return the capitoloUscitaPrevisioneNellaVariazione
	 */
	public CapitoloUscitaPrevisione getCapitoloUscitaPrevisioneNellaVariazione() {
		return capitoloUscitaPrevisioneNellaVariazione;
	}

	/**
	 * @param capitoloUscitaPrevisioneNellaVariazione the capitoloUscitaPrevisioneNellaVariazione to set
	 */
	public void setCapitoloUscitaPrevisioneNellaVariazione(
			CapitoloUscitaPrevisione capitoloUscitaPrevisioneNellaVariazione) {
		this.capitoloUscitaPrevisioneNellaVariazione = capitoloUscitaPrevisioneNellaVariazione;
	}

	/**
	 * @return the capitoloEntrataGestioneNellaVariazione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestioneNellaVariazione() {
		return capitoloEntrataGestioneNellaVariazione;
	}

	/**
	 * @param capitoloEntrataGestioneNellaVariazione the capitoloEntrataGestioneNellaVariazione to set
	 */
	public void setCapitoloEntrataGestioneNellaVariazione(CapitoloEntrataGestione capitoloEntrataGestioneNellaVariazione) {
		this.capitoloEntrataGestioneNellaVariazione = capitoloEntrataGestioneNellaVariazione;
	}

	/**
	 * @return the capitoloUscitaGestioneNellaVariazione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestioneNellaVariazione() {
		return capitoloUscitaGestioneNellaVariazione;
	}

	/**
	 * @return the dettaglioVariazioneImportoCapitoloTrovatoNellaVariazione
	 */
	public ElementoCapitoloVariazione getElementoCapitoloVariazioneTrovatoNellaVariazione() {
		return elementoCapitoloVariazioneTrovatoNellaVariazione;
	}

	/**
	 * @param elementoCapitoloVariazioneTrovatoNellaVariazione the dettaglioVariazioneImportoCapitoloTrovatoNellaVariazione to set
	 */
	public void setElementoCapitoloVariazioneTrovatoNellaVariazione(ElementoCapitoloVariazione elementoCapitoloVariazioneTrovatoNellaVariazione) {
		this.elementoCapitoloVariazioneTrovatoNellaVariazione = elementoCapitoloVariazioneTrovatoNellaVariazione;
	}

	/**
	 * @param capitoloUscitaGestioneNellaVariazione the capitoloUscitaGestioneNellaVariazione to set
	 */
	public void setCapitoloUscitaGestioneNellaVariazione(CapitoloUscitaGestione capitoloUscitaGestioneNellaVariazione) {
		this.capitoloUscitaGestioneNellaVariazione = capitoloUscitaGestioneNellaVariazione;
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
	 * @return the elementoStatoOperativoVariazione
	 */
	public ElementoStatoOperativoVariazione getElementoStatoOperativoVariazione() {
		return elementoStatoOperativoVariazione;
	}

	/**
	 * @param elementoStatoOperativoVariazione the elementoStatoOperativoVariazione to set
	 */
	public void setElementoStatoOperativoVariazione(ElementoStatoOperativoVariazione elementoStatoOperativoVariazione) {
		this.elementoStatoOperativoVariazione = elementoStatoOperativoVariazione;
	}

	/**
	 * @return the idAzioneReportVariazioni
	 */
	public Integer getIdAzioneReportVariazioni() {
		return this.idAzioneReportVariazioni;
	}

	/**
	 * @param idAzioneReportVariazioni the idAzioneReportVariazioni to set
	 */
	public void setIdAzioneReportVariazioni(Integer idAzioneReportVariazioni) {
		this.idAzioneReportVariazioni = idAzioneReportVariazioni;
	}
	
	
	/**
	 * @return the listaTipoComponente
	 */
	public List<TipoComponenteImportiCapitolo> getListaTipoComponente() {
		return listaTipoComponente;
	}

	/**
	 * @param listaTipoComponente the listaTipoComponente to set
	 */
	public void setListaTipoComponente(List<TipoComponenteImportiCapitolo> listaTipoComponente) {
		this.listaTipoComponente = listaTipoComponente != null? listaTipoComponente : new ArrayList<TipoComponenteImportiCapitolo>();
	}
	
	/*SIAC-6884 
	 * Annulla capitolo da inserisci Variazione decentrata*/
	public boolean isDecentrato() {
		return decentrato;
	}

	public void setDecentrato(boolean decentrato) {
		this.decentrato = decentrato;
	}
	
	public boolean isChiudiProposta() {
		return isDecentrato() && StatoOperativoVariazioneBilancio.PRE_BOZZA.equals(getStatoOperativoVariazioneDiBilancio());
	}
	
	public boolean isConcludiAttivita() {
		return !isDecentrato();
	}

	
//	/**
//	 * @return the statiOperativiElementoDiBilancio
//	 */
//	public List<StatoOperativoElementoDiBilancio> getStatiOperativiElementoDiBilancio() {
//		return statiOperativiElementoDiBilancio;
//	}
//
//	/**
//	 * @param statiOperativiElementoDiBilancio the statiOperativiElementoDiBilancio to set
//	 */
//	public void setStatiOperativiElementoDiBilancio(List<StatoOperativoElementoDiBilancio> statiOperativiElementoDiBilancio) {
//		this.statiOperativiElementoDiBilancio = statiOperativiElementoDiBilancio;
//	}

	/**
	 * @return the fromInserimento
	 */
	public boolean getFromInserimento() {
		return fromInserimento;
	}

	/**
	 * @param fromInserimento the fromInserimento to set
	 */
	public void setFromInserimento(boolean fromInserimento) {
		this.fromInserimento = fromInserimento;
	}
	
	/**
	 * @return the listaTipoComponenteDefault
	 */
	public List<TipoComponenteImportiCapitolo> getListaTipoComponenteDefault() {
		return listaTipoComponenteDefault;
	}

	/**
	 * @param listaTipoComponenteDefault the listaTipoComponenteDefault to set
	 */
	public void setListaTipoComponenteDefault(List<TipoComponenteImportiCapitolo> listaTipoComponenteDefault) {
		this.listaTipoComponenteDefault = listaTipoComponenteDefault;
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
	
	//SIAC-6884
	public boolean isRegionePiemonte() {
		return regionePiemonte;
	}

	public void setRegionePiemonte(boolean regionePiemonte) {
		this.regionePiemonte = regionePiemonte;
	}

	/**
	 * Injetta un oggetto in cache.
	 * 
	 * @param objectToCache l'oggetto da cachare
	 */
	public void putInCache(Object objectToCache) {
		cache.put(objectToCache.getClass().getSimpleName(), JAXBUtility.marshall(objectToCache));
	}
	
	/**
	 * Ottiene un oggetto dalla cache.
	 * @param <T> la tipizzazione dell'elemento in cache
	 * @param clazz la classe dell'oggetto da ottenere
	 * 
	 * @return l'oggetto in cache, se presente; <code>null</code> in caso contrario
	 */
	public <T> T getFromCache(Class<T> clazz) {
		return JAXBUtility.unmarshall(cache.get(clazz.getSimpleName()), clazz);
	}
	
	/**
	 * Gets the tipo dettaglio componente importi capitolo default.
	 *
	 * @return the tipo dettaglio componente importi capitolo default
	 */
	public TipoDettaglioComponenteImportiCapitolo getTipoDettaglioComponenteImportiCapitoloDefault() {
		return TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO;
	}
	
	/**
	 * @param p
	 * @return
	 */
	public boolean isPropostaDefaultCompatibile(PropostaDefaultComponenteImportiCapitolo p) {
		return p != null && (PropostaDefaultComponenteImportiCapitolo.SI.equals(p) || 
				(ApplicazioneVariazione.PREVISIONE.equals(getApplicazione()) && PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE.equals(p)) ||
				(ApplicazioneVariazione.GESTIONE.equals(getApplicazione()) && PropostaDefaultComponenteImportiCapitolo.SOLO_GESTIONE.equals(p)));
	}
	
	
	/* **** Requests **** */

	/**
	 * Crea una request per il salvataggio dell'anagrafica di una variazione di bilancio
	 * @return la request popolata
	 */
	public AggiornaAnagraficaVariazioneBilancio creaRequestAggiornaAnagraficaVariazioneBilancioPerSalvataggio() {

		return popolaRequestAnagrafica(Boolean.FALSE, Boolean.FALSE);
	}
	/**
	 * Crea una request per l'evoluzione del processo di una variazione di bilancio
	 * @return la request popolata
	 */
	public AggiornaAnagraficaVariazioneBilancio creaRequestAggiornaAnagraficaVariazioneBilancioPerConclusione() {
		// TODO Auto-generated method stub
		return popolaRequestAnagrafica(Boolean.TRUE, Boolean.FALSE);
	}
	
	/**
	 * Metodo generico per popolare una request per il servizio AggiornaAnagraficaVariazioneDiBilancioService
	 * 
	 * @param daEvolvere <code>true</code> se il servizio deve evolvere il processo, <code>false</code> altrimenti
	 * @param daAnnullare <code>true</code> se la variazione deve essere annullata, <code>false</code> altrimenti
	 * 
	 * @return la request popolata
	 */
	private AggiornaAnagraficaVariazioneBilancio popolaRequestAnagrafica(Boolean daEvolvere, Boolean daAnnullare) {
		AggiornaAnagraficaVariazioneBilancio request = creaRequest(AggiornaAnagraficaVariazioneBilancio.class);
		
		// Controllo di sicurezza
		Boolean injezioneDaEvolvere = (daAnnullare.booleanValue() && !daEvolvere.booleanValue()) ? Boolean.TRUE : daEvolvere;
		
		request.setAnnullaVariazione(daAnnullare);
		request.setDataOra(new Date());
		
		request.setEvolviProcesso(injezioneDaEvolvere);
		request.setIdAttivita(idAttivita);
		request.setInvioOrganoAmministrativo(invioAdOrganoAmministrativo);
		request.setInvioOrganoLegislativo(invioAdOrganoLegislativo);
		request.setRichiedente(getRichiedente());
		
		request.setSaltaCheckStanziamentoCassa(Boolean.parseBoolean(saltaCheckStanziamentoCassa));
		request.setSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio(isSaltaCheckProvvedimentoVariazioneBilancio());
		
		request.setVariazioneImportoCapitolo(creaUtilityAnagraficaVariazioneImportoCapitolo());
		request.setRegionePiemonte(this.regionePiemonte);
		
		// task-225
		request.setStatoCorrente(this.statoOperativoVariazioneBilancio.getVariableName());
		
		return request;
	}

	/**
	 * Crea una request per l'aggiornamento della variazione di codifiche a partire dal model, per il salvataggio della stessa.
	 * 
	 * @return la request creata
	 * @throws IllegalArgumentException nel caso di errori nella creazione della request
	 */
	public AggiornaVariazioneCodifiche creaRequestAggiornaVariazioneCodifichePerSalvataggio() {
		return popolaRequestCodifiche(Boolean.FALSE, Boolean.FALSE);
	}
	
	/**
	 * Crea una request per l'aggiornamento della variazione di codifiche a partire dal model, per il salvataggio della stessa.
	 * 
	 * @return la request creata
	 * @throws IllegalArgumentException nel caso di errori nella creazione della request
	 */
	public AggiornaVariazioneCodifiche creaRequestAggiornaVariazioneCodifichePerAnnullamento() {
		return popolaRequestCodifiche(Boolean.TRUE, Boolean.TRUE);
	}
	
	/**
	 * Crea una request per l'aggiornamento della variazione di codifiche a partire dal model, per il salvataggio della stessa.
	 * 
	 * @return la request creata
	 * @throws IllegalArgumentException nel caso di errori nella creazione della request
	 */
	public AggiornaVariazioneCodifiche creaRequestAggiornaVariazioneCodifichePerConclusione() {
		return popolaRequestCodifiche(Boolean.TRUE, Boolean.FALSE);
	}
	
	/**
	 * Crea una request per la ricerca di dettaglio della variazione di codifiche.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioVariazioneCodifiche creaRequestRicercaDettaglioVariazioneCodifiche() {
		RicercaDettaglioVariazioneCodifiche request = new RicercaDettaglioVariazioneCodifiche();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setUidVariazione(uidVariazione.intValue());
		
		return request;
	}
	
	/**
	 * Crea una request per la ricerca del provvedimento.
	 * 
	 * @param attoAmministrativoNellaVariazione l'atto amministrativo presente nella variazione
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento(AttoAmministrativo attoAmministrativoNellaVariazione) {
		RicercaProvvedimento request = new RicercaProvvedimento();
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		
		ricercaAtti.setAnnoAtto(attoAmministrativoNellaVariazione.getAnno());
		ricercaAtti.setNumeroAtto(attoAmministrativoNellaVariazione.getNumero());
		ricercaAtti.setTipoAtto(attoAmministrativoNellaVariazione.getTipoAtto());
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRicercaAtti(ricercaAtti);
		request.setRichiedente(getRichiedente());
		
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
	
	/* **** Metodi di utilita' **** */
	
	/**
	 * Metodo di utilit&agrave; per il popolamento della request di aggiornamento della variazione di codifiche.
	 * 
	 * @param daEvolvere  se la request sia da far evolvere
	 * @param daAnnullare se la request sia da annullare
	 * 
	 * @return la request popolata
	 */
	private AggiornaVariazioneCodifiche popolaRequestCodifiche(Boolean daEvolvere, Boolean daAnnullare) {
		AggiornaVariazioneCodifiche request = new AggiornaVariazioneCodifiche();
		
		// Controllo di sicurezza
		Boolean injezioneDaEvolvere = (daAnnullare.booleanValue() && !daEvolvere.booleanValue()) ? Boolean.TRUE : daEvolvere;
		
		request.setAnnullaVariazione(daAnnullare);
		request.setDataOra(new Date());
		request.setEvolviProcesso(injezioneDaEvolvere);
		request.setIdAttivita(idAttivita);
		//SIAC-8332-REGP
		//request.setInvioOrganoAmministrativo(invioAdOrganoAmministrativo);
		//request.setInvioOrganoLegislativo(invioAdOrganoLegislativo);
		request.setRichiedente(getRichiedente());
		
		request.setVariazioneCodificaCapitolo(creaUtilityVariazioneCodificaCapitolo());
		
		return request;
	}
	
	/**
	 * Crea un'utilit&agrave; per la variazione degli importi.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private VariazioneImportoCapitolo creaUtilityAnagraficaVariazioneImportoCapitolo() {
		VariazioneImportoCapitolo utility = new VariazioneImportoCapitolo();
		
//		SpecificaVariazioneModel specifica = isGestioneUEB() ? specificaUEB : specificaImporti;
		//TODO: valutare, potrebbe essere da togliere
		if(attoAmministrativoOld!=null && (attoAmministrativo == null || attoAmministrativo.getUid() == 0)) {
			attoAmministrativoOld.setDataFineValidita(new Date());
			utility.setAttoAmministrativo(attoAmministrativoOld);
		} else if(attoAmministrativo != null && attoAmministrativo.getUid() != 0) {
			utility.setAttoAmministrativo(attoAmministrativo);
		}
		
		if(attoAmministrativoAggiuntivoOld!=null && (attoAmministrativoAggiuntivo == null || attoAmministrativoAggiuntivo.getUid() == 0)) {
			attoAmministrativoOld.setDataFineValidita(new Date());
			utility.setAttoAmministrativo(attoAmministrativoAggiuntivoOld);
		} else if(attoAmministrativoAggiuntivo != null && attoAmministrativoAggiuntivo.getUid() != 0) {
			utility.setAttoAmministrativoVariazioneBilancio(attoAmministrativoAggiuntivo);
		}
		utility.setBilancio(getBilancio());
		utility.setData(dataVariazione);
		utility.setDescrizione(descrizione);
		utility.setApplicazioneVariazione(applicazione);
		utility.setEnte(getEnte());
		utility.setNote(note);
		utility.setNumero(numeroVariazione);
		utility.setStatoOperativoVariazioneDiBilancio(statoOperativoVariazioneBilancio);
		utility.setTipoVariazione(tipoVariazione);
		utility.setUid(uidVariazioneImportoCapitolo);
		//SIAC 6884
		utility.setFlagConsiglio(flagConsiglio);
		utility.setFlagGiunta(flagGiunta);
		
		return utility;
	}
	
	/**
	 * Crea un'utilit&agrave; per la variazione delle codifiche.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private VariazioneCodificaCapitolo creaUtilityVariazioneCodificaCapitolo() {
		VariazioneCodificaCapitolo utility = new VariazioneCodificaCapitolo();
		
		if(attoAmministrativoOld!=null && (attoAmministrativo == null || attoAmministrativo.getUid() == 0)) {
			attoAmministrativoOld.setDataFineValidita(new Date());
			utility.setAttoAmministrativo(attoAmministrativoOld);
		} else if(attoAmministrativo != null && attoAmministrativo.getUid() != 0) {
			utility.setAttoAmministrativo(attoAmministrativo);
		}
		
		utility.setBilancio(getBilancio());
		utility.setData(dataVariazione);
		utility.setDescrizione(descrizione);
		utility.setEnte(getEnte());
		utility.setNote(note);
		utility.setNumero(numeroVariazione);
		utility.setStatoOperativoVariazioneDiBilancio(statoOperativoVariazioneBilancio);
		utility.setTipoVariazione(tipoVariazione);
		utility.setUid(uidVariazioneImportoCapitolo);
		
		// Ottengo la lista da inserire nella variazione dal model della specifica
		List<ElementoCapitoloCodifiche> elementiNellaVariazione = specificaCodifiche.getListaElementoCapitoloCodifiche();
		List<DettaglioVariazioneCodificaCapitolo> listaDettaglioVariazioneCodifiche = ElementoCapitoloCodificheFactory.unwrap(elementiNellaVariazione);
		
		utility.setListaDettaglioVariazioneCodifica(listaDettaglioVariazioneCodifiche);
		
		//SIAC 6884
		utility.setFlagConsiglio(flagConsiglio);
		utility.setFlagGiunta(flagGiunta);
		
		return utility;
	}
	//SIAC-8332
	public void impostaDatiNelModel(AzioneRichiesta azioneRichiesta) {
		if(azioneRichiesta.getIdAttivita()!= null){
			String[] splitted = StringUtils.split(azioneRichiesta.getIdAttivita(), "%&");
			if(splitted.length > 0) {
				uidVariazione = Integer.valueOf(splitted[0]);
			}
		}
	}
	
	/**
	 * Injetta le variabili del processo.
	 * 
	 * @param azioneRichiesta l'azione richiesta da cui ottenere le variabili di processo
	 * @deprecated by SIAC-8332
	 */
	@Deprecated
	public void injettaVariabiliProcesso(AzioneRichiesta azioneRichiesta) {
		/*
		 * SIAC 6884 
		 * Se è decentrato non abbiamo bisogno di processare
		 * dati da bonita
		 */
		if(azioneRichiesta.getAzione()!= null && azioneRichiesta.getAzione().getNome()!= null && 
				Costanti.CODICE_AZIONE_VARIAZIONE_DECENTRATO.equals(azioneRichiesta.getAzione().getNome())){
			if(azioneRichiesta.getIdAttivita()!= null){
				uidVariazione = Integer.valueOf(azioneRichiesta.getIdAttivita());
			}
			idAttivita = azioneRichiesta.getIdAttivita();
//			this.setVariazioneDecentrataAperta(true);
			
		}else{
			VariabileProcesso numeroVariazioneVP = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_NUMERO_VARIAZIONE);
			VariabileProcesso invioOrganoLegislativo = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_INVIO_CONSIGLIO);
			VariabileProcesso invioOrganoAmministrativo = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_INVIO_GIUNTA);
			
			String uidVariazioneStringa = ((String)numeroVariazioneVP.getValore()).split("\\|")[0];
			uidVariazione = Integer.valueOf(uidVariazioneStringa);
			
			invioAdOrganoAmministrativo = (Boolean) invioOrganoAmministrativo.getValore();
			invioAdOrganoLegislativo = (Boolean) invioOrganoLegislativo.getValore();
			
			idAttivita = azioneRichiesta.getIdAttivita();
		}
		
		
	}
	
	/**
	 * Popola il model a partire dalla variazione di getBilancio().
	 * 
	 * @param variazioneDiBilancio la variazione tramite cui popolare il model
	 */
	public void popolaModel(VariazioneImportoCapitolo variazioneDiBilancio) {
		//TODO: vedere se posso togliere l'atto amministrativo almeno nell'execute
		//popolo la anagrafica
		numeroVariazione = variazioneDiBilancio.getNumero();
		descrizione = variazioneDiBilancio.getDescrizione();
		tipoVariazione = variazioneDiBilancio.getTipoVariazione();
		uidVariazioneImportoCapitolo = variazioneDiBilancio.getUid();
		//TODO:ma questo viene mai utilizzato? perche e' qui?
		dataVariazione = variazioneDiBilancio.getData();		

		statoOperativoVariazioneBilancio = variazioneDiBilancio.getStatoOperativoVariazioneDiBilancio();
		
		elementoStatoOperativoVariazione = ElementoStatoOperativoVariazioneFactory.getInstance(getEnte().getGestioneLivelli(), statoOperativoVariazioneBilancio);
		
		note = variazioneDiBilancio.getNote();
		attoAmministrativo = variazioneDiBilancio.getAttoAmministrativo();
		setStringaProvvedimento(getStringDescrizioneAtto(attoAmministrativo, "provvedimento variazione di PEG"));
		//SIAC-4737
		attoAmministrativoAggiuntivo = variazioneDiBilancio.getAttoAmministrativoVariazioneBilancio();
		setStringaProvvedimentoAggiuntivo(getStringDescrizioneAtto(attoAmministrativoAggiuntivo, "provvedimento variazione di bilancio"));
		applicazione = variazioneDiBilancio.getApplicazioneVariazione();
		//SIAC-6705
//		setStatiOperativiElementoDiBilancio(Arrays.asList(StatoOperativoElementoDiBilancio.VALIDO, StatoOperativoElementoDiBilancio.PROVVISORIO));
		getSpecificaImporti().setStatiOperativiElementoDiBilancio(Arrays.asList(StatoOperativoElementoDiBilancio.VALIDO, StatoOperativoElementoDiBilancio.PROVVISORIO));
		//SIAC-6884
		dataApertura = variazioneDiBilancio.getDataAperturaProposta();
		if(dataApertura != null){
			//la variazione è decentrata
			direzioneProponente = variazioneDiBilancio.getDirezioneProponente();
		}
		dataChiusuraProposta = variazioneDiBilancio.getDataChiusuraProposta();
		flagConsiglio = variazioneDiBilancio.getFlagConsiglio();
		flagGiunta = variazioneDiBilancio.getFlagGiunta();
		//SIAC-8332
		invioAdOrganoAmministrativo = variazioneDiBilancio.getFlagGiunta();
		invioAdOrganoLegislativo = variazioneDiBilancio.getFlagGiunta();
	}
	
	//SIAC-6884 - nella pagina mi serve sapere se la variazione è decentrata
	public boolean getIsDecentrata(){
		return StatoOperativoVariazioneBilancio.PRE_BOZZA.equals(statoOperativoVariazioneBilancio);
	}
	
	/**
	 * Popola il model a partire dalla variazione di getBilancio().
	 * 
	 * @param variazioneCodifica               la variazione tramite cui popolare il model
	 * @param attoAmministrativoPerPopolamento l'attoAmministrativo tramite cui popolare il model
	 * 
	 * @throws ApplicationException nel caso in cui la variazione non sia correttamente inserita 
	 */
	public void popolaModel(VariazioneCodificaCapitolo variazioneCodifica, AttoAmministrativo attoAmministrativoPerPopolamento) throws ApplicationException {
		numeroVariazione = variazioneCodifica.getNumero();
		descrizione = variazioneCodifica.getDescrizione();
		tipoVariazione = variazioneCodifica.getTipoVariazione();
		uidVariazioneImportoCapitolo = variazioneCodifica.getUid();
		dataVariazione = variazioneCodifica.getData();
		
		List<Capitolo<?, ?>> listaCapitoli = variazioneCodifica.getCapitoli();
		if(!listaCapitoli.isEmpty()){
			TipoCapitolo tipoCapitolo = listaCapitoli.get(0).getTipoCapitolo();
			if(tipoCapitolo == TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE || tipoCapitolo == TipoCapitolo.CAPITOLO_USCITA_PREVISIONE) {
				applicazione = ApplicazioneVariazione.PREVISIONE;
			} else {
				applicazione = ApplicazioneVariazione.GESTIONE;
			}
		}
		setBilancio(variazioneCodifica.getBilancio());
		annoCompetenza = getBilancio().getAnno();
		
		statoOperativoVariazioneBilancio = variazioneCodifica.getStatoOperativoVariazioneDiBilancio();
		elementoStatoOperativoVariazione = ElementoStatoOperativoVariazioneFactory.getInstance(getEnte().getGestioneLivelli(), statoOperativoVariazioneBilancio);
		
		note = variazioneCodifica.getNote();
		
		if(attoAmministrativoPerPopolamento != null) {
			attoAmministrativoOld = attoAmministrativoPerPopolamento;
			attoAmministrativo = attoAmministrativoPerPopolamento;
			strutturaAmministrativoContabile = attoAmministrativoPerPopolamento.getStrutturaAmmContabile();
			tipoAtto = attoAmministrativoPerPopolamento.getTipoAtto();
		}
		
		List<DettaglioVariazioneCodificaCapitolo> listaVariazioni = variazioneCodifica.getListaDettaglioVariazioneCodifica();
		if(listaVariazioni == null || listaVariazioni.isEmpty()) {
			throw new ApplicationException("Lista variazione importi non inizializzata correttamente");
		}
		
		List<ElementoCapitoloCodifiche> listaDaInjettare = ElementoCapitoloCodificheFactory.getInstancesFromDettaglio(listaVariazioni);
		specificaCodifiche.setListaElementoCapitoloCodifiche(listaDaInjettare);
		
		//SIAC 6884
		flagConsiglio = variazioneCodifica.getFlagConsiglio();
		flagGiunta = variazioneCodifica.getFlagGiunta();
		
	}
	
	/**
	 * POpolamento della stringa del provvedimento.
	 * 
	 * @param attoAmministrativo l'atto amministrativo da cui creare la stringa
	 */
	public void popolaStringaProvvedimento(AttoAmministrativo attoAmministrativo) {
		setStringaProvvedimento(getStringDescrizioneAtto(attoAmministrativo, null));
	}
	
	/**
	 * POpolamento della stringa del provvedimento.
	 * 
	 * @param attoAmministrativo l'atto amministrativo da cui creare la stringa
	 * @param descrizioneAtto la descrizione da pre-pendere alla stringa
	 */
	public void popolaStringaProvvedimento(AttoAmministrativo attoAmministrativo, String descrizioneAtto) {
		setStringaProvvedimento(getStringDescrizioneAtto(attoAmministrativo, descrizioneAtto));
	}
	
	/**
	 * POpolamento della stringa del provvedimento.
	 * 
	 * @param attoAmministrativo l'atto amministrativo da cui creare la stringa
	 */
	public void popolaStringaProvvedimentoAggiuntivo(AttoAmministrativo attoAmministrativo) {
		
		setStringaProvvedimentoAggiuntivo(getStringDescrizioneAtto(attoAmministrativo, "provvedimento variazione di bilancio"));
	}


	/**
	 * Gets the string descrizione atto.
	 *
	 * @param provvedimento il provvedimento da cui ottenere la desrizione
	 * @param prefissoDescrizione il prefisso della descrizione
	 * @return the string descrizione atto la descrizione dell'atto da visualizzare
	 */
	protected String getStringDescrizioneAtto(AttoAmministrativo provvedimento, String prefissoDescrizione) {
		StringBuilder sb = new StringBuilder();
		String attoAmmTesto = prefissoDescrizione != null? prefissoDescrizione : "provvedimento";
		if(provvedimento != null){
			sb.append(attoAmmTesto)
				.append(" : ")
				.append(provvedimento.getAnno())
				.append(provvedimento.getNumero() != 0 ? " / " : "")
				.append(provvedimento.getNumero() != 0 ? provvedimento.getNumero() : "")
				.append(provvedimento.getTipoAtto() != null  && provvedimento.getTipoAtto().getCodice() != null && !provvedimento.getTipoAtto().getCodice().isEmpty() ? " / " : "")
				.append(provvedimento.getTipoAtto() != null ? provvedimento.getTipoAtto().getCodice() + " - " + provvedimento.getTipoAtto().getDescrizione(): "");
			return sb.toString();
		}
		return sb.append("Nessun " + attoAmmTesto + " selezionato").toString();
	}

	/**
	 * Gets the tipo componente by macrotipo.
	 *
	 * @param macro the macro
	 * @return the tipo componente by macrotipo
	 */
	public List<TipoComponenteImportiCapitolo> getTipoComponenteByMacrotipo(MacrotipoComponenteImportiCapitolo macro) {
		List<TipoComponenteImportiCapitolo> lista = new ArrayList<TipoComponenteImportiCapitolo>();
		if(macro == null) {
			return lista;
		}
		for (TipoComponenteImportiCapitolo ti : getListaTipoComponente()) {
			if(ti.getMacrotipoComponenteImportiCapitolo().equals(macro)) {
				lista.add(ti);
			}
		}
		return lista;
	}
	
	/**
	 * Arrotonda importo.
	 *
	 * @param importo the importo
	 * @return the big decimal
	 */
	//SIAC-7284
	public BigDecimal arrotondaImporto(BigDecimal importo) {
		if(importo == null) {
			return BigDecimal.ZERO;
		}
		return importo.setScale(2, RoundingMode.HALF_DOWN);
	}
	/**
	 * Creo la request per la ricerca dell' anagrafica della variazione di bilancio
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioAnagraficaVariazioneBilancio creaRequestRicercaDettaglioAnagraficaVariazioneBilancio() {
		
		RicercaDettaglioAnagraficaVariazioneBilancio request = creaRequest(RicercaDettaglioAnagraficaVariazioneBilancio.class);
		request.setUidVariazione(uidVariazione);
		
		return request;
	}

	/** 
	 * Crea la request di ricerca dei capitoli collegati alla variazione
	 * @return la request
	 */
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazione creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione() {
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = creaRequest(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class);
		request.setUidVariazione(uidVariazione);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	/**
	 * Crea la request popolata di per l'annullamento della variazione di bilancio
	 * @return la request creata
	 */
	public AggiornaAnagraficaVariazioneBilancio creaRequestAggiornaAnagraficaVariazioneBilancioPerAnnullamento() {
		return popolaRequestAnagrafica(Boolean.TRUE, Boolean.TRUE);
		
	}
	
	/**
	 * Crea la request per l'aggiornamento di un capitolo nella variazione
	 * @return the aggiornaDettaglioVariazioneImportoCapitolo request
	 */
	public AggiornaDettaglioVariazioneImportoCapitolo creaRequestAggiornaDettaglioVariazioneImportoCapitolo() {
		ElementoCapitoloVariazione parametri = getSpecificaImporti().getElementoCapitoloVariazione();
		
		return popolaRequestAggiornaDettagliovariazioneImportoCapitolo(parametri);
	}
	
	/**
	 * Crea la request per l'aggiornamento di un capitolo nella variazione
	 * @return the aggiornaDettaglioVariazioneImportoCapitolo request
	 */
	public AggiornaDettaglioVariazioneImportoCapitolo creaRequestAggiornaDettaglioVariazioneImportoCapitoloUEB() {
		ElementoCapitoloVariazione parametri = getSpecificaUEB().getElementoCapitoloVariazione();
		
		return popolaRequestAggiornaDettagliovariazioneImportoCapitolo(parametri);
	}

	private AggiornaDettaglioVariazioneImportoCapitolo popolaRequestAggiornaDettagliovariazioneImportoCapitolo(ElementoCapitoloVariazione parametri) {
		AggiornaDettaglioVariazioneImportoCapitolo request = creaRequest(AggiornaDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo datiCapitoloDaAggiornare = new DettaglioVariazioneImportoCapitolo();
		
		datiCapitoloDaAggiornare.setStanziamento(arrotondaImporto(parametri.getCompetenza()));
		datiCapitoloDaAggiornare.setStanziamento1(arrotondaImporto(parametri.getCompetenza1()));
		datiCapitoloDaAggiornare.setStanziamento2(arrotondaImporto(parametri.getCompetenza2()));
		datiCapitoloDaAggiornare.setStanziamentoCassa(arrotondaImporto(parametri.getCassa()));
		datiCapitoloDaAggiornare.setStanziamentoResiduo(arrotondaImporto(parametri.getResiduo()));
		datiCapitoloDaAggiornare.setFlagNuovoCapitolo(parametri.getDaInserire());
		datiCapitoloDaAggiornare.setFlagAnnullaCapitolo(parametri.getDaAnnullare());

		
		Capitolo<?,?> cap = new Capitolo<ImportiCapitolo, ImportiCapitolo>();//datiCapitoloDaAggiornare.getCapitolo();//.setUid(parametri.getUid();
		cap.setUid(parametri.getUid());
		datiCapitoloDaAggiornare.setCapitolo(cap);
	
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneImportoCapitolo);
		datiCapitoloDaAggiornare.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
		request.setDettaglioVariazioneImportoCapitolo(datiCapitoloDaAggiornare);
		
		return request;
	}

	/**
	 * 
	 * Crea la request per scollegare un capitolo dalla variazione
	 * @return la request
	 */
	public EliminaDettaglioVariazioneImportoCapitolo creaRequestEliminaDettaglioVariazioneImportoCapitolo() {
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaImporti().getElementoCapitoloVariazione();
		return popolaRequestEliminaDettaglioVariazioneImportoCapitolo(elementoCapitoloVariazione);
	}
	
	/**
	 * 
	 * Crea la request per scollegare un capitolo dalla variazione
	 * @return la request
	 */
	public EliminaDettaglioVariazioneImportoCapitolo creaRequestEliminaDettaglioVariazioneImportoCapitoloUEB() {
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaUEB().getElementoCapitoloVariazione();
		return popolaRequestEliminaDettaglioVariazioneImportoCapitolo(elementoCapitoloVariazione);
	}

	private EliminaDettaglioVariazioneImportoCapitolo popolaRequestEliminaDettaglioVariazioneImportoCapitolo(ElementoCapitoloVariazione elementoCapitoloVariazione) {
		EliminaDettaglioVariazioneImportoCapitolo request = creaRequest(EliminaDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dett = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> cap = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		cap.setUid(elementoCapitoloVariazione.getUid());
		dett.setCapitolo(cap);
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneImportoCapitolo);
		dett.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
		request.setDettaglioVariazioneImportoCapitolo(dett);
		
		return request;
	}

	/**
	 * Creo la request per collegare un capitolo alla variazione
	 * @return la request
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitolo() {
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaImporti().getElementoCapitoloVariazione();
		return popolaRequestInserisciDettaglioImportoCapitolo(elementoCapitoloVariazione);
	}
	
	/**
	 * Creo la request per l'inserimento di un dettaglio variazione importo capitolo
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitoloUEB() {
		ElementoCapitoloVariazione elementoCapitoloVariazione = getSpecificaUEB().getElementoCapitoloVariazione();
		return popolaRequestInserisciDettaglioImportoCapitolo(elementoCapitoloVariazione);
	}
	/***
	 * Popolo la request InserisciDettaglioVariazioneImportoCapitolo a partire da un elemento capitolo
	 * @param elementoCapitoloVariazione l'elemento capitolo
	 * @return la request creata
	 * */
	private InserisciDettaglioVariazioneImportoCapitolo popolaRequestInserisciDettaglioImportoCapitolo(ElementoCapitoloVariazione elementoCapitoloVariazione) {
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = popolaDettaglioVariazioneImportoCapitoloByElementoCapitolo(elementoCapitoloVariazione);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}

	/**
	 * @param elementoCapitoloVariazione
	 * @return
	 */
	private DettaglioVariazioneImportoCapitolo popolaDettaglioVariazioneImportoCapitoloByElementoCapitolo(ElementoCapitoloVariazione elementoCapitoloVariazione) {
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(elementoCapitoloVariazione.getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		
		//SIAC-6705
		if(elementoCapitoloVariazione.getStatoOperativoElementoDiBilancio() != null && StatoOperativoElementoDiBilancio.PROVVISORIO.equals(elementoCapitoloVariazione.getStatoOperativoElementoDiBilancio())) {
			dettaglioVariazioneImportoCapitolo.setFlagNuovoCapitolo(Boolean.TRUE);
		}
		
		dettaglioVariazioneImportoCapitolo.setStanziamento(arrotondaImporto(elementoCapitoloVariazione.getCompetenza()));
		dettaglioVariazioneImportoCapitolo.setStanziamentoCassa(arrotondaImporto(elementoCapitoloVariazione.getCassa()));
		dettaglioVariazioneImportoCapitolo.setStanziamentoResiduo(arrotondaImporto(elementoCapitoloVariazione.getResiduo()));
		
		dettaglioVariazioneImportoCapitolo.setStanziamento1(arrotondaImporto(elementoCapitoloVariazione.getCompetenza1()));
		dettaglioVariazioneImportoCapitolo.setStanziamento2(arrotondaImporto(elementoCapitoloVariazione.getCompetenza2()));
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneImportoCapitolo);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		return dettaglioVariazioneImportoCapitolo;
	}
	


	/**
	 * Creo la request per collegare alla variazione un capitolo creato tramite la funzione "nuovo capitolo" della variazione
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoNuovoCapitolo() {
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		ElementoCapitoloVariazione elementoDaInserire = getSpecificaImporti().getListaCapitoliNellaVariazione().get(0);
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(elementoDaInserire.getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		dettaglioVariazioneImportoCapitolo.setFlagNuovoCapitolo(Boolean.TRUE);
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneImportoCapitolo);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		
		return request;
	}

	/**
	 * Creo la request per aggiornare il dettaglio del capitolo all'interno della variazione
	 * @return la request creata
	 */
	public AggiornaDettaglioVariazioneImportoCapitolo creaRequestAggiornaDettaglioVariazioneImportoCapitoloPerAnnullamento() {
		AggiornaDettaglioVariazioneImportoCapitolo request = creaRequest(AggiornaDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(getSpecificaImporti().getElementoCapitoloVariazione().getUid());
		capitolo.setStatoOperativoElementoDiBilancio(getSpecificaImporti().getElementoCapitoloVariazione().getStatoOperativoElementoDiBilancio());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		
		dettaglioVariazioneImportoCapitolo.setFlagAnnullaCapitolo(Boolean.TRUE);
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneImportoCapitolo);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}
	
	/**
	 * Creo la request per collegare un capitolo alla variazione
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoAnnullaCapitolo() {
		ElementoCapitoloVariazione elementoDaInserire = getSpecificaImporti().getElementoCapitoloVariazione();
		return popolaRequestInserisciDettaglioVariazioneImportoAnnullamento(elementoDaInserire);
	}
	
	/**
	 * Creo la request per collegare un capitolo alla variazione
	 * @param elementoDaCollegare l'elemento da collegare
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoAnnullaCapitoloUEB(ElementoCapitoloVariazione elementoDaCollegare) {
		
		return popolaRequestInserisciDettaglioVariazioneImportoAnnullamento(elementoDaCollegare);
	}

	private InserisciDettaglioVariazioneImportoCapitolo popolaRequestInserisciDettaglioVariazioneImportoAnnullamento(
			ElementoCapitoloVariazione elementoDaInserire) {
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(elementoDaInserire.getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		
		dettaglioVariazioneImportoCapitolo.setFlagAnnullaCapitolo(Boolean.TRUE);
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneImportoCapitolo);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		
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
		req.setCapitolo(getCapitoloUscitaPrevisioneNellaVariazione());
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
		req.setCapitolo(getCapitoloEntrataPrevisioneNellaVariazione());
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
		req.setCapitolo(getCapitoloEntrataGestioneNellaVariazione());
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
		req.setCapitolo(getCapitoloUscitaGestioneNellaVariazione());
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
		req.setUidVariazione(uidVariazioneImportoCapitolo);
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
		variazioneImportoCapitolo.setUid(getUidVariazioneImportoCapitolo().intValue());
		req.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
		return req;
	}

	/**
	 * Crea request ricerca tipo componenti importo capitolo.
	 *
	 * @return the ricerca tipo componente importi capitolo per capitolo
	 */
	public RicercaTipoComponenteImportiCapitoloPerCapitolo creaRequestRicercaTipoComponentiImportoCapitoloPerCapitolo() {
		RicercaTipoComponenteImportiCapitoloPerCapitolo req = creaRequest(RicercaTipoComponenteImportiCapitoloPerCapitolo.class);
		req.setCapitolo(new CapitoloUscitaPrevisione());
		req.getCapitolo().setUid(getSpecificaImporti().getUidCapitoloComponenti());
		return req;
	}
	
	/**
	 * Crea request ricerca tipo componenti importo capitolo.
	 *
	 * @return the ricerca tipo componente importi capitolo per capitolo
	 */
	public RicercaSinteticaTipoComponenteImportiCapitolo creaRequestRicercaSinteticaTipoComponentiImportoCapitolo() {
		RicercaSinteticaTipoComponenteImportiCapitolo req = creaRequest(RicercaSinteticaTipoComponenteImportiCapitolo.class);
		req.setTipoComponenteImportiCapitolo(new TipoComponenteImportiCapitolo());
		req.getTipoComponenteImportiCapitolo().setStatoTipoComponenteImportiCapitolo(StatoTipoComponenteImportiCapitolo.VALIDO);
		req.setMacrotipoComponenteImportiCapitoloDaEscludere(ImportiCapitoloComponenteVariazioneModificabile.getArrayMacrotipiNonDigitabiliSuiTreAnni(getApplicazione()));
		req.setSottotipoComponenteImportiCapitoloDaEscludere(ImportiCapitoloComponenteVariazioneModificabile.getArraySottoTipiNonDigitabiliSuiTreAnni(getApplicazione()));
		req.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		return req;
	}

	/**
	 * Crea request gestisci dettaglio variazione componente importo capitolo.
	 *
	 * @param detts0 the detts 0
	 * @param detts1 the detts 1
	 * @param detts2 the detts 2
	 * @param isInserimento 
	 * @return the gestisci dettaglio variazione componente importo capitolo
	 */
	public GestisciDettaglioVariazioneComponenteImportoCapitolo creaRequestGestisciDettaglioVariazioneComponenteImportoCapitolo(List<DettaglioVariazioneComponenteImportoCapitolo> detts0, List<DettaglioVariazioneComponenteImportoCapitolo> detts1, List<DettaglioVariazioneComponenteImportoCapitolo> detts2, Boolean isInserimento) {
		GestisciDettaglioVariazioneComponenteImportoCapitolo req = creaRequest(GestisciDettaglioVariazioneComponenteImportoCapitolo.class);
		req.setBilancio(getBilancio());
		req.setDettaglioVariazioneImportoCapitolo(popolaDettaglioVariazioneImportoCapitoloByElementoCapitolo(getSpecificaImporti().getElementoCapitoloVariazione()));
		req.getDettaglioVariazioneImportoCapitolo().setListaDettaglioVariazioneComponenteImportoCapitolo(detts0);
		req.getDettaglioVariazioneImportoCapitolo().setListaDettaglioVariazioneComponenteImportoCapitolo1(detts1);
		req.getDettaglioVariazioneImportoCapitolo().setListaDettaglioVariazioneComponenteImportoCapitolo2(detts2);
		req.setDettaglioInInserimento(isInserimento);
		return req;
	}

	/**
	 * Crea request ricerca dettaglio variazione componente importo capitolo.
	 *
	 * @return the ricerca dettaglio variazione componente importo capitolo
	 */
	public RicercaDettaglioVariazioneComponenteImportoCapitolo creaRequestRicercaDettaglioVariazioneComponenteImportoCapitolo() {
		RicercaDettaglioVariazioneComponenteImportoCapitolo req = creaRequest(RicercaDettaglioVariazioneComponenteImportoCapitolo.class);
		req.setDettaglioVariazioneImportoCapitolo(new DettaglioVariazioneImportoCapitolo());
		req.setModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag, DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo, ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo, ComponenteImportiCapitoloModelDetail.Importo, TipoComponenteImportiCapitoloModelDetail.MacrotipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail.SottotipoComponenteImportiCapitolo);
		CapitoloUscitaPrevisione cap = new CapitoloUscitaPrevisione();
		cap.setUid(getSpecificaImporti().getUidCapitoloAssociatoComponenti());
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(cap);
		VariazioneImportoCapitolo var = new VariazioneImportoCapitolo();
		var.setUid(getUidVariazioneImportoCapitolo());
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(var);
		return req;
	}
	
	
		/*
		 * SIAC 6884
		 * popola la request per la chiusura della proposta
		 */
		public AggiornaAnagraficaVariazioneBilancio creaRequestAggiornaAnagraficaVariazioneBilancioDecentrato() {
			AggiornaAnagraficaVariazioneBilancio request = creaRequest(AggiornaAnagraficaVariazioneBilancio.class);
			request.setAnnullaVariazione(Boolean.FALSE);
			request.setDataOra(new Date());
			request.setIdAttivita(idAttivita);
			request.setRichiedente(getRichiedente());
			request.setSaltaCheckStanziamentoCassa(Boolean.parseBoolean(saltaCheckStanziamentoCassa));
			request.setSaltaCheckStanziamento(Boolean.parseBoolean(saltaCheckStanziamento));
			request.setSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio(isSaltaCheckProvvedimentoVariazioneBilancio());
			request.setVariazioneImportoCapitolo(creaUtilityAnagraficaVariazioneImportoCapitolo());
			request.setEvolviProcesso(Boolean.TRUE);
			request.setAggiornamentoDaVariazioneDecentrata(true);
			request.setInvioOrganoAmministrativo(flagGiunta);
			request.setInvioOrganoLegislativo(flagConsiglio);
			request.setAggiornamentoVariazionieDecentrataFromAction(CostantiFin.AGG_VAR_FROM_CHIUDI_PROPOSTA);
			return request;
	}

		
		
		public AggiornaAnagraficaVariazioneBilancio creaRequestAggiornaAnagraficaVariazioneBilancioPerAnnullamentoDecentrato() {

			AggiornaAnagraficaVariazioneBilancio request = creaRequest(AggiornaAnagraficaVariazioneBilancio.class);
			request.setAnnullaVariazione(Boolean.TRUE);
			request.setDataOra(new Date());
			request.setEvolviProcesso(Boolean.FALSE);
			request.setIdAttivita(idAttivita);
			request.setRichiedente(getRichiedente());
			request.setSaltaCheckStanziamentoCassa(Boolean.parseBoolean(saltaCheckStanziamentoCassa));
			request.setSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio(isSaltaCheckProvvedimentoVariazioneBilancio());
			request.setVariazioneImportoCapitolo(creaUtilityAnagraficaVariazioneImportoCapitolo());
			request.setInvioOrganoAmministrativo(flagGiunta);
			request.setInvioOrganoLegislativo(flagConsiglio);
			request.setAggiornamentoDaVariazioneDecentrata(true);
			return request;
		
			
		}	
		
		
		/**
		 * @return the dataChiusuraProposta
		 */
		public Date getDataChiusuraProposta() {
			return dataChiusuraProposta;
		}

		/**
		 * @param dataChiusuraProposta the dataChiusuraProposta to set
		 */
		public void setDataChiusuraProposta(Date dataChiusuraProposta) {
			this.dataChiusuraProposta = dataChiusuraProposta;
		}
		
		
		//SIAC-6884 necessario per ottenere le info del capitolo da aggiungere
		public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione(int uidCapitolo) {
			
			
			
			
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @return the flagGiunta
		 */
		public Boolean getFlagGiunta() {
			return flagGiunta;
		}

		/**
		 * @return the flagConsiglio
		 */
		public Boolean getFlagConsiglio() {
			return flagConsiglio;
		}

		/**
		 * @param flagGiunta the flagGiunta to set
		 */
		public void setFlagGiunta(Boolean flagGiunta) {
			this.flagGiunta = flagGiunta;
		}

		/**
		 * @param flagConsiglio the flagConsiglio to set
		 */
		public void setFlagConsiglio(Boolean flagConsiglio) {
			this.flagConsiglio = flagConsiglio;
		}

		/**
		 * @return the richiediConfermaQuadraturaCP
		 */
		public Boolean getRichiediConfermaQuadraturaCP()
		{
			return richiediConfermaQuadraturaCP;
		}

		/**
		 * @param richiediConfermaQuadraturaCP the richiediConfermaQuadraturaCP to set
		 */
		public void setRichiediConfermaQuadraturaCP(Boolean richiediConfermaQuadraturaCP)
		{
			this.richiediConfermaQuadraturaCP = richiediConfermaQuadraturaCP;
		}
		
		
		

}
