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

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step2.DefinisciVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazioneFactory;
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
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;

/**
 * Classe di model per la consultazione delle variazioni Importi. 
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 * @author Elisa Chiari
 * @version 1.1.0 28/02/2017
 *
 */

public class ConsultaVariazioneImportiModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4256856520682050552L;
	
	private Integer numeroVariazione;
	private Integer uidVariazione;
	private String applicazioneVariazione;
	private String descrizioneVariazione;
	private TipoVariazione tipoVariazione;
	private Integer annoCompetenza;
	private StatoOperativoVariazioneDiBilancio statoVariazione;
	private String noteVariazione;
	
	//SIAC-6884
	private Date dataAperturaProposta;
	private Date dataChiusuraProposta;
	private StrutturaAmministrativoContabile direzioneProponente;

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getDirezioneProponente() {
		return direzioneProponente;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setDirezioneProponente(StrutturaAmministrativoContabile direzioneProponente) {
		this.direzioneProponente = direzioneProponente;
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

	private Integer uidProvvedimento;
	private String tipoProvvedimento;
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private String strutturaProvvedimento;
	private String oggettoProvvedimento;

	private Integer idxCapitoloSelezionato;
	private List<ElementoCapitoloVariazione> listaCapitoli = new ArrayList<ElementoCapitoloVariazione>();
	private List<ElementoCapitoloVariazione> listaUEB = new ArrayList<ElementoCapitoloVariazione>();
	
	//SIAC-4528
	private BigDecimal totaleStanziamentiEntrata;
	private BigDecimal totaleStanziamentiCassaEntrata;
	private BigDecimal totaleStanziamentiResiduiEntrata;
	private BigDecimal totaleStanziamentiSpesa;
	private BigDecimal totaleStanziamentiCassaSpesa;
	private BigDecimal totaleStanziamentiResiduiSpesa;
	
	//SIAC-4737
	private Integer uidProvvedimentoVariazioneDiBilancio;
	private String tipoProvvedimentoVariazioneDiBilancio;
	private Integer annoProvvedimentoVariazioneDiBilancio;
	private Integer numeroProvvedimentoVariazioneDiBilancio;
	private String strutturaProvvedimentoVariazioneDiBilancio;
	private String oggettoProvvedimentoVariazioneDiBilancio;
	
	//SIAC-4999
	private Date dataInizioValiditaStatoVariazione;
	
	//SIAC-5016
	private ApplicazioneVariazione applicazione;
	private CapitoloEntrataPrevisione capitoloEntrataPrevisioneNellaVariazione;
	private CapitoloUscitaPrevisione capitoloUscitaPrevisioneNellaVariazione;
	private CapitoloEntrataGestione capitoloEntrataGestioneNellaVariazione;
	private CapitoloUscitaGestione capitoloUscitaGestioneNellaVariazione;
	private ElementoCapitoloVariazione elementoCapitoloVariazioneTrovatoNellaVariazione;

	// SIAC-5016
	private Boolean isXlsx;
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	
	//SIAC-6177
	private ElementoStatoOperativoVariazione elementoStatoOperativoVariazione;
	//SIAC-6881 e SIAC-6883
	private Integer uidCapitoloConComponenti;
	List<ElementoComponenteVariazione> componentiCapitolo = new ArrayList<ElementoComponenteVariazione>();
	private BigDecimal totaleStanziamentiEntrata1;
	private BigDecimal totaleStanziamentiEntrata2;
	private BigDecimal totaleStanziamentiSpesa1;
	private BigDecimal totaleStanziamentiSpesa2;

	//SIAC 6884
	
	private boolean variazioneDecentrata;

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
	public ConsultaVariazioneImportiModel() {
		super();
		setTitolo(" Consultazione Variazioni");
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
	public StatoOperativoVariazioneDiBilancio getStatoVariazione() {
		return statoVariazione;
	}

	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(
			StatoOperativoVariazioneDiBilancio statoVariazione) {
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
	 * @return totaleStanziamentiEntrata
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
	 * Gets the uid provvedimento variazione di bilancio.
	 *
	 * @return the uid provvedimento variazione di bilancio
	 */
	public Integer getUidProvvedimentoVariazioneDiBilancio() {
		return uidProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Gets the tipo provvedimento variazione di bilancio.
	 *
	 * @return the tipo provvedimento variazione di bilancio
	 */
	public String getTipoProvvedimentoVariazioneDiBilancio() {
		return tipoProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Gets the anno provvedimento variazione di bilancio.
	 *
	 * @return the anno provvedimento variazione di bilancio
	 */
	public Integer getAnnoProvvedimentoVariazioneDiBilancio() {
		return annoProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Gets the numero provvedimento variazione di bilancio.
	 *
	 * @return the numero provvedimento variazione di bilancio
	 */
	public Integer getNumeroProvvedimentoVariazioneDiBilancio() {
		return numeroProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Gets the struttura provvedimento variazione di bilancio.
	 *
	 * @return the struttura provvedimento variazione di bilancio
	 */
	public String getStrutturaProvvedimentoVariazioneDiBilancio() {
		return strutturaProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Gets the oggetto provvedimento variazione di bilancio.
	 *
	 * @return the oggetto provvedimento variazione di bilancio
	 */
	public String getOggettoProvvedimentoVariazioneDiBilancio() {
		return oggettoProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Sets the uid provvedimento variazione di bilancio.
	 *
	 * @param uidProvvedimentoVariazioneDiBilancio the new uid provvedimento variazione di bilancio
	 */
	public void setUidProvvedimentoVariazioneDiBilancio(Integer uidProvvedimentoVariazioneDiBilancio) {
		this.uidProvvedimentoVariazioneDiBilancio = uidProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Sets the tipo provvedimento variazione di bilancio.
	 *
	 * @param tipoProvvedimentoVariazioneDiBilancio the new tipo provvedimento variazione di bilancio
	 */
	public void setTipoProvvedimentoVariazioneDiBilancio(String tipoProvvedimentoVariazioneDiBilancio) {
		this.tipoProvvedimentoVariazioneDiBilancio = tipoProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Sets the anno provvedimento variazione di bilancio.
	 *
	 * @param annoProvvedimentoVariazioneDiBilancio the new anno provvedimento variazione di bilancio
	 */
	public void setAnnoProvvedimentoVariazioneDiBilancio(Integer annoProvvedimentoVariazioneDiBilancio) {
		this.annoProvvedimentoVariazioneDiBilancio = annoProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Sets the numero provvedimento variazione di bilancio.
	 *
	 * @param numeroProvvedimentoVariazioneDiBilancio the new numero provvedimento variazione di bilancio
	 */
	public void setNumeroProvvedimentoVariazioneDiBilancio(Integer numeroProvvedimentoVariazioneDiBilancio) {
		this.numeroProvvedimentoVariazioneDiBilancio = numeroProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Sets the struttura provvedimento variazione di bilancio.
	 *
	 * @param strutturaProvvedimentoVariazioneDiBilancio the new struttura provvedimento variazione di bilancio
	 */
	public void setStrutturaProvvedimentoVariazioneDiBilancio(String strutturaProvvedimentoVariazioneDiBilancio) {
		this.strutturaProvvedimentoVariazioneDiBilancio = strutturaProvvedimentoVariazioneDiBilancio;
	}

	/**
	 * Sets the oggetto provvedimento variazione di bilancio.
	 *
	 * @param oggettoProvvedimentoVariazioneDiBilancio the new oggetto provvedimento variazione di bilancio
	 */
	public void setOggettoProvvedimentoVariazioneDiBilancio(String oggettoProvvedimentoVariazioneDiBilancio) {
		this.oggettoProvvedimentoVariazioneDiBilancio = oggettoProvvedimentoVariazioneDiBilancio;
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
	 * @return the dataInizioValiditaStatoVariazione
	 */
	public Date getDataInizioValiditaStatoVariazione() {
		return dataInizioValiditaStatoVariazione;
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
	 * Sets the capitolo uscita gestione nella variazione.
	 *
	 * @param capitoloUscitaGestioneNellaVariazione the new capitolo uscita gestione nella variazione
	 */
	public void setCapitoloUscitaGestioneNellaVariazione(CapitoloUscitaGestione capitoloUscitaGestioneNellaVariazione) {
		this.capitoloUscitaGestioneNellaVariazione = capitoloUscitaGestioneNellaVariazione;
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
	 * Gets la data in cui la variazione &egrave; diventata definitiva.
	 *
	 * @return the data inizio definizione variazione
	 */
	public String getDataDefinizioneVariazione(){
		return StatoOperativoVariazioneDiBilancio.DEFINITIVA.equals(statoVariazione)? FormatUtils.formatDate(getDataInizioValiditaStatoVariazione()) : "";
	}
	
	/** 
	 * Crea la request di ricerca dei capitoli collegati alla variazione
	 * @return la request
	 */
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazione creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione() {
		//SIAC-4528
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = creaRequest(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class);
		request.setUidVariazione(uidVariazione);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	
	/**
	 * Creo la request per la ricerca dell' anagrafica della variazione di bilancio
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioAnagraficaVariazioneBilancio creaRequestRicercaDettaglioAnagraficaVariazioneBilancio() {
		//SIAC-4528
		RicercaDettaglioAnagraficaVariazioneBilancio request = creaRequest(RicercaDettaglioAnagraficaVariazioneBilancio.class);
		request.setUidVariazione(uidVariazione);
		
		return request;
	}
	
	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di {@link RicercaVariazioneBilancioResponse} e dalla sessione.
	 * 
	 * @param response              la Response del servizio
	 */
	public void impostaDatiDaResponseESessione(RicercaDettaglioAnagraficaVariazioneBilancioResponse response) {
		VariazioneImportoCapitolo variazione = response.getVariazioneImportoCapitolo();
				
		numeroVariazione = variazione.getNumero();
		descrizioneVariazione = variazione.getDescrizione();
		tipoVariazione = variazione.getTipoVariazione();


		statoVariazione = variazione.getStatoOperativoVariazioneDiBilancio();
		elementoStatoOperativoVariazione = ElementoStatoOperativoVariazioneFactory.getInstance(getEnte().getGestioneLivelli(), statoVariazione);
		
		noteVariazione = variazione.getNote();
		
		applicazioneVariazione = variazione.getApplicazioneVariazione()!= null? variazione.getApplicazioneVariazione().getDescrizione() : "";
		applicazione= variazione.getApplicazioneVariazione();
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
			tipoProvvedimento = atto.getTipoAtto() != null ? atto.getTipoAtto().getCodice() +  " - "  +  atto.getTipoAtto().getDescrizione() : "";
			strutturaProvvedimento = atto.getStrutturaAmmContabile() != null
					? atto.getStrutturaAmmContabile().getCodice() + " - " + atto.getStrutturaAmmContabile().getDescrizione()
					: "";
		}
		
		AttoAmministrativo attoVariazioneDiBilancio = variazione.getAttoAmministrativoVariazioneBilancio();
		
		if(attoVariazioneDiBilancio!=null){
			annoProvvedimentoVariazioneDiBilancio = attoVariazioneDiBilancio.getAnno();
			numeroProvvedimentoVariazioneDiBilancio = attoVariazioneDiBilancio.getNumero();
			oggettoProvvedimentoVariazioneDiBilancio = attoVariazioneDiBilancio.getOggetto();
			tipoProvvedimentoVariazioneDiBilancio = attoVariazioneDiBilancio.getTipoAtto() != null ? attoVariazioneDiBilancio.getTipoAtto().getCodice() + " - " +  attoVariazioneDiBilancio.getTipoAtto().getDescrizione() : "";
			strutturaProvvedimentoVariazioneDiBilancio = attoVariazioneDiBilancio.getStrutturaAmmContabile() != null
					? attoVariazioneDiBilancio.getStrutturaAmmContabile().getCodice() + " - " + attoVariazioneDiBilancio.getStrutturaAmmContabile().getDescrizione()
					: "";
		}
		
		dataInizioValiditaStatoVariazione = variazione.getDataInizioValiditaStato();
		
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

	/**
	 * Crea una request per il servizio {@link StampaExcelVariazioneDiBilancio}.
	 * @return la request creata
	 */
	public StampaExcelVariazioneDiBilancio creaRequestStampaExcelVariazioneDiBilancio() {
		StampaExcelVariazioneDiBilancio req = creaRequest(StampaExcelVariazioneDiBilancio.class);
		
		req.setEnte(getEnte());
		req.setXlsx(getIsXlsx());
		
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(getUidVariazione());
		req.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		
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
		req.setModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo, ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo, DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag, TipoComponenteImportiCapitoloModelDetail.MacrotipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail.SottotipoComponenteImportiCapitolo);
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
