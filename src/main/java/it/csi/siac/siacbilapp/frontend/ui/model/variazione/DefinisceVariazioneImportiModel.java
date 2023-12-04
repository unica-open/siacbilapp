/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;

/**
 * Classe di model per la consultazione delle variazioni Importi. 
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 *
 */

public class DefinisceVariazioneImportiModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4256856520682050552L;
	
	private VariazioneImportoCapitolo variazioneImportoCapitolo;
	
	private Integer numeroVariazione;
	private Boolean definizioneEseguita = Boolean.FALSE;
	private String idAttivita;
	
	private Integer uidVariazione;
	private String applicazioneVariazione;
	private String descrizioneVariazione;
	private TipoVariazione tipoVariazione;
	private Integer annoCompetenza;
	private StatoOperativoVariazioneBilancio statoVariazione;
	private String noteVariazione;
	
	private Integer uidProvvedimento;
	private String tipoProvvedimento;
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private String strutturaProvvedimento;
	private String oggettoProvvedimento;
	
	//SIAC-6884
	private Date dataAperturaProposta;
	private Date dataChiusuraProposta;
	private StrutturaAmministrativoContabile direzioneProponente;
	private Date dataInizioValiditaStatoVariazione;
	private boolean variazioneDecentrata;
	
	//SIAC-4737
	private Integer uidProvvedimentoVariazioneDiBilancio;
	private String tipoProvvedimentoVariazioneDiBilancio;
	private String annoProvvedimentoVariazioneDiBilancio;
	private String numeroProvvedimentoVariazioneDiBilancio;
	private String strutturaProvvedimentoVariazioneDiBilancio;
	private String oggettoProvvedimentoVariazioneDiBilancio;
	
	private BigDecimal totaleStanziamentiEntrata;
	private BigDecimal totaleStanziamentiCassaEntrata;
	private BigDecimal totaleStanziamentiResiduiEntrata;
	private BigDecimal totaleStanziamentiSpesa;
	private BigDecimal totaleStanziamentiCassaSpesa;
	private BigDecimal totaleStanziamentiResiduiSpesa;
	
	private Integer idOperazioneAsincrona;
	
	private Boolean daInviareInGiunta;
	private Boolean daInviareInConsiglio;
	
	private Boolean isAsyncResponsePresent;
	
	private Integer idxCapitoloSelezionato;
	private List<ElementoCapitoloVariazione> listaCapitoli = new ArrayList<ElementoCapitoloVariazione>();
	private List<ElementoCapitoloVariazione> listaUEB = new ArrayList<ElementoCapitoloVariazione>();
	
	//SIAC-5016
	private ApplicazioneVariazione applicazione;
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
	
	//SIAC-6881
	private Integer uidCapitoloConComponenti;
	//SIAC-6883
	List<ElementoComponenteVariazione> componentiCapitolo = new ArrayList<ElementoComponenteVariazione>();
	private BigDecimal totaleStanziamentiEntrata1;
	private BigDecimal totaleStanziamentiEntrata2;
	private BigDecimal totaleStanziamentiSpesa1;
	private BigDecimal totaleStanziamentiSpesa2;
	
	//SIAC 6884
	/**
	 * @return the variazioneDecentrata
	 */
	public boolean isVariazioneDecentrata() {
		return variazioneDecentrata;
	}

	/**
	 * @param variazioneDecentrata the variazioneDecentrata to set
	 */
	public void setVariazioneDecentrata(boolean variazioneDecentrata) {
		this.variazioneDecentrata = variazioneDecentrata;
	}
	
	//END SIAC 6884
	
	/** Costruttore vuoto di default */
	public DefinisceVariazioneImportiModel() {
		super();
		setTitolo(" Definisci Variazione");
	}

	
	
	/**
	 * @return the dataAperturaProposta
	 */
	public Date getDataAperturaProposta() {
		return dataAperturaProposta;
	}



	/**
	 * @param dataAperturaProposta the dataAperturaProposta to set
	 */
	public void setDataAperturaProposta(Date dataAperturaProposta) {
		this.dataAperturaProposta = dataAperturaProposta;
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



	/**
	 * @return the direzioneProponente
	 */
	public StrutturaAmministrativoContabile getDirezioneProponente() {
		return direzioneProponente;
	}



	/**
	 * @param direzioneProponente the direzioneProponente to set
	 */
	public void setDirezioneProponente(StrutturaAmministrativoContabile direzioneProponente) {
		this.direzioneProponente = direzioneProponente;
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
	 * @return the applicazioneVariazione
	 */
	public String getApplicazioneVariazione() {
		return applicazioneVariazione;
	}



	/**
	 * @param applicazioneVariazione the applicazioneVariazione to set
	 */
	public void setApplicazioneVariazione(String applicazioneVariazione) {
		this.applicazioneVariazione = applicazioneVariazione;
	}



	/**
	 * @return the descrizioneVariazione
	 */
	public String getDescrizioneVariazione() {
		return descrizioneVariazione;
	}



	/**
	 * @param descrizioneVariazione the descrizioneVariazione to set
	 */
	public void setDescrizioneVariazione(String descrizioneVariazione) {
		this.descrizioneVariazione = descrizioneVariazione;
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
	 * @return the statoVariazione
	 */
	public StatoOperativoVariazioneBilancio getStatoVariazione() {
		return statoVariazione;
	}



	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(
			StatoOperativoVariazioneBilancio statoVariazione) {
		this.statoVariazione = statoVariazione;
	}



	/**
	 * @return the noteVariazione
	 */
	public String getNoteVariazione() {
		return noteVariazione;
	}



	/**
	 * @param noteVariazione the noteVariazione to set
	 */
	public void setNoteVariazione(String noteVariazione) {
		this.noteVariazione = noteVariazione;
	}



	/**
	 * @return the uidProvvedimento
	 */
	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}



	/**
	 * @param uidProvvedimento the uidProvvedimento to set
	 */
	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}



	/**
	 * @return the tipoProvvedimento
	 */
	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}



	/**
	 * @param tipoProvvedimento the tipoProvvedimento to set
	 */
	public void setTipoProvvedimento(String tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}



	/**
	 * @return the annoProvvedimento
	 */
	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}



	/**
	 * @param annoProvvedimento the annoProvvedimento to set
	 */
	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}



	/**
	 * @return the numeroProvvedimento
	 */
	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}



	/**
	 * @param numeroProvvedimento the numeroProvvedimento to set
	 */
	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}



	/**
	 * @return the strutturaProvvedimento
	 */
	public String getStrutturaProvvedimento() {
		return strutturaProvvedimento;
	}



	/**
	 * @param strutturaProvvedimento the strutturaProvvedimento to set
	 */
	public void setStrutturaProvvedimento(String strutturaProvvedimento) {
		this.strutturaProvvedimento = strutturaProvvedimento;
	}



	/**
	 * @return the oggettoProvvedimento
	 */
	public String getOggettoProvvedimento() {
		return oggettoProvvedimento;
	}



	/**
	 * @param oggettoProvvedimento the oggettoProvvedimento to set
	 */
	public void setOggettoProvvedimento(String oggettoProvvedimento) {
		this.oggettoProvvedimento = oggettoProvvedimento;
	}
	
	/**
	 * @return the uidProvvedimento
	 */
	public Integer getUidProvvedimentoVariazioneDiBilancio() {
		return uidProvvedimentoVariazioneDiBilancio != null? uidProvvedimentoVariazioneDiBilancio : Integer.valueOf(0);
	}



	/**
	 * @param uidProvvedimentoVariazioneDiBilancio the uidProvvedimentoVariazioneDiBilancio to set
	 */
	public void setUidProvvedimentoVariazioneDiBilancio(Integer uidProvvedimentoVariazioneDiBilancio) {
		this.uidProvvedimentoVariazioneDiBilancio = uidProvvedimentoVariazioneDiBilancio;
	}



	/**
	 * @return the tipoProvvedimentoVariazioneDiBilancio
	 */
	public String getTipoProvvedimentoVariazioneDiBilancio() {
		return tipoProvvedimentoVariazioneDiBilancio != null? tipoProvvedimentoVariazioneDiBilancio : "";
	}



	/**
	 * @param tipoProvvedimentoVariazioneDiBilancio the tipoProvvedimento to set
	 */
	public void setTipoProvvedimentoVariazioneDiBilancio(String tipoProvvedimentoVariazioneDiBilancio) {
		this.tipoProvvedimentoVariazioneDiBilancio = tipoProvvedimentoVariazioneDiBilancio;
	}



	/**
	 * @return the annoProvvedimento
	 */
	public String getAnnoProvvedimentoVariazioneDiBilancio() {
		return annoProvvedimentoVariazioneDiBilancio != null? annoProvvedimentoVariazioneDiBilancio : "";
	}



	/**
	 * @param annoProvvedimentoVariazioneDiBilancio the annoProvvedimento to set
	 */
	public void setAnnoProvvedimentoVariazioneDiBilancio(String annoProvvedimentoVariazioneDiBilancio) {
		this.annoProvvedimentoVariazioneDiBilancio = annoProvvedimentoVariazioneDiBilancio;
	}



	/**
	 * @return the numeroProvvedimento
	 */
	public String getNumeroProvvedimentoVariazioneDiBilancio() {
		return numeroProvvedimentoVariazioneDiBilancio != null? numeroProvvedimentoVariazioneDiBilancio : "";
	}



	/**
	 * @param numeroProvvedimentoVariazioneDiBilancio the numeroProvvedimentoVariazioneDiBilancio to set
	 */
	public void setNumeroProvvedimentoVariazioneDiBilancio(String numeroProvvedimentoVariazioneDiBilancio) {
		this.numeroProvvedimentoVariazioneDiBilancio = numeroProvvedimentoVariazioneDiBilancio;
	}



	/**
	 * @return the strutturaProvvedimento
	 */
	public String getStrutturaProvvedimentoVariazioneDiBilancio() {
		return strutturaProvvedimentoVariazioneDiBilancio != null? strutturaProvvedimentoVariazioneDiBilancio : "";
	}



	/**
	 * @param strutturaProvvedimentoVariazioneDiBilancio the strutturaProvvedimento to set
	 */
	public void setStrutturaProvvedimentoVariazioneDiBilancio(String strutturaProvvedimentoVariazioneDiBilancio) {
		this.strutturaProvvedimentoVariazioneDiBilancio = strutturaProvvedimentoVariazioneDiBilancio;
	}



	/**
	 * @return the oggettoProvvedimento
	 */
	public String getOggettoProvvedimentoVariazioneDiBilancio() {
		return oggettoProvvedimentoVariazioneDiBilancio != null? oggettoProvvedimentoVariazioneDiBilancio : "";
	}



	/**
	 * @param oggettoProvvedimentoVariazioneDiBilancio the oggettoProvvedimento to set
	 */
	public void setOggettoProvvedimentoVariazioneDiBilancio(String oggettoProvvedimentoVariazioneDiBilancio) {
		this.oggettoProvvedimentoVariazioneDiBilancio = oggettoProvvedimentoVariazioneDiBilancio;
	}




	/**
	 * @return the daInviareInGiunta
	 */
	public Boolean getDaInviareInGiunta() {
		return daInviareInGiunta;
	}



	/**
	 * @param daInviareInGiunta the daInviareInGiunta to set
	 */
	public void setDaInviareInGiunta(Boolean daInviareInGiunta) {
		this.daInviareInGiunta = daInviareInGiunta;
	}



	/**
	 * @return the daInviareInConsiglio
	 */
	public Boolean getDaInviareInConsiglio() {
		return daInviareInConsiglio;
	}



	/**
	 * @param daInviareInConsiglio the daInviareInConsiglio to set
	 */
	public void setDaInviareInConsiglio(Boolean daInviareInConsiglio) {
		this.daInviareInConsiglio = daInviareInConsiglio;
	}



	/**
	 * @return the idxCapitoloSelezionato
	 */
	public Integer getIdxCapitoloSelezionato() {
		return idxCapitoloSelezionato;
	}



	/**
	 * @param idxCapitoloSelezionato the idxCapitoloSelezionato to set
	 */
	public void setIdxCapitoloSelezionato(Integer idxCapitoloSelezionato) {
		this.idxCapitoloSelezionato = idxCapitoloSelezionato;
	}



	/**
	 * @return the listaVariazioni
	 */
	public List<ElementoCapitoloVariazione> getListaCapitoli() {
		return listaCapitoli;
	}



	/**
	 * @param listaCapitoli the listaVariazioni to set
	 */
	public void setListaCapitoli(List<ElementoCapitoloVariazione> listaCapitoli) {
		this.listaCapitoli = listaCapitoli;
	}
	
	
	/**
	 * @return the totaleStanziamentiEntrata
	 */
	public BigDecimal getTotaleStanziamentiEntrata() {
		return totaleStanziamentiEntrata;
	}



	/**
	 * @return the totaleStanziamentiCassaEntrata
	 */
	public BigDecimal getTotaleStanziamentiCassaEntrata() {
		return totaleStanziamentiCassaEntrata;
	}



	/**
	 * @return the totaleStanziamentiResiduiEntrata
	 */
	public BigDecimal getTotaleStanziamentiResiduiEntrata() {
		return totaleStanziamentiResiduiEntrata;
	}



	/**
	 * @return the totaleStanziamentiSpesa
	 */
	public BigDecimal getTotaleStanziamentiSpesa() {
		return totaleStanziamentiSpesa;
	}



	/**
	 * @return the totaleStanziamentiCassaSpesa
	 */
	public BigDecimal getTotaleStanziamentiCassaSpesa() {
		return totaleStanziamentiCassaSpesa;
	}



	/**
	 * @return the totaleStanziamentiResiduiSpesa
	 */
	public BigDecimal getTotaleStanziamentiResiduiSpesa() {
		return totaleStanziamentiResiduiSpesa;
	}



	/**
	 * @param totaleStanziamentiEntrata the totaleStanziamentiEntrata to set
	 */
	public void setTotaleStanziamentiEntrata(BigDecimal totaleStanziamentiEntrata) {
		this.totaleStanziamentiEntrata = totaleStanziamentiEntrata;
	}



	/**
	 * @param totaleStanziamentiCassaEntrata the totaleStanziamentiCassaEntrata to set
	 */
	public void setTotaleStanziamentiCassaEntrata(BigDecimal totaleStanziamentiCassaEntrata) {
		this.totaleStanziamentiCassaEntrata = totaleStanziamentiCassaEntrata;
	}



	/**
	 * @param totaleStanziamentiResiduiEntrata the totaleStanziamentiResiduiEntrata to set
	 */
	public void setTotaleStanziamentiResiduiEntrata(BigDecimal totaleStanziamentiResiduiEntrata) {
		this.totaleStanziamentiResiduiEntrata = totaleStanziamentiResiduiEntrata;
	}



	/**
	 * @param totaleStanziamentiSpesa the totaleStanziamentiSpesa to set
	 */
	public void setTotaleStanziamentiSpesa(BigDecimal totaleStanziamentiSpesa) {
		this.totaleStanziamentiSpesa = totaleStanziamentiSpesa;
	}



	/**
	 * @param totaleStanziamentiCassaSpesa the totaleStanziamentiCassaSpesa to set
	 */
	public void setTotaleStanziamentiCassaSpesa(BigDecimal totaleStanziamentiCassaSpesa) {
		this.totaleStanziamentiCassaSpesa = totaleStanziamentiCassaSpesa;
	}



	/**
	 * @param totaleStanziamentiResiduiSpesa the totaleStanziamentiResiduiSpesa to set
	 */
	public void setTotaleStanziamentiResiduiSpesa(BigDecimal totaleStanziamentiResiduiSpesa) {
		this.totaleStanziamentiResiduiSpesa = totaleStanziamentiResiduiSpesa;
	}



	/**
	 * @return the listaUEB
	 */
	public List<ElementoCapitoloVariazione> getListaUEB() {
		return listaUEB;
	}



	/**
	 * @param listaUEB the listaUEB to set
	 */
	public void setListaUEB(List<ElementoCapitoloVariazione> listaUEB) {
		this.listaUEB = listaUEB;
	}



	/**
	 * @return the variazioneImportoCapitolo
	 */
	public VariazioneImportoCapitolo getVariazioneImportoCapitolo() {
		return variazioneImportoCapitolo;
	}



	/**
	 * @param variazioneImportoCapitolo the variazioneImportoCapitolo to set
	 */
	public void setVariazioneImportoCapitolo(
			VariazioneImportoCapitolo variazioneImportoCapitolo) {
		this.variazioneImportoCapitolo = variazioneImportoCapitolo;
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
	 * @return the definizioneEseguita
	 */
	public Boolean getDefinizioneEseguita() {
		return definizioneEseguita;
	}



	/**
	 * @param definizioneEseguita the definizioneEseguita to set
	 */
	public void setDefinizioneEseguita(Boolean definizioneEseguita) {
		this.definizioneEseguita = definizioneEseguita;
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
	 * @return the uidCapitoloConComponenti
	 */
	public Integer getUidCapitoloConComponenti() {
		return uidCapitoloConComponenti;
	}

	/**
	 * @param uidCapitoloConComponenti the uidCapitoloConComponenti to set
	 */
	public void setUidCapitoloConComponenti(Integer uidCapitoloConComponenti) {
		this.uidCapitoloConComponenti = uidCapitoloConComponenti;
	}

	/**
	 * @return the componentiCapitolo
	 */
	public List<ElementoComponenteVariazione> getComponentiCapitolo() {
		return componentiCapitolo;
	}

	/**
	 * @param componentiCapitolo the componentiCapitolo to set
	 */
	public void setComponentiCapitolo(List<ElementoComponenteVariazione> componentiCapitolo) {
		this.componentiCapitolo = componentiCapitolo;
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
	 * @return the dataInizioValiditaStatoVariazione
	 */
	public Date getDataInizioValiditaStatoVariazione() {
		return dataInizioValiditaStatoVariazione;
	}
	
	/**
	 * Gets la data in cui la variazione &egrave; diventata definitiva.
	 *
	 * @return the data inizio definizione variazione
	 */
	public String getDataDefinizioneVariazione(){
		return StatoOperativoVariazioneBilancio.DEFINITIVA.equals(statoVariazione)? FormatUtils.formatDate(getDataInizioValiditaStatoVariazione()) : "";
	}
	
	/**
	 * Crea una Request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @param response la response da cui ottenere l'atto amministrativo
	 *
	 * @return la Request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento(RicercaDettaglioAnagraficaVariazioneBilancioResponse response) {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		request.setEnte(getEnte());
		
		AttoAmministrativo a = response.getVariazioneImportoCapitolo().getAttoAmministrativo();
		if(a != null){
			RicercaAtti utility = new RicercaAtti();
			utility.setUid(a.getUid());
			utility.setAnnoAtto(a.getAnno());
			utility.setNumeroAtto(a.getNumero());
			request.setRicercaAtti(utility);
		}
		return request;
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
	 */
	public void injettaVariabiliProcesso(AzioneRichiesta azioneRichiesta) {
		VariabileProcesso numeroVariazioneVP = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_NUMERO_VARIAZIONE);
		String uidVariazioneStringa = ((String)numeroVariazioneVP.getValore()).split("\\|")[0];
		uidVariazione = Integer.valueOf(uidVariazioneStringa);
		idAttivita = azioneRichiesta.getIdAttivita();
		
		VariabileProcesso variabileProcessoDaInviareInGiunta = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_INVIO_GIUNTA);
		VariabileProcesso variabileProcessoDaInviareInConsiglio = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_INVIO_CONSIGLIO);
		this.daInviareInGiunta = (Boolean)variabileProcessoDaInviareInGiunta.getValore();
		this.daInviareInGiunta = (Boolean)variabileProcessoDaInviareInConsiglio.getValore();
	}
	
	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di {@link RicercaVariazioneBilancioResponse} e dalla sessione.
	 * 
	 * @param response       la Response del servizio
	 * @param sessionHandler l'handler per la sessione
	 */
	public void impostaDatiDaResponseESessione(RicercaDettaglioAnagraficaVariazioneBilancioResponse response, SessionHandler sessionHandler) {
		VariazioneImportoCapitolo variazione = response.getVariazioneImportoCapitolo();
		
		/* Uso per definisci variazione */
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONI, variazione);
		
		numeroVariazione = variazione.getNumero();
		descrizioneVariazione = variazione.getDescrizione();
		tipoVariazione = variazione.getTipoVariazione();
		applicazioneVariazione = variazione.getApplicazioneVariazione().getDescrizione();
		applicazione = variazione.getApplicazioneVariazione();
		statoVariazione = variazione.getStatoOperativoVariazioneDiBilancio();
		noteVariazione = variazione.getNote();
		
		//SIAC-6884
		variazioneDecentrata = variazione.isDecentrata();
		if(variazioneDecentrata) {
			dataAperturaProposta = variazione.getDataAperturaProposta();
			dataChiusuraProposta = variazione.getDataChiusuraProposta();
			direzioneProponente = variazione.getDirezioneProponente();
		}
		
		AttoAmministrativo atto = variazione.getAttoAmministrativo();
		if(atto!=null){
			annoProvvedimento = atto.getAnno();
			numeroProvvedimento = atto.getNumero();
			oggettoProvvedimento = atto.getOggetto();
			tipoProvvedimento = atto.getTipoAtto().getCodice() + " - " + atto.getTipoAtto().getDescrizione();
			StrutturaAmministrativoContabile strutturaAmministrativoContabile = atto.getStrutturaAmmContabile();
			strutturaProvvedimento = strutturaAmministrativoContabile != null
					? strutturaAmministrativoContabile.getCodice() + " - " + strutturaAmministrativoContabile.getDescrizione()
					: "";
		}
		
		//SIAC-4737
		AttoAmministrativo attoVariazioneBilancio = variazione.getAttoAmministrativoVariazioneBilancio();
		if(attoVariazioneBilancio!=null && attoVariazioneBilancio.getUid() !=0){
			annoProvvedimentoVariazioneDiBilancio = Integer.toString(attoVariazioneBilancio.getAnno());
			numeroProvvedimentoVariazioneDiBilancio = Integer.toString(attoVariazioneBilancio.getNumero());
			oggettoProvvedimentoVariazioneDiBilancio= attoVariazioneBilancio.getOggetto();
			tipoProvvedimentoVariazioneDiBilancio = attoVariazioneBilancio.getTipoAtto().getCodice() + " - " + attoVariazioneBilancio.getTipoAtto().getDescrizione();
			
			StrutturaAmministrativoContabile strutturaAmministrativoContabileVariazioneDiBilancio = attoVariazioneBilancio.getStrutturaAmmContabile();
			strutturaProvvedimentoVariazioneDiBilancio = strutturaAmministrativoContabileVariazioneDiBilancio!= null
					? strutturaAmministrativoContabileVariazioneDiBilancio.getCodice() + " - " + strutturaAmministrativoContabileVariazioneDiBilancio.getDescrizione() 
					: "";
		}
		
		dataInizioValiditaStatoVariazione = variazione.getDataInizioValiditaStato();
		
	}



	/**
	 * Creo la request per ottenere i capitoli collegati alla variazione
	 * @return la request
	 */
	public RicercaDettaglioAnagraficaVariazioneBilancio creaRequestRicercaDettaglioAnagraficaVariazioneBilancio() {
		
		RicercaDettaglioAnagraficaVariazioneBilancio request = creaRequest(RicercaDettaglioAnagraficaVariazioneBilancio.class);
		request.setUidVariazione(uidVariazione);		
		return request;
	}






	/**
	 * Creo la request per la ricerca sintetica dei capitoli collegati alla variazione
	 * @return la request
	 */
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazione creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione() {
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = creaRequest(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class);
		request.setUidVariazione(uidVariazione);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}



	/**
	 * Creo la request per la definizione della variazione
	 * @return la request
	 */
	public DefinisceAnagraficaVariazioneBilancio creaRequestDefinisceAnagraficaVariazioneBilancio() {
		DefinisceAnagraficaVariazioneBilancio request = creaRequest(DefinisceAnagraficaVariazioneBilancio.class);			
		
		request.setIdAttivita(idAttivita);
		request.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setListaVariabiliProcesso(getAzioneRichiesta().getVariabiliProcesso());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio {@link VariazioneBilancioExcelReport}.
	 * @return la request creata
	 */
	public VariazioneBilancioExcelReport creaRequestStampaExcelVariazioneDiBilancio() {
		VariazioneBilancioExcelReport req = creaRequest(VariazioneBilancioExcelReport.class);
		
		req.setEnte(getEnte());
		req.setXlsx(getIsXlsx());
		
		VariazioneImportoCapitolo vic = new VariazioneImportoCapitolo();
		vic.setUid(getUidVariazione());
		req.setVariazioneImportoCapitolo(vic);
		
		return req;
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
		req.setUidVariazione(uidVariazione);
		return req;
	}
	
	//SIAC-6881
	/**
	 * Crea request ricerca dettaglio variazione componente importo capitolo.
	 *
	 * @return the ricerca dettaglio variazione componente importo capitolo
	 */
	public RicercaDettaglioVariazioneComponenteImportoCapitolo creaRequestRicercaDettaglioVariazioneComponenteImportoCapitolo() {
		RicercaDettaglioVariazioneComponenteImportoCapitolo req = creaRequest(RicercaDettaglioVariazioneComponenteImportoCapitolo.class);
		req.setDettaglioVariazioneImportoCapitolo(new DettaglioVariazioneImportoCapitolo());
		req.setModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag, DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo, ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail.MacrotipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail.SottotipoComponenteImportiCapitolo);
		CapitoloUscitaPrevisione cap = new CapitoloUscitaPrevisione();
		cap.setUid(getUidCapitoloConComponenti());
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(cap);
		VariazioneImportoCapitolo var = new VariazioneImportoCapitolo();
		var.setUid(getUidVariazione());
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(var);
		return req;
	}
	
	private Date dataApertura = null;
	public Date getDataApertura() {
			return dataApertura;
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
	
}
