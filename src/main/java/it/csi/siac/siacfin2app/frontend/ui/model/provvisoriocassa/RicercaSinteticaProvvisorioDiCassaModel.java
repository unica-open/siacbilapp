/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;

/**
 * Classe di model per la ricerca sintetica del provvisorio di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/03/2016
 */
public class RicercaSinteticaProvvisorioDiCassaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -434598592093072894L;
	
	private ProvvisorioDiCassa provvisorioDiCassa;
	private Date dataEmissioneDa;
	private Date dataEmissioneA;
	
	private Date dataInizioTrasmissione;
	private Date dataFineTrasmissione;
	
	private BigDecimal importoDa;
	private BigDecimal importoA;
	// Varranno S/N
	private String daAnnullare;
	private String daRegolarizzare;
	//SIAC-6357
	private ContoTesoreria contoTesoreria;
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
			
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private Boolean flagProvvisoriPagoPA;
	
	/** Cotruttore vuoto di default */
	public RicercaSinteticaProvvisorioDiCassaModel() {
		super();
		setTitolo("Ricerca provvisorio");
	}

	/**
	 * @return the provvisorioDiCassa
	 */
	public ProvvisorioDiCassa getProvvisorioDiCassa() {
		return provvisorioDiCassa;
	}

	/**
	 * @param provvisorioDiCassa the provvisorioDiCassa to set
	 */
	public void setProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa) {
		this.provvisorioDiCassa = provvisorioDiCassa;
	}

	/**
	 * @return the dataEmissioneDa
	 */
	public Date getDataEmissioneDa() {
		return dataEmissioneDa != null ? new Date(dataEmissioneDa.getTime()) : null;
	}

	/**
	 * @param dataEmissioneDa the dataEmissioneDa to set
	 */
	public void setDataEmissioneDa(Date dataEmissioneDa) {
		this.dataEmissioneDa = dataEmissioneDa != null ? new Date(dataEmissioneDa.getTime()) : null;
	}

	/**
	 * @return the dataEmissioneA
	 */
	public Date getDataEmissioneA() {
		return dataEmissioneA != null ? new Date(dataEmissioneA.getTime()) : null;
	}

	/**
	 * @param dataEmissioneA the dataEmissioneA to set
	 */
	public void setDataEmissioneA(Date dataEmissioneA) {
		this.dataEmissioneA = dataEmissioneA != null ? new Date(dataEmissioneA.getTime()) : null;
	}
	
	
	
	/**
	 * @return the dataInizioTrasmissione
	 */
	public Date getDataInizioTrasmissione() {
		return dataInizioTrasmissione != null ? new Date(dataInizioTrasmissione.getTime()) : null;
	}

	/**
	 * @param dataInizioTrasmissione the dataInizioTrasmissione to set
	 */
	public void setDataInizioTrasmissione(Date dataInizioTrasmissione) {
		this.dataInizioTrasmissione = dataInizioTrasmissione != null ? new Date(dataInizioTrasmissione.getTime()) : null;
	}

	/**
	 * @return the dataFineTrasmissione
	 */
	public Date getDataFineTrasmissione() {
		return dataFineTrasmissione != null ? new Date(dataFineTrasmissione.getTime()) : null;
	}

	/**
	 * @param dataFineTrasmissione the dataFineTrasmissione to set
	 */
	public void setDataFineTrasmissione(Date dataFineTrasmissione) {
		//this.dataFineTrasmissione = dataFineTrasmissione;
		this.dataFineTrasmissione = dataFineTrasmissione != null ? new Date(dataFineTrasmissione.getTime()) : null;
	}
	
	/**
	 * @return the importoDa
	 */
	public BigDecimal getImportoDa() {
		return importoDa;
	}

	/**
	 * @param importoDa the importoDa to set
	 */
	public void setImportoDa(BigDecimal importoDa) {
		this.importoDa = importoDa;
	}

	/**
	 * @return the importoA
	 */
	public BigDecimal getImportoA() {
		return importoA;
	}

	/**
	 * @param importoA the importoA to set
	 */
	public void setImportoA(BigDecimal importoA) {
		this.importoA = importoA;
	}

	/**
	 * @return the daAnnullare
	 */
	public String getDaAnnullare() {
		return daAnnullare;
	}

	/**
	 * @param daAnnullare the daAnnullare to set
	 */
	public void setDaAnnullare(String daAnnullare) {
		this.daAnnullare = daAnnullare;
	}

	/**
	 * @return the daRegolarizzare
	 */
	public String getDaRegolarizzare() {
		return daRegolarizzare;
	}

	/**
	 * @param daRegolarizzare the daRegolarizzare to set
	 */
	public void setDaRegolarizzare(String daRegolarizzare) {
		this.daRegolarizzare = daRegolarizzare;
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
		this.listaContoTesoreria = listaContoTesoreria != null ? listaContoTesoreria : new ArrayList<ContoTesoreria>();
	}

	/**
	 * @return the contoTesoreria
	*/
	public ContoTesoreria getContoTesoreria() {
		return contoTesoreria;
	}
	
	/**
	 * @param contoTesoreria the contoTesoreria to set
	*/
	public void setContoTesoreria(ContoTesoreria contoTesoreria) {
		this.contoTesoreria = contoTesoreria;
	}

	
	/* **** Requests **** */

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
	 * Crea una request per il servizio di {@link RicercaProvvisoriDiCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisoriDiCassa creaRequestRicercaProvvisoriDiCassa() {
		RicercaProvvisoriDiCassa request = creaRequest(RicercaProvvisoriDiCassa.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		// Le pagine sono 1-based
		request.setNumPagina(1);
		request.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		
		if(getProvvisorioDiCassa() != null) {
			parametroRicercaProvvisorio.setTipoProvvisorio(getProvvisorioDiCassa().getTipoProvvisorioDiCassa());
			parametroRicercaProvvisorio.setNumero(getProvvisorioDiCassa().getNumero());
			parametroRicercaProvvisorio.setDescCausale(getProvvisorioDiCassa().getCausale());
			parametroRicercaProvvisorio.setSubCausale(getProvvisorioDiCassa().getSubCausale());			
			parametroRicercaProvvisorio.setContoTesoriere(getContoTesoreria().getCodice());				
			parametroRicercaProvvisorio.setDenominazioneSoggetto(getProvvisorioDiCassa().getDenominazioneSoggetto());
		}

		
		//SIAC-6731
		parametroRicercaProvvisorio.setAnno(getAnnoEsercizioInt());
		
		parametroRicercaProvvisorio.setDataInizioTrasmissione(getDataInizioTrasmissione());
		parametroRicercaProvvisorio.setDataFineTrasmissione(getDataFineTrasmissione());

		Integer uidSAC = getStrutturaAmministrativoContabile() != null && getStrutturaAmministrativoContabile().getUid() != 0? getStrutturaAmministrativoContabile().getUid() : null; 
		parametroRicercaProvvisorio.setIdStrutturaAmministrativa(uidSAC);
		
		parametroRicercaProvvisorio.setDataInizioEmissione(getDataEmissioneDa());
		parametroRicercaProvvisorio.setDataFineEmissione(getDataEmissioneA());
		
		parametroRicercaProvvisorio.setImportoDa(getImportoDa());
		parametroRicercaProvvisorio.setImportoA(getImportoA());
		
		parametroRicercaProvvisorio.setDataInizioEmissione(getDataEmissioneDa());
		parametroRicercaProvvisorio.setDataFineEmissione(getDataEmissioneA());
		
		parametroRicercaProvvisorio.setFlagAnnullato(impostaStringaDefaultNull(getDaAnnullare()));
		parametroRicercaProvvisorio.setFlagDaRegolarizzare(impostaStringaDefaultNull(getDaRegolarizzare()));
		
		request.setParametroRicercaProvvisorio(parametroRicercaProvvisorio);
		
		return request;
	}
	
	/**
	 * Imposta la stringa con default selezionato.
	 * 
	 * @param str la stringa da valutare
	 * @param defaultValue il valore di default
	 * @return la stringa, se valorizzata; il valore di default fornito altrimenti
	 */
	protected String impostaStringaDefault(String str, String defaultValue) {
		return StringUtils.isBlank(str) ? defaultValue : str;
	}
	
	/**
	 * Imposta la stringa con default pari a una string avuota
	 * @param str la stringa da valutare
	 * @return la stringa, se valorizzata; una stringa vuota altrimenti
	 * @see #impostaStringaDefault(String, String)
	 */
	protected String impostaStringaDefaultEmpty(String str) {
		return impostaStringaDefault(str, "");
	}
	
	/**
	 * Imposta la stringa con default pari a null
	 * @param str la stringa da valutare
	 * @return la stringa, se valorizzata; <code>null</code> altrimenti
	 * @see #impostaStringaDefault(String, String)
	 */
	protected String impostaStringaDefaultNull(String str) {
		return impostaStringaDefault(str, null);
	}

	/**
	 * Creazione della request per la lettura dei conti di tesoreria
	 * @return la request creata
	 */
	public LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = new LeggiContiTesoreria();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}

	/**
	 * @return the flagProvvisoriPagoPA
	 */
	public Boolean getFlagProvvisoriPagoPA() {
		return flagProvvisoriPagoPA;
	}

	/**
	 * @param flagProvvisoriPagoPA the flagProvvisoriPagoPA to set
	 */
	public void setFlagProvvisoriPagoPA(Boolean flagProvvisoriPagoPA) {
		this.flagProvvisoriPagoPA = flagProvvisoriPagoPA;
	}

	


}
