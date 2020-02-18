/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;


/**
 * Classe base di model per la la richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2015
 */
public class BaseRichiestaEconomaleModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6662047372441462366L;
	
	private CassaEconomale cassaEconomale;
	private TipoRichiestaEconomale tipoRichiestaEconomale;
	private RichiestaEconomale richiestaEconomale;
	
	private ClassificatoreGenerico classificatoreGenerico1;
	private ClassificatoreGenerico classificatoreGenerico2;
	private ClassificatoreGenerico classificatoreGenerico3;
	
	private String labelClassificatoreGenerico1;
	private String labelClassificatoreGenerico2;
	private String labelClassificatoreGenerico3;
	
	private List<ClassificatoreGenerico> listaClassificatoreGenerico1 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico2 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico3 = new ArrayList<ClassificatoreGenerico>();
	
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<ModalitaPagamentoDipendente> listaModalitaPagamentoDipendente = new ArrayList<ModalitaPagamentoDipendente>();
	
	private ClassificatoreGenerico classificatoreGenerico1Copia;
	private ClassificatoreGenerico classificatoreGenerico2Copia;
	private ClassificatoreGenerico classificatoreGenerico3Copia;
	
	//aggiunti il 31_03_2015 ahmad sono stati aggiunti su questa classe di model per evitare di replicare il codice*N per tutte le stampe 
	//stessa cosa la request di stampa ricevuta
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	
	/**
	 * @return the cassaEconomale
	 */
	public CassaEconomale getCassaEconomale() {
		return cassaEconomale;
	}

	/**
	 * @param cassaEconomale the cassaEconomale to set
	 */
	public void setCassaEconomale(CassaEconomale cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}

	/**
	 * @return the tipoRichiestaEconomale
	 */
	public TipoRichiestaEconomale getTipoRichiestaEconomale() {
		return tipoRichiestaEconomale;
	}

	/**
	 * @param tipoRichiestaEconomale the tipoRichiestaEconomale to set
	 */
	public void setTipoRichiestaEconomale(TipoRichiestaEconomale tipoRichiestaEconomale) {
		this.tipoRichiestaEconomale = tipoRichiestaEconomale;
	}

	/**
	 * @return the richiestaEconomale
	 */
	public RichiestaEconomale getRichiestaEconomale() {
		return richiestaEconomale;
	}

	/**
	 * @param richiestaEconomale the richiestaEconomale to set
	 */
	public void setRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		this.richiestaEconomale = richiestaEconomale;
	}

	/**
	 * @return the classificatoreGenerico1
	 */
	public ClassificatoreGenerico getClassificatoreGenerico1() {
		return classificatoreGenerico1;
	}

	/**
	 * @param classificatoreGenerico1 the classificatoreGenerico1 to set
	 */
	public void setClassificatoreGenerico1(ClassificatoreGenerico classificatoreGenerico1) {
		this.classificatoreGenerico1 = classificatoreGenerico1;
	}

	/**
	 * @return the classificatoreGenerico2
	 */
	public ClassificatoreGenerico getClassificatoreGenerico2() {
		return classificatoreGenerico2;
	}

	/**
	 * @param classificatoreGenerico2 the classificatoreGenerico2 to set
	 */
	public void setClassificatoreGenerico2(ClassificatoreGenerico classificatoreGenerico2) {
		this.classificatoreGenerico2 = classificatoreGenerico2;
	}

	/**
	 * @return the classificatoreGenerico3
	 */
	public ClassificatoreGenerico getClassificatoreGenerico3() {
		return classificatoreGenerico3;
	}

	/**
	 * @param classificatoreGenerico3 the classificatoreGenerico3 to set
	 */
	public void setClassificatoreGenerico3(ClassificatoreGenerico classificatoreGenerico3) {
		this.classificatoreGenerico3 = classificatoreGenerico3;
	}

	/**
	 * @return the labelClassificatoreGenerico1
	 */
	public String getLabelClassificatoreGenerico1() {
		return labelClassificatoreGenerico1;
	}

	/**
	 * @param labelClassificatoreGenerico1 the labelClassificatoreGenerico1 to set
	 */
	public void setLabelClassificatoreGenerico1(String labelClassificatoreGenerico1) {
		this.labelClassificatoreGenerico1 = labelClassificatoreGenerico1;
	}

	/**
	 * @return the labelClassificatoreGenerico2
	 */
	public String getLabelClassificatoreGenerico2() {
		return labelClassificatoreGenerico2;
	}

	/**
	 * @param labelClassificatoreGenerico2 the labelClassificatoreGenerico2 to set
	 */
	public void setLabelClassificatoreGenerico2(String labelClassificatoreGenerico2) {
		this.labelClassificatoreGenerico2 = labelClassificatoreGenerico2;
	}

	/**
	 * @return the labelClassificatoreGenerico3
	 */
	public String getLabelClassificatoreGenerico3() {
		return labelClassificatoreGenerico3;
	}

	/**
	 * @param labelClassificatoreGenerico3 the labelClassificatoreGenerico3 to set
	 */
	public void setLabelClassificatoreGenerico3(String labelClassificatoreGenerico3) {
		this.labelClassificatoreGenerico3 = labelClassificatoreGenerico3;
	}

	/**
	 * @return the listaClassificatoreGenerico1
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico1() {
		return listaClassificatoreGenerico1;
	}

	/**
	 * @param listaClassificatoreGenerico1 the listaClassificatoreGenerico1 to set
	 */
	public void setListaClassificatoreGenerico1(List<ClassificatoreGenerico> listaClassificatoreGenerico1) {
		this.listaClassificatoreGenerico1 = listaClassificatoreGenerico1 != null ? listaClassificatoreGenerico1 : new ArrayList<ClassificatoreGenerico>();
	}

	/**
	 * @return the listaClassificatoreGenerico2
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico2() {
		return listaClassificatoreGenerico2;
	}

	/**
	 * @param listaClassificatoreGenerico2 the listaClassificatoreGenerico2 to set
	 */
	public void setListaClassificatoreGenerico2(List<ClassificatoreGenerico> listaClassificatoreGenerico2) {
		this.listaClassificatoreGenerico2 = listaClassificatoreGenerico2 != null ? listaClassificatoreGenerico2 : new ArrayList<ClassificatoreGenerico>();
	}

	/**
	 * @return the listaClassificatoreGenerico3
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico3() {
		return listaClassificatoreGenerico3;
	}

	/**
	 * @param listaClassificatoreGenerico3 the listaClassificatoreGenerico3 to set
	 */
	public void setListaClassificatoreGenerico3(List<ClassificatoreGenerico> listaClassificatoreGenerico3) {
		this.listaClassificatoreGenerico3 = listaClassificatoreGenerico3 != null ? listaClassificatoreGenerico3 : new ArrayList<ClassificatoreGenerico>();
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto;
	}

	/**
	 * @return the listaModalitaPagamentoDipendente
	 */
	public List<ModalitaPagamentoDipendente> getListaModalitaPagamentoDipendente() {
		return listaModalitaPagamentoDipendente;
	}

	/**
	 * @param listaModalitaPagamentoDipendente the listaModalitaPagamentoDipendente to set
	 */
	public void setListaModalitaPagamentoDipendente(List<ModalitaPagamentoDipendente> listaModalitaPagamentoDipendente) {
		this.listaModalitaPagamentoDipendente = listaModalitaPagamentoDipendente != null ? listaModalitaPagamentoDipendente : new ArrayList<ModalitaPagamentoDipendente>();
	}

	/**
	 * @return the numeroClassificatoriGenerici
	 */
	public int getNumeroClassificatoriGenerici() {
		return 3;
	}
	
	/**
	 * @return the codiceAmbito
	 */
	public String getCodiceAmbito() {
		return BilConstants.AMBITO_CEC.getConstant();
	}
	
	/**
	 * @return the codiceAmbito
	 */
	public String getCodiceAmbitoFIN() {
		return BilConstants.AMBITO_FIN.getConstant();
	}
	
	/**
	 * Controlla se l'ente gestisca o meno l'integrazione con HR.
	 * 
	 * @return <code>true</code> se l'ente gestisce l'integrazione con HR; <code>false</code> altrimenti
	 */
	public boolean isGestioneMissioneEsterna() {
		return getEnte() != null && getEnte().getGestioneLivelli() != null
				&& BilConstants.CARICA_MISSIONE_DA_ESTERNO.getConstant().equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.CARICA_MISSIONE_DA_ESTERNO));
	}
	
	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaClassificatoriGenericiCassaEconomale}.
	 * 
	 * @return la request creata
	 */
	public RicercaClassificatoriGenericiCassaEconomale creaRequestRicercaClassificatoriGenericiCassaEconomale() {
		return creaRequest(RicercaClassificatoriGenericiCassaEconomale.class);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @param soggetto il soggetto per cui effettuare la richiesta
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto soggetto) {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		
		parametroSoggettoK.setMatricola(soggetto.getMatricola());
		// Se non dovessi avere la matricola (eg: dopo aver ricercato il dettaglio della richiesta), utilizzo in codice soggetto
		if(soggetto.getMatricola() == null || soggetto.getMatricola().isEmpty()) {
			parametroSoggettoK.setCodice(soggetto.getCodiceSoggetto());
		}
		request.setParametroSoggettoK(parametroSoggettoK);
		// Ambito CEC
		request.setCodificaAmbito(getCodiceAmbito());
		// Lotto P
		request.setSorgenteDatiSoggetto(isGestioneHR() ? SorgenteDatiSoggetto.HR : SorgenteDatiSoggetto.SIAC);
		
		return request;
	}
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}, a partire dalla Richiesta economale (per integrazione HR).
	 * 
	 * @param ricEcon la richiesta economale per cui effettuare la richiesta
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(RichiestaEconomale ricEcon) {
		if(ricEcon.getSoggetto() != null) {
			return creaRequestRicercaSoggettoPerChiave(ricEcon.getSoggetto());
		}
		
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setMatricola(ricEcon.getMatricola());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		// Ambito CEC
		request.setCodificaAmbito(getCodiceAmbito());
		// Lotto P
		request.setSorgenteDatiSoggetto(isGestioneHR() ? SorgenteDatiSoggetto.HR : SorgenteDatiSoggetto.SIAC);
		
		return request;
	}
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @param soggetto il soggetto per cui effettuare la richiesta
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoFatturaPerChiave(Soggetto soggetto) {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		
		parametroSoggettoK.setCodice(soggetto.getCodiceSoggetto());
		
		request.setParametroSoggettoK(parametroSoggettoK);
		// Ambito FIN è il soggetto del documento non della richiesta
		request.setCodificaAmbito(getCodiceAmbitoFIN());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		return creaRequestRicercaSoggettoPerChiave(getRichiestaEconomale().getSoggetto());
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRichiestaEconomale}.
	 * 
	 * @param ricEcon la richiesta il cui dettaglio &eacute da cercare.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRichiestaEconomale creaRequestRicercaDettaglioRichiestaEconomale(RichiestaEconomale ricEcon) {
		RicercaDettaglioRichiestaEconomale request = creaRequest(RicercaDettaglioRichiestaEconomale.class);
		ricEcon.setBilancio(getBilancio());
		request.setRichiestaEconomale(ricEcon);
		
		return request;
	}
	
	/* **** Metodi di utilita' **** */
	
	/**
	 * Aggiunge il classificatore generico alla richiesta se selezionato.
	 * 
	 * @param ricEcon     la richiesta da popolare 
	 * @param classificatoreGenerico il classificatore da impostare se selezionato
	 */
	protected void addClassificatoreIfSelected(RichiestaEconomale ricEcon, ClassificatoreGenerico classificatoreGenerico) {
		if(impostaEntitaFacoltativa(classificatoreGenerico) != null) {
			ricEcon.getClassificatoriGenerici().add(classificatoreGenerico);
		}
	}

	/**
	 * @return the classificatoreGenerico1Copia
	 */
	public ClassificatoreGenerico getClassificatoreGenerico1Copia() {
		return classificatoreGenerico1Copia;
	}

	/**
	 * @param classificatoreGenerico1Copia the classificatoreGenerico1Copia to set
	 */
	public void setClassificatoreGenerico1Copia(
			ClassificatoreGenerico classificatoreGenerico1Copia) {
		this.classificatoreGenerico1Copia = classificatoreGenerico1Copia;
	}

	/**
	 * @return the classificatoreGenerico2Copia
	 */
	public ClassificatoreGenerico getClassificatoreGenerico2Copia() {
		return classificatoreGenerico2Copia;
	}

	/**
	 * @param classificatoreGenerico2Copia the classificatoreGenerico2Copia to set
	 */
	public void setClassificatoreGenerico2Copia(
			ClassificatoreGenerico classificatoreGenerico2Copia) {
		this.classificatoreGenerico2Copia = classificatoreGenerico2Copia;
	}

	/**
	 * @return the classificatoreGenerico3Copia
	 */
	public ClassificatoreGenerico getClassificatoreGenerico3Copia() {
		return classificatoreGenerico3Copia;
	}

	/**
	 * @param classificatoreGenerico3Copia the classificatoreGenerico3Copia to set
	 */
	public void setClassificatoreGenerico3Copia(
			ClassificatoreGenerico classificatoreGenerico3Copia) {
		this.classificatoreGenerico3Copia = classificatoreGenerico3Copia;
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
	 * Crea una request per il servizio di {@link StampaRicevutaRichiestaEconomale}.
	 * 
	 * @param richiesta la richiesta per cui creare la request
	 * @return la request creata
	 */
	public StampaRicevutaRichiestaEconomale creaRequestStampaRicevutaRichiestaEconomale(RichiestaEconomale richiesta) {
		StampaRicevutaRichiestaEconomale request = creaRequest(StampaRicevutaRichiestaEconomale.class);
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		request.setRichiestaEconomale(richiesta);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link StampaRicevutaRendicontoRichiestaEconomale}.
	 * 
	 * @param rendicontoRichiesta il rendiconto per cui creare la request
	 * @return la request creata
	 */
	public StampaRicevutaRendicontoRichiestaEconomale creaRequestStampaRicevutaRendicontoRichiestaEconomale(RendicontoRichiesta rendicontoRichiesta) {
		StampaRicevutaRendicontoRichiestaEconomale request = creaRequest(StampaRicevutaRendicontoRichiestaEconomale.class);
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		request.setRendicontoRichiesta(rendicontoRichiesta);
		
		return request;
	}
	/**
	 * Crea una request per il servizio di {@link CalcolaDisponibilitaCassaEconomale}.
	 * 
	 * @param cassa la cassaEconomale per cui effettuare la richiesta
	 * @return la request creata
	 */
	public CalcolaDisponibilitaCassaEconomale creaRequestCalcolaDisponibilitaCassaEconomale(CassaEconomale cassa) {
		CalcolaDisponibilitaCassaEconomale request = creaRequest(CalcolaDisponibilitaCassaEconomale.class);
		
		// Copio solo l'uid
		CassaEconomale ce = new CassaEconomale();
		ce.setUid(cassa.getUid());
		request.setBilancio(getBilancio());
		request.setCassaEconomale(ce);
		
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCassaEconomaleEnum.class));
		return request;
	}
}
