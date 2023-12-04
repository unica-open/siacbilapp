/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampare;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassa;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe di model per le stampa dell rendiconto CEC.
 * 
 * @author Paggio Simona,Nazha Ahmad
 * @version 1.0.0 - 10/02/2015
 * @version 1.0.1 - 02/04/2015
 */
public class StampaCECRendicontoModel extends GenericStampaCECModel {

	/** per serializzazione */
	private static final long serialVersionUID = -7225922556524108143L;
	private StampaRendiconto stampaRendiconto=new StampaRendiconto();
	private Integer numeroRendiconto;

	private Date periodoDaRendicontareDataInizio;
	private Date periodoDaRendicontareDataFine;
	private Integer uidCassaEconomale;
	
	// SIAC-4799
	private Integer startPage;
	private Long numeroTotaleMovimenti;
	private BigDecimal importoTotaleMovimenti;
	private Soggetto soggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private String causale;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private Boolean anticipiSpesaDaInserire;
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	//SIAC-5890
	private Integer numeroOperazioniDiCassa =0;
	
	//SIAC-6450
	private BigDecimal importoTotaleSenzaMovimentiAnnullati;
	
	/** Costruttore vuoto di default */
	public StampaCECRendicontoModel() {
		setTitolo("Stampa Rendiconto");
	}

	
	
	/**
	 * @return the numeroOperazioniDiCassa
	 */
	public Integer getNumeroOperazioniDiCassa() {
		return numeroOperazioniDiCassa;
	}



	/**
	 * @param numeroOperazioniDiCassa the numeroOperazioniDiCassa to set
	 */
	public void setNumeroOperazioniDiCassa(Integer numeroOperazioniDiCassa) {
		this.numeroOperazioniDiCassa = numeroOperazioniDiCassa;
	}


	/**
	 * @return the importoTotaleSenzaMovimentiAnnullati
	 */
	public BigDecimal getImportoTotaleSenzaMovimentiAnnullati() {
		return importoTotaleSenzaMovimentiAnnullati;
	}



	/**
	 * @param importoTotaleSenzaMovimentiAnnullati the importoTotaleSenzaMovimentiAnnullati to set
	 */
	public void setImportoTotaleSenzaMovimentiAnnullati(BigDecimal importoTotaleSenzaMovimentiAnnullati) {
		this.importoTotaleSenzaMovimentiAnnullati = importoTotaleSenzaMovimentiAnnullati;
	}



	/**
	 * @return the stampaRendiconto
	 */
	public StampaRendiconto getStampaRendiconto() {
		return stampaRendiconto;
	}

	/**
	 * @param stampaRendiconto the stampaRendiconto to set
	 */
	public void setStampaRendiconto(StampaRendiconto stampaRendiconto) {
		this.stampaRendiconto = stampaRendiconto;
	}

	/**
	 * @return the numeroRendiconto
	 */
	public Integer getNumeroRendiconto() {
		return numeroRendiconto;
	}

	/**
	 * @param numeroRendiconto the numeroRendiconto to set
	 */
	public void setNumeroRendiconto(Integer numeroRendiconto) {
		this.numeroRendiconto = numeroRendiconto;
	}

	/**
	 * @return the periodoDaRendicontareDataInizio
	 */
	public Date getPeriodoDaRendicontareDataInizio() {
		return periodoDaRendicontareDataInizio == null ? null : new Date(periodoDaRendicontareDataInizio.getTime());
	}

	/**
	 * @param periodoDaRendicontareDataInizio the periodoDaRendicontareDataInizio to set
	 */
	public void setPeriodoDaRendicontareDataInizio(Date periodoDaRendicontareDataInizio) {
		this.periodoDaRendicontareDataInizio = periodoDaRendicontareDataInizio == null ? null : new Date(periodoDaRendicontareDataInizio.getTime());
	}

	/**
	 * @return the periodoDaRendicontareDataFine
	 */
	public Date getPeriodoDaRendicontareDataFine() {
		return periodoDaRendicontareDataFine == null ? null : new Date(periodoDaRendicontareDataFine.getTime());
	}

	/**
	 * @param periodoDaRendicontareDataFine the periodoDaRendicontareDataFine to set
	 */
	public void setPeriodoDaRendicontareDataFine(Date periodoDaRendicontareDataFine) {
		this.periodoDaRendicontareDataFine = periodoDaRendicontareDataFine == null ? null : new Date(periodoDaRendicontareDataFine.getTime());
	}

	/**
	 * @return the uidCassaEconomale
	 */
	public Integer getUidCassaEconomale() {
		return uidCassaEconomale;
	}

	/**
	 * @param uidCassaEconomale the uidCassaEconomale to set
	 */
	public void setUidCassaEconomale(Integer uidCassaEconomale) {
		this.uidCassaEconomale = uidCassaEconomale;
	}

	/**
	 * @return the startPage
	 */
	public Integer getStartPage() {
		return startPage;
	}

	/**
	 * @param startPage the startPage to set
	 */
	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	/**
	 * @return the numeroTotaleMovimenti
	 */
	public Long getNumeroTotaleMovimenti() {
		return numeroTotaleMovimenti;
	}

	/**
	 * @param numeroTotaleMovimenti the numeroTotaleMovimenti to set
	 */
	public void setNumeroTotaleMovimenti(Long numeroTotaleMovimenti) {
		this.numeroTotaleMovimenti = numeroTotaleMovimenti;
	}

	/**
	 * @return the importoTotaleMovimenti
	 */
	public BigDecimal getImportoTotaleMovimenti() {
		return importoTotaleMovimenti;
	}

	/**
	 * @param importoTotaleMovimenti the importoTotaleMovimenti to set
	 */
	public void setImportoTotaleMovimenti(BigDecimal importoTotaleMovimenti) {
		this.importoTotaleMovimenti = importoTotaleMovimenti;
	}
	
	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the causale
	 */
	public String getCausale() {
		return causale;
	}

	/**
	 * @param causale the causale to set
	 */
	public void setCausale(String causale) {
		this.causale = causale;
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
	 * @return the anticipiSpesaDaInserire
	 */
	public Boolean getAnticipiSpesaDaInserire() {
		return anticipiSpesaDaInserire;
	}

	/**
	 * @param anticipiSpesaDaInserire the anticipiSpesaDaInserire to set
	 */
	public void setAnticipiSpesaDaInserire(Boolean anticipiSpesaDaInserire) {
		this.anticipiSpesaDaInserire = anticipiSpesaDaInserire;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}

	/**
	 * @return the stampaDefinitiva
	 */
	public boolean isStampaDefinitiva() {
		return TipoStampa.DEFINITIVA.equals(getTipoStampa());
	}
	
	/**
	 * @return the dati soggetto
	 */
	public String getDatiSoggetto() {
		if(getSoggetto() == null) {
			return "";
		}
		return CollectionUtil.join(" - ", soggetto.getCodiceSoggetto(), soggetto.getDenominazione(), soggetto.getCodiceFiscale());
	}
	
	/**
	 * @return the modalitaPagamentoCessione
	 */
	public boolean isModalitaPagamentoCessione() {
		return getModalitaPagamentoSoggetto() != null
				&& getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2() != null
				&& getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid() != 0;
	}

	/**
	 * Gets the descrizione importo totale.
	 *
	 * @return the descrizione importo totale
	 */
	public String getDescrizioneImportoTotale() {
		//SIAC-6450
		return new StringBuilder()
				.append(FormatUtils.formatCurrency(getImportoTotaleMovimenti()))
				.append(" ( senza annullati: ")
				.append(FormatUtils.formatCurrency(getImportoTotaleSenzaMovimentiAnnullati()))
				.append(" )")
				.toString();
	}
	
	// REQUEST

	/**
	 * crea la request findDatiUltimaStampaDefinitivaGiornaleCassa
	 * @return la request creata
	 */
	public RicercaUltimoRendicontoCassaStampato creaRequestRicercaUltimoRendicontoCassaStampato() {
		RicercaUltimoRendicontoCassaStampato request = creaRequest(RicercaUltimoRendicontoCassaStampato.class);
		
		StampeCassaFile stampeCassaFile = new StampeCassaFile();
		stampeCassaFile.setBilancio(getBilancio());
		stampeCassaFile.setEnte(getEnte());
		stampeCassaFile.setTipoDocumento(TipoDocumento.RENDICONTO);
		stampeCassaFile.setTipoStampa(TipoStampa.DEFINITIVA);
		
		CassaEconomale cassa = new CassaEconomale();
		cassa.setUid(getUidCassaEconomale().intValue());
		stampeCassaFile.setCassaEconomale(cassa);
		
		request.setStampeCassaFile(stampeCassaFile);

		return request;
	}

	/**
	 * crea la request per stampare un giornale di cassa
	 * 
	 * @return la request
	 */
	public StampaRendicontoCassa creaRequestStampaRendicontoCassa() {
		StampaRendicontoCassa request = creaRequest(StampaRendicontoCassa.class);
		
		request.setEnte(getEnte());
		request.setBilancio(getBilancio());
		request.setTipoStampa(getTipoStampa());
		request.setPeriodoDaRendicontareDataInizio(getPeriodoDaRendicontareDataInizio());
		request.setPeriodoDaRendicontareDataFine(getPeriodoDaRendicontareDataFine());
		request.setDataRendiconto(getStampaRendiconto().getDataRendiconto());
		request.setCassaEconomale(getCassaEconomale());
		
		// SIAC-4799
		request.setSoggetto(getSoggetto());
		request.setModalitaPagamentoSoggetto(getModalitaPagamentoSoggetto());
		request.setCausale(getCausale());
		request.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		request.setAnticipiSpesaDaInserire(getAnticipiSpesaDaInserire());
	
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link VerificaStampaRendicontoCassa}.
	 * @return la request creata
	 */
	public VerificaStampaRendicontoCassa creaRequestVerificaStampaRendicontoCassa() {
		VerificaStampaRendicontoCassa req = creaRequest(VerificaStampaRendicontoCassa.class);
		
		req.setBilancio(getBilancio());
		req.setCassaEconomale(getCassaEconomale());
		req.setPeriodoDaRendicontareDataFine(getPeriodoDaRendicontareDataFine());
		req.setPeriodoDaRendicontareDataInizio(getPeriodoDaRendicontareDataInizio());
		req.setTipoStampa(getTipoStampa());
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRendicontoCassaDaStampare}.
	 * @return la request creata
	 */
	public RicercaSinteticaRendicontoCassaDaStampare creaRequestRicercaSinteticaRendicontoCassaDaStampare() {
		RicercaSinteticaRendicontoCassaDaStampare req = creaRequest(RicercaSinteticaRendicontoCassaDaStampare.class);
		
		req.setBilancio(getBilancio());
		req.setCassaEconomale(getCassaEconomale());
		req.setPeriodoDaRendicontareDataFine(getPeriodoDaRendicontareDataFine());
		req.setPeriodoDaRendicontareDataInizio(getPeriodoDaRendicontareDataInizio());
		req.setParametriPaginazione(creaParametriPaginazione());
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave req = creaRequest(RicercaSoggettoPerChiave.class);
		
		req.setEnte(getEnte());
		req.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		req.setParametroSoggettoK(parametroSoggettoK);
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		parametroSoggettoK.setIncludeModif(false);
		
		return req;
	}

	/**
	 * imposta i dati neccessari all'interfaccia di stampa corrsipondente
	 */
	public void impostaDatiNelModel() {
		setTipoStampa(TipoStampa.BOZZA);
		if (getStampaRendiconto() != null && getStampaRendiconto().getNumeroRendiconto() != null) {
			setNumeroRendiconto(getStampaRendiconto().getNumeroRendiconto() + 1);
		} else {
			getStampaRendiconto().setNumeroRendiconto(0);
			setNumeroRendiconto(getStampaRendiconto().getNumeroRendiconto() + 1);
		}
		getStampaRendiconto().setDataRendiconto(new Date());
	}

	/**
	 * inizializza l'oggetto stampa rendiconto
	 */
	public void impostaStampaRendiconto() {
		// imposto l'oggetto stampaG per evitare i problemi nella jsp
		StampaRendiconto sr = new StampaRendiconto();

		setStampaRendiconto(sr);
	}

}
